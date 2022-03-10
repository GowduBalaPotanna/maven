package org.apache.maven.api;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;

import org.apache.maven.api.services.ArtifactDeployer;
import org.apache.maven.api.services.ArtifactDeployerException;
import org.apache.maven.api.services.ArtifactFactory;
import org.apache.maven.api.services.ArtifactFactoryException;
import org.apache.maven.api.services.ArtifactInstaller;
import org.apache.maven.api.services.ArtifactInstallerException;
import org.apache.maven.api.services.ArtifactResolver;
import org.apache.maven.api.services.ArtifactResolverException;
import org.apache.maven.api.services.ArtifactResolverResult;
import org.apache.maven.api.services.DependencyCollector;
import org.apache.maven.api.services.DependencyCollectorException;
import org.apache.maven.api.services.DependencyCollectorResult;
import org.apache.maven.api.services.DependencyFactory;
import org.apache.maven.api.services.DependencyResolver;
import org.apache.maven.api.services.DependencyResolverException;
import org.apache.maven.api.services.DependencyResolverResult;
import org.apache.maven.api.services.LocalRepositoryManager;
import org.apache.maven.api.services.RepositoryFactory;
import org.apache.maven.api.services.Service;
import org.apache.maven.settings.Settings;

/**
 * The session to install / deploy / resolve artifacts and dependencies.
 */
@ThreadSafe
public interface Session
{

    @Nonnull
    Settings getSettings();

    @Nonnull
    LocalRepository getLocalRepository();

    @Nonnull
    List<RemoteRepository> getRemoteRepositories();

    @Nonnull
    SessionData getData();

    @Nonnull
    Properties getUserProperties();

    @Nonnull
    Properties getSystemProperties();

    /**
     * Retrieves the service for the interface
     *
     * @throws NoSuchElementException if the service could not be found
     */
    @Nonnull
    <T extends Service> T getService( Class<T> clazz ) throws NoSuchElementException;

    /**
     * Creates a derived session using the given local repository.
     *
     * @param localRepository the new local repository
     * @return the derived session
     */
    @Nonnull
    Session withLocalRepository( @Nonnull LocalRepository localRepository );

    /**
     * Creates a derived session using the given remote repositories.
     *
     * @param repositories the new list of remote repositories
     * @return the derived session
     */
    @Nonnull
    Session withRemoteRepositories( @Nonnull List<RemoteRepository> repositories );

    /**
     * Register the given listener which will receive all events.
     *
     * @param listener the listener to register
     */
    void registerListener( @Nonnull Listener listener );

    /**
     * Unregisters a previously registered listener.
     *
     * @param listener the listener to unregister
     */
    void unregisterListener( @Nonnull Listener listener );

    /**
     * Returns the list of registered listeners.
     *
     * @return an immutable collection of listeners
     */
    @Nonnull
    Collection<Listener> getListeners();

    /**
     * Shortcut for <code>getService(RepositoryFactory.class).createLocal(...)</code>
     */
    default LocalRepository createLocalRepository( Path path )
            throws ArtifactFactoryException, IllegalArgumentException
    {
        return getService( RepositoryFactory.class ).createLocal( path );
    }

    /**
     * Shortcut for <code>getService(ArtifactFactory.class).create(...)</code>
     */
    default Artifact createArtifact( String groupId, String artifactId, String version, String extension )
            throws ArtifactFactoryException, IllegalArgumentException
    {
        return getService( ArtifactFactory.class )
                .create( this, groupId, artifactId, version, extension );
    }

    /**
     * Shortcut for <code>getService(ArtifactFactory.class).create(...)</code>
     */
    default Artifact createArtifact( String groupId, String artifactId, String version, String classifier,
                                     String extension, String type )
            throws ArtifactFactoryException, IllegalArgumentException
    {
        return getService( ArtifactFactory.class )
                .create( this, groupId, artifactId, version, classifier, extension, type );
    }

    /**
     * Shortcut for <code>getService(ArtifactResolver.class).resolve(...)</code>
     */
    default ArtifactResolverResult resolveArtifact( Artifact artifact )
            throws ArtifactResolverException, IllegalArgumentException
    {
        return getService( ArtifactResolver.class )
                .resolve( this, artifact );
    }

    /**
     * Shortcut for <code>getService(ArtifactResolver.class).install(...)</code>
     */
    default void installArtifact( Artifact... artifacts )
        throws ArtifactInstallerException, IllegalArgumentException
    {
        getService( ArtifactInstaller.class )
                .install( this, Arrays.asList( artifacts ) );
    }

    /**
     * Shortcut for <code>getService(ArtifactResolver.class).deploy(...)</code>
     */
    default void deployArtifact( RemoteRepository repository, Artifact... artifacts )
        throws ArtifactDeployerException, IllegalArgumentException
    {
        getService( ArtifactDeployer.class )
                .deploy( this, repository, Arrays.asList( artifacts ) );
    }

    /**
     * Shortcut for <code>getService(DependencyFactory.class).create(...)</code>
     */
    default Dependency createDependency( Artifact artifact )
    {
        return getService( DependencyFactory.class )
                .create( this, artifact );
    }

    /**
     * Shortcut for <code>getService(DependencyCollector.class).collect(...)</code>
     */
    default DependencyCollectorResult collectDependencies( Artifact artifact )
            throws DependencyCollectorException, IllegalArgumentException
    {
        return getService( DependencyCollector.class )
                .collect( this, artifact );
    }

    /**
     * Shortcut for <code>getService(DependencyCollector.class).collect(...)</code>
     */
    default DependencyCollectorResult collectDependencies( Project project )
            throws DependencyCollectorException, IllegalArgumentException
    {
        return getService( DependencyCollector.class )
                .collect( this, project );
    }

    /**
     * Shortcut for <code>getService(DependencyCollector.class).collect(...)</code>
     */
    default DependencyCollectorResult collectDependencies( Dependency dependency )
            throws DependencyCollectorException, IllegalArgumentException
    {
        return getService( DependencyCollector.class )
                .collect( this, dependency );
    }

    /**
     * Shortcut for <code>getService(DependencyResolver.class).resolve(...)</code>
     */
    default DependencyResolverResult resolveDependencies( Dependency dependency )
            throws DependencyResolverException, IllegalArgumentException
    {
        return getService( DependencyResolver.class )
                .resolve( this, dependency, null );
    }

    default Path getPathForLocalArtifact( Artifact artifact )
    {
        return getService( LocalRepositoryManager.class )
                .getPathForLocalArtifact( this, getLocalRepository(), artifact );
    }

    default Path getPathForLocalMetadata( Metadata metadata )
    {
        return getService( LocalRepositoryManager.class )
                .getPathForLocalMetadata( this, getLocalRepository(), metadata );
    }

    default Path getPathForRemoteArtifact( RemoteRepository remote, Artifact artifact )
    {
        return getService( LocalRepositoryManager.class )
                .getPathForRemoteArtifact( this, getLocalRepository(), remote, artifact );
    }

    default Path getPathForRemoteMetadata( RemoteRepository remote, Metadata metadata )
    {
        return getService( LocalRepositoryManager.class )
                .getPathForRemoteMetadata( this, getLocalRepository(), remote, metadata );
    }

}