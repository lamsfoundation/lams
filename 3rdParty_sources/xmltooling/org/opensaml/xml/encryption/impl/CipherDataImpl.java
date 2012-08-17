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

package org.opensaml.xml.encryption.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opensaml.xml.XMLObject;
import org.opensaml.xml.encryption.CipherData;
import org.opensaml.xml.encryption.CipherReference;
import org.opensaml.xml.encryption.CipherValue;
import org.opensaml.xml.validation.AbstractValidatingXMLObject;

/**
 * Concrete implementation of {@link org.opensaml.xml.encryption.CipherData}.
 */
public class CipherDataImpl extends AbstractValidatingXMLObject implements CipherData {
    
    /** CipherValue child element. */
    private CipherValue cipherValue;
    
    /** CipherReference child element. */
    private CipherReference cipherReference;

    /**
     * Constructor.
     *
     * @param namespaceURI namespace URI
     * @param elementLocalName local name
     * @param namespacePrefix namespace prefix
     */
    protected  CipherDataImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }

    /** {@inheritDoc} */
    public CipherValue getCipherValue() {
        return this.cipherValue;
    }

    /** {@inheritDoc} */
    public void setCipherValue(CipherValue newCipherValue) {
        this.cipherValue = prepareForAssignment(this.cipherValue, newCipherValue);
    }

    /** {@inheritDoc} */
    public CipherReference getCipherReference() {
        return this.cipherReference;
    }

    /** {@inheritDoc} */
    public void setCipherReference(CipherReference newCipherReference) {
        this.cipherReference = prepareForAssignment(this.cipherReference, newCipherReference);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();
        
        if (cipherValue != null) {
            children.add(cipherValue);
        }
        if (cipherReference != null) {
            children.add(cipherReference);
        }
        
        if (children.size() == 0) {
            return null;
        }
        
        return Collections.unmodifiableList(children);
    }

}
