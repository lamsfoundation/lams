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
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

import org.joda.time.DateTime;
import org.opensaml.common.impl.AbstractSignableSAMLObject;
import org.opensaml.saml2.common.Extensions;
import org.opensaml.saml2.metadata.AdditionalMetadataLocation;
import org.opensaml.saml2.metadata.AffiliationDescriptor;
import org.opensaml.saml2.metadata.AttributeAuthorityDescriptor;
import org.opensaml.saml2.metadata.AuthnAuthorityDescriptor;
import org.opensaml.saml2.metadata.ContactPerson;
import org.opensaml.saml2.metadata.EntityDescriptor;
import org.opensaml.saml2.metadata.IDPSSODescriptor;
import org.opensaml.saml2.metadata.Organization;
import org.opensaml.saml2.metadata.PDPDescriptor;
import org.opensaml.saml2.metadata.RoleDescriptor;
import org.opensaml.saml2.metadata.SPSSODescriptor;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.AttributeMap;
import org.opensaml.xml.util.IndexedXMLObjectChildrenList;
import org.opensaml.xml.util.XMLObjectChildrenList;

/**
 * Concretate implementation of {@link org.opensaml.saml2.metadata.EntitiesDescriptor}.
 */
public class EntityDescriptorImpl extends AbstractSignableSAMLObject implements EntityDescriptor {

    /** Entity ID of this Entity. */
    private String entityID;

    /** ID attribute. */
    private String id;

    /** validUntil attribute. */
    private DateTime validUntil;

    /** cacheDurection attribute. */
    private Long cacheDuration;

    /** Extensions child. */
    private Extensions extensions;

    /** Role descriptors for this entity. */
    private final IndexedXMLObjectChildrenList<RoleDescriptor> roleDescriptors;

    /** Affiliatition descriptor for this entity. */
    private AffiliationDescriptor affiliationDescriptor;

    /** Organization the administers this entity. */
    private Organization organization;

    /** Contact persons for this entity. */
    private final XMLObjectChildrenList<ContactPerson> contactPersons;

    /** Additional metadata locations for this entity. */
    private final XMLObjectChildrenList<AdditionalMetadataLocation> additionalMetadata;

    /** "anyAttribute" attributes. */
    private final AttributeMap unknownAttributes;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected EntityDescriptorImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        roleDescriptors = new IndexedXMLObjectChildrenList<RoleDescriptor>(this);
        contactPersons = new XMLObjectChildrenList<ContactPerson>(this);
        additionalMetadata = new XMLObjectChildrenList<AdditionalMetadataLocation>(this);
        unknownAttributes = new AttributeMap(this);
    }

    /** {@inheritDoc} */
    public String getEntityID() {
        return entityID;
    }

    /** {@inheritDoc} */
    public void setEntityID(String newId) {
        if (newId != null && newId.length() > 1024) {
            throw new IllegalArgumentException("Entity ID can not exceed 1024 characters in length");
        }
        entityID = prepareForAssignment(entityID, newId);
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
    public void setValidUntil(DateTime newValidUntil) {
        validUntil = prepareForAssignment(validUntil, newValidUntil);
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
    public Extensions getExtensions() {
        return extensions;
    }

    /** {@inheritDoc} */
    public void setExtensions(Extensions newExtensions) {
        extensions = prepareForAssignment(extensions, newExtensions);
    }

    /** {@inheritDoc} */
    public List<RoleDescriptor> getRoleDescriptors() {
        return roleDescriptors;
    }

    /** {@inheritDoc} */
    public List<RoleDescriptor> getRoleDescriptors(QName typeOrName) {
        return (List<RoleDescriptor>) roleDescriptors.subList(typeOrName);
    }

    /** {@inheritDoc} */
    public List<RoleDescriptor> getRoleDescriptors(QName type, String supportedProtocol) {
        ArrayList<RoleDescriptor> supportingRoleDescriptors = new ArrayList<RoleDescriptor>();
        for (RoleDescriptor descriptor : roleDescriptors.subList(type)) {
            if (descriptor.isSupportedProtocol(supportedProtocol)) {
                supportingRoleDescriptors.add(descriptor);
            }
        }

        return supportingRoleDescriptors;
    }

    /** {@inheritDoc} */
    public IDPSSODescriptor getIDPSSODescriptor(String supportedProtocol) {
        List<RoleDescriptor> descriptors = getRoleDescriptors(IDPSSODescriptor.DEFAULT_ELEMENT_NAME, supportedProtocol);
        if (descriptors.size() > 0) {
            return (IDPSSODescriptor) descriptors.get(0);
        }

        return null;
    }

    /** {@inheritDoc} */
    public SPSSODescriptor getSPSSODescriptor(String supportedProtocol) {
        List<RoleDescriptor> descriptors = getRoleDescriptors(SPSSODescriptor.DEFAULT_ELEMENT_NAME, supportedProtocol);
        if (descriptors.size() > 0) {
            return (SPSSODescriptor) descriptors.get(0);
        }

        return null;
    }

    /** {@inheritDoc} */
    public AuthnAuthorityDescriptor getAuthnAuthorityDescriptor(String supportedProtocol) {
        List<RoleDescriptor> descriptors = getRoleDescriptors(AuthnAuthorityDescriptor.DEFAULT_ELEMENT_NAME,
                supportedProtocol);
        if (descriptors.size() > 0) {
            return (AuthnAuthorityDescriptor) descriptors.get(0);
        }

        return null;
    }

    /** {@inheritDoc} */
    public AttributeAuthorityDescriptor getAttributeAuthorityDescriptor(String supportedProtocol) {
        List<RoleDescriptor> descriptors = getRoleDescriptors(AttributeAuthorityDescriptor.DEFAULT_ELEMENT_NAME,
                supportedProtocol);
        if (descriptors.size() > 0) {
            return (AttributeAuthorityDescriptor) descriptors.get(0);
        }

        return null;
    }

    /** {@inheritDoc} */
    public PDPDescriptor getPDPDescriptor(String supportedProtocol) {
        List<RoleDescriptor> descriptors = getRoleDescriptors(PDPDescriptor.DEFAULT_ELEMENT_NAME, supportedProtocol);
        if (descriptors.size() > 0) {
            return (PDPDescriptor) descriptors.get(0);
        }

        return null;
    }

    /** {@inheritDoc} */
    public AffiliationDescriptor getAffiliationDescriptor() {
        return affiliationDescriptor;
    }

    /** {@inheritDoc} */
    public void setAffiliationDescriptor(AffiliationDescriptor descriptor) {
        affiliationDescriptor = prepareForAssignment(affiliationDescriptor, descriptor);
    }

    /** {@inheritDoc} */
    public Organization getOrganization() {
        return organization;
    }

    /** {@inheritDoc} */
    public void setOrganization(Organization newOrganization) {
        organization = prepareForAssignment(organization, newOrganization);
    }

    /** {@inheritDoc} */
    public List<ContactPerson> getContactPersons() {
        return contactPersons;
    }

    /** {@inheritDoc} */
    public List<AdditionalMetadataLocation> getAdditionalMetadataLocations() {
        return additionalMetadata;
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
        children.add(getExtensions());
        children.addAll(roleDescriptors);
        children.add(getAffiliationDescriptor());
        children.add(getOrganization());
        children.addAll(contactPersons);
        children.addAll(additionalMetadata);

        return Collections.unmodifiableList(children);
    }
}