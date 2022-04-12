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

        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());

        System.out.println(history.getHistory());
    }
}