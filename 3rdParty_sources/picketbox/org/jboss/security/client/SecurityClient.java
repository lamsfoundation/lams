/*
  * JBoss, Home of Professional Open Source
  * Copyright 2007, JBoss Inc., and individual contributors as indicated
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
package org.jboss.security.client;
 
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

import org.jboss.security.SecurityContextAssociation;
 
/**
 *  Generic Security Client class <br/>
 *  <b>Basic Users:</b><br/>
 *  <p>Basic users will just use the methods that set the username and credential 
 *  @see {@link #setUserName(String)} and @see {@link #setCredential(Object)} </p>
 *  <b>Intermediate Users:</b></br/>
 *  <p>You can specify usage of JAAS as the framework in the client implementation.
 *  In this case, you will @see {@link #setLoginConfigName(String)} and
 *  @see #setCallbackHandler(CallbackHandler)</p>
 *  <b>Advanced Users:</b>
 *  <p>You will use the @see {@link #setSASLMechanism(String)} method</p>
 *  
 *  @author Anil.Saldhana@redhat.com
 *  @since  May 1, 2007 
 *  @version $Revision$
 */
public abstract class SecurityClient
{   
   protected Object userPrincipal = null; 
   protected Object credential = null;
   protected CallbackHandler callbackHandler = null;
   protected String loginConfigName = null;
   protected String saslMechanism = null;
   protected String saslAuthorizationId = null;
   
   protected boolean jaasDesired = false;
   protected boolean saslDesired = false;
   
   /**
    * Perform a VMWide association of security context
    */
   protected boolean vmwideAssociation = false;
   
   /**
    * Login with the desired method
    * @throws LoginException
    */
   public void login() throws LoginException
   {
      if(jaasDesired)
         performJAASLogin();
      else
         if(saslDesired)
            peformSASLLogin();
         else
            performSimpleLogin(); 
   }
   
   /**
    * Log Out
    */
   public void logout()
   {
      setSimple(null,null);
      setJAAS(null,null);
      setSASL(null,null,null);
      clearUpDesires();
      cleanUp();
   }
   
   /**
    * Set the user name and credential for simple login (non-jaas, non-sasl)
    * @param username (Can be null)
    * @param credential (Can be null)
    */
   public void setSimple(Object username, Object credential)
   {
      this.userPrincipal = username;
      this.credential = credential;
   }
   
   /**
    * Set the JAAS Login Configuration Name and Call back handler
    * @param configName can be null
    * @param cbh can be null
    */
   public void setJAAS(String configName, CallbackHandler cbh)
   {
      this.loginConfigName = configName;
      this.callbackHandler = cbh;
      clearUpDesires();
      this.jaasDesired = true;
   }
   
   /**
    * Set the mechanism and other parameters for SASL Client
    * @param mechanism
    * @param authorizationId
    * @param cbh
    */
   public void setSASL(String mechanism, String authorizationId,
         CallbackHandler cbh)
   {
      this.saslMechanism = mechanism;
      this.saslAuthorizationId = authorizationId;
      this.callbackHandler = cbh;
      clearUpDesires();
      this.saslDesired = true;
   }
   
   protected abstract void performJAASLogin() throws LoginException;
   protected abstract void peformSASLLogin();
   protected abstract void performSimpleLogin();
   
   /**
    * Is the Security Context establishment vm wide?
    * @return
    */
   public boolean isVmwideAssociation()
   {
      return vmwideAssociation;
   }

   /**
    * Set the vm wide association of security context
    * (Default : false)s
    * @param vmwideAssociation
    */
   public void setVmwideAssociation(boolean vmwideAssociation)
   {
      this.vmwideAssociation = vmwideAssociation;
      if(vmwideAssociation)
         SecurityContextAssociation.setClient();
   }

   /**
    * Provide an opportunity for client implementations to clean up
    */
   protected abstract void cleanUp();
   
   private void clearUpDesires()
   {
      jaasDesired = false;
      saslDesired = false;  
   } 
}