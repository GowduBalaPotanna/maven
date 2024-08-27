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
package org.apache.maven.cling.support;

import java.io.PrintStream;
import java.util.function.Consumer;

import org.codehaus.plexus.classworlds.ClassWorld;
import picocli.CommandLine;

/**
 * Maven CLI "new-gen".
 *
 * @param <O> The options type.
 */
public abstract class MavenClingSupport<O extends MavenOptionsSupport> {
    protected static final String CORE_CLASS_REALM_ID = "plexus.core";

    public int run(ClassWorld classWorld, String... args) {
        O mavenOptions = getMavenOptions();
        CommandLine commandLine = new CommandLine(mavenOptions).setCommandName(name());
        try {
            commandLine.parseArgs(args);
        } catch (CommandLine.ParameterException e) {
            System.err.println("Bad CLI arguments: " + e.getMessage());
            commandLine.usage(System.out);
            return 1;
        }
        return doRun(classWorld, mavenOptions, commandLine::usage, args);
    }

    public abstract String name();

    protected abstract O getMavenOptions();

    protected abstract int doRun(ClassWorld classWorld, O mavenOptions, Consumer<PrintStream> usage, String... args);
}
