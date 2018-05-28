package com.google.sample.gcpcicdtutorial.di;

import com.google.common.collect.ImmutableSet;
import com.google.sample.gcpcicdtutorial.UnitTest;
import com.google.sample.gcpcicdtutorial.routing.Routing;
import io.javalin.Javalin;
import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import java.util.Set;

import static org.easymock.EasyMock.expect;

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
public class WebEntryPointTest extends EasyMockSupport {
    @Mock
    private Javalin javalin;
    @Mock
    private Routing routing;

    @Test
    public void boot() {
        Set<Routing> routes = ImmutableSet.of(routing);
        WebEntryPoint webEntryPoint = new WebEntryPoint(javalin, routes);
        routing.bindRoutes();
        expect(javalin.enableStaticFiles("/public/")).andReturn(javalin);
        expect(javalin.port(7000)).andReturn(javalin);
        expect(javalin.start()).andReturn(javalin);
        replayAll();

        webEntryPoint.boot(7000);

        verifyAll();
    }
}