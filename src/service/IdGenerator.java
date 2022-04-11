package service;

public class IdGenerator {
    private static Long id = 0L;

    public static Long generate() {
        return id++;
    }

}
