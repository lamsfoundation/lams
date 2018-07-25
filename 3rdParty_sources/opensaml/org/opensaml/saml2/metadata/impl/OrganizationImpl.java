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
import java.util.List;

import org.opensaml.common.impl.AbstractSAMLObject;
import org.opensaml.saml2.common.Extensions;
import org.opensaml.saml2.metadata.Organization;
import org.opensaml.saml2.metadata.OrganizationDisplayName;
import org.opensaml.saml2.metadata.OrganizationName;
import org.opensaml.saml2.metadata.OrganizationURL;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.AttributeMap;
import org.opensaml.xml.util.XMLObjectChildrenList;

/**
 * Concrete implementation of {@link org.opensaml.saml2.metadata.Organization}
 */
public class OrganizationImpl extends AbstractSAMLObject implements Organization {

    /** element extensions */
    private Extensions extensions;

    /** OrganizationName children */
    private final XMLObjectChildrenList<OrganizationName> names;

    /** OrganizationDisplayName children */
    private final XMLObjectChildrenList<OrganizationDisplayName> displayNames;

    /** OrganizationURL children */
    private final XMLObjectChildrenList<OrganizationURL> urls;
    
    /** "anyAttribute" attributes */
    private final AttributeMap unknownAttributes;

    /**
     * Constructor
     * 
     * @param namespaceURI
     * @param elementLocalName
     * @param namespacePrefix
     */
    protected OrganizationImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        names = new XMLObjectChildrenList<OrganizationName>(this);
        displayNames = new XMLObjectChildrenList<OrganizationDisplayName>(this);
        urls = new XMLObjectChildrenList<OrganizationURL>(this);
        unknownAttributes = new AttributeMap(this);
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
    public List<OrganizationName> getOrganizationNames() {
        return names;
    }

    /** {@inheritDoc} */
    public List<OrganizationDisplayName> getDisplayNames() {
        return displayNames;
    }

    /** {@inheritDoc} */
    public List<OrganizationURL> getURLs() {
        return urls;
    }
    
    /**
     * {@inheritDoc}
     */
    public AttributeMap getUnknownAttributes() {
        return unknownAttributes;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();

        children.add(extensions);
        children.addAll(names);
        children.addAll(displayNames);
        children.addAll(urls);

        return children;
    }
}