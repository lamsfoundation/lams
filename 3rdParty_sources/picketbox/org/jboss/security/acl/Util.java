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
package org.jboss.security.acl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Locale;

import org.jboss.security.PicketBoxMessages;
import org.jboss.security.authorization.Resource;
import org.jboss.security.identity.Identity;
import org.jboss.security.identity.plugins.IdentityFactory;

/**
 * <p>
 * Utility class used by the {@code ACL} implementation.
 * </p>
 * 
 * @author <a href="mailto:sguilhen@redhat.com">Stefan Guilhen</a>
 */
public class Util
{

   /**
    * <p>
    * Returns a {@code String} representation of the specified resource. The format of this representation
    * is as follows: {@code resource_fqn:resource_id}, where {@code resource_fqn} is the fully-qualified
    * name of the resource class, and {@code resource_id} is the unique identifier of the resource.
    * </p>
    * 
    * @param resource   the {@code Resource} to be converted.
    * @return   a {@code String} representation of the resource.
    */
   public static String getResourceAsString(Resource resource)
   {
      String resourceString = null;
      if (resource != null)
      {
         String resourceClass = resource.getClass().getCanonicalName();
         resourceString = resourceClass + ":" + getResourceKey(resource);
      }
      return resourceString;
   }

   /**
    * <p>
    * Returns a {@code String} representation of the specified identity. The format of this representation
    * is as follows: {@code identity_fqn:identity_name}, where {@code identity_fqn} is the fully-qualified
    * name of the identity class, and {@code identity_name} is a {@code String} representing the name of
    * the identity.
    * </p>
    * 
    * @param identity   the {@code Identity} to be converted.
    * @return   a {@code String} representation of the identity.
    */
   public static String getIdentityAsString(Identity identity)
   {
      String identityString = null;
      if (identity != null)
         identityString = identity.getClass().getCanonicalName() + ":" + identity.getName();
      return identityString;
   }

   /**
    * <p>
    * Builds and returns an identity from the specified {@code String} representation. It parses the 
    * {@code identityString} argument, and passes the parsed identity class, and identity name to
    * the {@code IdentityFactory} to retrieve an instance of {@code Identity}.
    * </p>
    * 
    * @param identityString a {@code String} representation of the identity to be created.
    * @return   the constructed {@code Identity} instance.
    */
   public static Identity getIdentityFromString(String identityString)
   {
      Identity identity = null;
      if (identityString != null)
      {
         String[] identityParts = identityString.split(":");
         if (identityParts.length != 2)
            throw PicketBoxMessages.MESSAGES.malformedIdentityString(identityString);
         try
         {
            identity = IdentityFactory.createIdentity(identityParts[0], identityParts[1]);
         }
         catch (Exception e)
         {
            throw new RuntimeException(e);
         }
      }
      return identity;
   }

   /**
    * <p>
    * Obtains an {@code Object} that can represent the specified resource uniquely. It first tries to find
    * a {@code Field} annotated with a {@code javax.persistence.Id} annotation. If such field is found, the
    * method tries to read the field's value. If no annotated field is found, this method just tries to
    * invoke a {@code getId()} method on the resource.
    * </p>
    * 
    * @param resource   the {@code Resource} whose id is to be retrieved.
    * @return   an {@code Object} representing the resource's id, or {@code null} if no id could be found
    * according to the rules used by this method for retrieving the resource's id.
    */
   private static Object getResourceKey(Resource resource)
   {
      Class<? extends Resource> resourceClass = resource.getClass();
      Object resourceKey = null;
      // first search for a field with a javax.persistence.Id annotation.
      for (Field field : resourceClass.getDeclaredFields())
      {
         if (field.getAnnotation(javax.persistence.Id.class) != null)
         {
            // found a field - try to get its value reflectively.
            try
            {
               resourceKey = field.get(resource);
            }
            catch (Exception e)
            {
               // in case of error, try executing the field getter method.
               String fieldName = field.getName();
               String methodName = "get" + fieldName.substring(0, 1).toUpperCase(Locale.ENGLISH) + fieldName.substring(1);
               resourceKey = executeNoArgMethod(resource, methodName);
            }
            break;
         }
      }

      // if the above fails, try to simply locate a getId method.
      if (resourceKey == null)
         resourceKey = executeNoArgMethod(resource, "getId");
      return resourceKey;
   }

   /**
    * <p>
    * Invokes a no-arg method reflectively in the specified target, and returns the result.
    * </p>
    * 
    * @param target the {@code Object} to be invoked. 
    * @param methodName a {@code String} representing the name of the method to be invoked.
    * @return an {@code Object} containing the result of the invocation, or {@code null} if an
    * the method is {@code void} or if an error occurs when invoking the method using reflection.
    */
   @SuppressWarnings("all")
   private static Object executeNoArgMethod(Object target, String methodName)
   {
      Class<?> targetClass = target.getClass();
      try
      {
         Method method = targetClass.getMethod(methodName, (Class<?>[])null);
         return method.invoke(target, (Object[])null);
      }
      catch (Exception e)
      {
         // ignore exception and return null.
         return null;
      }
   }
}
