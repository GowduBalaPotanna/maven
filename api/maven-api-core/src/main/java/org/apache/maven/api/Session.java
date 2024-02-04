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

import java.nio.file.Path;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.apache.maven.api.annotations.Experimental;
import org.apache.maven.api.annotations.Nonnull;
import org.apache.maven.api.annotations.Nullable;
import org.apache.maven.api.annotations.ThreadSafe;
import org.apache.maven.api.model.Repository;
import org.apache.maven.api.services.DependencyCoordinateFactory;
import org.apache.maven.api.settings.Settings;

/**
 * The session to install / deploy / resolve artifacts and dependencies.
 *
 * @since 4.0.0
 */
@Experimental
@ThreadSafe
public interface Session {

    @Nonnull
    Settings getSettings();

    @Nonnull
    LocalRepository getLocalRepository();

    @Nonnull
    List<RemoteRepository> getRemoteRepositories();

    @Nonnull
    SessionData getData();

    /**
     * Returns immutable user properties to use for interpolation. The user properties have been configured directly
     * by the user, e.g. via the {@code -Dkey=value} parameter on the command line.
     *
     * @return the user properties, never {@code null}
     */
    @Nonnull
    Map<String, String> getUserProperties();

    /**
     * Returns immutable system properties to use for interpolation. The system properties are collected from the
     * runtime environment such as {@link System#getProperties()} and environment variables
     * (prefixed with {@code env.}).
     *
     * @return the system properties, never {@code null}
     */
    @Nonnull
    Map<String, String> getSystemProperties();

    /**
     * Each invocation computes a new map of effective properties. To be used in interpolation.
     * <p>
     * Effective properties are computed from system, user and optionally project properties, layered with
     * defined precedence onto each other to achieve proper precedence. Precedence is defined as:
     * <ul>
     *     <li>System properties (lowest precedence)</li>
     *     <li>Project properties (optional)</li>
     *     <li>User properties (highest precedence)</li>
     * </ul>
     * Note: Project properties contains properties injected from profiles, if applicable. Their precedence is
     * {@code profile > project}, hence active profile property may override project property.
     * <p>
     * The caller of this method should decide whether there is a project in scope (hence, a project instance
     * needs to be passed) or not.
     *
     * @param project {@link Project} or {@code null}.
     * @return the effective properties, never {@code null}
     */
    @Nonnull
    Map<String, String> getEffectiveProperties(@Nullable Project project);

    /**
     * Returns the current maven version
     * @return the maven version, never {@code null}
     */
    @Nonnull
    Version getMavenVersion();

    int getDegreeOfConcurrency();

    @Nonnull
    Instant getStartTime();

    /**
     * Gets the directory of the topmost project being built, usually the current directory or the
     * directory pointed at by the {@code -f/--file} command line argument.
     */
    @Nonnull
    Path getTopDirectory();

    /**
     * Gets the root directory of the session, which is the root directory for the top directory project.
     *
     * @throws IllegalStateException if the root directory could not be found
     * @see #getTopDirectory()
     * @see Project#getRootDirectory()
     */
    @Nonnull
    Path getRootDirectory();

    @Nonnull
    List<Project> getProjects();

    /**
     * Returns the plugin context for mojo being executed and the specified
     * {@link Project}, never returns {@code null} as if context not present, creates it.
     *
     * <strong>Implementation note:</strong> while this method return type is {@link Map}, the
     * returned map instance implements {@link java.util.concurrent.ConcurrentMap} as well.
     *
     * @throws org.apache.maven.api.services.MavenException if not called from the within a mojo execution
     */
    @Nonnull
    Map<String, Object> getPluginContext(@Nonnull Project project);

    /**
     * Retrieves the service for the interface
     *
     * @throws NoSuchElementException if the service could not be found
     */
    @Nonnull
    <T extends Service> T getService(@Nonnull Class<T> clazz);

    /**
     * Creates a derived session using the given local repository.
     *
     * @param localRepository the new local repository
     * @return the derived session
     * @throws NullPointerException if {@code localRepository} is null
     */
    @Nonnull
    Session withLocalRepository(@Nonnull LocalRepository localRepository);

    /**
     * Creates a derived session using the given remote repositories.
     *
     * @param repositories the new list of remote repositories
     * @return the derived session
     * @throws NullPointerException if {@code repositories} is null
     */
    @Nonnull
    Session withRemoteRepositories(@Nonnull List<RemoteRepository> repositories);

