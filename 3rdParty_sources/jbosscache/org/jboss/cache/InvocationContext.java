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
package org.jboss.cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.annotations.Compat;
import org.jboss.cache.commands.VisitableCommand;
import org.jboss.cache.config.Option;
import org.jboss.cache.marshall.MethodCall;
import org.jboss.cache.transaction.GlobalTransaction;
import org.jboss.cache.transaction.TransactionContext;
import org.jboss.cache.transaction.TransactionTable;
import org.jboss.cache.util.Immutables;

import javax.transaction.Transaction;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A context that holds information regarding the scope of a single invocation.  May delegate some calls to a {@link org.jboss.cache.transaction.TransactionContext}
 * if one is in scope.
 * <p/>
 * Note that prior to 3.0.0, InvocationContext was a concrete class and not an abstract one.
 * <p/>
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @see org.jboss.cache.transaction.TransactionContext
 */
@SuppressWarnings("deprecation")
@Compat(notes = "This really ought to be an interface, just like TransactionContext, but since this is public API making this an interface will break binary compat with 2.x.")
public abstract class InvocationContext
{
   private static final Log log = LogFactory.getLog(InvocationContext.class);
   private static final boolean trace = log.isTraceEnabled();

   private Transaction transaction;
   private GlobalTransaction globalTransaction;
   protected TransactionContext transactionContext;
   private Option optionOverrides;
   // defaults to true.
   private boolean originLocal = true;
   private boolean localRollbackOnly;
   private boolean bypassUnmarshalling = false;
   @Deprecated
   private MethodCall methodCall;
   @Deprecated
   private VisitableCommand command;

   /**
    * Set of Fqns loaded by the cache loader interceptor.  Only recorded if needed, such as by the ActivationInterceptor
    */
   private Set<Fqn> fqnsLoaded;

   /**
    * LinkedHashSet of locks acquired by the invocation. We use a LinkedHashSet because we need efficient Set semantics
    * but also need guaranteed ordering for use by lock release code (see JBCCACHE-874).
    * <p/>
    * This needs to be unchecked since we support both MVCC (Fqns held here) or legacy Opt/Pess locking (NodeLocks held here).
    * once we drop support for opt/pess locks we can genericise this to contain Fqns. - Manik Surtani, June 2008
    */
   protected LinkedHashSet invocationLocks;

   /**
    * Retrieves a node from the registry of looked up nodes in the current scope.
    *
    * @param fqn fqn to look up
    * @return a node, or null if it cannot be found.
    * @since 3.0.
    */
   public abstract NodeSPI lookUpNode(Fqn fqn);

   /**
    * Puts an entry in the registry of looked up nodes in the current scope.
    *
    * @param f fqn to add
    * @param n node to add
    * @since 3.0.
    */
   public abstract void putLookedUpNode(Fqn f, NodeSPI n);

   /**
    * Adds a map of looked up nodes to the current map of looked up nodes
    *
    * @param lookedUpNodes looked up nodes to add
    */
   public abstract void putLookedUpNodes(Map<Fqn, NodeSPI> lookedUpNodes);

   /**
    * Clears the registry of looked up nodes.
    *
    * @since 3.0.
    */
   public abstract void clearLookedUpNodes();

   /**
    * Retrieves a map of nodes looked up within the current invocation's scope.
    *
    * @return a map of looked up nodes.
    * @since 3.0
    */
   public abstract Map<Fqn, NodeSPI> getLookedUpNodes();

   /**
    * Marks teh context as only rolling back.
    *
    * @param localRollbackOnly if true, the context is only rolling back.
    */
   public void setLocalRollbackOnly(boolean localRollbackOnly)
   {
      this.localRollbackOnly = localRollbackOnly;
   }

   /**
    * Retrieves the transaction associated with this invocation
    *
    * @return The transaction associated with this invocation
    */
   public Transaction getTransaction()
   {
      return transaction;
   }

   /**
    * Sets a transaction object on the invocation context.
    *
    * @param transaction transaction to set
    */
   public void setTransaction(Transaction transaction)
   {
      this.transaction = transaction;
   }

