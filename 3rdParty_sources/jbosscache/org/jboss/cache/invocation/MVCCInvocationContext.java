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
package org.jboss.cache.invocation;

import org.jboss.cache.Fqn;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.transaction.MVCCTransactionContext;
import org.jboss.cache.transaction.TransactionContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * An invocation context that is specific to MVCC locking
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 3.0
 */
public class MVCCInvocationContext extends InvocationContext
{
   private HashMap<Fqn, NodeSPI> lookedUpNodes = null;
   private MVCCTransactionContext mvccTCtx;

   @Override
   public void setTransactionContext(TransactionContext transactionContext)
   {
      super.setTransactionContext(transactionContext);
      mvccTCtx = (MVCCTransactionContext) transactionContext;
   }

   /**
    * Retrieves a node from the registry of looked up nodes in the current scope.
    * <p/>
    * If a transaction is in progress, implementations should delegate to {@link org.jboss.cache.transaction.MVCCTransactionContext#lookUpNode(Fqn)}
    * <p/>
    *
    * @param fqn fqn to look up
    * @return a node, or null if it cannot be found.
    */
   public NodeSPI lookUpNode(Fqn fqn)
   {
      if (mvccTCtx != null) return mvccTCtx.lookUpNode(fqn);
      return lookedUpNodes == null ? null : lookedUpNodes.get(fqn);
   }

   /**
    * Puts an entry in the registry of looked up nodes in the current scope.
    * <p/>
    * If a transaction is in progress, implementations should delegate to {@link org.jboss.cache.transaction.MVCCTransactionContext#putLookedUpNode(Fqn, NodeSPI)}
    * <p/>
    *
    * @param f fqn to add
    * @param n node to add
    */
   public void putLookedUpNode(Fqn f, NodeSPI n)
   {
      if (mvccTCtx != null)
         mvccTCtx.putLookedUpNode(f, n);
      else
      {
         if (lookedUpNodes == null) lookedUpNodes = new HashMap<Fqn, NodeSPI>(4);
         lookedUpNodes.put(f, n);
      }
   }

   public void putLookedUpNodes(Map<Fqn, NodeSPI> lookedUpNodes)
   {
      if (mvccTCtx != null)
         mvccTCtx.putLookedUpNodes(lookedUpNodes);
      else
      {
         if (this.lookedUpNodes == null)
            this.lookedUpNodes = new HashMap<Fqn, NodeSPI>(lookedUpNodes);
         else
            lookedUpNodes.putAll(lookedUpNodes);
      }
   }

   /**
    * Clears the registry of looked up nodes.
    * <p/>
    * If a transaction is in progress, implementations should delegate to {@link org.jboss.cache.transaction.MVCCTransactionContext#clearLookedUpNodes()}.
    */
   public void clearLookedUpNodes()
   {
      // TODO: see if we can reinstate common behaviour once we have the ICI calling ctx.reset() instead of ctx.clearLookedUpNodes()
//      if (mvccTCtx != null)
//         mvccTCtx.clearLookedUpNodes();
//      else

      if (lookedUpNodes != null) lookedUpNodes.clear();
   }

   /**
    * Retrieves a map of nodes looked up within the current invocation's scope.
    * <p/>
    * If a transaction is in progress, implementations should delegate to {@link org.jboss.cache.transaction.MVCCTransactionContext#getLookedUpNodes()}.
    * <p/>
    *
    * @return a map of looked up nodes.
    */
   @SuppressWarnings("unchecked")
   public Map<Fqn, NodeSPI> getLookedUpNodes()
   {
      if (mvccTCtx != null) return mvccTCtx.getLookedUpNodes();
      return (Map<Fqn, NodeSPI>) (lookedUpNodes == null ? Collections.emptyMap() : lookedUpNodes);
   }

   @Override
   public void reset()
   {
      super.reset();
      if (lookedUpNodes != null)
      {
         lookedUpNodes.clear();
         lookedUpNodes = null;
      }
   }

   @SuppressWarnings("unchecked")
   public InvocationContext copy()
   {
      MVCCInvocationContext copy = new MVCCInvocationContext();
      doCopy(copy);
      if (lookedUpNodes != null) copy.lookedUpNodes = (HashMap<Fqn, NodeSPI>) lookedUpNodes.clone();
      return copy;
   }
}
