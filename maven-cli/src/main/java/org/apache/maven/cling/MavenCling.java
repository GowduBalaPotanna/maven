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

import javax.tools.Tool;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.Consumer;

import org.apache.maven.cli.CLIReportingUtils;
import org.apache.maven.cli.MavenCli;
import org.apache.maven.cling.support.MavenClingSupport;
import org.codehaus.plexus.classworlds.ClassWorld;

import static java.util.Objects.requireNonNull;

/**
 * Maven CLI "new-gen".
 */
public class MavenCling extends MavenClingSupport<MavenClingOptions> implements Tool {
    /**
     * "Normal" Java entry point. Note: Maven uses ClassWorld Launcher and this entry point is NOT used!
     */
    public static void main(String[] args) {
        System.exit(main(args, createClassWorld()));
    }

    /**
     * ClassWorld Launcher entry point.
     */
    public static int main(String[] args, ClassWorld world) {
        return new MavenCling(world).run(args);
    }

    private static ClassWorld createClassWorld() {
        return new ClassWorld(CORE_CLASS_REALM_ID, Thread.currentThread().getContextClassLoader());
    }

    private final ClassWorld classWorld;

    public MavenCling() {
        this(createClassWorld());
    }

    public MavenCling(ClassWorld classWorld) {
        this.classWorld = requireNonNull(classWorld);
    }

    @Override
    public String name() {
        return "mvn";
    }

    @Override
    protected MavenClingOptions getMavenOptions() {
        return new MavenClingOptions();
    }

    @Override
    protected int doRun(MavenClingOptions options, Consumer<PrintStream> usage, String... args) {
        if (options.isLegacyCli()) {
            // just delegate it: but filter out one command legacy does not know
            // TODO: how to not duplicate string literal here?
            return MavenCli.main(
                    Arrays.stream(args).filter(a -> !a.equals("--legacy-cli")).toArray(String[]::new), classWorld);
        } else {
            if (options.isShowVersionAndExit()) {
                if (options.isQuiet()) {
                    System.out.println(CLIReportingUtils.showVersionMinimal());
                } else {
                    System.out.println(CLIReportingUtils.showVersion());
                }
                return 0;
            } else if (options.getGoals().orElseGet(Collections::emptyList).isEmpty()) {
                System.err.println("No goals specified!");
                usage.accept(System.out);
                return 1;
            }
            System.out.println("Hello world!");
            return 0;
        }
    }
}
