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
import org.jboss.cache.util.Util;

import java.util.Properties;

/**
 * AdjListJDBCCacheLoaderConfig
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani (manik AT jboss DOT org)</a>
 * @author <a href="mailto:galder.zamarreno@jboss.com">Galder Zamarreno</a>
 */
@SuppressWarnings("deprecation")
public class AdjListJDBCCacheLoaderConfig extends IndividualCacheLoaderConfig
{
   /**
    * The serialVersionUID
    */
   private static final long serialVersionUID = -8371846151643130281L;

   private static final boolean CREATE_TABLE_DEFAULT = true;
   private static final boolean DROP_TABLE_DEFAULT = false;
   private static final String PARENT_COLUMN_DEFAULT = "parent";
   private static final String NODE_TYPE_DEFAULT = "BLOB";
   private static final String NODE_COLUMN_DEFAULT = "node";
   private static final String FQN_TYPE_DEFAULT = "VARCHAR(255)";
   private static final String FQN_COLUMN_DEFAULT = "fqn";
   private static final String PRIMARY_KEY_DEFAULT = "jbosscache_pk";
   private static final String TABLE_DEFAULT = "jbosscache";

   protected boolean createTable = CREATE_TABLE_DEFAULT;
   protected String createTableDDL;
   protected String datasourceName;
   protected String deleteAllSql;
   protected String deleteNodeSql;
   protected boolean dropTable = DROP_TABLE_DEFAULT;
   protected String dropTableDDL;
   protected String driverClass;
   protected String insertNodeSql;
   protected String jdbcURL;
   protected String jdbcUser;
   protected String jdbcPassword;
   protected String selectChildFqnsSql;
   protected String selectChildNamesSql;
   protected String selectNodeSql;
   protected String updateNodeSql;
   protected String updateTableSql;
   protected String existsSql;
   protected String connectionFactoryClass;
   protected String primaryKey = PRIMARY_KEY_DEFAULT;
   protected String fqnType = FQN_TYPE_DEFAULT;
   protected String nodeType = NODE_TYPE_DEFAULT;
   protected String parentColumn = PARENT_COLUMN_DEFAULT;
   protected String table = TABLE_DEFAULT;
   protected String nodeColumn = NODE_COLUMN_DEFAULT;
   protected String fqnColumn = FQN_COLUMN_DEFAULT;

   public AdjListJDBCCacheLoaderConfig()
   {
   }

   /**
    * For use by {@link JDBCCacheLoaderOld}.
    *
    * @param base generic config object created by XML parsing.
    */
   AdjListJDBCCacheLoaderConfig(IndividualCacheLoaderConfig base)
   {
      populateFromBaseConfig(base);
   }

   public boolean getCreateTable()
   {
      return createTable;
   }

   public void setCreateTable(boolean createTable)
   {
      testImmutability("createTable");
      this.createTable = createTable;
   }

   public String getCreateTableDDL()
   {
      if (createTableDDL == null)
      {
         setCreateTableDDL(constructCreateTableDDL());
      }
      return createTableDDL;
   }

   public void setCreateTableDDL(String createTableDDL)
   {
      testImmutability("createTableDDL");
      this.createTableDDL = createTableDDL;
   }

   public String getDatasourceName()
   {
      return datasourceName;
   }

   public void setDatasourceName(String datasourceName)
   {
      testImmutability("datasourceName");
      this.datasourceName = datasourceName;
   }

   public String getDeleteAllSql()
   {
      if (deleteAllSql == null)
      {
         setDeleteAllSql(constructDeleteAllSql());
      }
      return deleteAllSql;
   }

   public void setDeleteAllSql(String deleteAllSql)
   {
      testImmutability("deleteAllSql");
      this.deleteAllSql = deleteAllSql;
   }

   public String getDeleteNodeSql()
   {
      if (deleteNodeSql == null)
      {
         setDeleteNodeSql(constructDeleteNodeSql());
      }
      return deleteNodeSql;
   }

