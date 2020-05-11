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
package org.jboss.security.identitytrust;

import java.security.GeneralSecurityException;

//$Id$

/**
 *  Identity Trust Exception
 *  @author Anil.Saldhana@redhat.com
 *  @since  Aug 2, 2007 
 *  @version $Revision$
 */
public class IdentityTrustException extends GeneralSecurityException
{ 
   private static final long serialVersionUID = 1L;

   public IdentityTrustException()
   {
      super(); 
   }

   public IdentityTrustException(String msg, Throwable t)
   {
      super(msg, t); 
   }

   public IdentityTrustException(String msg)
   {
      super(msg); 
   }

   public IdentityTrustException(Throwable t)
   {
      super(t); 
   } 
}