   /**
    * @return the transaction entry associated with the current transaction, or null if the current thread is not associated with a transaction.
    * @since 2.2.0
    */
   public TransactionContext getTransactionContext()
   {
      return transactionContext;
   }

   /**
    * Sets the transaction context to be associated with the current thread.
    *
    * @param transactionContext transaction context to set
    * @since 2.2.0
    */
   public void setTransactionContext(TransactionContext transactionContext)
   {
      this.transactionContext = transactionContext;
   }

   /**
    * Retrieves the global transaction associated with this invocation
    *
    * @return the global transaction associated with this invocation
    */
   public GlobalTransaction getGlobalTransaction()
   {
      return globalTransaction;
   }

   /**
    * Sets the global transaction associated with this invocation
    *
    * @param globalTransaction global transaction to set
    */
   public void setGlobalTransaction(GlobalTransaction globalTransaction)
   {
      this.globalTransaction = globalTransaction;
   }


   /**
    * Retrieves the option overrides associated with this invocation
    *
    * @return the option overrides associated with this invocation
    */
   public Option getOptionOverrides()
   {
      if (optionOverrides == null)
      {
         optionOverrides = new Option();
      }
      return optionOverrides;
   }

   /**
    * @return true of no options have been set on this context, false otherwise.
    */
   public boolean isOptionsUninitialised()
   {
      return optionOverrides == null;
   }

   /**
    * Sets the option overrides to be associated with this invocation
    *
    * @param optionOverrides options to set
    */
   public void setOptionOverrides(Option optionOverrides)
   {
      this.optionOverrides = optionOverrides;
   }

   /**
    * Tests if this invocation originated locally or from a remote cache.
    *
    * @return true if the invocation originated locally.
    */
   public boolean isOriginLocal()
   {
      return originLocal;
   }

   /**
    * Returns an immutable,  defensive copy of the List of locks currently maintained for the current scope.
    * <p/>
    * Note that if a transaction is in scope, implementations should retrieve these locks from the {@link org.jboss.cache.transaction.TransactionContext}.
    * Retrieving locks from this method should always ensure they are retrieved from  the appropriate scope.
    * <p/>
    * Note that currently (as of 3.0.0) this list is unchecked.  This is to allow support for both MVCC (which uses Fqns as locks)
    * as well as legacy Optimistic and Pessimistic Locking schemes (which use {@link org.jboss.cache.lock.NodeLock} as locks).  Once support for
    * legacy node locking schemes are dropped, this method will be more strongly typed to return <tt>List<Fqn></tt>.
    *
    * @return locks held in current scope.
    */
   @SuppressWarnings("unchecked")
   public List getLocks()
   {
      // first check transactional scope
      if (transactionContext != null) return transactionContext.getLocks();
      return invocationLocks == null || invocationLocks.isEmpty() ? Collections.emptyList() : Immutables.immutableListConvert(invocationLocks);
   }

   /**
    * Adds a List of locks to the currently maintained collection of locks acquired.
    * <p/>
    * Note that if a transaction is in scope, implementations should record locks on the {@link org.jboss.cache.transaction.TransactionContext}.
    * Adding locks using this method should always ensure they are applied to the appropriate scope.
    * <p/>
    * Note that currently (as of 3.0.0) this list is unchecked.  This is to allow support for both MVCC (which uses Fqns as locks)
    * as well as legacy Optimistic and Pessimistic Locking schemes (which use {@link org.jboss.cache.lock.NodeLock} as locks).  Once support for
    * legacy node locking schemes are dropped, this method will be more strongly typed to accept <tt>List<Fqn></tt>.
    *
    * @param locks locks to add
    */
   @SuppressWarnings("unchecked")
   public void addAllLocks(List locks)
   {
      // first check transactional scope
      if (transactionContext != null)
      {
         transactionContext.addAllLocks(locks);
      }
      else
      {
         // no need to worry about concurrency here - a context is only valid for a single thread.
         if (invocationLocks == null) invocationLocks = new LinkedHashSet(4);
         invocationLocks.addAll(locks);
      }
   }

