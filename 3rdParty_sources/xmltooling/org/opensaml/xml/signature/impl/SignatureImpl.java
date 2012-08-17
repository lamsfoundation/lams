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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.xml.security.signature.XMLSignature;
import org.opensaml.xml.AbstractXMLObject;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.signature.ContentReference;
import org.opensaml.xml.signature.KeyInfo;
import org.opensaml.xml.signature.Signature;

/**
 * XMLObject representing an enveloped or detached XML Digital Signature, version 20020212, Signature element.
 */
public class SignatureImpl extends AbstractXMLObject implements Signature {

    /** Canonicalization algorithm used in signature. */
    private String canonicalizationAlgorithm;

    /** Algorithm used to generate the signature. */
    private String signatureAlgorithm;

    /** Optional HMAC output length parameter to the signature algorithm. */
    private Integer hmacOutputLength;

    /** Key used to sign the signature. */
    private Credential signingCredential;

    /** Public key information to embed in the signature. */
    private KeyInfo keyInfo;

    /** References to content to be signed. */
    private List<ContentReference> contentReferences;

    /** Constructed Apache XML Security signature object. */
    private XMLSignature xmlSignature;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected SignatureImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        contentReferences = new LinkedList<ContentReference>();
    }

    /** {@inheritDoc} */
    public String getCanonicalizationAlgorithm() {
        return canonicalizationAlgorithm;
    }

    /** {@inheritDoc} */
    public void setCanonicalizationAlgorithm(String newAlgorithm) {
        canonicalizationAlgorithm = prepareForAssignment(canonicalizationAlgorithm, newAlgorithm);
    }

    /** {@inheritDoc} */
    public String getSignatureAlgorithm() {
        return signatureAlgorithm;
    }

    /** {@inheritDoc} */
    public void setSignatureAlgorithm(String newAlgorithm) {
        signatureAlgorithm = prepareForAssignment(signatureAlgorithm, newAlgorithm);
    }

    /** {@inheritDoc} */
    public Integer getHMACOutputLength() {
        return hmacOutputLength;
    }

    /** {@inheritDoc} */
    public void setHMACOutputLength(Integer length) {
        hmacOutputLength = prepareForAssignment(hmacOutputLength, length);
    }

    /** {@inheritDoc} */
    public Credential getSigningCredential() {
        return signingCredential;
    }

    /** {@inheritDoc} */
    public void setSigningCredential(Credential newCredential) {
        signingCredential = prepareForAssignment(signingCredential, newCredential);
    }

    /** {@inheritDoc} */
    public KeyInfo getKeyInfo() {
        return keyInfo;
    }

    /** {@inheritDoc} */
    public void setKeyInfo(KeyInfo newKeyInfo) {
        keyInfo = prepareForAssignment(keyInfo, newKeyInfo);
    }

    /** {@inheritDoc} */
    public List<ContentReference> getContentReferences() {
        // TODO worry about detecting changes and releasing this object's and parent's DOM?
        // would need something like an Observable list/collection impl or something similar
        return contentReferences;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        return Collections.EMPTY_LIST;
    }

    /** {@inheritDoc} */
    public void releaseDOM() {
        super.releaseDOM();
        xmlSignature = null;
        
        // Signature's does not treat its children as other XMLObjects do
        // they are more tightly bound to the Signature and can not exist
        // without it.  So when Signature releases its DOM it whacks the 
        // DOM for its children too
        if (keyInfo != null) {
            keyInfo.releaseChildrenDOM(true);
            keyInfo.releaseDOM();
        }
    }

    /**
     * Get the Apache XML Security signature instance held by this object.
     * 
     * @return an Apache XML Security signature object
     */
    public XMLSignature getXMLSignature() {
        return xmlSignature;
    }

    /**
     * Set the Apache XML Security signature instance held by this object.
     * 
     * @param signature an Apache XML Security signature object
     */
    public void setXMLSignature(XMLSignature signature) {
        xmlSignature = prepareForAssignment(xmlSignature, signature);
    }
}