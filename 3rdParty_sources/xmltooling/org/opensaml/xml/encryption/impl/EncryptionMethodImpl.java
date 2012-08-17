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

import javax.xml.namespace.QName;

import org.opensaml.xml.XMLObject;
import org.opensaml.xml.encryption.EncryptionMethod;
import org.opensaml.xml.encryption.KeySize;
import org.opensaml.xml.encryption.OAEPparams;
import org.opensaml.xml.util.IndexedXMLObjectChildrenList;
import org.opensaml.xml.validation.AbstractValidatingXMLObject;

/**
 * Concrete implementation of {@link org.opensaml.xml.encryption.EncryptionMethod}.
 */
public class EncryptionMethodImpl extends AbstractValidatingXMLObject implements EncryptionMethod {
    
    /** Algorithm attribute value. */
    private String algorithm;
    
    /** KeySize child element value. */
    private KeySize keySize;
    
    /** OAEPparams child element value. */
    private OAEPparams oaepParams;
    
    /** "any" children. */
    private final IndexedXMLObjectChildrenList<XMLObject> unknownChildren;
    
    /**
     * Constructor.
     *
     * @param namespaceURI
     * @param elementLocalName
     * @param namespacePrefix
     */
    protected EncryptionMethodImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        
        unknownChildren = new IndexedXMLObjectChildrenList<XMLObject>(this);
    }

    /** {@inheritDoc} */
    public String getAlgorithm() {
        return this.algorithm;
    }

    /** {@inheritDoc} */
    public void setAlgorithm(String newAlgorithm) {
        this.algorithm = prepareForAssignment(this.algorithm, newAlgorithm);
    }

    /** {@inheritDoc} */
    public KeySize getKeySize() {
        return this.keySize;
    }

    /** {@inheritDoc} */
    public void setKeySize(KeySize newKeySize) {
        this.keySize = prepareForAssignment(this.keySize, newKeySize);
    }

    /** {@inheritDoc} */
    public OAEPparams getOAEPparams() {
        return this.oaepParams;
    }

    /** {@inheritDoc} */
    public void setOAEPparams(OAEPparams newOAEPparams) {
        this.oaepParams = prepareForAssignment(this.oaepParams, newOAEPparams);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getUnknownXMLObjects() {
        return this.unknownChildren;
    }
    /** {@inheritDoc} */
    public List<XMLObject> getUnknownXMLObjects(QName typeOrName) {
        return (List<XMLObject>) unknownChildren.subList(typeOrName);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();
        
        if (keySize != null) {
            children.add(keySize);
        }
        if (oaepParams != null) {
            children.add(oaepParams);
        }
        
        children.addAll(unknownChildren);
        
        if (children.size() == 0) {
            return null;
        }
        
        return Collections.unmodifiableList(children);
    }

}
