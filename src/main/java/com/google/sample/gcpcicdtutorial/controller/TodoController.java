package com.google.sample.gcpcicdtutorial.controller;

import com.google.sample.gcpcicdtutorial.model.Status;
import com.google.sample.gcpcicdtutorial.model.Todo;
import com.google.sample.gcpcicdtutorial.services.TodoService;
import org.apache.commons.lang3.tuple.Pair;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/*
 * Copyright 2018 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@Singleton
public class TodoController {
    private final TodoService todoService;

    @Inject
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    public Pair<String, Map<String, Object>> home(String statusStr, boolean icRequest) {
        Map<String, Object> model = new HashMap<>();
        model.put("todos", todoService.ofStatus(statusStr));
        model.put("filter", Optional.ofNullable(statusStr).orElse(""));
        model.put("activeCount", todoService.ofStatus(Status.ACTIVE.name()).size());
        Collection<Todo> completedTodos = todoService.ofStatus(Status.COMPLETE.name());
        model.put("anyCompleteTodos", completedTodos.size() > 0);
        model.put("allComplete", todoService.all().size() == completedTodos.size());
        model.put("status", Optional.ofNullable(statusStr).orElse(""));
        if (icRequest) {
            return Pair.of("velocity/todoList.vm", model);
        } else {
            return Pair.of("velocity/index.vm", model);
        }
    }


    public void addNewTodo(String title) {
        this.todoService.add(Todo.create(title));
    }

    public Pair<String, Map<String, Object>> edit(String id) {
        Map<String, Object> todos = new HashMap<>();
        todoService.find(id).ifPresent(todo -> todos.put("todo", todo));
        return Pair.of("velocity/editTodo.vm", todos);
    }

    public void toggleStatus(String id) {
        this.todoService.toggleStatus(id);
    }

    public void removeCompleted() {
        this.todoService.removeCompleted();
    }

    public void remove(String id) {
        this.todoService.remove(id);
    }

    public void update(String id, String title) {
        this.todoService.update(id, title);
    }

    public void toggleAllStatus() {
        this.todoService.toggleAll(true);
    }
}
