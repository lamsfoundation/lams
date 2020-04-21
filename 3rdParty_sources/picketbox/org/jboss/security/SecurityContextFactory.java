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
package org.jboss.security;

import java.lang.reflect.Constructor;
import java.security.Principal;
import javax.security.auth.Subject;
 

/**
 *  Factory class to create Security Context instances
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @since  Dec 28, 2006 
 *  @version $Revision$
 */
@SuppressWarnings("unchecked")
public class SecurityContextFactory
{
   private static final Class[] CONTEXT_CONSTRUCTOR_TYPES = new Class[]{String.class};

   private static final Class[] CONTEXT_UTIL_CONSTRUCTOR_TYPES = new Class[]{SecurityContext.class};

   private static String defaultFQN = "org.jboss.security.plugins.JBossSecurityContext";
   
   private static String defaultUtilClassFQN = "org.jboss.security.plugins.JBossSecurityContextUtil";
   
   private static Class<? extends SecurityContext> defaultSecurityContextClass = null;

   private static Constructor<SecurityContext> defaultSecurityContextConstructor = null;

   private static Class<? extends SecurityContextUtil> defaultUtilClass = null;

   private static Constructor<SecurityContextUtil> defaultUtilConstructor =  null;
   
   /**
    * Classloader.loadClass is a synchronized method in the JDK. Under heavy concurrent requests,
    * a loadClass() operation can be extremely troublesome for performance
    */
   static
   {
      try
      {
         defaultSecurityContextClass = (Class<? extends SecurityContext>) SecuritySPIActions.getCurrentClassLoader(SecurityContextFactory.class).loadClass(defaultFQN);
      }
      catch(Exception ignore)
      {
         try
         {
            defaultSecurityContextClass = (Class<? extends SecurityContext>) SecuritySPIActions.getContextClassLoader().loadClass(defaultFQN);
         }
         catch (Exception e)
         {
         }
      }
      try
      {
         defaultUtilClass = (Class<? extends SecurityContextUtil>) SecuritySPIActions.getCurrentClassLoader(SecurityContextFactory.class).loadClass(defaultUtilClassFQN);
      }
      catch(Exception ignore)
      {
         try
         {
            defaultUtilClass = (Class<? extends SecurityContextUtil>) SecuritySPIActions.getContextClassLoader().loadClass(defaultUtilClassFQN);
         }
         catch(Exception e)
         {
         }
      }
      if (defaultSecurityContextClass != null) {
         try
         {
            defaultSecurityContextConstructor = (Constructor<SecurityContext>) defaultSecurityContextClass.getConstructor(CONTEXT_CONSTRUCTOR_TYPES);
         }
         catch(Exception e)
         {
         }
      }
   }

   /**
    * Create a security context 
    * @param securityDomain Security Domain driving the context
    * @return
    * @throws Exception 
    */
   public static SecurityContext createSecurityContext(String securityDomain) throws Exception
   {
      if (defaultSecurityContextConstructor != null)
          return createSecurityContext(securityDomain, defaultSecurityContextConstructor);
      if(defaultSecurityContextClass != null)
         return createSecurityContext(securityDomain, defaultSecurityContextClass);
      return createSecurityContext(securityDomain, defaultFQN, SecuritySPIActions.getCurrentClassLoader(SecurityContextFactory.class));
   }
   
   /**
    * Create a security context 
    * @param securityDomain Security Domain driving the context
    * @param classLoader ClassLoader to use
    * @return
    * @throws Exception 
    */
   public static SecurityContext createSecurityContext(String securityDomain, ClassLoader classLoader) throws Exception
   {
      if (defaultSecurityContextConstructor != null)
         return createSecurityContext(securityDomain, defaultSecurityContextConstructor);
      if(defaultSecurityContextClass != null)
         return createSecurityContext(securityDomain, defaultSecurityContextClass);
      return createSecurityContext(securityDomain, defaultFQN, classLoader);
   }
   
   /**
    * Construct a SecurityContext
    * @param securityDomain  The Security Domain
    * @param fqnClass  Fully Qualified Name of the SecurityContext Class
    * @return an instance of SecurityContext
    * @throws Exception
    */
   public static SecurityContext createSecurityContext(String securityDomain,
         String fqnClass) throws Exception
   {
      return createSecurityContext(securityDomain, fqnClass, SecuritySPIActions.getCurrentClassLoader(SecurityContextFactory.class));
   }
   
