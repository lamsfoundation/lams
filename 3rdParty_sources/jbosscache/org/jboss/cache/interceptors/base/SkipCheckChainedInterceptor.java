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

import org.jboss.cache.InvocationContext;
import org.jboss.cache.commands.VisitableCommand;
import org.jboss.cache.commands.read.ExistsCommand;
import org.jboss.cache.commands.read.GetChildrenNamesCommand;
import org.jboss.cache.commands.read.GetDataMapCommand;
import org.jboss.cache.commands.read.GetKeyValueCommand;
import org.jboss.cache.commands.read.GetKeysCommand;
import org.jboss.cache.commands.read.GetNodeCommand;
import org.jboss.cache.commands.read.GravitateDataCommand;
import org.jboss.cache.commands.tx.CommitCommand;
import org.jboss.cache.commands.tx.OptimisticPrepareCommand;
import org.jboss.cache.commands.tx.PrepareCommand;
import org.jboss.cache.commands.tx.RollbackCommand;
import org.jboss.cache.commands.write.ClearDataCommand;
import org.jboss.cache.commands.write.EvictCommand;
import org.jboss.cache.commands.write.InvalidateCommand;
import org.jboss.cache.commands.write.MoveCommand;
import org.jboss.cache.commands.write.PutDataMapCommand;
import org.jboss.cache.commands.write.PutForExternalReadCommand;
import org.jboss.cache.commands.write.PutKeyValueCommand;
import org.jboss.cache.commands.write.RemoveKeyCommand;
import org.jboss.cache.commands.write.RemoveNodeCommand;

/**
 * This interceptor will call {@link #skipInterception(org.jboss.cache.InvocationContext ,org.jboss.cache.commands.VisitableCommand)} before invoking each visit method
 * (and the {@link #handleDefault(org.jboss.cache.InvocationContext , org.jboss.cache.commands.VisitableCommand)} method).  If
 * {@link #skipInterception(org.jboss.cache.InvocationContext ,org.jboss.cache.commands.VisitableCommand)} returns <tt>false</tt>, the invocation will be skipped
 * and passed up the interceptor chain instead.
 * <p/>
 * Instead of overriding visitXXX() methods, implementations should override their handleXXX() counterparts defined in this class
 * instead, as well as the {@link #skipInterception(org.jboss.cache.InvocationContext ,org.jboss.cache.commands.VisitableCommand)} method.
 * Also, instead of overriding {@link #handleDefault(org.jboss.cache.InvocationContext , org.jboss.cache.commands.VisitableCommand)}, implementors
 * should override {@link #handleAll(org.jboss.cache.InvocationContext , org.jboss.cache.commands.VisitableCommand)}.
 *
 * @author Mircea.Markus@jboss.com
 * @since 2.2
 */
public abstract class SkipCheckChainedInterceptor extends CommandInterceptor
{
   @Override
   public final Object visitPutDataMapCommand(InvocationContext ctx, PutDataMapCommand command) throws Throwable
   {
      if (skipInterception(ctx, command))
      {
         return invokeNextInterceptor(ctx, command);
      }
      return handlePutDataMapCommand(ctx, command);
   }

   protected Object handlePutDataMapCommand(InvocationContext ctx, PutDataMapCommand command) throws Throwable
   {
      return handleAll(ctx, command);
   }

   @Override
   public final Object visitPutKeyValueCommand(InvocationContext ctx, PutKeyValueCommand command) throws Throwable
   {
      if (skipInterception(ctx, command))
      {
         return invokeNextInterceptor(ctx, command);
      }
      return handlePutKeyValueCommand(ctx, command);
   }

   @Override
   public final Object visitPutForExternalReadCommand(InvocationContext ctx, PutForExternalReadCommand command) throws Throwable
   {
      if (skipInterception(ctx, command))
      {
         return invokeNextInterceptor(ctx, command);
      }
      return handlePutForExternalReadCommand(ctx, command);
   }

   protected Object handlePutKeyValueCommand(InvocationContext ctx, PutKeyValueCommand command) throws Throwable
   {
      return handleAll(ctx, command);
   }

   protected Object handlePutForExternalReadCommand(InvocationContext ctx, PutForExternalReadCommand command) throws Throwable
   {
      return handleAll(ctx, command);
   }

