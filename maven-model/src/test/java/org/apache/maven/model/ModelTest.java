/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.maven.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests {@code Model}.
 *
 */
class ModelTest {

    @Test
    void testHashCodeNullSafe() {
        new Model().hashCode();
    }

    @Test
    void testBuild() {
        Model model = new Model();
        Build build = new Build();
        build.setOutputDirectory("myOutputDirectory");
        model.setBuild(build);
        Build build2 = model.getBuild();
        assertNotNull(build2);
        assertEquals("myOutputDirectory", build2.getOutputDirectory());
        model.setBuild(null);
        assertNull(model.getBuild());
    }

    @Test
    void testEqualsNullSafe() {
        assertFalse(new Model().equals(null));

        new Model().equals(new Model());
    }

    @Test
    void testEqualsIdentity() {
        Model thing = new Model();
        assertTrue(thing.equals(thing));
    }

    @Test
    void testToStringNullSafe() {
        assertNotNull(new Model().toString());
    }

    @Test
    void testModelGroupId() {
        Model model = new Model();
        model.setGroupId("org.apache.maven");
        assertEquals("org.apache.maven", model.getGroupId());
    }
}
