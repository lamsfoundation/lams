/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2000 - 2008, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.jboss.cache.marshall;

import org.jboss.cache.Fqn;
import org.jboss.cache.commands.ReplicableCommand;
import org.jboss.cache.transaction.GlobalTransaction;
import org.jgroups.Address;

/**
 * Common functionality used by the {@link org.jboss.cache.interceptors.MarshalledValueInterceptor} and the {@link MarshalledValueMap}.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @see MarshalledValue
 * @see org.jboss.cache.interceptors.MarshalledValueInterceptor
 * @see org.jboss.cache.marshall.MarshalledValueMap
 * @since 2.1.0
 */
public class MarshalledValueHelper
{
   /**
    * Tests whether the type should be excluded from MarshalledValue wrapping.
    *
    * @param type type to test.  Should not be null.
    * @return true if it should be excluded from MarshalledValue wrapping.
    */
   public static boolean isTypeExcluded(Class type)
   {
      return type.equals(String.class) || type.isPrimitive() ||
            type.equals(Void.class) || type.equals(Boolean.class) || type.equals(Character.class) ||
            type.equals(Byte.class) || type.equals(Short.class) || type.equals(Integer.class) ||
            type.equals(Long.class) || type.equals(Float.class) || type.equals(Double.class) ||
            (type.isArray() && isTypeExcluded(type.getComponentType())) || type.equals(Fqn.class) || type.equals(GlobalTransaction.class) || type.equals(Address.class) ||
            ReplicableCommand.class.isAssignableFrom(type) || type.equals(MarshalledValue.class);
   }
}
