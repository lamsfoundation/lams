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
import org.jboss.cache.Fqn;
import org.jboss.cache.Modification;
import org.jboss.cache.config.CacheLoaderConfig;
import org.jboss.cache.io.ByteBuffer;
import org.jboss.cache.lock.StripedLock;
import org.jboss.cache.util.Util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Adjacency List Model is the model of persisting trees in which each children holds a reference to its parent.
 * An alternative model is the Nested Set Model (a.k.a. Modified Preorder Model) - this approach adds some additional
 * indexing information to each persisted node. This indexing info is further used for optimizing operations like
 * subtree loading, deleting etc. The indexes are update for each insertion.
 * <p/>
 * Adjacency List Model proved more performance-effective for the following reason: the entire path is persisted rather
 * than only a reference to parent. Looking up nodes heavily relies on that, and the performance is similar as in the
 * case of Modified Preorder Model. Even more there is no costly update indexes operation.
 *
 * @author Mircea.Markus@iquestint.com
 * @author <a href="mailto:galder.zamarreno@jboss.com">Galder Zamarreno</a>
 * @version 1.0
 */
@ThreadSafe
public abstract class AdjListJDBCCacheLoader extends AbstractCacheLoader
{
   protected ConnectionFactory cf;
   protected String driverName;
   private AdjListJDBCCacheLoaderConfig config;
   protected StripedLock lock = new StripedLock(128);
   // dummy, used for serializing empty maps.  One should NOT use Collections.emptyMap() here since if an emptyMap is
   // serialized, upon deserialization it cannot be added to.
   private static final Map<Object, Object> EMPTY_HASHMAP = new HashMap<Object, Object>(0, 1);

   /**
    * Creates a prepared statement using the given connection and SQL string, logs the statement that is about to be
    * executed to the logger, and optionally sets String parameters provided on the prepared statement before
    * returning the prepared statement.
    *
    * @param conn   Connection to use to create the prepared statement
    * @param sql    SQL to use with the prepared statement
    * @param params optional parameters to add to the statement.
    * @return a prepared statement
    * @throws Exception if there are problems
    */
   protected PreparedStatement prepareAndLogStatement(Connection conn, String sql, String... params) throws Exception
   {
      PreparedStatement ps = conn.prepareStatement(sql);
      for (int i = 0; i < params.length; i++) ps.setString(i + 1, params[i]);

      // Logging the SQL we plan to run
      if (getLogger().isTraceEnabled())
      {
         StringBuilder sb = new StringBuilder("Executing SQL statement [");
         sb.append(sql).append("]");
         if (params.length != 0)
         {
            sb.append(" with params ");
            boolean first = true;
            for (String param : params)
            {
               if (first)
               {
                  first = false;
               }
               else
               {
                  sb.append(", ");
               }
               sb.append("[").append(param).append("]");
            }
         }

         getLogger().trace(sb.toString());
      }
      return ps;
   }

   public void setConfig(CacheLoaderConfig.IndividualCacheLoaderConfig base)
   {
      config = processConfig(base);

      if (config.getDatasourceName() == null)
      {
         try
         {
            /* Instantiate an standalone connection factory as per configuration, either explicitly
       defined or the default one */
            getLogger().debug("Initialising with a connection factory since data source is not provided.");
            if (getLogger().isDebugEnabled())
            {
               getLogger().debug("Using connection factory " + config.getConnectionFactoryClass());
            }
            cf = (ConnectionFactory) Util.loadClass(config.getConnectionFactoryClass()).newInstance();
         }
         catch (Exception e)
         {
            getLogger().error("Connection factory class could not be loaded", e);
            throw new IllegalStateException("Connection factory class could not be loaded", e);
         }
      }
      else
      {
         /* We create the ManagedConnectionFactory instance but the JNDI lookup is no done until
the start method is called, since that's when its registered in its lifecycle */
         cf = new ManagedConnectionFactory();
      }
      /* Regardless of the type of connection factory, we set the configuration */
      cf.setConfig(config);
   }


