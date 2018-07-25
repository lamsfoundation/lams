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

import javax.xml.namespace.QName;

import org.opensaml.common.impl.AbstractSAMLObject;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml1.core.Assertion;
import org.opensaml.saml1.core.AssertionIDReference;
import org.opensaml.saml1.core.Evidence;
import org.opensaml.saml1.core.Evidentiary;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.IndexedXMLObjectChildrenList;

/**
 * Concrete implementation of the {@link org.opensaml.saml1.core.Evidence} interface.
 */
public class EvidenceImpl extends AbstractSAMLObject implements Evidence {

    /** The Evidentiary child elements. */
    private final IndexedXMLObjectChildrenList<Evidentiary> evidence;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected EvidenceImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        evidence = new IndexedXMLObjectChildrenList<Evidentiary>(this);
    }

    /** {@inheritDoc} */
    public List<AssertionIDReference> getAssertionIDReferences() {
        QName qname = new QName(SAMLConstants.SAML1_NS, AssertionIDReference.DEFAULT_ELEMENT_LOCAL_NAME);
        return (List<AssertionIDReference>) evidence.subList(qname);
    }

    /** {@inheritDoc} */
    public List<Assertion> getAssertions() {
        QName qname = new QName(SAMLConstants.SAML1_NS, Assertion.DEFAULT_ELEMENT_LOCAL_NAME);
        return (List<Assertion>) evidence.subList(qname);
    }

    /** {@inheritDoc} */
    public List<Evidentiary> getEvidence() {
        return evidence;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        if (evidence.size() == 0) {
            return null;
        }

        ArrayList<XMLObject> list = new ArrayList<XMLObject>();
        list.addAll(evidence);

        return Collections.unmodifiableList(list);
    }
}