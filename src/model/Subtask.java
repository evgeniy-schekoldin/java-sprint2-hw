package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Objects;

public class Subtask extends Task {
    private final long epicId;

    public Subtask(
            long id,
            String name,
            String details,
            Status status,
            Duration duration,
            LocalDateTime startTime,
            long epicId
    ) {
        super(id, name, details, status, duration, startTime);
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