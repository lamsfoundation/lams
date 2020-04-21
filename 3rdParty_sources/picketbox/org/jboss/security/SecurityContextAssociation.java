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
 
import java.security.AccessController;
import java.security.Principal;
import java.security.PrivilegedAction;

import javax.security.auth.Subject;


/**
 *  Security Context association in a threadlocal
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @since  Dec 27, 2006 
 *  @version $Revision$
 */
public class SecurityContextAssociation
{
   /**
    * A flag that denotes whether SCA operates in a client side vm-wide mode
    */
   private static boolean SERVER = true;
   
   private static SecurityContext securityContext = null;
   
   private static RuntimePermission SetSecurityContextPermission = 
      new RuntimePermission("org.jboss.security.setSecurityContext");
   
   private static RuntimePermission GetSecurityContextPermission = 
      new RuntimePermission("org.jboss.security.getSecurityContext");
   
   private static RuntimePermission ClearSecurityContextPermission = 
      new RuntimePermission("org.jboss.security.clearSecurityContext");
   
   private static final RuntimePermission SetRunAsIdentity =
      new RuntimePermission("org.jboss.security.setRunAsRole");
   
   private static final RuntimePermission GetContextInfo =
      new RuntimePermission("org.jboss.security.accessContextInfo", "get");

   private static final RuntimePermission SetContextInfo =
      new RuntimePermission("org.jboss.security.accessContextInfo", "set");
   
   /**
    * Flag to indicate whether threads that are spawned inherit the security context from parent
    * Set this to false if you do not want inheritance. By default the context is inherited.
    */
   public static final String SECURITYCONTEXT_THREADLOCAL = "org.jboss.security.context.ThreadLocal";
   
   /**
    * In JBoss AS4, the SecurityAssociation inheritance is managed with a different system property
    * This flag should be private and not visible.
    */
   private static final String SECURITYASSOCIATION_THREADLOCAL = "org.jboss.security.SecurityAssociation.ThreadLocal";
   
   private static ThreadLocal<SecurityContext> securityContextLocal ;
   
   static
   {
      String saflag = getSystemProperty(SECURITYASSOCIATION_THREADLOCAL, "false");
      String scflag = getSystemProperty(SECURITYCONTEXT_THREADLOCAL, "false");
      
      boolean useThreadLocal = Boolean.valueOf(saflag).booleanValue() || Boolean.valueOf(scflag).booleanValue();
      
      if(useThreadLocal)
      {
         securityContextLocal = new ThreadLocal<SecurityContext>();
      }
      else
      {
         securityContextLocal = new InheritableThreadLocal<SecurityContext>();
      }
   }
   
   /**
    * Indicates whether we are on the client side
    * @return
    */
   public static boolean isClient()
   {
      return !SERVER;
   }
   
