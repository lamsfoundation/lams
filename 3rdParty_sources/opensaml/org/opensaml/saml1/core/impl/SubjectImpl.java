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
import org.opensaml.saml1.core.NameIdentifier;
import org.opensaml.saml1.core.Subject;
import org.opensaml.saml1.core.SubjectConfirmation;
import org.opensaml.xml.XMLObject;

/**
 * Complete implementation of {@link org.opensaml.saml1.core.Subject}
 */
public class SubjectImpl extends AbstractSAMLObject implements Subject {

    /** Contains the NameIdentifier inside the Subject */
    private NameIdentifier nameIdentifier;

    /** Contains the SubjectConfirmation inside the Subject */
    private SubjectConfirmation subjectConfirmation;

    /**
     * Constructor
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected SubjectImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }

    /** {@inheritDoc} */
    public NameIdentifier getNameIdentifier() {
        return nameIdentifier;
    }

    /** {@inheritDoc} */
    public void setNameIdentifier(NameIdentifier nameIdentifier) throws IllegalArgumentException {
        this.nameIdentifier = prepareForAssignment(this.nameIdentifier, nameIdentifier);
    }

    /** {@inheritDoc} */
    public SubjectConfirmation getSubjectConfirmation() {
        return subjectConfirmation;
    }

    /** {@inheritDoc} */
    public void setSubjectConfirmation(SubjectConfirmation subjectConfirmation) throws IllegalArgumentException {
        this.subjectConfirmation = prepareForAssignment(this.subjectConfirmation, subjectConfirmation);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {

        List<XMLObject> list = new ArrayList<XMLObject>(2);

        if (nameIdentifier != null) {
            list.add(nameIdentifier);
        }

        if (subjectConfirmation != null) {
            list.add(subjectConfirmation);
        }
        if (list.size() == 0) {
            return null;
        }

        return Collections.unmodifiableList(list);
    }
}