/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
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

import java.security.acl.Group;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.sql.DataSource;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.jboss.security.PicketBoxLogger;
import org.jboss.security.PicketBoxMessages;
import org.jboss.security.plugins.TransactionManagerLocator;


/**
 * A JDBC based login module that supports authentication and role mapping.
 * It is based on two logical tables:
 * <ul>
 * <li>Principals(PrincipalID text, Password text)
 * <li>Roles(PrincipalID text, Role text, RoleGroup text)
 * </ul>
 * <p>
 * LoginModule options:
 * <ul>
 * <li><em>dsJndiName</em>: The name of the DataSource of the database
 * containing the Principals, Roles tables
 * <li><em>principalsQuery</em>: The prepared statement query, equivalent to:
 * <pre>
 *    "select Password from Principals where PrincipalID=?"
 * </pre>
 * <li><em>rolesQuery</em>: The prepared statement query, equivalent to:
 * <pre>
 *    "select Role, RoleGroup from Roles where PrincipalID=?"
 * </pre>
 * </ul>
 *
 * @author <a href="mailto:on@ibis.odessa.ua">Oleg Nitz</a>
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class DatabaseServerLoginModule extends UsernamePasswordLoginModule
{
   // see AbstractServerLoginModule
   private static final String DS_JNDI_NAME = "dsJndiName";
   private static final String ROLES_QUERY = "rolesQuery";
   private static final String SUSPEND_RESUME = "suspendResume";
   private static final String PRINCIPALS_QUERY = "principalsQuery";
   private static final String TRANSACTION_MANAGER_JNDI_NAME = "transactionManagerJndiName";

   private static final String[] ALL_VALID_OPTIONS =
   {
      DS_JNDI_NAME,ROLES_QUERY,SUSPEND_RESUME,PRINCIPALS_QUERY,TRANSACTION_MANAGER_JNDI_NAME
   };
   
   /** The JNDI name of the DataSource to use */
   protected String dsJndiName;
   /** The sql query to obtain the user password */
   protected String principalsQuery = "select Password from Principals where PrincipalID=?";
   /** The sql query to obtain the user roles */
   protected String rolesQuery;
   /** Whether to suspend resume transactions during database operations */
   protected boolean suspendResume = true;
   /** The JNDI name of the transaction manager */
   protected String txManagerJndiName = "java:/TransactionManager";
   /** The TransactionManagaer instance to be used */
   protected TransactionManager tm = null;

   /**
    * Initialize this LoginModule.
    * 
    * @param options -
    * dsJndiName: The name of the DataSource of the database containing the
    *    Principals, Roles tables
    * principalsQuery: The prepared statement query, equivalent to:
    *    "select Password from Principals where PrincipalID=?"
    * rolesQuery: The prepared statement query, equivalent to:
    *    "select Role, RoleGroup from Roles where PrincipalID=?"
    */
   public void initialize(Subject subject, CallbackHandler callbackHandler,
      Map<String,?> sharedState, Map<String,?> options)
   {
      addValidOptions(ALL_VALID_OPTIONS);
      super.initialize(subject, callbackHandler, sharedState, options);
      dsJndiName = (String) options.get(DS_JNDI_NAME);
      if( dsJndiName == null )
         dsJndiName = "java:/DefaultDS";
      Object tmp = options.get(PRINCIPALS_QUERY);
      if( tmp != null )
         principalsQuery = tmp.toString();
      tmp = options.get(ROLES_QUERY);
      if( tmp != null )
         rolesQuery = tmp.toString();
      tmp = options.get(SUSPEND_RESUME);
      if( tmp != null )
         suspendResume = Boolean.valueOf(tmp.toString()).booleanValue();
	  
      //Get the Transaction Manager JNDI Name
      String jname = (String) options.get(TRANSACTION_MANAGER_JNDI_NAME);
      if(jname != null)
         this.txManagerJndiName = jname;

      PicketBoxLogger.LOGGER.traceDBCertLoginModuleOptions(dsJndiName, principalsQuery, rolesQuery, suspendResume);

	  try
      {
         if(this.suspendResume)
            tm = this.getTransactionManager();
      }
      catch (NamingException e)
      {
         throw PicketBoxMessages.MESSAGES.failedToGetTransactionManager(e);
      }
   }

   /** Get the expected password for the current username available via
    * the getUsername() method. This is called from within the login()
    * method after the CallbackHandler has returned the username and
    * candidate password.
    * @return the valid password String
    */
   protected String getUsersPassword() throws LoginException
   {
      String username = getUsername();
      String password = null;
      Connection conn = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      
      Transaction tx = null;
      if (suspendResume)
      {
         //tx = TransactionDemarcationSupport.suspendAnyTransaction();
         try
         {
            if(tm == null)
               throw PicketBoxMessages.MESSAGES.invalidNullTransactionManager();
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
         // Get the password
         PicketBoxLogger.LOGGER.traceExecuteQuery(principalsQuery, username);
         ps = conn.prepareStatement(principalsQuery);
         ps.setString(1, username);
         rs = ps.executeQuery();
         if( rs.next() == false )
         {
            throw PicketBoxMessages.MESSAGES.noMatchingUsernameFoundInPrincipals();
         }
         
         password = rs.getString(1);
         password = convertRawPassword(password);
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
         if (rs != null)
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
            catch (SQLException ex)
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
      return password;
   }

   /** Execute the rolesQuery against the dsJndiName to obtain the roles for
    the authenticated user.
     
    @return Group[] containing the sets of roles
    */
   protected Group[] getRoleSets() throws LoginException
   {
      if (rolesQuery != null)
      {
         String username = getUsername();
         PicketBoxLogger.LOGGER.traceExecuteQuery(rolesQuery, username);
         Group[] roleSets = Util.getRoleSets(username, dsJndiName, txManagerJndiName, rolesQuery, this,
               suspendResume);
         return roleSets;
      }
      return new Group[0];
   }
   
   /** A hook to allow subclasses to convert a password from the database
    into a plain text string or whatever form is used for matching against
    the user input. It is called from within the getUsersPassword() method.
    @param rawPassword - the password as obtained from the database
    @return the argument rawPassword
    */
   protected String convertRawPassword(String rawPassword)
   {
      return rawPassword;
   }
   
   protected TransactionManager getTransactionManager() throws NamingException
   {
      TransactionManagerLocator tml = new TransactionManagerLocator();
      return tml.getTM(this.txManagerJndiName);
   } 
}
