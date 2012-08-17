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
import org.jboss.cache.Fqn;
import org.jboss.cache.config.CacheLoaderConfig.IndividualCacheLoaderConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JDBC CacheLoader implementation.
 * <p/>
 * This implementation uses one table. The table consists of three columns:
 * <ul>
 * <li>text column for fqn (which is also a primary key)</li>
 * <li>blob column for attributes (can contain null)</li>
 * <li>text column for parent fqn (can contain null)</li>
 * </ul>
 * <p/>
 * The configuration options are:
 * <p/>
 * <b>Table configuration</b>
 * <ul>
 * <li><b>cache.jdbc.table.name</b> - the table name (default is <i>jbosscache</i>)</li>
 * <li><b>cache.jdbc.table.create</b> - should be true or false, indicates whether to create the table at start phase</li>
 * <li><b>cache.jdbc.table.drop</b> - should be true or false, indicates whether to drop the table at stop phase</li>
 * <li><b>cache.jdbc.table.primarykey</b> - the name for the table primary key (default is <i>jbosscache_pk</i>)</li>
 * <li><b>cache.jdbc.fqn.column</b> - the name for the fqn column (default is <i>fqn</i>)</li>
 * <li><b>cache.jdbc.fqn.type</b> - the type for the fqn column (default is <i>varchar(255)</i>)</li>
 * <li><b>cache.jdbc.node.column</b> - the name for the node's contents column (default is <i>node</i>)</li>
 * <li><b>cache.jdbc.node.type</b> - the type for the node's contents column (default is <i>blob</i>)</li>
 * <li><b>cache.jdbc.parent.column</b> - the name for the parent fqn column (default is <i>parent</i>)</li>
 * </ul>
 * <p/>
 * <b>DataSource configuration</b>
 * <ul>
 * <li><b>cache.jdbc.datasource</b> - the JNDI name of the datasource</li>
 * </ul>
 * <p/>
 * <b>JDBC driver configuration (used when DataSource is not configured)</b>
 * <ul>
 * <li><b>cache.jdbc.driver</b> - fully qualified JDBC driver name</li>
 * <li><b>cache.jdbc.url</b> - URL to connect to the database</li>
 * <li><b>cache.jdbc.user</b> - the username to use to connect to the database</li>
 * <li><b>cache.jdbc.password</b> - the password to use to connect to the database</li>
 * </ul>
 *
 * @author <a href="mailto:alex@jboss.org">Alexey Loubyansky</a>
 * @author <a href="mailto:hmesha@novell.com">Hany Mesha </a>
 * @author <a href="mailto:galder.zamarreno@jboss.com">Galder Zamarreno</a>
 * @version <tt>$Revision$</tt>
 * @deprecated please use the {@link org.jboss.cache.loader.JDBCCacheLoader}.
 */
@Deprecated
@SuppressWarnings("deprecation")
public class JDBCCacheLoaderOld extends AdjListJDBCCacheLoader
{
   private static final Log log = LogFactory.getLog(JDBCCacheLoaderOld.class);

   private JDBCCacheLoaderOldConfig config;


   @Override
   public AdjListJDBCCacheLoaderConfig processConfig(IndividualCacheLoaderConfig base)
   {
      if (base instanceof JDBCCacheLoaderOldConfig)
      {
         config = (JDBCCacheLoaderOldConfig) base;
      }
      else
      {
         config = new JDBCCacheLoaderOldConfig(base);
      }
      return config;
   }

   public IndividualCacheLoaderConfig getConfig()
   {
      return config;
   }


   /**
    * Adds/overrides a value in a node for a key.
    * If the node does not exist yet, the node will be created.
    * If parent nodes do not exist for the node, empty parent nodes will be created.
    *
    * @param name  node's fqn
    * @param key   attribute's key
    * @param value attribute's value
    * @return old value associated with the attribute's key or null if there was no value previously
    *         associated with the attribute's key
    * @throws Exception
    */
   public Object put(Fqn name, Object key, Object value) throws Exception
   {
      Map oldNode = loadNode(name);
      Object oldValue;
      Map node;

      if (oldNode == null || oldNode == NULL_NODE_IN_ROW)
      {
         node = new HashMap();
      }
      else
      {
         node = oldNode;
      }
      oldValue = node.put(key, value);

      if (oldNode != null)
      {
         updateNode(name, node);
      }
      else
      {
         if (name.size() > 1)
         {
            for (int i = 1; i < name.size(); ++i)
            {
               final Fqn parent = name.getAncestor(i);
               if (!exists(parent))
               {
                  insertNode(parent, null, false);
               }
            }
         }
         insertNode(name, node, false);
      }

      return oldValue;
   }

