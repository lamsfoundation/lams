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
package org.jboss.cache.mvcc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.DataContainer;
import org.jboss.cache.Fqn;
import org.jboss.cache.InternalNode;
import org.jboss.cache.InvocationContext;
import static org.jboss.cache.mvcc.ReadCommittedNode.Flags.CHANGED;
import static org.jboss.cache.mvcc.ReadCommittedNode.Flags.DELETED;
import org.jboss.cache.optimistic.DataVersioningException;

/**
 * A node delegate that encapsulates repeatable read semantics when writes are initiated, committed or rolled back.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 3.0
 */
public class RepeatableReadNode extends ReadCommittedNode
{
   private static final Log log = LogFactory.getLog(RepeatableReadNode.class);

   public RepeatableReadNode(InternalNode node, InternalNode parent)
   {
      super(node, parent);
   }

   @Override
   public void markForUpdate(DataContainer container, boolean writeSkewCheck)
   {
      if (isFlagSet(CHANGED)) return; // already copied

      Fqn fqn = getFqn();

      // mark node as changed.
      setFlag(CHANGED);

      if (writeSkewCheck)
      {
         // check for write skew.
         InternalNode underlyingNode = container.peekInternalNode(fqn, true);

         if (underlyingNode != null && underlyingNode != node)
         {
            String errormsg = new StringBuilder().append("Detected write skew on Fqn [").append(fqn).append("].  Another process has changed the node since we last read it!").toString();
            if (log.isWarnEnabled()) log.warn(errormsg + ".  Unable to copy node for update.");
            throw new DataVersioningException(errormsg);
         }
      }

      // make a backup copy
      backup = node;
      node = copyNode(backup);
   }
   
   private InternalNode copyNode(InternalNode nodeToCopy)
   {
      return nodeToCopy == null ? null : nodeToCopy.copy();
   }

   @Override
   @SuppressWarnings("unchecked")
   protected void updateNode(Fqn fqn, InvocationContext ctx, DataContainer dataContainer)
   {
      if (fqn.isRoot())
      {
         dataContainer.setRoot(node);
      }
      else if (!isFlagSet(DELETED))
      {
         InternalNode parent = lookupParent(fqn, ctx, dataContainer);
         parent.addChild(node, true);  // we know this is safe since we calculated the parent from the child.  No need to have the parent re-do checks when adding the child again.
      }
   }
}
