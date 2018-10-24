/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat Middleware LLC, and individual contributors
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

import java.security.Principal;
import java.util.Map;

import javax.naming.NamingException;
import javax.transaction.TransactionManager;

import org.jboss.security.PicketBoxMessages;
import org.jboss.security.identity.RoleGroup;
import org.jboss.security.plugins.TransactionManagerLocator;

/**
 * A {@code MappingProvider} that reads roles from a database.
 * 
 * rolesQuery option should be a prepared statement equivalent to
 * "select RoleName from Roles where User=?"
 * 
 * @author <a href="mmoyses@redhat.com">Marcus Moyses</a>
 */
public class DatabaseRolesMappingProvider extends AbstractRolesMappingProvider
{
   protected String dsJndiName;
   
   protected String rolesQuery;

   protected boolean suspendResume = true;

   protected String TX_MGR_JNDI_NAME = "java:/TransactionManager";

   protected TransactionManager tm = null;
 
   public void init(Map<String, Object> options)
   {
      if (options != null)
      {
         dsJndiName = (String) options.get("dsJndiName");
         if (dsJndiName == null)
            throw PicketBoxMessages.MESSAGES.invalidNullProperty("dsJndiName");
         rolesQuery = (String) options.get("rolesQuery");
         if (rolesQuery == null)
            throw PicketBoxMessages.MESSAGES.invalidNullProperty("rolesQuery");
         String option = (String) options.get("suspendResume");
         if (option != null)
            suspendResume = Boolean.valueOf(option.toString()).booleanValue();

         // Get the Transaction Manager JNDI Name
         option = (String) options.get("transactionManagerJndiName");
         if (option != null)
            TX_MGR_JNDI_NAME = option;
         try
         {
            if (suspendResume)
               tm = getTransactionManager();
         }
         catch (NamingException e)
         {
            throw PicketBoxMessages.MESSAGES.failedToGetTransactionManager(e);
         }
      }
   }
 
   public void performMapping(Map<String, Object> contextMap, RoleGroup mappedObject)
   {
      if (contextMap == null || contextMap.isEmpty())
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("contextMap");

      //Obtain the principal to roles mapping
      Principal principal = getCallerPrincipal(contextMap);

      if (principal != null && rolesQuery != null)
      {
         String username = principal.getName();
         Util.addRolesToGroup(username, mappedObject, dsJndiName, rolesQuery, suspendResume, tm);
         result.setMappedObject(mappedObject);
      }

   }

   protected TransactionManager getTransactionManager() throws NamingException
   {
      TransactionManagerLocator tml = new TransactionManagerLocator();
      return tml.getTM(this.TX_MGR_JNDI_NAME);
   }

}