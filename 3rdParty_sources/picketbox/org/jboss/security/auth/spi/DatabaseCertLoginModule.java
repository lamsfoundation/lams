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
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

import org.jboss.security.PicketBoxLogger;

/**
 * A Certificate Login Module that gets its role information from a database.
 * 
 * This module is the functional equivelant of the 
 * {@link org.jboss.security.auth.spi.DatabaseServerLoginModule} minus the
 * usersQuery.
 * @see org.jboss.security.auth.spi.DatabaseServerLoginModule
 *
 * @author <a href="mailto:jasone@greenrivercomputing.com">Jason Essington</a>
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class DatabaseCertLoginModule extends BaseCertLoginModule
{
   // see AbstractServerLoginModule
   private static final String DS_JNDI_NAME = "dsJndiName";
   private static final String ROLES_QUERY = "rolesQuery";
   private static final String SUSPEND_RESUME = "suspendResume";
   private static final String TRANSACTION_MANAGER_JNDI_NAME = "transactionManagerJndiName";

    private static final String[] ALL_VALID_OPTIONS =
   {
	   DS_JNDI_NAME,ROLES_QUERY,SUSPEND_RESUME
   };
   
   /** The JNDI name of the DataSource to use */
   private String dsJndiName;
    /** The JNDI name of the transaction manager */
   protected String txManagerJndiName = "java:/TransactionManager";
    /** The sql query to obtain the user roles */
   private String rolesQuery = "select Role, RoleGroup from Roles where PrincipalID=?";
   /** Whether to suspend resume transactions during database operations */
   protected boolean suspendResume = true;

   /**
    * @param options -
    * dsJndiName: The name of the DataSource of the database containing the
    *    Principals, Roles tables
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
      
      Object tmp = options.get(ROLES_QUERY);
      if( tmp != null )
         rolesQuery = tmp.toString();

      tmp = options.get(SUSPEND_RESUME);
      if( tmp != null )
         suspendResume = Boolean.valueOf(tmp.toString()).booleanValue();

      tmp = options.get(TRANSACTION_MANAGER_JNDI_NAME);
      if (tmp != null)
         txManagerJndiName = tmp.toString();

      PicketBoxLogger.LOGGER.traceDBCertLoginModuleOptions(dsJndiName, "", rolesQuery, suspendResume);
   }

   /**
    * @see org.jboss.security.auth.spi.DatabaseServerLoginModule#getRoleSets
    */
   protected Group[] getRoleSets() throws LoginException
   {
      String username = getUsername();
      Group[] roleSets = Util.getRoleSets(username, dsJndiName, txManagerJndiName, rolesQuery, this, suspendResume);
      return roleSets;
   }
   
}