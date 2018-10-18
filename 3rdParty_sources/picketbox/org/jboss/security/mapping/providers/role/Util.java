/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.security.mapping.providers.role;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.PrivilegedActionException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.jboss.security.PicketBoxLogger;
import org.jboss.security.PicketBoxMessages;
import org.jboss.security.identity.RoleGroup;
import org.jboss.security.identity.plugins.SimpleRole;

/**
 * Utility class for this package.
 * 
 * @author <a href="mmoyses@redhat.com">Marcus Moyses</a>
 */
public class Util
{

   /** 
    * Utility method which loads the given properties file and returns a
    * Properties object containing the key,value pairs in that file.
    * The properties files should be in the class path as this method looks
    * to the thread context class loader (TCL) to locate the resource. If the
    * TCL is a URLClassLoader the findResource(String) method is first tried.
    * If this fails or the TCL is not a URLClassLoader getResource(String) is
    * tried.
    * @param propertiesName - the name of the properties file resource
    * @return the loaded properties file if found
    * @exception java.io.IOException thrown if the properties file cannot be found
    *    or loaded 
    */
   static Properties loadProperties(String propertiesName) throws IOException
   {

      Properties bundle = null;
      ClassLoader loader = SecurityActions.getContextClassLoader();
      URL url = null;
      // First check for local visibility via a URLClassLoader.findResource
      if (loader instanceof URLClassLoader)
      {
         URLClassLoader ucl = (URLClassLoader) loader;
         url = SecurityActions.findResource(ucl, propertiesName);
         PicketBoxLogger.LOGGER.traceAttemptToLoadResource(propertiesName);
      }
      // Do a general resource search
      if (url == null)
         url = loader.getResource(propertiesName);
      if (url == null) {
         try {
            url = new URL(propertiesName);
         } catch (MalformedURLException mue) {
            PicketBoxLogger.LOGGER.debugFailureToOpenPropertiesFromURL(mue);
            File tmp = new File(propertiesName);
            if (tmp.exists())
               url = tmp.toURI().toURL();
         }
      }
      if (url == null)
      {
         throw PicketBoxMessages.MESSAGES.unableToLoadPropertiesFile(propertiesName);
      }

      Properties defaults = new Properties();
      bundle = new Properties(defaults);
      InputStream is = null;
      try
      {
         is = SecurityActions.openStream(url);
      }
      catch (PrivilegedActionException e)
      {
         throw new IOException(e.getLocalizedMessage());
      }
      if (is != null)
      {
         try
         {
            bundle.load(is);
         }
         finally
         {
            safeClose(is);
         }
      }
      else
      {
         throw PicketBoxMessages.MESSAGES.unableToLoadPropertiesFile(propertiesName);
      }
      if (PicketBoxLogger.LOGGER.isTraceEnabled())
      {
         PicketBoxLogger.LOGGER.tracePropertiesFileLoaded(propertiesName, bundle.keySet());
      }

      return bundle;
   }

   /** 
    * Create the set of roles the user belongs to by parsing the roles.properties
    * data for username=role1,role2,...
    * 
    * @param username - name of user
    * @param roleGroup - group containing the user's roles
    * @param roles - the Properties containing the user=roles mappings
    * @return Group[] containing the sets of roles
    */
   static void addRolesToGroup(String username, RoleGroup roleGroup, Properties roles)
   {
      String[] roleNames = null;
      if (roles.containsKey(username))
      {
         String value = roles.getProperty(username);
         PicketBoxLogger.LOGGER.traceAdditionOfRoleToGroup(value, roleGroup.getRoleName());
         roleNames = parseRoles(value);
      }
      if (roleNames != null)
      {
         for (int i = 0; i < roleNames.length; i++)
         {
            roleGroup.addRole(new SimpleRole(roleNames[i]));
         }
      }
   }

   /** 
    * Parse the comma delimited roles names given by value
    *
    * @param roles - the comma delimited role names.
    */
   static String[] parseRoles(String roles)
   {
      return roles.split(",");
   }

   /**
    * Create the set of roles the user belongs to by querying a database
    * 
    * @param username - name of the user
    * @param roleGroup - group containing the user's roles
    * @param dsJndiName - JNDI name of the datasource
    * @param rolesQuery - prepared statement to query
    * @param suspendResume - flag to indicate if transactions should be suspended/resumed
    * @param tm - transaction manager
    */
   static void addRolesToGroup(String username, RoleGroup roleGroup, String dsJndiName, String rolesQuery, boolean suspendResume, TransactionManager tm)
   {
      Connection conn = null;
      PreparedStatement ps = null;
      ResultSet rs = null;

      if (suspendResume)
      {
         if (tm == null)
            throw PicketBoxMessages.MESSAGES.invalidNullTransactionManager();
      }
      Transaction tx = null;
      if (suspendResume)
      {
         try
         {
            tx = tm.suspend();
         }
         catch (SystemException e)
         {
            throw new RuntimeException(e);
         }
      }

      try
      {
         InitialContext ctx = new InitialContext();
         DataSource ds = (DataSource) ctx.lookup(dsJndiName);
         conn = ds.getConnection();
         // Get the user role names
         PicketBoxLogger.LOGGER.traceExecuteQuery(rolesQuery, username);
         ps = conn.prepareStatement(rolesQuery);
         try
         {
            ps.setString(1, username);
         }
         catch (ArrayIndexOutOfBoundsException ignore)
         {
            // The query may not have any parameters so just try it
         }
         rs = ps.executeQuery();
         if (!rs.next())
         {
            PicketBoxLogger.LOGGER.traceQueryWithEmptyResult();
         }
         else
         {
           do
           {
              String name = rs.getString(1);
              roleGroup.addRole(new SimpleRole(name));
           }
           while (rs.next());
         }
      }
      catch (NamingException ex)
      {
         throw new IllegalArgumentException(PicketBoxMessages.MESSAGES.failedToLookupDataSourceMessage(dsJndiName), ex);
      }
      catch (SQLException ex)
      {
         throw new IllegalArgumentException(PicketBoxMessages.MESSAGES.failedToProcessQueryMessage(), ex);
      }
      finally
      {
         if (rs != null)
         {
            try
            {
               rs.close();
            }
            catch (SQLException e)
            {
            }
         }
         if (ps != null)
         {
            try
            {
               ps.close();
            }
            catch (SQLException e)
            {
            }
         }
         if (conn != null)
         {
            try
            {
               conn.close();
            }
            catch (Exception ex)
            {
            }
         }
         if (suspendResume)
         {
            try
            {
               tm.resume(tx);
            }
            catch (Exception e)
            {
               throw new RuntimeException(e);
            }
         }
      }
   }
   
   private static void safeClose(InputStream fis)
   {
      try
      {
         if(fis != null)
         {
            fis.close();
         }
      }
      catch(Exception e)
      {}
   }
}