   /**
    * Construct a SecurityContext
    * @param securityDomain  The Security Domain
    * @param fqnClass  Fully Qualified Name of the SecurityContext Class
    * @param classLoader ClassLoader to use
    * @return an instance of SecurityContext
    * @throws Exception
    */
   public static SecurityContext createSecurityContext(String securityDomain,
         String fqnClass, ClassLoader classLoader) throws Exception
   {
      if(securityDomain == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("security domain");
      if(fqnClass == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("fqnClass");
      defaultSecurityContextClass = getContextClass(fqnClass, classLoader);
      defaultSecurityContextConstructor = (Constructor<SecurityContext>) defaultSecurityContextClass.getConstructor(CONTEXT_CONSTRUCTOR_TYPES);
      return createSecurityContext(securityDomain, defaultSecurityContextConstructor);
   }
   
   
   /**
    * Create a security context given the class
    * This method exists because classloader.loadClass is an expensive
    * operation due to synchronization
    * @param securityDomain
    * @param clazz
    * @return
    * @throws Exception
    */
   public static SecurityContext createSecurityContext(String securityDomain,
         Class<? extends SecurityContext> clazz) throws Exception
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityContextFactory.class.getName() + ".createSecurityContext"));
      }
      if(securityDomain == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("security domain");
       if(clazz == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("clazz");
      //Get the CTR
      Constructor<? extends SecurityContext> ctr = clazz.getConstructor(CONTEXT_CONSTRUCTOR_TYPES);
      return ctr.newInstance(securityDomain);
   }

   private static SecurityContext createSecurityContext(String securityDomain, Constructor<SecurityContext> constructor) throws Exception
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityContextFactory.class.getName() + ".createSecurityContext"));
      }
      if (securityDomain == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("security domain");
      if (constructor == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("constructor");
      return constructor.newInstance(securityDomain);
   }

   /**
    * Create a security context
    * @param p Principal
    * @param cred Credential
    * @param s Subject
    * @param securityDomain SecurityDomain
    * @return
    * @throws Exception 
    * @see #createSecurityContext(String)
    */
   public static SecurityContext createSecurityContext(Principal p, 
         Object cred, Subject s, String securityDomain) throws Exception
   {
      return createSecurityContext(p, cred, s, securityDomain, SecuritySPIActions.getCurrentClassLoader(SecurityContextFactory.class));
   }
   
   /**
    * Create a security context
    * @param p Principal
    * @param cred Credential
    * @param s Subject
    * @param securityDomain SecurityDomain
    * @param classLoader ClassLoader to use
    * @return
    * @throws Exception 
    * @see #createSecurityContext(String)
    */
   public static SecurityContext createSecurityContext(Principal p, 
         Object cred, Subject s, String securityDomain, ClassLoader classLoader) throws Exception
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityContextFactory.class.getName() + ".createSecurityContext"));
      }
      SecurityContext jsc = createSecurityContext(securityDomain, classLoader);
      jsc.getUtil().createSubjectInfo(p,cred,s);
      return jsc;
   }
   
   /**
    * Create a security context
    * @param p Principal
    * @param cred Credential
    * @param s Subject
    * @param securityDomain SecurityDomain
    * @param fqnClass FQN of the SecurityContext class to be instantiated
    * @param classLoader ClassLoader to use
    * @return
    * @see #createSecurityContext(String)
    * @throws Exception
    */
   public static SecurityContext createSecurityContext(Principal p, 
         Object cred,Subject s, String securityDomain, String fqnClass, ClassLoader classLoader) 
   throws Exception
   {
      SecurityContext sc = createSecurityContext(securityDomain, fqnClass, classLoader);
      sc.getUtil().createSubjectInfo(p,cred,s);
      return sc;
   }
   
   /**
    * Return an instance of the SecurityContextUtil
    * @param sc SecurityContext
    * @return
    */
   public static SecurityContextUtil createUtil(SecurityContext sc) throws Exception
   {
       SecurityManager sm = System.getSecurityManager();
       if (sm != null) {
           sm.checkPermission(new RuntimePermission(SecurityContextFactory.class.getName() + ".createUtil"));
       }
       Constructor<SecurityContextUtil> ctr = defaultUtilConstructor;

       if(ctr == null)
       {
           Class<? extends SecurityContextUtil> clazz = (Class<? extends SecurityContextUtil>) loadClass(defaultUtilClassFQN,  SecuritySPIActions.getCurrentClassLoader(SecurityContextFactory.class));
           defaultUtilClass = clazz;
           ctr = defaultUtilConstructor = (Constructor<SecurityContextUtil>) clazz.getConstructor(CONTEXT_UTIL_CONSTRUCTOR_TYPES);
       }
       return ctr.newInstance(sc);
   }
   
   /**
    * Return an instance of the SecurityContextUtil
    * @param sc SecurityContext
    * @param classLoader ClassLoader to use
    * @return
    */
   public static SecurityContextUtil createUtil(SecurityContext sc, ClassLoader classLoader) throws Exception
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityContextFactory.class.getName() + ".createUtil"));
      }
      Constructor<SecurityContextUtil> ctr = defaultUtilConstructor;
      
      if(ctr == null)
      {
         Class<? extends SecurityContextUtil> clazz = (Class<? extends SecurityContextUtil>) loadClass(defaultUtilClassFQN, classLoader);
         defaultUtilClass = clazz;
         ctr = defaultUtilConstructor = (Constructor<SecurityContextUtil>) clazz.getConstructor(CONTEXT_UTIL_CONSTRUCTOR_TYPES);
      }
      
      return ctr.newInstance(sc);
   }
   
   /**
    * Return an instance of the SecurityContextUtil given a FQN of the util class
    * @param sc SecurityContext
    * @param utilFQN fqn of the util class
    * @return
    */ 
   public static SecurityContextUtil createUtil(SecurityContext sc, String utilFQN) throws Exception
   {
      return createUtil(sc, utilFQN, SecuritySPIActions.getCurrentClassLoader(SecurityContextFactory.class));
   }
   
   /**
    * Return an instance of the SecurityContextUtil given a FQN of the util class
    * @param sc SecurityContext
    * @param utilFQN fqn of the util class
    * @param classLoader ClassLoader to use
    * @return
    */ 
   public static SecurityContextUtil createUtil(SecurityContext sc, String utilFQN, ClassLoader classLoader) throws Exception
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityContextFactory.class.getName() + ".createUtil"));
      }
      Class<?> clazz = loadClass(utilFQN, classLoader);
      //Get the CTR
      Constructor<? extends SecurityContextUtil> ctr = 
         (Constructor<? extends SecurityContextUtil>) clazz.getConstructor(CONTEXT_UTIL_CONSTRUCTOR_TYPES);
      return ctr.newInstance(sc);
   }
   
   /**
    * Return an instance of the SecurityContextUtil given a Class instance of the util class
    * @param sc SecurityContext
    * @return
    */
   public static SecurityContextUtil createUtil(SecurityContext sc, 
         Class<? extends SecurityContextUtil> utilClazz) throws Exception
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityContextFactory.class.getName() + ".createUtil"));
      }
      //Get the CTR
      Constructor<? extends SecurityContextUtil> ctr = utilClazz.getConstructor(new Class[]{SecurityContext.class});
      return ctr.newInstance(new Object[]{sc}); 
   }
   
   /**
    * Set the default security context fqn
    * @param fqn
    */
   public static void setDefaultSecurityContextFQN(String fqn)
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityContextFactory.class.getName() + ".setDefaultSecurityContextFQN"));
      }
      defaultFQN = fqn;
      defaultSecurityContextClass = null;
      defaultSecurityContextConstructor = null;
   }
   
   
   /**
    * Set the default util class fqn
    * @param fqn
    */
   public static void setDefaultSecurityContextUtilFQN(String fqn)
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityContextFactory.class.getName() + ".setDefaultSecurityContextUtilFQN"));
      }
      defaultUtilClassFQN = fqn;
      defaultUtilClass = null; //reset
      defaultUtilConstructor = null;
   }
   
   /**
    * Load a class
    * @param fqn
    * @param classLoader
    * @return
    * @throws Exception
    */
   private static Class<?> loadClass(String fqn, ClassLoader classLoader) throws Exception
   {
      try
      {
         return classLoader.loadClass(fqn);
      }
      catch (Exception e)
      {
         ClassLoader tcl = SecuritySPIActions.getContextClassLoader();
         return tcl.loadClass(fqn);
      }
   }
    
   private static Class<SecurityContext> getContextClass(String className, ClassLoader classLoader) throws Exception
   {
      try
      {
         return (Class<SecurityContext>) classLoader.loadClass(className);
      }
      catch (Exception e)
      {
         ClassLoader tcl = SecuritySPIActions.getContextClassLoader();
         return (Class<SecurityContext>) tcl.loadClass(className);
      }
   }
}