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
package org.jboss.cache.notifications;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.Cache;
import org.jboss.cache.CacheException;
import org.jboss.cache.CacheSPI;
import org.jboss.cache.Fqn;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.buddyreplication.BuddyGroup;
import org.jboss.cache.config.Configuration;
import org.jboss.cache.factories.annotations.Destroy;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.factories.annotations.NonVolatile;
import org.jboss.cache.factories.annotations.Start;
import org.jboss.cache.factories.annotations.Stop;
import org.jboss.cache.marshall.MarshalledValueMap;
import org.jboss.cache.notifications.annotation.*;
import org.jboss.cache.notifications.event.*;
import static org.jboss.cache.notifications.event.Event.Type.*;
import org.jboss.cache.util.Immutables;
import org.jboss.cache.util.concurrent.BoundedExecutors;
import org.jboss.cache.util.concurrent.WithinThreadExecutor;
import org.jgroups.View;

import javax.transaction.Transaction;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Helper class that handles all notifications to registered listeners.
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani (manik AT jboss DOT org)</a>
 * @author <a href="mailto:galder.zamarreno@jboss.com">Galder Zamarreno</a>
 */
@NonVolatile
public class NotifierImpl implements Notifier
{
   private static final Log log = LogFactory.getLog(NotifierImpl.class);

   private static final Class emptyMap = Collections.emptyMap().getClass();

   private static final Class singletonMap = Collections.singletonMap(null, null).getClass();
   private static final Class[] allowedMethodAnnotations =
         {
               CacheStarted.class, CacheStopped.class, CacheBlocked.class, CacheUnblocked.class, NodeCreated.class, NodeRemoved.class, NodeVisited.class, NodeModified.class, NodeMoved.class,
               NodeActivated.class, NodePassivated.class, NodeLoaded.class, NodeEvicted.class, TransactionRegistered.class, TransactionCompleted.class, ViewChanged.class, BuddyGroupChanged.class,
               NodeInvalidated.class
         };
   private static final Class[] parameterTypes =
         {
               CacheStartedEvent.class, CacheStoppedEvent.class, CacheBlockedEvent.class, CacheUnblockedEvent.class, NodeCreatedEvent.class, NodeRemovedEvent.class, NodeVisitedEvent.class, NodeModifiedEvent.class, NodeMovedEvent.class,
               NodeActivatedEvent.class, NodePassivatedEvent.class, NodeLoadedEvent.class, NodeEvictedEvent.class, TransactionRegisteredEvent.class, TransactionCompletedEvent.class, ViewChangedEvent.class, BuddyGroupChangedEvent.class,
               NodeInvalidatedEvent.class
         };

   final Map<Class<? extends Annotation>, List<ListenerInvocation>> listenersMap = new HashMap<Class<? extends Annotation>, List<ListenerInvocation>>(32);
   final List<ListenerInvocation> cacheStartedListeners = new CopyOnWriteArrayList<ListenerInvocation>();
   final List<ListenerInvocation> cacheStoppedListeners = new CopyOnWriteArrayList<ListenerInvocation>();
   final List<ListenerInvocation> cacheBlockedListeners = new CopyOnWriteArrayList<ListenerInvocation>();
   final List<ListenerInvocation> cacheUnblockedListeners = new CopyOnWriteArrayList<ListenerInvocation>();
   final List<ListenerInvocation> nodeCreatedListeners = new CopyOnWriteArrayList<ListenerInvocation>();
   final List<ListenerInvocation> nodeRemovedListeners = new CopyOnWriteArrayList<ListenerInvocation>();
   final List<ListenerInvocation> nodeVisitedListeners = new CopyOnWriteArrayList<ListenerInvocation>();
   final List<ListenerInvocation> nodeModifiedListeners = new CopyOnWriteArrayList<ListenerInvocation>();
   final List<ListenerInvocation> nodeMovedListeners = new CopyOnWriteArrayList<ListenerInvocation>();
   final List<ListenerInvocation> nodeActivatedListeners = new CopyOnWriteArrayList<ListenerInvocation>();
   final List<ListenerInvocation> nodePassivatedListeners = new CopyOnWriteArrayList<ListenerInvocation>();
   final List<ListenerInvocation> nodeLoadedListeners = new CopyOnWriteArrayList<ListenerInvocation>();
   final List<ListenerInvocation> nodeInvalidatedListeners = new CopyOnWriteArrayList<ListenerInvocation>();
   final List<ListenerInvocation> nodeEvictedListeners = new CopyOnWriteArrayList<ListenerInvocation>();
   final List<ListenerInvocation> transactionRegisteredListeners = new CopyOnWriteArrayList<ListenerInvocation>();
   final List<ListenerInvocation> transactionCompletedListeners = new CopyOnWriteArrayList<ListenerInvocation>();
   final List<ListenerInvocation> viewChangedListeners = new CopyOnWriteArrayList<ListenerInvocation>();
   final List<ListenerInvocation> buddyGroupChangedListeners = new CopyOnWriteArrayList<ListenerInvocation>();

