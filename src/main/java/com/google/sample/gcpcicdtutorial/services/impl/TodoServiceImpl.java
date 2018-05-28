package com.google.sample.gcpcicdtutorial.services.impl;

import com.google.sample.gcpcicdtutorial.model.Status;
import com.google.sample.gcpcicdtutorial.model.Todo;
import com.google.sample.gcpcicdtutorial.services.TodoService;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
public class TodoServiceImpl implements TodoService {
    private List<Todo> todos = new ArrayList<>();

    @Override
    public Collection<Todo> ofStatus(String statusString) {
        return (statusString == null || statusString.isEmpty()) ? todos : ofStatus(Status.valueOf(statusString.toUpperCase()));
    }

    @Override
    public Collection<Todo> ofStatus(Status status) {
        return todos.stream().filter(todo -> todo.getStatus() == status).collect(Collectors.toList());
    }

    @Override
    public Collection<Todo> all() {
        return todos;
    }

    @Override
    public void add(Todo todo) {
        this.todos.add(todo);
    }

    @Override
    public Optional<Todo> find(String id) {
        return this.todos.stream().filter(t -> t.getId().equals(id)).findFirst();
    }

    @Override
    public void update(String id, String title) {
        find(id).ifPresent(t -> t.setTitle(title));
    }


    @Override
    public void remove(String id) {
        find(id).ifPresent(t -> todos.remove(t));
    }

    @Override
    public void removeCompleted() {
        ofStatus(Status.COMPLETE).forEach(t -> this.remove(t.getId()));
    }

    @Override
    public void toggleStatus(String id) {
        find(id).ifPresent(Todo::toggleStatus);
    }

    @Override
    public void toggleAll(boolean complete) {
        all().forEach(t -> t.setStatus(complete ? Status.COMPLETE : Status.ACTIVE));
    }

}
