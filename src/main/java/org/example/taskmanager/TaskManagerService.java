package org.example.taskmanager;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskManagerService {

    public List<Task> findAllTasks() {
        return List.of(new Task(123L),
                new Task(124L)
        );
    }
}
