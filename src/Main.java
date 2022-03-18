import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Пришло время практики!");
        TaskManager manager = Managers.getDefault();
        HistoryManager history = Managers.getDefaultHistory();

        Epic epic1 = new Epic(IdGenerator.generate(), "Epic-1", "With 3 subtasks",
                Status.NEW);
        manager.updateEpic(epic1);
        Subtask subtask1 = new Subtask(IdGenerator.generate(), "Subtask-1", "For Epic-1",
                Status.NEW, epic1.getId());
        manager.updateSubtask(subtask1);
        Subtask subtask2 = new Subtask(IdGenerator.generate(), "Subtask-2", "For Epic-1",
                Status.NEW, epic1.getId());
        manager.updateSubtask(subtask2);
        Subtask subtask3 = new Subtask(IdGenerator.generate(), "Subtask-3", "For Epic-1",
                Status.NEW, epic1.getId());
        manager.updateSubtask(subtask3);
        Epic epic2 = new Epic(IdGenerator.generate(), "Epic-2", "With 0 subtasks",
                Status.NEW);
        manager.updateEpic(epic2);

        manager.getSubtask(subtask1.getId());
        manager.getSubtask(subtask2.getId());
        manager.getSubtask(subtask3.getId());
        manager.getEpic(epic1.getId());
        manager.getEpic(epic2.getId());
        printHistory(history);
        manager.getEpic(epic1.getId());
        printHistory(history);
        manager.deleteEpic(epic1.getId());
        printHistory(history);
    }

    private static void printHistory(HistoryManager history) {
        for (Task task : history.getHistory()) {
            System.out.println("\"" + task.getName() + "\" - " + task.getStatus());
        }
        System.out.println("");
    }
}