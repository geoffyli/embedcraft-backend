import threading
import time
import uuid
import tempfile
import nltk
from nltk.tokenize import sent_tokenize
from nltk.corpus import stopwords

from gensim.models import Word2Vec
from gensim.models.callbacks import CallbackAny2Vec
from gensim.utils import simple_preprocess
from util.google_cloud_storage_util import *

nltk.download('punkt')
nltk.download('stopwords')
stop_words = set(stopwords.words('english'))



class TrainingTask:
    def __init__(
        self, name, tag, algorithm, blobName, minCount, vectorSize, windowSize, epochs
    ):
        self.name = name
        self.tag = tag
        self.algorithm = algorithm
        self.blob_name = blobName
        self.minCount = minCount
        self.vectorSize = vectorSize
        self.windowSize = windowSize
        self.epochs = epochs
        # Additional initialization
        self.status = 0  # 0: not finished, 1: finished, -1: failed
        self.task_id = str(uuid.uuid4())  # Generate  a unique task ID
        self.training_text = ""
        self.preprocessed_data = None
        self.google_client = initialize_client()
        self.model = None
        self.model_file_path = None
        self.training_time = None
        self.loss_over_time = []
        self.vocabulary_size = 0

    def prepare_data(self):
        # Load text data from the dataset file
        try:
            self.training_text = read_file_from_gcs(self.google_client, self.blob_name)
            # Preprocess the data
            sentences = sent_tokenize(self.training_text)
            # Process each sentence
            processed_sentences = []
            for sentence in sentences:
                # Tokenize words and preprocess
                words = simple_preprocess(sentence, deacc=True)  # Remove punctuation and tokenize
                # Remove stopwords and words with less than 3 characters
                words = [word for word in words if word not in stop_words and len(word) > 1]
                processed_sentences.append(words)
            self.preprocessed_data = processed_sentences

        except Exception as e:
            self.status = -1
            print(f"Failed to load dataset from {self.datasetUrl}: {e}")

    def train_model(self):
        # Record the start time
        start_time = time.time()
        # Logic to train the model using the specified parameters
        try:
            # Training logic
            self.prepare_data()
            # Setup model
            self.model = Word2Vec(
                window=self.windowSize,
                min_count=self.minCount,
                workers=8,
                compute_loss=True
            )
            # Build vocabulary
            self.model.build_vocab(self.preprocessed_data, progress_per=1000)
            # Set callback
            loss_callback = Callback()
            # Train model
            self.model.train(self.preprocessed_data, total_examples=self.model.corpus_count, epochs=self.epochs, compute_loss=True, callbacks=[loss_callback])
            # Save training results
            self.save_results()
            self.status = 1
            # Record the end time
            ent_time = time.time()
            self.training_time = int(ent_time - start_time)
            self.loss_over_time = loss_callback.loss_over_time
            self.vocabulary_size = len(self.model.wv.key_to_index)
        except Exception as e:
            self.status = -1

    def save_results(self):
        model_name = "models/" + self.task_id + ".model"
        # Logic to save the trained model and any other results
        with tempfile.NamedTemporaryFile(prefix='model-', suffix='.model', delete=False) as tmp:
        # Save model to the temporary file
            self.model.save(tmp.name)
            # Read the model back as bytes
            tmp.seek(0)
            model_bytes = tmp.read()
            upload_model_to_gcs(self.google_client, model_name, model_bytes)
        
        # Close and delete the temporary file
        tmp.close()
        self.model_file_path = model_name

    def execute_async(self):
        thread = threading.Thread(target=self.train_model)
        thread.start()

class Callback(CallbackAny2Vec):
    '''Callback to print loss after each epoch.'''

    def __init__(self):
        self.epoch = 0
        self.loss_to_be_subed = 0
        self.loss_over_time = []

    def on_epoch_end(self, model):
        loss = model.get_latest_training_loss()
        loss_now = loss - self.loss_to_be_subed
        self.loss_to_be_subed = loss
        self.loss_over_time.append(loss_now)
        print('Loss after epoch {}: {}'.format(self.epoch, loss_now))
        self.epoch += 1