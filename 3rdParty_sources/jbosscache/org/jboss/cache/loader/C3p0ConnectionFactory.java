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

import com.mchange.v2.c3p0.DataSources;
import com.mchange.v2.c3p0.PooledDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Standalone connection factory based on c3p0 connection pooling library
 *
 * @author <a href="mailto:galder.zamarreno@jboss.com">Galder Zamarreno</a>
 */
public class C3p0ConnectionFactory extends NonManagedConnectionFactory
{
   private static final Log log = LogFactory.getLog(C3p0ConnectionFactory.class);
   private static final boolean trace = log.isTraceEnabled();

   private PooledDataSource ds;
   private Properties config = new Properties();

   @Override
   public void setConfig(AdjListJDBCCacheLoaderConfig config)
   {
      super.setConfig(config);

      writeProps(config.getProperties());
      writeProps(System.getProperties());
      if (trace) log.trace("Using props: " + this.config);
   }

   private void writeProps(Properties properties)
   {
      Enumeration e = properties.propertyNames();
      while (e.hasMoreElements())
      {
         String property = (String) e.nextElement();
         if (property.startsWith("c3p0."))
         {
            String newName = property.substring("c3p0.".length());
            this.config.put(newName, properties.get(property));
         }
      }
   }

   @Override
   public void start() throws Exception
   {
      /* We need to call super so that the driver is loaded. This is required by the C3P0 manual. */
      super.start();

      DataSource unpooled = DataSources.unpooledDataSource(getUrl(), getUsr(), getPwd());
      ds = (PooledDataSource) DataSources.pooledDataSource(unpooled, config);

      if (log.isDebugEnabled())
      {
         log.debug("Pooled datasource(url=" + getUrl() + ",usr=" + getUsr() + ",pwd=" + getPwd() + ") started.");
      }
   }

   @Override
   public Connection checkoutConnection() throws SQLException
   {
      if (trace)
      {
         log.trace("DataSource before checkout (NumBusyConnectionsAllUsers) : " + ds.getNumBusyConnectionsAllUsers());
         log.trace("DataSource before checkout (NumConnectionsAllUsers) : " + ds.getNumConnectionsAllUsers());
      }
      Connection connection = ds.getConnection();
      if (trace)
      {
         log.trace("DataSource after checkout (NumBusyConnectionsAllUsers) : " + ds.getNumBusyConnectionsAllUsers());
         log.trace("DataSource after checkout (NumConnectionsAllUsers) : " + ds.getNumConnectionsAllUsers());
         log.trace("Connection checked out: " + connection);
      }
      return connection;
   }

   @Override
   public void stop()
   {
      try
      {
         DataSources.destroy(ds);
         if (log.isDebugEnabled())
         {
            log.debug("Pooled datasource destroyed.");
         }
      }
      catch (SQLException sqle)
      {
         log.warn("Could not destroy C3P0 connection pool: " + ds, sqle);
      }
   }

   protected DataSource getDataSource()
   {
      return ds;
   }
}
