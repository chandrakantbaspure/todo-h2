package com.nitor.todoh2.controller;

import com.nitor.todoh2.exception.TodoIdMismatchException;
import com.nitor.todoh2.exception.TodoNotFoundException;
import com.nitor.todoh2.model.Todo;
import com.nitor.todoh2.service.TodoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin
public class TodoController {

    private static final Logger LOG = LoggerFactory.getLogger(TodoController.class);
    private static final String TODO_NOT_FOUND = "Todo with id is not found: ";
    private static final String FAILED_OPERATION = "Failed to {} todo";
    private static final String TODO_MISMATCH = "Todo id is mismatch with id: ";

    @Autowired
    private TodoService todoService;

    @PostMapping("/create")
    public ResponseEntity<Todo> createTodo(@Valid @RequestBody Todo todo) {
        LOG.info("Create new todo");
        return new ResponseEntity<>(todoService.addTodo(todo), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @Valid @RequestBody Todo todo) throws TodoNotFoundException {
        LOG.info("Update todo with id: {}", id);
        if (!todo.getId().toString().equals(id.toString())) {
            throw new TodoIdMismatchException(TODO_MISMATCH + id);
        } else if (todoService.findTodoById(id).isEmpty() || !todoService.findTodoById(id).get().getId().equals(id)) {
            throw new TodoNotFoundException(TODO_NOT_FOUND + id);
        }
        try {
            Todo updatedTodo = todoService.updateTodo(id, todo);
            return ResponseEntity.ok(updatedTodo);
        } catch (Exception e) {
            LOG.error(FAILED_OPERATION, "update", e);
            throw new RuntimeException("Failed to update todo.");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTodo(@PathVariable Long id) throws TodoNotFoundException {
        LOG.info("Delete todo with id: {}", id);

        try {
            Optional<Todo> todo = todoService.findTodoById(id);

            if (todo.isEmpty() || !id.equals(todo.get().getId())) {
                throw new TodoNotFoundException("Todo with id is not found: " + id);
            }

            todoService.deleteTodo(id);
            return ResponseEntity.ok("Todo deleted successfully");
        } catch (TodoNotFoundException e) {
            LOG.error(FAILED_OPERATION, "delete", e);
            throw e;
        } catch (Exception e) {
            LOG.error(FAILED_OPERATION, "delete", e);
            throw new RuntimeException("Failed to delete todo.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Todo>> getTodoById(@PathVariable Long id) throws TodoNotFoundException {
        LOG.info("Get todo by id: {}", id);
        Optional<Todo> todoOptional = todoService.findTodoById(id);
        if (todoOptional.isEmpty()) {
            throw new TodoNotFoundException(TODO_NOT_FOUND + id);
        }
        return ResponseEntity.ok(todoOptional);
    }

    @GetMapping("/alltodos")
    public ResponseEntity<List<Todo>> getAllTodos() {
        LOG.info("Get all todos");
        try {
            List<Todo> todos = todoService.getAllTodos();
            if (todos.isEmpty()) {
                throw new TodoNotFoundException("No todos found");
            }
            return ResponseEntity.ok(todos);
        } catch (Exception e) {
            LOG.error(FAILED_OPERATION, "get all", e);
            throw new RuntimeException("Failed to get all todos.");
        }
    }

    @GetMapping(path = "/byname/{name}")
    public ResponseEntity<List<Todo>> getTodoByUser(@PathVariable String name) {
        LOG.info("Get all todos by user: {}", name);
        try {
            List<Todo> todos = todoService.getTodoByUser(name);
            if (todos.isEmpty()) {
                throw new TodoNotFoundException("No todos found for user: " + name);
            }
            return ResponseEntity.ok(todos);
        } catch (TodoNotFoundException e) {
            LOG.error(TODO_NOT_FOUND);
            throw e;
        }
    }

    @GetMapping("/target/{date}")
    public ResponseEntity<List<Todo>> getTodosByTargetDate(@PathVariable LocalDate date) {
        return ResponseEntity.ok(todoService.getTodosBeforeTargetDate(date));
    }
}
