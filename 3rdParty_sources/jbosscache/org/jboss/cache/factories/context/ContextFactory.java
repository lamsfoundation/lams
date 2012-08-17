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
package org.jboss.cache.factories.context;

import org.jboss.cache.InvocationContext;
import org.jboss.cache.transaction.TransactionContext;

import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

/**
 * This is the factory responsible for creating {@link org.jboss.cache.InvocationContext}s and {@link org.jboss.cache.transaction.TransactionContext}s
 * for requests, based on the configuration used.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 3.0
 */
public interface ContextFactory
{
   /**
    * @return a new invocation context
    */
   InvocationContext createInvocationContext();

   /**
    * @param tx JTA transaction to associate the new context with
    * @return a new transaction context
    * @throws javax.transaction.RollbackException
    *          in the event of an invalid transaaction
    * @throws javax.transaction.SystemException
    *          in the event of an invalid transaction
    */
   TransactionContext createTransactionContext(Transaction tx) throws SystemException, RollbackException;
}
