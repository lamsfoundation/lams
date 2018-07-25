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
import org.opensaml.xml.signature.DSAKeyValue;
import org.opensaml.xml.signature.KeyValue;
import org.opensaml.xml.signature.RSAKeyValue;
import org.opensaml.xml.validation.AbstractValidatingXMLObject;

/**
 * Concrete implementation of {@link org.opensaml.xml.signature.KeyValue}
 */
public class KeyValueImpl extends AbstractValidatingXMLObject implements KeyValue {
    
    /** DSAKeyValue child element */
    private DSAKeyValue dsaKeyValue;
    
    /** RSAKeyValue child element */
    private RSAKeyValue rsaKeyValue;
    
    /** Wildcard &lt;any&gt; XMLObject child element */
    private XMLObject unknownXMLObject;

    /**
     * Constructor
     *
     * @param namespaceURI
     * @param elementLocalName
     * @param namespacePrefix
     */
    protected KeyValueImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }

    /** {@inheritDoc} */
    public DSAKeyValue getDSAKeyValue() {
        return this.dsaKeyValue;
    }

    /** {@inheritDoc} */
    public void setDSAKeyValue(DSAKeyValue newDSAKeyValue) {
        this.dsaKeyValue = prepareForAssignment(this.dsaKeyValue, newDSAKeyValue);
    }

    /** {@inheritDoc} */
    public RSAKeyValue getRSAKeyValue() {
        return this.rsaKeyValue;
    }

    /** {@inheritDoc} */
    public void setRSAKeyValue(RSAKeyValue newRSAKeyValue) {
        this.rsaKeyValue = prepareForAssignment(this.rsaKeyValue, newRSAKeyValue);
    }

    /** {@inheritDoc} */
    public XMLObject getUnknownXMLObject() {
        return this.unknownXMLObject;
    }

    /** {@inheritDoc} */
    public void setUnknownXMLObject(XMLObject newXMLObject) {
        this.unknownXMLObject = prepareForAssignment(this.unknownXMLObject, newXMLObject);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();
        
        if (dsaKeyValue != null) {
            children.add(dsaKeyValue);
        }
        if (rsaKeyValue != null) {
            children.add(rsaKeyValue);
        }
        if (unknownXMLObject != null) {
            children.add(unknownXMLObject);
        }
        
        if (children.size() == 0) {
            return null;
        }
        
        return Collections.unmodifiableList(children);
    }

}
