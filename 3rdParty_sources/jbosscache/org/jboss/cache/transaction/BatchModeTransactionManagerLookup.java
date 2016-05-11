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


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.transaction.TransactionManager;


/**
 * Returns an instance of {@link BatchModeTransactionManager}.
 *
 * @author Bela Ban Sept 5 2003
 *
 * @deprecated Use batching API on Cache instead.
 */
@Deprecated
public class BatchModeTransactionManagerLookup implements TransactionManagerLookup
{
   private Log log = LogFactory.getLog(BatchModeTransactionManagerLookup.class);

   public TransactionManager getTransactionManager() throws Exception
   {
      // (BES 2208/11/2) We decided to use JBC 3 in JBoss AS 5. Therefore 
      // this is not a correct statement and I am removing this logging.
      //log.warn("Using a deprecated/unsupported transaction manager!");
      return BatchModeTransactionManager.getInstance();
   }
}
