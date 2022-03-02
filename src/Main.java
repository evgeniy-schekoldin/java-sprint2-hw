public class Main {
    public static void main(String[] args) {
        System.out.println("Пришло время практики!");
        TaskManager manager = Managers.getDefault();
        HistoryManager history = Managers.getDefaultHistory();

        Task task = new Task(manager.getNewId(), "test Task", "This is new test Task",
                Status.NEW);
        manager.updateTask(task);
        Task task2 = new Task(manager.getNewId(), "test Task - 2", "This is another new test Task",
                Status.NEW);
        manager.updateTask(task2);
        Epic epic = new Epic(manager.getNewId(), "test Epic", "This is  new test Epic",
                Status.NEW);
        manager.updateEpic(epic);
        Subtask subtask = new Subtask(manager.getNewId(), "test Sub", "This is new test Subtask",
                Status.NEW, epic.getId());
        manager.updateSubtask(subtask);
        Subtask subtask2 = new Subtask(manager.getNewId(), "test Sub - 2", "This is new test Subtask - 2",
                Status.NEW, epic.getId());
        manager.updateSubtask(subtask2);
        Subtask subtask3 = new Subtask(subtask2.getId(), "test Sub - 2", "This is new test Subtask - 2",
                Status.DONE, epic.getId());
        manager.updateSubtask(subtask3);
        Epic epic2 = new Epic(manager.getNewId(), "test Epic - 2", "This is  new test Epic - 2",
                Status.NEW);
        manager.updateEpic(epic2);
        Subtask subtask4 = new Subtask(manager.getNewId(), "test Sub", "This is new test Subtask",
                Status.NEW, epic2.getId());
        manager.updateSubtask(subtask4);

        manager.getTask(task.getId());
        manager.getTask(task2.getId());
        manager.getEpic(epic.getId());
        manager.getSubtask(subtask.getId());
        manager.getSubtask(subtask2.getId());
        manager.getTask(task.getId());
        manager.getTask(task2.getId());
        manager.getEpic(epic.getId());
        manager.getSubtask(subtask.getId());
        manager.getSubtask(subtask2.getId());
        manager.getTask(task.getId());
        manager.getTask(task2.getId());
        manager.getEpic(epic.getId());

        for (Task item : history.getHistory()) {
            System.out.println("\"" + item.getName() + "\" - " + item.getStatus());
        }




    }
}