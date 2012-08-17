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

package org.opensaml.ws.wsfed.impl;

import org.opensaml.ws.wsfed.RequestedSecurityToken;
import org.opensaml.ws.wsfed.WSFedConstants;
import org.opensaml.ws.wsfed.WSFedObjectBuilder;
import org.opensaml.xml.AbstractXMLObjectBuilder;

/** Builder of {@link RequestedSecurityTokenImpl} objects. */
public class RequestedSecurityTokenBuilder extends AbstractXMLObjectBuilder<RequestedSecurityToken> implements
        WSFedObjectBuilder<RequestedSecurityToken> {

    /** Constructor. */
    public RequestedSecurityTokenBuilder() {

    }

    /** {@inheritDoc} */
    public RequestedSecurityToken buildObject() {
        return buildObject(WSFedConstants.WSFED11P_NS, RequestedSecurityToken.DEFAULT_ELEMENT_LOCAL_NAME,
                WSFedConstants.WSFED1P_PREFIX);
    }

    /** {@inheritDoc} */
    public RequestedSecurityToken buildObject(String namespaceURI, String localName, String namespacePrefix) {
        return new RequestedSecurityTokenImpl(namespaceURI, localName, namespacePrefix);
    }
}