import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.*;

import java.time.Duration;
import java.time.LocalDateTime;

import static java.time.LocalTime.now;

public class Main {
    public static void main(String[] args) {
        System.out.println("Пришло время практики!");
        TaskManager manager = Managers.getDefault();
        HistoryManager history = Managers.getDefaultHistory();


        Task task2 = new Task(0L, "NEW", "TASK", Status.NEW, Duration.ofMinutes(90), LocalDateTime.now().plusMinutes(100));
        //manager.updateTask(task2);


        Epic epic = new Epic(1L, "NEW_0L", "NEW", Status.NEW, null,null);
        //manager.updateEpic(epic);

        Epic epic1 = new Epic(2L, "NEW_2L", "EPIC 2L", Status.NEW, null,null);
        //manager.updateEpic(epic1);

        Subtask subtask = new Subtask(3L, "SUBTASK_NAME_3L", "DETAILS", Status.DONE,
                Duration.ofMinutes(20), LocalDateTime.now().plusMinutes(1), 1L);
        //manager.updateSubtask(subtask);
        Subtask subtask2 = new Subtask(4L, "SUBTASK_NAME_4L", "DETAILS", Status.DONE,
                Duration.ofMinutes(20), LocalDateTime.now().plusMinutes(10), 1L);
        //manager.updateSubtask(subtask2);

        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());
        System.out.println();

        System.out.println(manager.getSortedTasks());
        System.out.println(history.getHistory());

        System.out.println(history.getHistory());

    }
}