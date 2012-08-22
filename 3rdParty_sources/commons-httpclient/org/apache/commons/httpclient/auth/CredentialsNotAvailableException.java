/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 *  Copyright 2002-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.commons.httpclient.auth;

/**
 * Authentication credentials required to respond to a authentication 
 * challenge are not available
 *
 * @author <a href="mailto:oleg@ural.ru">Oleg Kalnichevski</a>
 * 
 * @since 3.0
 */
public class CredentialsNotAvailableException extends AuthenticationException {
    /**
     * Creates a new CredentialsNotAvailableException with a <tt>null</tt> detail message. 
     */
    public CredentialsNotAvailableException() {
        super();
    }

    /**
     * Creates a new CredentialsNotAvailableException with the specified message.
     * 
     * @param message the exception detail message
     */
    public CredentialsNotAvailableException(String message) {
        super(message);
    }

    /**
     * Creates a new CredentialsNotAvailableException with the specified detail message and cause.
     * 
     * @param message the exception detail message
     * @param cause the <tt>Throwable</tt> that caused this exception, or <tt>null</tt>
     * if the cause is unavailable, unknown, or not a <tt>Throwable</tt>
     */
    public CredentialsNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
