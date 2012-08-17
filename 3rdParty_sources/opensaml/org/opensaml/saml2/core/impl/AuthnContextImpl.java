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
import org.opensaml.saml2.core.AuthenticatingAuthority;
import org.opensaml.saml2.core.AuthnContext;
import org.opensaml.saml2.core.AuthnContextClassRef;
import org.opensaml.saml2.core.AuthnContextDecl;
import org.opensaml.saml2.core.AuthnContextDeclRef;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.XMLObjectChildrenList;

/**
 * A concrete implemenation of {@link org.opensaml.saml2.core.AuthnContext}.
 */
public class AuthnContextImpl extends AbstractSAMLObject implements AuthnContext {

    /** URI of the Context Class. */
    private AuthnContextClassRef authnContextClassRef;

    /** Declaration of the Authentication Context. */
    private AuthnContextDecl authnContextDecl;

    /** URI of the Declaration of the Authentication Context. */
    private AuthnContextDeclRef authnContextDeclRef;

    /** List of the Authenticating Authorities. */
    private final XMLObjectChildrenList<AuthenticatingAuthority> authenticatingAuthority;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected AuthnContextImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        authenticatingAuthority = new XMLObjectChildrenList<AuthenticatingAuthority>(this);
    }

    /** {@inheritDoc} */
    public AuthnContextClassRef getAuthnContextClassRef() {
        return authnContextClassRef;
    }

    /** {@inheritDoc} */
    public void setAuthnContextClassRef(AuthnContextClassRef newAuthnContextClassRef) {
        this.authnContextClassRef = prepareForAssignment(this.authnContextClassRef, newAuthnContextClassRef);
    }

    /** {@inheritDoc} */
    public AuthnContextDecl getAuthContextDecl() {
        return authnContextDecl;
    }

    /** {@inheritDoc} */
    public void setAuthnContextDecl(AuthnContextDecl newAuthnContextDecl) {
        this.authnContextDecl = prepareForAssignment(this.authnContextDecl, newAuthnContextDecl);
    }

    /** {@inheritDoc} */
    public AuthnContextDeclRef getAuthnContextDeclRef() {
        return authnContextDeclRef;
    }

    /** {@inheritDoc} */
    public void setAuthnContextDeclRef(AuthnContextDeclRef newAuthnContextDeclRef) {
        this.authnContextDeclRef = prepareForAssignment(this.authnContextDeclRef, newAuthnContextDeclRef);
    }

    /** {@inheritDoc} */
    public List<AuthenticatingAuthority> getAuthenticatingAuthorities() {
        return authenticatingAuthority;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();

        children.add(authnContextClassRef);
        children.add(authnContextDecl);
        children.add(authnContextDeclRef);
        children.addAll(authenticatingAuthority);

        return Collections.unmodifiableList(children);
    }
}