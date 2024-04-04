class TaskManager:
    def __init__(self):
        self.tasks = {}

    def add_task(self, task):
        self.tasks[task.task_id] = task

    def get_task(self, task_id):
        return self.tasks.get(task_id)
    
    def remove_task(self, task_id):
        self.tasks.pop(task_id)


task_manager = TaskManager()