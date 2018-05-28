package com.google.sample.gcpcicdtutorial;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.sample.gcpcicdtutorial.di.AppEntrypoint;
import com.google.sample.gcpcicdtutorial.di.TodoModule;

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
// $COVERAGE-IGNORE$
public class Main {
    public static void main(String[] args) {
        // $COVERAGE-IGNORE$
        Injector injector = Guice.createInjector(TodoModule.create());
        injector.getInstance(AppEntrypoint.class).boot(7000);
    }
}
