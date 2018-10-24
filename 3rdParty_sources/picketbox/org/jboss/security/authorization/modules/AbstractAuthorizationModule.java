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
package org.jboss.security.authorization.modules;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;

import org.jboss.security.PicketBoxMessages;
import org.jboss.security.authorization.AuthorizationContext;
import org.jboss.security.authorization.AuthorizationException;
import org.jboss.security.authorization.AuthorizationModule;
import org.jboss.security.authorization.Resource;
import org.jboss.security.authorization.ResourceType;
import org.jboss.security.identity.RoleGroup;

//$Id$

/**
 *  Abstraction of Authorization Module
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @since  Jun 14, 2006 
 *  @version $Revision$
 */
public abstract class AbstractAuthorizationModule implements AuthorizationModule
{
   protected Subject subject = null;
   protected CallbackHandler handler = null;
   protected Map<String,Object> sharedState = null;
   protected Map<String,Object> options = null;
   
   protected RoleGroup role = null;
   
   /** Map of delegates for the various layers */
   protected Map<ResourceType,String> delegateMap = new HashMap<ResourceType,String>();

   /** A map that is available to reduce the loadClass synchronization */
   protected static Map<String, Class<?>> clazzMap = new ConcurrentHashMap<String, Class<?>>();

   /**
    * @see AuthorizationModule#authorize(Resource)
    */
   public abstract int authorize(Resource resource);  

   /**
    * @see AuthorizationModule#abort()
    */
   public boolean abort() throws AuthorizationException
   {
      return true;
   }

   /**
    * @see AuthorizationModule#commit()
    */
   public boolean commit() throws AuthorizationException
   {
      return true;
   } 
   
   /**
    * @see AuthorizationModule#destroy()
    */
   public boolean destroy()
   {
      subject = null;
      handler = null;
      sharedState = null;
      options = null;
      return true;
   } 

   /**
    * @see AuthorizationModule#initialize(javax.security.auth.Subject, javax.security.auth.callback.CallbackHandler, java.util.Map, java.util.Map, org.jboss.security.identity.RoleGroup)
    */
   public void initialize(Subject subject, CallbackHandler handler, Map<String,Object> sharedState,
         Map<String,Object> options, RoleGroup subjectRole)
   {
      this.subject = subject;
      this.handler = handler;
      this.sharedState = sharedState;
      this.options = options;
      //Check if there is a delegate map via options
      if(options != null)
      {
         String commaSeparatedDelegates = (String)options.get("delegateMap");
         if(commaSeparatedDelegates != null && commaSeparatedDelegates.length() > 0)
            populateDelegateMap(commaSeparatedDelegates);
      } 
      this.role = subjectRole;
   }
   
   /**
    * Override to print more details
    */
   public String toString()
   {
      StringBuffer buf = new StringBuffer("Name="+getClass().getName());
      buf.append(":subject="+subject);
      buf.append(":role="+this.role);
      return buf.toString();
   } 
   
   //PROTECTED METHODS
   /**
    * Subclasses can use this method to leave the authorization 
    * decision to the delegate configured
    */
   protected int invokeDelegate(Resource resource)
   {
      int authorizationDecision = AuthorizationContext.DENY;

      ResourceType layer = resource.getLayer();
      String delegateStr = (String)delegateMap.get(layer);
      if(delegateStr == null)
         throw PicketBoxMessages.MESSAGES.missingDelegateForLayer(layer != null ? layer.toString() : null);
      AuthorizationModuleDelegate delegate = null;
      try
      {
         delegate = getDelegate(delegateStr); 
         authorizationDecision = delegate.authorize(resource,this.subject, this.role); 
      }
      catch(Exception e)
      { 
         IllegalStateException ise = new IllegalStateException(e.getLocalizedMessage());
         ise.initCause(e);
         throw ise;
      } 
      return authorizationDecision;
   }
   
   /**
    * Load the delegate
    * @param delegateStr FQN of the delegate
    * @return Delegate Instance
    * @throws Exception
    */
   protected AuthorizationModuleDelegate getDelegate(String delegateStr) 
   throws Exception
   {
      Class<?> clazz = clazzMap.get(delegateStr);
      if(clazz == null)
      {
         try
         {
            clazz = getClass().getClassLoader().loadClass(delegateStr);
         }
         catch (Exception e)
         {
            ClassLoader tcl = SecurityActions.getContextClassLoader();
            clazz = tcl.loadClass(delegateStr);
         }
         clazzMap.put(delegateStr, clazz); 
      } 
      
      return (AuthorizationModuleDelegate)clazz.newInstance();
   }
   
   /**
    * Options may have a comma separated delegate map
    * @param commaSeparatedDelegates
    */
   protected void populateDelegateMap(String commaSeparatedDelegates)
   {
      StringTokenizer st = new StringTokenizer(commaSeparatedDelegates,",");
      while(st.hasMoreTokens())
      {
         String keyPair = st.nextToken();
         StringTokenizer keyst = new StringTokenizer(keyPair,"=");
         if(keyst.countTokens() != 2)
            throw PicketBoxMessages.MESSAGES.invalidDelegateMapEntry(keyPair);
         String key = keyst.nextToken();
         String value = keyst.nextToken();
         this.delegateMap.put(ResourceType.valueOf(key),value);
      }   
   }    
}