/*
  * JBoss, Home of Professional Open Source
  * Copyright 2007, JBoss Inc., and individual contributors as indicated
  * by the @authors tag. See the copyright.txt in the distribution for a
  * full listing of individual contributors.
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
package org.jboss.security.identity.plugins;

import org.jboss.security.identity.Identity;

/**
 * Interface for an <code>Identity</code> persistence strategy (file, db, etc.).
 * 
 * @author <a href="mmoyses@redhat.com">Marcus Moyses</a>
 * @version $Revision: 1.1 $
 */
public interface PersistenceStrategy
{

   /**
    * Persists the <code>Identity</code> in the backend.
    * 
    * @param identity <code>Identity</code> to be persisted.
    * @return the persisted <code>Identity</code> or <code>null</code> if persistence failed.
    */
   public Identity persistIdentity(Identity identity);

   /**
    * Retrieves an <code>Identity</code> from the backend.
    * 
    * @param name unique name of the <code>Identity</code>.
    * @return the <code>Identity</code> or <code>null</code> if not found.
    */
   public Identity getIdentity(String name);

   /**
    * Updates the <code>Identity</code> in the backend.
    * 
    * @param identity <code>Identity</code> to be updated.
    * @return the updated <code>Identity</code> or <code>null</code> if the update was not successful.
    */
   public Identity updateIdentity(Identity identity);

   /**
    * Removes an <code>Identity</code> from the backend.
    * 
    * @param identity <code>Identity</code> to be removed. 
    * @return <code>true</code> if successfully removed, <code>false</code> otherwise.
    */
   public boolean removeIdentity(Identity identity);

}
