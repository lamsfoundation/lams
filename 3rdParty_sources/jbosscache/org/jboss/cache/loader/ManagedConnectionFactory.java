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

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * ManagedConnectionFactory for Application Server managed environments
 *
 * @author <a href="mailto:hmesha@novell.com">Hany Mesha </a>
 * @author <a href="mailto:galder.zamarreno@jboss.com">Galder Zamarreno</a>
 */
public class ManagedConnectionFactory implements ConnectionFactory
{
   private static final Log log = LogFactory.getLog(ManagedConnectionFactory.class);
   private static final boolean trace = log.isTraceEnabled();

   private DataSource dataSource;
   private String datasourceName;

   public void setConfig(AdjListJDBCCacheLoaderConfig config)
   {
      datasourceName = config.getDatasourceName();
   }

   public void start() throws Exception
   {
      // A datasource will be registered in JNDI in the start portion of
      // its lifecycle, so now that we are in start() we can look it up
      InitialContext ctx = null;
      try
      {
         ctx = new InitialContext();
         dataSource = (DataSource) ctx.lookup(datasourceName);
         if (trace)
         {
            log.trace("Datasource lookup for " + datasourceName + " succeded: " + dataSource);
         }
      }
      catch (NamingException e)
      {
         reportAndRethrowError("Failed to lookup datasource " + datasourceName, e);
      }
      finally
      {
         if (ctx != null)
         {
            try
            {
               ctx.close();
            }
            catch (NamingException e)
            {
               log.warn("Failed to close naming context.", e);
            }
         }
      }
   }

   public Connection getConnection()
         throws SQLException
   {
      Connection connection = dataSource.getConnection();
      if (trace)
      {
         log.trace("Connection checked out: " + connection);
      }
      return connection;
   }

   public void prepare(Object txId)
   {
      /* This implementation should be left empty. In a managed environment, we retrieve the datasource from
      the application server, and it's down to the configuration of the datasource whether it will participate
      in the current transaction, i.e. local-tx-datasource, no-tx-datasource,...etc. The application server will
      make sure that if configured, the datasource will participate in the transaction and will commit/rollback
      changes if required. Therefore, we don't have to do anything for these transactional methods but leave them
      empty. */
   }

   public void commit(Object txId)
   {
      /* This implementation should be left empty. In a managed environment, we retrieve the datasource from
      the application server, and it's down to the configuration of the datasource whether it will participate
      in the current transaction, i.e. local-tx-datasource, no-tx-datasource,...etc. The application server will
      make sure that if configured, the datasource will participate in the transaction and will commit/rollback
      changes if required. Therefore, we don't have to do anything for these transactional methods but leave them
      empty. */
   }

   public void rollback(Object txId)
   {
      /* This implementation should be left empty. In a managed environment, we retrieve the datasource from
      the application server, and it's down to the configuration of the datasource whether it will participate
      in the current transaction, i.e. local-tx-datasource, no-tx-datasource,...etc. The application server will
      make sure that if configured, the datasource will participate in the transaction and will commit/rollback
      changes if required. Therefore, we don't have to do anything for these transactional methods but leave them
      empty. */
   }

   public void close(Connection con)
   {
      safeClose(con);
   }

   public void stop()
   {
   }

   private void safeClose(Connection con)
   {
      if (con != null)
      {
         try
         {
            con.close();
         }
         catch (SQLException e)
         {
            log.warn("Failed to close connection", e);
         }
      }
   }

   private void reportAndRethrowError(String message, Exception cause) throws IllegalStateException
   {
      log.error(message, cause);
      throw new IllegalStateException(message, cause);
   }
}
