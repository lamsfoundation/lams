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
package org.jboss.cache.interceptors.base;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.commands.AbstractVisitor;
import org.jboss.cache.commands.VisitableCommand;
import org.jboss.cache.config.Configuration;
import org.jboss.cache.factories.annotations.Inject;

/**
 * This is the base class for all interceptors to extend, and implements the {@link org.jboss.cache.commands.Visitor} interface
 * allowing it to intercept invocations on {@link org.jboss.cache.commands.VisitableCommand}s.
 * <p/>
 * Commands are created either by the {@link org.jboss.cache.invocation.CacheInvocationDelegate} (for invocations on the {@link org.jboss.cache.Cache}
 * public interface), the {@link org.jboss.cache.invocation.NodeInvocationDelegate} for invocations on the {@link org.jboss.cache.Node}
 * public interface, or by the {@link org.jboss.cache.marshall.CommandAwareRpcDispatcher} for remotely originating invocations, and
 * are passed up the interceptor chain by using the {@link org.jboss.cache.interceptors.InterceptorChain} helper class.
 * <p/>
 * When writing interceptors, authors can either override a specific visitXXX() method (such as {@link #visitGetKeyValueCommand(org.jboss.cache.InvocationContext , org.jboss.cache.commands.read.GetKeyValueCommand)})
 * or the more generic {@link #handleDefault(org.jboss.cache.InvocationContext , org.jboss.cache.commands.VisitableCommand)} which is the default behaviour of
 * any visit method, as defined in {@link org.jboss.cache.commands.AbstractVisitor#handleDefault(org.jboss.cache.InvocationContext , org.jboss.cache.commands.VisitableCommand)}.
 * <p/>
 * The preferred approach is to override the specific visitXXX() methods that are of interest rather than to override {@link #handleDefault(org.jboss.cache.InvocationContext , org.jboss.cache.commands.VisitableCommand)}
 * and then write a series of if statements or a switch block, if command-specific behaviour is needed.
 * <p/>
 *
 * @author Mircea.Markus@jboss.com
 * @see VisitableCommand
 * @see org.jboss.cache.commands.Visitor
 * @see org.jboss.cache.interceptors.InterceptorChain
 * @since 2.2
 */
public class CommandInterceptor extends AbstractVisitor
{
   private CommandInterceptor next;

   protected Log log;
   protected boolean trace;

   protected Configuration configuration;

   public CommandInterceptor()
   {
      log = LogFactory.getLog(getClass());
      trace = log.isTraceEnabled();
   }

   @Inject
   void injectConfiguration(Configuration configuration)
   {
      this.configuration = configuration;
   }

   /**
    * Retrieves the next interceptor in the chain.
    *
    * @return the next interceptor in the chain.
    */
   public CommandInterceptor getNext()
   {
      return next;
   }

   /**
    * @return true if there is another interceptor in the chain after this; false otherwise.
    */
   public boolean hasNext()
   {
      return getNext() != null;
   }

   /**
    * Sets the next interceptor in the chain to the interceptor passed in.
    *
    * @param next next interceptor in the chain.
    */
   public void setNext(CommandInterceptor next)
   {
      this.next = next;
   }

   /**
    * Invokes the next interceptor in the chain.  This is how interceptor implementations should pass a call up the chain
    * to the next interceptor.  In previous (pre-2.2.0) implementations of JBoss Cache, this was done by calling
    * <pre>super.invoke()</pre>.
    *
    * @param ctx     invocation context
    * @param command command to pass up the chain.
    * @return return value of the invocation
    * @throws Throwable in the event of problems
    */
   public Object invokeNextInterceptor(InvocationContext ctx, VisitableCommand command) throws Throwable
   {
      return command.acceptVisitor(ctx, next);
   }

   /**
    * The default behaviour of the visitXXX methods, which is to ignore the call and pass the call up to the next
    * interceptor in the chain.
    *
    * @param ctx     invocation context
    * @param command command to invoke
    * @return return value
    * @throws Throwable in the event of problems
    */
   @Override
   protected Object handleDefault(InvocationContext ctx, VisitableCommand command) throws Throwable
   {
      return invokeNextInterceptor(ctx, command);
   }
}
