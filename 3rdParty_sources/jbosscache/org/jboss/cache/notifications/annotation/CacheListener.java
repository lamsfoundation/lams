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
package org.jboss.cache.notifications.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Class-level annotation used to annotate an object as being a valid cache listener.  Used with the
 * {@link org.jboss.cache.Cache#addCacheListener(Object)} and related APIs.
 * <p/>
 * Note that even if a class is annotated with this annotation, it still needs method-level annotation (such as
 * {@link org.jboss.cache.notifications.annotation.CacheStarted}) to actually receive notifications.
 * <p/>
 * Objects annotated with this annotation - listeners - can be attached to a running {@link org.jboss.cache.Cache} so
 * users can be notified of {@link org.jboss.cache.Cache} events.
 * <p/>
 * <p/>
 * There can be multiple methods that are annotated to receive the same event,
 * and a method may receive multiple events by using a super type.
 * </p>
 * <p/>
 * <h4>Delivery Semantics</h4>
 * <p/>
 * An event is delivered immediately after the
 * respective operation, but before the underlying cache call returns. For this
 * reason it is important to keep listener processing logic short-lived. If a
 * long running task needs to be performed, it's recommended to use another
 * thread.
 * </p>
 * <p/>
 * <h4>Transactional Semantics</h4>
 * <p/>
 * Since the event is delivered during the actual cache call, the transactional
 * outcome is not yet known. For this reason, <i>events are always delivered, even
 * if the changes they represent are discarded by their containing transaction</i>.
 * For applications that must only process events that represent changes in a
 * completed transaction, {@link org.jboss.cache.notifications.event.TransactionalEvent#getTransaction()} can be used,
 * along with {@link org.jboss.cache.notifications.event.TransactionCompletedEvent#isSuccessful()} to record events and
 * later process them once the transaction has been successfully committed.
 * Example 4 demonstrates this.
 * </p>
 * <p/>
 * <h4>Threading Semantics</h4>
 * <p/>
 * A listener implementation must be capable of handling concurrent invocations. Local
 * notifications reuse the calling thread; remote notifications reuse the network thread.
 * </p>
 * <p/>
 * Since notifications reuse the calling or network thread, it is important to realise that
 * if your listener implementation blocks or performs a long-running task, the original caller which
 * triggered the cache event may block until the listener callback completes.  It is therefore a good idea to use
 * the listener to be notified of an event but to perform any
 * long running tasks in a separate thread so as not to block the original caller.
 * </p>
 * <p/>
 * In addition, any locks acquired for the operation being performed will still be held for the callback.  This needs to be kept in mind
 * as locks may be held longer than necessary or intended to and may cause deadlocking in certain situations.  See above paragraph
 * on long-running tasks that should be run in a separate thread.
 * </p>
 * <b>Note</b>: Since 3.0, a new parameter, <tt>sync</tt>, has been introduced on this annotation.  This defaults to <tt>true</tt>
 * which provides the above semantics.  Alternatively, if you set <tt>sync</tt> to <tt>false</tt>, then invocations are made in a
 * <i>separate</i> thread, which will not cause any blocking on the caller or network thread.  The separate thread is taken
 * from a pool, which can be configured using {@link org.jboss.cache.config.Configuration#setListenerAsyncPoolSize(int)}.
 * <p/>
 * <b>Summary of Notification Annotations</b>
 * <table border="1" cellpadding="1" cellspacing="1" summary="Summary of notification annotations">
 * <tr>
 * <th bgcolor="#CCCCFF" align="left">Annotation</th>
 * <th bgcolor="#CCCCFF" align="left">Event</th>
 * <th bgcolor="#CCCCFF" align="left">Description</th>
 * </tr>
 * <tr>
 * <td valign="top">{@link CacheStarted}</td>
 * <td valign="top">{@link org.jboss.cache.notifications.event.CacheStartedEvent}</td>
 * <td valign="top">A cache was started</td>
 * </tr>
 * <tr>
 * <td valign="top">{@link CacheStopped}</td>
 * <td valign="top">{@link org.jboss.cache.notifications.event.CacheStoppedEvent}</td>
 * <td valign="top">A cache was stopped</td>
 * </tr>
 * <tr>
 * <td valign="top">{@link NodeModified}</td>
 * <td valign="top">{@link org.jboss.cache.notifications.event.NodeModifiedEvent}</td>
 * <td valign="top">A node was modified</td>
 * </tr>
 * <tr>
 * <td valign="top">{@link NodeMoved}</td>
 * <td valign="top">{@link org.jboss.cache.notifications.event.NodeMovedEvent}</td>
 * <td valign="top">A node was moved</td>
 * </tr>
 * <tr>
 * <td valign="top">{@link NodeCreated}</td>
 * <td valign="top">{@link org.jboss.cache.notifications.event.NodeCreatedEvent}</td>
 * <td valign="top">A node was created</td>
 * </tr>
 * <tr>
 * <td valign="top">{@link NodeRemoved}</td>
 * <td valign="top">{@link org.jboss.cache.notifications.event.NodeRemovedEvent}</td>
 * <td valign="top">A node was removed</td>
 * </tr>
 * <tr>
 * <td valign="top">{@link NodeVisited}</td>
 * <td valign="top">{@link org.jboss.cache.notifications.event.NodeVisitedEvent}</td>
 * <td valign="top">A node was visited</td>
 * </tr>
 * <tr>
 * <td valign="top">{@link NodeLoaded}</td>
 * <td valign="top">{@link org.jboss.cache.notifications.event.NodeLoadedEvent}</td>
 * <td valign="top">A node was loaded</td>
 * </tr>
 * <tr>
 * <td valign="top">{@link org.jboss.cache.notifications.annotation.NodeEvicted}</td>
 * <td valign="top">{@link org.jboss.cache.notifications.event.NodeEvictedEvent}</td>
 * <td valign="top">A node was evicted</td>
 * </tr>
 * <tr>
 * <td valign="top">{@link org.jboss.cache.notifications.annotation.NodeActivated}</td>
 * <td valign="top">{@link org.jboss.cache.notifications.event.NodeActivatedEvent}</td>
 * <td valign="top">A node was activated</td>
 * </tr>
 * <tr>
 * <td valign="top">{@link org.jboss.cache.notifications.annotation.NodePassivated}</td>
 * <td valign="top">{@link org.jboss.cache.notifications.event.NodePassivatedEvent}</td>
 * <td valign="top">A node was passivated</td>
 * </tr>
 * <tr>
 * <td valign="top">{@link org.jboss.cache.notifications.annotation.ViewChanged}</td>
 * <td valign="top">{@link org.jboss.cache.notifications.event.ViewChangedEvent}</td>
 * <td valign="top">A view change event was detected</td>
 * </tr>
 * <tr>
 * <td valign="top">{@link org.jboss.cache.notifications.annotation.CacheBlocked}</td>
 * <td valign="top">{@link org.jboss.cache.notifications.event.CacheBlockedEvent}</td>
 * <td valign="top">A cache block event was detected</td>
 * </tr>
 * <tr>
 * <td valign="top">{@link CacheUnblocked}</td>
 * <td valign="top">{@link org.jboss.cache.notifications.event.CacheUnblockedEvent}</td>
 * <td valign="top">A cache unblock event was detected</td>
 * </tr>
 * <tr>
 * <td valign="top">{@link TransactionRegistered}</td>
 * <td valign@="top">{@link org.jboss.cache.notifications.event.TransactionRegisteredEvent}</td>
 * <td valign="top">The cache has started to participate in a transaction</td>
 * </tr>
 * <tr>
 * <td valign="top">{@link TransactionCompleted}</td>
 * <td valign=@"top">{@link org.jboss.cache.notifications.event.TransactionCompletedEvent}</td>
 * <td valign="top">The cache has completed its participation in a transaction</td>
 * </tr>
 * <tr>
 * <td valign="top">{@link BuddyGroupChanged}</td>
 * <td valign=@"top">{@link org.jboss.cache.notifications.event.BuddyGroupChangedEvent}</td>
 * <td valign="top">Buddy replication is enabled and one of the buddy groups that the instance is a member of has changed its membership.</td>
 * </tr>
 * <tr>
 * <td valign="top">{@link NodeInvalidated}</td>
 * <td valign=@"top">{@link org.jboss.cache.notifications.event.NodeInvalidatedEvent}</td>
 * <td valign="top">A node was invalidated by a remote cache.  Only if cache mode is INVALIDATION_SYNC or INVALIDATION_ASYNC.</td>
 * </tr>
 * <p/>
 * </table>
 * <p/>
 * <h4>Example 1 - Method receiving a single event</h4>
 * <pre>
 *    &#064;CacheListener
 *    public class SingleEventListener
 *    {
 *       &#064;CacheStarted
 *       public void doSomething(Event event)
 *       {
 *          System.out.println(&quot;Cache started.  Details = &quot; + event);
 *       }
 *    }
 * </pre>
 * <p/>
 * <h4>Example 2 - Method receiving multiple events</h4>
 * <pre>
 *    &#064;CacheListener
 *    public class MultipleEventListener
 *    {
 *       &#064;CacheStarted
 *       &#064;CacheStopped
 *       public void doSomething(Event event)
 *       {
 *          if (event.getType() == Event.Type.CACHE_STARTED)
 *             System.out.println(&quot;Cache started.  Details = &quot; + event);
 *          else if (event.getType() == Event.Type.CACHE_STOPPED)
 *             System.out.println(&quot;Cache stopped.  Details = &quot; + event);
 *       }
 *    }
 * </pre>
 * <p/>
 * <h4>Example 3 - Multiple methods receiving the same event</h4>
 * <pre>
 *    &#064;CAcheListener
 *    public class SingleEventListener
 *    {
 *       &#064;CacheStarted
 *       public void handleStart(Event event)
 *       {
 *          System.out.println(&quot;Cache started&quot;);
 *       }
 * <p/>
 *       &#064;CacheStarted
 *       &#064;CacheStopped
 *       &#064;CacheBlocked
 *       &#064;CacheUnblocked
 *       &#064;ViewChanged
 *       public void logEvent(Event event)
 *       {
 *          logSystem.logEvent(event.getType());
 *       }
 *    }
 * </pre>
 * <p/>
 * <p/>
 * <b>Example 4 - Processing only events with a committed transaction.</b>
 * <p/>
 * <pre>
 *    &#064;CacheListener
 *    public class TxGuaranteedListener
 *    {
 *       private class TxEventQueue
 *       {
 *          private ConcurrentMap&lt;Transaction, Queue&lt;Event&gt;&gt; map = new ConcurrentHashMap&lt;Transaction, Queue&lt;Event&gt;&gt;();
 * <p/>
 *          public void offer(Event event)
 *          {
 *             Queue&lt;Event&gt; queue = getQueue(event.getContext().getTransaction());
 *             queue.offer(event);
 *          }
 * <p/>
 *          private Queue&lt;Event&gt; getQueue(Transaction transaction)
 *          {
 *             Queue&lt;Event&gt; queue = map.get(transaction);
 *             if (queue == null)
 *             {
 *                queue = new ConcurrentLinkedQueue&lt;Event&gt;();
 *                map.putIfAbsent(transaction, queue);
 *             }
 * <p/>
 *             return queue;
 *          }
 * <p/>
 *          public Queue&lt;Event&gt; takeAll(Transaction transaction)
 *          {
 *             return map.remove(transaction);
 *          }
 *       }
 * <p/>
 *       private TxEventQueue events = new TxEventQueue();
 * <p/>
 *       &#064;NodeModified
 *       &#064;NodeMoved
 *       &#064;NodeCreated
 *       &#064;NodeRemoved
 *       public void handle(Event event)
 *       {
 *          events.offer(event);
 *       }
 * <p/>
 *       &#064;TransactionCompleted
 *       public void handleTx(TransactionCompletedEvent event)
 *       {
 *          Queue&lt;Event&gt; completed = events.takeAll(event.getTransaction());
 *          if (completed != null &amp;&amp; event.isSuccessful())
 *             System.out.println("Comitted events = " + completed);
 *       }
 *    }
 * </pre>
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani</a>
 * @author Jason T. Greene
 * @see CacheStarted
 * @see CacheStopped
 * @see NodeModified
 * @see NodeMoved
 * @see NodeCreated
 * @see NodeRemoved
 * @see NodeVisited
 * @see NodeLoaded
 * @see NodeEvicted
 * @see NodeActivated
 * @see NodePassivated
 * @see ViewChanged
 * @see CacheBlocked
 * @see CacheUnblocked
 * @see TransactionCompleted
 * @see TransactionRegistered
 * @see BuddyGroupChanged
 * @see NodeInvalidated
 * @see org.jboss.cache.Cache#addCacheListener(Object)
 * @see org.jboss.cache.Cache#removeCacheListener(Object)
 * @see org.jboss.cache.Cache#getCacheListeners()
 * @since 2.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CacheListener
{
   /**
    * Specifies whether callbacks on any class annotated with this annotation happens synchronously (in the caller's thread)
    * or asynchronously (using a separate thread).  Defaults to <tt>true</tt>.
    *
    * @return true if the expectation is that callbacks are called using the caller's thread; false if they are to be made in a separate thread.
    * @since 3.0
    */
   boolean sync() default true;
}
