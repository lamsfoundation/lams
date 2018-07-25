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

package org.opensaml.ws.wsaddressing.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

import org.opensaml.ws.wsaddressing.Address;
import org.opensaml.ws.wsaddressing.EndpointReferenceType;
import org.opensaml.ws.wsaddressing.Metadata;
import org.opensaml.ws.wsaddressing.ReferenceParameters;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.AttributeMap;
import org.opensaml.xml.util.IndexedXMLObjectChildrenList;

/**
 * Abstract implementation of the element of type {@link EndpointReferenceType }.
 * 
 */
public class EndpointReferenceTypeImpl extends AbstractWSAddressingObject implements EndpointReferenceType {

    /** {@link Address} child element. */
    private Address address;

    /** Optional {@link Metadata} child element. */
    private Metadata metadata;

    /** Optional {@link ReferenceParameters} child element. */
    private ReferenceParameters referenceParameters;
    
    /** Wildcard child elements. */
    private IndexedXMLObjectChildrenList<XMLObject>  unknownChildren;
    
    /** Wildcard attributes. */
    private AttributeMap unknownAttributes;

    /**
     * Constructor.
     * 
     * @param namespaceURI The namespace of the element
     * @param elementLocalName The local name of the element
     * @param namespacePrefix The namespace prefix of the element
     */
    public EndpointReferenceTypeImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        unknownChildren = new IndexedXMLObjectChildrenList<XMLObject>(this);
        unknownAttributes = new AttributeMap(this);
    }

    /** {@inheritDoc} */
    public Address getAddress() {
        return address;
    }

    /** {@inheritDoc} */
    public void setAddress(Address newAddress) {
        address = prepareForAssignment(address, newAddress);
    }

    /** {@inheritDoc} */
    public Metadata getMetadata() {
        return metadata;
    }

    /** {@inheritDoc} */
    public void setMetadata(Metadata newMetadata) {
        metadata = prepareForAssignment(metadata, newMetadata);
    }

    /** {@inheritDoc} */
    public ReferenceParameters getReferenceParameters() {
        return referenceParameters;
    }

    /** {@inheritDoc} */
    public void setReferenceParameters(ReferenceParameters newReferenceParameters) {
        referenceParameters = prepareForAssignment(referenceParameters, newReferenceParameters);
    }

    /** {@inheritDoc} */
    public AttributeMap getUnknownAttributes() {
        return unknownAttributes;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getUnknownXMLObjects() {
        return unknownChildren;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getUnknownXMLObjects(QName typeOrName) {
        return (List<XMLObject>) unknownChildren.subList(typeOrName);
    }
    
    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();
        if (address != null) {
            children.add(address);
        }
        if (referenceParameters != null) {
            children.add(referenceParameters);
        }
        if (metadata != null) {
            children.add(metadata);
        }

        // xs:any element
        if (!getUnknownXMLObjects().isEmpty()) {
            children.addAll(getUnknownXMLObjects());
        }

        return Collections.unmodifiableList(children);
    }
 
}
