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

import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.metadata.AssertionIDRequestService;
import org.opensaml.saml2.metadata.AttributeAuthorityDescriptor;
import org.opensaml.saml2.metadata.AttributeProfile;
import org.opensaml.saml2.metadata.AttributeService;
import org.opensaml.saml2.metadata.Endpoint;
import org.opensaml.saml2.metadata.NameIDFormat;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.XMLObjectChildrenList;

/**
 * A concrete implementation of {@link org.opensaml.saml2.metadata.AttributeAuthorityDescriptor}.
 */
public class AttributeAuthorityDescriptorImpl extends RoleDescriptorImpl implements AttributeAuthorityDescriptor {

    /** Attribte query endpoints. */
    private final XMLObjectChildrenList<AttributeService> attributeServices;

    /** Assertion request endpoints. */
    private final XMLObjectChildrenList<AssertionIDRequestService> assertionIDRequestServices;

    /** Supported NameID formats. */
    private final XMLObjectChildrenList<NameIDFormat> nameFormats;

    /** Supported attribute profiles. */
    private final XMLObjectChildrenList<AttributeProfile> attributeProfiles;

    /** Supported attribute. */
    private final XMLObjectChildrenList<Attribute> attributes;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected AttributeAuthorityDescriptorImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        attributeServices = new XMLObjectChildrenList<AttributeService>(this);
        assertionIDRequestServices = new XMLObjectChildrenList<AssertionIDRequestService>(this);
        attributeProfiles = new XMLObjectChildrenList<AttributeProfile>(this);
        nameFormats = new XMLObjectChildrenList<NameIDFormat>(this);
        attributes = new XMLObjectChildrenList<Attribute>(this);
    }

    /** {@inheritDoc} */
    public List<AttributeService> getAttributeServices() {
        return attributeServices;
    }

    /** {@inheritDoc} */
    public List<AssertionIDRequestService> getAssertionIDRequestServices() {
        return assertionIDRequestServices;
    }

    /** {@inheritDoc} */
    public List<NameIDFormat> getNameIDFormats() {
        return nameFormats;
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
        endpoints.addAll(attributeServices);
        endpoints.addAll(assertionIDRequestServices);
        return Collections.unmodifiableList(endpoints);
    }
    
    /** {@inheritDoc} */
    public List<Endpoint> getEndpoints(QName type) {
        if(type.equals(AttributeService.DEFAULT_ELEMENT_NAME)){
            return Collections.unmodifiableList(new ArrayList<Endpoint>(attributeServices));
        }else if(type.equals(AssertionIDRequestService.DEFAULT_ELEMENT_NAME)){
            return Collections.unmodifiableList(new ArrayList<Endpoint>(assertionIDRequestServices));
        }
        
        return null;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();

        children.addAll(super.getOrderedChildren());
        children.addAll(attributeServices);
        children.addAll(assertionIDRequestServices);
        children.addAll(nameFormats);
        children.addAll(attributeProfiles);
        children.addAll(attributes);

        return Collections.unmodifiableList(children);
    }
}