package main.java.service;

public class IdGenerator {
    private static long id = 0L;

    public static long generate() {
        return id++;
    }

    public static void setNextId(long newId) {
        id = newId;
    }

}
