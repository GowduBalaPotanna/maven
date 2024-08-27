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

import java.util.List;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * Maven CLI options.
 */
@Command(name = "mvn")
public class MavenOptions {
    @Option(
            names = {"-h", "--help"},
            arity = "0",
            defaultValue = "false",
            description = "Display help information")
    boolean help;

    @Option(
            names = {"-f", "--file"},
            arity = "1",
            description = "Force the use of an alternate POM file (or directory with pom.xml)")
    String alternatePomFile;

    @Option(
            names = {"-D", "--define"},
            arity = "1",
            description = "Define a user property")
    List<String> userProperties;

    @Option(
            names = {"-o", "--offline"},
            arity = "0",
            defaultValue = "false",
            description = "Work offline")
    boolean offline;

    @Option(
            names = {"-v", "--version"},
            arity = "0",
            defaultValue = "false",
            description = "Display version information and exit")
    boolean version;

    @Option(
            names = {"-q", "--quiet"},
            arity = "0",
            defaultValue = "false",
            description = "Quiet output - only show errors")
    boolean quiet;

    @Option(
            names = {"-X", "--verbose"},
            arity = "0",
            defaultValue = "false",
            description = "Quiet output - only show errors")
    boolean verbose;

    @Option(
            names = {"-e", "--errors"},
            arity = "0",
            defaultValue = "false",
            description = "Produce execution error messages")
    boolean errors;

    @Option(
            names = {"-N", "--non-recursive"},
            arity = "0",
            defaultValue = "false",
            description =
                    "Do not recurse into sub-projects. When used together with -pl, do not recurse into sub-projects of selected aggregators")
    boolean nonRecursive;

    @Option(
            names = {"-U", "--update-snapshots"},
            arity = "0",
            defaultValue = "false",
            description = "Forces a check for missing releases and updated snapshots on remote repositories")
    boolean updateSnapshots;

    @Option(
            names = {"-P", "--activate-profiles"},
            arity = "1..*",
            description =
                    "Comma-delimited list of profiles to activate. Prefixing a profile with ! excludes it, and ? marks it as optional")
    List<String> activateProfiles;

    @Option(
            names = {"-B", "--batch-mode", "--non-interactive"},
            arity = "0",
            defaultValue = "false",
            description = "Run in non-interactive (batch) mode")
    boolean nonInteractive;

    @Option(
            names = {"--force-interactive"},
            arity = "0",
            defaultValue = "false",
            description =
                    "Run in interactive mode. Overrides, if applicable, the CI environment variable and --non-interactive/--batch-mode options")
    boolean forceInteractive;

    @Option(
            names = {"-nsu", "--no-snapshot-updates"},
            arity = "0",
            defaultValue = "false",
            description = "Suppress SNAPSHOT updates")
    boolean suppressSnapshotUpdates;

    @Option(
            names = {"-C", "--strict-checksums"},
            arity = "0",
            defaultValue = "false",
            description = "Fail the build if checksums don't match")
    boolean strictChecksums;

    @Option(
            names = {"-c", "--lax-checksums"},
            arity = "0",
            defaultValue = "false",
            description = "Warn if checksums don't match")
    boolean relaxedChecksums;

    @Option(
            names = {"-s", "--settings"},
            arity = "1",
            description = "Alternate path for the user settings file")
    String altUserSettings;

    @Option(
            names = {"-ps", "--project-settings"},
            arity = "1",
            description = "Alternate path for the project settings file")
    String altProjectSettings;

    @Option(
            names = {"-gs", "--global-settings"},
            arity = "1",
            description = "Alternate path for the global settings file")
    String altGlobalSettings;

    @Option(
            names = {"-is", "--install-settings"},
            arity = "1",
            description = "Alternate path for the installation settings file")
    String altInstallationSettings;

    @Option(
            names = {"-t", "--toolchains"},
            arity = "1",
            description = "Alternate path for the user toolchains file")
    String altUserToolchains;

    @Option(
            names = {"-gt", "--global-toolchains"},
            arity = "1",
            description = "Alternate path for the global toolchains file")
    String altGlobalToolchains;

    @Option(
            names = {"-it", "--install-toolchains"},
            arity = "1",
            description = "Alternate path for the installation toolchains file")
    String altInstallationToolchains;

    @Option(
            names = {"-fos", "--fail-on-severity"},
            arity = "1",
            description = "Configure which severity of logging should cause the build to fail")
    String failOnSeverity;

    @Option(
            names = {"-ff", "--fail-fast"},
            arity = "0",
            description = "Stop at first failure in build")
    boolean failFast;

    @Option(
            names = {"-fae", "--fail-at-end"},
            arity = "0",
            description = "Only fail the build afterwards; allow all non-impacted builds to continue")
    boolean failAtEnd;

    @Option(
            names = {"-fn", "--fail-never"},
            arity = "0",
            description = "Never fail the build, regardless of project result")
    boolean failNever;

    @Option(
            names = {"-r", "--resume"},
            arity = "0",
            description =
                    "Resume reactor from the last failed project, using the resume.properties file in the build directory")
    boolean resume;

    @Option(
            names = {"-rf", "--resume-from"},
            arity = "1",
            description = "Resume reactor from specified project")
    String resumeFrom;

    @Option(
            names = {"-pl", "--projects"},
            arity = "1..*",
            description =
                    "Comma-delimited list of specified reactor projects to build instead of all projects. A project can be specified by [groupId]:artifactId or by its relative path. Prefixing a project with ! excludes it, and ? marks it as optional")
    List<String> projectList;

    @Option(
            names = {"-am", "--also-make"},
            arity = "1..*",
            description = "If project list is specified, also build projects required by this list")
    List<String> alsoMake;

    @Option(
            names = {"-amd", "--also-make-dependents"},
            arity = "0",
            description = "If project list is specified, also build projects that depend on projects on the list")
    boolean alsoMakeDependents;

    @Option(
            names = {"-l", "--log-file"},
            arity = "1",
            description = "Log file where all build output will go (disables output color)")
    String logFile;

    @Option(
            names = {"-V", "--show-version"},
            arity = "0",
            description = "Display version information without exiting")
    boolean showVersion;

    @Option(
            names = {"-T", "--threads"},
            arity = "1",
            description = "Thread count, for instance 4 (int) or 2C/2.5C (int/float) where C is core multiplied")
    String threads;

    @Option(
            names = {"-b", "--builder"},
            arity = "1",
            description = "The id of the build strategy to use")
    String builder;

    @Option(
            names = {"-ntp", "--no-transfer-progress"},
            arity = "0",
            description = "Do not display transfer progress when downloading or uploading")
    boolean noTransferProgress;

    @Option(
            names = {"--color"},
            arity = "1",
            defaultValue = "auto",
            description = "Defines the color mode of the output. Supported are 'auto', 'always', 'never'")
    String color;

    @Option(
            names = {"-canf", "--cache-artifact-not-found"},
            arity = "0",
            description =
                    "Defines caching behaviour for 'not found' artifacts. Supported values are 'true' (default), 'false'")
    boolean cacheArtifactNotFound;

    @Option(
            names = {"-sadp", "--strict-artifact-descriptor-policy"},
            arity = "0",
            description = "Defines 'strict' artifact descriptor policy. Supported values are 'true', 'false' (default)")
    boolean strictArtifactDescriptorPolicy;

    @Option(
            names = {"-itr", "--ignore-transitive-repositories"},
            arity = "0",
            description = "If set, Maven will ignore remote repositories introduced by transitive dependencies")
    boolean ignoreTransitiveRepositories;

    @Parameters(paramLabel = "GOALS")
    List<String> goals;
}
