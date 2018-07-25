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

package org.opensaml.saml1.core.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opensaml.common.impl.AbstractSAMLObject;
import org.opensaml.saml1.core.ConfirmationMethod;
import org.opensaml.saml1.core.SubjectConfirmation;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.signature.KeyInfo;
import org.opensaml.xml.util.XMLObjectChildrenList;

/**
 * Concrete implementation of a <code> SubjectConfirmation </code> object
 */
public class SubjectConfirmationImpl extends AbstractSAMLObject implements SubjectConfirmation {

    /** Contains the list of ConfirmationMethods */
    private final XMLObjectChildrenList<ConfirmationMethod> confirmationMethods;

    /** Contains the SubjectConfirmationData element */
    private XMLObject subjectConfirmationData;

    /** Contains the KeyInfo element */
    private KeyInfo keyInfo;

    /**
     * Constructor
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected SubjectConfirmationImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        confirmationMethods = new XMLObjectChildrenList<ConfirmationMethod>(this);
    }

    /** {@inheritDoc} */
    public List<ConfirmationMethod> getConfirmationMethods() {
        return confirmationMethods;
    }

    /** {@inheritDoc} */
    public void setSubjectConfirmationData(XMLObject subjectConfirmationData)
            throws IllegalArgumentException {

        this.subjectConfirmationData = prepareForAssignment(this.subjectConfirmationData, subjectConfirmationData);
    }

    /** {@inheritDoc} */
    public XMLObject getSubjectConfirmationData() {
        return subjectConfirmationData;
    }

    /** {@inheritDoc} */
    public KeyInfo getKeyInfo() {
        return keyInfo;
    }

    /** {@inheritDoc} */
    public void setKeyInfo(KeyInfo keyInfo) {
        this.keyInfo = prepareForAssignment(this.keyInfo, keyInfo);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {

        List<XMLObject> list = new ArrayList<XMLObject>(confirmationMethods.size() + 1);

        list.addAll(confirmationMethods);

        if (subjectConfirmationData != null) {
            list.add(subjectConfirmationData);
        }

        if(keyInfo != null){
            list.add(keyInfo);
        }

        if (list.size() == 0) {
            return null;
        }
        return Collections.unmodifiableList(list);
    }
}