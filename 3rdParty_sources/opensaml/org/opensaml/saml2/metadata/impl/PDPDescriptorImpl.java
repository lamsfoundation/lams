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

import org.opensaml.saml2.metadata.AssertionIDRequestService;
import org.opensaml.saml2.metadata.AuthzService;
import org.opensaml.saml2.metadata.Endpoint;
import org.opensaml.saml2.metadata.NameIDFormat;
import org.opensaml.saml2.metadata.PDPDescriptor;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.XMLObjectChildrenList;

/**
 * Concrete implementation of {@link org.opensaml.saml2.metadata.PDPDescriptor}.
 */
public class PDPDescriptorImpl extends RoleDescriptorImpl implements PDPDescriptor {

    /** AuthzService children. */
    private final XMLObjectChildrenList<AuthzService> authzServices;

    /** AssertionIDRequestService children. */
    private final XMLObjectChildrenList<AssertionIDRequestService> assertionIDRequestServices;

    /** NameIDFormat children. */
    private final XMLObjectChildrenList<NameIDFormat> nameIDFormats;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected PDPDescriptorImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        authzServices = new XMLObjectChildrenList<AuthzService>(this);
        assertionIDRequestServices = new XMLObjectChildrenList<AssertionIDRequestService>(this);
        nameIDFormats = new XMLObjectChildrenList<NameIDFormat>(this);
    }

    /** {@inheritDoc} */
    public List<AuthzService> getAuthzServices() {
        return authzServices;
    }

    /** {@inheritDoc} */
    public List<AssertionIDRequestService> getAssertionIDRequestServices() {
        return assertionIDRequestServices;
    }

    /** {@inheritDoc} */
    public List<NameIDFormat> getNameIDFormats() {
        return nameIDFormats;
    }
    
    /** {@inheritDoc} */
    public List<Endpoint> getEndpoints() {
        List<Endpoint> endpoints = new ArrayList<Endpoint>();
        endpoints.addAll(authzServices);
        endpoints.addAll(assertionIDRequestServices);
        return Collections.unmodifiableList(endpoints);
    }
    
    /** {@inheritDoc} */
    public List<Endpoint> getEndpoints(QName type) {
        if(type.equals(AuthzService.DEFAULT_ELEMENT_NAME)){
            return Collections.unmodifiableList(new ArrayList<Endpoint>(authzServices));
        }else if(type.equals(AssertionIDRequestService.DEFAULT_ELEMENT_NAME)){
            return Collections.unmodifiableList(new ArrayList<Endpoint>(assertionIDRequestServices));
        }
        
        return null;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();

        children.addAll(super.getOrderedChildren());
        children.addAll(authzServices);
        children.addAll(assertionIDRequestServices);
        children.addAll(nameIDFormats);

        return children;
    }
}