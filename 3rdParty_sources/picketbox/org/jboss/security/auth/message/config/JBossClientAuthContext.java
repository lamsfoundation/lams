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
package org.jboss.security.auth.message.config;

import java.util.Iterator;

import javax.security.auth.Subject;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.config.ClientAuthContext;
import javax.security.auth.message.module.ClientAuthModule;

import org.jboss.security.PicketBoxMessages;

//$Id$

/**
 *  Default Client Authentication Context
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @since  May 17, 2006 
 *  @version $Revision$
 */
public class JBossClientAuthContext implements ClientAuthContext
{
   private JBossClientAuthConfig config;
   
   /**
    * Create a new JBossClientAuthContext.
    * 
    * @param config Client Auth Config
    */
   public JBossClientAuthContext(JBossClientAuthConfig config)
   {
      if(config == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("config");
      this.config = config;
   }
   
   /**
    * @see ClientAuthContext#cleanSubject(javax.security.auth.message.MessageInfo, javax.security.auth.Subject)
    */
   @SuppressWarnings({"rawtypes"})
   public void cleanSubject(MessageInfo messageInfo, Subject subject) 
   throws AuthException
   { 
      Iterator iter = config.getClientAuthModules().iterator();
      while(iter.hasNext())
      {
         ((ClientAuthModule)iter.next()).cleanSubject(messageInfo,subject); 
      } 
   }
   
   /**
    * @see ClientAuthContext#secureRequest(javax.security.auth.message.MessageInfo, javax.security.auth.Subject
    */ 
   @SuppressWarnings("rawtypes")
   public AuthStatus secureRequest(MessageInfo messageInfo, Subject clientSubject) throws AuthException
   {
      Iterator iter = config.getClientAuthModules().iterator();
      AuthStatus status = null;
      while(iter.hasNext())
      {
         status = ((ClientAuthModule)iter.next()).secureRequest(messageInfo,clientSubject);
         if(status == AuthStatus.FAILURE)
            break;
      }
      return status;
   }
   
   /**
    * @see ClientAuthContext#validateResponse(javax.security.auth.message.MessageInfo, javax.security.auth.Subject, javax.security.auth.Subject)
    */ 
   @SuppressWarnings("rawtypes")
   public AuthStatus validateResponse(MessageInfo messageInfo, Subject clientSubject, 
         Subject serviceSubject) throws AuthException
   {
      Iterator iter = config.getClientAuthModules().iterator();
      AuthStatus status = null;
      while(iter.hasNext())
      {
         status = ((ClientAuthModule)iter.next()).validateResponse(messageInfo,clientSubject,
                                                                                serviceSubject);
         if(status == AuthStatus.FAILURE)
            break;
      }
      return status;
   } 
}