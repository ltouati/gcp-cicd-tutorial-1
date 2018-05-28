package com.google.sample.gcpcicdtutorial.pageobjects;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

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
public class TodoPage {
    private final WebDriver webDriver;


    public TodoPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        if (!"Simple Todo List".equals(webDriver.getTitle())) {
            // Alternatively, we could navigate to the login page, perhaps logging out first
            throw new IllegalStateException("Invalid page");
        }
    }

    private void waitUntil(ExpectedCondition<?> condition) {
        WebDriverWait wait = new WebDriverWait(this.webDriver, 20);
        try {
            wait.until(condition);
        } catch (StaleElementReferenceException e) {
            wait.until(condition);
        }
    }

    public void addTodo(String title) {
        WebElement inputTextElement = webDriver.findElement(By.id("new-todo"));
        inputTextElement.sendKeys(title);
        inputTextElement.sendKeys(Keys.RETURN);
        waitUntil(textToBePresentInElementLocated(By.tagName("label"), title));
    }

    public List<String> getTodos() {
        return webDriver
                .findElement(By.id("todo-list"))
                .findElements(By.tagName("label"))
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public void editTodo(String oldTodoTitle, String newTodoTitle) {
        findTodoByTitle(oldTodoTitle).ifPresent(webElement -> {
            Actions act = new Actions(webDriver);
            act.doubleClick(webElement).build().perform();
            waitUntil(visibilityOfElementLocated(By.id("todo-edit")));
            webDriver.findElement(By.id("todo-edit")).clear();
            webDriver.findElement(By.id("todo-edit")).sendKeys(newTodoTitle);
            webDriver.findElement(By.id("todo-edit")).sendKeys(Keys.RETURN);
            waitUntil(invisibilityOfElementLocated(By.id("todo-edit")));
        });
    }

    private Optional<WebElement> findTodoByTitle(String title) {
        return webDriver
                .findElement(By.id("todo-list"))
                .findElements(By.tagName("label"))
                .stream()
                .filter(webElement -> title.equals(webElement.getText()))
                .findFirst();
    }

    public void toggleTodo(String title) {
        findTodoByTitle(title).ifPresent(webElement -> {
            String id = webElement.getAttribute("id");
            String labelId = StringUtils.replace(id, "label-todo-", "li-todo-");
            boolean wasCompleted = webDriver.findElement(By.id(labelId))
                    .getAttribute("class")
                    .contains("completed");
            webDriver.findElement(By.id(StringUtils.replace(id, "label-todo-", "checkbox-todo-"))).click();
            if (wasCompleted) {
                waitUntil(
                        attributeToBe(
                                By.id(labelId),
                                "class",
                                ""
                        ));

            } else {
                waitUntil(
                        attributeContains(
                                By.id(labelId),
                                "class",
                                "completed"
                        ));
            }
        });
    }

    public List<String> getCompletedTodos() {
        return webDriver
                .findElement(By.id("todo-list"))
                .findElements(By.tagName("li"))
                .stream()
                .filter(webElement -> webElement.getAttribute("class") != null && webElement.getAttribute("class").contains("completed"))
                .map(webElement -> webElement.findElement(By.tagName("label")).getText())
                .collect(Collectors.toList());

    }

    public void removeTodo(String title) {
        findTodoByTitle(title).ifPresent(webElement -> {
            Actions act = new Actions(webDriver);
            act.moveToElement(webElement).build().perform();
            String id = webElement.getAttribute("id");
            String buttonId = StringUtils.replace(id, "label-todo-", "delete-todo-");
            WebElement deleteButton = webDriver.findElement(By.id(buttonId));
            deleteButton.click();
            waitUntil(invisibilityOfElementLocated(By.id(id)));
        });
    }
}
