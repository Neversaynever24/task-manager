package org.example.taskmanager;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "tasks")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "creator_id")
    private Long creatorId;

    @Column(name = "assigned_user_id")
    private Long assignedUserId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TaskStatus status;

    @Column(name = "create_date_time")
    private LocalDateTime createDateTime;

    @Column(name = "deadline_date_time")
    private LocalDateTime deadlineDateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private TaskPriority priority;
}
