package com.google.sample.gcpcicdtutorial.services;

import com.google.sample.gcpcicdtutorial.model.Status;
import com.google.sample.gcpcicdtutorial.model.Todo;

import java.util.Collection;
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
public interface TodoService {
    /**
     * Get a collection of todos
     *
     * @param status the status of the todos
     * @return a collection of todos
     */
    Collection<Todo> ofStatus(String status);

    /**
     * Get a collection of todos
     *
     * @param status the status of the todos
     * @return a collection of todos
     */
    Collection<Todo> ofStatus(Status status);

    /**
     * Get all Todos
     *
     * @return all todos
     */
    Collection<Todo> all();

    void add(Todo todo);

    Optional<Todo> find(String id);

    void update(String id, String title);

    void remove(String id);

    void removeCompleted();

    void toggleStatus(String id);

    void toggleAll(boolean complete);
}
