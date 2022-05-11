package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {

    protected long id;
    protected String name;
    protected String details;
    protected Status status;
    protected Duration duration;
    protected LocalDateTime startTime;

    public Task(String name, String details, Status status, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.details = details;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    public long getId() { return id; }

    public void setId(long id) {this.id = id; }

    public String getName() { return name; }

    public String getDetails() { return details; }

    public Status getStatus() { return status; }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null) {
            return startTime.plus(duration);
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return (String.format("%s,%s,%s,%s,%s,%s,%s", id, TaskType.TASK, name, status, details, duration, startTime));
    }

}