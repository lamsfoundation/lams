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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.CacheException;
import org.jboss.cache.CacheSPI;
import org.jboss.cache.Fqn;
import org.jboss.cache.Modification;
import org.jboss.cache.Modification.ModificationType;
import org.jboss.cache.Region;
import org.jboss.cache.RegionManager;
import org.jboss.cache.buddyreplication.BuddyFqnTransformer;
import org.jboss.cache.buddyreplication.BuddyManager;
import org.jboss.cache.marshall.Marshaller;
import org.jboss.cache.marshall.NodeData;
import org.jboss.cache.marshall.NodeDataExceptionMarker;
import org.jboss.cache.marshall.NodeDataMarker;
import org.jboss.cache.util.Immutables;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A convenience abstract implementation of a {@link CacheLoader}.  Specific methods to note are methods like
 * {@link #storeState(org.jboss.cache.Fqn,java.io.ObjectInputStream)}, {@link #loadState(org.jboss.cache.Fqn,java.io.ObjectOutputStream)},
 * {@link #storeEntireState(java.io.ObjectInputStream)} and {@link #loadEntireState(java.io.ObjectOutputStream)} which have convenience
 * implementations here.
 * <p/>
 * Also useful to note is the implementation of {@link #put(java.util.List)}, used during the prepare phase of a transaction.
 * <p/>
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani (manik AT jboss DOT org)</a>
 * @author <a href="mailto:galder.zamarreno@jboss.com">Galder Zamarreno</a>
 * @since 2.0.0
 */
public abstract class AbstractCacheLoader implements CacheLoader
{
   protected CacheSPI cache;
   protected RegionManager regionManager;
   private static final Log log = LogFactory.getLog(AbstractCacheLoader.class);
   private static final boolean trace = log.isTraceEnabled();
   protected BuddyFqnTransformer buddyFqnTransformer = new BuddyFqnTransformer();
   /**
    * HashMap<Object,List<Modification>>. List of open transactions. Note that this is purely transient, as
    * we don't use a log, recovery is not available
    */
   protected Map<Object, List<Modification>> transactions = new ConcurrentHashMap<Object, List<Modification>>();

   public void put(Fqn fqn, Map<Object, Object> attributes, boolean erase) throws Exception
   {
      if (erase)
      {
         removeData(fqn);
      }

      // JBCACHE-769 -- make a defensive copy
      Map<Object, Object> attrs = (attributes == null ? null : Immutables.immutableMapCopy(attributes));
      put(fqn, attrs);
   }

   public void storeEntireState(ObjectInputStream is) throws Exception
   {
      storeState(Fqn.ROOT, is);
   }

   public void storeState(Fqn subtree, ObjectInputStream in) throws Exception
   {
      // remove entire existing state
      this.remove(subtree);

      boolean moveToBuddy = subtree.isChildOf(BuddyManager.BUDDY_BACKUP_SUBTREE_FQN) && subtree.size() > 1;

      // store new state
      Object objectFromStream = cache.getMarshaller().objectFromObjectStream(in);
      if (objectFromStream instanceof NodeDataMarker)
      {
         // no persistent state sent across; return?
         if (trace) log.trace("Empty persistent stream?");
         return;
      }
      if (objectFromStream instanceof NodeDataExceptionMarker)
      {
         NodeDataExceptionMarker ndem = (NodeDataExceptionMarker) objectFromStream;
         throw new CacheException("State provider cacheloader at node " + ndem.getCacheNodeIdentity()
               + " threw exception during loadState (see Caused by)", ndem.getCause());
      }

      List nodeData = (List) objectFromStream;
      for (Object aNodeData : nodeData)
      {
         NodeData nd = (NodeData) aNodeData;
         if (nd.isExceptionMarker())
         {
            NodeDataExceptionMarker ndem = (NodeDataExceptionMarker) nd;
            throw new CacheException("State provider cacheloader at node " + ndem.getCacheNodeIdentity()
                  + " threw exception during loadState (see Caused by)", ndem.getCause());
         }
      }
      storeStateHelper(subtree, nodeData, moveToBuddy);
   }

   protected void storeStateHelper(Fqn subtree, List nodeData, boolean moveToBuddy) throws Exception
   {
      List<Modification> mod = new ArrayList<Modification>(nodeData.size());
      for (Object aNodeData : nodeData)
      {
         NodeData nd = (NodeData) aNodeData;
         if (nd.isMarker())
         {
            if (trace) log.trace("Reached delimiter; exiting loop");
            break;
         }
         Fqn fqn;
         if (moveToBuddy)
         {
            fqn = buddyFqnTransformer.getBackupFqn(subtree, nd.getFqn());
         }
         else
         {
            fqn = nd.getFqn();
         }
         if (trace) log.trace("Storing state in Fqn " + fqn);
         mod.add(new Modification(ModificationType.PUT_DATA_ERASE, fqn, nd.getAttributes()));
      }
      prepare(null, mod, true);
   }

   public void loadEntireState(ObjectOutputStream os) throws Exception
   {
      loadState(Fqn.ROOT, os);
   }

   public void loadState(Fqn subtree, ObjectOutputStream os) throws Exception
   {
      loadStateHelper(subtree, os);
   }


   public void setCache(CacheSPI c)
   {
      this.cache = c;
   }

   public void setRegionManager(RegionManager regionManager)
   {
      this.regionManager = regionManager;
   }

   protected void regionAwareMarshall(Fqn fqn, Object toMarshall) throws Exception
   {
      Region r = regionManager == null ? null : regionManager.getValidMarshallingRegion(fqn);
      ClassLoader originalClassLoader = null;
      boolean needToResetLoader = false;
      Thread current = null;

      if (r != null)
      {
         // set the region's class loader as the thread's context classloader
         needToResetLoader = true;
         current = Thread.currentThread();
         originalClassLoader = current.getContextClassLoader();
         current.setContextClassLoader(r.getClassLoader());
      }

      try
      {
         doMarshall(fqn, toMarshall);
      }
      finally
      {
         if (needToResetLoader) current.setContextClassLoader(originalClassLoader);
      }
   }

   protected Object regionAwareUnmarshall(Fqn fqn, Object toUnmarshall) throws Exception
   {
      Region r = regionManager == null ? null : regionManager.getValidMarshallingRegion(fqn);
      ClassLoader originalClassLoader = null;
      boolean needToResetLoader = false;
      Thread current = null;

      if (r != null)
      {
         if (trace)
         {
            log.trace("Using region " + r.getFqn() + ", which has registered class loader " + r.getClassLoader() + " as a context class loader.");
         }
         // set the region's class loader as the thread's context classloader
         needToResetLoader = true;
         current = Thread.currentThread();
         originalClassLoader = current.getContextClassLoader();
         current.setContextClassLoader(r.getClassLoader());
      }

      try
      {
         return doUnmarshall(fqn, toUnmarshall);
      }
      finally
      {
         if (needToResetLoader) current.setContextClassLoader(originalClassLoader);
      }
   }

   protected void doMarshall(Fqn fqn, Object toMarshall) throws Exception
   {
      throw new RuntimeException("Should be overridden");
   }

   protected Object doUnmarshall(Fqn fqn, Object toUnmarshall) throws Exception
   {
      throw new RuntimeException("Should be overridden");
   }


   /**
    * Do a preorder traversal: visit the node first, then the node's children
    */
   protected void loadStateHelper(Fqn fqn, ObjectOutputStream out) throws Exception
   {
      List<NodeData> list = new LinkedList<NodeData>();
      getNodeDataList(fqn, list);
      if (trace) log.trace("Loading state of " + list.size() + " nodes into stream");
      cache.getMarshaller().objectToObjectStream(list, out, fqn);
   }

   protected void getNodeDataList(Fqn fqn, List<NodeData> list) throws Exception
   {
      Map<Object, Object> attrs;
      Set<?> childrenNames;
      Object childName;
      Fqn tmpFqn;
      NodeData nd;

      // first handle the current node
      attrs = get(fqn);
      if (attrs == null || attrs.size() == 0)
      {
         nd = new NodeData(fqn);
      }
      else
      {
         nd = new NodeData(fqn, attrs, true);
      }
      //out.writeObject(nd);
      list.add(nd);

      // then visit the children
      childrenNames = getChildrenNames(fqn);
      if (childrenNames == null)
      {
         return;
      }
      for (Object childrenName : childrenNames)
      {
         childName = childrenName;
         tmpFqn = Fqn.fromRelativeElements(fqn, childName);
         if (!cache.getInternalFqns().contains(tmpFqn)) getNodeDataList(tmpFqn, list);
      }
   }

   public void put(List<Modification> modifications) throws Exception
   {
      for (Modification m : modifications)
      {
         switch (m.getType())
         {
            case PUT_DATA:
               put(m.getFqn(), m.getData());
               break;
            case PUT_DATA_ERASE:
               removeData(m.getFqn());
               put(m.getFqn(), m.getData());
               break;
            case PUT_KEY_VALUE:
               put(m.getFqn(), m.getKey(), m.getValue());
               break;
            case REMOVE_DATA:
               removeData(m.getFqn());
               break;
            case REMOVE_KEY_VALUE:
               remove(m.getFqn(), m.getKey());
               break;
            case REMOVE_NODE:
               remove(m.getFqn());
               break;
            case MOVE:
               // involve moving all children too
               move(m.getFqn(), m.getFqn2());
               break;
            default:
               throw new CacheException("Unknown modification " + m.getType());
         }
      }
   }

   protected void move(Fqn fqn, Fqn parent) throws Exception
   {
      Object name = fqn.getLastElement();
      Fqn newFqn = Fqn.fromRelativeElements(parent, name);

      // start deep.
      Set childrenNames = getChildrenNames(fqn);
      if (childrenNames != null)
      {
         for (Object c : childrenNames)
         {
            move(Fqn.fromRelativeElements(fqn, c), newFqn);
         }
      }
      // get data for node.
      Map<Object, Object> data = get(fqn);
      if (data != null)// if null, then the node never existed.  Don't bother removing?
      {
         remove(fqn);
         put(newFqn, data);
      }
   }

   protected Marshaller getMarshaller()
   {
      return cache.getMarshaller();
   }

   // empty implementations for loaders that do not wish to implement lifecycle.
   public void create() throws Exception
   {
   }

   public void start() throws Exception
   {
   }

   public void stop()
   {
   }

   public void destroy()
   {
   }

   // Adds simple transactional capabilities to cache loaders that are inherently non-transactional.  If your cache loader implementation
   // is tansactional though, then override these.

   public void prepare(Object tx, List<Modification> modifications, boolean one_phase) throws Exception
   {
      if (one_phase)
      {
         put(modifications);
      }
      else
      {
         transactions.put(tx, modifications);
      }
   }

   public void commit(Object tx) throws Exception
   {
      List<Modification> modifications = transactions.remove(tx);
      if (modifications == null)
      {
         throw new Exception("transaction " + tx + " not found in transaction table");
      }
      put(modifications);
   }

   public void rollback(Object tx)
   {
      transactions.remove(tx);
   }
}
