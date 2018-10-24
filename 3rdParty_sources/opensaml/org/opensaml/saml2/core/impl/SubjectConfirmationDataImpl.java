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

import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

import org.joda.time.DateTime;
import org.opensaml.common.impl.AbstractSAMLObject;
import org.opensaml.saml2.core.SubjectConfirmationData;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.AttributeMap;
import org.opensaml.xml.util.IndexedXMLObjectChildrenList;

/**
 * Concrete implementation of {@link org.opensaml.saml2.core.SubjectConfirmationData}.
 */
public class SubjectConfirmationDataImpl extends AbstractSAMLObject implements SubjectConfirmationData {

    /** NotBefore of the Confirmation Data. */
    private DateTime notBefore;

    /** NotOnOrAfter of the Confirmation Data. */
    private DateTime notOnOrAfter;

    /** Recipient of the Confirmation Data. */
    private String recipient;

    /** InResponseTo of the Confirmation Data. */
    private String inResponseTo;

    /** Address of the Confirmation Data. */
    private String address;
    
    /** "anyAttribute" attributes. */
    private final AttributeMap unknownAttributes;
    
    /** "any" children. */
    private final IndexedXMLObjectChildrenList<XMLObject> unknownChildren;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected SubjectConfirmationDataImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        unknownAttributes = new AttributeMap(this);
        unknownChildren = new IndexedXMLObjectChildrenList<XMLObject>(this);
    }

    /** {@inheritDoc} */
    public DateTime getNotBefore() {
        return notBefore;
    }

    /** {@inheritDoc} */
    public void setNotBefore(DateTime newNotBefore) {
        this.notBefore = prepareForAssignment(this.notBefore, newNotBefore);
    }

    /** {@inheritDoc} */
    public DateTime getNotOnOrAfter() {
        return notOnOrAfter;
    }

    /** {@inheritDoc} */
    public void setNotOnOrAfter(DateTime newNotOnOrAfter) {
        this.notOnOrAfter = prepareForAssignment(this.notOnOrAfter, newNotOnOrAfter);
    }

    /** {@inheritDoc} */
    public String getRecipient() {
        return recipient;
    }

    /** {@inheritDoc} */
    public void setRecipient(String newRecipient) {
        this.recipient = prepareForAssignment(this.recipient, newRecipient);
    }

    /** {@inheritDoc} */
    public String getInResponseTo() {
        return inResponseTo;
    }

    /** {@inheritDoc} */
    public void setInResponseTo(String newInResponseTo) {
        this.inResponseTo = prepareForAssignment(this.inResponseTo, newInResponseTo);
    }

    /** {@inheritDoc} */
    public String getAddress() {
        return address;
    }

    /** {@inheritDoc} */
    public void setAddress(String newAddress) {
        this.address = prepareForAssignment(this.address, newAddress);
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

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        return Collections.unmodifiableList(unknownChildren);
    }
}