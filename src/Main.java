import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.TreeSet;

import static java.time.LocalTime.now;

public class Main {
    public static void main(String[] args) {
        System.out.println("Пришло время практики!");

        TaskManager manager = Managers.getDefault();
        Epic epic = new Epic("NEW_0L", "NEW", null,null);
        //manager.addEpic(epic);
        Subtask subtask = new Subtask("Subtask", "Subtask for Epic 0", Status.DONE, Duration.ofMinutes(0),LocalDateTime.now(),0);
        //manager.addSubtask(subtask);
        manager.deleteSubtask(1L);
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());
        System.out.println(manager.getSortedTasks());

    }
}