/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2000 - 2008, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.cache;

import net.jcip.annotations.ThreadSafe;
import org.jboss.cache.config.Configuration;
import org.jboss.cache.config.ConfigurationException;

import java.io.InputStream;

/**
 * This factory constructs a cache from a given or default configuration set.
 * <p/>
 * Typical usage would be:
 * <p/>
 * <pre>
 *   CacheFactory factory = DefaultCacheFactory.getInstance();
 *   Cache cache = factory.createCache("replSync-service.xml"); // expects this file to be in classpath
 *   cache.stop();
 * </pre>
 * Factory methods provide options for creating a cache using
 * <ul>
 * <li>default configuration settings</li>
 * <li>an XML file containing the configuration</li>
 * <li>a constructed and populated {@link org.jboss.cache.config.Configuration} object</li>
 * </ul>
 * In addition, methods provide anadditional option to create and return a cache without starting it.
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani (manik AT jboss DOT org)</a>
 * @see org.jboss.cache.Cache
 * @see org.jboss.cache.DefaultCacheFactory
 * @since 2.0.0
 */
@ThreadSafe
public interface CacheFactory<K, V>
{
   /**
    * Creates and starts a {@link Cache} instance using default configuration settings.  See {@link Configuration} for default values.
    *
    * @return a cache
    * @throws ConfigurationException if there are problems with the default configuration
    */
   Cache<K, V> createCache() throws ConfigurationException;

   /**
    * Creates and optionally starts a {@link Cache} instance using default configuration settings.  See {@link Configuration} for default values.
    *
    * @param start if true, starts the cache
    * @return a cache
    * @throws ConfigurationException if there are problems with the default configuration
    */
   Cache<K, V> createCache(boolean start) throws ConfigurationException;

   /**
    * Creates and starts a {@link org.jboss.cache.Cache} instance.  The following are all valid calls:
    * <pre>
    *    factory.createCache("myCacheService.xml"); // file is in class path
    *    factory.createCache("etc/myCacheService.xml"); // file is in etc/ relative to the directory you started the JVM
    *    factory.createCache("/home/jbosscache/myCacheService.xml"); // file is in the /home/jbosscache directory
    * </pre>
    *
    * @param configFileName the named XML file should exist in the classpath or should be a fully qualified or relative (to your JVM working directory) path to a file on the local file system.  Note that the classpath is checked first for the existence of this file.
    * @return a running {@link org.jboss.cache.Cache} instance
    * @throws org.jboss.cache.config.ConfigurationException
    *          if there are problems with the configuration
    */
   Cache<K, V> createCache(String configFileName) throws ConfigurationException;

   /**
    * Creates {@link Cache} instance, and optionally starts it.
    *
    * @param configFileName the named XML file should exist in the classpath or should be a fully qualified or relative (to your JVM working directory) path to a file on the local file system.  Note that the classpath is checked first for the existence of this file.
    * @param start          if true, the cache is started before returning.
    * @return an optionally running {@link Cache} instance
    * @throws org.jboss.cache.config.ConfigurationException
    *          if there are problems with the configuration
    * @see #createCache(String) for examples on valid config file names.
    */
   Cache<K, V> createCache(String configFileName, boolean start) throws ConfigurationException;

   /**
    * Creates a {@link Cache} instance based on a {@link org.jboss.cache.config.Configuration} passed in.
    * <p/>
    * Ensure that the Configuration you pass in is not used by another cache instance in the same JVM,
    * as it may be concurrently modified. Clone the configuration, if shared, using
    * {@link org.jboss.cache.config.Configuration#clone()}.
    *
    * @param configuration the {@link Configuration} object that is passed in to setCache the {@link Cache}.
    * @return a running {@link Cache} instance
    * @throws org.jboss.cache.config.ConfigurationException
    *          if there are problems with the configuration
    */
   Cache<K, V> createCache(Configuration configuration) throws ConfigurationException;

   /**
    * Creates {@link Cache} instance, and optionally starts it, based on a {@link org.jboss.cache.config.Configuration} passed in.
    * <p/>
    * Ensure that the Configuration you pass in is not used by another cache instance in the same JVM,
    * as it may be concurrently modified. Clone the configuration, if shared, using
    * {@link org.jboss.cache.config.Configuration#clone()}.
    *
    * @param configuration the {@link Configuration} object that is passed in to setCache the {@link Cache}.
    * @param start         if true, the cache is started before returning.
    * @return an optionally running {@link Cache} instance
    * @throws org.jboss.cache.config.ConfigurationException
    *          if there are problems with the configuration
    */
   Cache<K, V> createCache(Configuration configuration, boolean start) throws ConfigurationException;

   /**
    * Creates a {@link Cache} instance based on an {@link java.io.InputStream} passed in, which should be a stream to a valid
    * XML configuration file.
    *
    * @param is the {@link java.io.InputStream}
    * @return a running {@link Cache} instance
    * @throws org.jboss.cache.config.ConfigurationException
    *          if there are problems with the configuration
    * @since 2.1.0
    */
   Cache<K, V> createCache(InputStream is) throws ConfigurationException;

   /**
    * Creates a {@link Cache} instance based on an {@link java.io.InputStream} passed in, which should be a stream to a valid
    * XML configuration file.
    *
    * @param is    the {@link java.io.InputStream}
    * @param start if true, the cache is started before returning.
    * @return a running {@link Cache} instance
    * @throws org.jboss.cache.config.ConfigurationException
    *          if there are problems with the configuration
    * @since 2.1.0
    */
   Cache<K, V> createCache(InputStream is, boolean start) throws ConfigurationException;
}
