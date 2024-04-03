import grpc

# Import the generated classes
from services.embeddings_pb2 import TrainRequest
from services.embeddings_pb2_grpc import EmbeddingsServiceStub

def run():
    # Assuming the server is running on localhost:50051
    channel = grpc.insecure_channel('localhost:50051')
    stub = EmbeddingsServiceStub(channel)

    # Create a TrainRequest message with your data
    response = stub.TrainEmbeddings(TrainRequest(data="Sample data for training"))

    print("Embeddings service responded with message: " + response.message)

if __name__ == '__main__':
    run()
