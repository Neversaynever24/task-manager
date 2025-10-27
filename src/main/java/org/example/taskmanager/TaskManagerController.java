package org.example.taskmanager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
@RestController
@RequestMapping("/tasks")
public class TaskManagerController {
    private final TaskManagerService taskManagerService;

    @Autowired
    public TaskManagerController(TaskManagerService taskManagerService) {
        this.taskManagerService = taskManagerService;
    }

    @GetMapping()
    public ResponseEntity<List<Task>> getTasks() {
        log.info("called getTasks");
        return ResponseEntity.ok(taskManagerService.findAllTasks());
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTaskById(
            @PathVariable("taskId") Long taskId
    ) {
        log.info("called getTaskById with id = " + taskId);
        try {
            return ResponseEntity.ok(taskManagerService.getTaskById(taskId));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping()
    public ResponseEntity<Task> createTask(
            @RequestBody Task task
    ) {
        log.info("called createTask");
        var createdTask = taskManagerService.createTask(task);
        return ResponseEntity.status(201).body(createdTask);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(
            @PathVariable("taskId") Long taskId,
            @RequestBody Task taskToUpdate
    ) {
        log.info("called updateTask with id = " + taskId);
        try {
            return ResponseEntity.status(HttpStatus.OK).body(taskManagerService.updateTask(taskId, taskToUpdate));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }

    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long taskId
    ) {
        log.info("called deleteTask with id = " + taskId);
        try {
            taskManagerService.deleteTask(taskId);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }

    }
}
