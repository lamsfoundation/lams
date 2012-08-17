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
package org.jboss.cache.transaction;

import org.jboss.cache.Fqn;
import org.jboss.cache.NodeSPI;

import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import java.util.HashMap;
import java.util.Map;

/**
 * A transaction context specially geared to dealing with MVCC.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 3.0
 */
public class MVCCTransactionContext extends AbstractTransactionContext
{
   private final Map<Fqn, NodeSPI> lookedUpNodes = new HashMap<Fqn, NodeSPI>(8);

   public MVCCTransactionContext(Transaction tx) throws SystemException, RollbackException
   {
      super(tx);
   }

   /**
    * Retrieves a node from the registry of looked up nodes in the current scope.
    * <p/>
    * This is not normally called directly since {@link org.jboss.cache.InvocationContext#lookUpNode(org.jboss.cache.Fqn)}
    * would delegate to this method if a transaction is in scope.
    * <p/>
    *
    * @param fqn fqn to look up
    * @return a node, or null if it cannot be found.
    */
   public NodeSPI lookUpNode(Fqn fqn)
   {
      return lookedUpNodes.get(fqn);
   }

   /**
    * Puts an entry in the registry of looked up nodes in the current scope.
    * <p/>
    * This is not normally called directly since {@link org.jboss.cache.InvocationContext#putLookedUpNode(org.jboss.cache.Fqn, org.jboss.cache.NodeSPI)}
    * would delegate to this method if a transaction is in scope.
    * <p/>
    *
    * @param f fqn to add
    * @param n node to add
    */
   public void putLookedUpNode(Fqn f, NodeSPI n)
   {
      lookedUpNodes.put(f, n);
   }

   /**
    * Clears the registry of looked up nodes.
    * <p/>
    * This is not normally called directly since {@link org.jboss.cache.InvocationContext#clearLookedUpNodes()}
    * would delegate to this method if a transaction is in scope.
    * <p/>
    */
   public void clearLookedUpNodes()
   {
      lookedUpNodes.clear();
   }

   /**
    * Retrieves a map of nodes looked up within the current invocation's scope.
    * <p/>
    * This is not normally called directly since {@link org.jboss.cache.InvocationContext#getLookedUpNodes()}
    * would delegate to this method if a transaction is in scope.
    * <p/>
    *
    * @return a map of looked up nodes.
    */
   public Map<Fqn, NodeSPI> getLookedUpNodes()
   {
      return lookedUpNodes;
   }

   @Override
   public void reset()
   {
      super.reset();
      lookedUpNodes.clear();
   }

   public void putLookedUpNodes(Map<Fqn, NodeSPI> lookedUpNodes)
   {
      lookedUpNodes.putAll(lookedUpNodes);
   }
}
