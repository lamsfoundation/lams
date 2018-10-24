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
package org.jboss.security.plugins;

import java.util.List;

/**
 * An interface to locate a {@code ClassLoader}
 * The primary use of this interface is in the JBoss Application Server,
 * which needs to inject a module class loader for custom login modules etc
 * @author Anil Saldhana
 * @since Nov 14, 2011
 */
public interface ClassLoaderLocator 
{
	/**
	 * Given a module name, return a {@link ClassLoader}
	 * @param module the name of the module for which we want a {@link ClassLoader}.
	 * @return the module {@link java.lang.ClassLoader}.
	 */
	ClassLoader get(String module);

	/**
	 * Given a list of module names, return a {@link ClassLoader} that combines all module loaders. This method was made
	 * default to ensure API compatibility in WildFly. Implementations that are meant to provide support for multiple modules
	 * must override this default method implementation, which uses only the first module found in the list.
	 *
	 * @param modules the set of modules for which we want a {@link ClassLoader}.
	 * @return the combined {@link ClassLoader}
     */
    default ClassLoader get(List<String> modules) {
		if (modules != null && modules.size() > 0) {
			return this.get(modules.get(0));
		}
        return null;
    }
}