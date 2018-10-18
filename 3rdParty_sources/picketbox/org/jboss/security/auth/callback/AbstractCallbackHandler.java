/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.security.auth.callback;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;

/**
 * Base class for data store driven {@code CallbackHandler}
 * @author YOUR_NAME
 * @since Nov 1, 2011
 */
public class AbstractCallbackHandler 
{
	/**
	 * User Name that we are interested in getting the password for
	 */
	protected String userName;
	
	
	/**
	 * Given the callbacks, look for {@code NameCallback}
	 * @param callbacks
	 * @return
	 */
	protected String getUserName(Callback[] callbacks)
	{
		if(userName == null)
		{ 
			for (int i = 0; i < callbacks.length; i++)
			{
				Callback callback = callbacks[i];
				if(callback instanceof NameCallback)
				{
					NameCallback nc = (NameCallback) callback;
					userName = nc.getName();
					break;
				}  
			}
		}
		return userName;
	}

}