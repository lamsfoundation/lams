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
package org.jboss.security.auth.callback;

import java.lang.reflect.UndeclaredThrowableException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import javax.security.auth.callback.CallbackHandler;
import javax.security.jacc.PolicyContext;
import javax.security.jacc.PolicyContextException;

import org.jboss.security.SecurityConstants;
import org.jboss.security.SecurityContext;
import org.jboss.security.SecurityContextAssociation;

/**
 Security actions for the callback package

 @author Scott.Stark@jboss.org
 @version $Revision$
 */
class SecurityActions
{
   interface PolicyContextActions
   {
      PolicyContextActions PRIVILEGED = new PolicyContextActions()
      {
         private final PrivilegedExceptionAction<CallbackHandler> exAction = new PrivilegedExceptionAction<CallbackHandler>()
         {
            public CallbackHandler run() throws Exception
            {
               return (CallbackHandler) PolicyContext.getContext(SecurityConstants.CALLBACK_HANDLER_KEY);
            }
         };
         public CallbackHandler getContextCallbackHandler()
            throws PolicyContextException
         {
            try
            {
               return (CallbackHandler) AccessController.doPrivileged(exAction);
            }
            catch(PrivilegedActionException e)
            {
               Exception ex = e.getException();
               if( ex instanceof PolicyContextException )
                  throw (PolicyContextException) ex;
               else
                  throw new UndeclaredThrowableException(ex);
            }
         }
      };

      PolicyContextActions NON_PRIVILEGED = new PolicyContextActions()
      {
         public CallbackHandler getContextCallbackHandler()
            throws PolicyContextException
         {
            return (CallbackHandler) PolicyContext.getContext(SecurityConstants.CALLBACK_HANDLER_KEY);
         }
      };

      CallbackHandler getContextCallbackHandler()
         throws PolicyContextException;
   }

   static CallbackHandler getContextCallbackHandler()
      throws Exception
   {
      if(System.getSecurityManager() == null)
      {
         return PolicyContextActions.NON_PRIVILEGED.getContextCallbackHandler();
      }
      else
      {
         return PolicyContextActions.PRIVILEGED.getContextCallbackHandler();
      }
   }
   
   static SecurityContext getCurrentSecurityContext()
   {
      return AccessController.doPrivileged( new PrivilegedAction<SecurityContext>() 
      {

		public SecurityContext run() 
		{
			return SecurityContextAssociation.getSecurityContext();
		}
      });
   }
   static ClassLoader getContextClassLoader()
   {
      return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>()
      { 
         public ClassLoader run()
         { 
            return Thread.currentThread().getContextClassLoader();
         }
       });  
   }
   

   static Void setContextClassLoader(final ClassLoader cl)
   {
      return AccessController.doPrivileged(new PrivilegedAction<Void>()
      {
         public Void run()
         {
            Thread.currentThread().setContextClassLoader(cl);
            return null;
         }
      });
   }
}