   /**
    * Adds a lock to the currently maintained collection of locks acquired.
    * <p/>
    * Note that if a transaction is in scope, implementations should record this lock on the {@link org.jboss.cache.transaction.TransactionContext}.
    * Using this method should always ensure that the appropriate scope is used.
    * <p/>
    * Note that currently (as of 3.0.0) this lock is weakly typed.  This is to allow support for both MVCC (which uses {@link Fqn}s as locks)
    * as well as legacy Optimistic and Pessimistic Locking schemes (which use {@link org.jboss.cache.lock.NodeLock} as locks).  Once support for
    * legacy node locking schemes are dropped, this method will be more strongly typed to accept {@link Fqn}.
    *
    * @param lock lock to add
    */
   @SuppressWarnings("unchecked")
   public void addLock(Object lock)
   {
      // first check transactional scope
      if (transactionContext != null)
      {
         transactionContext.addLock(lock);
      }
      else
      {
         // no need to worry about concurrency here - a context is only valid for a single thread.
         if (invocationLocks == null) invocationLocks = new LinkedHashSet(4);
         invocationLocks.add(lock);
      }
   }

   /**
    * Removes a lock from the currently maintained collection of locks acquired.
    * <p/>
    * Note that if a transaction is in scope, implementations should remove this lock from the {@link org.jboss.cache.transaction.TransactionContext}.
    * Using this method should always ensure that the lock is removed from the appropriate scope.
    * <p/>
    * Note that currently (as of 3.0.0) this lock is weakly typed.  This is to allow support for both MVCC (which uses {@link Fqn}s as locks)
    * as well as legacy Optimistic and Pessimistic Locking schemes (which use {@link org.jboss.cache.lock.NodeLock} as locks).  Once support for
    * legacy node locking schemes are dropped, this method will be more strongly typed to accept {@link Fqn}.
    *
    * @param lock lock to remove
    */
   @SuppressWarnings("unchecked")
   public void removeLock(Object lock)
   {
      // first check transactional scope
      if (transactionContext != null)
      {
         transactionContext.removeLock(lock);
      }
      else
      {
         // no need to worry about concurrency here - a context is only valid for a single thread.
         if (invocationLocks != null) invocationLocks.remove(lock);
      }
   }

   /**
    * Clears all locks from the currently maintained collection of locks acquired.
    * <p/>
    * Note that if a transaction is in scope, implementations should clear locks from the {@link org.jboss.cache.transaction.TransactionContext}.
    * Using this method should always ensure locks are cleared in the appropriate scope.
    * <p/>
    * Note that currently (as of 3.0.0) this lock is weakly typed.  This is to allow support for both MVCC (which uses {@link Fqn}s as locks)
    * as well as legacy Optimistic and Pessimistic Locking schemes (which use {@link org.jboss.cache.lock.NodeLock} as locks).  Once support for
    * legacy node locking schemes are dropped, this method will be more strongly typed to accept {@link Fqn}.
    */
   public void clearLocks()
   {
      // first check transactional scope
      if (transactionContext != null)
      {
         transactionContext.clearLocks();
      }
      else
      {
         // no need to worry about concurrency here - a context is only valid for a single thread.
         if (invocationLocks != null) invocationLocks.clear();
      }
   }

   /**
    * Note that if a transaction is in scope, implementations should test this lock from on {@link org.jboss.cache.transaction.TransactionContext}.
    * Using this method should always ensure locks checked in the appropriate scope.
    *
    * @param lock lock to test
    * @return true if the lock being tested is already held in the current scope, false otherwise.
    */
   public boolean hasLock(Object lock)
   {
      // first check transactional scope
      if (transactionContext != null)
      {
         return transactionContext.hasLock(lock);
      }
      else
      {
         return invocationLocks != null && invocationLocks.contains(lock);
      }
   }

   /**
    * @return true if options exist to suppress locking - false otherwise.  Note that this is only used by the {@link org.jboss.cache.interceptors.PessimisticLockInterceptor}.
    */
   public boolean isLockingSuppressed()
   {
      return getOptionOverrides() != null && getOptionOverrides().isSuppressLocking();
   }

