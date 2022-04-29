package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Epic extends Task {

    public Epic(String name, String details, Duration duration, LocalDateTime startTime) {
        super(name, details, null, duration, startTime);
    }

    public void setStatus(Status status) {
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