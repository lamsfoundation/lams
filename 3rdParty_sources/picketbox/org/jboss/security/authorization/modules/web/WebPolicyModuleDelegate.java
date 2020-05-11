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
package org.jboss.security.authorization.modules.web;

import javax.security.auth.Subject;

import org.jboss.security.authorization.AuthorizationContext;
import org.jboss.security.authorization.Resource;
import org.jboss.security.authorization.modules.AuthorizationModuleDelegate;
import org.jboss.security.identity.RoleGroup;

//$Id: WebPolicyModuleDelegate.java 62923 2007-05-09 03:08:14Z anil.saldhana@jboss.com $

/**
 *  Authorization Module Delegate that deals with the default authorization
 *  behavior (Simply returns PERMIT, because the final decision will be
 *  made by the base class of Realm (RealmBase))
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @since  Jul 21, 2006 
 *  @version $Revision: 62923 $
 */
public class WebPolicyModuleDelegate extends AuthorizationModuleDelegate
{ 

   public int authorize(Resource resource, Subject subject, RoleGroup role)
   {
      return AuthorizationContext.PERMIT;
   } 
}