   /**
    * Returns a map representing a node.
    *
    * @param name node's fqn
    * @return node
    * @throws Exception
    */
   public Map<Object, Object> get(Fqn name) throws Exception
   {
      lock.acquireLock(name, false);
      try
      {
         final Map<Object, Object> node = loadNode(name);
         return node == NULL_NODE_IN_ROW ? new HashMap<Object, Object>(0) : node;
      }
      finally
      {
         lock.releaseLock(name);
      }
   }

   /**
    * Fetches child node names (not pathes).
    *
    * @param fqn parent fqn
    * @return a set of child node names or null if there are not children found for the fqn
    * @throws Exception
    */
   public Set<String> getChildrenNames(Fqn fqn) throws Exception
   {
      Set<String> children = null;
      Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      try
      {
         con = cf.getConnection();
         ps = prepareAndLogStatement(con, config.getSelectChildNamesSql(), fqn.toString());
         lock.acquireLock(fqn, false);
         rs = ps.executeQuery();
         if (rs.next())
         {
            children = new HashSet<String>();
            do
            {
               String child = rs.getString(1);
               int slashInd = child.lastIndexOf('/');
               String name = child.substring(slashInd + 1);
               //Fqn childFqn = Fqn.fromString(child);
               //String name = (String) childFqn.get(childFqn.size() - 1);
               children.add(name);
            }
            while (rs.next());
         }
      }
      catch (SQLException e)
      {
         reportAndRethrowError("Failed to get children names for fqn " + fqn, e);
      }
      finally
      {
         safeClose(rs);
         safeClose(ps);
         cf.close(con);
         lock.releaseLock(fqn);
      }

      return children == null ? null : Collections.unmodifiableSet(children);
   }


   /**
    * Nullifies the node.
    *
    * @param name node's fqn
    * @throws Exception
    */
   public void removeData(Fqn name) throws Exception
   {
      updateNode(name, null);
   }

   /**
    * First phase in transaction commit process. The changes are committed if only one phase if requested.
    * All the modifications are committed using the same connection.
    *
    * @param tx            something representing transaction
    * @param modifications a list of modifications
    * @param one_phase     indicates whether it's one or two phase commit transaction
    * @throws Exception
    */
   @Override
   public void prepare(Object tx, List<Modification> modifications, boolean one_phase) throws Exception
   {
      // start a tx
      cf.prepare(tx);
      put(modifications);
      // commit if it's one phase only
      if (one_phase) commit(tx);
   }

   /**
    * Commits a transaction.
    *
    * @param tx the tx to commit
    * @throws Exception
    */
   @Override
   public void commit(Object tx) throws Exception
   {
      cf.commit(tx);
   }

   /**
    * Rolls back a transaction.
    *
    * @param tx the tx to rollback
    */
   @Override
   public void rollback(Object tx)
   {
      cf.rollback(tx);
   }

   // Service implementation

   @Override
   public void start() throws Exception
   {
      cf.start();

      Connection con = null;
      Statement st = null;

      try
      {
         con = cf.getConnection();
         driverName = getDriverName(con);
         if (config.getCreateTable() && !tableExists(config.getTable(), con))
         {
            if (getLogger().isDebugEnabled())
            {
               getLogger().debug("executing ddl: " + config.getCreateTableDDL());
            }
            st = con.createStatement();
            st.executeUpdate(config.getCreateTableDDL());
         }
      }
      finally
      {
         safeClose(st);
         cf.close(con);
      }

      createDummyTableIfNeeded();
   }

