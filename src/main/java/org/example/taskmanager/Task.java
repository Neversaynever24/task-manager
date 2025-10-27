package org.example.taskmanager;

import lombok.Builder;
import lombok.Value;
import java.time.LocalDateTime;

@Value
@Builder
public class Task {
    Long taskId;
    Long creatorId;
    Long assignedUserId;
    TaskStatus status;
    LocalDateTime createDateTime;
    LocalDateTime deadlineDateTime;
    TaskPriority priority;
}