   public void setDeleteNodeSql(String deleteNodeSql)
   {
      testImmutability("deleteNodeSql");
      this.deleteNodeSql = deleteNodeSql;
   }

   public String getDriverClass()
   {
      return driverClass;
   }

   public void setDriverClass(String driverClass)
   {
      testImmutability("driverClass");
      this.driverClass = driverClass;
   }

   public boolean getDropTable()
   {
      return dropTable;
   }

   public void setDropTable(boolean dropTable)
   {
      testImmutability("dropTable");
      this.dropTable = dropTable;
   }

   public String getInsertNodeSql()
   {
      if (insertNodeSql == null)
      {
         setInsertNodeSql(constructInsertNodeSql());
      }
      return insertNodeSql;
   }

   public String getExistsSql()
   {
      if (existsSql == null)
      {
         setExistsSql(constructExistsSql());
      }
      return existsSql;
   }

   public void setExistsSql(String existsSql)
   {
      testImmutability("existsSql");
      this.existsSql = existsSql;
   }

   public void setInsertNodeSql(String insertNodeSql)
   {
      testImmutability("insertNodeSql");
      this.insertNodeSql = insertNodeSql;
   }

   public String getSelectChildFqnsSql()
   {
      if (selectChildFqnsSql == null)
      {
         setSelectChildFqnsSql(constructSelectChildNamesSql());
      }
      return selectChildFqnsSql;
   }

   public void setSelectChildFqnsSql(String selectChildFqnsSql)
   {
      testImmutability("selectChildFqnsSql");
      this.selectChildFqnsSql = selectChildFqnsSql;
   }

   public String getSelectNodeSql()
   {
      if (selectNodeSql == null)
      {
         setSelectNodeSql(constructSelectNodeSql());
      }
      return selectNodeSql;
   }

   public void setSelectNodeSql(String selectNodeSql)
   {
      testImmutability("selectNodeSql");
      this.selectNodeSql = selectNodeSql;
   }

   public String getTable()
   {
      return table;
   }

   public void setTable(String table)
   {
      testImmutability("table");
      this.table = table;
   }

   public String getUpdateTableSql()
   {
      return updateTableSql;
   }

   public void setUpdateTableSql(String updateTableSql)
   {
      testImmutability("updateTableSql");
      this.updateTableSql = updateTableSql;
   }

   public String getDropTableDDL()
   {
      if (dropTableDDL == null)
      {
         setDropTableDDL(constructDropTableDDL());
      }
      return dropTableDDL;
   }

   public void setDropTableDDL(String dropTableDDL)
   {
      testImmutability("dropTableDDL");
      this.dropTableDDL = dropTableDDL;
   }

   public String getSelectChildNamesSql()
   {
      if (selectChildNamesSql == null)
      {
         setSelectChildNamesSql(constructSelectChildNamesSql());
      }
      return selectChildNamesSql;
   }

   public void setSelectChildNamesSql(String selectChildNamesSql)
   {
      testImmutability("selectChildNamesSql");
      this.selectChildNamesSql = selectChildNamesSql;
   }

   public String getUpdateNodeSql()
   {
      if (updateNodeSql == null)
      {
         setUpdateNodeSql(constructUpdateNodeSql());
      }
      return updateNodeSql;
   }

   public void setUpdateNodeSql(String updateNodeSql)
   {
      testImmutability("updateNodeSql");
      this.updateNodeSql = updateNodeSql;
   }

   public String getJdbcPassword()
   {
      return jdbcPassword;
   }

   public void setJdbcPassword(String jdbcPassword)
   {
      testImmutability("jdbcPassword");
      this.jdbcPassword = jdbcPassword;
   }

   public String getJdbcURL()
   {
      return jdbcURL;
   }

