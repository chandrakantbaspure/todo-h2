package com.nitor.todoh2.service;


import com.nitor.todoh2.exception.TodoNotFoundException;
import com.nitor.todoh2.model.Todo;
import com.nitor.todoh2.repository.TodoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TodoServiceImpl implements TodoService {
    private final Logger log = LoggerFactory.getLogger(TodoServiceImpl.class);
    @Autowired
    private TodoRepository todoRepository;

    @Override
    public List<Todo> getAllTodos() {
        log.info("Get all todos");
        return todoRepository.findAll();
    }

    @Override
    public Todo addTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    @Override
    public Todo updateTodo(Long id, Todo updatedTodo) {
        Optional<Todo> todoToUpdate = findTodoById(id);
        if (todoToUpdate.isPresent()) {
            updatedTodo.setId(id);
            return todoRepository.save(updatedTodo);
        }
        return null;
    }

    @Override
    public String deleteTodo(Long id) {
        Optional<Todo> todoToDelete = findTodoById(id);
        if (todoToDelete.isPresent()) {
            todoRepository.deleteById(id);
            return "Todo deleted successfully";
        }
        return "Todo not found";
    }

    @Override
    public List<Todo> getTodoByUser(String user) {
        try {
            return todoRepository.findAll()
                    .stream()
                    .filter(todo -> todo.getUser().equalsIgnoreCase(user))
                    .collect(Collectors.toList());
        } catch (TodoNotFoundException e) {
            log.error("No todos found for user: {}", user);
            throw new TodoNotFoundException("No todos found for user: " + user);
        }
    }

    @Override
    public Optional<Todo> findTodoById(Long id) {
        return todoRepository.findById(id);
    }

    public List<Todo> getTodosBeforeTargetDate(LocalDate targetDate) {
        return todoRepository.findAllByTargetDateBefore(targetDate);
    }

}