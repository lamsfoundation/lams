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
package org.jboss.cache.statetransfer;

import org.jboss.cache.Fqn;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * This interface handles requests to generate or integrate state from neighbouring caches in a cluster.
 * <p/>
 * This has existed prior to 3.0.0 as a concrete class.  An interface was introduced in 3.0.0 to provide more flexibility
 * in state transfer implementations.
 * <p/>
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 3.0
 */
public interface StateTransferManager
{
   /**
    * Writes the state for the portion of the tree named by <code>fqn</code> to
    * the provided OutputStream.
    * <p/>
    * <p/>
    *
    * @param out            stream to write state to
    * @param fqn            Fqn indicating the uppermost node in the
    *                       portion of the tree whose state should be returned.
    * @param timeout        max number of millis this method should wait to acquire
    *                       any locks, if necessary, on the nodes being transferred
    * @param force          if locks are needed and cannot be acquired after
    *                       <code>timeout</code> millis, should the lock acquisition
    *                       be forced, and any existing transactions holding locks
    *                       on the nodes be rolled back?
    * @param suppressErrors if true, all exceptions are logged but not propagated.
    * @throws Exception in event of error
    */
   void getState(ObjectOutputStream out, Fqn fqn, long timeout, boolean force, boolean suppressErrors) throws Exception;

   /**
    * Set the portion of the cache rooted in <code>targetRoot</code>
    * to match the given state. Updates the contents of <code>targetRoot</code>
    * to reflect those in <code>new_state</code>.
    * <p/>
    * <strong>NOTE:</strong> This method performs no locking of nodes; it
    * is up to the caller to lock <code>targetRoot</code> before calling
    * this method.
    * <p/>
    * This method will use any {@link ClassLoader} needed as defined by the active {@link org.jboss.cache.Region}
    * in the {@link org.jboss.cache.RegionManager}, pertaining to the targetRoot passed in.
    *
    * @param in         an input stream containing the state
    * @param targetRoot fqn of the node into which the state should be integrated
    * @throws Exception In event of error
    */
   void setState(ObjectInputStream in, Fqn targetRoot) throws Exception;

}
