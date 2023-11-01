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
package org.apache.maven.api;

import java.util.Map;

import org.apache.maven.api.annotations.Experimental;
import org.apache.maven.api.annotations.Immutable;

/**
 * Dependency properties supported by Maven Core.
 *
 * @since 4.0.0
 */
@Experimental
@Immutable
public interface DependencyProperties {
    /**
     * Boolean flag telling that dependency contains all of its dependencies.
     * Note: this flag must be kept in sync with resolver!
     */
    String FLAG_INCLUDES_DEPENDENCIES = "includesDependencies";

    /**
     * Boolean flag telling that dependency is meant to be placed on class path.
     */
    String FLAG_CLASS_PATH_CONSTITUENT = "classPathConstituent";

    /**
     * Boolean flag telling that dependency is meant to be placed on module path.
     */
    String FLAG_MODULE_PATH_CONSTITUENT = "modulePathConstituent";

    /**
     * Boolean flag telling that dependency is meant to be used as Java Agent.
     */
    String FLAG_IS_JAVA_AGENT = "isJavaAgent";

    /**
     * Boolean flag telling that dependency is meant to be used as Java Annotation Processor.
     */
    String FLAG_IS_JAVA_ANNOTATION_PROCESSOR = "isJavaAnnotationProcessor";

    /**
     * Boolean flag telling that dependency is meant to be used as Java Doclet.
     */
    String FLAG_IS_JAVA_DOCLET = "isJavaDoclet";

    /**
     * Returns immutable "map view" of all the properties.
     */
    Map<String, String> asMap();

    /**
     * Returns {@code true} if given flag is {@code true}.
     */
    boolean checkFlag(String flag);
}