   /**
    * If set to true, the invocation is assumed to have originated locally.  If set to false,
    * assumed to have originated from a remote cache.
    *
    * @param originLocal flag to set
    */
   public void setOriginLocal(boolean originLocal)
   {
      this.originLocal = originLocal;
   }

   /**
    * @return true if the current transaction is set to rollback only.
    */
   public boolean isLocalRollbackOnly()
   {
      return localRollbackOnly;
   }

   /**
    * Resets the context, freeing up any references.
    */
   public void reset()
   {
      transaction = null;
      globalTransaction = null;
      optionOverrides = null;
      originLocal = true;
      invocationLocks = null;
      methodCall = null;
      command = null;
      fqnsLoaded = null;
      bypassUnmarshalling = false;
   }

   /**
    * This is a "copy-factory-method" that should be used whenever a clone of this class is needed.  The resulting instance
    * is equal() to, but not ==, to the InvocationContext invoked on.  Note that this is a shallow copy with the exception
    * of the Option object, which is deep, as well as any collections held on the context such as locks.  Note that the reference
    * to a {@link org.jboss.cache.transaction.TransactionContext}, if any, is maintained.
    *
    * @return a new InvocationContext
    */
   @SuppressWarnings("unchecked")
   public abstract InvocationContext copy();

   /**
    * Sets the state of the InvocationContext based on the template context passed in
    *
    * @param template template to copy from
    */
   public void setState(InvocationContext template)
   {
      if (template == null)
      {
         throw new NullPointerException("Template InvocationContext passed in to InvocationContext.setState() passed in is null");
      }

      this.setGlobalTransaction(template.getGlobalTransaction());
      this.setLocalRollbackOnly(template.isLocalRollbackOnly());
      this.setOptionOverrides(template.getOptionOverrides());
      this.setOriginLocal(template.isOriginLocal());
      this.setTransaction(template.getTransaction());
   }

   /**
    * @return the method call associated with this invocation
    */
   @Deprecated
   @SuppressWarnings("deprecation")
   public MethodCall getMethodCall()
   {
      if (methodCall == null)
      {
         methodCall = createMethodCall();
      }
      return methodCall;
   }

   @SuppressWarnings("deprecation")
   private MethodCall createMethodCall()
   {
      if (command == null) return null;
      MethodCall call = new MethodCall();
      call.setMethodId(command.getCommandId());
      call.setArgs(command.getParameters());
      return call;
   }

   /**
    * Sets the method call associated with this invocation.
    *
    * @param methodCall methodcall to set
    * @deprecated not used anymore.  Interceptors will get a {@link org.jboss.cache.commands.ReplicableCommand} instance passed in along with an InvocationContext.
    */
   @Deprecated
   public void setMethodCall(MethodCall methodCall)
   {
      this.methodCall = methodCall;
   }

   /**
    * If the lock acquisition timeout is overridden for current call using an option, then return that one.
    * If not overridden, return default value.
    *
    * @param timeout timeout to fall back to
    * @return timeout to use
    */
   public long getLockAcquisitionTimeout(long timeout)
   {
      // TODO: this stuff really doesn't belong here.  Put it somewhere else.
      if (getOptionOverrides() != null
            && getOptionOverrides().getLockAcquisitionTimeout() >= 0)
      {
         timeout = getOptionOverrides().getLockAcquisitionTimeout();
      }
      return timeout;
   }

   /**
    * This is only used for backward compatibility with old interceptors implementation and should <b>NOT</b> be
    * use by any new custom interceptors. The commands is now passed in as the second param in each implementing
    * handlers (handler = method in ChainedInterceptor class)
    *
    * @param cacheCommand command to set
    */
   @Deprecated
   @SuppressWarnings("deprecation")
   public void setCommand(VisitableCommand cacheCommand)
   {
      this.command = cacheCommand;
   }

   /**
    * @return command that is in scope
    * @see #setCommand(org.jboss.cache.commands.VisitableCommand)
    */
   @Deprecated
   @SuppressWarnings("deprecation")
   public VisitableCommand getCommand()
   {
      return command;
   }

