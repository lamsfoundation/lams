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

import org.jboss.cache.config.Configuration;
import org.jgroups.ChannelFactory;

import java.util.Set;

/**
 * Factory and registry for JBoss Cache instances configured using
 * named configurations.
 *
 * @author <a href="brian.stansberry@jboss.com">Brian Stansberry</a>
 * @version $Revision$
 */
public interface CacheManager
{

   /**
    * Gets the names of all the configurations of which this object
    * is aware.
    *
    * @return a set of names. Will not be <code>null</code>.
    */
   Set<String> getConfigurationNames();

   /**
    * Gets the name of all caches that are registered, i.e. that can be
    * by a call to {@link #getCache(String, boolean) getCache(name, false)}.
    *
    * @return a set of names. Will not be <code>null</code>.
    */
   Set<String> getCacheNames();

   /**
    * Gets the JGroups <code>ChannelFactory</code> that will be injected
    * into any {@link Configuration} that has a
    * {@link Configuration#getMultiplexerStack() multiplexer stack}
    * configured.
    *
    * @return a channel factory
    */
   ChannelFactory getChannelFactory();

   /**
    * Get a cache configured according to the given configuration name,
    * optionally instantiating the cache if it hasn't already been
    * instantiated.
    * <p>
    * The caller is free to invoke the {@link Cache#create()} and
    * {@link Cache#start()} lifecycle methods on the returned cache, but
    * the @link Cache#stop()} and {@link Cache#destroy()} methods should not
    * be invoked, since it is quite possible other users are still using the
    * cache. Use {@link #releaseCache(String)} to notify this
    * registry that the caller is no longer using a cache; let the registry
    * control stopping and destroying the cache.
    * </p>
    * <p>
    * If invoking this method leads to the instantiation of the cache,
    * <code>create()</code> and <code>start()</code> will not be invoked
    * on the cache before it is returned.
    * </p>
    *
    * @param configName the name of the configuration
    * @param create     should the cache be instantiated if it
    *                   hasn't already been?
    * @return the cache, or <code>null</code> if
    *         <code>create</code> is false and the cache hasn't
    *         been created previously.
    * @throws IllegalArgumentException if this object is unaware of
    *                                  <code>configName</code>; i.e. the set
    *                                  returned from {@link #getConfigurationNames()}
    *                                  does not include <code>configName</code>
    * @throws Exception                if there is a problem instantiating the cache
    */
   Cache<Object, Object> getCache(String configName, boolean create) throws Exception;

   /**
    * Notifies the registry that the caller is no longer using the given
    * cache.  The registry may perform cleanup operations, such as
    * stopping and destroying the cache.
    *
    * @param configName
    */
   void releaseCache(String configName);

}