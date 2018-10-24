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

import javax.xml.namespace.QName;

import org.opensaml.common.impl.AbstractSAMLObject;
import org.opensaml.saml2.core.Advice;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.AssertionIDRef;
import org.opensaml.saml2.core.AssertionURIRef;
import org.opensaml.saml2.core.EncryptedAssertion;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.IndexedXMLObjectChildrenList;

/**
 * A concrete implementation of {@link org.opensaml.saml2.core.Advice}.
 */
public class AdviceImpl extends AbstractSAMLObject implements Advice {

    /** Children. */
    private final IndexedXMLObjectChildrenList<XMLObject> indexedChildren;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected AdviceImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        indexedChildren = new IndexedXMLObjectChildrenList<XMLObject>(this);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getChildren() {
        return indexedChildren;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getChildren(QName typeOrName) {
        return (List<XMLObject>) indexedChildren.subList(typeOrName);
    }

    /** {@inheritDoc} */
    public List<AssertionIDRef> getAssertionIDReferences() {
        return (List<AssertionIDRef>) indexedChildren.subList(AssertionIDRef.DEFAULT_ELEMENT_NAME);
    }

    /** {@inheritDoc} */
    public List<AssertionURIRef> getAssertionURIReferences() {
        return (List<AssertionURIRef>) indexedChildren.subList(AssertionURIRef.DEFAULT_ELEMENT_NAME);
    }

    /** {@inheritDoc} */
    public List<Assertion> getAssertions() {
        return (List<Assertion>) indexedChildren.subList(Assertion.DEFAULT_ELEMENT_NAME);
    }

    /** {@inheritDoc} */
    public List<EncryptedAssertion> getEncryptedAssertions() {
        return (List<EncryptedAssertion>) indexedChildren.subList(EncryptedAssertion.DEFAULT_ELEMENT_NAME);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();

        children.addAll(indexedChildren);

        return Collections.unmodifiableList(children);
    }
}