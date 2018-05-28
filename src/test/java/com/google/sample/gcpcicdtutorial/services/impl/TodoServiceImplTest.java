package com.google.sample.gcpcicdtutorial.services.impl;

import com.google.sample.gcpcicdtutorial.UnitTest;
import com.google.sample.gcpcicdtutorial.model.Status;
import com.google.sample.gcpcicdtutorial.model.Todo;
import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.TestSubject;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

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
@RunWith(EasyMockRunner.class)
@Category(UnitTest.class)
public class TodoServiceImplTest extends EasyMockSupport {

    final Todo completedTodo = Todo.create("TODO-1");

    @TestSubject
    private TodoServiceImpl todoService = new TodoServiceImpl();

    @Test
    public void testFindByStatus() {
        assertThat(todoService.ofStatus(Status.COMPLETE).size(), equalTo(1));
        assertThat(todoService.ofStatus("COMPLETE").size(), equalTo(1));
        assertThat(todoService.ofStatus((String) null).size(), equalTo(2));
        assertThat(todoService.ofStatus("").size(), equalTo(2));
    }

    @Before
    public void setup() {
        completedTodo.toggleStatus();
        todoService.add(completedTodo);
        todoService.add(Todo.create("TODO-2"));
    }

    @Test
    public void testFindAll() {
        assertThat(todoService.all().size(), equalTo(2));
    }

    @Test
    public void testFindById() {
        assertThat(todoService.find("FAKE-ID").isPresent(), equalTo(false));
        assertThat(todoService.find(completedTodo.getId()).isPresent(), equalTo(true));
    }

    @Test
    public void testUpdate() {
        todoService.update(completedTodo.getId(), "UPDATED TITLE");
        assertThat(todoService.find(completedTodo.getId()).get().getTitle(), equalTo("UPDATED TITLE"));
    }

    @Test
    public void testRemove() {
        todoService.remove(completedTodo.getId());
        assertThat(todoService.find(completedTodo.getId()).isPresent(), equalTo(false));
    }

    @Test
    public void testRemoveCompleted() {
        todoService.removeCompleted();
        assertThat(todoService.find(completedTodo.getId()).isPresent(), equalTo(false));
    }

    @Test
    public void testToggleStatus() {
        todoService.toggleStatus(completedTodo.getId());
        assertThat(todoService.ofStatus(Status.COMPLETE).size(), equalTo(0));
    }

    @Test
    public void testToggleAll() {
        todoService.toggleAll(true);
        assertThat(todoService.ofStatus(Status.COMPLETE).size(), equalTo(2));
        todoService.toggleAll(false);
        assertThat(todoService.ofStatus(Status.COMPLETE).size(), equalTo(0));
    }

}