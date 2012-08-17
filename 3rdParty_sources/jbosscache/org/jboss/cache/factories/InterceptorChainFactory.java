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
package org.jboss.cache.factories;

import org.jboss.cache.config.Configuration;
import org.jboss.cache.config.Configuration.NodeLockingScheme;
import org.jboss.cache.config.ConfigurationException;
import org.jboss.cache.config.CustomInterceptorConfig;
import org.jboss.cache.factories.annotations.DefaultFactoryFor;
import org.jboss.cache.interceptors.*;
import org.jboss.cache.interceptors.base.CommandInterceptor;

import java.util.List;

/**
 * Factory class that builds an interceptor chain based on cache configuration.
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani (manik AT jboss DOT org)</a>
 */
@DefaultFactoryFor(classes = InterceptorChain.class)
public class InterceptorChainFactory extends ComponentFactory
{
   /**
    * Note - this method used to return a singleton instance, and since 2.1.0 returns a new instance.  The method is
    * deprecated and you should use the no-arg constructor to create a new instance of this factory.
    *
    * @return a NEW instance of this class.
    */
   @Deprecated
   public static InterceptorChainFactory getInstance()
   {
      return new InterceptorChainFactory();
   }

   private CommandInterceptor createInterceptor(Class<? extends CommandInterceptor> clazz) throws IllegalAccessException, InstantiationException
   {
      CommandInterceptor chainedInterceptor = componentRegistry.getComponent(clazz);
      if (chainedInterceptor == null)
      {
         chainedInterceptor = clazz.newInstance();
         componentRegistry.registerComponent(chainedInterceptor, clazz);
      }
      else
      {
         // wipe next/last chaining!!
         chainedInterceptor.setNext(null);
      }
      return chainedInterceptor;
   }

   public InterceptorChain buildInterceptorChain() throws IllegalAccessException, InstantiationException, ClassNotFoundException
   {
      boolean optimistic = configuration.getNodeLockingScheme() == NodeLockingScheme.OPTIMISTIC;
      boolean invocationBatching = configuration.isInvocationBatchingEnabled();
      // load the icInterceptor first
      CommandInterceptor first = invocationBatching ? createInterceptor(BatchingInterceptor.class) : createInterceptor(InvocationContextInterceptor.class);
      InterceptorChain interceptorChain = new InterceptorChain(first);

      // add the interceptor chain to the registry first, since some interceptors may ask for it.
      componentRegistry.registerComponent(interceptorChain, InterceptorChain.class);

      // NOW add the ICI if we are using batching!
      if (invocationBatching)
         interceptorChain.appendIntereceptor(createInterceptor(InvocationContextInterceptor.class));

      // load the cache management interceptor next
      if (configuration.getExposeManagementStatistics())
         interceptorChain.appendIntereceptor(createInterceptor(CacheMgmtInterceptor.class));

      // load the tx interceptor
      interceptorChain.appendIntereceptor(createInterceptor(optimistic ? OptimisticTxInterceptor.class : TxInterceptor.class));

      if (configuration.isUseLazyDeserialization())
         interceptorChain.appendIntereceptor(createInterceptor(MarshalledValueInterceptor.class));
      interceptorChain.appendIntereceptor(createInterceptor(NotificationInterceptor.class));

      switch (configuration.getCacheMode())
      {
         case REPL_SYNC:
         case REPL_ASYNC:
            interceptorChain.appendIntereceptor(optimistic ? createInterceptor(OptimisticReplicationInterceptor.class) : createInterceptor(ReplicationInterceptor.class));
            break;
         case INVALIDATION_SYNC:
         case INVALIDATION_ASYNC:
            interceptorChain.appendIntereceptor(createInterceptor(InvalidationInterceptor.class));
            break;
         case LOCAL:
            //Nothing...
      }

      if (configuration.getNodeLockingScheme() == NodeLockingScheme.MVCC)
      {
         // if MVCC, then the CLI or AI must come before the MVCCLI.
         if (configuration.isUsingCacheLoaders())
         {
            if (configuration.getCacheLoaderConfig().isPassivation())
            {
               interceptorChain.appendIntereceptor(createInterceptor(ActivationInterceptor.class));
            }
            else
            {
               interceptorChain.appendIntereceptor(createInterceptor(CacheLoaderInterceptor.class));
            }
         }
         interceptorChain.appendIntereceptor(createInterceptor(MVCCLockingInterceptor.class));
      }
      else if (configuration.getNodeLockingScheme() == NodeLockingScheme.PESSIMISTIC)
      {
         interceptorChain.appendIntereceptor(createInterceptor(PessimisticLockInterceptor.class));
      }

      if (configuration.isUsingCacheLoaders())
      {
         if (configuration.getCacheLoaderConfig().isPassivation())
         {
            if (configuration.getNodeLockingScheme() != NodeLockingScheme.MVCC)
            {
               interceptorChain.appendIntereceptor(createInterceptor(LegacyActivationInterceptor.class));
               interceptorChain.appendIntereceptor(createInterceptor(LegacyPassivationInterceptor.class));
            }
            else
            {
               interceptorChain.appendIntereceptor(createInterceptor(PassivationInterceptor.class));
            }
         }
         else
         {
            if (configuration.getNodeLockingScheme() != NodeLockingScheme.MVCC)
            {
               interceptorChain.appendIntereceptor(createInterceptor(LegacyCacheLoaderInterceptor.class));
               interceptorChain.appendIntereceptor(createInterceptor(LegacyCacheStoreInterceptor.class));
            }
            else
            {
               interceptorChain.appendIntereceptor(createInterceptor(CacheStoreInterceptor.class));
            }
         }
      }

      if (configuration.isUsingBuddyReplication())
      {
         if (configuration.getNodeLockingScheme() == NodeLockingScheme.MVCC)
            interceptorChain.appendIntereceptor(createInterceptor(DataGravitatorInterceptor.class));
         else
            interceptorChain.appendIntereceptor(createInterceptor(LegacyDataGravitatorInterceptor.class));
      }


      if (optimistic)
      {
         interceptorChain.appendIntereceptor(createInterceptor(OptimisticLockingInterceptor.class));
         interceptorChain.appendIntereceptor(createInterceptor(OptimisticValidatorInterceptor.class));
         interceptorChain.appendIntereceptor(createInterceptor(OptimisticCreateIfNotExistsInterceptor.class));
      }
      // eviction interceptor to come before the optimistic node interceptor
      if (configuration.getEvictionConfig() != null && configuration.getEvictionConfig().isValidConfig())
         interceptorChain.appendIntereceptor(createInterceptor(configuration.isUsingBuddyReplication() ? BuddyRegionAwareEvictionInterceptor.class : EvictionInterceptor.class));

      if (optimistic) interceptorChain.appendIntereceptor(createInterceptor(OptimisticNodeInterceptor.class));
      CommandInterceptor callInterceptor = createInterceptor(CallInterceptor.class);
      interceptorChain.appendIntereceptor(callInterceptor);
      if (log.isTraceEnabled()) log.trace("Finished building default interceptor chain.");
      buildCustomInterceptors(interceptorChain, configuration.getCustomInterceptors());
      return interceptorChain;
   }

