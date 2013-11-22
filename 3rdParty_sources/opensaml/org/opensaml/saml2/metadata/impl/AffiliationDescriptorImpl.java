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

package org.opensaml.saml2.metadata.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;
import org.opensaml.common.impl.AbstractSignableSAMLObject;
import org.opensaml.saml2.common.Extensions;
import org.opensaml.saml2.metadata.AffiliateMember;
import org.opensaml.saml2.metadata.AffiliationDescriptor;
import org.opensaml.saml2.metadata.KeyDescriptor;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.AttributeMap;
import org.opensaml.xml.util.XMLObjectChildrenList;

/**
 * Concrete implementation of {@link org.opensaml.saml2.metadata.AffiliationDescriptor}.
 */
public class AffiliationDescriptorImpl extends AbstractSignableSAMLObject implements AffiliationDescriptor {

    /** ID of the owner of this affiliation */
    private String ownerID;
    
    /** ID attribute*/
    private String id;

    /** validUntil attribute */
    private DateTime validUntil;

    /** cacheDurection attribute */
    private Long cacheDuration;

    /** Extensions child */
    private Extensions extensions;
    
    /** "anyAttribute" attributes */
    private final AttributeMap unknownAttributes;

    /** Members of this affiliation */
    private final XMLObjectChildrenList<AffiliateMember> members;

    /** Key descriptors for this role */
    private final XMLObjectChildrenList<KeyDescriptor> keyDescriptors;

    /**
     * Constructor
     * 
     * @param namespaceURI
     * @param elementLocalName
     * @param namespacePrefix
     */
    protected AffiliationDescriptorImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        unknownAttributes = new AttributeMap(this);
        members = new XMLObjectChildrenList<AffiliateMember>(this);
        keyDescriptors = new XMLObjectChildrenList<KeyDescriptor>(this);
    }

    /** {@inheritDoc} */
    public String getOwnerID() {
        return ownerID;
    }

    /** {@inheritDoc} */
    public void setOwnerID(String newOwnerID) {
        if (newOwnerID != null && newOwnerID.length() > 1024) {
            throw new IllegalArgumentException("Owner ID can not exceed 1024 characters in length");
        }
        ownerID = prepareForAssignment(ownerID, newOwnerID);
    }
    
    public String getID() {
        return id;
    }
    
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
    public Extensions getExtensions() {
        return extensions;
    }

    /** {@inheritDoc} */
    public void setExtensions(Extensions extensions) throws IllegalArgumentException {
        this.extensions = prepareForAssignment(this.extensions, extensions);
    }

    /** {@inheritDoc} */
    public List<AffiliateMember> getMembers() {
        return members;
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
    public String getSignatureReferenceID(){
        return id;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();

        if(getSignature() != null){
            children.add(getSignature());
        }
        
        children.add(getExtensions());

        children.addAll(getMembers());

        children.addAll(getKeyDescriptors());

        return Collections.unmodifiableList(children);
    }
}