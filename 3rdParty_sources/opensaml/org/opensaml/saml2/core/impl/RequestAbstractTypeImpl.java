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

import org.joda.time.DateTime;
import org.opensaml.common.SAMLVersion;
import org.opensaml.common.impl.AbstractSignableSAMLObject;
import org.opensaml.saml2.common.Extensions;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.RequestAbstractType;
import org.opensaml.xml.XMLObject;

/**
 * Concrete implementation of {@link org.opensaml.saml2.core.RequestAbstractType}.
 */
public abstract class RequestAbstractTypeImpl extends AbstractSignableSAMLObject implements RequestAbstractType {

    /** SAML Version of the request. */
    private SAMLVersion version;

    /** Unique identifier of the request. */
    private String id;

    /** Date/time request was issued. */
    private DateTime issueInstant;

    /** URI of the request destination. */
    private String destination;

    /** URI of the SAML user consent type. */
    private String consent;

    /** URI of the SAML user consent type. */
    private Issuer issuer;

    /** Extensions child element. */
    private Extensions extensions;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected RequestAbstractTypeImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        version = SAMLVersion.VERSION_20;
    }

    /** {@inheritDoc} */
    public SAMLVersion getVersion() {
        return version;
    }

    /** {@inheritDoc} */
    public void setVersion(SAMLVersion newVersion) {
        this.version = prepareForAssignment(this.version, newVersion);
    }

    /** {@inheritDoc} */
    public String getID() {
        return id;
    }

    /** {@inheritDoc} */
    public void setID(String newID) {
        String oldID = this.id;
        this.id = prepareForAssignment(this.id, newID);
        registerOwnID(oldID, this.id);
    }

    /** {@inheritDoc} */
    public DateTime getIssueInstant() {
        return issueInstant;
    }

    /** {@inheritDoc} */
    public void setIssueInstant(DateTime newIssueInstant) {
        this.issueInstant = prepareForAssignment(this.issueInstant, newIssueInstant);
    }

    /** {@inheritDoc} */
    public String getDestination() {
        return destination;
    }

    /** {@inheritDoc} */
    public void setDestination(String newDestination) {
        this.destination = prepareForAssignment(this.destination, newDestination);
    }

    /** {@inheritDoc} */
    public String getConsent() {
        return consent;
    }

    /** {@inheritDoc} */
    public void setConsent(String newConsent) {
        this.consent = prepareForAssignment(this.consent, newConsent);
    }

    /** {@inheritDoc} */
    public Issuer getIssuer() {
        return issuer;
    }

    /** {@inheritDoc} */
    public void setIssuer(Issuer newIssuer) {
        this.issuer = prepareForAssignment(this.issuer, newIssuer);
    }

    /** {@inheritDoc} */
    public Extensions getExtensions() {
        return this.extensions;
    }

    /** {@inheritDoc} */
    public void setExtensions(Extensions newExtensions) {
        this.extensions = prepareForAssignment(this.extensions, newExtensions);
    }

    /** {@inheritDoc} */
    public String getSignatureReferenceID() {
        return id;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();

        if (issuer != null) {
            children.add(issuer);
        }
        if (getSignature() != null) {
            children.add(getSignature());
        }
        if (extensions != null) {
            children.add(extensions);
        }

        if (children.size() == 0) {
            return null;
        }

        return Collections.unmodifiableList(children);
    }
}