   private void buildCustomInterceptors(InterceptorChain interceptorChain, List<CustomInterceptorConfig> customInterceptors)
   {
      for (CustomInterceptorConfig config : customInterceptors)
      {
         if (interceptorChain.containsInstance(config.getInterceptor())) continue;
         if (config.isFirst())
         {
            interceptorChain.addInterceptor(config.getInterceptor(), 0);
         }
         if (config.isLast()) interceptorChain.appendIntereceptor(config.getInterceptor());
         if (config.getIndex() >= 0) interceptorChain.addInterceptor(config.getInterceptor(), config.getIndex());
         if (config.getAfterClass() != null)
         {
            List<CommandInterceptor> withClassName = interceptorChain.getInterceptorsWithClassName(config.getAfterClass());
            if (withClassName.isEmpty())
            {
               throw new ConfigurationException("Cannot add after class: " + config.getAfterClass()
                     + " as no such iterceptor exists in the default chain");
            }
            interceptorChain.addAfterInterceptor(config.getInterceptor(), withClassName.get(0).getClass());
         }
         if (config.getBeforeClass() != null)
         {
            List<CommandInterceptor> withClassName = interceptorChain.getInterceptorsWithClassName(config.getBeforeClass());
            if (withClassName.isEmpty())
            {
               throw new ConfigurationException("Cannot add before class: " + config.getAfterClass()
                     + " as no such iterceptor exists in the default chain");
            }
            interceptorChain.addBeforeInterceptor(config.getInterceptor(), withClassName.get(0).getClass());
         }
      }
   }

   @Override
   protected <T> T construct(Class<T> componentType)
   {
      try
      {
         return componentType.cast(buildInterceptorChain());
      }
      catch (Exception e)
      {
         throw new ConfigurationException("Unable to build interceptor chain", e);
      }
   }

   public static InterceptorChainFactory getInstance(ComponentRegistry componentRegistry, Configuration configuration)
   {
      InterceptorChainFactory icf = new InterceptorChainFactory();
      icf.componentRegistry = componentRegistry;
      icf.configuration = configuration;
      return icf;
   }
}
