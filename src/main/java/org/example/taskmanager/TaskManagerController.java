package org.example.taskmanager;

import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/tasks")
public class TaskManagerController {
    private final TaskManagerService taskManagerService;
    private static final Logger log = LoggerFactory.getLogger(TaskManagerController.class);

    @Autowired
    public TaskManagerController(TaskManagerService taskManagerService) {
        this.taskManagerService = taskManagerService;
    }

    @GetMapping()
    public List<Task> getTasks() {
        log.info("called getTasks");
        return taskManagerService.findAllTasks();
    }

    @GetMapping("/{taskId}")
    public Task getTaskById(
            @PathVariable("taskId") Long taskId
    ) {
        log.info("called getTaskById with id = " + taskId);
        return taskManagerService.getTaskById(taskId);
    }
}
