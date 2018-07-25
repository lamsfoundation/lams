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

import javax.xml.namespace.QName;

import org.opensaml.xml.XMLObject;
import org.opensaml.xml.encryption.AgreementMethod;
import org.opensaml.xml.encryption.EncryptedKey;
import org.opensaml.xml.signature.KeyInfoType;
import org.opensaml.xml.signature.KeyName;
import org.opensaml.xml.signature.KeyValue;
import org.opensaml.xml.signature.MgmtData;
import org.opensaml.xml.signature.PGPData;
import org.opensaml.xml.signature.RetrievalMethod;
import org.opensaml.xml.signature.SPKIData;
import org.opensaml.xml.signature.X509Data;
import org.opensaml.xml.util.IndexedXMLObjectChildrenList;
import org.opensaml.xml.validation.AbstractValidatingXMLObject;

/**
 * Concrete implementation of {@link org.opensaml.xml.signature.KeyInfoType}
 */
public class KeyInfoTypeImpl extends AbstractValidatingXMLObject implements KeyInfoType {
    
    /** The list of XMLObject child elements */
    private final IndexedXMLObjectChildrenList indexedChildren;
    
    /** The Id attribute value */
    private String id;

    /**
     * Constructor
     *
     * @param namespaceURI
     * @param elementLocalName
     * @param namespacePrefix
     */
    protected KeyInfoTypeImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        indexedChildren = new IndexedXMLObjectChildrenList(this);
    }
    
    /** {@inheritDoc} */
    public String getID() {
        return this.id;
    }

    /** {@inheritDoc} */
    public void setID(String newID) {
        String oldID = this.id;
        this.id = prepareForAssignment(this.id, newID);
        registerOwnID(oldID, this.id);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getXMLObjects() {
        return (List<XMLObject>) indexedChildren;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getXMLObjects(QName typeOrName) {
        return (List<XMLObject>) indexedChildren.subList(typeOrName);
    }

    /** {@inheritDoc} */
    public List<KeyName> getKeyNames() {
        return (List<KeyName>) indexedChildren.subList(KeyName.DEFAULT_ELEMENT_NAME);
    }

    /** {@inheritDoc} */
    public List<KeyValue> getKeyValues() {
        return (List<KeyValue>) indexedChildren.subList(KeyValue.DEFAULT_ELEMENT_NAME);
    }

    /** {@inheritDoc} */
    public List<RetrievalMethod> getRetrievalMethods() {
        return (List<RetrievalMethod>) indexedChildren.subList(RetrievalMethod.DEFAULT_ELEMENT_NAME);
    }

    /** {@inheritDoc} */
    public List<X509Data> getX509Datas() {
        return (List<X509Data>) indexedChildren.subList(X509Data.DEFAULT_ELEMENT_NAME);
    }

    /** {@inheritDoc} */
    public List<PGPData> getPGPDatas() {
        return (List<PGPData>) indexedChildren.subList(PGPData.DEFAULT_ELEMENT_NAME);
    }

    /** {@inheritDoc} */
    public List<SPKIData> getSPKIDatas() {
        return (List<SPKIData>) indexedChildren.subList(SPKIData.DEFAULT_ELEMENT_NAME);
    }

    /** {@inheritDoc} */
    public List<MgmtData> getMgmtDatas() {
        return (List<MgmtData>) indexedChildren.subList(MgmtData.DEFAULT_ELEMENT_NAME);
    }

    /** {@inheritDoc} */
    public List<AgreementMethod> getAgreementMethods() {
        return (List<AgreementMethod>) indexedChildren.subList(AgreementMethod.DEFAULT_ELEMENT_NAME);
    }

    /** {@inheritDoc} */
    public List<EncryptedKey> getEncryptedKeys() {
        return (List<EncryptedKey>) indexedChildren.subList(EncryptedKey.DEFAULT_ELEMENT_NAME);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();
        
        children.addAll(indexedChildren);
        
        if (children.size() == 0) {
            return null;
        }
        
        return Collections.unmodifiableList(children);
    }

}