   @Override
   public final Object visitRemoveNodeCommand(InvocationContext ctx, RemoveNodeCommand command) throws Throwable
   {
      if (skipInterception(ctx, command))
      {
         return invokeNextInterceptor(ctx, command);
      }
      return handleRemoveNodeCommand(ctx, command);
   }

   protected Object handleRemoveNodeCommand(InvocationContext ctx, RemoveNodeCommand command) throws Throwable
   {
      return handleAll(ctx, command);
   }

   @Override
   public final Object visitClearDataCommand(InvocationContext ctx, ClearDataCommand command) throws Throwable
   {
      if (skipInterception(ctx, command))
      {
         return invokeNextInterceptor(ctx, command);
      }
      return handleRemoveDataCommand(ctx, command);
   }

   protected Object handleRemoveDataCommand(InvocationContext ctx, ClearDataCommand command) throws Throwable
   {
      return handleAll(ctx, command);
   }

   @Override
   public final Object visitEvictFqnCommand(InvocationContext ctx, EvictCommand command) throws Throwable
   {
      if (skipInterception(ctx, command))
      {
         return invokeNextInterceptor(ctx, command);
      }
      return handleEvictFqnCommand(ctx, command);
   }

   protected Object handleEvictFqnCommand(InvocationContext ctx, EvictCommand command) throws Throwable
   {
      return handleAll(ctx, command);
   }

   @Override
   public final Object visitInvalidateCommand(InvocationContext ctx, InvalidateCommand command) throws Throwable
   {
      if (skipInterception(ctx, command))
      {
         return invokeNextInterceptor(ctx, command);
      }
      return handleInvalidateCommand(ctx, command);
   }

   protected Object handleInvalidateCommand(InvocationContext ctx, InvalidateCommand command) throws Throwable
   {
      return handleAll(ctx, command);
   }

   @Override
   public final Object visitRemoveKeyCommand(InvocationContext ctx, RemoveKeyCommand command) throws Throwable
   {
      if (skipInterception(ctx, command))
      {
         return invokeNextInterceptor(ctx, command);
      }
      return handleRemoveKeyCommand(ctx, command);
   }

   protected Object handleRemoveKeyCommand(InvocationContext ctx, RemoveKeyCommand command) throws Throwable
   {
      return handleAll(ctx, command);
   }

   @Override
   public final Object visitGetDataMapCommand(InvocationContext ctx, GetDataMapCommand command) throws Throwable
   {
      if (skipInterception(ctx, command))
      {
         return invokeNextInterceptor(ctx, command);
      }
      return handleGetDataMapCommand(ctx, command);
   }

   protected Object handleGetDataMapCommand(InvocationContext ctx, GetDataMapCommand command) throws Throwable
   {
      return handleAll(ctx, command);
   }

   @Override
   public final Object visitExistsNodeCommand(InvocationContext ctx, ExistsCommand command) throws Throwable
   {
      if (skipInterception(ctx, command))
      {
         return invokeNextInterceptor(ctx, command);
      }
      return handleExistsNodeCommand(ctx, command);
   }

   protected Object handleExistsNodeCommand(InvocationContext ctx, ExistsCommand command) throws Throwable
   {
      return handleAll(ctx, command);
   }

   @Override
   public final Object visitGetKeyValueCommand(InvocationContext ctx, GetKeyValueCommand command) throws Throwable
   {
      if (skipInterception(ctx, command))
      {
         return invokeNextInterceptor(ctx, command);
      }
      return handleGetKeyValueCommand(ctx, command);
   }

   protected Object handleGetKeyValueCommand(InvocationContext ctx, GetKeyValueCommand command) throws Throwable
   {
      return handleAll(ctx, command);
   }

   @Override
   public final Object visitGetNodeCommand(InvocationContext ctx, GetNodeCommand command) throws Throwable
   {
      if (skipInterception(ctx, command))
      {
         return invokeNextInterceptor(ctx, command);
      }
      return handleGetNodeCommand(ctx, command);
   }

   protected Object handleGetNodeCommand(InvocationContext ctx, GetNodeCommand command) throws Throwable
   {
      return handleAll(ctx, command);
   }

   @Override
   public final Object visitGetKeysCommand(InvocationContext ctx, GetKeysCommand command) throws Throwable
   {
      if (skipInterception(ctx, command))
      {
         return invokeNextInterceptor(ctx, command);
      }
      return handleGetKeysCommand(ctx, command);
   }

