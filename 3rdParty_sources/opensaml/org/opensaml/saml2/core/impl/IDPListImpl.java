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
import org.opensaml.saml2.core.GetComplete;
import org.opensaml.saml2.core.IDPEntry;
import org.opensaml.saml2.core.IDPList;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.XMLObjectChildrenList;

/**
 * Concrete implementation of {@link org.opensaml.saml2.core.IDPList}.
 */
public class IDPListImpl extends AbstractSAMLObject implements IDPList {

    /** List of IDPEntry's. */
    private final XMLObjectChildrenList<IDPEntry> idpEntries;

    /** GetComplete child element. */
    private GetComplete getComplete;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected IDPListImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        idpEntries = new XMLObjectChildrenList<IDPEntry>(this);
    }

    /** {@inheritDoc} */
    public List<IDPEntry> getIDPEntrys() {
        return idpEntries;
    }

    /** {@inheritDoc} */
    public GetComplete getGetComplete() {
        return getComplete;
    }

    /** {@inheritDoc} */
    public void setGetComplete(GetComplete newGetComplete) {
        this.getComplete = prepareForAssignment(this.getComplete, newGetComplete);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();
        children.addAll(idpEntries);
        children.add(getComplete);
        if (children.size() > 0) {
            return Collections.unmodifiableList(children);
        } else {
            return null;
        }
    }
}