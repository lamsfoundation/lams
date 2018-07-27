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

package org.opensaml.ws.soap.client;

import org.opensaml.ws.soap.common.SOAPException;
import org.opensaml.xml.security.SecurityException;

import net.jcip.annotations.ThreadSafe;

/**
 * An interface for a very basic SOAP client.
 * 
 * Implementations of this interface do NOT attempt to do intelligent things like figure out when and how to attach
 * WS-Security headers. It is strictly meant to open sockets, shuttle messages over it, and return a response.
 */
@ThreadSafe
public interface SOAPClient {

    /**
     * Sends a message and waits for a response.
     * 
     * @param endpoint the endpoint to which to send the message
     * @param messageContext the message context containing the outbound SOAP message
     * 
     * @throws SOAPClientException thrown if there is a problem sending the message or receiving the response or if the
     *             response is a SOAP fault
     * @throws SecurityException thrown if the response does not meet any security policy associated with the message
     *             context
     */
    public void send(String endpoint, SOAPMessageContext messageContext) throws SOAPException, SecurityException;

    /** Marker interface for binding/transport request parameters. */
    public interface SOAPRequestParameters {};
}