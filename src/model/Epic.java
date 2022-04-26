package model;

import service.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeSet;

public class Epic extends Task {

    public Epic(long id, String name, String details, Status status, Duration duration, LocalDateTime startTime) {
        super(id, name, details, status, duration, startTime);
    }

    public void setTaskStatus(Status status) {
        this.status = status;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return (String.format("%s,%s,%s,%s,%s,%s,%s", id, TaskType.EPIC, name, status, details, duration, startTime));
    }

}