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

package org.opensaml.ws.wsaddressing;

import javax.xml.namespace.QName;

/**
 * WS-Addressing 1.0 constants.
 * 
 * @see "WS-Addressing 1.0 - Core"
 * 
 */
public class WSAddressingConstants {
    
    /** WS-Addressing 1.0 namespace. */
    public static final String WSA_NS= "http://www.w3.org/2005/08/addressing";

    /** WS-Addressing prefix. */
    public static final String WSA_PREFIX= "wsa";
    
    // SOAP fault codes
    
    /** WS-Addressing SOAP fault code: "wsa:InvalidAddressingHeader". */
    public static final QName SOAP_FAULT_INVALID_ADDRESSING_HEADER =
        new QName(WSA_NS, "InvalidAddressingHeader", WSA_PREFIX);
    
    /** WS-Addressing SOAP fault code: "wsa:InvalidAddress". */
    public static final QName SOAP_FAULT_INVALID_ADDRESS =
        new QName(WSA_NS, "InvalidAddress", WSA_PREFIX);
    
    /** WS-Addressing SOAP fault code: "wsa:InvalidEPR". */
    public static final QName SOAP_FAULT_INVALID_EPR =
        new QName(WSA_NS, "InvalidEPR", WSA_PREFIX);
    
    /** WS-Addressing SOAP fault code: "wsa:InvalidCardinality". */
    public static final QName SOAP_FAULT_INVALID_CARDINALITY =
        new QName(WSA_NS, "InvalidCardinality", WSA_PREFIX);
    
    /** WS-Addressing SOAP fault code: "wsa:MissingAddressInEPR". */
    public static final QName SOAP_FAULT_MISSING_ADDRESS_IN_EPR =
        new QName(WSA_NS, "MissingAddressInEPR", WSA_PREFIX);
    
    /** WS-Addressing SOAP fault code: "wsa:DuplicateMessageID". */
    public static final QName SOAP_FAULT_DUPLICATE_MESSAGE_ID =
        new QName(WSA_NS, "DuplicateMessageID", WSA_PREFIX);
    
    /** WS-Addressing SOAP fault code: "wsa:ActionMismatch". */
    public static final QName SOAP_FAULT_ACTION_MISMATCH =
        new QName(WSA_NS, "ActionMismatch", WSA_PREFIX);
    
    /** WS-Addressing SOAP fault code: "wsa:MessageAddressingHeaderRequired". */
    public static final QName SOAP_FAULT_MESSAGE_ADDRESSING_HEADER_REQUIRED =
        new QName(WSA_NS, "MessageAddressingHeaderRequired", WSA_PREFIX);
    
    /** WS-Addressing SOAP fault code: "wsa:DestinationUnreachable". */
    public static final QName SOAP_FAULT_DESTINATION_UNREACHABLE =
        new QName(WSA_NS, "DestinationUnreachable", WSA_PREFIX);
    
    /** WS-Addressing SOAP fault code: "wsa:ActionNotSupported". */
    public static final QName SOAP_FAULT_ACTION_NOT_SUPPORTED =
        new QName(WSA_NS, "ActionNotSupported", WSA_PREFIX);
    
    /** WS-Addressing SOAP fault code: "wsa:EndpointUnavailable". */
    public static final QName SOAP_FAULT_ENDPOINT_UNAVAILABLE =
        new QName(WSA_NS, "EndpointUnavailable", WSA_PREFIX);
    
    /** Prevent instantiation. */
    private WSAddressingConstants() { }

}