   private void createDummyTableIfNeeded() throws Exception
   {
      Connection conn = null;
      PreparedStatement ps = null;
      try
      {
         conn = cf.getConnection();
         ps = prepareAndLogStatement(conn, config.getDummyTableRemovalDDL());
         ps.execute();
      }
      catch (Exception e)
      {
         if (getLogger().isTraceEnabled()) getLogger().trace("No need to drop tables!");
      }
      finally
      {
         safeClose(ps);
         cf.close(conn);
      }

      try
      {
         conn = cf.getConnection();
         ps = prepareAndLogStatement(conn, config.getDummyTableCreationDDL());
         ps.execute();
         safeClose(ps);
         ps = prepareAndLogStatement(conn, config.getDummyTablePopulationSql());
         ps.execute();
      }
      finally
      {
         safeClose(ps);
         cf.close(conn);
      }
   }

   @Override
   public void stop()
   {
      try
      {
         if (config.getDropTable())
         {
            Connection con = null;
            Statement st = null;
            try
            {
               if (getLogger().isDebugEnabled())
               {
                  getLogger().debug("executing ddl: " + config.getDropTableDDL());
               }

               con = cf.getConnection();
               st = con.createStatement();
               st.executeUpdate(config.getDropTableDDL());
               safeClose(st);
            }
            catch (SQLException e)
            {
               getLogger().error("Failed to drop table: " + e.getMessage(), e);
            }
            finally
            {
               safeClose(st);
               cf.close(con);
            }
         }
      }
      finally
      {
         cf.stop();
      }
   }

   /**
    * Checks that there is a row for the fqn in the database.
    *
    * @param name node's fqn
    * @return true if there is a row in the database for the given fqn even if the node column is null.
    * @throws Exception
    */
   public boolean exists(Fqn name) throws Exception
   {
      lock.acquireLock(name, false);
      Connection conn = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      try
      {
         conn = cf.getConnection();
         ps = prepareAndLogStatement(conn, config.getExistsSql(), name.toString());
         rs = ps.executeQuery();
         return rs.next();
      }
      finally
      {
         lock.releaseLock(name);
         safeClose(rs);
         safeClose(ps);
         cf.close(conn);
      }
   }

   /**
    * Removes attribute's value for a key. If after removal the node contains no attributes, the node is nullified.
    *
    * @param name node's name
    * @param key  attribute's key
    * @return removed value or null if there was no value for the passed in key
    * @throws Exception
    */
   public Object remove(Fqn name, Object key) throws Exception
   {
      lock.acquireLock(name, true);
      try
      {
         Object removedValue = null;
         Map<Object, Object> node = loadNode(name);
         if (node != null && node != NULL_NODE_IN_ROW)
         {
            removedValue = node.remove(key);
            if (node.isEmpty())
            {
               updateNode(name, null);
            }
            else
            {
               updateNode(name, node);
            }
         }
         return removedValue;
      }
      finally
      {
         lock.releaseLock(name);
      }
   }


   /**
    * Loads a node from the database.
    *
    * @param name the fqn
    * @return non-null Map representing the node,
    *         null if there is no row with the fqn in the table,
    *         NULL_NODE_IN_ROW if there is a row in the table with the fqn but the node column contains null.
    */
   @SuppressWarnings("unchecked")
   protected Map<Object, Object> loadNode(Fqn name)
   {
      boolean rowExists = false;
      Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      try
      {
         con = cf.getConnection();
         ps = prepareAndLogStatement(con, config.getSelectNodeSql(), name.toString());
         rs = ps.executeQuery();

         if (rs.next())
         {
            rowExists = true;
            InputStream is = rs.getBinaryStream(1);
            if (is != null && !rs.wasNull())
            {
               try
               {
                  // deserialize result
                  return (Map<Object, Object>) unmarshall(is);
               }
               catch (Exception e)
               {
                  throw new Exception("Unable to load to deserialize result: ", e);
               }
               finally
               {
                  safeClose(is);
               }
            }
         }
      }
      catch (Exception e)
      {
         reportAndRethrowError("Failed to load node for fqn " + name, e);
      }
      finally
      {
         safeClose(rs);
         safeClose(ps);
         cf.close(con);
      }

      return rowExists ? NULL_NODE_IN_ROW : null;
   }


