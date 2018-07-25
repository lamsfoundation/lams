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

import javax.xml.namespace.QName;

import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.metadata.AssertionIDRequestService;
import org.opensaml.saml2.metadata.AttributeProfile;
import org.opensaml.saml2.metadata.Endpoint;
import org.opensaml.saml2.metadata.IDPSSODescriptor;
import org.opensaml.saml2.metadata.NameIDMappingService;
import org.opensaml.saml2.metadata.SingleSignOnService;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.schema.XSBooleanValue;
import org.opensaml.xml.util.XMLObjectChildrenList;

/**
 * Concrete implementation of {@link org.opensaml.saml2.metadata.IDPSSODescriptor}.
 */
public class IDPSSODescriptorImpl extends SSODescriptorImpl implements IDPSSODescriptor {

    /** wantAuthnRequestSigned attribute. */
    private XSBooleanValue wantAuthnRequestsSigned;

    /** SingleSignOn services for this entity. */
    private final XMLObjectChildrenList<SingleSignOnService> singleSignOnServices;

    /** NameID mapping services for this entity. */
    private final XMLObjectChildrenList<NameIDMappingService> nameIDMappingServices;

    /** AssertionID request services for this entity. */
    private final XMLObjectChildrenList<AssertionIDRequestService> assertionIDRequestServices;

    /** Attribute profiles supported by this entity. */
    private final XMLObjectChildrenList<AttributeProfile> attributeProfiles;

    /** Attributes accepted by this entity. */
    private final XMLObjectChildrenList<Attribute> attributes;
    
    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected IDPSSODescriptorImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        singleSignOnServices = new XMLObjectChildrenList<SingleSignOnService>(this);
        nameIDMappingServices = new XMLObjectChildrenList<NameIDMappingService>(this);
        assertionIDRequestServices = new XMLObjectChildrenList<AssertionIDRequestService>(this);
        attributeProfiles = new XMLObjectChildrenList<AttributeProfile>(this);
        attributes = new XMLObjectChildrenList<Attribute>(this);
    }

    /** {@inheritDoc} */
    public Boolean getWantAuthnRequestsSigned(){
        if(wantAuthnRequestsSigned != null){
            return wantAuthnRequestsSigned.getValue();
        }
        
        return Boolean.FALSE;
    }
    
    /** {@inheritDoc} */
    public XSBooleanValue getWantAuthnRequestsSignedXSBoolean() {
        return wantAuthnRequestsSigned;
    }
    
    /** {@inheritDoc} */
    public void setWantAuthnRequestsSigned(Boolean newWantSigned){
        if(newWantSigned != null){
            wantAuthnRequestsSigned = prepareForAssignment(wantAuthnRequestsSigned, new XSBooleanValue(newWantSigned, false));
        }else{
            wantAuthnRequestsSigned = prepareForAssignment(wantAuthnRequestsSigned, null);
        }
    }

    /** {@inheritDoc} */
    public void setWantAuthnRequestsSigned(XSBooleanValue wantSigned) {
        wantAuthnRequestsSigned = prepareForAssignment(wantAuthnRequestsSigned, wantSigned);
    }

    /** {@inheritDoc} */
    public List<SingleSignOnService> getSingleSignOnServices() {
        return singleSignOnServices;
    }

    /** {@inheritDoc} */
    public List<NameIDMappingService> getNameIDMappingServices() {
        return nameIDMappingServices;
    }

    /** {@inheritDoc} */
    public List<AssertionIDRequestService> getAssertionIDRequestServices() {
        return assertionIDRequestServices;
    }

    /** {@inheritDoc} */
    public List<AttributeProfile> getAttributeProfiles() {
        return attributeProfiles;
    }

    /** {@inheritDoc} */
    public List<Attribute> getAttributes() {
        return attributes;
    }
    
    /** {@inheritDoc} */
    public List<Endpoint> getEndpoints() {
        List<Endpoint> endpoints = new ArrayList<Endpoint>();
        endpoints.addAll(super.getEndpoints());
        endpoints.addAll(singleSignOnServices);
        endpoints.addAll(nameIDMappingServices);
        endpoints.addAll(assertionIDRequestServices);
        return Collections.unmodifiableList(endpoints);
    }
    
    /** {@inheritDoc} */
    public List<Endpoint> getEndpoints(QName type) {
        if(type.equals(SingleSignOnService.DEFAULT_ELEMENT_NAME)){
            return Collections.unmodifiableList(new ArrayList<Endpoint>(singleSignOnServices));
        }else if(type.equals(NameIDMappingService.DEFAULT_ELEMENT_NAME)){
            return Collections.unmodifiableList(new ArrayList<Endpoint>(nameIDMappingServices));
        }else if(type.equals(AssertionIDRequestService.DEFAULT_ELEMENT_NAME)){
            return Collections.unmodifiableList(new ArrayList<Endpoint>(assertionIDRequestServices));
        }else{
            return super.getEndpoints(type);
        }
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();

        children.addAll(super.getOrderedChildren());
        children.addAll(singleSignOnServices);
        children.addAll(nameIDMappingServices);
        children.addAll(assertionIDRequestServices);
        children.addAll(attributeProfiles);
        children.addAll(attributes);

        return Collections.unmodifiableList(children);
    }
}