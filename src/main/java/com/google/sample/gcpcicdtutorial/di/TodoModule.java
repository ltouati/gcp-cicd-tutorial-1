package com.google.sample.gcpcicdtutorial.di;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.google.sample.gcpcicdtutorial.controller.TodoController;
import com.google.sample.gcpcicdtutorial.routing.Routing;
import com.google.sample.gcpcicdtutorial.routing.TodoRouting;
import com.google.sample.gcpcicdtutorial.services.TodoService;
import com.google.sample.gcpcicdtutorial.services.impl.TodoServiceImpl;
import io.javalin.Javalin;

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
public class TodoModule extends AbstractModule {
    protected Javalin app;

    protected TodoModule(Javalin app) {
        this.app = app;
    }

    public static TodoModule create() {
        return new TodoModule(Javalin.create());
    }

    @Override
    protected void configure() {
        bind(TodoController.class);
        bind(TodoService.class).to(TodoServiceImpl.class);
        bind(Javalin.class).toInstance(app);
        bind(AppEntrypoint.class).to(WebEntryPoint.class);
        Multibinder.newSetBinder(binder(), Routing.class).addBinding().to(TodoRouting.class);
    }

    public void stop() {
        app.stop();
    }
}