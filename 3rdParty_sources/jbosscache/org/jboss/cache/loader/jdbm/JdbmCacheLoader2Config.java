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
package org.jboss.cache.loader.jdbm;

import org.jboss.cache.config.CacheLoaderConfig.IndividualCacheLoaderConfig;

/**
 * Configuration for {@link JdbmCacheLoader2}.
 * 
 * @author Elias Ross
 */
public class JdbmCacheLoader2Config extends JdbmCacheLoaderConfig
{
   
   private static final long serialVersionUID = 8905490360516820352L;

   /**
    * Constructs a new JdbmCacheLoader2Config.
    */
   public JdbmCacheLoader2Config()
   {
      super();
   }

   /**
    * Constructs a new JdbmCacheLoader2Config.
    */
   public JdbmCacheLoader2Config(IndividualCacheLoaderConfig base)
   {
      super(base);
   }

   @Override
   void setClassName()
   {
      setClassName(JdbmCacheLoader2.class.getName());
   }

}