   protected Object handleGetKeysCommand(InvocationContext ctx, GetKeysCommand command) throws Throwable
   {
      return handleAll(ctx, command);
   }

   @Override
   public final Object visitGetChildrenNamesCommand(InvocationContext ctx, GetChildrenNamesCommand command) throws Throwable
   {
      if (skipInterception(ctx, command))
      {
         return invokeNextInterceptor(ctx, command);
      }
      return handleGetChildrenNamesCommand(ctx, command);
   }

   protected Object handleGetChildrenNamesCommand(InvocationContext ctx, GetChildrenNamesCommand command) throws Throwable
   {
      return handleAll(ctx, command);
   }

   @Override
   public final Object visitMoveCommand(InvocationContext ctx, MoveCommand command) throws Throwable
   {
      if (skipInterception(ctx, command))
      {
         return invokeNextInterceptor(ctx, command);
      }
      return handleMoveCommand(ctx, command);
   }

   protected Object handleMoveCommand(InvocationContext ctx, MoveCommand command) throws Throwable
   {
      return handleAll(ctx, command);
   }

   @Override
   public final Object visitGravitateDataCommand(InvocationContext ctx, GravitateDataCommand command) throws Throwable
   {
      if (skipInterception(ctx, command))
      {
         return invokeNextInterceptor(ctx, command);
      }
      return handleGravitateDataCommand(ctx, command);
   }

   protected Object handleGravitateDataCommand(InvocationContext ctx, GravitateDataCommand command) throws Throwable
   {
      return handleAll(ctx, command);
   }

   @Override
   public final Object visitPrepareCommand(InvocationContext ctx, PrepareCommand command) throws Throwable
   {
      if (skipInterception(ctx, command))
      {
         return invokeNextInterceptor(ctx, command);
      }
      return handlePrepareCommand(ctx, command);
   }

   protected Object handlePrepareCommand(InvocationContext ctx, PrepareCommand command) throws Throwable
   {
      return handleAll(ctx, command);
   }

   @Override
   public final Object visitRollbackCommand(InvocationContext ctx, RollbackCommand command) throws Throwable
   {
      if (skipInterception(ctx, command))
      {
         return invokeNextInterceptor(ctx, command);
      }
      return handleRollbackCommand(ctx, command);
   }

   protected Object handleRollbackCommand(InvocationContext ctx, RollbackCommand command) throws Throwable
   {
      return handleAll(ctx, command);
   }

   @Override
   public final Object visitCommitCommand(InvocationContext ctx, CommitCommand command) throws Throwable
   {
      if (skipInterception(ctx, command))
      {
         return invokeNextInterceptor(ctx, command);
      }
      return handleCommitCommand(ctx, command);
   }

   protected Object handleCommitCommand(InvocationContext ctx, CommitCommand command) throws Throwable
   {
      return handleAll(ctx, command);
   }

   @Override
   public final Object visitOptimisticPrepareCommand(InvocationContext ctx, OptimisticPrepareCommand command) throws Throwable
   {
      if (skipInterception(ctx, command))
      {
         return invokeNextInterceptor(ctx, command);
      }
      return handleOptimisticPrepareCommand(ctx, command);
   }

   protected Object handleOptimisticPrepareCommand(InvocationContext ctx, OptimisticPrepareCommand command) throws Throwable
   {
      return handleAll(ctx, command);
   }

   @Override
   public final Object handleDefault(InvocationContext ctx, VisitableCommand command) throws Throwable
   {
      if (skipInterception(ctx, command))
      {
         return invokeNextInterceptor(ctx, command);
      }
      return handleAll(ctx, command);
   }

   /**
    * Default implementation, which just passes the call up the interceptor chain
    *
    * @param ctx     invocation context
    * @param command command
    * @return return value
    * @throws Throwable in the event of problems
    */
   protected Object handleAll(InvocationContext ctx, VisitableCommand command) throws Throwable
   {
      return invokeNextInterceptor(ctx, command);
   }

   /**
    * Tests whether the command should be intercepted or not.  This is invoked before any of the handleXXX() methods.
    *
    * @param ctx     invocation context
    * @param command command
    * @return true if the invocation should skip the current interceptor and move on to the next in the chain, false otherwise.
    */
   protected abstract boolean skipInterception(InvocationContext ctx, VisitableCommand command);
}
