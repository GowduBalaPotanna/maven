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
package org.apache.maven.cling.basedir;

import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.maven.api.annotations.Nonnull;

import static java.util.Objects.requireNonNull;

/**
 * Basedir represents a base directory of some structure or layout, like "user home" or "maven home" is.
 * Creating these instances is possible only with not yet existing path, or if path is existing, it is enforced
 * it is a directory (maybe a symlink, but then it should point to a directory).
 */
public abstract class Basedir {
    private final Path basedir;

    public Basedir(Path basedir) {
        this.basedir = validateDirectory(requireNonNull(basedir));
    }

    /**
     * A directory path is valid if:
     * <ul>
     *     <li>is {@code null}</li>
     *     <li>is not {@code null} and does not exists</li>
     *     <li>is not {@code null} and is an existing directory</li>
     * </ul>
     */
    protected Path validateDirectory(Path path) {
        if (path != null && Files.exists(path) && !Files.isDirectory(path)) {
            throw new IllegalArgumentException("The path exists but is not a directory: " + path);
        }
        return path;
    }

    /**
     * A file path is valid if:
     * <ul>
     *     <li>is {@code null}</li>
     *     <li>is not {@code null} and does not exists</li>
     *     <li>is not {@code null} and is an existing file</li>
     * </ul>
     */
    protected Path validateFile(Path path) {
        if (path != null && Files.exists(path) && !Files.isRegularFile(path)) {
            throw new IllegalArgumentException("The path exists but is not a file: " + path);
        }
        return path;
    }

    @Nonnull
    public Path basedir() {
        return basedir;
    }
}
