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
package org.jboss.security.plugins;

import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.security.jacc.PolicyContext;
import javax.security.jacc.PolicyContextException;

import org.jboss.security.SecurityConstants;
import org.jboss.security.SecurityContext;
import org.jboss.security.SecurityContextAssociation;
import org.jboss.security.SecurityContextFactory;

/** Common PrivilegedAction used by classes in this package.
 * 
 * @author Scott.Stark@jboss.org
 * @author Anil.Saldhana@redhat.com
 * @version $Revision$
 */
class SubjectActions
{
   private static class ToStringSubjectAction implements PrivilegedAction<String>
   {
      Subject subject;
      ToStringSubjectAction(Subject subject)
      {
         this.subject = subject;
      }
      public String run()
      {
         StringBuffer tmp = new StringBuffer();
         tmp.append("Subject(");
         tmp.append(System.identityHashCode(subject));
         tmp.append(").principals=");
         Iterator<Principal> principals = subject.getPrincipals().iterator();
         while( principals.hasNext() )
         {
            Object p = principals.next();
            Class<?> c = p.getClass();
            tmp.append(c.getName());
            tmp.append('@');
            tmp.append(System.identityHashCode(c));
            tmp.append('(');
            tmp.append(p);
            tmp.append(')');
         }
         return tmp.toString();
      }
   }

   private static class GetSubjectAction implements PrivilegedAction<Subject>
   {
      static PrivilegedAction<Subject> ACTION = new GetSubjectAction();
      public Subject run()
      {
         Subject subject = null;
         try
         {
            subject = (Subject) PolicyContext.getContext(SecurityConstants.SUBJECT_CONTEXT_KEY);
         }
         catch(PolicyContextException pce)
         {
            SecurityContext sc = getSecurityContext();
            subject = sc.getUtil().getSubject();
         }
         return subject;
      }
   }

   private static class CopySubjectAction implements PrivilegedAction<Object>
   {
      Subject fromSubject;
      Subject toSubject;
      boolean setReadOnly;
      boolean deepCopy;
      
      CopySubjectAction(Subject fromSubject, Subject toSubject, boolean setReadOnly)
      {
         this.fromSubject = fromSubject;
         this.toSubject = toSubject;
         this.setReadOnly = setReadOnly;
      }
      public void setDeepCopy(boolean flag)
      {
         this.deepCopy = flag;
      }
      
      public Object run()
      {
         Set<Principal> principals = fromSubject.getPrincipals();
         Set<Principal> principals2 = toSubject.getPrincipals();
         Iterator<Principal> iter = principals.iterator();
         while( iter.hasNext() )
            principals2.add((Principal) getCloneIfNeeded(iter.next()));  
         Set<Object> privateCreds = fromSubject.getPrivateCredentials();
         Set<Object> privateCreds2 = toSubject.getPrivateCredentials();
         Iterator<Object> iterCred = privateCreds.iterator();
         while( iterCred.hasNext() )
            privateCreds2.add(getCloneIfNeeded(iter.next()));
         Set<Object> publicCreds = fromSubject.getPublicCredentials();
         Set<Object> publicCreds2 = toSubject.getPublicCredentials();
         iterCred = publicCreds.iterator();
         while( iterCred.hasNext() )
            publicCreds2.add(getCloneIfNeeded(iter.next()));
         if( setReadOnly == true )
            toSubject.setReadOnly();
         return null;
      }
      
      /** Check if the deepCopy flag is ON &&
       *  Object implements Cloneable and return cloned object */
      private Object getCloneIfNeeded(Object obj)
      {
         Object clonedObject = null;
         if(this.deepCopy && obj instanceof Cloneable)
         {
            Class<?> clazz = obj.getClass();
            try
            {
               Method cloneMethod = clazz.getMethod("clone", (Class[])null);
               clonedObject = cloneMethod.invoke(obj, (Object[])null);
   }
            catch (Exception e)
            {//Ignore non-cloneable issues 
            } 
         }
         if(clonedObject == null)
            clonedObject = obj;
         return clonedObject;
      }
   }

   private static class LoginContextAction implements PrivilegedExceptionAction<LoginContext>
   {
      String securityDomain;
      Subject subject;
      CallbackHandler handler;
      LoginContextAction(String securityDomain, Subject subject,
         CallbackHandler handler)
      {
         this.securityDomain = securityDomain;
         this.subject = subject;
         this.handler = handler;
      }
      public LoginContext run() throws Exception
      {
         LoginContext lc = new LoginContext(securityDomain, subject, handler);
         return lc;
      }
   }

