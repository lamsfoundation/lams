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

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.message.config.AuthConfigFactory;
import javax.security.auth.message.config.AuthConfigProvider;
import javax.security.auth.message.config.RegistrationListener;

import org.jboss.security.PicketBoxMessages;

//$Id$

/**
 * Default Authentication Configuration Factory
 * 
 * @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 * @since May 15, 2006
 * @version $Revision$
 */
public class JBossAuthConfigFactory extends AuthConfigFactory
{
   /**
    * Map of String key to provider.
    */
   private Map<String, AuthConfigProvider> keyToAuthConfigProviderMap = new HashMap<String, AuthConfigProvider>();

   /**
    * Map of key to listener.
    */
   private Map<String, RegistrationListener> keyToRegistrationListenerMap = new HashMap<String, RegistrationListener>();

   /**
    * Map of key to registration context.
    */
   private Map<String, RegistrationContext> keyToRegistrationContextMap = new HashMap<String, RegistrationContext>();
    
   /**
    * <p>
    * Creates an instance of {@code JBossAuthConfigFactory}.
    * </p>
    */
   public JBossAuthConfigFactory()
   {
      Map<String, Object> props = new HashMap<String, Object>();
      JBossAuthConfigProvider provider = new JBossAuthConfigProvider(props, null);
      // register a few default providers for the layers
      this.registerConfigProvider(provider, "HTTP", null, "Default Provider");
      this.registerConfigProvider(provider, "HttpServlet", null, "Default Provider");
   }

