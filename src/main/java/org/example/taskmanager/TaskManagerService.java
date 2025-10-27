package org.example.taskmanager;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service

public class TaskManagerService {

    private final Map<Long, Task> tasksMap;
    private final AtomicLong idCounter;

    private final TaskRepository taskRepository;

    public TaskManagerService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        tasksMap = new HashMap<>();
        idCounter = new AtomicLong();
    }

    public List<Task> findAllTasks() {
        List<TaskEntity> allTaskEntities = taskRepository.findAll();

        List<Task> tasksList = allTaskEntities.stream()
                .map(it -> toDomainTask(it))
                .toList();

        return tasksList;
    }

    public Task getTaskById(Long taskId) {
        TaskEntity taskEntity = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Not found task by id = " + taskId
                ));

        return toDomainTask(taskEntity);
    }

    public Task createTask(Task taskToCreate) {
        if (taskToCreate.getTaskId() != null) {
            throw new NoSuchElementException("TaskId must be empty, taskId: " + taskToCreate.getTaskId());
        }

        if (taskToCreate.getStatus() != null) {
            throw new NoSuchElementException("TaskStatus must be empty, taskStatus" + taskToCreate.getStatus());
        }

        var createdTask = Task.builder()
                .taskId(idCounter.incrementAndGet())
                .creatorId(taskToCreate.getCreatorId())
                .assignedUserId(taskToCreate.getAssignedUserId())
                .status(TaskStatus.CREATED)
                .createDateTime(taskToCreate.getCreateDateTime())
                .deadlineDateTime(taskToCreate.getDeadlineDateTime())
                .priority(taskToCreate.getPriority())
                .build();

        tasksMap.put(createdTask.getTaskId(), createdTask);
        return createdTask;
    }

    public Task updateTask(Long taskId, Task taskToUpdate) {
        if (!tasksMap.containsKey(taskId)) {
            throw new NoSuchElementException("Not found task by id = " + taskId);
        }

        if (taskToUpdate.getStatus().equals(TaskStatus.DONE)) {
            throw new IllegalStateException("TaskStatus should not be DONE");
        }

        var oldTask = tasksMap.get(taskId);
        var createdTask = Task.builder()
                .taskId(oldTask.getTaskId())
                .creatorId(taskToUpdate.getCreatorId())
                .assignedUserId(taskToUpdate.getAssignedUserId())
                .status(taskToUpdate.getStatus())
                .createDateTime(taskToUpdate.getCreateDateTime())
                .deadlineDateTime(taskToUpdate.getDeadlineDateTime())
                .priority(taskToUpdate.getPriority())
                .build();

        tasksMap.put(taskId, createdTask);
        return createdTask;
    }

    public void deleteTask(Long taskId) {
        if (!tasksMap.containsKey(taskId)) {
            throw new NoSuchElementException("Not found task by id = " + taskId);
        }

        tasksMap.remove(taskId);
    }

    private Task toDomainTask(TaskEntity taskEntity) {
        return Task.builder()
                .taskId(taskEntity.getTaskId())
                .creatorId(taskEntity.getCreatorId())
                .assignedUserId(taskEntity.getAssignedUserId())
                .status(taskEntity.getStatus())
                .createDateTime(taskEntity.getCreateDateTime())
                .deadlineDateTime(taskEntity.getDeadlineDateTime())
                .priority(taskEntity.getPriority())
                .build();
    }
}