   public void setJdbcURL(String jdbcURL)
   {
      testImmutability("jdbcURL");
      this.jdbcURL = jdbcURL;
   }

   public String getJdbcUser()
   {
      return jdbcUser;
   }

   public void setJdbcUser(String jdbcUser)
   {
      testImmutability("jdbcUser");
      this.jdbcUser = jdbcUser;
   }

   public String getConnectionFactoryClass()
   {
      return connectionFactoryClass;
   }

   public void setConnectionFactoryClass(String connectionFactoryClass)
   {
      testImmutability("connectionFactoryClass");
      this.connectionFactoryClass = connectionFactoryClass;
   }

   public String getPrimaryKey()
   {
      return primaryKey;
   }

   public void setPrimaryKey(String primaryKey)
   {
      testImmutability("primaryKey");
      this.primaryKey = primaryKey;
   }

   public String getFqnType()
   {
      return fqnType;
   }

   public void setFqnType(String fqnType)
   {
      testImmutability("fqnType");
      this.fqnType = fqnType;
   }

   public String getNodeType()
   {
      return nodeType;
   }

   public void setNodeType(String nodeType)
   {
      testImmutability("nodeType");
      this.nodeType = nodeType;
   }

   public String getParentColumn()
   {
      return parentColumn;
   }

   public void setParentColumn(String parentColumn)
   {
      testImmutability("parentColumn");
      this.parentColumn = parentColumn;
   }

   public String getNodeColumn()
   {
      return nodeColumn;
   }

   public void setNodeColumn(String nodeColumn)
   {
      testImmutability("nodeColumn");
      this.nodeColumn = nodeColumn;
   }

   public String getFqnColumn()
   {
      return fqnColumn;
   }

   public void setFqnColumn(String fqnColumn)
   {
      testImmutability("fqnColumn");
      this.fqnColumn = fqnColumn;
   }

   @Override
   public void setProperties(Properties props)
   {
      super.setProperties(props);
      datasourceName = props.getProperty("cache.jdbc.datasource");
      if (datasourceName == null)
      {
         this.driverClass = AdjListJDBCCacheLoader.getRequiredProperty(props, "cache.jdbc.driver");
         this.jdbcURL = AdjListJDBCCacheLoader.getRequiredProperty(props, "cache.jdbc.url");
         this.jdbcUser = AdjListJDBCCacheLoader.getRequiredProperty(props, "cache.jdbc.user");
         this.jdbcPassword = AdjListJDBCCacheLoader.getRequiredProperty(props, "cache.jdbc.password");

         if (log.isDebugEnabled())
         {
            log.debug("Properties: " +
                  "cache.jdbc.url=" +
                  jdbcURL +
                  ", cache.jdbc.driver=" +
                  driverClass +
                  ", cache.jdbc.user=" +
                  jdbcUser +
                  ", cache.jdbc.password=" +
                  jdbcPassword +
                  ", cache.jdbc.table=" + table);
         }
      }

      String prop = props.getProperty("cache.jdbc.table.create");
      this.createTable = prop == null ? CREATE_TABLE_DEFAULT : Boolean.valueOf(prop);
      prop = props.getProperty("cache.jdbc.table.drop");
      this.dropTable = prop == null ? DROP_TABLE_DEFAULT : Boolean.valueOf(prop);

      this.table = props.getProperty("cache.jdbc.table.name", TABLE_DEFAULT);
      primaryKey = props.getProperty("cache.jdbc.table.primarykey", PRIMARY_KEY_DEFAULT);
      fqnColumn = props.getProperty("cache.jdbc.fqn.column", FQN_COLUMN_DEFAULT);
      fqnType = props.getProperty("cache.jdbc.fqn.type", FQN_TYPE_DEFAULT);
      nodeColumn = props.getProperty("cache.jdbc.node.column", NODE_COLUMN_DEFAULT);
      nodeType = props.getProperty("cache.jdbc.node.type", NODE_TYPE_DEFAULT);
      parentColumn = props.getProperty("cache.jdbc.parent.column", PARENT_COLUMN_DEFAULT);

      selectChildNamesSql = constructSelectChildNamesSql();
      deleteNodeSql = constructDeleteNodeSql();
      deleteAllSql = constructDeleteAllSql();
      /* select child fqns and select child names sql is the same */
      selectChildFqnsSql = constructSelectChildNamesSql();
      insertNodeSql = constructInsertNodeSql();
      updateNodeSql = constructUpdateNodeSql();
      selectNodeSql = constructSelectNodeSql();

      createTableDDL = constructCreateTableDDL();

      dropTableDDL = constructDropTableDDL();
      connectionFactoryClass = props.getProperty("cache.jdbc.connection.factory", "org.jboss.cache.loader.NonManagedConnectionFactory");
   }

