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
package org.jboss.cache.jmx;

import org.jboss.cache.Cache;
import org.jboss.cache.CacheException;
import org.jboss.cache.CacheStatus;
import org.jboss.cache.config.Configuration;
import org.jgroups.Address;

import java.util.List;

/**
 * JMX interface to the {@link org.jboss.cache.Cache}.  Full access to the cache is not supported, only a certain
 * set of operations are exposed via JMX:
 * <p/>
 * <ol>
 * <li> Lifecycle methods - create, start, stop, destroy</li>
 * <li> Configuration (read-only) getter - which retrieves a String (or formatted HTML for web based JMX consoles) representation of the configuration</li>
 * <li> Properties exposing {@link Configuration} elements</li>
 * <li> Cache information methods (numNodes, numAttributes, lockInfo, printDetails) which print as Strings or as formatted HTML (for web based JMX consoles)</li>
 *
 * @since 2.0.0
 * @deprecated use {@link org.jboss.cache.jmx.JmxRegistrationManager}
 */
@Deprecated
public interface CacheJmxWrapperMBean<K, V> extends LegacyConfiguration
{
   /**
    * The lifecycle method stop has completed
    */
   int STOPPED = 0;
   /**
    * The lifecycle method stop has been invoked
    */
   int STOPPING = 1;
   /**
    * The lifecycle method start has been invoked
    */
   int STARTING = 2;
   /**
    * The lifecycle method start has completed
    */
   int STARTED = 3;
   /**
    * There has been an error during some operation
    */
   int FAILED = 4;
   /**
    * The lifecycle method destroy has completed
    */
   int DESTROYED = 5;
   /**
    * The lifecycle method create has completed
    */
   int CREATED = 6;
   /**
    * The MBean has been instantiated but has not completed MBeanRegistration.postRegister
    */
   int UNREGISTERED = 7;
   /**
    * The MBean has been instantiated and has completed MBeanRegistration.postRegister
    */
   int REGISTERED = 8;

   void create() throws CacheException;

   void start() throws CacheException;

   void stop();

   void destroy();

   /**
    * Gets where this object is in its lifecycle transitions.
    *
    * @return the current status. Will not return <code>null</code>
    */
   CacheStatus getCacheStatus();

   /**
    * Legacy attribute to expose the {@link #getCacheStatus() cache status}
    * in terms of the JBoss AS ServiceMBean values.  This interface does
    * not extend ServiceMBean, but this attribute is retained to provide
    * compatibility with the JBoss AS JSR-77 integration layer.
    *
    * @return the current status, e.g. {@link #STARTED}.
    */
   int getState();

   /**
    * Retrieves a reference to the underlying {@link Cache}
    */
   Cache<K, V> getCache();

   /**
    * @return an immutable configuration
    */
   Configuration getConfiguration();

   /**
    * @return a string based representation of the configuration
    */
   String printConfigurationAsString();

   /**
    * @return an HTML formatted string based representation of the configuration
    */
   String printConfigurationAsHtmlString();

   /**
    * @return details of nodes in the cache
    */
   String printCacheDetails();

   /**
    * @return details of nodes in the cache, formatted as HTML
    */
   String printCacheDetailsAsHtml();

   /**
    * Returns the local address of this cache in a cluster, or <code>null</code>
    * if running in local mode.
    *
    * @return the local address of this cache in a cluster, or <code>null</code>
    *         if running in local mode.
    */
   Address getLocalAddress();

   /**
    * Returns a list of members in the cluster, or <code>null</code>
    * if running in local mode.
    *
    * @return a {@link List} of members in the cluster, or <code>null</code>
    *         if running in local mode.
    */
   List<Address> getMembers();

   /**
    * @return number of nodes in the cache
    */
   int getNumberOfNodes();

   /**
    * @return number of attributes in the cache
    */
   int getNumberOfAttributes();

   /**
    * @return information on the state of node locks
    */
   String printLockInfo();

   /**
    * @return information on the state of node locks, formatted as HTML
    */
   String printLockInfoAsHtml();

   /**
    * Gets whether this object should register the cache's interceptors
    * with JMX during {@link #create()}.
    * <p/>
    * Default is <code>true</code>.
    */
   boolean getRegisterJmxResource();

   /**
    * Sets whether this object should register the cache's interceptors
    * with JMX during {@link #create()}.
    * <p/>
    * Default is <code>true</code>.
    */
   void setRegisterJmxResource(boolean register);
}
