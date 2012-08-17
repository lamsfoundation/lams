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

/**
 * 
 */

package org.opensaml.saml2.core.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opensaml.saml2.core.AuthnQuery;
import org.opensaml.saml2.core.RequestedAuthnContext;
import org.opensaml.xml.XMLObject;

/**
 * Concrete implementation of {@link org.opensaml.saml2.core.AuthnQuery}.
 */
public class AuthnQueryImpl extends SubjectQueryImpl implements AuthnQuery {

    /** SessionIndex attribute. */
    private String sessionIndex;

    /** RequestedAuthnContext child element. */
    private RequestedAuthnContext requestedAuthnContext;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected AuthnQueryImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }

    /** {@inheritDoc} */
    public String getSessionIndex() {
        return this.sessionIndex;
    }

    /** {@inheritDoc} */
    public void setSessionIndex(String newSessionIndex) {
        this.sessionIndex = prepareForAssignment(this.sessionIndex, newSessionIndex);
    }

    /** {@inheritDoc} */
    public RequestedAuthnContext getRequestedAuthnContext() {
        return this.requestedAuthnContext;
    }

    /** {@inheritDoc} */
    public void setRequestedAuthnContext(RequestedAuthnContext newRequestedAuthnContext) {
        this.requestedAuthnContext = prepareForAssignment(this.requestedAuthnContext, newRequestedAuthnContext);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();

        if (super.getOrderedChildren() != null) {
            children.addAll(super.getOrderedChildren());
        }

        if (requestedAuthnContext != null) {
            children.add(requestedAuthnContext);
        }

        if (children.size() == 0) {
            return null;
        }

        return Collections.unmodifiableList(children);
    }
}