   protected String constructDropTableDDL()
   {
      return "DROP TABLE " + table;
   }

   protected String constructCreateTableDDL()
   {
      // removed CONSTRAINT clause as this causes problems with some databases, like Informix.
      return "CREATE TABLE " + table + "(" + fqnColumn + " " + fqnType + " NOT NULL, " + nodeColumn + " " + nodeType +
            ", " + parentColumn + " " + fqnType + ", PRIMARY KEY (" + fqnColumn + "))";
   }

   @Override
   public boolean equals(Object obj)
   {
      if (obj instanceof AdjListJDBCCacheLoaderConfig && equalsExcludingProperties(obj))
      {
         AdjListJDBCCacheLoaderConfig other = (AdjListJDBCCacheLoaderConfig) obj;

         return (this.createTable == other.createTable)
               && Util.safeEquals(createTableDDL, other.createTableDDL)
               && Util.safeEquals(datasourceName, other.datasourceName)
               && Util.safeEquals(deleteAllSql, other.deleteAllSql)
               && Util.safeEquals(deleteNodeSql, other.deleteNodeSql)
               && Util.safeEquals(driverClass, other.driverClass)
               && (dropTable == other.dropTable)
               && Util.safeEquals(dropTableDDL, other.dropTableDDL)
               && Util.safeEquals(insertNodeSql, other.insertNodeSql)
               && Util.safeEquals(jdbcPassword, other.jdbcPassword)
               && Util.safeEquals(jdbcURL, other.jdbcURL)
               && Util.safeEquals(jdbcUser, other.jdbcUser)
               && Util.safeEquals(selectChildFqnsSql, other.selectChildFqnsSql)
               && Util.safeEquals(selectChildNamesSql, other.selectChildNamesSql)
               && Util.safeEquals(selectNodeSql, other.selectNodeSql)
               && Util.safeEquals(table, other.table)
               && Util.safeEquals(updateNodeSql, other.updateNodeSql)
               && Util.safeEquals(updateTableSql, other.updateTableSql)
               && Util.safeEquals(connectionFactoryClass, other.connectionFactoryClass)
               && Util.safeEquals(primaryKey, other.primaryKey)
               && Util.safeEquals(nodeType, other.nodeType)
               && Util.safeEquals(fqnType, other.fqnType)
               && Util.safeEquals(parentColumn, other.parentColumn);
      }

      return false;
   }

