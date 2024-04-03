# Import the generated classes
from generated.embeddings_pb2 import TrainResponse
from generated.embeddings_pb2_grpc import ModelTrainingServiceServicer
from training.training_task import TrainingTask
from task_manager import task_manager

# Import your word embeddings training scripts here
# ...

class ModelTrainingService(ModelTrainingServiceServicer):

    def TrainModel(self, request, context):
        # name = request.name
        # tag = request.tag
        # algorithm = request.algorithm
        # minCount = request.minCount
        # vectorSize = request.vectorSize
        # windowSize = request.windowSize
        # epochs = request.epochs
        # datasetUrl = request.datasetUrl

        print(f"Received training request with : {request.name}")
        response_message = None
        try:    
            # Create a training task
            train_task = TrainingTask(request.name, request.tag, request.algorithm, request.blobName, request.minCount, request.vectorSize, request.windowSize, request.epochs)
            # Add the task to the task manager
            task_manager.add_task(train_task)
            # Start training
            train_task.execute_async()
            # ...
        except Exception as e:
            print(e)
            response_message = "error"
        if (train_task.status == -1):
            response_message = "failed"
        else:
            response_message = "success"
        task_id = train_task.task_id

        return TrainResponse(message=response_message, taskId=task_id)
