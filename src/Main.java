import main.java.service.HTTPTaskServer;
import main.java.service.KVServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Пришло время практики!");
        new KVServer().start();
        new HTTPTaskServer();
    }
}