package com.google.sample.gcpcicdtutorial.model;

import com.google.sample.gcpcicdtutorial.UnitTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static com.google.code.beanmatchers.BeanMatchers.*;
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
@Category(UnitTest.class)
public class TodoTest {

    @Test
    public void shouldHaveANoArgsConstructor() {
        assertThat(Todo.class, hasValidBeanConstructor());
    }

    @Test
    public void gettersAndSettersShouldWorkForEachProperty() {
        assertThat(Todo.class, hasValidGettersAndSettersExcluding("complete"));
    }

    @Test
    public void allPropertiesShouldInfluenceHashCode() {
        assertThat(Todo.class, hasValidBeanHashCodeExcluding("complete"));
    }

    @Test
    public void allPropertiesShouldBeComparedDuringEquals() {
        assertThat(Todo.class, hasValidBeanEqualsExcluding("complete"));
    }

    @Test
    public void allPropertiesShouldBeRepresentedInToStringOutput() {
        assertThat(Todo.class, hasValidBeanToStringExcluding("complete"));
    }

    @Test
    public void testCreateATodo() {
        Todo todo = Todo.create("TITLE");

        assertThat(todo.getTitle(), equalTo("TITLE"));
    }

    @Test
    public void testToggle() {
        Todo todo = Todo.create("TITLE");
        todo.toggleStatus();
        assertThat(todo.getStatus(), equalTo(Status.COMPLETE));
        assertThat(todo.isComplete(), equalTo(true));
        todo.toggleStatus();
        assertThat(todo.getStatus(), equalTo(Status.ACTIVE));
        assertThat(todo.isComplete(), equalTo(false));

    }

}