    /**
     * Register the given listener which will receive all events.
     *
     * @param listener the listener to register
     * @throws NullPointerException if {@code listener} is null
     */
    void registerListener(@Nonnull Listener listener);

    /**
     * Unregisters a previously registered listener.
     *
     * @param listener the listener to unregister
     * @throws NullPointerException if {@code listener} is null
     */
    void unregisterListener(@Nonnull Listener listener);

    /**
     * Returns the list of registered listeners.
     *
     * @return an immutable collection of listeners, never {@code null}
     */
    @Nonnull
    Collection<Listener> getListeners();

    /**
     * Shortcut for {@code getService(RepositoryFactory.class).createLocal(...)}.
     *
     * @param path location of the local repository to create
     * @return cache of artifacts downloaded from a remote repository or built locally
     *
     * @see org.apache.maven.api.services.RepositoryFactory#createLocal(Path)
     */
    @Nonnull
    LocalRepository createLocalRepository(@Nonnull Path path);

    /**
     * Shortcut for {@code getService(RepositoryFactory.class).createRemote(...)}.
     *
     * @param  id identifier of the remote repository to create
     * @param  url location of the remote repository
     * @return remote repository that can be used to download or upload artifacts
     *
     * @see org.apache.maven.api.services.RepositoryFactory#createRemote(String, String)
     */
    @Nonnull
    RemoteRepository createRemoteRepository(@Nonnull String id, @Nonnull String url);

    /**
     * Shortcut for {@code getService(RepositoryFactory.class).createRemote(...)}.
     *
     * @param repository information needed for establishing connections with remote repository
     * @return remote repository that can be used to download or upload artifacts
     *
     * @see org.apache.maven.api.services.RepositoryFactory#createRemote(Repository)
     */
    @Nonnull
    RemoteRepository createRemoteRepository(@Nonnull Repository repository);

    /**
     * Creates a coordinate out of string that is formatted like:
     * {@code <groupId>:<artifactId>[:<extension>[:<classifier>]]:<version>}.
     * <p>
     * Shortcut for {@code getService(ArtifactFactory.class).create(...)}.
     *
     * @param coordString the string having "standard" coordinate.
     * @return coordinate used to point to the artifact
     *
     * @see org.apache.maven.api.services.ArtifactCoordinateFactory#create(Session, String)
     */
    @Nonnull
    ArtifactCoordinate createArtifactCoordinate(@Nonnull String coordString);

    /**
     * Shortcut for {@code getService(ArtifactFactory.class).create(...)}.
     *
     * @param groupId the group identifier, or {@code null} is unspecified
     * @param artifactId the artifact identifier, or {@code null} is unspecified
     * @param version the artifact version, or {@code null} is unspecified
     * @param extension the artifact extension, or {@code null} is unspecified
     * @return coordinate used to point to the artifact
     *
     * @see org.apache.maven.api.services.ArtifactCoordinateFactory#create(Session, String, String, String, String)
     */
    @Nonnull
    ArtifactCoordinate createArtifactCoordinate(String groupId, String artifactId, String version, String extension);

    /**
     * Shortcut for {@code getService(ArtifactFactory.class).create(...)}.
     *
     * @param groupId the group identifier, or {@code null} is unspecified
     * @param artifactId the artifact identifier, or {@code null} is unspecified
     * @param version the artifact version, or {@code null} is unspecified
     * @param classifier the artifact classifier, or {@code null} is unspecified
     * @param extension the artifact extension, or {@code null} is unspecified
     * @param type the artifact type, or {@code null} is unspecified
     * @return coordinate used to point to the artifact
     *
     * @see org.apache.maven.api.services.ArtifactCoordinateFactory#create(Session, String, String, String, String, String, String)
     */
    @Nonnull
    ArtifactCoordinate createArtifactCoordinate(
            String groupId, String artifactId, String version, String classifier, String extension, String type);

    /**
     * Shortcut for {@code getService(ArtifactFactory.class).create(...)}.
     *
     * @param artifact artifact from which to get coordinates
     * @return coordinate used to point to the artifact
     *
     * @see org.apache.maven.api.services.ArtifactCoordinateFactory#create(Session, String, String, String, String, String, String)
     */
    @Nonnull
    ArtifactCoordinate createArtifactCoordinate(@Nonnull Artifact artifact);