   //   final Map<Class, List<ListenerInvocation>> listenerInvocations = new ConcurrentHashMap<Class, List<ListenerInvocation>>();
   private Cache cache;
   private boolean useMarshalledValueMaps;
   private Configuration config;
   // two separate executor services, one for sync and one for async listeners
   private ExecutorService syncProcessor;
   private ExecutorService asyncProcessor;
   private static final AtomicInteger asyncNotifierThreadNumber = new AtomicInteger(0);

   public NotifierImpl()
   {
      listenersMap.put(CacheStarted.class, cacheStartedListeners);
      listenersMap.put(CacheStopped.class, cacheStoppedListeners);
      listenersMap.put(CacheBlocked.class, cacheBlockedListeners);
      listenersMap.put(CacheUnblocked.class, cacheUnblockedListeners);
      listenersMap.put(NodeCreated.class, nodeCreatedListeners);
      listenersMap.put(NodeRemoved.class, nodeRemovedListeners);
      listenersMap.put(NodeVisited.class, nodeVisitedListeners);
      listenersMap.put(NodeModified.class, nodeModifiedListeners);
      listenersMap.put(NodeMoved.class, nodeMovedListeners);
      listenersMap.put(NodeActivated.class, nodeActivatedListeners);
      listenersMap.put(NodePassivated.class, nodePassivatedListeners);
      listenersMap.put(NodeLoaded.class, nodeLoadedListeners);
      listenersMap.put(NodeEvicted.class, nodeEvictedListeners);
      listenersMap.put(TransactionRegistered.class, transactionRegisteredListeners);
      listenersMap.put(TransactionCompleted.class, transactionCompletedListeners);
      listenersMap.put(ViewChanged.class, viewChangedListeners);
      listenersMap.put(BuddyGroupChanged.class, buddyGroupChangedListeners);
      listenersMap.put(NodeInvalidated.class, nodeInvalidatedListeners);
   }

   @Inject
   void injectDependencies(CacheSPI cache, Configuration config)
   {
      this.cache = cache;
      this.config = config;
   }

   @Stop
   void stop()
   {
      if (syncProcessor != null) syncProcessor.shutdownNow();
      if (asyncProcessor != null) asyncProcessor.shutdownNow();
   }

   @Destroy
   void destroy()
   {
      removeAllCacheListeners();
   }

   @Start
   void start()
   {
      useMarshalledValueMaps = config.isUseLazyDeserialization();
      syncProcessor = new WithinThreadExecutor();

      // first try and use an injected executor for async listeners
      if ((asyncProcessor = config.getRuntimeConfig().getAsyncCacheListenerExecutor()) == null)
      {
         // create one if needed
         if (config.getListenerAsyncPoolSize() > 0)
         {
            asyncProcessor = BoundedExecutors.newFixedThreadPool(config.getListenerAsyncPoolSize(), new ThreadFactory()
            {
               public Thread newThread(Runnable r)
               {
                  return new Thread(r, "AsyncNotifier-" + asyncNotifierThreadNumber.getAndIncrement());
               }
            }, config.getListenerAsyncQueueSize());
         }
         else
         {
            // use the same sync executor
            asyncProcessor = syncProcessor;
         }
      }
   }

