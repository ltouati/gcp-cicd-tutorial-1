package com.google.sample.gcpcicdtutorial.controller;

import com.google.common.collect.ImmutableList;
import com.google.sample.gcpcicdtutorial.UnitTest;
import com.google.sample.gcpcicdtutorial.model.Todo;
import com.google.sample.gcpcicdtutorial.services.TodoService;
import org.apache.commons.lang3.tuple.Pair;
import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.hamcrest.CoreMatchers.equalTo;
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
public class TodoControllerTest extends EasyMockSupport {
    @Mock
    private TodoService todoService;
    @TestSubject
    private TodoController todoController = new TodoController(todoService);

    private List<Todo> todoList = ImmutableList.of(
            Todo.create("TOTO-1"),
            Todo.create("TOTO-2")
    );

    @Before
    public void setup() {
        todoController = new TodoController(todoService);
        todoList.get(0).toggleStatus();
    }

    @Test
    public void a_user_should_be_able_to_see_the_home_page() {
        expect(todoService.ofStatus((String) null)).andReturn(todoList);
        expect(todoService.ofStatus("ACTIVE")).andReturn(todoList);
        expect(todoService.ofStatus("COMPLETE")).andReturn(todoList);
        expect(todoService.all()).andReturn(todoList);
        replayAll();
        Pair<String, Map<String, Object>> value = todoController.home(null, false);
        verifyAll();
        assertThat(value.getLeft(), equalTo("velocity/index.vm"));
    }

    @Test
    public void a_user_should_be_able_to_see_the_fragment_containing_the_todo_list() {
        expect(todoService.ofStatus((String) null)).andReturn(todoList);
        expect(todoService.ofStatus("ACTIVE")).andReturn(todoList);
        expect(todoService.ofStatus("COMPLETE")).andReturn(todoList);
        expect(todoService.all()).andReturn(todoList);
        replayAll();
        Pair<String, Map<String, Object>> value = todoController.home(null, true);
        verifyAll();
        assertThat(value.getLeft(), equalTo("velocity/todoList.vm"));
    }

    @Test
    public void a_user_should_be_able_to_add_a_todo() {
        todoService.add(isA(Todo.class));
        replayAll();
        todoController.addNewTodo("TODO-TITLE");
        verifyAll();
    }

    @Test
    public void a_user_should_be_able_to_edit_a_todo() {
        Todo todo = Todo.create("TODO-1");
        expect(todoService.find("ID")).andReturn(Optional.of(todo));
        replayAll();
        Pair<String, Map<String, Object>> value = todoController.edit("ID");
        verifyAll();
        assertThat(value.getLeft(), equalTo("velocity/editTodo.vm"));
        assertThat(value.getRight().size(), equalTo(1));
        assertThat(value.getRight().get("todo"), equalTo(todo));
    }

    @Test
    public void a_user_should_be_able_to_toggle_a_todo() {
        todoService.toggleStatus("ID");
        replayAll();
        todoController.toggleStatus("ID");
        verifyAll();
    }

    @Test
    public void a_user_should_be_able_to_remove_all_completed_todos() {
        todoService.removeCompleted();
        replayAll();
        todoController.removeCompleted();
        verifyAll();

    }

    @Test
    public void a_user_should_be_able_to_remove_a_todo() {
        todoService.remove("ID");
        replayAll();
        todoController.remove("ID");
        verifyAll();
    }

    @Test
    public void a_user_should_be_able_to_toggle_all_todos() {
        todoService.toggleAll(true);
        replayAll();
        todoController.toggleAllStatus();
        verifyAll();
    }

    @Test
    public void a_user_should_be_able_to_update_a_todo() {
        todoService.update("ID", "NEW_TITLE");
        replayAll();
        todoController.update("ID", "NEW_TITLE");
        verifyAll();
    }

}