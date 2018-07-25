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

/**
 * 
 */

package org.opensaml.saml2.core.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opensaml.common.impl.AbstractSAMLObject;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.AssertionIDRef;
import org.opensaml.saml2.core.AssertionURIRef;
import org.opensaml.saml2.core.EncryptedAssertion;
import org.opensaml.saml2.core.Evidence;
import org.opensaml.saml2.core.Evidentiary;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.IndexedXMLObjectChildrenList;

/**
 * A concrete implementation of {@link org.opensaml.saml2.core.Evidence}.
 */
public class EvidenceImpl extends AbstractSAMLObject implements Evidence {

    /** Assertion of the Evidence. */
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
    public List<Evidentiary> getEvidence() {
        return evidence;
    }

    /** {@inheritDoc} */
    public List<AssertionIDRef> getAssertionIDReferences() {
        return (List<AssertionIDRef>) evidence.subList(AssertionIDRef.DEFAULT_ELEMENT_NAME);
    }

    /** {@inheritDoc} */
    public List<AssertionURIRef> getAssertionURIReferences() {
        return (List<AssertionURIRef>) evidence.subList(AssertionURIRef.DEFAULT_ELEMENT_NAME);
    }

    /** {@inheritDoc} */
    public List<Assertion> getAssertions() {
        return (List<Assertion>) evidence.subList(Assertion.DEFAULT_ELEMENT_NAME);
    }

    /** {@inheritDoc} */
    public List<EncryptedAssertion> getEncryptedAssertions() {
        return (List<EncryptedAssertion>) evidence.subList(EncryptedAssertion.DEFAULT_ELEMENT_NAME);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();

        if (evidence.size() == 0) {
            return null;
        }

        children.addAll(evidence);

        return Collections.unmodifiableList(children);
    }
}