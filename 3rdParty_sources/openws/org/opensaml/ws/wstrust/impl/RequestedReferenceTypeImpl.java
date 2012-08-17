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

import org.opensaml.ws.wssecurity.SecurityTokenReference;
import org.opensaml.ws.wstrust.RequestedReferenceType;
import org.opensaml.xml.XMLObject;

/**
 * RequestedReferenceTypeImpl.
 * 
 */
public class RequestedReferenceTypeImpl extends AbstractWSTrustObject implements RequestedReferenceType {
    
    /** SecurityTokenReference child element. */
    private SecurityTokenReference securityTokenReference;

    /**
     * Constructor.
     * 
     * @param namespaceURI namespace of the element
     * @param elementLocalName name of the element
     * @param namespacePrefix namespace prefix of the element
     */
    public RequestedReferenceTypeImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }

    /** {@inheritDoc} */
    public SecurityTokenReference getSecurityTokenReference() {
        return securityTokenReference;
    }

    /** {@inheritDoc} */
    public void setSecurityTokenReference(SecurityTokenReference newSecurityTokenReference) {
        securityTokenReference = prepareForAssignment(securityTokenReference, newSecurityTokenReference);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        List<XMLObject> children = new ArrayList<XMLObject>();
        if (securityTokenReference != null) {
            children.add(securityTokenReference);
        }
        return Collections.unmodifiableList(children);
    }

}
