/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
package org.lamsfoundation.lams.util;

import org.aopalliance.intercept.MethodInvocation;
import org.lamsfoundation.lams.util.hibernate.HibernateSessionManager;

/**
 * Invokes the given service method with a new transaction.
 */
public class TransactionRetryService implements ITransactionRetryService {
    @Override
    public Object retry(MethodInvocation invocation) throws Throwable {
	// LDEV-4915 introduced it

	// This method uses PROPAGATION_REQUIRES_NEW,
	// but if first attempt of running the target method failed then we are left without a Hibernate session
	// and we need to open it manually.

	// Hopefully this can be rewritten back to automatic session management at some point.
	HibernateSessionManager.openSessionIfNecessary();
	
	return invocation.proceed();
    }
}