    /**
     * Shortcut for {@code getService(DependencyFactory.class).create(...)}.
     *
     * @param coordinate artifact coordinate to get as a dependency coordinate
     * @return dependency coordinate for the given artifact
     *
     * @see DependencyCoordinateFactory#create(Session, ArtifactCoordinate)
     */
    @Nonnull
    DependencyCoordinate createDependencyCoordinate(@Nonnull ArtifactCoordinate coordinate);

    /**
     * Shortcut for {@code getService(DependencyFactory.class).create(...)}.
     *
     * @param dependency dependency for which to get the coordinate
     * @return coordinate for the given dependency
     *
     * @see DependencyCoordinateFactory#create(Session, Dependency)
     */
    @Nonnull
    DependencyCoordinate createDependencyCoordinate(@Nonnull Dependency dependency);

    /**
     * Shortcut for {@code getService(ArtifactFactory.class).create(...)}.
     *
     * @param groupId the group identifier, or {@code null} is unspecified
     * @param artifactId the artifact identifier, or {@code null} is unspecified
     * @param version the artifact version, or {@code null} is unspecified
     * @param extension the artifact extension, or {@code null} is unspecified
     * @return artifact with the given coordinates
     *
     * @see org.apache.maven.api.services.ArtifactFactory#create(Session, String, String, String, String)
     */
    @Nonnull
    Artifact createArtifact(String groupId, String artifactId, String version, String extension);

    /**
     * Shortcut for {@code getService(ArtifactFactory.class).create(...)}.
     *
     * @param groupId the group identifier, or {@code null} is unspecified
     * @param artifactId the artifact identifier, or {@code null} is unspecified
     * @param version the artifact version, or {@code null} is unspecified
     * @param classifier the artifact classifier, or {@code null} is unspecified
     * @param extension the artifact extension, or {@code null} is unspecified
     * @param type the artifact type, or {@code null} is unspecified
     * @return artifact with the given coordinates
     *
     * @see org.apache.maven.api.services.ArtifactFactory#create(Session, String, String, String, String, String, String)
     */
    @Nonnull
    Artifact createArtifact(
            String groupId, String artifactId, String version, String classifier, String extension, String type);

    /**
     * Shortcut for {@code getService(ArtifactResolver.class).resolve(...)}.
     *
     * @param coordinate coordinates of the artifact to resolve
     * @return requested artifact together with the path to its file
     * @throws org.apache.maven.api.services.ArtifactResolverException if the artifact resolution failed
     *
     * @see org.apache.maven.api.services.ArtifactResolver#resolve(Session, Collection)
     */
    @Nonnull
    Map.Entry<Artifact, Path> resolveArtifact(@Nonnull ArtifactCoordinate coordinate);

    /**
     * Shortcut for {@code getService(ArtifactResolver.class).resolve(...)}.
     *
     * @param coordinates coordinates of all artifacts to resolve
     * @return requested artifacts together with the paths to their files
     * @throws org.apache.maven.api.services.ArtifactResolverException if the artifact resolution failed
     *
     * @see org.apache.maven.api.services.ArtifactResolver#resolve(Session, Collection)
     */
    @Nonnull
    Map<Artifact, Path> resolveArtifacts(@Nonnull ArtifactCoordinate... coordinates);

    /**
     * Shortcut for {@code getService(ArtifactResolver.class).resolve(...)}.
     *
     * @param coordinates coordinates of all artifacts to resolve
     * @return requested artifacts together with the paths to their files
     * @throws org.apache.maven.api.services.ArtifactResolverException if the artifact resolution failed
     *
     * @see org.apache.maven.api.services.ArtifactResolver#resolve(Session, Collection)
     */
    @Nonnull
    Map<Artifact, Path> resolveArtifacts(@Nonnull Collection<? extends ArtifactCoordinate> coordinates);

    /**
     * Shortcut for {@code getService(ArtifactResolver.class).resolve(...)}.
     *
     * @param artifact the artifact to resolve
     * @return requested artifact together with the path to its file
     * @throws org.apache.maven.api.services.ArtifactResolverException if the artifact resolution failed
     *
     * @see org.apache.maven.api.services.ArtifactResolver#resolve(Session, Collection)
     */
    @Nonnull
    Map.Entry<Artifact, Path> resolveArtifact(@Nonnull Artifact artifact);

