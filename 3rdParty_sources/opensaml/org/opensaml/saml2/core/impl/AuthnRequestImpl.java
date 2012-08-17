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

import org.opensaml.saml2.core.AuthnRequest;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.NameIDPolicy;
import org.opensaml.saml2.core.RequestedAuthnContext;
import org.opensaml.saml2.core.Scoping;
import org.opensaml.saml2.core.Subject;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.schema.XSBooleanValue;

/**
 * A concrete implementation of {@link org.opensaml.saml2.core.AuthnRequest}.
 */
public class AuthnRequestImpl extends RequestAbstractTypeImpl implements AuthnRequest {

    /** Subject child element. */
    private Subject subject;

    /** NameIDPolicy child element. */
    private NameIDPolicy nameIDPolicy;

    /** Conditions child element. */
    private Conditions conditions;

    /** RequestedAuthnContext child element. */
    private RequestedAuthnContext requestedAuthnContext;

    /** Scoping child element. */
    private Scoping scoping;

    /** ForeceAuthn attribute. */
    private XSBooleanValue forceAuthn;

    /** IsPassive attribute. */
    private XSBooleanValue isPassive;

    /** ProtocolBinding attribute. */
    private String protocolBinding;

    /** AssertionConsumerServiceIndex attribute. */
    private Integer assertionConsumerServiceIndex;

    /** AssertionConsumerServiceURL attribute. */
    private String assertionConsumerServiceURL;

    /** AttributeConsumingServiceIndex attribute. */
    private Integer attributeConsumingServiceIndex;

    /** ProviderName attribute. */
    private String providerName;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected AuthnRequestImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }

    /** {@inheritDoc} */
    public Boolean isForceAuthn() {
        if (forceAuthn != null) {
            return forceAuthn.getValue();
        }

        return Boolean.FALSE;
    }

    /** {@inheritDoc} */
    public XSBooleanValue isForceAuthnXSBoolean() {
        return forceAuthn;
    }

    /** {@inheritDoc} */
    public void setForceAuthn(Boolean newForceAuth) {
        if (newForceAuth != null) {
            forceAuthn = prepareForAssignment(forceAuthn, new XSBooleanValue(newForceAuth, false));
        } else {
            forceAuthn = prepareForAssignment(forceAuthn, null);
        }
    }

    /** {@inheritDoc} */
    public void setForceAuthn(XSBooleanValue newForceAuthn) {
        forceAuthn = prepareForAssignment(this.forceAuthn, newForceAuthn);
    }

    /** {@inheritDoc} */
    public Boolean isPassive() {
        if (isPassive != null) {
            return isPassive.getValue();
        }

        return Boolean.FALSE;
    }

    /** {@inheritDoc} */
    public XSBooleanValue isPassiveXSBoolean() {
        return isPassive;
    }

    /** {@inheritDoc} */
    public void setIsPassive(Boolean newIsPassive) {
        if (newIsPassive != null) {
            isPassive = prepareForAssignment(isPassive, new XSBooleanValue(newIsPassive, false));
        } else {
            isPassive = prepareForAssignment(isPassive, null);
        }
    }

    /** {@inheritDoc} */
    public void setIsPassive(XSBooleanValue newIsPassive) {
        this.isPassive = prepareForAssignment(this.isPassive, newIsPassive);
    }

    /** {@inheritDoc} */
    public String getProtocolBinding() {
        return this.protocolBinding;
    }

    /** {@inheritDoc} */
    public void setProtocolBinding(String newProtocolBinding) {
        this.protocolBinding = prepareForAssignment(this.protocolBinding, newProtocolBinding);
    }

    /** {@inheritDoc} */
    public Integer getAssertionConsumerServiceIndex() {
        return assertionConsumerServiceIndex;
    }

    /** {@inheritDoc} */
    public void setAssertionConsumerServiceIndex(Integer newAssertionConsumerServiceIndex) {
        this.assertionConsumerServiceIndex = prepareForAssignment(this.assertionConsumerServiceIndex,
                newAssertionConsumerServiceIndex);
    }

    /** {@inheritDoc} */
    public String getAssertionConsumerServiceURL() {
        return this.assertionConsumerServiceURL;
    }

    /** {@inheritDoc} */
    public void setAssertionConsumerServiceURL(String newAssertionConsumerServiceURL) {
        this.assertionConsumerServiceURL = prepareForAssignment(this.assertionConsumerServiceURL,
                newAssertionConsumerServiceURL);
    }

    /** {@inheritDoc} */
    public Integer getAttributeConsumingServiceIndex() {
        return this.attributeConsumingServiceIndex;
    }

    /** {@inheritDoc} */
    public void setAttributeConsumingServiceIndex(Integer newAttributeConsumingServiceIndex) {
        this.attributeConsumingServiceIndex = prepareForAssignment(this.attributeConsumingServiceIndex,
                newAttributeConsumingServiceIndex);
    }

    /** {@inheritDoc} */
    public String getProviderName() {
        return this.providerName;
    }

    /** {@inheritDoc} */
    public void setProviderName(String newProviderName) {
        this.providerName = prepareForAssignment(this.providerName, newProviderName);
    }

    /** {@inheritDoc} */
    public Subject getSubject() {
        return this.subject;
    }

    /** {@inheritDoc} */
    public void setSubject(Subject newSubject) {
        this.subject = prepareForAssignment(this.subject, newSubject);
    }

    /** {@inheritDoc} */
    public NameIDPolicy getNameIDPolicy() {
        return this.nameIDPolicy;
    }

    /** {@inheritDoc} */
    public void setNameIDPolicy(NameIDPolicy newNameIDPolicy) {
        this.nameIDPolicy = prepareForAssignment(this.nameIDPolicy, newNameIDPolicy);
    }

    /** {@inheritDoc} */
    public Conditions getConditions() {
        return this.conditions;
    }

    /** {@inheritDoc} */
    public void setConditions(Conditions newConditions) {
        this.conditions = prepareForAssignment(this.conditions, newConditions);
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
    public Scoping getScoping() {
        return this.scoping;
    }

    /** {@inheritDoc} */
    public void setScoping(Scoping newScoping) {
        this.scoping = prepareForAssignment(this.scoping, newScoping);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();

        if (super.getOrderedChildren() != null) {
            children.addAll(super.getOrderedChildren());
        }

        if (subject != null) {
            children.add(subject);
        }

        if (nameIDPolicy != null) {
            children.add(nameIDPolicy);
        }

        if (conditions != null) {
            children.add(conditions);
        }

        if (requestedAuthnContext != null) {
            children.add(requestedAuthnContext);
        }

        if (scoping != null) {
            children.add(scoping);
        }

        if (children.size() == 0) {
            return null;
        }

        return Collections.unmodifiableList(children);
    }
}