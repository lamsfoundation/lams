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
package org.jboss.security.plugins.auth;

import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Iterator;
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
 * @version $Revision: 65313 $
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

   private static class GetSubjectAction implements PrivilegedExceptionAction<Subject>
   {
      static PrivilegedExceptionAction<Subject> ACTION = new GetSubjectAction();
      public Subject run() throws PolicyContextException
      {
         return (Subject) PolicyContext.getContext(SecurityConstants.SUBJECT_CONTEXT_KEY);  
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
      
      @SuppressWarnings({"unchecked", "rawtypes"})
      public Object run()
      {
         Set principals = fromSubject.getPrincipals();
         Set principals2 = toSubject.getPrincipals();
         Iterator<Principal> iter = principals.iterator();
         while( iter.hasNext() )
            principals2.add(getCloneIfNeeded(iter.next()));  
         Set privateCreds = fromSubject.getPrivateCredentials();
         Set privateCreds2 = toSubject.getPrivateCredentials();
         iter = privateCreds.iterator();
         while( iter.hasNext() )
            privateCreds2.add(getCloneIfNeeded(iter.next()));
         Set publicCreds = fromSubject.getPublicCredentials();
         Set publicCreds2 = toSubject.getPublicCredentials();
         iter = publicCreds.iterator();
         while( iter.hasNext() )
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
            SecurityContextAssociation.clearSecurityContext();
         }
      };

      void push(Principal principal, Object credential, Subject subject, String securityDomain);
      void pop();
   }

   static Subject getActiveSubject() throws PrivilegedActionException
   {
      Subject subject = (Subject) AccessController.doPrivileged(GetSubjectAction.ACTION);
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
         LoginContext lc = (LoginContext) AccessController.doPrivileged(action);
         return lc;
      }
      catch(PrivilegedActionException e)
      {
         Exception ex = e.getException();
         if( ex instanceof LoginException )
            throw (LoginException) ex;
         else
            throw new LoginException(ex.getMessage());
      }
   } 
   
   static void setContextClassLoader(final ClassLoader cl)
   {
	   AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() 
	   {
		   public ClassLoader run() 
		   {
			   Thread.currentThread().setContextClassLoader(cl);
			   return null;
		   }
	   });
   }
   
   static ClassLoader getContextClassLoader()
   {
      ClassLoader loader = (ClassLoader) AccessController.doPrivileged(GetTCLAction.ACTION);
      return loader;
   }

   static Object setContextInfo(String key, Object value)
   {
      SetContextInfoAction action = new SetContextInfoAction(key, value);
      Object prevInfo = AccessController.doPrivileged(action);
      return prevInfo;
   }

   static String toString(Subject subject)
   {
      ToStringSubjectAction action = new ToStringSubjectAction(subject);
      String info = (String) AccessController.doPrivileged(action);
      return info;
   }
}