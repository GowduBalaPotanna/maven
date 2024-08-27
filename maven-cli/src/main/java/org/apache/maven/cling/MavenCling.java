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

import javax.lang.model.SourceVersion;
import javax.tools.Tool;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

import org.apache.maven.cli.MavenCli;
import org.codehaus.plexus.classworlds.ClassWorld;
import picocli.CommandLine;

import static java.util.Objects.requireNonNull;

/**
 * Maven CLI "new-gen".
 */
public class MavenCling implements Tool {
    private static final String CORE_CLASS_REALM_ID = "plexus.core";

    /**
     * "Normal" Java entry point. Note: Maven uses ClassWorld Launcher and this entry point is NOT used!
     */
    public static void main(String[] args) {
        System.exit(main(
                args, new ClassWorld(CORE_CLASS_REALM_ID, Thread.currentThread().getContextClassLoader())));
    }

    /**
     * ClassWorld Launcher entry point.
     */
    public static int main(String[] args, ClassWorld world) {
        return new MavenCling().run(args, world);
    }

    @Override
    public int run(InputStream in, OutputStream out, OutputStream err, String... arguments) {
        return run(
                arguments,
                new ClassWorld(CORE_CLASS_REALM_ID, Thread.currentThread().getContextClassLoader()));
    }

    @Override
    public Set<SourceVersion> getSourceVersions() {
        return Set.of(SourceVersion.latestSupported());
    }

    @Override
    public String name() {
        return "mvn";
    }

    protected MavenOptions getMavenOptions() {
        return new MavenOptions();
    }

    public int run(String[] args, ClassWorld classWorld) {
        requireNonNull(args);
        requireNonNull(classWorld);
        MavenOptions mavenOptions = getMavenOptions();
        CommandLine commandLine = new CommandLine(mavenOptions).setCommandName(name());
        boolean legacyCli = false;
        boolean help = false;
        boolean showVersionAndExit = false;
        try {
            commandLine.parseArgs(args);
            legacyCli = mavenOptions.isLegacyCli();
            help = mavenOptions.isHelp();
            showVersionAndExit = mavenOptions.isShowVersionAndExit();
        } catch (CommandLine.ParameterException e) {
            help = true;
            System.err.println("Bad CLI arguments: " + e.getMessage());
        }
        int exitCode = 0;
        if (legacyCli) {
            exitCode = MavenCli.main(args, classWorld);
        } else if (help || mavenOptions.getGoals().isEmpty()) {
            commandLine.usage(System.out);
        } else if (showVersionAndExit) {
            System.out.println("Version XXX");
        } else {
            System.out.println("Hello world!");
        }
        return exitCode;
    }
}
