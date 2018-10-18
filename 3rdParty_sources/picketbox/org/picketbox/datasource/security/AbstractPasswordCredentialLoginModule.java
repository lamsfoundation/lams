/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
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
package org.picketbox.datasource.security;

import javax.security.auth.login.LoginException;

import org.jboss.security.auth.spi.AbstractServerLoginModule;


/** 
 * A base login module that handles {@code PasswordCredential}s
 * 
 * @see javax.resource.spi.security.PasswordCredential
 *
 * @author <a href="mailto:d_jencks@users.sourceforge.net">David Jencks</a>
 * @author Scott.Stark@jboss.org
 * @version $Revision: 71545 $
 */

public abstract class AbstractPasswordCredentialLoginModule extends AbstractServerLoginModule
{

   @Override
   public boolean logout() throws LoginException
   {
      removeCredentials();
      return super.logout();
   }

   /** This removes the javax.security.auth.login.name and
    * javax.security.auth.login.password settings from the sharteState map
    * along with any PasswordCredential found in the PrivateCredentials set
    */
   protected void removeCredentials()
   {
      sharedState.remove("javax.security.auth.login.name");
      sharedState.remove("javax.security.auth.login.password");
      SubjectActions.removeCredentials(subject);
   }

}

