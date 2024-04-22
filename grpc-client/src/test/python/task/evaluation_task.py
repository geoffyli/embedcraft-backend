from gensim.models import Word2Vec
from util.google_cloud_storage_util import *
import numpy as np
from sklearn.manifold import TSNE
from pinecone import Pinecone, ServerlessSpec


class EvaluationTask:
    def __init__(self, model_file_path):
        self.model_file_path = model_file_path
        self.model = None
        self.google_client = initialize_client()

    def loadModel(self):
        # Get the file from the google cloud storage
        local_model_path = "/tmp/model.model"  # Temp path to store the model locally
        download_blob(self.google_client, self.model_file_path, local_model_path)
        # Load the model file with Gensim
        self.model = Word2Vec.load(local_model_path)

        # # Load model into Pinecone
        # pc = Pinecone(api_key="4eb061b4-27cf-4e21-9d19-a3ed37b450f3")
        # index_name = "word-vectors"
        # if index_name not in pc.list_indexes().names():
        #     pc.create_index(
        #         name=index_name,
        #         dimension=self.model.vector_size,
        #         spec=ServerlessSpec(cloud="aws", region="us-east-1"),
        #     )
        # index = pc.Index(index_name)
        # # Insert vectors into Pinecone index
        # batch_size = (
        #     1000  # Batch size can be tuned based on your preference or Pinecone limits
        # )
        # batches = [
        #     list(self.model.wv.key_to_index.keys())[i : i + batch_size]
        #     for i in range(0, len(self.model.wv.key_to_index), batch_size)
        # ]
        # for batch in batches:
        #     vectors = [
        #         (word, self.model.wv[word].tolist())
        #         for word in batch
        #         if word in self.model.wv
        #     ]
        #     ids = [word for word, _ in vectors]
        #     vectors = [vector for _, vector in vectors]
        #     index.upsert(vectors=zip(ids, vectors))
        # print("Vectors successfully inserted into Pinecone.")

    def similarity_search(self, word):
        res_list = []
        # Search the most similar words
        most_similar_list = self.model.wv.most_similar(word, topn=50)
        words_to_visualize = [_[0] for _ in most_similar_list]
        # Get the vectors for these words
        subset_vectors = np.array(
            [
                self.model.wv[word]
                for word in words_to_visualize
                if word in self.model.wv
            ]
        )
        perplexity_value = len(subset_vectors) - 1 if len(subset_vectors) > 1 else 1
        # ZUse t-SNE to reduce dimensionality
        tsne = TSNE(n_components=2, random_state=0, perplexity=perplexity_value)
        vectors_2d_subset = tsne.fit_transform(subset_vectors)
        # Compose the result list
        for _ in range(len(words_to_visualize)):
            res_list.append(
                [
                    most_similar_list[_][0],
                    most_similar_list[_][1],
                    [vectors_2d_subset[_][0], vectors_2d_subset[_][1]],
                ]
            )
        return res_list
