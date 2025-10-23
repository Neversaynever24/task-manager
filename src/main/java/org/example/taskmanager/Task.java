package org.example.taskmanager;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record Task (
    Long taskId,
    Long creatorId,
    Long assignedUserId,
    TaskStatus status,
    LocalDateTime createDateTime,
    LocalDateTime deadlineDateTime,
    TaskPriority priority
) {}
