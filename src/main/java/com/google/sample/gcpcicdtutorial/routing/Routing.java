package com.google.sample.gcpcicdtutorial.routing;

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

import com.google.inject.Inject;
import com.google.inject.Injector;

import java.lang.reflect.ParameterizedType;

public abstract class Routing<T> {
    private final Injector injector;

    private Class<T> controller;

    @Inject
    Routing(Injector injector) {
        this.injector = injector;
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        //noinspection unchecked
        this.controller = (Class<T>) type.getActualTypeArguments()[0];
    }

    public abstract void bindRoutes();

    public T getController() {
        return injector.getInstance(getControllerFromGenericType());
    }

    private Class<T> getControllerFromGenericType() {
        return controller;
    }
}