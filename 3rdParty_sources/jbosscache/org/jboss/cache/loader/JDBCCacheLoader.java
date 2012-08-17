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

import net.jcip.annotations.ThreadSafe;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.Fqn;
import org.jboss.cache.config.CacheLoaderConfig;
import org.jboss.cache.config.CacheLoaderConfig.IndividualCacheLoaderConfig;
import org.jboss.cache.marshall.NodeData;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JDBC implementation of <tt>AdjListJDBCCacheLoader</tt>.
 * Represents a faster alternative than JDBCCacheLoaderOld and relies on the same database structrure.
 * It is backward compatible with data created by existing <tt>JDBCCacheLoaderOld</tt> implemetation.
 * All configuration elements described there {@link org.jboss.cache.loader.JDBCCacheLoaderOld} also apply for this
 * implementation.
 * <p/>
 * <p/>
 * Additional configuration info: <br>
 * <ul>
 * <li>
 * cache.jdbc.batch.enable: whether or not to use batching on repetitive operations (e.g. inserts during state transfer).
 * Enabling batching should give an important performance boost. It might be required to disable this if the JDBC driver
 * does not support batching. Default set to 'true'
 * </li>
 * <li>
 * cache.jdbc.batch.size: number of operations afer which the batching buffer will be flushed. If 'cache.jdbc.batch.enable'
 * is false, this will be ignored. Default value is 1000.
 * </li>
 * </ul>
 *
 * @author Mircea.Markus@iquestint.com
 * @author <a href="mailto:galder.zamarreno@jboss.com">Galder Zamarreno</a>
 * @version 1.0
 */
@ThreadSafe
public class JDBCCacheLoader extends AdjListJDBCCacheLoader
{
   private static final Log log = LogFactory.getLog(JDBCCacheLoader.class);

   private JDBCCacheLoaderConfig config;

   /**
    * Builds a AdjListJDBCCacheLoaderConfig based on the supplied base config.
    */
   @Override
   protected AdjListJDBCCacheLoaderConfig processConfig(CacheLoaderConfig.IndividualCacheLoaderConfig base)
   {
      if (base instanceof JDBCCacheLoaderConfig)
      {
         config = (JDBCCacheLoaderConfig) base;
      }
      else
      {
         config = new JDBCCacheLoaderConfig(base);
      }
      return config;
   }

   /**
    * As per interface's contract.
    * Performance Note: Optimised O(nodeDepth) db calls.
    */
   public Object put(Fqn name, Object key, Object value) throws Exception
   {
      lock.acquireLock(name, true);
      try
      {
         Map<Object, Object> existing = loadNode(name);

         if (existing == null)
         {
            // do not use a singleton map here since this is serialized and stored in the DB.
            Map<Object, Object> m = new HashMap<Object, Object>(1);
            m.put(key, value);
            addNewSubtree(name, m);
            return null;
         }

         if (existing == NULL_NODE_IN_ROW)
         {
            // do not use a singleton map here since this is serialized and stored in the DB.
            Map<Object, Object> m = new HashMap<Object, Object>(1);
            m.put(key, value);
            updateNode(name, m);
            return null;
         }

         //creation sequence important - we need to overwrite old values
         Object oldVal = existing.put(key, value);
         updateNode(name, existing);
         return oldVal;
      }
      finally
      {
         lock.releaseLock(name);
      }
   }

   /**
    * As per interface's contract.
    * Performance Note: Optimised O(nodeDepth) db calls.
    */
   public void put(Fqn name, Map attributes) throws Exception
   {
      lock.acquireLock(name, true);
      Map toStore = attributes;
      if (toStore != null && !(toStore instanceof HashMap)) toStore = new HashMap(attributes);
      try
      {
         if (!exists(name))
         {
            addNewSubtree(name, toStore);
         }
         else
         {
            updateNode(name, toStore);
         }
      }
      finally
      {
         lock.releaseLock(name);
      }
   }

   @Override
   protected void storeStateHelper(Fqn subtree, List nodeData, boolean moveToBuddy) throws Exception
   {
      lock.acquireLock(subtree, true);
      Connection con = null;
      PreparedStatement ps = null;
      boolean autocommitPrev = true;//this initialization is here only for making code compilable, ignore it
      int batchCaount = 0;
      try
      {
         con = cf.getConnection();
         autocommitPrev = con.getAutoCommit();
         if (config.isBatchEnabled()) con.setAutoCommit(false);
         ps = prepareAndLogStatement(con, config.getInsertNodeSql());
         for (Object aNodeData : nodeData)
         {
            NodeData nd = (NodeData) aNodeData;
            if (nd.isMarker()) break;
            Fqn fqn;
            if (moveToBuddy)
            {
               fqn = buddyFqnTransformer.getBackupFqn(subtree, nd.getFqn());
            }
            else
            {
               fqn = nd.getFqn();
            }

            Map attributes = nd.getAttributes() == null ? null : new HashMap(nd.getAttributes());
            populatePreparedStatementForInsert(fqn, attributes, ps);
            if (!config.isBatchEnabled())
            {
               if (ps.executeUpdate() != 1)
               {
                  throw new IllegalStateException("One and only one row must have been updated!");
               }
            }
            else
            {
               ps.addBatch();
               batchCaount++;
               if (batchCaount >= config.getBatchSize())
               {
                  int result[] = ps.executeBatch();
                  for (int aResult : result)
                  {
                     if (aResult != 1 /* one and only one row must have been updated */
                           && aResult != Statement.SUCCESS_NO_INFO)
                     {
                        throw new IllegalStateException("Failure executing batch insert during state transfer!");
                     }
                  }
                  batchCaount = 0;
               }
            }
         }
         if (batchCaount > 0)
         {
            if (batchCaount > config.getBatchSize())
            {
               throw new IllegalStateException("batchCaount > config.getBatchSize() should never happen!");
            }
            ps.executeBatch();//flush the batch here
         }
      }
      finally
      {
         lock.releaseLock(subtree);
         if (con != null)
         {
            con.setAutoCommit(autocommitPrev);
            safeClose(ps);
            cf.close(con);
         }
      }
   }

