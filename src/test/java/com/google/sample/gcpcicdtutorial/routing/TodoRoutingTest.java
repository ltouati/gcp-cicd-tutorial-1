package com.google.sample.gcpcicdtutorial.routing;

import com.google.inject.Injector;
import com.google.sample.gcpcicdtutorial.UnitTest;
import com.google.sample.gcpcicdtutorial.controller.TodoController;
import io.javalin.Context;
import io.javalin.Javalin;
import org.apache.commons.lang3.tuple.Pair;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static java.util.Collections.emptyMap;
import static org.easymock.EasyMock.*;

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
@RunWith(PowerMockRunner.class)
@PrepareForTest(Context.class)
@Category(UnitTest.class)
public class TodoRoutingTest extends EasyMockSupport {
    @Mock
    private Javalin javalin;
    @Mock
    private Injector injector;
    private TodoRouting todoRouting;

    @Before
    public void setup() {
        todoRouting = new TodoRouting(injector, javalin);
    }

    @Test
    public void testRoutingBinding() {
        expect(javalin.routes(anyObject())).andReturn(javalin);
        replayAll();
        todoRouting.bindRoutes();
        verifyAll();
    }

    @Test
    public void testRouteCreation() {
        expect(javalin.get(eq("/"), anyObject())).andReturn(javalin);
        expect(javalin.post(eq("/todos"), anyObject())).andReturn(javalin);
        expect(javalin.get(eq("/todos/:id/edit"), anyObject())).andReturn(javalin);
        expect(javalin.delete(eq("/todos/completed"), anyObject())).andReturn(javalin);
        expect(javalin.delete(eq("/todos/:id"), anyObject())).andReturn(javalin);
        expect(javalin.put(eq("/todos/toggle_status"), anyObject())).andReturn(javalin);
        expect(javalin.put(eq("/todos/:id"), anyObject())).andReturn(javalin);
        expect(javalin.put(eq("/todos/:id/toggle_status"), anyObject())).andReturn(javalin);
        expect(javalin.after(anyObject())).andReturn(javalin);
        replayAll();
        todoRouting.createRoutes();
        verifyAll();
    }

    @Test
    public void testHomeRoute() {
        TodoController controller = createMock(TodoController.class);
        expect(injector.getInstance(TodoController.class)).andReturn(controller);
        expect(controller.home(null, false)).andReturn(Pair.of("template", emptyMap()));
        Context context = PowerMock.createMock(Context.class);
        expect(context.queryParam("status")).andReturn(null);
        expect(context.formParam("ic-request")).andReturn(null);
        expect(context.queryParam("ic-request")).andReturn(null);
        expect(context.renderVelocity("template", emptyMap())).andReturn(context);
        PowerMock.replay(context);
        replayAll();
        todoRouting.home(context);
        verifyAll();
        PowerMock.verify(context);
    }

    @Test
    public void testHomeRouteFromAjax() {
        TodoController controller = createMock(TodoController.class);
        expect(injector.getInstance(TodoController.class)).andReturn(controller);
        expect(controller.home(null, true)).andReturn(Pair.of("template", emptyMap()));
        Context context = PowerMock.createMock(Context.class);
        expect(context.queryParam("status")).andReturn(null);
        expect(context.formParam("ic-request")).andReturn(null);
        expect(context.queryParam("ic-request")).andReturn("ic-request");
        expect(context.renderVelocity("template", emptyMap())).andReturn(context);
        PowerMock.replay(context);
        replayAll();
        todoRouting.home(context);
        verifyAll();
        PowerMock.verify(context);
    }

    @Test
    public void testHomeRouteFromAjaxWithPost() {
        TodoController controller = createMock(TodoController.class);
        expect(injector.getInstance(TodoController.class)).andReturn(controller);
        expect(controller.home(null, true)).andReturn(Pair.of("template", emptyMap()));
        Context context = PowerMock.createMock(Context.class);
        expect(context.queryParam("status")).andReturn(null);
        expect(context.formParam("ic-request")).andReturn("ic-request");
        expect(context.renderVelocity("template", emptyMap())).andReturn(context);
        PowerMock.replay(context);
        replayAll();
        todoRouting.home(context);
        verifyAll();
        PowerMock.verify(context);
    }