   /**
    * Loops through all valid methods on the object passed in, and caches the relevant methods as {@link NotifierImpl.ListenerInvocation}
    * for invocation by reflection.
    *
    * @param listener object to be considered as a listener.
    */
   @SuppressWarnings("unchecked")
   private void validateAndAddListenerInvocation(Object listener)
   {
      boolean sync = testListenerClassValidity(listener.getClass());

      boolean foundMethods = false;
      // now try all methods on the listener for anything that we like.  Note that only PUBLIC methods are scanned.
      for (Method m : listener.getClass().getMethods())
      {
         // loop through all valid method annotations
         for (int i = 0; i < allowedMethodAnnotations.length; i++)
         {
            if (m.isAnnotationPresent(allowedMethodAnnotations[i]))
            {
               testListenerMethodValidity(m, parameterTypes[i], allowedMethodAnnotations[i].getName());
               addListenerInvocation(allowedMethodAnnotations[i], new ListenerInvocation(listener, m, sync));
               foundMethods = true;
            }
         }
      }

      if (!foundMethods && log.isWarnEnabled())
         log.warn("Attempted to register listener of class " + listener.getClass() + ", but no valid, public methods annotated with method-level event annotations found! Ignoring listener.");
   }

   /**
    * Tests if a class is properly annotated as a CacheListener and returns whether callbacks on this class should be invoked
    * synchronously or asynchronously.
    *
    * @param listenerClass class to inspect
    * @return true if callbacks on this class should use the syncProcessor; false if it should use the asyncProcessor.
    */
   private static boolean testListenerClassValidity(Class<?> listenerClass)
   {
      CacheListener cl = listenerClass.getAnnotation(CacheListener.class);
      if (cl == null)
         throw new IncorrectCacheListenerException("Cache listener class MUST be annotated with org.jboss.cache.notifications.annotation.CacheListener");
      if (!Modifier.isPublic(listenerClass.getModifiers()))
         throw new IncorrectCacheListenerException("Cache listener class MUST be public!");
      return cl.sync();

   }

   private static void testListenerMethodValidity(Method m, Class allowedParameter, String annotationName)
   {
      if (m.getParameterTypes().length != 1 || !m.getParameterTypes()[0].isAssignableFrom(allowedParameter))
         throw new IncorrectCacheListenerException("Methods annotated with " + annotationName + " must accept exactly one parameter, of assignable from type " + allowedParameter.getName());
      if (!m.getReturnType().equals(void.class))
         throw new IncorrectCacheListenerException("Methods annotated with " + annotationName + " should have a return type of void.");
   }

   private void addListenerInvocation(Class annotation, ListenerInvocation li)
   {
      List<ListenerInvocation> result = getListenerCollectionForAnnotation(annotation);
      result.add(li);
   }

   public void addCacheListener(Object listener)
   {
      validateAndAddListenerInvocation(listener);
   }

   public void removeCacheListener(Object listener)
   {
      for (Class annotation : allowedMethodAnnotations) removeListenerInvocation(annotation, listener);
   }

   private void removeListenerInvocation(Class annotation, Object listener)
   {
      if (listener == null) return;
      List<ListenerInvocation> l = getListenerCollectionForAnnotation(annotation);
      Set<Object> markedForRemoval = new HashSet<Object>();
      for (ListenerInvocation li : l)
      {
         if (listener.equals(li.target)) markedForRemoval.add(li);
      }
      l.removeAll(markedForRemoval);
   }

   /**
    * Removes all listeners from the notifier, including the evictionPolicyListener.
    */
   @Stop(priority = 99)
   public void removeAllCacheListeners()
   {
      cacheStartedListeners.clear();
      cacheStoppedListeners.clear();
      cacheBlockedListeners.clear();
      cacheUnblockedListeners.clear();
      nodeCreatedListeners.clear();
      nodeRemovedListeners.clear();
      nodeVisitedListeners.clear();
      nodeModifiedListeners.clear();
      nodeMovedListeners.clear();
      nodeActivatedListeners.clear();
      nodePassivatedListeners.clear();
      nodeLoadedListeners.clear();
      nodeEvictedListeners.clear();
      transactionRegisteredListeners.clear();
      transactionCompletedListeners.clear();
      viewChangedListeners.clear();
      buddyGroupChangedListeners.clear();
   }

