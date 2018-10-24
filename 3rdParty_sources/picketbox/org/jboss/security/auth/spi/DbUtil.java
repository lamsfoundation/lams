/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.security.auth.spi;

import java.security.Principal;
import java.security.acl.Group;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.login.LoginException;
import javax.sql.DataSource;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.jboss.security.PicketBoxLogger;
import org.jboss.security.PicketBoxMessages;
import org.jboss.security.SimpleGroup;
import org.jboss.security.plugins.TransactionManagerLocator;

/**
 * Database related util methods
 * @author Anil.Saldhana@redhat.com
 * @since May 31, 2008
 */
class DbUtil
{
   /** Execute the rolesQuery against the dsJndiName to obtain the roles for
   the authenticated user.
    
   @return Group[] containing the sets of roles
   */
  static Group[] getRoleSets(String username, String dsJndiName, String txManagerJndiName,
     String rolesQuery, AbstractServerLoginModule aslm, boolean suspendResume)
     throws LoginException
  {
     Connection conn = null;
     HashMap<String,Group> setsMap = new HashMap<String,Group>();
     PreparedStatement ps = null;
     ResultSet rs = null;
     
     TransactionManager tm = null;
     
     if(suspendResume)
     {
        TransactionManagerLocator tml = new TransactionManagerLocator();
        try
        {
           tm = tml.getTM(txManagerJndiName);
        }
        catch (NamingException e1)
        {
           throw new RuntimeException(e1);
        }
        if(tm == null)
           throw PicketBoxMessages.MESSAGES.invalidNullTransactionManager();
     }
     Transaction tx = null;
     if (suspendResume)
     {
       // tx = TransactionDemarcationSupport.suspendAnyTransaction();
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
        catch(ArrayIndexOutOfBoundsException ignore)
        {
           // The query may not have any parameters so just try it
        }
        rs = ps.executeQuery();
        if( rs.next() == false )
        {
           if( aslm.getUnauthenticatedIdentity() == null )
              throw PicketBoxMessages.MESSAGES.noMatchingUsernameFoundInRoles();
           /* We are running with an unauthenticatedIdentity so create an empty Roles set and return. */
           Group[] roleSets = { new SimpleGroup("Roles") };
           return roleSets;
        }

        do
        {
           String name = rs.getString(1);
           String groupName = rs.getString(2);
           if( groupName == null || groupName.length() == 0 )
              groupName = "Roles";
           Group group = (Group) setsMap.get(groupName);
           if( group == null )
           {
              group = new SimpleGroup(groupName);
              setsMap.put(groupName, group);
           }

           try
           {
              Principal p = aslm.createIdentity(name);
              group.addMember(p);
           }
           catch(Exception e)
           {
              PicketBoxLogger.LOGGER.debugFailureToCreatePrincipal(name, e);
           }
        } while( rs.next() );
     }
     catch(NamingException ex)
     {
        LoginException le = new LoginException(PicketBoxMessages.MESSAGES.failedToLookupDataSourceMessage(dsJndiName));
        le.initCause(ex);
        throw le;
     }
     catch(SQLException ex)
     {
        LoginException le = new LoginException(PicketBoxMessages.MESSAGES.failedToProcessQueryMessage());
        le.initCause(ex);
        throw le;
     }
     finally
     {
        if( rs != null )
        {
           try
           {
              rs.close();
           }
           catch(SQLException e)
           {}
        }
        if( ps != null )
        {
           try
           {
              ps.close();
           }
           catch(SQLException e)
           {}
        }
        if( conn != null )
        {
           try
           {
              conn.close();
           }
           catch (Exception ex)
           {}
        }
        if (suspendResume)
        {
           //TransactionDemarcationSupport.resumeAnyTransaction(tx);
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
     
     Group[] roleSets = new Group[setsMap.size()];
     setsMap.values().toArray(roleSets);
     return roleSets;
  }
}