   private static class GetTCLAction implements PrivilegedAction<ClassLoader>
   {
      static PrivilegedAction<ClassLoader> ACTION = new GetTCLAction();
      public ClassLoader run()
      {
         ClassLoader loader = Thread.currentThread().getContextClassLoader();
         return loader;
      }
   }
   
   private static class SetTCLAction implements PrivilegedAction<Void>
   {
      ClassLoader cl;
      
      SetTCLAction(ClassLoader cl)
      {
         this.cl = cl;
      }
      
      public Void run()
      {
         Thread.currentThread().setContextClassLoader(cl);
         return null;
      }
   }

   private static class SetContextInfoAction implements PrivilegedAction<Object>
   {
      String key;
      Object value;
      SetContextInfoAction(String key, Object value)
      {
         this.key = key;
         this.value = value;
      }
      public Object run()
      {
         //Set it on the current security context also
         SecurityContext sc = SecurityContextAssociation.getSecurityContext();
         if(sc != null)
         {
            sc.getData().put(key, value);
         }
         return SecurityContextAssociation.setContextInfo(key, value);
      }
   }

   interface PrincipalInfoAction
   {
      PrincipalInfoAction PRIVILEGED = new PrincipalInfoAction()
      {
         public void push(final Principal principal, final Object credential,
            final Subject subject, final String securityDomain) 
         {
            AccessController.doPrivileged(
               new PrivilegedAction<Object>()
               {
                  public Object run()
                  {
                     //SecurityAssociation.pushSubjectContext(subject, principal, credential);
                     SecurityContext sc = SecurityContextAssociation.getSecurityContext();
                     if(sc == null)
                     {
                        try
                        {
                           sc = SecurityContextFactory.createSecurityContext(principal, credential,
                                 subject, securityDomain);
                        }
                        catch (Exception e)
                        {
                           throw new RuntimeException(e);
                        }
                     }
                     SecurityContextAssociation.setSecurityContext(sc);
                     return null;
                  }
               }
            );
         }
         public void pop()
         {
            AccessController.doPrivileged(
               new PrivilegedAction<Object>()
               {
                  public Object run()
                  {
                     //SecurityAssociation.popSubjectContext();
                     SecurityContextAssociation.clearSecurityContext();
                     return null;
                  }
               }
            );
         }
      };

      PrincipalInfoAction NON_PRIVILEGED = new PrincipalInfoAction()
      {
         public void push(Principal principal, Object credential, Subject subject,
               String securityDomain)
         {
            //SecurityAssociation.pushSubjectContext(subject, principal, credential);
            SecurityContext sc = SecurityContextAssociation.getSecurityContext();
            if(sc == null)
            {
               try
               {
                  sc = SecurityContextFactory.createSecurityContext(principal, credential,
                        subject, securityDomain);
               }
               catch (Exception e)
               {
                  throw new RuntimeException(e);
               }
            }
            else
            {
               sc.getUtil().createSubjectInfo(principal, credential, subject); 
            }
            SecurityContextAssociation.setSecurityContext(sc); 
         }
         public void pop()
         {
            //SecurityAssociation.popSubjectContext();
            SecurityContextAssociation.clearSecurityContext();
         }
      };

      void push(Principal principal, Object credential, Subject subject, String securityDomain);
      void pop();
   }

   static Subject getActiveSubject()
   {
      Subject subject = AccessController.doPrivileged(GetSubjectAction.ACTION);
      return subject;
   }
   static void copySubject(Subject fromSubject, Subject toSubject)
   {
      copySubject(fromSubject, toSubject, false);
   }
   static void copySubject(Subject fromSubject, Subject toSubject, boolean setReadOnly)
   {
      CopySubjectAction action = new CopySubjectAction(fromSubject, toSubject, setReadOnly);
      if( System.getSecurityManager() != null )
         AccessController.doPrivileged(action);
      else
         action.run();
   }

   static void copySubject(Subject fromSubject, Subject toSubject, boolean setReadOnly,
         boolean deepCopy)
   {
      CopySubjectAction action = new CopySubjectAction(fromSubject, toSubject, setReadOnly);
      action.setDeepCopy(deepCopy);
      if( System.getSecurityManager() != null )
         AccessController.doPrivileged(action);
      else
         action.run();
   }