   public Set<Object> getCacheListeners()
   {
      Set<Object> result = new HashSet<Object>();
      for (List<ListenerInvocation> list : listenersMap.values())
      {
         for (ListenerInvocation li : list) result.add(li.target);
      }
      return Collections.unmodifiableSet(result);
   }

   public void notifyNodeCreated(Fqn fqn, boolean pre, InvocationContext ctx)
   {
      if (!nodeCreatedListeners.isEmpty() && !ctx.getOptionOverrides().isSuppressEventNotification())
      {
         boolean originLocal = ctx.isOriginLocal();
         Transaction tx = ctx.getTransaction();
         InvocationContext backup = resetInvocationContext(ctx);
         EventImpl e = new EventImpl();
         e.setCache(cache);
         e.setOriginLocal(originLocal);
         e.setPre(pre);
         e.setFqn(fqn);
         e.setTransaction(tx);
         e.setType(NODE_CREATED);
         for (ListenerInvocation listener : nodeCreatedListeners) listener.invoke(e);
         restoreInvocationContext(backup);
      }
   }

   public void notifyNodeModified(Fqn fqn, boolean pre, NodeModifiedEvent.ModificationType modificationType, Map data, InvocationContext ctx)
   {
      if (!nodeModifiedListeners.isEmpty() && !ctx.getOptionOverrides().isSuppressEventNotification())
      {
         boolean originLocal = ctx.isOriginLocal();
         Map dataCopy = copy(data, useMarshalledValueMaps);
         Transaction tx = ctx.getTransaction();
         InvocationContext backup = resetInvocationContext(ctx);
         EventImpl e = new EventImpl();
         e.setCache(cache);
         e.setOriginLocal(originLocal);
         e.setPre(pre);
         e.setFqn(fqn);
         e.setTransaction(tx);
         e.setModificationType(modificationType);
         e.setData(dataCopy);
         e.setType(NODE_MODIFIED);
         for (ListenerInvocation listener : nodeModifiedListeners) listener.invoke(e);
         restoreInvocationContext(backup);
      }
   }

   public boolean shouldNotifyOnNodeModified()
   {
      return !nodeModifiedListeners.isEmpty();
   }

   public void notifyNodeRemoved(Fqn fqn, boolean pre, Map data, InvocationContext ctx)
   {
      if (!nodeRemovedListeners.isEmpty() && !ctx.getOptionOverrides().isSuppressEventNotification())
      {
         boolean originLocal = ctx.isOriginLocal();
         Map dataCopy = copy(data, useMarshalledValueMaps);
         Transaction tx = ctx.getTransaction();
         InvocationContext backup = resetInvocationContext(ctx);
         EventImpl e = new EventImpl();
         e.setCache(cache);
         e.setOriginLocal(originLocal);
         e.setPre(pre);
         e.setFqn(fqn);
         e.setTransaction(tx);
         e.setData(dataCopy);
         e.setType(NODE_REMOVED);
         for (ListenerInvocation listener : nodeRemovedListeners) listener.invoke(e);
         restoreInvocationContext(backup);
      }
   }

   public void notifyNodeVisited(Fqn fqn, boolean pre, InvocationContext ctx)
   {
      if (!nodeVisitedListeners.isEmpty() && !ctx.getOptionOverrides().isSuppressEventNotification())
      {
         Transaction tx = ctx.getTransaction();
         InvocationContext backup = resetInvocationContext(ctx);
         EventImpl e = new EventImpl();
         e.setCache(cache);
         e.setPre(pre);
         e.setFqn(fqn);
         e.setTransaction(tx);
         e.setType(NODE_VISITED);
         for (ListenerInvocation listener : nodeVisitedListeners) listener.invoke(e);
         restoreInvocationContext(backup);
      }
   }

