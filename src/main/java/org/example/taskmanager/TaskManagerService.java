package org.example.taskmanager;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

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
        if (taskToCreate.taskId() != null) {
            throw new NoSuchElementException("TaskId must be empty, taskId: " + taskToCreate.taskId());
        }

        if (taskToCreate.status() != null) {
            throw new NoSuchElementException("TaskStatus must be empty, taskStatus" + taskToCreate.status());
        }

        var createdTask = new Task(
                idCounter.incrementAndGet(),
                taskToCreate.creatorId(),
                taskToCreate.assignedUserId(),
                TaskStatus.CREATED,
                taskToCreate.createDateTime(),
                taskToCreate.deadlineDateTime(),
                taskToCreate.priority()
        );

        tasksMap.put(createdTask.taskId(), createdTask);
        return createdTask;
    }

    public Task updateTask(Long taskId, Task taskToUpdate) {
        if (!tasksMap.containsKey(taskId)) {
            throw new NoSuchElementException("Not found task by id = " + taskId);
        }

        if (taskToUpdate.status().equals(TaskStatus.DONE)) {
            throw new IllegalStateException("TaskStatus should not be DONE");
        }

        var oldTask = tasksMap.get(taskId);
        var updatedTask = new Task(
                oldTask.taskId(),
                taskToUpdate.creatorId(),
                taskToUpdate.assignedUserId(),
                taskToUpdate.status(),
                taskToUpdate.createDateTime(),
                taskToUpdate.deadlineDateTime(),
                taskToUpdate.priority()
        );
        tasksMap.put(taskId, updatedTask);
        return updatedTask;
    }

    public void deleteTask(Long taskId) {
        if (!tasksMap.containsKey(taskId)) {
            throw new NoSuchElementException("Not found task by id = " + taskId);
        }

        tasksMap.remove(taskId);
    }
}
