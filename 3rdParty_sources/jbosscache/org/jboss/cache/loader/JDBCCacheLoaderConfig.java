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

import org.jboss.cache.config.CacheLoaderConfig.IndividualCacheLoaderConfig;

import java.util.Properties;

/**
 * Builds the different SQLs needed by <tt>JDBCCacheLoader</tt>.
 *
 * @author Mircea.Markus@iquestint.com
 * @author <a href="mailto:galder.zamarreno@jboss.com">Galder Zamarreno</a>
 * @version 1.0
 */
public class JDBCCacheLoaderConfig extends AdjListJDBCCacheLoaderConfig
{
   private static final long serialVersionUID = -8371846151643130271L;

   private String recursiveChildrenSql;
   private String nodeCountSql;

   private boolean batchEnabled = true;//by default enable batching during state transfer

   private long batchSize = 1000; //default state transfer batch size

   public JDBCCacheLoaderConfig(IndividualCacheLoaderConfig base)
   {
      super(base);
      setClassName(JDBCCacheLoader.class.getName());
   }

   public JDBCCacheLoaderConfig()
   {
      setClassName(JDBCCacheLoader.class.getName());
   }

   @Override
   public void setProperties(Properties props)
   {
      super.setProperties(props);
      deleteNodeSql = constructDeleteNodeSql();
      recursiveChildrenSql = constructRecursiveChildrenSql();
      nodeCountSql = constructNodeCountSql();

      batchEnabled = Boolean.valueOf(props.getProperty("cache.jdbc.batch.enable"));
      if (props.containsKey("cache.jdbc.batch.size"))
      {
         batchSize = Long.parseLong(props.getProperty("cache.jdbc.batch.size"));
      }
   }

   /**
    * Returns the sql string for removing a node and all its children.
    */
   @Override
   public String getDeleteNodeSql()
   {
      if (deleteNodeSql == null) deleteNodeSql = constructDeleteNodeSql();

      return deleteNodeSql;
   }

   /**
    * Returns an sql that will return a node and all its children.
    */
   public String getRecursiveChildrenSql()
   {
      if (recursiveChildrenSql == null) recursiveChildrenSql = constructRecursiveChildrenSql();

      return recursiveChildrenSql;
   }

   public void setRecursiveChildrenSql(String recursiveChildrenSql)
   {
      this.recursiveChildrenSql = recursiveChildrenSql;
   }

   /**
    * Returns an sql that will count all the persisted node.
    */
   public String getNodeCountSql()
   {
      if (nodeCountSql == null) nodeCountSql = constructNodeCountSql();

      return nodeCountSql;
   }

   public void setNodeCountSql(String nodeCountSql)
   {
      this.nodeCountSql = nodeCountSql;
   }

   @Deprecated
   public String getSqlConcat()
   {
      return "";
   }

   @Deprecated
   public void setSqlConcat(String sqlConcat)
   {
   }

   /**
    * If batch is enabled certain operations (e.g. state transfer) will use {@link java.sql.PreparedStatement#addBatch(String)}
    * approach for insertig data into the database. This normally brings significant performance improvements.
    *
    * @return
    */
   public boolean isBatchEnabled()
   {
      return batchEnabled;
   }

   /**
    * The statement will be flushed after batching batchSize operations.
    *
    * @see #isBatchEnabled()
    */
   public long getBatchSize()
   {
      return batchSize;
   }

   private String constructNodeCountSql()
   {
      return "SELECT COUNT(*) FROM " + table;
   }

   private String constructRecursiveChildrenSql()
   {
      return "SELECT " + fqnColumn + "," + nodeColumn + " FROM " + table + " WHERE " + fqnColumn + " = ? OR " + fqnColumn + " LIKE ?";
   }

   @Override
   protected String constructDeleteNodeSql()
   {
      return "DELETE FROM " + table + " WHERE " + fqnColumn + " = ? OR " + fqnColumn + " LIKE ?";
   }
}