   public void notifyNodeMoved(Fqn originalFqn, Fqn newFqn, boolean pre, InvocationContext ctx)
   {
      if (!nodeMovedListeners.isEmpty() && !ctx.getOptionOverrides().isSuppressEventNotification())
      {
         boolean originLocal = ctx.isOriginLocal();
         Transaction tx = ctx.getTransaction();
         InvocationContext backup = resetInvocationContext(ctx);
         EventImpl e = new EventImpl();
         e.setCache(cache);
         e.setOriginLocal(originLocal);
         e.setPre(pre);
         e.setFqn(originalFqn);
         e.setTargetFqn(newFqn);
         e.setTransaction(tx);
         e.setType(NODE_MOVED);
         for (ListenerInvocation listener : nodeMovedListeners) listener.invoke(e);
         restoreInvocationContext(backup);
      }
   }

   public void notifyNodeEvicted(final Fqn fqn, final boolean pre, InvocationContext ctx)
   {
      if (!nodeEvictedListeners.isEmpty() && !ctx.getOptionOverrides().isSuppressEventNotification())
      {
         final boolean originLocal = ctx.isOriginLocal();
         Transaction tx = ctx.getTransaction();
         InvocationContext backup = resetInvocationContext(ctx);
         EventImpl e = new EventImpl();
         e.setCache(cache);
         e.setOriginLocal(originLocal);
         e.setPre(pre);
         e.setFqn(fqn);
         e.setTransaction(tx);
         e.setType(NODE_EVICTED);
         for (ListenerInvocation listener : nodeEvictedListeners) listener.invoke(e);
         restoreInvocationContext(backup);
      }
   }

   public void notifyNodeInvalidated(final Fqn fqn, final boolean pre, InvocationContext ctx)
   {
      if (!nodeInvalidatedListeners.isEmpty() && !ctx.getOptionOverrides().isSuppressEventNotification())
      {
         final boolean originLocal = ctx.isOriginLocal();
         Transaction tx = ctx.getTransaction();
         InvocationContext backup = resetInvocationContext(ctx);
         EventImpl e = new EventImpl();
         e.setCache(cache);
         e.setOriginLocal(originLocal);
         e.setPre(pre);
         e.setFqn(fqn);
         e.setTransaction(tx);
         e.setType(NODE_INVALIDATED);
         for (ListenerInvocation listener : nodeInvalidatedListeners) listener.invoke(e);
         restoreInvocationContext(backup);
      }
   }

   public void notifyNodeLoaded(Fqn fqn, boolean pre, Map data, InvocationContext ctx)
   {
      if (!nodeLoadedListeners.isEmpty() && !ctx.getOptionOverrides().isSuppressEventNotification())
      {
         boolean originLocal = ctx.isOriginLocal();
         Map dataCopy = copy(data, useMarshalledValueMaps);
         Transaction tx = ctx.getTransaction();
         InvocationContext backup = resetInvocationContext(ctx);
         EventImpl e = new EventImpl();
         e.setCache(cache);
         e.setOriginLocal(originLocal);
         e.setPre(pre);
         e.setFqn(fqn);
         e.setTransaction(tx);
         e.setData(dataCopy);
         e.setType(NODE_LOADED);
         for (ListenerInvocation listener : nodeLoadedListeners) listener.invoke(e);
         restoreInvocationContext(backup);
      }
   }

   public void notifyNodeActivated(Fqn fqn, boolean pre, Map data, InvocationContext ctx)
   {
      if (!nodeActivatedListeners.isEmpty() && !ctx.getOptionOverrides().isSuppressEventNotification())
      {
         boolean originLocal = ctx.isOriginLocal();
         Map dataCopy = copy(data, useMarshalledValueMaps);
         Transaction tx = ctx.getTransaction();
         InvocationContext backup = resetInvocationContext(ctx);
         EventImpl e = new EventImpl();
         e.setCache(cache);
         e.setOriginLocal(originLocal);
         e.setPre(pre);
         e.setFqn(fqn);
         e.setTransaction(tx);
         e.setData(dataCopy);
         e.setType(NODE_ACTIVATED);
         for (ListenerInvocation listener : nodeActivatedListeners) listener.invoke(e);
         restoreInvocationContext(backup);
      }
   }