    @Test
    public void testAddTodoRoute() {
        TodoController controller = createMock(TodoController.class);
        expect(injector.getInstance(TodoController.class)).andReturn(controller);
        controller.addNewTodo("title");
        Context context = PowerMock.createMock(Context.class);
        expect(context.formParam("todo-title")).andReturn("title");
        PowerMock.replay(context);
        replayAll();
        todoRouting.addNewTodo(context);
        verifyAll();
        PowerMock.verify(context);
    }

    @Test
    public void testToggleTodoByIdRoute() {
        TodoController controller = createMock(TodoController.class);
        expect(injector.getInstance(TodoController.class)).andReturn(controller);
        controller.toggleStatus("id");
        Context context = PowerMock.createMock(Context.class);
        expect(context.param("id")).andReturn("id");
        PowerMock.replay(context);
        replayAll();
        todoRouting.toggleTodoById(context);
        verifyAll();
        PowerMock.verify(context);
    }

    @Test
    public void testChangeTodoTitleRoute() {
        TodoController controller = createMock(TodoController.class);
        expect(injector.getInstance(TodoController.class)).andReturn(controller);
        controller.update("id", "title");
        Context context = PowerMock.createMock(Context.class);
        expect(context.param("id")).andReturn("id");
        expect(context.formParam("todo-title")).andReturn("title");
        PowerMock.replay(context);
        replayAll();
        todoRouting.changeTodoTitle(context);
        verifyAll();
        PowerMock.verify(context);
    }

    @Test
    public void testEditTodoRoute() {
        TodoController controller = createMock(TodoController.class);
        expect(injector.getInstance(TodoController.class)).andReturn(controller);
        Context context = PowerMock.createMock(Context.class);
        expect(controller.edit("id")).andReturn(Pair.of("template", emptyMap()));
        expect(context.param("id")).andReturn("id");
        expect(context.renderVelocity("template", emptyMap())).andReturn(context);
        PowerMock.replay(context);
        replayAll();
        todoRouting.editTodo(context);
        verifyAll();
        PowerMock.verify(context);
    }

    @Test
    public void testRemoveTodoByIdRoute() {
        TodoController controller = createMock(TodoController.class);
        expect(injector.getInstance(TodoController.class)).andReturn(controller);
        controller.remove("id");
        Context context = PowerMock.createMock(Context.class);
        expect(context.param("id")).andReturn("id");
        PowerMock.replay(context);
        replayAll();
        todoRouting.removeTodoById(context);
        verifyAll();
        PowerMock.verify(context);
    }

    @Test
    public void testRemoveCompletedRoute() {
        TodoController controller = createMock(TodoController.class);
        expect(injector.getInstance(TodoController.class)).andReturn(controller);
        controller.removeCompleted();
        Context context = PowerMock.createMock(Context.class);
        PowerMock.replay(context);
        replayAll();
        todoRouting.removeCompleted(context);
        verifyAll();
        PowerMock.verify(context);
    }

    @Test
    public void testToggleAllTodoStatusRoute() {
        TodoController controller = createMock(TodoController.class);
        expect(injector.getInstance(TodoController.class)).andReturn(controller);
        controller.toggleAllStatus();
        Context context = PowerMock.createMock(Context.class);
        PowerMock.replay(context);
        replayAll();
        todoRouting.toggleAllTodoStatus(context);
        verifyAll();
        PowerMock.verify(context);
    }

    @Test
    public void addContentAfterRequestIfEmpty() {
        TodoController controller = createMock(TodoController.class);
        expect(injector.getInstance(TodoController.class)).andReturn(controller);
        Context context = PowerMock.createMock(Context.class);
        expect(context.contentLength()).andReturn(1);
        expect(context.formParam("ic-request")).andReturn("ic-request");
        expect(context.renderVelocity("template", emptyMap())).andReturn(context);
        PowerMock.replay(context);
        expect(controller.home(null, true)).andReturn(Pair.of("template", emptyMap()));
        replayAll();
        todoRouting.afterRequestIsProcessed(context);
        verifyAll();
        PowerMock.verify(context);
    }

    @Test
    public void addContentAfterRequestIfNotEmpty() {
        Context context = PowerMock.createMock(Context.class);
        expect(context.contentLength()).andReturn(-1);
        PowerMock.replay(context);
        replayAll();
        todoRouting.afterRequestIsProcessed(context);
        verifyAll();
        PowerMock.verify(context);
    }

}