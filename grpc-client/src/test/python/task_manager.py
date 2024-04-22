class TaskManager:
    def __init__(self):
        self.training_tasks = {}
        self.evaluation_tasks = {}

    def add_training_task(self, task):
        self.training_tasks[task.task_id] = task

    def get_training_task(self, task_id):
        return self.training_tasks.get(task_id)
    
    def remove_training_task(self, task_id):
        self.training_tasks.pop(task_id)

    def add_evaluation_task(self, task):
        self.evaluation_tasks[task.model_file_path] = task

    def get_evaluation_task(self, task_id):
        return self.evaluation_tasks.get(task_id)
    
    def remove_evaluation_task(self, task_id):
        self.evaluation_tasks.pop(task_id)


task_manager = TaskManager()