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
import org.opensaml.xml.encryption.AgreementMethod;
import org.opensaml.xml.encryption.KANonce;
import org.opensaml.xml.encryption.OriginatorKeyInfo;
import org.opensaml.xml.encryption.RecipientKeyInfo;
import org.opensaml.xml.util.IndexedXMLObjectChildrenList;
import org.opensaml.xml.validation.AbstractValidatingXMLObject;

/**
 * Concrete implementation of {@link org.opensaml.xml.encryption.AgreementMethod}.
 */
public class AgreementMethodImpl extends AbstractValidatingXMLObject implements AgreementMethod {
    
    /** Algorithm attribute value. */
    private String algorithm;
    
    /** KA-Nonce child element value. */
    private KANonce kaNonce;
    
    /** OriginatorKeyInfo child element value. */
    private OriginatorKeyInfo originatorKeyInfo;
    
    /** RecipientKeyInfo child element value. */
    private RecipientKeyInfo recipientKeyInfo;
    
    /** List of wildcard &lt;any&gt; XMLObject children. */
    private IndexedXMLObjectChildrenList xmlChildren;

    /**
     * Constructor.
     *
     * @param namespaceURI namespace URI
     * @param elementLocalName element local name
     * @param namespacePrefix namespace prefix
     */
    protected AgreementMethodImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        xmlChildren = new IndexedXMLObjectChildrenList(this);
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
    public KANonce getKANonce() {
        return this.kaNonce;
    }

    /** {@inheritDoc} */
    public void setKANonce(KANonce newKANonce) {
        this.kaNonce = prepareForAssignment(this.kaNonce, newKANonce);
    }

    /** {@inheritDoc} */
    public OriginatorKeyInfo getOriginatorKeyInfo() {
        return this.originatorKeyInfo;
    }

    /** {@inheritDoc} */
    public void setOriginatorKeyInfo(OriginatorKeyInfo newOriginatorKeyInfo) {
        this.originatorKeyInfo = prepareForAssignment(this.originatorKeyInfo, newOriginatorKeyInfo);
    }

    /** {@inheritDoc} */
    public RecipientKeyInfo getRecipientKeyInfo() {
        return this.recipientKeyInfo;
    }

    /** {@inheritDoc} */
    public void setRecipientKeyInfo(RecipientKeyInfo newRecipientKeyInfo) {
        this.recipientKeyInfo = prepareForAssignment(this.recipientKeyInfo, newRecipientKeyInfo);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getUnknownXMLObjects() {
        return (List<XMLObject>) this.xmlChildren;
    }
    /** {@inheritDoc} */
    public List<XMLObject> getUnknownXMLObjects(QName typeOrName) {
        return (List<XMLObject>) this.xmlChildren.subList(typeOrName);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();
        
        if (kaNonce != null) {
            children.add(kaNonce);
        }
        
        children.addAll(xmlChildren);
        
        if (originatorKeyInfo != null) {
            children.add(originatorKeyInfo);
        }
        if (recipientKeyInfo != null) {
            children.add(recipientKeyInfo);
        }
        
        if (children.size() == 0) {
            return null;
        }
        
        return Collections.unmodifiableList(children);
    }

}
