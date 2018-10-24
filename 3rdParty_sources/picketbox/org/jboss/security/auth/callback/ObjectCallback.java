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
package org.jboss.security.auth.callback;

import javax.security.auth.callback.Callback;

/** An implementation of Callback that simply obtains an Object to be used
as the authentication credential. Interpretation of the Object is up to
the LoginModules that validate the credential.

@author  Scott.Stark@jboss.org
@version $Revision$
*/
public class ObjectCallback implements Callback
{
    private transient String prompt;
    private transient Object credential;

    /** Initialize the SecurityAssociationCallback
    */
    public ObjectCallback(String prompt)
    {
        this.prompt = prompt;
    }

    public String getPrompt()
    {
        return prompt;
    }
    public Object getCredential()
    {
        return credential;
    }
    public void setCredential(Object credential)
    {
        this.credential = credential;
    }
    public void clearCredential()
    {
        this.credential = null;
    }
}