    /**
     * Shortcut for {@code getService(ArtifactResolver.class).resolve(...)}.
     *
     * @param artifacts all artifacts to resolve
     * @return requested artifacts together with the paths to their files
     * @throws org.apache.maven.api.services.ArtifactResolverException if the artifact resolution failed
     *
     * @see org.apache.maven.api.services.ArtifactResolver#resolve(Session, Collection)
     */
    @Nonnull
    Map<Artifact, Path> resolveArtifacts(@Nonnull Artifact... artifacts);

    /**
     * Shortcut for {@code getService(ArtifactInstaller.class).install(...)}.
     *
     * @param artifacts the artifacts to install
     * @throws org.apache.maven.api.services.ArtifactInstallerException if the artifacts installation failed
     *
     * @see org.apache.maven.api.services.ArtifactInstaller#install(Session, Collection)
     */
    void installArtifacts(@Nonnull Artifact... artifacts);

    /**
     * Shortcut for {@code getService(ArtifactInstaller.class).install(...)}.
     *
     * @param artifacts the artifacts to install
     * @throws org.apache.maven.api.services.ArtifactInstallerException if the artifacts installation failed
     *
     * @see org.apache.maven.api.services.ArtifactInstaller#install(Session, Collection)
     */
    void installArtifacts(@Nonnull Collection<Artifact> artifacts);

    /**
     * Shortcut for {@code getService(ArtifactDeployer.class).deploy(...)}.
     *
     * @param repository the repository where to deploy artifacts
     * @param artifacts the artifacts to deploy
     * @throws org.apache.maven.api.services.ArtifactDeployerException if the artifacts deployment failed
     *
     * @see org.apache.maven.api.services.ArtifactDeployer#deploy(Session, RemoteRepository, Collection)
     */
    void deployArtifact(@Nonnull RemoteRepository repository, @Nonnull Artifact... artifacts);

    /**
     * Shortcut for {@code getService(ArtifactManager.class).setPath(...)}.
     *
     * @param artifact the artifact for which to associate a path
     * @param path path to associate to the given artifact
     *
     * @see org.apache.maven.api.services.ArtifactManager#setPath(Artifact, Path)
     */
    void setArtifactPath(@Nonnull Artifact artifact, @Nonnull Path path);

    /**
     * Shortcut for {@code getService(ArtifactManager.class).getPath(...)}.
     *
     * @param artifact the artifact for which to get a path
     * @return path associated to the given artifact
     *
     * @see org.apache.maven.api.services.ArtifactManager#getPath(Artifact)
     */
    @Nonnull
    Optional<Path> getArtifactPath(@Nonnull Artifact artifact);

    /**
     * Gets the relative path for a locally installed artifact. Note that the artifact need not actually exist yet at
     * the returned location, the path merely indicates where the artifact would eventually be stored.
     * <p>
     * Shortcut for {@code getService(LocalArtifactManager.class).getPathForLocalArtitact(...)}.
     *
     * @param artifact the artifact for which to get a local path
     * @return local path associated to the given artifact, or {@code null} if none
     *
     * @see org.apache.maven.api.services.LocalRepositoryManager#getPathForLocalArtifact(Session, LocalRepository, Artifact)
     */
    Path getPathForLocalArtifact(@Nonnull Artifact artifact);

    /**
     * Gets the relative path for an artifact cached from a remote repository.
     * Note that the artifact need not actually exist yet at the returned location,
     * the path merely indicates where the artifact would eventually be stored.
     * <p>
     * Shortcut for {@code getService(LocalArtifactManager.class).getPathForRemoteArtifact(...)}.
     *
     * @param remote the repository from where artifacts are downloaded
     * @param artifact the artifact for which to get a path
     * @return path associated to the given artifact
     *
     * @see org.apache.maven.api.services.LocalRepositoryManager#getPathForRemoteArtifact(Session, LocalRepository, RemoteRepository, Artifact)
     */
    @Nonnull
    Path getPathForRemoteArtifact(@Nonnull RemoteRepository remote, @Nonnull Artifact artifact);

    /**
     * Checks whether a given artifact version is considered a {@code SNAPSHOT} or not.
     * <p>
     * Shortcut for {@code getService(ArtifactManager.class).isSnapshot(...)}.
     * <p>
     * In case there is {@link Artifact} in scope, the recommended way to perform this check is
     * use of {@link Artifact#isSnapshot()} instead.
     *
     * @param version artifact version
     * @return whether the given version is a snapshot
     *
     * @see org.apache.maven.api.services.VersionParser#isSnapshot(String)
     */
    boolean isVersionSnapshot(@Nonnull String version);