   /**
    * Adds attributes from the passed in map to the existing node.
    * If there is no node for the fqn, a new node will be created.
    *
    * @param name       node's fqn
    * @param attributes attributes
    * @throws Exception
    */
   public void put(Fqn name, Map attributes) throws Exception
   {
      put(name, attributes, false);
   }

   /**
    * Removes a node and all its children.
    * Uses the same connection for all the db work.
    *
    * @param name node's fqn
    * @throws Exception
    */
   public void remove(Fqn name) throws Exception
   {
      Connection con = null;
      PreparedStatement ps = null;
      try
      {
         if (name.size() == 0)
         {
            if (log.isDebugEnabled())
            {
               log.debug("executing sql: " + config.getDeleteAllSql());
            }

            con = cf.getConnection();
            ps = con.prepareStatement(config.getDeleteAllSql());
            int deletedRows = ps.executeUpdate();

            if (log.isDebugEnabled())
            {
               log.debug("total rows deleted: " + deletedRows);
            }
         }
         else
         {
            StringBuilder sql = new StringBuilder(300);
            sql.append("delete from ").append(config.getTable()).append(" where fqn in (");
            //sql2.append("delete from " + table + " where fqn=? or parent in (");
            List fqns = new ArrayList();

            addChildrenToDeleteSql(name.toString(), sql, fqns);

            sql.append(')');

            if (fqns.size() == 1)
            {
               if (log.isDebugEnabled())
               {
                  log.debug("executing sql: " + config.getDeleteNodeSql() + "(" + name + ")");
               }

               con = cf.getConnection();
               ps = con.prepareStatement(config.getDeleteNodeSql());
               ps.setString(1, name.toString());
            }
            else
            {
               if (log.isDebugEnabled())
               {
                  log.debug("executing sql: " + sql + " " + fqns);
               }

               con = cf.getConnection();
               ps = con.prepareStatement(sql.toString());
               for (int i = 0; i < fqns.size(); ++i)
               {
                  ps.setString(i + 1, (String) fqns.get(i));
               }
            }

            int deletedRows = ps.executeUpdate();

            if (log.isDebugEnabled())
            {
               log.debug("total rows deleted: " + deletedRows);
            }
         }
      }
      catch (SQLException e)
      {
         reportAndRethrowError("Failed to remove node " + name, e);
      }
      finally
      {
         safeClose(ps);
         cf.close(con);
      }
   }

   // Private

   private void addChildrenToDeleteSql(String name, StringBuilder sql, List fqns)
         throws SQLException
   {
      // for now have to use connection per method, i.e. can't pass the same connection to recursive
      // invocations because buggy PointBase driver invalidates result sets.
      Connection con = null;
      PreparedStatement selChildrenPs = null;
      ResultSet rs = null;
      try
      {
         if (log.isDebugEnabled())
         {
            log.debug("executing sql: " + config.getSelectChildFqnsSql() + "(" + name + ")");
         }

         con = cf.getConnection();
         selChildrenPs = con.prepareStatement(config.getSelectChildFqnsSql());
         selChildrenPs.setString(1, name);
         rs = selChildrenPs.executeQuery();

         if (rs.next())
         {
            do
            {
               String childStr = rs.getString(1);
               addChildrenToDeleteSql(childStr, sql, fqns);
            }
            while (rs.next());
         }

         if (fqns.size() == 0)
         {
            sql.append("?");
         }
         else
         {
            sql.append(", ?");
         }
         fqns.add(name);
      }
      finally
      {
         safeClose(rs);
         safeClose(selChildrenPs);
         cf.close(con);
      }
   }

   @Override
   public void put(Fqn name, Map attributes, boolean override) throws Exception
   {
      // JBCACHE-769 -- make a defensive copy
      Map attrs = (attributes == null ? null : new HashMap(attributes));

      Map oldNode = loadNode(name);
      if (oldNode != null)
      {
         if (!override && oldNode != NULL_NODE_IN_ROW && attrs != null)
         {
            attrs.putAll(oldNode);
         }
         updateNode(name, attrs);
      }
      else
      {
         if (name.size() > 1)
         {
            for (int i = 1; i < name.size(); ++i)
            {
               final Fqn parent = name.getAncestor(i);
               if (!exists(parent))
               {
                  insertNode(parent, null, false);
               }
            }
         }
         insertNode(name, attrs, false);
      }
   }


   @Override
   protected Log getLogger()
   {
      return log;
   }

}
