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
package org.jboss.cache.lock;

import org.jboss.cache.InvocationContext;
import org.jboss.cache.NodeSPI;

import java.util.Collection;

/**
 * An abstract lock manager that deals with Fqns rather than nodes.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 3.0
 */
public abstract class FqnLockManager extends AbstractLockManager
{
   public boolean lock(NodeSPI node, LockType lockType, Object owner) throws InterruptedException
   {
      return lock(node.getFqn(), lockType, owner);
   }

   public boolean lock(NodeSPI node, LockType lockType, Object owner, long timeout) throws InterruptedException
   {
      return lock(node.getFqn(), lockType, owner, timeout);
   }

   public boolean lockAndRecord(NodeSPI node, LockType lockType, InvocationContext ctx) throws InterruptedException
   {
      return lockAndRecord(node.getFqn(), lockType, ctx);
   }

   public void unlock(NodeSPI node, Object owner)
   {
      unlock(node.getFqn(), owner);
   }

   public boolean ownsLock(NodeSPI node, Object owner)
   {
      return ownsLock(node.getFqn(), owner);
   }

   public Object getWriteOwner(NodeSPI node)
   {
      return getWriteOwner(node.getFqn());
   }

   public Collection<Object> getReadOwners(NodeSPI node)
   {
      return getReadOwners(node.getFqn());
   }

   public boolean isLocked(NodeSPI node)
   {
      return isLocked(node.getFqn());
   }
}
