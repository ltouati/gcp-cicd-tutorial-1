package com.google.sample.gcpcicdtutorial.routing;

import com.google.inject.Injector;
import com.google.sample.gcpcicdtutorial.controller.TodoController;
import io.javalin.Context;
import io.javalin.Javalin;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;

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
public class TodoRouting extends Routing<TodoController> {
    private Javalin javalin;

    @Inject
    public TodoRouting(Injector injector, Javalin javalin) {
        super(injector);
        this.javalin = javalin;
    }

    @Override
    public void bindRoutes() {
        javalin.routes(this::createRoutes);
    }

    protected void createRoutes() {
        this.javalin
                .get("/", this::home)
                .post("/todos", this::addNewTodo)
                .get("/todos/:id/edit", this::editTodo)
                .delete("/todos/completed", this::removeCompleted)
                .delete("/todos/:id", this::removeTodoById)
                .put("/todos/toggle_status", this::toggleAllTodoStatus)
                .put("/todos/:id", this::changeTodoTitle)
                .put("/todos/:id/toggle_status", this::toggleTodoById)
                .after(this::afterRequestIsProcessed);
    }

    protected void afterRequestIsProcessed(Context context) {
        if (context.contentLength() > 0) { // if the route didn't return anything
            renderVelocityTemplate(context, getController().home(null, isICRequest(context)));
        }
    }

    private boolean isICRequest(Context context) {
        String ret = context.formParam("ic-request");
        if (ret == null) {
            return context.queryParam("ic-request") != null;
        }
        return true;
    }


    protected void toggleTodoById(Context ctx) {
        getController().toggleStatus(ctx.param("id"));
    }

    protected void changeTodoTitle(Context ctx) {
        getController().update(ctx.param("id"), ctx.formParam("todo-title"));
    }

    protected void toggleAllTodoStatus(Context context) {
        getController().toggleAllStatus();
    }

    protected void removeTodoById(Context ctx) {
        getController().remove(ctx.param("id"));
    }

    protected void removeCompleted(Context ignored) {
        getController().removeCompleted();
    }

    protected void editTodo(Context context) {
        renderVelocityTemplate(context, getController().edit(context.param("id")));
    }

    @NotNull
    private Context renderVelocityTemplate(Context context, Pair<String, Map<String, Object>> tuple) {
        return context.renderVelocity(tuple.getLeft(), tuple.getRight());
    }

    protected void addNewTodo(Context ctx) {
        getController().addNewTodo(ctx.formParam("todo-title"));
    }

    protected void home(Context context) {
        renderVelocityTemplate(context, getController().home(context.queryParam("status"), isICRequest(context)));
    }
}
