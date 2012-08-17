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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.CacheSPI;
import org.jboss.cache.Fqn;
import org.jboss.cache.InternalNode;
import org.jboss.cache.Node;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.Version;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.factories.annotations.Start;
import org.jboss.cache.loader.CacheLoader;
import org.jboss.cache.marshall.NodeData;
import org.jboss.cache.marshall.NodeDataExceptionMarker;

import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Deprecated
public class LegacyStateTransferGenerator implements StateTransferGenerator
{

   public static final short STATE_TRANSFER_VERSION = Version.getVersionShort("2.0.0.GA");

   private Log log = LogFactory.getLog(getClass().getName());

   private CacheSPI cache;

   private Set<Fqn> internalFqns;

   @Inject
   public void inject(CacheSPI cache)
   {
      this.cache = cache;
   }

   @Start(priority = 18)
   void start()
   {
      this.internalFqns = cache.getInternalFqns();
   }

   public void generateState(ObjectOutputStream out, Object rootNode, boolean generateTransient,
                             boolean generatePersistent, boolean suppressErrors) throws Exception
   {
      Fqn fqn = getFqn(rootNode);
      try
      {
         cache.getMarshaller().objectToObjectStream(STATE_TRANSFER_VERSION, out);
         if (generateTransient)
         {
            //transient + marker
            if (log.isTraceEnabled())
            {
               log.trace("writing transient state for " + fqn);
            }
            marshallTransientState((NodeSPI) rootNode, out);
            delimitStream(out);

            if (log.isTraceEnabled())
            {
               log.trace("transient state succesfully written");
            }

            //associated + marker
            if (log.isTraceEnabled())
            {
               log.trace("writing associated state");
            }

            delimitStream(out);

            if (log.isTraceEnabled())
            {
               log.trace("associated state succesfully written");
            }

         }
         else
         {
            //we have to write two markers for transient and associated
            delimitStream(out);
            delimitStream(out);
         }

         CacheLoader cacheLoader = cache.getCacheLoaderManager() == null ? null : cache.getCacheLoaderManager().getCacheLoader();
         if (cacheLoader != null && generatePersistent)
         {
            if (log.isTraceEnabled())
            {
               log.trace("writing persistent state for " + fqn + ",using " + cache.getCacheLoaderManager().getCacheLoader().getClass());
            }

            if (fqn.isRoot())
            {
               cacheLoader.loadEntireState(out);
            }
            else
            {
               cacheLoader.loadState(fqn, out);
            }

            if (log.isTraceEnabled())
            {
               log.trace("persistent state succesfully written");
            }
         }
         delimitStream(out);
      }
      catch (Exception e)
      {
         cache.getMarshaller().objectToObjectStream(new NodeDataExceptionMarker(e, cache.getLocalAddress()), out);
         throw e;
      }
   }

   private Fqn getFqn(Object o)
   {
      if (o instanceof Node) return ((Node) o).getFqn();
      if (o instanceof InternalNode) return ((InternalNode) o).getFqn();
      throw new IllegalArgumentException();
   }

   /**
    * Places a delimiter marker on the stream
    *
    * @param out stream
    * @throws java.io.IOException if there are errs
    */
   protected void delimitStream(ObjectOutputStream out) throws Exception
   {
      cache.getMarshaller().objectToObjectStream(DefaultStateTransferManager.STREAMING_DELIMITER_NODE, out);
   }

   /**
    * Do a preorder traversal: visit the node first, then the node's children
    *
    * @param out
    * @throws Exception
    */
   protected void marshallTransientState(NodeSPI node, ObjectOutputStream out) throws Exception
   {
      List<NodeData> nodeData = new LinkedList<NodeData>();
      generateNodeDataList(node, nodeData);
      cache.getMarshaller().objectToObjectStream(nodeData, out, node.getFqn());
   }

   protected void generateNodeDataList(NodeSPI<?, ?> node, List<NodeData> list) throws Exception
   {
      if (internalFqns.contains(node.getFqn()))
      {
         return;
      }

      Map attrs;
      NodeData nd;

      // first handle the current node
      attrs = node.getInternalState(false);

      if (attrs.size() == 0)
      {
         nd = new NodeData(node.getFqn());
      }
      else
      {
         nd = new NodeData(node.getFqn(), attrs, true);
      }

      list.add(nd);

      // then visit the children
      for (NodeSPI child : node.getChildrenDirect()) generateNodeDataList(child, list);
   }
}