   /**
    * @return true if there is current transaction associated with the invocation, and this transaction is in a valid state.
    */
   public boolean isValidTransaction()
   {
      // ought to move to the transaction context
      return transaction != null && TransactionTable.isValid(transaction);
   }

   /**
    * Throws the given throwable provided no options suppress or prevent this from happening.
    *
    * @param e throwable to throw
    * @throws Throwable if allowed to throw one.
    */
   public void throwIfNeeded(Throwable e) throws Throwable
   {
      // TODO: this stuff really doesn't belong here.  Put it somewhere else.
      Option optionOverride = getOptionOverrides();
      boolean shouldRethtrow = optionOverride == null || !optionOverride.isFailSilently();
      if (!shouldRethtrow)
      {
         if (trace)
         {
            log.trace("There was a problem handling this request, but failSilently was set, so suppressing exception", e);
         }
         return;
      }
      throw e;
   }

   @SuppressWarnings("unchecked")
   protected void doCopy(InvocationContext copy)
   {
      copy.command = command;
      copy.globalTransaction = globalTransaction;
      copy.invocationLocks = invocationLocks == null ? null : new LinkedHashSet(invocationLocks);
      copy.localRollbackOnly = localRollbackOnly;
      copy.optionOverrides = optionOverrides == null ? null : optionOverrides.copy();
      copy.originLocal = originLocal;
      copy.transaction = transaction;
      copy.transactionContext = transactionContext;
      copy.fqnsLoaded = fqnsLoaded;
      copy.bypassUnmarshalling = bypassUnmarshalling;
   }

   /**
    * Adds an Fqn to the set of Fqns loaded by the cache loader interceptor.  Instantiates the set lazily if needed.
    *
    * @param fqn fqn to add
    */
   public void addFqnLoaded(Fqn fqn)
   {
      if (fqnsLoaded == null) fqnsLoaded = new HashSet<Fqn>();
      fqnsLoaded.add(fqn);
   }

   /**
    * @return Set of Fqns loaded by the cache loader interceptor.  Only recorded if needed, such as by the ActivationInterceptor
    */
   public Set<Fqn> getFqnsLoaded()
   {
      return fqnsLoaded;
   }

   public void setFqnsLoaded(Set<Fqn> fqnsLoaded)
   {
      this.fqnsLoaded = fqnsLoaded;
   }

   @Override
   public String toString()
   {
      return "InvocationContext{" +
            "transaction=" + transaction +
            ", globalTransaction=" + globalTransaction +
            ", transactionContext=" + transactionContext +
            ", optionOverrides=" + optionOverrides +
            ", originLocal=" + originLocal +
            ", bypassUnmarshalling=" + bypassUnmarshalling +
            '}';
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      final InvocationContext that = (InvocationContext) o;

      if (localRollbackOnly != that.localRollbackOnly) return false;
      if (bypassUnmarshalling != that.bypassUnmarshalling) return false;
      if (originLocal != that.originLocal) return false;
      if (globalTransaction != null ? !globalTransaction.equals(that.globalTransaction) : that.globalTransaction != null)
      {
         return false;
      }
      if (optionOverrides != null ? !optionOverrides.equals(that.optionOverrides) : that.optionOverrides != null)
      {
         return false;
      }
      if (transaction != null ? !transaction.equals(that.transaction) : that.transaction != null) return false;

      return true;
   }

   @Override
   public int hashCode()
   {
      int result;
      result = (transaction != null ? transaction.hashCode() : 0);
      result = 29 * result + (globalTransaction != null ? globalTransaction.hashCode() : 0);
      result = 29 * result + (optionOverrides != null ? optionOverrides.hashCode() : 0);
      result = 29 * result + (originLocal ? 1 : 0);
      result = 29 * result + (localRollbackOnly ? 1 : 0);
      result = 29 * result + (bypassUnmarshalling ? 1 : 0);
      return result;
   }

   public void setBypassUnmarshalling(boolean b)
   {
      this.bypassUnmarshalling = b;
   }

   public boolean isBypassUnmarshalling()
   {
      return bypassUnmarshalling;
   }
}
