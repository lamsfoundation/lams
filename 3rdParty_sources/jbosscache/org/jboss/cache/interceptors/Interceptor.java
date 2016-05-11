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
package org.jboss.cache.interceptors;

import org.jboss.cache.CacheSPI;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.commands.VisitableCommand;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.factories.annotations.Start;
import org.jboss.cache.interceptors.base.CommandInterceptor;

/**
 * Class representing an interceptor.
 *
 * @author Bela Ban
 *
 * @deprecated this will be removed in a 3.x release.  Please use {@link org.jboss.cache.interceptors.base.CommandInterceptor} instead, since it provides strongly typed callbacks which are more efficient.
 */
@Deprecated
public abstract class Interceptor extends CommandInterceptor
{
   protected CacheSPI<?, ?> cache;
   protected boolean trace;

   public void setCache(CacheSPI cache)
   {
      this.cache = cache;
   }

   @Start
   void start()
   {
      // for backward compatibility, this must only be done when the cache starts.
      setCache(cache);
   }

   @Inject
   void injectDependencies(CacheSPI cache)
   {
      this.cache = cache;
   }

   /**
    * Using this method call for forwarding a call in the chain is not redable and error prone in the case of interceptors
    * extending other interceptors. This metod rather refers to interceptor doing its business operations rather than
    * delegating to the nextInterceptor interceptor in chain. For delegation please use {@link #nextInterceptor(org.jboss.cache.InvocationContext)}
    */
   public Object invoke(InvocationContext ctx) throws Throwable
   {
      return handleDefault(ctx, null);
   }

   /**
    * Forwards the call to the nextInterceptor interceptor in the chain.
    * This is here for backward compatibility.
    */
   public Object nextInterceptor(InvocationContext ctx) throws Throwable
   {
      return invokeNextInterceptor(ctx, null);
   }

   @Override
   public String toString()
   {
      return getClass().getName()
            + "{next: "
            + (getNext() == null ? null : getNext().getClass())
            + "}";
   }

   @Override
   @SuppressWarnings("deprecation")
   public Object handleDefault(InvocationContext ctx, VisitableCommand command) throws Throwable
   {
      if (command != null) //call originated from a command's accept() method.
      {
         return invoke(ctx);
      }

      //this means that another Interceptor called this method, we have to dispatch the call to the appropriate method.  Probably called directly using super.invoke().
      command = ctx.getCommand();
      return command.acceptVisitor(ctx, getNext());
   }
}
