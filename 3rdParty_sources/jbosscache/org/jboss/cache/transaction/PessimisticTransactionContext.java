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
package org.jboss.cache.transaction;


import org.jboss.cache.commands.WriteCommand;

import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Information associated with a {@link GlobalTransaction} about the transaction state.
 * <p/>
 * A TransactionContext maintains:
 * <ul>
 * <li>Handle to the local Transaction</li>
 * <li>List of {@link org.jboss.cache.commands.WriteCommand}s that make up this transaction</li>
 * <li>List of locks acquired</li>
 * <li>Any transaction-scope options</li>
 * </ul>
 *
 * @author <a href="mailto:bela@jboss.org">Bela Ban</a>
 * @author Manik Surtani
 * @version $Revision$
 * @deprecated will be removed along with optimistic and pessimistic locking.
 */
@Deprecated
public class PessimisticTransactionContext extends AbstractTransactionContext
{
   private List<WriteCommand> allModifications;

   public PessimisticTransactionContext(Transaction tx) throws SystemException, RollbackException
   {
      super(tx);
   }

   @Override
   public void addLocalModification(WriteCommand command)
   {
      if (command == null) return;
      super.addLocalModification(command);
      if (allModifications == null) allModifications = new LinkedList<WriteCommand>();
      allModifications.add(command);
   }

   @Override
   public void addModification(WriteCommand command)
   {
      if (command == null) return;
      super.addModification(command);
      if (allModifications == null) allModifications = new LinkedList<WriteCommand>();
      allModifications.add(command);
   }

   public List<WriteCommand> getAllModifications()
   {
      if (allModifications == null) return Collections.emptyList();
      return allModifications;
   }

   @Override
   public void reset()
   {
      super.reset();
      allModifications = null;
   }
}
