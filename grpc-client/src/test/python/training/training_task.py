import threading
import time
import uuid
import urllib.request
import urllib.request
from util.google_cloud_storage_util import *



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
        self.google_client = initialize_client()

    def prepare_data(self):
        # Load text data from the dataset file
        try:
            self.training_text = read_file_from_gcs(self.google_client, self.blob_name)
        except Exception as e:
            self.status = -1
            print(f"Failed to load dataset from {self.datasetUrl}: {e}")

    def train_model(self):
        # Logic to train the model using the specified parameters
        try:
            # Training logic
            self.prepare_data()
            # self.train_model()
            # self.save_results()
            time.sleep(10)
            self.status = 1
        except Exception as e:
            self.status = -1

    def save_results(self):
        # Logic to save the trained model and any other results
        pass

    def execute_async(self):
        thread = threading.Thread(target=self.train_model)
        thread.start()
