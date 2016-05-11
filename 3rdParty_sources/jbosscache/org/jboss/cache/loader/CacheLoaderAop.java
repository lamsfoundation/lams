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
package org.jboss.cache.loader;

import org.jboss.cache.Fqn;

/**
 * Responsible for storing and retrieving objects to/from secondary storage.
 *
 * @author Bela Ban Oct 31, 2003
 *
 */
public interface CacheLoaderAop extends CacheLoader
{

   /**
    * Loads an object from a persistent store.
    *
    * @param name The key under which the object is stored
    * @return The object
    * @throws Exception Thrown if the object cannot be loaded
    */
   Object loadObject(Fqn name) throws Exception;

   /**
    * Stores an object under a given key in the persistent store. If the object is already present, it will
    * be overwritten
    *
    * @param name
    * @param pojo
    * @throws Exception
    */
   void storeObject(Fqn name, Object pojo) throws Exception;

   /**
    * Removes the object with the given key from the persistent store.
    *
    * @param name
    * @throws Exception
    */
   void removeObject(Fqn name) throws Exception;
}
