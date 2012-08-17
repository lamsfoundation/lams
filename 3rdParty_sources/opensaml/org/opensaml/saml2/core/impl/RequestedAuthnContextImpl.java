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

import org.opensaml.common.impl.AbstractSAMLObject;
import org.opensaml.saml2.core.AuthnContextClassRef;
import org.opensaml.saml2.core.AuthnContextComparisonTypeEnumeration;
import org.opensaml.saml2.core.AuthnContextDeclRef;
import org.opensaml.saml2.core.RequestedAuthnContext;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.XMLObjectChildrenList;

/**
 * Concrete implementation of {@link org.opensaml.saml2.core.RequestedAuthnContext}.
 */
public class RequestedAuthnContextImpl extends AbstractSAMLObject implements RequestedAuthnContext {

    /** AuthnContextClassRef child elements. */
    private final XMLObjectChildrenList<AuthnContextClassRef> authnContextClassRefs;

    /** AuthnContextDeclRef child elements. */
    private final XMLObjectChildrenList<AuthnContextDeclRef> authnContextDeclRefs;

    /** Comparison attribute. */
    private AuthnContextComparisonTypeEnumeration comparison;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected RequestedAuthnContextImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        authnContextClassRefs = new XMLObjectChildrenList<AuthnContextClassRef>(this);
        authnContextDeclRefs = new XMLObjectChildrenList<AuthnContextDeclRef>(this);
    }

    /** {@inheritDoc} */
    public AuthnContextComparisonTypeEnumeration getComparison() {
        return this.comparison;
    }

    /** {@inheritDoc} */
    public void setComparison(AuthnContextComparisonTypeEnumeration newComparison) {
        this.comparison = prepareForAssignment(this.comparison, newComparison);
    }

    /** {@inheritDoc} */
    public List<AuthnContextClassRef> getAuthnContextClassRefs() {
        return this.authnContextClassRefs;
    }

    /** {@inheritDoc} */
    public List<AuthnContextDeclRef> getAuthnContextDeclRefs() {
        return this.authnContextDeclRefs;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();

        children.addAll(authnContextClassRefs);
        children.addAll(authnContextDeclRefs);

        if (children.size() == 0) {
            return null;
        }

        return Collections.unmodifiableList(children);
    }
}