   /**
    * Set the VM-wide client side usage
    */
   public static void setClient()
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityContextAssociation.class.getName() + ".setClient"));
      }
     SERVER = false;
   }
   
   /**
    * Set a security context 
    * @param sc
    */
   public static void setSecurityContext(SecurityContext sc)
   { 
      SecurityManager sm = System.getSecurityManager();
      if(sm != null)
         sm.checkPermission(SetSecurityContextPermission);
      
      if(!SERVER)
         securityContext = sc;
      else
      {
         if(sc == null)
            securityContextLocal.remove();
         else
            securityContextLocal.set(sc); 
      }
   }
   
   /**
    * Get a security context
    * @return
    */
   public static SecurityContext getSecurityContext()
   {
      SecurityManager sm = System.getSecurityManager();
      if(sm != null)
         sm.checkPermission(GetSecurityContextPermission);
      
      if(!SERVER)
         return securityContext;
      
      return securityContextLocal.get();
   } 
   
   /**
    * Clear the current security context
    */
   public static void clearSecurityContext() 
   {
      SecurityManager sm = System.getSecurityManager();
      if(sm != null)
         sm.checkPermission(ClearSecurityContextPermission);
      
      if(!SERVER)
         securityContext = null;
      else
         securityContextLocal.remove();
   }
   
   /**
    * Pushes a RunAs identity
    * 
    * @param runAs
    */
   public static void pushRunAsIdentity(RunAs runAs)
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
         sm.checkPermission(SetRunAsIdentity);
      
      SecurityContext sc = getSecurityContext();
      if (sc != null)
      {
         sc.setOutgoingRunAs(runAs);
      }
   }
   
   /**
    * Pops a RunAs identity
    * 
    * @return
    */
   public static RunAs popRunAsIdentity()
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
         sm.checkPermission(SetRunAsIdentity);
      
      SecurityContext sc = SecurityContextAssociation.getSecurityContext();
      RunAs ra = null;
      if (sc != null)
      {
         ra = (RunAs) sc.getOutgoingRunAs();
         sc.setOutgoingRunAs(null);
      }
      return ra;
   }
   
   /**
    * Look at the current thread of control's run-as identity
    */
   public static RunAs peekRunAsIdentity()
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityContextAssociation.class.getName() + ".peekRunAsIdentity"));
      }
      RunAs ra = null;
      SecurityContext sc = SecurityContextAssociation.getSecurityContext();
      if (sc != null)
      {
         ra = (RunAs) sc.getOutgoingRunAs();
      }
      return ra;
   }
   
   /**
    * Get the current thread context info. If a security manager is present,
    * then this method calls the security manager's <code>checkPermission</code>
    * method with a <code> RuntimePermission("org.jboss.security.accessContextInfo",
    * "get") </code> permission to ensure it's ok to access context information.
    * If not, a <code>SecurityException</code> will be thrown.
    * @param key - the context key
    * @return the mapping for the key in the current thread context
    */
   public static Object getContextInfo(String key)
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
         sm.checkPermission(GetContextInfo);

      if (key == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("key");
      //SECURITY-459 get it from the current security context
      SecurityContext sc = getSecurityContext();
      if (sc != null)
         return sc.getData().get(key);
      return null;
   }
   
   /**
    * Set the current thread context info. If a security manager is present,
    * then this method calls the security manager's <code>checkPermission</code>
    * method with a <code> RuntimePermission("org.jboss.security.accessContextInfo",
    * "set") </code> permission to ensure it's ok to access context information.
    * If not, a <code>SecurityException</code> will be thrown.
    * @param key - the context key
    * @param value - the context value to associate under key
    * @return the previous mapping for the key if one exists
    */
   public static Object setContextInfo(String key, Object value)
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
         sm.checkPermission(SetContextInfo);

      SecurityContext sc = getSecurityContext();
      if (sc != null)
         return sc.getData().put(key, value);
      return null;
   }
   
   private static String getSystemProperty(final String propertyName, final String defaultString)
   {
      return AccessController.doPrivileged(new PrivilegedAction<String>()
      {
         public String run()
         { 
            return System.getProperty(propertyName, defaultString);
         }
      });
   }
   
   public static Subject getSubject()
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
         sm.checkPermission(GetSecurityContextPermission);
      
      SecurityContext sc = getSecurityContext();
      if (sc != null)
         return sc.getUtil().getSubject();
      return null;
   }
   
   public static Principal getPrincipal()
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
         sm.checkPermission(GetSecurityContextPermission);
      
      SecurityContext sc = getSecurityContext();
      if (sc != null)
         return sc.getUtil().getUserPrincipal();
      return null;
   }
   
   public static void setPrincipal(Principal principal)
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
         sm.checkPermission(SetSecurityContextPermission);

      SecurityContext securityContext = SecurityContextAssociation.getSecurityContext();
      if (securityContext == null)
      {
         try
         {
            securityContext = SecurityContextFactory.createSecurityContext("CLIENT_SIDE");
         }
         catch (Exception e)
         {
            throw new RuntimeException(e);
         }
         SecurityContextAssociation.setSecurityContext(securityContext);
      }
      Object credential = securityContext.getUtil().getCredential();
      Subject subj = securityContext.getUtil().getSubject();
      securityContext.getUtil().createSubjectInfo(principal, credential, subj);
   }
   
   public static Object getCredential()
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
         sm.checkPermission(GetSecurityContextPermission);
      
      SecurityContext sc = getSecurityContext();
      if (sc != null)
         return sc.getUtil().getCredential();
      return null;
   }
   
   public static void setCredential(Object credential)
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
         sm.checkPermission(SetSecurityContextPermission);
      
      SecurityContext securityContext = SecurityContextAssociation.getSecurityContext();
      if (securityContext == null)
      {
         try
         {
            securityContext = SecurityContextFactory.createSecurityContext("CLIENT_SIDE");
         }
         catch (Exception e)
         {
            throw new RuntimeException(e);
         }
         SecurityContextAssociation.setSecurityContext(securityContext);
      }
      Principal principal = securityContext.getUtil().getUserPrincipal();
      Subject subj = securityContext.getUtil().getSubject();
      securityContext.getUtil().createSubjectInfo(principal, credential, subj);
   }
}