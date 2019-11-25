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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* TransactionRetryInterceptor.java,v 1.1 2015/07/22 08:00:18 marcin Exp */
package org.lamsfoundation.lams.web.filter;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.mutable.MutableInt;
import org.apache.log4j.Logger;
import org.hibernate.PessimisticLockException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.LockAcquisitionException;
import org.lamsfoundation.lams.util.ITransactionRetryService;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.UnexpectedRollbackException;

/**
 * Retries proxied method in case of an exception. First attempt is processed as usual. Retrying requires a new
 * transaction, which is done by the service.
 *
 * @author Marcin Cieslak
 *
 */
public class TransactionRetryInterceptor implements MethodInterceptor {
    private static final Logger log = Logger.getLogger(TransactionRetryInterceptor.class);

    private ITransactionRetryService transactionRetryService;

    private static final int MAX_ATTEMPTS = 5;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
	MutableInt attempt = new MutableInt(1);
	Throwable exception = null;
	do {
	    try {
		if (attempt.intValue() == 1) {
		    return invocation.proceed();
		} else {
		    return transactionRetryService.retry(invocation);
		}
	    } catch (DataIntegrityViolationException e) {
		exception = e;
		if (exception.getCause() instanceof ConstraintViolationException) {
		    TransactionRetryInterceptor.log.error("Schema error", exception);
		}
		processException(e, invocation, attempt);
	    } catch (ConstraintViolationException e) {
		exception = e;
		TransactionRetryInterceptor.log.error("Schema error", exception);
		processException(e, invocation, attempt);
	    } catch (CannotAcquireLockException | LockAcquisitionException | UnexpectedRollbackException
		    | PessimisticLockException e) {
		exception = e;
		processException(e, invocation, attempt);
	    }
	} while (attempt.intValue() <= TransactionRetryInterceptor.MAX_ATTEMPTS);
	throw exception;
    }

    private void processException(Exception e, MethodInvocation invocation, MutableInt attempt) {

	StringBuilder message = new StringBuilder("When invoking method ")
		.append(invocation.getMethod().getDeclaringClass().getName()).append("#")
		.append(invocation.getMethod().getName()).append(" caught \"").append(e.getMessage())
		.append("\". Attempt #").append(attempt);

	attempt.increment();
	if (attempt.intValue() <= TransactionRetryInterceptor.MAX_ATTEMPTS) {
	    message.append(". Retrying.");
	} else {
	    message.append(". Giving up.");
	}
	TransactionRetryInterceptor.log.warn(message);
    }

    public void setTransactionRetryService(ITransactionRetryService transactionRetryService) {
	this.transactionRetryService = transactionRetryService;
    }
}