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
package org.apache.maven.cling;

import org.codehaus.plexus.classworlds.ClassWorld;
import picocli.CommandLine;

import static java.util.Objects.requireNonNull;

/**
 * Maven CLI "new-gen".
 */
public class MavenCling {
    private static final String CORE_CLASS_REALM_ID = "plexus.core";

    /**
     * "Normal" Java entry point. Note: Maven uses ClassWorld Launcher and this entry point is NOT used!
     */
    public static void main(String[] args) {
        main(args, new ClassWorld(CORE_CLASS_REALM_ID, Thread.currentThread().getContextClassLoader()));
    }

    /**
     * ClassWorld Launcher entry point.
     */
    public static void main(String[] args, ClassWorld world) {
        new MavenCling(args, world).run();
    }

    private final String[] args;
    private final ClassWorld classWorld;
    private final MavenOptions mavenOptions;
    private final CommandLine commandLine;

    public MavenCling(String[] args, ClassWorld classWorld) {
        this.args = requireNonNull(args);
        this.classWorld = requireNonNull(classWorld);
        this.mavenOptions = getMavenOptions();
        this.commandLine = new CommandLine(this.mavenOptions).setCommandName(getCommandName());
    }

    protected MavenOptions getMavenOptions() {
        return new MavenOptions();
    }

    protected String getCommandName() {
        return "mvn";
    }

    public void run() {
        try {
            commandLine.parseArgs(args);
        } catch (CommandLine.ParameterException e) {
            mavenOptions.help = true;
            System.err.println("Bad CLI arguments: " + e.getMessage());
        }
        if (mavenOptions.help) {
            commandLine.usage(System.out);
        } else {
            System.out.println("Hello world!");
        }
    }
}
