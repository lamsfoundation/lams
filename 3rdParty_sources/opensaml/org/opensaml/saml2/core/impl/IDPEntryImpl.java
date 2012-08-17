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

import java.util.List;

import org.opensaml.common.impl.AbstractSAMLObject;
import org.opensaml.saml2.core.IDPEntry;
import org.opensaml.xml.XMLObject;

/**
 * Concrete implementation of {@link org.opensaml.saml2.core.IDPEntry}.
 */
public class IDPEntryImpl extends AbstractSAMLObject implements IDPEntry {

    /** The unique identifier of the IdP. */
    private String providerID;

    /** Human-readable name for the IdP. */
    private String name;

    /**
     * URI reference representing the location of a profile-specific endpoint supporting the authentication request
     * protocol.
     */
    private String loc;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected IDPEntryImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }

    /** {@inheritDoc} */
    public String getProviderID() {
        return this.providerID;
    }

    /** {@inheritDoc} */
    public void setProviderID(String newProviderID) {
        this.providerID = prepareForAssignment(this.providerID, newProviderID);

    }

    /** {@inheritDoc} */
    public String getName() {
        return this.name;
    }

    /** {@inheritDoc} */
    public void setName(String newName) {
        this.name = prepareForAssignment(this.name, newName);

    }

    /** {@inheritDoc} */
    public String getLoc() {
        return this.loc;
    }

    /** {@inheritDoc} */
    public void setLoc(String newLoc) {
        this.loc = prepareForAssignment(this.loc, newLoc);

    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        // no children
        return null;
    }
}