   /**
    * Inserts a node into the database
    *
    * @param name        the fqn
    * @param dataMap     the node
    * @param rowMayExist if true, then this method will not be strict in testing for 1 row being inserted, since 0 may be inserted if the row already exists.
    */
   protected void insertNode(Fqn name, Map dataMap, boolean rowMayExist)
   {
      Connection con = null;
      PreparedStatement ps = null;
      try
      {
         con = cf.getConnection();
         ps = prepareAndLogStatement(con, config.getInsertNodeSql());

         populatePreparedStatementForInsert(name, dataMap, ps);

         int rows = ps.executeUpdate();
         if (!rowMayExist && rows != 1)
         {
            throw new IllegalStateException("Expected one insert row but got " + rows);
         }
      }
      catch (RuntimeException e)
      {
         throw e;
      }
      catch (Exception e)
      {
         getLogger().error("Failed to insert node :" + e.getMessage());
         throw new IllegalStateException("Failed to insert node: " + e.getMessage(), e);
      }
      finally
      {
         safeClose(ps);
         cf.close(con);
      }
   }

   /**
    * Expects a PreparedStatement binded to {@link org.jboss.cache.loader.JDBCCacheLoaderConfig#getInsertNodeSql()}
    */
   protected void populatePreparedStatementForInsert(Fqn name, Map dataMap, PreparedStatement ps)
         throws Exception
   {
      String fqnString = name.toString();
      ps.setString(1, fqnString);

      if (dataMap != null)
      {
         ByteBuffer byteBuffer = marshall(dataMap);
         ps.setBinaryStream(2, byteBuffer.getStream(), byteBuffer.getLength());
      }
      else
      {
         // a hack to handles the incomp. of SQL server jdbc driver prior to SQL SERVER 2005
         if (driverName != null && (driverName.contains("SQLSERVER")
               || driverName.contains("POSTGRESQL")
               || driverName.contains("JCONNECT")))
         {
            ps.setNull(2, Types.LONGVARBINARY);
         }
         else
         {
            ps.setNull(2, Types.BLOB);
         }
         //ps.setNull(2, Types.LONGVARBINARY);
      }

      if (name.size() == 0)
      {
         ps.setNull(3, Types.VARCHAR);
      }
      else
      {
         ps.setString(3, name.getAncestor(name.size() - 1).toString());
      }

      // and a repeat - the 4th param is the same as the 1st one.
      ps.setString(4, fqnString);
   }


   /**
    * Updates a node in the database.
    *
    * @param name the fqn
    * @param node new node value
    */
   protected void updateNode(Fqn name, Map<Object, Object> node)
   {
      Connection con = null;
      PreparedStatement ps = null;
      try
      {
         con = cf.getConnection();
         ps = prepareAndLogStatement(con, config.getUpdateNodeSql());

         if (node == null) node = EMPTY_HASHMAP;

         ByteBuffer byteBuffer = marshall(node);
         ps.setBinaryStream(1, byteBuffer.getStream(), byteBuffer.getLength());

         ps.setString(2, name.toString());

         /*int rows = */
         ps.executeUpdate();
      }
      catch (Exception e)
      {
         reportAndRethrowError("Failed to update node for fqn " + name, e);
      }
      finally
      {
         safeClose(ps);
         cf.close(con);
      }
   }

   protected String getDriverName(Connection con)
   {
      if (con == null) return null;
      try
      {
         DatabaseMetaData dmd = con.getMetaData();
         return toUpperCase(dmd.getDriverName());
      }
      catch (SQLException e)
      {
         // This should not happen. A J2EE compatiable JDBC driver is
         // required to fully support metadata.
         throw new IllegalStateException("Error while getting the driver name", e);
      }
   }

