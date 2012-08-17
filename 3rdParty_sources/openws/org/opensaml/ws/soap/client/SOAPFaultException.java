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
import org.opensaml.ws.soap.soap11.Fault;

/** Exception indicating a SOAP fault. */
public class SOAPFaultException extends SOAPException {

    /** Serial version UID. */
    private static final long serialVersionUID = 4770411452264097320L;

    /** The fault that caused this exception. */
    private Fault soapFault;

    /** Constructor. */
    public SOAPFaultException() {
        super();
    }

    /**
     * Constructor.
     * 
     * @param message exception message
     */
    public SOAPFaultException(String message) {
        super(message);
    }

    /**
     * Constructor.
     * 
     * @param wrappedException exception to be wrapped by this one
     */
    public SOAPFaultException(Exception wrappedException) {
        super(wrappedException);
    }

    /**
     * Constructor.
     * 
     * @param message exception message
     * @param wrappedException exception to be wrapped by this one
     */
    public SOAPFaultException(String message, Exception wrappedException) {
        super(message, wrappedException);
    }

    /**
     * Gets the fault that caused the exception.
     * 
     * @return fault that caused the exception
     */
    public Fault getFault() {
        return soapFault;
    }

    /**
     * Sets the fault that caused the exception.
     * 
     * @param fault fault that caused the exception
     */
    public void setFault(Fault fault) {
        soapFault = fault;
    }
}