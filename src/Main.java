import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.*;
import com.google.gson.Gson;

import java.io.IOException;
import java.time.Duration;

import static java.time.LocalTime.now;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Пришло время практики!");
        new KVServer().start();
        new HTTPTaskServer();
    }
}