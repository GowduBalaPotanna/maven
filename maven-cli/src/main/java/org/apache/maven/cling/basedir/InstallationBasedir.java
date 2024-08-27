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

import java.nio.file.Path;

import org.apache.maven.api.annotations.Nonnull;

/**
 * Maven Installation (former "maven home") base directory.
 */
public class InstallationBasedir extends Basedir {
    public static class Builder {
        private final Path basedir;
        private Path settingsXml;
        private Path toolchainsXml;
        private Path extensionsXml;

        private Builder(Path basedir) {
            this.basedir = basedir;
        }

        @Nonnull
        public InstallationBasedir build() {
            return new InstallationBasedir(basedir, settingsXml, toolchainsXml, extensionsXml);
        }

        @Nonnull
        public Builder settingsXml(final Path settingsXml) {
            this.settingsXml = settingsXml;
            return this;
        }

        @Nonnull
        public Builder toolchainsXml(final Path toolchainsXml) {
            this.toolchainsXml = toolchainsXml;
            return this;
        }

        @Nonnull
        public Builder extensionsXml(final Path extensionsXml) {
            this.extensionsXml = extensionsXml;
            return this;
        }
    }

    @Nonnull
    public static Builder builder(Path basedir) {
        return new Builder(basedir);
    }

    private final Path settingsXml;
    private final Path toolchainsXml;
    private final Path extensionsXml;

    private InstallationBasedir(Path basedir, Path settingsXml, Path toolchainsXml, Path extensionsXml) {
        super(basedir);
        this.settingsXml = validateFile(settingsXml);
        this.toolchainsXml = validateFile(toolchainsXml);
        this.extensionsXml = validateFile(extensionsXml);
    }

    @Nonnull
    public Path bin() {
        return basedir().resolve("bin");
    }

    @Nonnull
    public Path boot() {
        return basedir().resolve("boot");
    }

    @Nonnull
    public Path conf() {
        return basedir().resolve("conf");
    }

    @Nonnull
    public Path lib() {
        return basedir().resolve("lib");
    }

    @Nonnull
    public Path libExt() {
        return lib().resolve("ext");
    }

    @Nonnull
    public Path settingsXml() {
        if (settingsXml != null) {
            return settingsXml;
        }
        return conf().resolve("settings.xml");
    }

    @Nonnull
    public Path toolchainsXml() {
        if (toolchainsXml != null) {
            return toolchainsXml;
        }
        return conf().resolve("toolchains.xml");
    }

    @Nonnull
    public Path extensionsXml() {
        if (extensionsXml != null) {
            return extensionsXml;
        }
        return conf().resolve("extensions.xml");
    }
}
