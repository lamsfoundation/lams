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

package org.opensaml.saml2.metadata.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opensaml.common.impl.AbstractSAMLObject;
import org.opensaml.saml2.metadata.EncryptionMethod;
import org.opensaml.saml2.metadata.KeyDescriptor;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.security.credential.UsageType;
import org.opensaml.xml.signature.KeyInfo;
import org.opensaml.xml.util.XMLObjectChildrenList;

/**
 * Concrete implementation of {@link org.opensaml.saml2.metadata.KeyDescriptor}.
 */
public class KeyDescriptorImpl extends AbstractSAMLObject implements KeyDescriptor {

    /** Key usage type. */
    private UsageType keyUseType;

    /** Key information. */
    private KeyInfo keyInfo;

    /** Encryption methods supported by the entity. */
    private final XMLObjectChildrenList<EncryptionMethod> encryptionMethods;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected KeyDescriptorImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        encryptionMethods = new XMLObjectChildrenList<EncryptionMethod>(this);
        keyUseType = UsageType.UNSPECIFIED;
    }

    /** {@inheritDoc} */
    public UsageType getUse() {
        return keyUseType;
    }

    /** {@inheritDoc} */
    public void setUse(UsageType newType) {
        if (newType != null) {
            keyUseType = prepareForAssignment(keyUseType, newType);
        } else {
            keyUseType = prepareForAssignment(keyUseType, UsageType.UNSPECIFIED);
        }
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
    public List<EncryptionMethod> getEncryptionMethods() {
        return encryptionMethods;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();

        children.add(keyInfo);
        children.addAll(encryptionMethods);

        return Collections.unmodifiableList(children);
    }
}