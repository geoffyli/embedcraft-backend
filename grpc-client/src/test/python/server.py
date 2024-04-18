from concurrent import futures
import grpc
import time

# Import the generated classes and the service implementation
from generated.embeddings_pb2_grpc import add_ModelTrainingServiceServicer_to_server
from services.embeddings_service import ModelTrainingService

def serve():
    # Set up the server
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    # Add the model training servicer to server
    add_ModelTrainingServiceServicer_to_server(ModelTrainingService(), server)
    # Start the server
    server.add_insecure_port('[::]:50051') # Listen on port 50051
    server.start()
    print("Server started on port 50051.")
    try:
        # Keep the server alive
        while True:
            time.sleep(86400)  # Server sleeps
    except KeyboardInterrupt:
        server.stop(0)

if __name__ == '__main__':
    serve()