   static LoginContext createLoginContext(String securityDomain, Subject subject,
      CallbackHandler handler)
      throws LoginException
   {
      LoginContextAction action = new LoginContextAction(securityDomain, subject, handler);
      try
      {
         LoginContext lc = AccessController.doPrivileged(action);
         return lc;
      }
      catch(PrivilegedActionException e)
      {
         Exception ex = e.getException();
         if( ex instanceof LoginException )
            throw (LoginException) ex;
         else
            throw new LoginException(ex.getLocalizedMessage());
      }
   } 
   
   static ClassLoader getContextClassLoader()
   {
      ClassLoader loader = AccessController.doPrivileged(GetTCLAction.ACTION);
      return loader;
   }
   
   static void setContextClassLoader(ClassLoader cl)
   {
      AccessController.doPrivileged(new SetTCLAction(cl));
   }

   static Object setContextInfo(String key, Object value)
   {
      SetContextInfoAction action = new SetContextInfoAction(key, value);
      Object prevInfo = AccessController.doPrivileged(action);
      return prevInfo;
   }

   static LoginException getContextLoginException()
   {
      LoginException exp = null;
      SecurityContext sc = getSecurityContext();
      if (sc != null) {
          Map<String, Object> ctxInfo = sc.getData();
          if (ctxInfo != null) {
              for (Object obj: ctxInfo.values()) {
                  if (obj != null && obj instanceof LoginException) {
                      return (LoginException)obj;
                  }
              }
          }
      }
      return exp;
   }

   static void pushSubjectContext(Principal principal, Object credential,
      Subject subject, String securityDomain)
   {
      if(System.getSecurityManager() == null)
      {
         PrincipalInfoAction.NON_PRIVILEGED.push(principal, credential, subject, securityDomain);
      }
      else
      {
         PrincipalInfoAction.PRIVILEGED.push(principal, credential, subject, securityDomain);
      }
   }
   static void popSubjectContext()
   {
      if(System.getSecurityManager() == null)
      {
         PrincipalInfoAction.NON_PRIVILEGED.pop();
      }
      else
      {
         PrincipalInfoAction.PRIVILEGED.pop();
      }
   }

   
   static String toString(Subject subject)
   {
      ToStringSubjectAction action = new ToStringSubjectAction(subject);
      String info = (String) AccessController.doPrivileged(action);
      return info;
   }
   
   static SecurityContext getSecurityContext()
   { 
      return AccessController.doPrivileged(new PrivilegedAction<SecurityContext>(){

         public SecurityContext run()
         {   
            return SecurityContextAssociation.getSecurityContext();
         }});
   }
   
   static void setSecurityContext(final SecurityContext sc)
   { 
      AccessController.doPrivileged(new PrivilegedAction<SecurityContext>(){

         public SecurityContext run()
         { 
            SecurityContextAssociation.setSecurityContext(sc);
            return null;
         }});
   }
   
   /**
    * Indicates whether the user has requested a refresh of the security context roles
    * via a system property ("jbosssx.context.roles.refresh") which is either "true"
    * or "false". default is "false"
    * TODO: Externalize this system property setting such that it is passed as a map of
    * options on the AuthorizationManagerService to be passed to AuthorizationManagers via
    * the optional setOptions(Properties props) method
    * @return
    */
   static String getRefreshSecurityContextRoles()
   {
      return  AccessController.doPrivileged(new PrivilegedAction<String>()
      { 
         public String run()
         {
            return System.getProperty("jbosssx.context.roles.refresh","false"); 
         }}
      );
   }
   
   static String getSystemProperty(final String key, final String defaultValue)
   {
      return AccessController.doPrivileged(new PrivilegedAction<String>()
      { 
         public String run()
         {
            return System.getProperty(key,defaultValue); 
         }}
      );
   } 
   
   static Principal getPrincipal()
   {
      return AccessController.doPrivileged(new PrivilegedAction<Principal>()
      {
         public Principal run()
         {
            Principal principal = null;
            SecurityContext sc = getSecurityContext();
            if(sc != null)
            {
               principal = sc.getUtil().getUserPrincipal();
            }
            return principal;
         }
      });
   }

   static Object getCredential()
   {
      return AccessController.doPrivileged(new PrivilegedAction<Object>()
      {
         public Object run()
         {
            Object credential = null;
            SecurityContext sc = getSecurityContext();
            if(sc != null)
            {
               credential = sc.getUtil().getCredential();
            }
            return credential;
         }
      });
   }
}
