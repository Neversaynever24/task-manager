package org.example.taskmanager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
public class TaskManagerService {

    private final Map<Long, Task> tasksMap;
    private final AtomicLong idCounter;

    public TaskManagerService() {
        tasksMap = new HashMap<>();
        idCounter = new AtomicLong();
    }

    public List<Task> findAllTasks() {
        return tasksMap.values().stream().toList();
    }

    public Task getTaskById(Long taskId) {
        if (!tasksMap.containsKey(taskId)) {
            throw new NoSuchElementException("Not found task by id = " + taskId);
        }
        return tasksMap.get(taskId);
    }

    public Task createTask(Task taskToCreate) {
        if (taskToCreate.getTaskId() != null) {
            throw new NoSuchElementException("TaskId must be empty, taskId: " + taskToCreate.getTaskId());
        }

        if (taskToCreate.getStatus() != null) {
            throw new NoSuchElementException("TaskStatus must be empty, taskStatus" + taskToCreate.getStatus());
        }

        var createdTask = Task.builder()
                .taskId(idCounter.getAndIncrement())
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
}
