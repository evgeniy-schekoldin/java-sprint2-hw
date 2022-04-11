package model;

public class Subtask extends Task {
    private final long epicId;

    public Subtask(long id, String name, String details, Status status, long epicId) {
        super(id, name, details, status);
        this.epicId = epicId;
    }

    public long getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return (String.format("%s,%s,%s,%s,%s,%s", id, TaskType.SUBTASK, name, status, details,epicId));
    }

}