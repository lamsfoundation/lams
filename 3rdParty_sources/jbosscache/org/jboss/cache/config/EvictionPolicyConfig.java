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
package org.jboss.cache.config;

import org.jboss.cache.eviction.EvictionPolicy;
import org.jboss.cache.eviction.EvictionPolicyConfigBase;

/**
 * This class encapsulates the configuration element for an eviction policy.
 * <p/>
 * In its most basic form, it is implemented by {@link EvictionPolicyConfigBase}, but
 * more specific eviction policies may subclass or re-implement this interface
 * to provide access to more config variables.
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani (manik AT jboss DOT org)</a>
 * @deprecated see {@link org.jboss.cache.config.EvictionAlgorithmConfig}
 */
@Deprecated
public interface EvictionPolicyConfig
{
   /**
    * Gets the class name of the {@link EvictionPolicy} implementation
    * this object will configure. Used by {@link org.jboss.cache.RegionManager}
    * to instantiate the policy.
    *
    * @return fully qualified class name
    */
   String getEvictionPolicyClass();

   /**
    * Validate the configuration. Will be called after any configuration
    * properties are set.
    *
    * @throws ConfigurationException if any values for the configuration
    *                                properties are invalid
    */
   void validate() throws ConfigurationException;

   /**
    * Resets the values to their defaults.
    */
   void reset();
}
