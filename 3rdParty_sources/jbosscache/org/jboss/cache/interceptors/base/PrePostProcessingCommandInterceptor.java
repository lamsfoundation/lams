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
import org.jboss.cache.commands.legacy.write.CreateNodeCommand;
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
 * This interceptor adds pre and post processing to each <tt>visitXXX()</tt> method.
 * <p/>
 * For each <tt>visitXXX()</tt> method invoked, it will first call {@link #doBeforeCall(org.jboss.cache.InvocationContext, org.jboss.cache.commands.VisitableCommand)}
 * and if this method returns true, it will proceed to invoking a <tt>handleXXX()</tt> method and lastly, {@link #doAfterCall(org.jboss.cache.InvocationContext, org.jboss.cache.commands.VisitableCommand)}
 * in a <tt>finally</tt> block.  Note that the <tt>doAfterCall()</tt> method is still invoked even if <tt>doBeforeCall()</tt> returns <tt>false</tt>.
 * <p/>
 * Instead of overriding <tt>visitXXX()</tt> methods, implementations should override their <tt>handleXXX()</tt> counterparts defined in this class
 * instead, as well as the {@link #doAfterCall(org.jboss.cache.InvocationContext ,org.jboss.cache.commands.VisitableCommand)} method and
 * optionally {@link #doBeforeCall(org.jboss.cache.InvocationContext, org.jboss.cache.commands.VisitableCommand)}.
 * <p/>
 *
 * @author Mircea.Markus@jboss.com
 * @since 2.2
 */
public abstract class PrePostProcessingCommandInterceptor extends CommandInterceptor
{
   @Override
   public final Object visitPutDataMapCommand(InvocationContext ctx, PutDataMapCommand command) throws Throwable
   {
      try
      {
         return doBeforeCall(ctx, command) ? handlePutDataMapCommand(ctx, command) : null;
      }
      finally
      {
         doAfterCall(ctx, command);
      }
   }

   protected Object handlePutDataMapCommand(InvocationContext ctx, PutDataMapCommand command) throws Throwable
   {
      return handleDefault(ctx, command);
   }

   @Override
   public final Object visitPutKeyValueCommand(InvocationContext ctx, PutKeyValueCommand command) throws Throwable
   {
      try
      {
         return doBeforeCall(ctx, command) ? handlePutKeyValueCommand(ctx, command) : null;
      }
      finally
      {
         doAfterCall(ctx, command);
      }
   }

   @Override
   public final Object visitPutForExternalReadCommand(InvocationContext ctx, PutForExternalReadCommand command) throws Throwable
   {
      try
      {
         return doBeforeCall(ctx, command) ? handlePutForExternalReadCommand(ctx, command) : null;
      }
      finally
      {
         doAfterCall(ctx, command);
      }
   }


   protected Object handlePutKeyValueCommand(InvocationContext ctx, PutKeyValueCommand command) throws Throwable
   {
      return handleDefault(ctx, command);
   }

   protected Object handlePutForExternalReadCommand(InvocationContext ctx, PutForExternalReadCommand command) throws Throwable
   {
      return handleDefault(ctx, command);
   }


   @Override
   public final Object visitRemoveNodeCommand(InvocationContext ctx, RemoveNodeCommand command) throws Throwable
   {
      try
      {
         return doBeforeCall(ctx, command) ? handleRemoveNodeCommand(ctx, command) : null;
      }
      finally
      {
         doAfterCall(ctx, command);
      }
   }

   protected Object handleRemoveNodeCommand(InvocationContext ctx, RemoveNodeCommand command) throws Throwable
   {
      return handleDefault(ctx, command);
   }

   @Override
   public final Object visitCreateNodeCommand(InvocationContext ctx, CreateNodeCommand command) throws Throwable
   {
      try
      {
         return doBeforeCall(ctx, command) ? handleCreateNodeCommand(ctx, command) : null;
      }
      finally
      {
         doAfterCall(ctx, command);
      }
   }

   /**
    * @deprecated in 3.0.  Will be removed when Optimistic and Pessimistic locking is removed.
    */
   @Deprecated
   protected Object handleCreateNodeCommand(InvocationContext ctx, CreateNodeCommand command) throws Throwable
   {
      return handleDefault(ctx, command);
   }


   @Override
   public final Object visitClearDataCommand(InvocationContext ctx, ClearDataCommand command) throws Throwable
   {
      try
      {
         return doBeforeCall(ctx, command) ? handleClearDataCommand(ctx, command) : null;
      }
      finally
      {
         doAfterCall(ctx, command);
      }
   }

   protected Object handleClearDataCommand(InvocationContext ctx, ClearDataCommand command) throws Throwable
   {
      return handleDefault(ctx, command);
   }

   @Override
   public final Object visitEvictFqnCommand(InvocationContext ctx, EvictCommand command) throws Throwable
   {
      try
      {
         return doBeforeCall(ctx, command) ? handleEvictFqnCommand(ctx, command) : null;
      }
      finally
      {
         doAfterCall(ctx, command);
      }
   }

   protected Object handleEvictFqnCommand(InvocationContext ctx, EvictCommand command) throws Throwable
   {
      return handleDefault(ctx, command);
   }

   @Override
   public final Object visitInvalidateCommand(InvocationContext ctx, InvalidateCommand command) throws Throwable
   {
      try
      {
         return doBeforeCall(ctx, command) ? handleInvalidateCommand(ctx, command) : null;
      }
      finally
      {
         doAfterCall(ctx, command);
      }
   }

   protected Object handleInvalidateCommand(InvocationContext ctx, InvalidateCommand command) throws Throwable
   {
      return handleDefault(ctx, command);
   }

   @Override
   public final Object visitRemoveKeyCommand(InvocationContext ctx, RemoveKeyCommand command) throws Throwable
   {
      try
      {
         return doBeforeCall(ctx, command) ? handleRemoveKeyCommand(ctx, command) : null;
      }
      finally
      {
         doAfterCall(ctx, command);
      }
   }

   protected Object handleRemoveKeyCommand(InvocationContext ctx, RemoveKeyCommand command) throws Throwable
   {
      return handleDefault(ctx, command);
   }

   @Override
   public final Object visitGetDataMapCommand(InvocationContext ctx, GetDataMapCommand command) throws Throwable
   {
      try
      {
         return doBeforeCall(ctx, command) ? handleGetDataMapCommand(ctx, command) : null;
      }
      finally
      {
         doAfterCall(ctx, command);
      }
   }

   protected Object handleGetDataMapCommand(InvocationContext ctx, GetDataMapCommand command) throws Throwable
   {
      return handleDefault(ctx, command);
   }

   @Override
   public final Object visitExistsNodeCommand(InvocationContext ctx, ExistsCommand command) throws Throwable
   {
      try
      {
         return doBeforeCall(ctx, command) ? handleExistsNodeCommand(ctx, command) : null;
      }
      finally
      {
         doAfterCall(ctx, command);
      }
   }

   protected Object handleExistsNodeCommand(InvocationContext ctx, ExistsCommand command) throws Throwable
   {
      return handleDefault(ctx, command);
   }

   @Override
   public final Object visitGetKeyValueCommand(InvocationContext ctx, GetKeyValueCommand command) throws Throwable
   {
      try
      {
         return doBeforeCall(ctx, command) ? handleGetKeyValueCommand(ctx, command) : null;
      }
      finally
      {
         doAfterCall(ctx, command);
      }
   }

   protected Object handleGetKeyValueCommand(InvocationContext ctx, GetKeyValueCommand command) throws Throwable
   {
      return handleDefault(ctx, command);
   }

   @Override
   public final Object visitGetNodeCommand(InvocationContext ctx, GetNodeCommand command) throws Throwable
   {
      try
      {
         return doBeforeCall(ctx, command) ? handleGetNodeCommand(ctx, command) : null;
      }
      finally
      {
         doAfterCall(ctx, command);
      }
   }

   protected Object handleGetNodeCommand(InvocationContext ctx, GetNodeCommand command) throws Throwable
   {
      return handleDefault(ctx, command);
   }

   @Override
   public final Object visitGetKeysCommand(InvocationContext ctx, GetKeysCommand command) throws Throwable
   {
      try
      {
         return doBeforeCall(ctx, command) ? handleGetKeysCommand(ctx, command) : null;
      }
      finally
      {
         doAfterCall(ctx, command);
      }
   }

   protected Object handleGetKeysCommand(InvocationContext ctx, GetKeysCommand command) throws Throwable
   {
      return handleDefault(ctx, command);
   }

   @Override
   public final Object visitGetChildrenNamesCommand(InvocationContext ctx, GetChildrenNamesCommand command) throws Throwable
   {
      try
      {
         return doBeforeCall(ctx, command) ? handleGetChildrenNamesCommand(ctx, command) : null;
      }
      finally
      {
         doAfterCall(ctx, command);
      }
   }

   protected Object handleGetChildrenNamesCommand(InvocationContext ctx, GetChildrenNamesCommand command) throws Throwable
   {
      return handleDefault(ctx, command);
   }

   @Override
   public final Object visitMoveCommand(InvocationContext ctx, MoveCommand command) throws Throwable
   {
      try
      {
         return doBeforeCall(ctx, command) ? handleMoveCommand(ctx, command) : null;
      }
      finally
      {
         doAfterCall(ctx, command);
      }
   }

   protected Object handleMoveCommand(InvocationContext ctx, MoveCommand command) throws Throwable
   {
      return handleDefault(ctx, command);
   }

   @Override
   public final Object visitGravitateDataCommand(InvocationContext ctx, GravitateDataCommand command) throws Throwable
   {
      try
      {
         return doBeforeCall(ctx, command) ? handleGravitateDataCommand(ctx, command) : null;
      }
      finally
      {
         doAfterCall(ctx, command);
      }
   }

   protected Object handleGravitateDataCommand(InvocationContext ctx, GravitateDataCommand command) throws Throwable
   {
      return handleDefault(ctx, command);
   }

   @Override
   public final Object visitPrepareCommand(InvocationContext ctx, PrepareCommand command) throws Throwable
   {
      try
      {
         return doBeforeCall(ctx, command) ? handlePrepareCommand(ctx, command) : null;
      }
      finally
      {
         doAfterCall(ctx, command);
      }
   }

   protected Object handlePrepareCommand(InvocationContext ctx, PrepareCommand command) throws Throwable
   {
      return handleDefault(ctx, command);
   }

   @Override
   public final Object visitRollbackCommand(InvocationContext ctx, RollbackCommand command) throws Throwable
   {
      try
      {
         return doBeforeCall(ctx, command) ? handleRollbackCommand(ctx, command) : null;
      }
      finally
      {
         doAfterCall(ctx, command);
      }
   }

   protected Object handleRollbackCommand(InvocationContext ctx, RollbackCommand command) throws Throwable
   {
      return handleDefault(ctx, command);
   }

   @Override
   public final Object visitCommitCommand(InvocationContext ctx, CommitCommand command) throws Throwable
   {
      try
      {
         return doBeforeCall(ctx, command) ? handleCommitCommand(ctx, command) : null;
      }
      finally
      {
         doAfterCall(ctx, command);
      }
   }

   protected Object handleCommitCommand(InvocationContext ctx, CommitCommand command) throws Throwable
   {
      return handleDefault(ctx, command);
   }

   @Override
   public final Object visitOptimisticPrepareCommand(InvocationContext ctx, OptimisticPrepareCommand command) throws Throwable
   {
      try
      {
         return doBeforeCall(ctx, command) ? handleOptimisticPrepareCommand(ctx, command) : null;
      }
      finally
      {
         doAfterCall(ctx, command);
      }
   }

   protected Object handleOptimisticPrepareCommand(InvocationContext ctx, OptimisticPrepareCommand command) throws Throwable
   {
      return handleDefault(ctx, command);
   }

   /**
    * Callback that is invoked after every handleXXX() method defined above.
    *
    * @param ctx     invocation context
    * @param command command which was invoked
    */
   protected abstract void doAfterCall(InvocationContext ctx, VisitableCommand command);

   protected boolean doBeforeCall(InvocationContext ctx, VisitableCommand command)
   {
      return true;
   }
}
