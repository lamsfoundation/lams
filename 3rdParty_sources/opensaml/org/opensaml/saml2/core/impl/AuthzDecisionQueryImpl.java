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

import org.opensaml.saml2.core.Action;
import org.opensaml.saml2.core.AuthzDecisionQuery;
import org.opensaml.saml2.core.Evidence;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.XMLObjectChildrenList;

/**
 * Concrete implementation of {@link org.opensaml.saml2.core.AuthzDecisionQuery}.
 */
public class AuthzDecisionQueryImpl extends SubjectQueryImpl implements AuthzDecisionQuery {

    /** Resource attribute value. */
    private String resource;

    /** Evidence child element. */
    private Evidence evidence;

    /** Action child elements. */
    private final XMLObjectChildrenList<Action> actions;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected AuthzDecisionQueryImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        actions = new XMLObjectChildrenList<Action>(this);
    }

    /** {@inheritDoc} */
    public String getResource() {
        return this.resource;
    }

    /** {@inheritDoc} */
    public void setResource(String newResource) {
        this.resource = prepareForAssignment(this.resource, newResource);
    }

    /** {@inheritDoc} */
    public List<Action> getActions() {
        return actions;
    }

    /** {@inheritDoc} */
    public Evidence getEvidence() {
        return this.evidence;
    }

    /** {@inheritDoc} */
    public void setEvidence(Evidence newEvidence) {
        this.evidence = prepareForAssignment(this.evidence, newEvidence);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();

        if (super.getOrderedChildren() != null) {
            children.addAll(super.getOrderedChildren());
        }
        children.addAll(actions);
        if (evidence != null) {
            children.add(evidence);
        }

        if (children.size() == 0) {
            return null;
        }

        return Collections.unmodifiableList(children);
    }
}