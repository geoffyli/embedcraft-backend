class TaskManager:
    def __init__(self):
        self.tasks = {}

    def add_task(self, task):
        self.tasks[task.task_id] = task

    def get_task_status(self, task_id):
        task = self.tasks.get(task_id)
        return -1 if task is None else task.status

    # def start_training(self, task_id):
    #     task = self.tasks.get(task_id)
    #     if task:
    #         task.train_model_async()
    #     else:
    #         print("Task not found")


task_manager = TaskManager()