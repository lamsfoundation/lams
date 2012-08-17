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

import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

import org.opensaml.common.impl.AbstractSAMLObject;
import org.opensaml.saml2.metadata.Endpoint;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.AttributeMap;
import org.opensaml.xml.util.IndexedXMLObjectChildrenList;

/**
 * A concrete implementation of {@link org.opensaml.saml2.metadata.Endpoint}
 */
public abstract class EndpointImpl extends AbstractSAMLObject implements Endpoint {

    /** Binding URI */
    private String bindingId;

    /** Endpoint location URI */
    private String location;

    /** Response location URI */
    private String responseLocation;
    
    /** "anyAttribute" attributes */
    private final AttributeMap unknownAttributes;
    
    /** child "any" elements */
    private final IndexedXMLObjectChildrenList<XMLObject> unknownChildren;

    /**
     * Constructor
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected EndpointImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        unknownAttributes = new AttributeMap(this);
        unknownChildren = new IndexedXMLObjectChildrenList<XMLObject>(this);
    }

    /** {@inheritDoc} */
    public String getBinding() {
        return bindingId;
    }

    /** {@inheritDoc} */
    public void setBinding(String binding) {
        bindingId = prepareForAssignment(bindingId, binding);
    }

    /** {@inheritDoc} */
    public String getLocation() {
        return location;
    }

    /** {@inheritDoc} */
    public void setLocation(String location) {
        this.location = prepareForAssignment(this.location, location);
    }

    /** {@inheritDoc} */
    public String getResponseLocation() {
        return responseLocation;
    }

    /** {@inheritDoc} */
    public void setResponseLocation(String location) {
        responseLocation = prepareForAssignment(responseLocation, location);
    }
    
    /**
     * {@inheritDoc}
     */
    public AttributeMap getUnknownAttributes() {
        return unknownAttributes;
    }
    
    /**
     * {@inheritDoc}
     */
    public List<XMLObject> getUnknownXMLObjects() {
        return unknownChildren;
    }
    
    /** {@inheritDoc} */
    public List<XMLObject> getUnknownXMLObjects(QName typeOrName) {
        return (List<XMLObject>) unknownChildren.subList(typeOrName);
    }
    
    /**
     * {@inheritDoc}
     */
    public List<XMLObject> getOrderedChildren() {
        return Collections.unmodifiableList(unknownChildren);
    }
}