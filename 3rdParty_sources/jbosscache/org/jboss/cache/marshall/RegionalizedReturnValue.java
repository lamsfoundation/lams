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

/**
 * A return value that holds region information, so that the marshaller knows which region to use (and hence which
 * class loader to use) when marshalling this return value.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 2.1.1
 */
class RegionalizedReturnValue
{
   final Object returnValue;
   final Fqn region;

   /**
    * Creates this value object.
    *
    * @param returnValue            return value to marshall
    * @param regionalizedMethodCall method call that requested this return value.
    */
   RegionalizedReturnValue(Object returnValue, RegionalizedMethodCall regionalizedMethodCall)
   {
      this.returnValue = returnValue;
      this.region = regionalizedMethodCall.region;
   }
}
