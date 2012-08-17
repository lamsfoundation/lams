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
package org.jboss.cache.lock;

import org.jboss.cache.config.Configuration;
import org.jboss.cache.config.Configuration.NodeLockingScheme;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.factories.annotations.NonVolatile;
import org.jboss.cache.factories.annotations.Start;


/**
 * Factory to create LockStragtegy instance.
 *
 * @author Ben Wang
 */
@NonVolatile
public class LockStrategyFactory
{

   /**
    * Transaction locking isolation level. Default.
    */
   private IsolationLevel lockingLevel = IsolationLevel.REPEATABLE_READ;
   private Configuration configuration;

   @Inject
   public void injectDependencies(Configuration configuration)
   {
      this.configuration = configuration;
   }

   @Start(priority = 1)
   public void start()
   {
      lockingLevel = configuration.getNodeLockingScheme() == NodeLockingScheme.OPTIMISTIC ? IsolationLevel.REPEATABLE_READ : configuration.getIsolationLevel();
   }

   public LockStrategy getLockStrategy()
   {
      return getLockStrategy(lockingLevel);
   }

   public LockStrategy getLockStrategy(IsolationLevel lockingLevel)
   {
      //if(log_.isTraceEnabled()) {
      // log_.trace("LockStrategy is: " + lockingLevel);
      //}
      if (lockingLevel == null)
         return new LockStrategyNone();
      switch (lockingLevel)
      {
         case NONE:
            return new LockStrategyNone();
         case SERIALIZABLE:
            return new LockStrategySerializable();
         case READ_UNCOMMITTED:
            return new LockStrategyReadUncommitted();
         case READ_COMMITTED:
            return new LockStrategyReadCommitted();
         case REPEATABLE_READ:
         default:
            return new LockStrategyRepeatableRead();
      }
   }

   public void setIsolationLevel(IsolationLevel level)
   {
      lockingLevel = level;
   }
}
