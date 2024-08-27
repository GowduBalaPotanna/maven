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
import java.io.PrintStream;
import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;

import org.apache.maven.cli.MavenCli;
import org.apache.maven.cling.support.MavenClingSupport;
import org.codehaus.plexus.classworlds.ClassWorld;

/**
 * Maven CLI "new-gen".
 */
public class MavenCling extends MavenClingSupport<MavenClingOptions> implements Tool {
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
        return new MavenCling().run(world, args);
    }

    @Override
    public String name() {
        return "mvn";
    }

    @Override
    public int run(InputStream in, OutputStream out, OutputStream err, String... arguments) {
        return run(new ClassWorld(CORE_CLASS_REALM_ID, Thread.currentThread().getContextClassLoader()), arguments);
    }

    @Override
    public Set<SourceVersion> getSourceVersions() {
        return Set.of(SourceVersion.latestSupported());
    }

    @Override
    protected MavenClingOptions getMavenOptions() {
        return new MavenClingOptions();
    }

    @Override
    protected int doRun(ClassWorld classWorld, MavenClingOptions options, Consumer<PrintStream> usage, String... args) {
        if (options.isLegacyCli()) {
            return MavenCli.main(args, classWorld); // just delegate it
        } else if (options.isShowVersionAndExit()) {
            System.out.println("Version XXX");
        } else {
            if (options.getGoals().orElseGet(Collections::emptyList).isEmpty()) {
                usage.accept(System.out);
                return 1;
            }
            System.out.println("Hello world!");
        }
        return 0;
    }
}
