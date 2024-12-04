package com.nitor.todoh2.service;


import com.nitor.todoh2.model.Todo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TodoService {
    Optional<Todo> findTodoById(Long id);

    List<Todo> getAllTodos();

    Todo addTodo(Todo todo);

    Todo updateTodo(Long id, Todo updatedTodo);

    String deleteTodo(Long id);
    List<Todo> getTodoByUser(String user);

    List<Todo> getTodosBeforeTargetDate(LocalDate date);
}