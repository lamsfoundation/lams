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

package org.opensaml.xml.signature.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opensaml.xml.XMLObject;
import org.opensaml.xml.signature.X509IssuerName;
import org.opensaml.xml.signature.X509IssuerSerial;
import org.opensaml.xml.signature.X509SerialNumber;
import org.opensaml.xml.validation.AbstractValidatingXMLObject;

/**
 * Concrete implementation of {@link org.opensaml.xml.signature.X509IssuerSerial}
 */
public class X509IssuerSerialImpl extends AbstractValidatingXMLObject implements X509IssuerSerial {
    
    /** X509IssuerName child element */
    private X509IssuerName issuerName;
    
    /** X509SerialNumber child element */
    private X509SerialNumber serialNumber;

    /**
     * Constructor
     *
     * @param namespaceURI
     * @param elementLocalName
     * @param namespacePrefix
     */
    protected X509IssuerSerialImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }

    /** {@inheritDoc} */
    public X509IssuerName getX509IssuerName() {
        return this.issuerName;
    }

    /** {@inheritDoc} */
    public void setX509IssuerName(X509IssuerName newX509IssuerName) {
        this.issuerName = prepareForAssignment(this.issuerName, newX509IssuerName);
    }

    /** {@inheritDoc} */
    public X509SerialNumber getX509SerialNumber() {
        return this.serialNumber;
    }

    /** {@inheritDoc} */
    public void setX509SerialNumber(X509SerialNumber newX509SerialNumber) {
        this.serialNumber = prepareForAssignment(this.serialNumber, newX509SerialNumber);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();
        
        if (issuerName != null) {
            children.add(issuerName);
        }
        if (serialNumber != null) {
            children.add(serialNumber);
        }
        
        if (children.size() == 0) {
            return null;
        }
        
        return Collections.unmodifiableList(children);
    }

}