   @Override
   public int hashCode()
   {
      int result = hashCodeExcludingProperties();
      result = 31 * result + (createTable ? 0 : 1);
      result = 31 * result + (createTableDDL == null ? 0 : createTableDDL.hashCode());
      result = 31 * result + (datasourceName == null ? 0 : datasourceName.hashCode());
      result = 31 * result + (deleteAllSql == null ? 0 : deleteAllSql.hashCode());
      result = 31 * result + (deleteNodeSql == null ? 0 : deleteNodeSql.hashCode());
      result = 31 * result + (driverClass == null ? 0 : driverClass.hashCode());
      result = 31 * result + (dropTable ? 0 : 1);
      result = 31 * result + (dropTableDDL == null ? 0 : dropTableDDL.hashCode());
      result = 31 * result + (insertNodeSql == null ? 0 : insertNodeSql.hashCode());
      result = 31 * result + (jdbcPassword == null ? 0 : jdbcPassword.hashCode());
      result = 31 * result + (jdbcUser == null ? 0 : jdbcUser.hashCode());
      result = 31 * result + (jdbcURL == null ? 0 : jdbcURL.hashCode());
      result = 31 * result + (selectChildFqnsSql == null ? 0 : selectChildFqnsSql.hashCode());
      result = 31 * result + (selectChildNamesSql == null ? 0 : selectChildNamesSql.hashCode());
      result = 31 * result + (selectNodeSql == null ? 0 : selectNodeSql.hashCode());
      result = 31 * result + (table == null ? 0 : table.hashCode());
      result = 31 * result + (updateNodeSql == null ? 0 : updateNodeSql.hashCode());
      result = 31 * result + (updateTableSql == null ? 0 : updateTableSql.hashCode());
      result = 31 * result + (connectionFactoryClass == null ? 0 : connectionFactoryClass.hashCode());
      result = 31 * result + (primaryKey == null ? 0 : primaryKey.hashCode());
      result = 31 * result + (nodeType == null ? 0 : nodeType.hashCode());
      result = 31 * result + (fqnType == null ? 0 : fqnType.hashCode());
      result = 31 * result + (parentColumn == null ? 0 : parentColumn.hashCode());

      return result;
   }

   @Override
   public AdjListJDBCCacheLoaderConfig clone() throws CloneNotSupportedException
   {
      return (AdjListJDBCCacheLoaderConfig) super.clone();
   }

   protected String constructSelectNodeSql()
   {
      return "SELECT " + nodeColumn + " FROM " + table + " WHERE " + fqnColumn + " = ?";
   }

   protected String constructUpdateNodeSql()
   {
      return "UPDATE " + table + " SET " + nodeColumn + " = ? WHERE " + fqnColumn + " = ?";
   }

   protected String constructDeleteAllSql()
   {
      return "DELETE FROM " + table;
   }

   protected String constructDeleteNodeSql()
   {
      return "DELETE FROM " + table + " WHERE " + fqnColumn + " = ?";
   }

   protected String constructSelectChildNamesSql()
   {
      return "SELECT " + fqnColumn + " FROM " + table + " WHERE " + parentColumn + " = ?";
   }

   protected String constructExistsSql()
   {
      return "SELECT '1' FROM " + table + " WHERE " + fqnColumn + " = ?";
   }

   protected String constructInsertNodeSql()
   {
      // This SQL string takes in 4 params - fqn, node (serialized data), parent, and fqn AGAIN.
      // the benefit of this is is that it will run without failing even if the row already exists, so you don't need
      // to check if the row exists before running this query.  Returns '1' if the row was inserted, '0' otherwise,
      // but does NOT fail on primary key conflict.

      // the 'dummy' table, table_D, *must* exist though, and could contain just a single dummy constant row.

      return "INSERT INTO "
            + table
            + " ("
            + fqnColumn
            + ", "
            + nodeColumn
            + ", "
            + parentColumn
            + ") SELECT ?, ?, ? FROM "
            + table
            + "_D WHERE NOT EXISTS (SELECT "
            + fqnColumn
            + " FROM "
            + table
            + " WHERE "
            + fqnColumn
            + " = ?)";
   }

   public String getDummyTableCreationDDL()
   {
      return "CREATE TABLE " + table + "_D (i CHAR)";
   }

   public String getDummyTableRemovalDDL()
   {
      return "DROP TABLE " + table + "_D";
   }

   public String getDummyTablePopulationSql()
   {
      return "INSERT INTO " + table + "_D VALUES ('x')";
   }
}