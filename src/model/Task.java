package model;

public class Task {

    protected final Long id;
    protected String name;
    protected String details;
    protected Status status;

    public Task(long id, String name, String details, Status status) {
        this.id = id;
        this.name = name;
        this.details = details;
        this.status = status;
    }

    public long getId() { return id; }

    public String getName() { return name; }

    public String getDetails() { return details; }

    public Status getStatus() { return status; }

    @Override
    public String toString() {
        return (String.format("%s,%s,%s,%s,%s,", id, TaskType.TASK, name, status, details));
    }

}