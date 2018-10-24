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

import org.opensaml.saml2.core.AssertionIDRef;
import org.opensaml.saml2.core.AssertionIDRequest;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.XMLObjectChildrenList;

/**
 * A concrete implementation of {@link org.opensaml.saml2.core.AssertionIDRequest}.
 */
public class AssertionIDRequestImpl extends RequestAbstractTypeImpl implements AssertionIDRequest {

    /** List of AssertionIDRef child elements. */
    private final XMLObjectChildrenList<AssertionIDRef> assertionIDRefs;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected AssertionIDRequestImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        assertionIDRefs = new XMLObjectChildrenList<AssertionIDRef>(this);
    }

    /** {@inheritDoc} */
    public List<AssertionIDRef> getAssertionIDRefs() {
        return assertionIDRefs;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();

        if (super.getOrderedChildren() != null) {
            children.addAll(super.getOrderedChildren());
        }
        children.addAll(assertionIDRefs);

        if (children.size() == 0) {
            return null;
        }

        return Collections.unmodifiableList(children);
    }
}