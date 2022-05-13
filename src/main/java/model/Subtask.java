package main.java.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private final long epicId;

    public Subtask(
            String name,
            String details,
            Status status,
            Duration duration,
            LocalDateTime startTime,
            long epicId
    ) {
        super(name, details, status, duration, startTime);
        this.epicId = epicId;
    }

    public long getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return (String.format("%s,%s,%s,%s,%s,%s,%s,%s", id, TaskType.SUBTASK, name, status, details, duration,
                startTime,epicId));
    }

}