    /**
     * Shortcut for {@code getService(DependencyCollector.class).collect(...)}
     *
     * @param artifact artifact for which to get the dependencies, including transitive ones
     * @return root node of the dependency graph for the given artifact
     *
     * @see org.apache.maven.api.services.DependencyCollector#collect(Session, Artifact)
     * @throws org.apache.maven.api.services.DependencyCollectorException if the dependency collection failed
     */
    @Nonnull
    Node collectDependencies(@Nonnull Artifact artifact);

    /**
     * Shortcut for {@code getService(DependencyCollector.class).collect(...)}
     *
     * @param project project for which to get the dependencies, including transitive ones
     * @return root node of the dependency graph for the given project
     *
     * @see org.apache.maven.api.services.DependencyCollector#collect(Session, Project)
     * @throws org.apache.maven.api.services.DependencyCollectorException if the dependency collection failed
     */
    @Nonnull
    Node collectDependencies(@Nonnull Project project);

    /**
     * Collects the transitive dependencies of some artifacts and builds a dependency graph. Note that this operation is
     * only concerned about determining the coordinates of the transitive dependencies and does not actually resolve the
     * artifact files.
     * <p>
     * Shortcut for {@code getService(DependencyCollector.class).resolve(...)}
     *
     * @param dependency dependency for which to get transitive dependencies
     * @return root node of the dependency graph for the given artifact
     *
     * @see org.apache.maven.api.services.DependencyCollector#collect(Session, DependencyCoordinate)
     * @throws org.apache.maven.api.services.DependencyCollectorException if the dependency collection failed
     */
    @Nonnull
    Node collectDependencies(@Nonnull DependencyCoordinate dependency);

    /**
     * Shortcut for {@code getService(DependencyResolver.class).flatten(...)}.
     *
     * @param node node for which to get a flattened list
     * @param scope build path scope (main compile, test compile, etc.) of desired nodes
     * @return flattened list of node with the given build path scope
     * @throws org.apache.maven.api.services.DependencyResolverException if the dependency flattening failed
     *
     * @see org.apache.maven.api.services.DependencyResolver#flatten(Session, Node, PathScope)
     *
     * @todo Does the returned list include the given node?
     */
    @Nonnull
    List<Node> flattenDependencies(@Nonnull Node node, @Nonnull PathScope scope);

    /**
     * Shortcut for {@code getService(DependencyResolver.class).resolve(...).getPaths()}.
     *
     * @param dependencyCoordinate coordinate of the dependency for which to get the paths
     * @return paths to the transitive dependencies of the given dependency
     *
     * @see org.apache.maven.api.services.DependencyResolver#resolve(Session, DependencyCoordinate)
     *
     * @todo Does the returned list include the path to the given dependency?
     */
    @Nonnull
    List<Path> resolveDependencies(@Nonnull DependencyCoordinate dependencyCoordinate);

    /**
     * Shortcut for {@code getService(DependencyResolver.class).resolve(...).getPaths()}.
     *
     * @param dependencyCoordinates coordinates of all dependency for which to get the paths
     * @return paths to the transitive dependencies of the given dependencies
     *
     * @see org.apache.maven.api.services.DependencyResolver#resolve(Session, List)
     *
     * @todo Does the returned list include the path to the given dependencies?
     */
    @Nonnull
    List<Path> resolveDependencies(@Nonnull List<DependencyCoordinate> dependencyCoordinates);

    /**
     * Shortcut for {@code getService(DependencyResolver.class).resolve(...).getPaths()}.
     *
     * @param project the project for which to get dependencies
     * @param scope build path scope (main compile, test compile, etc.) of desired paths
     * @return paths to the transitive dependencies of the given project
     *
     * @see org.apache.maven.api.services.DependencyResolver#resolve(Session, Project, PathScope)
     */
    @Nonnull
    List<Path> resolveDependencies(@Nonnull Project project, @Nonnull PathScope scope);