   public void notifyNodePassivated(Fqn fqn, boolean pre, Map data, InvocationContext ctx)
   {
      if (!nodePassivatedListeners.isEmpty() && !ctx.getOptionOverrides().isSuppressEventNotification())
      {
         Map dataCopy = copy(data, useMarshalledValueMaps);
         Transaction tx = ctx.getTransaction();
         InvocationContext backup = resetInvocationContext(ctx);
         EventImpl e = new EventImpl();
         e.setCache(cache);
         e.setPre(pre);
         e.setFqn(fqn);
         e.setTransaction(tx);
         e.setData(dataCopy);
         e.setType(NODE_PASSIVATED);
         for (ListenerInvocation listener : nodePassivatedListeners) listener.invoke(e);
         restoreInvocationContext(backup);
      }
   }

   /**
    * Notifies all registered listeners of a cacheStarted event.
    */
   @Start(priority = 99)
   public void notifyCacheStarted()
   {
      if (!cacheStartedListeners.isEmpty())
      {
         EventImpl e = new EventImpl();
         e.setCache(cache);
         e.setType(CACHE_STARTED);
         for (ListenerInvocation listener : cacheStartedListeners) listener.invoke(e);
      }
   }

   /**
    * Notifies all registered listeners of a cacheStopped event.
    */
   @Stop(priority = 98)
   public void notifyCacheStopped()
   {
      if (!cacheStoppedListeners.isEmpty())
      {
         EventImpl e = new EventImpl();
         e.setCache(cache);
         e.setType(CACHE_STOPPED);
         for (ListenerInvocation listener : cacheStoppedListeners) listener.invoke(e);
      }
   }

   public void notifyViewChange(final View newView, InvocationContext ctx)
   {
      if (!viewChangedListeners.isEmpty())
      {
         InvocationContext backup = resetInvocationContext(ctx);
         EventImpl e = new EventImpl();
         e.setCache(cache);
         e.setNewView(newView);
         e.setType(VIEW_CHANGED);
         for (ListenerInvocation listener : viewChangedListeners) listener.invoke(e);
         restoreInvocationContext(backup);
      }
   }

   public void notifyBuddyGroupChange(final BuddyGroup buddyGroup, boolean pre)
   {
      if (!buddyGroupChangedListeners.isEmpty())
      {
         EventImpl e = new EventImpl();
         e.setCache(cache);
         e.setBuddyGroup(buddyGroup);
         e.setPre(pre);
         e.setType(BUDDY_GROUP_CHANGED);
         for (ListenerInvocation listener : buddyGroupChangedListeners) listener.invoke(e);
      }
   }

   public void notifyTransactionCompleted(Transaction transaction, boolean successful, InvocationContext ctx)
   {
      if (!transactionCompletedListeners.isEmpty() && !ctx.getOptionOverrides().isSuppressEventNotification())
      {
         boolean isOriginLocal = ctx.isOriginLocal();
         InvocationContext backup = resetInvocationContext(ctx);
         EventImpl e = new EventImpl();
         e.setCache(cache);
         e.setOriginLocal(isOriginLocal);
         e.setTransaction(transaction);
         e.setSuccessful(successful);
         e.setType(TRANSACTION_COMPLETED);
         for (ListenerInvocation listener : transactionCompletedListeners) listener.invoke(e);
         restoreInvocationContext(backup);
      }
   }

   public void notifyTransactionRegistered(Transaction transaction, InvocationContext ctx)
   {
      if (!transactionRegisteredListeners.isEmpty() && !ctx.getOptionOverrides().isSuppressEventNotification())
      {
         boolean isOriginLocal = ctx.isOriginLocal();
         InvocationContext backup = resetInvocationContext(ctx);
         EventImpl e = new EventImpl();
         e.setCache(cache);
         e.setOriginLocal(isOriginLocal);
         e.setTransaction(transaction);
         e.setType(TRANSACTION_REGISTERED);
         for (ListenerInvocation listener : transactionRegisteredListeners) listener.invoke(e);
         restoreInvocationContext(backup);
      }
   }

   public void notifyCacheBlocked(boolean pre)
   {
      if (!cacheBlockedListeners.isEmpty())
      {
         EventImpl e = new EventImpl();
         e.setCache(this.cache);
         e.setPre(pre);
         e.setType(CACHE_BLOCKED);
         for (ListenerInvocation listener : cacheBlockedListeners) listener.invoke(e);
      }
   }

