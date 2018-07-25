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

package org.opensaml.ws.wstrust.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opensaml.ws.wstrust.RequestSecurityTokenResponse;
import org.opensaml.ws.wstrust.RequestSecurityTokenResponseCollection;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.AttributeMap;

/**
 * RequestSecurityTokenResponseCollectionImpl.
 * 
 */
public class RequestSecurityTokenResponseCollectionImpl extends AbstractWSTrustObject implements
        RequestSecurityTokenResponseCollection {

    /** Wildcard attributes. */
    private AttributeMap unknownAttributes;

    /** The list of wst:RequestSecurityTokenResponse child elements. */
    private List<RequestSecurityTokenResponse> requestSecurityTokenResponses;

    /**
     * Constructor.
     * 
     * @param namespaceURI The namespace of the element
     * @param elementLocalName The local name of the element
     * @param namespacePrefix The namespace prefix of the element
     * 
     */
    public RequestSecurityTokenResponseCollectionImpl(String namespaceURI, String elementLocalName,
            String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        unknownAttributes = new AttributeMap(this);
        requestSecurityTokenResponses = new ArrayList<RequestSecurityTokenResponse>();
    }
    
    /** {@inheritDoc} */
    public AttributeMap getUnknownAttributes() {
        return unknownAttributes;
    }

    /** {@inheritDoc} */
    public List<RequestSecurityTokenResponse> getRequestSecurityTokenResponses() {
        return requestSecurityTokenResponses;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        List<XMLObject> children = new ArrayList<XMLObject>();
        children.addAll(requestSecurityTokenResponses);
        return Collections.unmodifiableList(children);
    }

}
