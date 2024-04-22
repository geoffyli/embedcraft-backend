# Import the generated classes
from generated.embeddings_pb2 import (
    TrainResponse,
    StatusQueryResponse,
    NotifyModelLoadingResponse,
    QuerySimilarWordListResponse
)
from generated.embeddings_pb2_grpc import (
    ModelTrainingServiceServicer,
    ModelEvaluationServiceServicer,
)
from task.training_task import TrainingTask
from task.evaluation_task import EvaluationTask
from util.google_cloud_storage_util import *
from task_manager import task_manager
from gensim.models import Word2Vec


class ModelTrainingService(ModelTrainingServiceServicer):
    def TrainModel(self, request, context):
        print(f"Received training request with : {request.name}")
        # Create an instance of the response message
        response_message = TrainResponse()
        try:
            # Create a training task
            train_task = TrainingTask(
                request.name,
                request.tag,
                request.algorithm,
                request.blobName,
                request.minCount,
                request.vectorSize,
                request.windowSize,
                request.epochs,
            )
            # Add the task to the task manager
            task_manager.add_training_task(train_task)
            # Start training
            train_task.execute_async()
            # ...
        except Exception as e:
            print(e)
            response_message = "error"
        if train_task.status == -1:
            response_message = "failed"
        else:
            response_message = "success"
        task_id = train_task.task_id

        return TrainResponse(message=response_message, taskId=task_id)

    def QueryTrainingStatus(self, request, context):
        print(f"Received status query for task: {request.taskId}")
        response = StatusQueryResponse()
        try:
            task = task_manager.get_training_task(request.taskId)
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
            loss_over_time = task.loss_over_time  # list of floats
            response.lossOverTime.extend(loss_over_time)
            # Remove the task from the container
            task_manager.remove_training_task(request.taskId)
        return response


class ModelEvaluationService(ModelEvaluationServiceServicer):
    def NotifyModelLoading(self, request, context):

        print(f"Received model loading request for : {request.modelFilePath}")
        # Create an instance of the response message
        response_message = NotifyModelLoadingResponse()

        try:
            evaluation_task = EvaluationTask(request.modelFilePath)
            # Add the task to the task manager
            task_manager.add_evaluation_task(evaluation_task)
            # Load the model
            evaluation_task.loadModel()
            # TODO

        except Exception as e:
            print(e)
            response_message = "error"
        response_message = "success"
        return response_message
    
    def QuerySimilarWordList(self, request, context):
        print(f"Received similar word list request for : {request.modelFilePath}")
        # Create an instance of the response message
        response_message = QuerySimilarWordListResponse()
        try:
            # Get the evaluation task
            evaluation_task = task_manager.get_evaluation_task(request.modelFilePath)
            # Query the similar word list
            res_list = evaluation_task.similarity_search(request.word)
            # Compose the response object
            for _ in res_list:
                response_message.words[_[0]].similarity = _[1]
                response_message.words[_[0]].coordinates.extend(_[2])

        except Exception as e:
            print(e)
            response_message = "error"
        return response_message
