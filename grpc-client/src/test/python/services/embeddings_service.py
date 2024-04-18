# Import the generated classes
from generated.embeddings_pb2 import TrainResponse, StatusQueryResponse
from generated.embeddings_pb2_grpc import ModelTrainingServiceServicer
from training.training_task import TrainingTask
from task_manager import task_manager

# Import your word embeddings training scripts here
# ...

class ModelTrainingService(ModelTrainingServiceServicer):

    def TrainModel(self, request, context):
        print(f"Received training request with : {request.name}")
        # Create an instance of the response message
        response_message = TrainResponse()
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

    def QueryTrainingStatus(self, request, context):
        print(f"Received status query for task: {request.taskId}")
        response = StatusQueryResponse()
        try:    
            task = task_manager.get_task(request.taskId)
        except Exception as e:
            print(e)
        # Construct the response (Set each field of the message)
        response.status = task.status  # e.g., 0 for success
        if response.status == 1:  
            response.name = task.name
            response.tag = task.tag
            response.algorithm = task.algorithm
            response.minCount = task.minCount
            response.vectorSize = task.vectorSize
            response.windowSize = task.windowSize
            response.epochs = task.epochs
            response.trainingTime = task.training_time
            response.vocabularySize = task.vocabulary_size
            response.modelFilePath = task.model_file_path
            loss_over_time = task.loss_over_time # list of floats
            response.lossOverTime.extend(loss_over_time)
            # Remove the task from the container
            task_manager.remove_task(request.taskId)
        return response
