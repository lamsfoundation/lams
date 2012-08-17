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

import java.util.List;

import org.apache.xml.security.Init;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.SignedInfo;
import org.apache.xml.security.signature.XMLSignature;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.signature.KeyInfo;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.util.XMLConstants;
import org.opensaml.xml.util.XMLHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

/**
 * An unmarshaller for {@link org.opensaml.xml.signature.Signature} objects.
 */
public class SignatureUnmarshaller implements Unmarshaller {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(SignatureUnmarshaller.class);

    /** Constructor. */
    public SignatureUnmarshaller() {
        if (!Init.isInitialized()) {
            log.debug("Initializing XML security library");
            Init.init();
        }
    }

    /** {@inheritDoc} */
    public Signature unmarshall(Element signatureElement) throws UnmarshallingException {
        log.debug("Starting to unmarshall Apache XML-Security-based SignatureImpl element");

        SignatureImpl signature = new SignatureImpl(signatureElement.getNamespaceURI(),
                signatureElement.getLocalName(), signatureElement.getPrefix());

        try {
            log.debug("Constructing Apache XMLSignature object");

            XMLSignature xmlSignature = new XMLSignature(signatureElement, "");

            SignedInfo signedInfo = xmlSignature.getSignedInfo();

            log.debug("Adding canonicalization and signing algorithms, and HMAC output length to Signature");
            signature.setCanonicalizationAlgorithm(signedInfo.getCanonicalizationMethodURI());
            signature.setSignatureAlgorithm(signedInfo.getSignatureMethodURI());
            signature.setHMACOutputLength(getHMACOutputLengthValue(signedInfo.getSignatureMethodElement()));

            org.apache.xml.security.keys.KeyInfo xmlSecKeyInfo = xmlSignature.getKeyInfo();
            if (xmlSecKeyInfo != null) {
                log.debug("Adding KeyInfo to Signature");
                Unmarshaller unmarshaller = Configuration.getUnmarshallerFactory().getUnmarshaller(
                        xmlSecKeyInfo.getElement());
                KeyInfo keyInfo = (KeyInfo) unmarshaller.unmarshall(xmlSecKeyInfo.getElement());
                signature.setKeyInfo(keyInfo);
            }
            signature.setXMLSignature(xmlSignature);
            signature.setDOM(signatureElement);
            return signature;
        } catch (XMLSecurityException e) {
            log.error("Error constructing Apache XMLSignature instance from Signature element: {}", e.getMessage());
            throw new UnmarshallingException("Unable to unmarshall Signature with Apache XMLSignature", e);
        }
    }

    /**
     * Find and return the integer value contained within the HMACOutputLength element, if present.
     * 
     * @param signatureMethodElement the ds:SignatureMethod element
     * @return the HMAC output length value, or null if not present
     */
    private Integer getHMACOutputLengthValue(Element signatureMethodElement) {
        if (signatureMethodElement == null) {
            return null;
        }
        // Should be at most one element
        List<Element> children = XMLHelper.getChildElementsByTagNameNS(signatureMethodElement, XMLConstants.XMLSIG_NS,
                "HMACOutputLength");
        if (!children.isEmpty()) {
            Element hmacElement = children.get(0);
            String value = DatatypeHelper.safeTrimOrNullString(hmacElement.getTextContent());
            if (value != null) {
                return new Integer(value);
            }
        }
        return null;
    }
}