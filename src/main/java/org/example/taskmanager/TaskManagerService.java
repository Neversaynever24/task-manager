package org.example.taskmanager;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class TaskManagerService {

    private final Map<Long, Task> tasksMap = Map.of(
            1L, new Task(
                    1L,
                    232L,
                    32L,
                    TaskStatus.CREATED,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusDays(5),
                    TaskPriority.Low
            ),
            2L, new Task(
                    2L,
                    21232L,
                    32L,
                    TaskStatus.CREATED,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusDays(5),
                    TaskPriority.Low
            ),
            3L, new Task(
                    3L,
                    232L,
                    32L,
                    TaskStatus.CREATED,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusDays(5),
                    TaskPriority.Low
            )
    );

    public List<Task> findAllTasks() {
        return tasksMap.values().stream().toList();
    }

    public Task getTaskById(Long taskId) {
        if (!tasksMap.containsKey(taskId)) {
            throw new NoSuchElementException("Not found task by id = " + taskId);
        }
        return tasksMap.get(taskId);
    }
}