   public void notifyCacheUnblocked(boolean pre)
   {
      if (!cacheUnblockedListeners.isEmpty())
      {
         EventImpl e = new EventImpl();
         e.setCache(this.cache);
         e.setPre(pre);
         e.setType(CACHE_UNBLOCKED);
         for (ListenerInvocation listener : cacheUnblockedListeners) listener.invoke(e);
      }
   }

   private static Map copy(Map data, boolean useMarshalledValueMaps)
   {
      if (data == null) return null;
      if (data.isEmpty()) return Collections.emptyMap();
      if (safe(data)) return useMarshalledValueMaps ? new MarshalledValueMap(data) : data;
      Map defensivelyCopiedData = Immutables.immutableMapCopy(data);
      return useMarshalledValueMaps ? new MarshalledValueMap(defensivelyCopiedData) : defensivelyCopiedData;
   }

   private void restoreInvocationContext(InvocationContext backup)
   {
      InvocationContext currentIC = cache.getInvocationContext();
      backup.putLookedUpNodes(currentIC.getLookedUpNodes());
      cache.setInvocationContext(backup);
   }

   /**
    * Resets the current (passed-in) invocation, and returns a temp InvocationContext containing its state so it can
    * be restored later using {@link #restoreInvocationContext(org.jboss.cache.InvocationContext)}
    *
    * @param ctx the current context to be reset
    * @return a clone of ctx, before it was reset
    */
   private InvocationContext resetInvocationContext(InvocationContext ctx)
   {
      // wipe current context.
      cache.setInvocationContext(null);
      // get a new Invocation Context
      InvocationContext newContext = cache.getInvocationContext();
      newContext.putLookedUpNodes(ctx.getLookedUpNodes());
      return ctx;
   }

   /**
    * A map is deemed 'safe' to be passed as-is to a listener, if either of the following are true:
    * <ul>
    * <li>It is null</li>
    * <li>It is an instance of {@link org.jboss.cache.util.ImmutableMapCopy}, which is immutable</li>
    * <li>It is an instance of {@link java.util.Collections#emptyMap()}, which is also immutable</li>
    * <li>It is an instance of {@link java.util.Collections#singletonMap(Object,Object)}, which is also immutable</li>
    * </ul>
    *
    * @param map
    * @return
    */
   private static boolean safe(Map map)
   {
      return map == null || Immutables.isImmutable(map) || map.getClass().equals(emptyMap) || map.getClass().equals(singletonMap);
   }

   /**
    * Class that encapsulates a valid invocation for a given registered listener - containing a reference to the
    * method to be invoked as well as the target object.
    */
   class ListenerInvocation
   {
      private final Object target;
      private final Method method;
      private final boolean sync;

      public ListenerInvocation(Object target, Method method, boolean sync)
      {
         this.target = target;
         this.method = method;
         this.sync = sync;
      }

      public void invoke(final Event e)
      {
         Runnable r = new Runnable()
         {

            public void run()
            {
               try
               {
                  method.invoke(target, e);
               }
               catch (InvocationTargetException exception)
               {
                  Throwable cause = exception.getCause();
                  if (cause != null)
                     throw new CacheException("Caught exception invoking method " + method + " on listener instance " + target, cause);
                  else
                     throw new CacheException("Caught exception invoking method " + method + " on listener instance " + target, exception);
               }
               catch (IllegalAccessException exception)
               {
                  log.warn("Unable to invoke method " + method + " on Object instance " + target + " - removing this target object from list of listeners!", exception);
                  removeCacheListener(target);
               }
            }
         };

         if (sync)
            syncProcessor.execute(r);
         else
            asyncProcessor.execute(r);

      }

   }

   private List<ListenerInvocation> getListenerCollectionForAnnotation(Class<? extends Annotation> annotation)
   {
      List<ListenerInvocation> list = listenersMap.get(annotation);
      if (list == null) throw new CacheException("Unknown listener annotation: " + annotation);
      return list;
   }
}
