package com.google.sample.gcpcicdtutorial.di;

import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.internal.RealMultibinder;
import com.google.inject.multibindings.Multibinder;
import com.google.sample.gcpcicdtutorial.UnitTest;
import com.google.sample.gcpcicdtutorial.controller.TodoController;
import com.google.sample.gcpcicdtutorial.routing.Routing;
import com.google.sample.gcpcicdtutorial.routing.TodoRouting;
import com.google.sample.gcpcicdtutorial.services.TodoService;
import com.google.sample.gcpcicdtutorial.services.impl.TodoServiceImpl;
import io.javalin.Javalin;
import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

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
@SuppressWarnings("SameParameterValue")
@RunWith(EasyMockRunner.class)
@Category(UnitTest.class)
public class TodoModuleTest extends EasyMockSupport {

    @Mock
    private Binder binder;

    @SuppressWarnings("unchecked")
    private <T> void testBindToClass(Class<T> source, Class<? extends T> target) {
        AnnotatedBindingBuilder<T> annotatedBindingBuilder = createMock(AnnotatedBindingBuilder.class);
        expect(binder.bind(source)).andReturn(annotatedBindingBuilder);
        expect(annotatedBindingBuilder.to(target)).andReturn(null);
        replay(annotatedBindingBuilder);
    }

    @SuppressWarnings("unchecked")
    private <T> void testBindClass(Class<T> source) {
        AnnotatedBindingBuilder annotatedBindingBuilder = createMock(AnnotatedBindingBuilder.class);
        expect(binder.bind(source)).andReturn(annotatedBindingBuilder);
        replay(annotatedBindingBuilder);
    }

    @SuppressWarnings("unchecked")
    private <T> void testBindInstance(Class<T> source, T target) {
        AnnotatedBindingBuilder<T> annotatedBindingBuilder = createMock(AnnotatedBindingBuilder.class);
        expect(binder.bind(source)).andReturn(annotatedBindingBuilder);
        annotatedBindingBuilder.toInstance(target);
        replay(annotatedBindingBuilder);
    }

    @SuppressWarnings("unchecked")
    private <T> void testMultiBinding(Class<T> source, Class<? extends T>... targets) {
        expect(binder.skipSources(Multibinder.class)).andReturn(binder);
        expect(binder.skipSources(RealMultibinder.class)).andReturn(binder);
        binder.install(isA(RealMultibinder.class));
        LinkedBindingBuilder<T> linkedBindingBuilder = createMock(LinkedBindingBuilder.class);
        expect(binder.bind(isA(Key.class))).andReturn(linkedBindingBuilder);
        for (Class<? extends T> target : targets) {
            expect(linkedBindingBuilder.to(target)).andReturn(null);
        }
        replay(linkedBindingBuilder);

    }

    @Test
    public void testConfigureModule() {
        TodoModule todoModule = TodoModule.create();

        testBindClass(TodoController.class);
        testBindToClass(TodoService.class, TodoServiceImpl.class);
        testBindInstance(Javalin.class, todoModule.app);
        testBindToClass(AppEntrypoint.class, WebEntryPoint.class);

        testMultiBinding(Routing.class, TodoRouting.class);

        replay(binder);
        todoModule.configure(binder);
        verifyAll();
    }

}