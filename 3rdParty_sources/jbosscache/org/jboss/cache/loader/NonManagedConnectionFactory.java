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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Standard connection factory for standalone JBossCache implementations without connection pooling capabilities.
 *
 * @author <a href="mailto:hmesha@novell.com">Hany Mesha </a>
 * @author <a href="mailto:galder.zamarreno@jboss.com">Galder Zamarreno</a>
 */
class NonManagedConnectionFactory implements ConnectionFactory
{
   private static final Log log = LogFactory.getLog(NonManagedConnectionFactory.class);
   private static final boolean trace = log.isTraceEnabled();

   static final ThreadLocal<Connection> connection = new ThreadLocal<Connection>();

   private String url;
   private String usr;
   private String pwd;
   private String driverClass;

   public void setConfig(AdjListJDBCCacheLoaderConfig config)
   {
      url = config.getJdbcURL();
      usr = config.getJdbcUser();
      pwd = config.getJdbcPassword();
      driverClass = config.getDriverClass();
   }

   public void start() throws Exception
   {
      loadDriver(driverClass);
   }

   public void prepare(Object tx)
   {
      Connection con = getConnection();
      try
      {
         if (con.getAutoCommit())
         {
            con.setAutoCommit(false);
         }
      }
      catch (Exception e)
      {
         reportAndRethrowError("Failed to set auto-commit", e);
      }

      /* Connection set in ThreadLocal, no reason to return. It was previously returned for legacy purpouses
      and to trace log the connection opening in JDBCCacheLoader. */
      connection.set(con);

      if (trace)
      {
         log.trace("opened tx connection: tx=" + tx + ", con=" + con);
      }

   }

   public Connection getConnection()
   {
      Connection con = connection.get();

      if (con == null)
      {
         try
         {
            con = checkoutConnection();
            //               connection.set(con);
         }
         catch (SQLException e)
         {
            reportAndRethrowError("Failed to get connection for url=" + url + ", user=" + usr + ", password=" + pwd, e);
         }
      }

      if (trace)
      {
         log.trace("using connection: " + con);
      }

      return con;
   }

   public Connection checkoutConnection() throws SQLException
   {
      return DriverManager.getConnection(url, usr, pwd);
   }

   public void commit(Object tx)
   {
      Connection con = connection.get();
      if (con == null)
      {
         throw new IllegalStateException("Failed to commit: thread is not associated with the connection!");
      }

      try
      {
         con.commit();
         if (trace)
         {
            log.trace("committed tx=" + tx + ", con=" + con);
         }
      }
      catch (SQLException e)
      {
         reportAndRethrowError("Failed to commit", e);
      }
      finally
      {
         closeTxConnection(con);
      }
   }

   public void rollback(Object tx)
   {
      Connection con = connection.get();

      try
      {
         con.rollback();
         if (trace)
         {
            log.trace("rolledback tx=" + tx + ", con=" + con);
         }
      }
      catch (SQLException e)
      {
         reportAndRethrowError("Failed to rollback", e);
      }
      finally
      {
         closeTxConnection(con);
      }
   }

   public void close(Connection con)
   {
      if (con != null && con != connection.get())
      {
         try
         {
            con.close();

            if (trace)
            {
               log.trace("closed non tx connection: " + con);
            }
         }
         catch (SQLException e)
         {
            log.warn("Failed to close connection " + con, e);
         }
      }
   }

   public void stop()
   {
   }

   public String getUrl()
   {
      return url;
   }

   public String getUsr()
   {
      return usr;
   }

   public String getPwd()
   {
      return pwd;
   }

   public String getDriverClass()
   {
      return driverClass;
   }

   protected void loadDriver(String drv)
   {
      try
      {
         if (trace)
         {
            log.trace("Attempting to load driver: " + drv);
         }
         Class.forName(drv).newInstance();
      }
      catch (Exception e)
      {
         reportAndRethrowError("Failed to load driver " + drv, e);
      }
   }

   private void closeTxConnection(Connection con)
   {
      safeClose(con);
      connection.set(null);
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

