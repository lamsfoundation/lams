/*
 * Licensed to the University Corporation for Advanced Internet Development, 
 * Inc. (UCAID) under one or more contributor license agreements.  See the 
 * NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The UCAID licenses this file to You under the Apache 
 * License, Version 2.0 (the "License"); you may not use this file except in 
 * compliance with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensaml.ws.message.decoder;

import org.opensaml.ws.message.MessageException;

/**
 * Exception thrown when a problem occurs decoding a message from an input transport.
 */
public class MessageDecodingException extends MessageException {

    /** Serial version UID. */
    private static final long serialVersionUID = 8151752605618631906L;

    /**
     * Constructor.
     */
    public MessageDecodingException() {
        super();
    }

    /**
     * Constructor.
     * 
     * @param message exception message
     */
    public MessageDecodingException(String message) {
        super(message);
    }

    /**
     * Constructor.
     * 
     * @param wrappedException exception to be wrapped by this one
     */
    public MessageDecodingException(Exception wrappedException) {
        super(wrappedException);
    }

    /**
     * Constructor.
     * 
     * @param message exception message
     * @param wrappedException exception to be wrapped by this one
     */
    public MessageDecodingException(String message, Exception wrappedException) {
        super(message, wrappedException);
    }
}