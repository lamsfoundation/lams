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

package org.opensaml.saml2.metadata.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;
import org.opensaml.common.impl.AbstractSignableSAMLObject;
import org.opensaml.saml2.common.Extensions;
import org.opensaml.saml2.metadata.ContactPerson;
import org.opensaml.saml2.metadata.KeyDescriptor;
import org.opensaml.saml2.metadata.Organization;
import org.opensaml.saml2.metadata.RoleDescriptor;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.AttributeMap;
import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.util.LazyList;
import org.opensaml.xml.util.XMLObjectChildrenList;

/** Concrete implementation of {@link org.opensaml.saml2.metadata.RoleDescriptor}. */
public abstract class RoleDescriptorImpl extends AbstractSignableSAMLObject implements RoleDescriptor {

    /** ID attribute. */
    private String id;

    /** validUntil attribute. */
    private DateTime validUntil;

    /** cacheDurection attribute. */
    private Long cacheDuration;

    /** Set of supported protocols. */
    private final List<String> supportedProtocols;

    /** Error URL. */
    private String errorURL;

    /** Extensions child. */
    private Extensions extensions;

    /** Organization administering this role. */
    private Organization organization;

    /** "anyAttribute" attributes. */
    private final AttributeMap unknownAttributes;

    /** Contact persons for this role. */
    private final XMLObjectChildrenList<ContactPerson> contactPersons;

    /** Key descriptors for this role. */
    private final XMLObjectChildrenList<KeyDescriptor> keyDescriptors;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected RoleDescriptorImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        unknownAttributes = new AttributeMap(this);
        supportedProtocols = new LazyList<String>();
        contactPersons = new XMLObjectChildrenList<ContactPerson>(this);
        keyDescriptors = new XMLObjectChildrenList<KeyDescriptor>(this);
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
    public boolean isValid() {
        if (null == validUntil) {
            return true;
        }
        
        DateTime now = new DateTime();
        return now.isBefore(validUntil);
    }

    /** {@inheritDoc} */
    public DateTime getValidUntil() {
        return validUntil;
    }

    /** {@inheritDoc} */
    public void setValidUntil(DateTime validUntil) {
        this.validUntil = prepareForAssignment(this.validUntil, validUntil);
    }

    /** {@inheritDoc} */
    public Long getCacheDuration() {
        return cacheDuration;
    }

    /** {@inheritDoc} */
    public void setCacheDuration(Long duration) {
        cacheDuration = prepareForAssignment(cacheDuration, duration);
    }

    /** {@inheritDoc} */
    public List<String> getSupportedProtocols() {
        return Collections.unmodifiableList(supportedProtocols);
    }

    /** {@inheritDoc} */
    public boolean isSupportedProtocol(String protocol) {
        return supportedProtocols.contains(protocol);
    }

    /** {@inheritDoc} */
    public void addSupportedProtocol(String protocol) {
        protocol = DatatypeHelper.safeTrimOrNullString(protocol);
        if (protocol != null && !supportedProtocols.contains(protocol)) {
            releaseThisandParentDOM();
            supportedProtocols.add(protocol);
        }
    }

    /** {@inheritDoc} */
    public void removeSupportedProtocol(String protocol) {
        protocol = DatatypeHelper.safeTrimOrNullString(protocol);
        if (protocol != null && supportedProtocols.contains(protocol)) {
            releaseThisandParentDOM();
            supportedProtocols.remove(protocol);
        }
    }

    /** {@inheritDoc} */
    public void removeSupportedProtocols(Collection<String> protocols) {
        for (String protocol : protocols) {
            removeSupportedProtocol(protocol);
        }
    }

    /** {@inheritDoc} */
    public void removeAllSupportedProtocols() {
        supportedProtocols.clear();
    }

    /** {@inheritDoc} */
    public String getErrorURL() {
        return errorURL;
    }

    /** {@inheritDoc} */
    public void setErrorURL(String errorURL) {

        this.errorURL = prepareForAssignment(this.errorURL, errorURL);
    }

    /** {@inheritDoc} */
    public Extensions getExtensions() {
        return extensions;
    }

    /** {@inheritDoc} */
    public void setExtensions(Extensions extensions) throws IllegalArgumentException {
        this.extensions = prepareForAssignment(this.extensions, extensions);
    }

    /** {@inheritDoc} */
    public Organization getOrganization() {
        return organization;
    }

    /** {@inheritDoc} */
    public void setOrganization(Organization organization) throws IllegalArgumentException {
        this.organization = prepareForAssignment(this.organization, organization);
    }

    /** {@inheritDoc} */
    public List<ContactPerson> getContactPersons() {
        return contactPersons;
    }

    /** {@inheritDoc} */
    public List<KeyDescriptor> getKeyDescriptors() {
        return keyDescriptors;
    }

    /**
     * {@inheritDoc}
     */
    public AttributeMap getUnknownAttributes() {
        return unknownAttributes;
    }

    /** {@inheritDoc} */
    public String getSignatureReferenceID() {
        return id;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();

        if (getSignature() != null) {
            children.add(getSignature());
        }

        if (extensions != null) {
            children.add(getExtensions());
        }
        children.addAll(getKeyDescriptors());
        if (organization != null) {
            children.add(getOrganization());
        }
        children.addAll(getContactPersons());

        return Collections.unmodifiableList(children);
    }
}