   static String getRequiredProperty(Properties props, String name)
   {
      String value = props.getProperty(name);
      if (value == null)
      {
         throw new IllegalStateException("Missing required property: " + name);
      }
      return value;
   }

   protected boolean tableExists(String tableName, Connection con)
   {
      ResultSet rs = null;
      try
      {
         // (a j2ee spec compatible jdbc driver has to fully
         // implement the DatabaseMetaData)
         DatabaseMetaData dmd = con.getMetaData();
         String catalog = con.getCatalog();
         String schema = null;
         String quote = dmd.getIdentifierQuoteString();
         if (tableName.startsWith(quote))
         {
            if (!tableName.endsWith(quote))
            {
               throw new IllegalStateException("Mismatched quote in table name: " + tableName);
            }
            int quoteLength = quote.length();
            tableName = tableName.substring(quoteLength, tableName.length() - quoteLength);
            if (dmd.storesLowerCaseQuotedIdentifiers())
            {
               tableName = toLowerCase(tableName);
            }
            else if (dmd.storesUpperCaseQuotedIdentifiers())
            {
               tableName = toUpperCase(tableName);
            }
         }
         else
         {
            if (dmd.storesLowerCaseIdentifiers())
            {
               tableName = toLowerCase(tableName);
            }
            else if (dmd.storesUpperCaseIdentifiers())
            {
               tableName = toUpperCase(tableName);
            }
         }

         int dotIndex;
         if ((dotIndex = tableName.indexOf('.')) != -1)
         {
            // Yank out schema name ...
            schema = tableName.substring(0, dotIndex);
            tableName = tableName.substring(dotIndex + 1);
         }

         rs = dmd.getTables(catalog, schema, tableName, null);
         return rs.next();
      }
      catch (SQLException e)
      {
         // This should not happen. A J2EE compatiable JDBC driver is
         // required fully support metadata.
         throw new IllegalStateException("Error while checking if table aleady exists " + tableName, e);
      }
      finally
      {
         safeClose(rs);
      }
   }


   protected abstract Log getLogger();

   protected abstract AdjListJDBCCacheLoaderConfig processConfig(CacheLoaderConfig.IndividualCacheLoaderConfig base);

   protected void reportAndRethrowError(String message, Exception cause) throws IllegalStateException
   {
      getLogger().error(message, cause);
      throw new IllegalStateException(message, cause);
   }

   protected void safeClose(InputStream is)
   {
      if (is != null)
      {
         try
         {
            is.close();
         }
         catch (IOException e)
         {
            getLogger().warn("Failed to close input stream: " + e.getMessage());
         }
      }
   }

   protected void safeClose(Statement st)
   {
      if (st != null)
      {
         try
         {
            st.close();
         }
         catch (SQLException e)
         {
            getLogger().warn("Failed to close statement: " + e.getMessage());
         }
      }
   }

   protected void safeClose(ResultSet rs)
   {
      if (rs != null)
      {
         try
         {
            rs.close();
         }
         catch (SQLException e)
         {
            getLogger().warn("Failed to close result set: " + e.getMessage());
         }
      }
   }

   protected Object unmarshall(InputStream from) throws Exception
   {
      return getMarshaller().objectFromStream(from);
   }

   protected ByteBuffer marshall(Object obj) throws Exception
   {
      return getMarshaller().objectToBuffer(obj);
   }

   private static String toUpperCase(String s)
   {
      return s.toUpperCase(Locale.ENGLISH);
   }

   private static String toLowerCase(String s)
   {
      return s.toLowerCase((Locale.ENGLISH));
   }

   // Inner

   protected static final Map<Object, Object> NULL_NODE_IN_ROW = new AbstractMap<Object, Object>()
   {

      @Override
      public Set<java.util.Map.Entry<Object, Object>> entrySet()
      {
         throw new UnsupportedOperationException();
      }

   };

}
