/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */
package org.apache.log4j;

import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.test.appender.ListAppender;
import org.apache.logging.log4j.core.test.junit.LoggerContextSource;
import org.apache.logging.log4j.test.junit.UsingThreadContextMap;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test passing MDC values to the Routing appender.
 */
@UsingThreadContextMap
public class LogWithRouteTest {

    private static final String CONFIG = "log-RouteWithMDC.xml";

    @Test
    @LoggerContextSource(CONFIG)
    public void testMDC(final Configuration configuration) {
        MDC.put("Type", "Service");
        MDC.put("Name", "John Smith");
        try {
            final Logger logger = Logger.getLogger("org.apache.test.logging");
            logger.debug("This is a test");
            final ListAppender listApp = configuration.getAppender("List");
            assertNotNull(listApp);
            final List<String> msgs = listApp.getMessages();
            assertNotNull(msgs, "No messages received");
            assertEquals(1, msgs.size());
            assertTrue(msgs.get(0).contains("Type=Service"), "Type is missing");
            assertTrue(msgs.get(0).contains("Name=John Smith"), "Name is missing");
        } finally {
            MDC.remove("Type");
            MDC.remove("Name");
        }
    }
}
