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
import org.opensaml.saml1.core.Advice;
import org.opensaml.saml1.core.Assertion;
import org.opensaml.saml1.core.AssertionIDReference;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.IndexedXMLObjectChildrenList;

/**
 * Concrete Implementation of the {@link org.opensaml.saml1.core.Advice} Object
 */
public class AdviceImpl extends AbstractSAMLObject implements Advice {

    /** Contains all the SAML objects we have added */
    private final IndexedXMLObjectChildrenList<XMLObject> assertionChildren;
    
    /** "any" children */
    private final IndexedXMLObjectChildrenList<XMLObject> unknownChildren;

    /**
     * Constructor
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected AdviceImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        assertionChildren = new IndexedXMLObjectChildrenList<XMLObject>(this);
        unknownChildren = new IndexedXMLObjectChildrenList<XMLObject>(this);
    }
    
    /** {@inheritDoc} */
    public List<AssertionIDReference> getAssertionIDReferences() {
        //
        // The cast in the line below is unsafe. (it's checking against the erasure of l - which is List.
        // We are, howeverever guaranteed by sublist that although l is 'just' a List it
        // will only contain <AssertionIDReferences> explicit code in IndexedXMLObjectChildrenList$ListView.indexCheck
        // helps us be sure.

        QName assertionIDRefQName = new QName(SAMLConstants.SAML1_NS, AssertionIDReference.DEFAULT_ELEMENT_LOCAL_NAME);
        return (List<AssertionIDReference>) assertionChildren.subList(assertionIDRefQName);
    }

    /** {@inheritDoc} */
    public List<Assertion> getAssertions() {
        // See Comment for getAssertionIDReference as to why this unsafe casting is OK
        QName assertionQname = new QName(SAMLConstants.SAML1_NS, Assertion.DEFAULT_ELEMENT_LOCAL_NAME);
        return (List<Assertion>) assertionChildren.subList(assertionQname);
    }
    
    /**
     * {@inheritDoc}
     */
    public List<XMLObject> getUnknownXMLObjects() {
        return unknownChildren;
    }
    
    /** {@inheritDoc} */
    public List<XMLObject> getUnknownXMLObjects(QName typeOrName) {
        return (List<XMLObject>) unknownChildren.subList(typeOrName);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();
        
        children.addAll(assertionChildren);
        children.addAll(unknownChildren);
        
        return Collections.unmodifiableList(children);
    }
}