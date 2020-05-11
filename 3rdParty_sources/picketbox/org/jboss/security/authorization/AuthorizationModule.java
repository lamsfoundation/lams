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
package org.jboss.security.authorization;

import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;

import org.jboss.security.identity.RoleGroup;

//$Id: AuthorizationModule.java 45685 2006-06-20 04:46:23Z asaldhana $

/**
 *  Represents a Policy Decision Module that is used by the
 *  Authorization Context
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @since  Jun 11, 2006 
 *  @version $Revision: 45685 $
 */
public interface AuthorizationModule
{
   /**
    * Abort the Authorization Process
    * @return true - abort passed, false-otherwise
    */
   boolean abort() throws AuthorizationException;
   
   /**
    * Overall authorization process has succeeded.
    * The module can commit any decisions it has made, with
    * third party systems like a database.
    * @return 
    */
   boolean commit() throws AuthorizationException;
   
   /**
    * Initialize the module
    * 
    * @param subject the authenticated subject
    * @param handler CallbackHandler
    * @param sharedState state shared with other configured modules 
    * @param options options specified in the Configuration 
    *                for this particular module
    * @param roles Roles of the subject               
    */
   void initialize(Subject subject, CallbackHandler handler,
         Map<String,Object> sharedState, Map<String,Object> options, RoleGroup roles);
   
   /**
    * Authorize the resource
    * @param resource
    * @return AuthorizationContext.PERMIT or AuthorizationContext.DENY
    */
   int authorize(Resource resource);
   
   /**
    * A final cleanup opportunity offered
    * @return cleanup by the module passed or not
    */
   boolean destroy();
}