   /**
    * As per interface's contrect.
    * Performance Note: O(1) db calls.
    */
   public void remove(Fqn fqn) throws Exception
   {
      Connection conn = null;
      PreparedStatement ps = null;
      try
      {
         conn = cf.getConnection();

         String fqnString = fqn.toString();
         //apend / at the end avoids this issue: 'a/b/cd' is not a child of 'a/b/c'
         String fqnWildcardString = getFqnWildcardString(fqnString, fqn);
         ps = prepareAndLogStatement(conn, config.getDeleteNodeSql(), fqnString, fqnWildcardString);
         lock.acquireLock(fqn, true);
         ps.executeUpdate();
      }
      catch (SQLException e)
      {
         log.error("Failed to remove the node : " + fqn, e);
         throw new IllegalStateException("Failure while removing sub-tree (" + fqn + ")" + e.getMessage());
      }
      finally
      {
         safeClose(ps);
         cf.close(conn);
         lock.releaseLock(fqn);
      }
   }


   /**
    * Subscribes to contract.
    * Performance Note: O(2) db calls.
    */
   @Override
   protected void getNodeDataList(Fqn fqn, List<NodeData> list) throws Exception
   {
      Map nodeAttributes = loadNode(fqn);
      if (nodeAttributes == null)
      {
         return;
      }
      Connection connection = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      try
      {
         connection = cf.getConnection();
         String fqnString = fqn.toString();
         String fqnWildcardString = getFqnWildcardString(fqnString, fqn);
         ps = prepareAndLogStatement(connection, config.getRecursiveChildrenSql(), fqnString, fqnWildcardString);
         rs = ps.executeQuery();

         while (rs.next())
         {
            Map<Object, Object> attributes = readAttributes(rs, 2);
            Fqn path = Fqn.fromString(rs.getString(1));
            NodeData nodeData = (attributes == null || attributes.isEmpty()) ? new NodeData(path) : new NodeData(path, attributes, true);
            list.add(nodeData);
         }
      }
      catch (SQLException e)
      {
         log.error("Failed to load state for node(" + fqn + ") :" + e.getMessage(), e);
         throw new IllegalStateException("Failed to load state for node(" + fqn + ") :" + e.getMessage());
      }
      finally
      {
         safeClose(rs);
         safeClose(ps);
         cf.close(connection);
      }
   }

   private String getFqnWildcardString(String fqnString, Fqn fqn)
   {
      return fqnString + (fqn.isRoot() ? "" : Fqn.SEPARATOR) + '%';
   }

   private Map<Object, Object> readAttributes(ResultSet rs, int index) throws SQLException
   {
      Map<Object, Object> result;
      InputStream is = rs.getBinaryStream(index);
      if (is != null && !rs.wasNull())
      {
         try
         {
            Object marshalledNode = unmarshall(is);
            result = (Map<Object, Object>) marshalledNode;
         }
         catch (Exception e)
         {
            log.error("Failure while reading attribute set from db", e);
            throw new SQLException("Failure while reading attribute set from db " + e);
         }
      }
      else
      {
         result = null;
      }
      return result;
   }

   private void addNewSubtree(Fqn name, Map attributes) throws Exception
   {
      Fqn currentNode = name;
      do
      {
         if (currentNode.equals(name))
         {
            insertNode(currentNode, attributes, false);
         }
         else
         {
            insertNode(currentNode, null, true);
         }
         if (currentNode.isRoot()) break;
         currentNode = currentNode.getParent();
      }
      while (!exists(currentNode));
   }

   @Override
   protected Log getLogger()
   {
      return log;
   }

   /**
    * Start is overwritten for the sake of backward compatibility only.
    * Here is the issue: old implementation does not create a Fqn.ROOT if not specifically told so.
    * As per put's contract, when calling put('/a/b/c', 'key', 'value') all parent nodes should be created up to root.
    * Root is not created, though. The compatibility problem comes in the case of loade ENTIRE state.
    * The implementation checks node's existence firstly, and based on that continues or not. As root is not
    * persisted nothing is loaded etc.
    */
   @Override
   public void start() throws Exception
   {
      super.start();
      if (!exists(Fqn.ROOT) && getNodeCount() > 0)
      {
         put(Fqn.ROOT, null);
      }
   }

   /**
    * Returns a number representing the count of persisted children.
    */
   public int getNodeCount() throws Exception
   {
      Connection conn = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      try
      {
         conn = cf.getConnection();
         ps = prepareAndLogStatement(conn, config.getNodeCountSql());
         rs = ps.executeQuery();
         rs.next();//count(*) will always return one row
         return rs.getInt(1);
      }
      catch (Exception e)
      {
         log.error("Failure while trying to get the count of persisted nodes: " + e.getMessage(), e);
         throw new IllegalStateException("Failure while trying to get the count of persisted nodes: " + e.getMessage());
      }
      finally
      {
         safeClose(rs);
         safeClose(ps);
         cf.close(conn);
      }
   }

   @Override
   public void storeState(Fqn subtree, ObjectInputStream in) throws Exception
   {
      super.storeState(subtree, in);
   }

   public IndividualCacheLoaderConfig getConfig()
   {
      return config;
   }
}
