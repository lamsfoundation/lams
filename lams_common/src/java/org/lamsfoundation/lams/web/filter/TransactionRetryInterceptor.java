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
import org.apache.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * Retries proxied method in case of an exception. It should kick in after Hibernate session gets created.
 * 
 * @author Marcin Cieslak
 *
 */
public class TransactionRetryInterceptor implements MethodInterceptor {
    
    private static final Logger log = Logger.getLogger(TransactionRetryInterceptor.class);

    private static final int MAX_ATTEMPTS = 3;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
	int attempt = 1;
	Throwable exception = null;
	do {
	    try {
		return invocation.proceed();
	    } catch (DataIntegrityViolationException e) {
		exception = e;
		StringBuilder message = new StringBuilder("When invoking method \"")
			.append(invocation.getMethod().getName()).append("\" caught ").append(e.getMessage())
			.append(". Attempt #").append(attempt);
		attempt++;
		if (attempt <= TransactionRetryInterceptor.MAX_ATTEMPTS) {
		    message.append(". Retrying.");
		} else {
		    message.append(". Giving up.");
		}
		TransactionRetryInterceptor.log.warn(message);
	    }
	} while (attempt <= TransactionRetryInterceptor.MAX_ATTEMPTS);
	throw exception;
    }
}