    /**
     * Shortcut for {@code getService(DependencyResolver.class).resolve(...).getDispatchedPaths()}.
     *
     * @param dependencyCoordinate coordinate of the dependency for which to get the paths
     * @param scope build path scope (main compile, test compile, etc.) of desired paths
     * @param desiredTypes the type of paths to include in the result
     * @return paths to the transitive dependencies of the given project
     *
     * @see org.apache.maven.api.services.DependencyResolver#resolve(Session, Project, PathScope)
     */
    @Nonnull
    Map<PathType, List<Path>> resolveDependencies(
            @Nonnull DependencyCoordinate dependencyCoordinate,
            @Nonnull PathScope scope,
            @Nonnull Collection<PathType> desiredTypes);

    /**
     * Shortcut for {@code getService(DependencyResolver.class).resolve(...).getDispatchedPaths()}.
     *
     * @param project the project for which to get dependencies
     * @param scope build path scope (main compile, test compile, etc.) of desired paths
     * @param desiredTypes the type of paths to include in the result
     * @return paths to the transitive dependencies of the given project
     *
     * @see org.apache.maven.api.services.DependencyResolver#resolve(Session, Project, PathScope)
     */
    @Nonnull
    Map<PathType, List<Path>> resolveDependencies(
            @Nonnull Project project, @Nonnull PathScope scope, @Nonnull Collection<PathType> desiredTypes);

    /**
     * Resolves an artifact's meta version (if any) to a concrete version. For example, resolves "1.0-SNAPSHOT"
     * to "1.0-20090208.132618-23" or "RELEASE"/"LATEST" to "2.0".
     * <p>
     * Shortcut for {@code getService(VersionResolver.class).resolve(...)}
     *
     * @param artifact the artifact for which to resolve the version
     * @return resolved version of the given artifact
     * @throws org.apache.maven.api.services.VersionResolverException if the resolution failed
     *
     * @see org.apache.maven.api.services.VersionResolver#resolve(Session, ArtifactCoordinate) (String)
     */
    @Nonnull
    Version resolveVersion(@Nonnull ArtifactCoordinate artifact);

    /**
     * Expands a version range to a list of matching versions, in ascending order.
     * For example, resolves "[3.8,4.0)" to "3.8", "3.8.1", "3.8.2".
     * The returned list of versions is only dependent on the configured repositories and their contents.
     * The supplied request may also refer to a single concrete version rather than a version range.
     * In this case though, the result contains simply the (parsed) input version, regardless of the
     * repositories and their contents.
     *
     * @param artifact the artifact for which to resolve the versions
     * @return a list of resolved {@code Version}s.
     * @throws org.apache.maven.api.services.VersionRangeResolverException if the resolution failed
     * @see org.apache.maven.api.services.VersionRangeResolver#resolve(Session, ArtifactCoordinate) (String)
     */
    @Nonnull
    List<Version> resolveVersionRange(@Nonnull ArtifactCoordinate artifact);

    /**
     * Parses the specified version string, for example "1.0".
     * <p>
     * Shortcut for {@code getService(VersionParser.class).parseVersion(...)}.
     *
     * @param version the version string to parse
     * @return the version parsed from the given string
     * @throws org.apache.maven.api.services.VersionParserException if the parsing failed
     * @see org.apache.maven.api.services.VersionParser#parseVersion(String)
     */
    @Nonnull
    Version parseVersion(@Nonnull String version);

    /**
     * Parses the specified version range specification, for example "[1.0,2.0)".
     * <p>
     * Shortcut for {@code getService(VersionParser.class).parseVersionRange(...)}.
     *
     * @param versionRange the version string to parse
     * @return the version range parsed from the given string
     * @throws org.apache.maven.api.services.VersionParserException if the parsing failed
     * @see org.apache.maven.api.services.VersionParser#parseVersionRange(String)
     */
    @Nonnull
    VersionRange parseVersionRange(@Nonnull String versionRange);

    /**
     * Parses the specified version constraint specification, for example "1.0" or "[1.0,2.0)".
     * <p>
     * Shortcut for {@code getService(VersionParser.class).parseVersionConstraint(...)}.
     *
     * @param versionConstraint the version string to parse
     * @return the version constraint parsed from the given string
     * @throws org.apache.maven.api.services.VersionParserException if the parsing failed
     * @see org.apache.maven.api.services.VersionParser#parseVersionConstraint(String)
     */
    @Nonnull
    VersionConstraint parseVersionConstraint(@Nonnull String versionConstraint);

    Type requireType(String id);

    Language requireLanguage(String id);

    Packaging requirePackaging(String id);

    ProjectScope requireProjectScope(String id);

    DependencyScope requireDependencyScope(String id);

    PathScope requirePathScope(String id);
}
