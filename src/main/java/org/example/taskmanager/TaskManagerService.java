package org.example.taskmanager;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskManagerService {

    private final TaskRepository taskRepository;

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

        var taskEntityToSave = TaskEntity.builder()
                .creatorId(taskToCreate.getCreatorId())
                .assignedUserId(taskToCreate.getAssignedUserId())
                .status(TaskStatus.CREATED)
                .createDateTime(taskToCreate.getCreateDateTime())
                .deadlineDateTime(taskToCreate.getDeadlineDateTime())
                .priority(taskToCreate.getPriority())
                .build();

        var savedTaskEntity = taskRepository.save(taskEntityToSave);
        return toDomainTask(savedTaskEntity);
    }

    public Task updateTask(Long taskId, Task taskToUpdate) {
        var taskEntity = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Not found task by id = " + taskId));

        if (taskEntity.getStatus() == (TaskStatus.DONE)) {
            throw new IllegalStateException("TaskStatus should not be DONE");
        }

        var createdTaskEntity = TaskEntity.builder()
                .taskId(taskEntity.getTaskId())
                .creatorId(taskToUpdate.getCreatorId())
                .assignedUserId(taskToUpdate.getAssignedUserId())
                .status(taskToUpdate.getStatus())
                .createDateTime(taskToUpdate.getCreateDateTime())
                .deadlineDateTime(taskToUpdate.getDeadlineDateTime())
                .priority(taskToUpdate.getPriority())
                .build();

        taskRepository.save(createdTaskEntity);
        return toDomainTask(createdTaskEntity);
    }

    public void deleteTask(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new EntityNotFoundException("Not found task by id = " + taskId);
        }

        taskRepository.deleteById(taskId);
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
