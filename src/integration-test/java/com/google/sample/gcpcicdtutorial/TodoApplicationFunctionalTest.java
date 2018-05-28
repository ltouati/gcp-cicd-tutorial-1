package com.google.sample.gcpcicdtutorial;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.sample.gcpcicdtutorial.di.AppEntrypoint;
import com.google.sample.gcpcicdtutorial.di.TodoModule;
import com.google.sample.gcpcicdtutorial.pageobjects.TodoPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.net.Socket;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
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
@Category(IntegrationTest.class)
public class TodoApplicationFunctionalTest {

    public static final String FIRST_TODO_TITLE = "My New TODO";
    private WebDriver webDriver;
    private TodoModule todoModule;

    @Before
    public void beforeTest() {
        int serverPort = (int) (6000 + (Math.random() * 3000));
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");

        webDriver = new ChromeDriver(chromeOptions);
        Thread t = new Thread(() -> {
            todoModule = TodoModule.create();
            Injector injector = Guice.createInjector(todoModule);
            injector.getInstance(AppEntrypoint.class).boot(serverPort);
        });
        t.start();
        waitUntilPortIsUp(serverPort);
        webDriver.get(String.format("http://localhost:%d/", serverPort));
    }


    public void waitUntilPortIsUp(int port) {
        while (true) {
            try (Socket socket = new Socket("127.0.0.1", port)) {
                return;
            } catch (Exception e) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {

                }
            }
        }
    }

    @After
    public void quitWebDriver() {
        webDriver.quit();
    }

    @After
    public void quitJavalin() {
        todoModule.stop();
    }


    @Test
    public void a_user_should_be_able_to_add_a_new_todo() {
        TodoPage todoPage = new TodoPage(webDriver);
        todoPage.addTodo(FIRST_TODO_TITLE);
        List<String> todos = todoPage.getTodos();
        assertThat(todos.size(), equalTo(1));
        assertThat(todos, hasItem(FIRST_TODO_TITLE));
    }

    @Test
    public void a_user_should_be_able_to_edit_a_todo() {
        TodoPage todoPage = new TodoPage(webDriver);
        todoPage.addTodo(FIRST_TODO_TITLE);
        List<String> todos = todoPage.getTodos();
        assertThat(todos.size(), equalTo(1));
        assertThat(todos, hasItem(FIRST_TODO_TITLE));
        todoPage.editTodo(FIRST_TODO_TITLE, "My Modified Todo");
        todos = todoPage.getTodos();
        assertThat(todos.size(), equalTo(1));
        assertThat(todos, hasItem("My Modified Todo"));

    }

    @Test
    public void a_user_should_be_able_to_toggle_a_todo() {
        TodoPage todoPage = new TodoPage(webDriver);
        todoPage.addTodo(FIRST_TODO_TITLE);
        todoPage.toggleTodo(FIRST_TODO_TITLE);
        List<String> todos = todoPage.getCompletedTodos();
        assertThat(todos.size(), equalTo(1));
        assertThat(todos, hasItem(FIRST_TODO_TITLE));
        todoPage.toggleTodo(FIRST_TODO_TITLE);
        todos = todoPage.getCompletedTodos();
        assertThat(todos.size(), equalTo(0));
    }

    @Test
    public void a_user_should_be_able_to_remove_a_todo() {
        TodoPage todoPage = new TodoPage(webDriver);
        todoPage.addTodo(FIRST_TODO_TITLE);
        List<String> todos = todoPage.getTodos();
        assertThat(todos.size(), equalTo(1));
        todoPage.removeTodo(FIRST_TODO_TITLE);
        todos = todoPage.getTodos();
        assertThat(todos.size(), equalTo(0));
    }
}