   /*
    * (non-Javadoc)
    * @see javax.security.auth.message.config.AuthConfigFactory#detachListener(javax.security.auth.message.config.RegistrationListener, java.lang.String, java.lang.String)
    */
   public String[] detachListener(RegistrationListener listener, String layer, String appContext)
   { 
      if (listener == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("listener");

      String[] arr = new String[0];
      String input = layer + appContext;
      String allLayer = "null" + appContext;
      String allContext = layer + "null";
      String general = "nullnull";

      RegistrationListener origListener = null;
      String key = null;
      for (int i = 0; i < 4 && origListener == null; i++)
      {
         if (i == 0)
            key = input;
         if (i == 1)
            key = allLayer;
         if (i == 2)
            key = allContext;
         if (i == 3)
            key = general;
         origListener = keyToRegistrationListenerMap.get(key);
      }

      if (origListener == listener)
      {
          keyToRegistrationListenerMap.remove(key);
         // Get the ID List
      }
      return arr;
   }

   /*
    * (non-Javadoc)
    * @see javax.security.auth.message.config.AuthConfigFactory#getConfigProvider(java.lang.String, java.lang.String, javax.security.auth.message.config.RegistrationListener)
    */
   public AuthConfigProvider getConfigProvider(String layer, String appContext, RegistrationListener listener)
   {
      String input = new StringBuilder().append(layer).append(appContext).toString();
      String allLayer = "null" + appContext;
      String allContext = layer + "null";
      String general = "nullnull";

      AuthConfigProvider acp = null;
      String key = null;
      for (int i = 0; i < 4; i++)
      {
         if (i == 0)
            key = input;
         if (i == 1)
            key = allLayer;
         if (i == 2)
            key = allContext;
         if (i == 3)
            key = general;

         if (this.keyToAuthConfigProviderMap.containsKey(key))
         {
            acp = this.keyToAuthConfigProviderMap.get(key);
            break;
         }
      }

      //
      if (listener != null)
         this.keyToRegistrationListenerMap.put(input, listener);

      return acp;
   }

   /*
    * (non-Javadoc)
    * @see javax.security.auth.message.config.AuthConfigFactory#getRegistrationContext(java.lang.String)
    */
   public RegistrationContext getRegistrationContext(String registrationID)
   {
      return this.keyToRegistrationContextMap.get(registrationID);
   }

   /*
    * (non-Javadoc)
    * @see javax.security.auth.message.config.AuthConfigFactory#getRegistrationIDs(javax.security.auth.message.config.AuthConfigProvider)
    */
   public String[] getRegistrationIDs(AuthConfigProvider provider)
   {
      List<String> al = new ArrayList<String>();
      if (provider == null)
      {
         al.addAll(keyToAuthConfigProviderMap.keySet());
      }
      else
      {
         // get all entries that have the supplied provider as value and store their keys.
         for (Map.Entry<String, AuthConfigProvider> entry : this.keyToAuthConfigProviderMap.entrySet())
         {
            if (entry.getValue().equals(provider))
               al.add(entry.getKey());
         }
      }
      String[] sarr = new String[al.size()];
      al.toArray(sarr);
      return sarr;
   }

   /*
    * (non-Javadoc)
    * @see javax.security.auth.message.config.AuthConfigFactory#refresh()
    */
   public void refresh()
   {
   }

   /*
    * (non-Javadoc)
    * @see javax.security.auth.message.config.AuthConfigFactory#registerConfigProvider(java.lang.String, java.util.Map, java.lang.String, java.lang.String, java.lang.String)
    */
   public String registerConfigProvider(String className, Map properties, String layer, String appContext,
         String description)
   {
      // Instantiate the provider
      AuthConfigProvider acp = null;
      if (className != null) {
         try
         {
            // An AuthConfigProvider must have a two-argument constructor (Map properties, AuthConfigFactory factory).
            Class<?> provClass = SecurityActions.getContextClassLoader().loadClass(className);
            Constructor<?> ctr = provClass.getConstructor(new Class[] {Map.class, AuthConfigFactory.class});
            acp = (AuthConfigProvider) ctr.newInstance(new Object[] {properties, null});
         }
         catch (Exception e)
         {
            throw PicketBoxMessages.MESSAGES.failedToRegisterAuthConfigProvider(className, e);
         }
      }

      // build the provider registration id using layer + appContext, which is a unique pair.
      String registrationID = new StringBuilder().append(layer).append(appContext).toString();
      
      // check if we already have a registration for the layer/appContext key.
      AuthConfigProvider oldProvider = this.keyToAuthConfigProviderMap.put(registrationID, acp);
      if (oldProvider != null)
      {
         // registration already exists and provider has been replaced. Update the registration context.
         JBossRegistrationContext context = (JBossRegistrationContext) this.keyToRegistrationContextMap.get(registrationID);
         context.setDescription(description);
         context.setIsPersistent(true);
         // if there is a listener attached to the registration, notify it that the registration has been replaced.
         RegistrationListener listener = this.keyToRegistrationListenerMap.get(registrationID);
         if (listener != null)
            listener.notify(layer, appContext);
      }
      else
      {
         // create a registration context for the new registration.
         RegistrationContext context = new JBossRegistrationContext(layer, appContext, description, true);
         this.keyToRegistrationContextMap.put(registrationID, context);
      }

      return registrationID;
   }

   /*
    * (non-Javadoc)
    * @see javax.security.auth.message.config.AuthConfigFactory#registerConfigProvider(javax.security.auth.message.config.AuthConfigProvider, java.lang.String, java.lang.String, java.lang.String)
    */
   public String registerConfigProvider(AuthConfigProvider provider, String layer, String appContext, String description)
   {
      String registrationID = new StringBuilder().append(layer).append(appContext).toString();

       // check if we already have a registration for the layer/appContext key.
      AuthConfigProvider oldProvider = this.keyToAuthConfigProviderMap.put(registrationID, provider);
      if (oldProvider != null)
      {
          // registration already exists and provider has been replaced. Update the registration context.
          JBossRegistrationContext context = (JBossRegistrationContext) this.keyToRegistrationContextMap.get(registrationID);
          context.setDescription(description);
          context.setIsPersistent(false);
          // if there is a listener attached to the registration, notify it that the registration has been replaced.
          RegistrationListener listener = this.keyToRegistrationListenerMap.get(registrationID);
          if (listener != null)
              listener.notify(layer, appContext);
      }
      else
      {
         // create a registration context for the new registration.
          RegistrationContext context = new JBossRegistrationContext(layer, appContext, description, false);
          this.keyToRegistrationContextMap.put(registrationID, context);
      }

      return registrationID;
   }

   /*
    * (non-Javadoc)
    * @see javax.security.auth.message.config.AuthConfigFactory#removeRegistration(java.lang.String)
    */
   public boolean removeRegistration(String registrationID)
   {
      if (registrationID == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("registrationID");

      RegistrationListener listener = this.keyToRegistrationListenerMap.get(registrationID);
      RegistrationContext rc = this.keyToRegistrationContextMap.get(registrationID);

      // remove the provider and notify listener of the change.
      boolean removed = this.keyToAuthConfigProviderMap.containsKey(registrationID);
      this.keyToAuthConfigProviderMap.remove(registrationID);
      if (removed && listener != null)
         listener.notify(rc.getMessageLayer(), rc.getAppContext());
      this.keyToRegistrationContextMap.remove(registrationID);

      return removed;
   }

   static class JBossRegistrationContext implements RegistrationContext {

       private String messageLayer;

       private String appContext;

       private String description;

       private boolean isPersistent;

       JBossRegistrationContext(String layer, String appContext, String description, boolean isPersistent)
       {
          this.messageLayer = layer;
          this.appContext = appContext;
          this.description = description;
          this.isPersistent = isPersistent;
       }

       public String getAppContext()
       {
          return this.appContext;
       }
       
       public void setAppContext(String appContext)
       {
          this.appContext = appContext;
       }

       public String getDescription()
       {
          return this.description;
       }
       
       public void setDescription(String description)
       {
          this.description = description;
       }

       public String getMessageLayer()
       {
          return this.messageLayer;
       }

       public void setMessageLayer(String messageLayer)
       {
          this.messageLayer = messageLayer;
       }

       public boolean isPersistent()
       {
          return this.isPersistent;
       }

       public void setIsPersistent(boolean isPersistent)
       {
          this.isPersistent = isPersistent;
       }
   }       
}