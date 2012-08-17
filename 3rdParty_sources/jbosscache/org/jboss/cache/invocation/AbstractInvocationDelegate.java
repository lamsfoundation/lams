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
package org.jboss.cache.invocation;

import org.jboss.cache.InvocationContext;
import org.jboss.cache.config.Configuration;
import org.jboss.cache.factories.ComponentRegistry;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.interceptors.InterceptorChain;

/**
 * The JBoss Cache hand-wired interceptor stack.  A "minimal" AOP framework which uses delegation through an
 * interceptor chain rather than any bytecode manipulation.
 * <p/>
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @see org.jboss.cache.interceptors.base.CommandInterceptor
 * @see InvocationContext
 * @since 2.1.0
 */
public abstract class AbstractInvocationDelegate
{
   protected Configuration configuration;
   protected InvocationContextContainer invocationContextContainer;
   protected ComponentRegistry componentRegistry;
   protected InterceptorChain invoker;

   protected boolean originLocal = true;

   /**
    * Used by the interceptor chain factory to inject dependencies.
    */
   @Inject
   public void initialize(Configuration configuration, InvocationContextContainer invocationContextContainer,
                          ComponentRegistry componentRegistry, InterceptorChain interceptorChain)
   {
      this.configuration = configuration;
      this.invocationContextContainer = invocationContextContainer;
      this.invoker = interceptorChain;
      this.componentRegistry = componentRegistry;
   }


   protected void assertIsConstructed()
   {
      if (invocationContextContainer == null) throw new IllegalStateException("The cache has been destroyed!");
   }

}
