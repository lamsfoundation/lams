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

package org.opensaml.ws.wspolicy.impl;

import java.util.List;

import org.opensaml.ws.wspolicy.PolicyReference;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.AttributeMap;

/**
 * PolicyReferenceImpl.
 * 
 */
public class PolicyReferenceImpl extends AbstractWSPolicyObject implements PolicyReference {

    /** URI attribute value. */
    private String uri;

    /** Digest attribute value. */
    private String digest;

    /** DigestAlgorithm attribute value. */
    private String digestAlgorithm;

    /** xs:anyAttribute attributes. */
    private AttributeMap unknownAttributes;

    /**
     * Constructor.
     * 
     * @param namespaceURI The namespace of the element
     * @param elementLocalName The local name of the element
     * @param namespacePrefix The namespace prefix of the element
     */
    public PolicyReferenceImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        unknownAttributes = new AttributeMap(this);
    }

    /** {@inheritDoc} */
    public String getDigest() {
        return digest;
    }

    /** {@inheritDoc} */
    public String getDigestAlgorithm() {
        return digestAlgorithm;
    }

    /** {@inheritDoc} */
    public String getURI() {
        return uri;
    }

    /** {@inheritDoc} */
    public void setDigest(String newDigest) {
        digest = prepareForAssignment(digest, newDigest);
    }

    /** {@inheritDoc} */
    public void setDigestAlgorithm(String newDigestAlgorithm) {
        digestAlgorithm = prepareForAssignment(digestAlgorithm, newDigestAlgorithm);
    }

    /** {@inheritDoc} */
    public void setURI(String newURI) {
        uri = prepareForAssignment(uri, newURI);
    }

    /** {@inheritDoc} */
    public AttributeMap getUnknownAttributes() {
        return unknownAttributes;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        return null;
    }

}
