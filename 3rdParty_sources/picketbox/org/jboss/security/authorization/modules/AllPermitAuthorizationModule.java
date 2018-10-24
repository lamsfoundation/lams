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

import org.jboss.security.authorization.AuthorizationContext;
import org.jboss.security.authorization.AuthorizationException;
import org.jboss.security.authorization.AuthorizationModule;
import org.jboss.security.authorization.Resource;

//$Id$

/**
 *  Noop Authorization Module that lets all authenticated users
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @since  Jun 23, 2006 
 *  @version $Revision$
 */
public class AllPermitAuthorizationModule extends AbstractAuthorizationModule
{ 
   /**
    * @see AuthorizationModule#authorize(Resource)
    * @return AuthorizationContext.PERMIT
    */
   public int authorize(Resource resource)
   {
      return AuthorizationContext.PERMIT;
   }

   /**
    * @see AuthorizationModule#abort()
    * @return true
    */
   public boolean abort() throws AuthorizationException
   {
      return true;
   }

   /**
    * @see AuthorizationModule#commit()
    * @return true
    */
   public boolean commit() throws AuthorizationException
   {
      return true;
   } 
}
