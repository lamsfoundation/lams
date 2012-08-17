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

import org.opensaml.saml1.core.Action;
import org.opensaml.saml1.core.AuthorizationDecisionStatement;
import org.opensaml.saml1.core.DecisionTypeEnumeration;
import org.opensaml.saml1.core.Evidence;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.XMLObjectChildrenList;

/**
 * A concrete implementation of {@link org.opensaml.saml1.core.AuthorizationDecisionStatement}
 */
public class AuthorizationDecisionStatementImpl extends SubjectStatementImpl implements AuthorizationDecisionStatement {

    /** Contains the Resource attribute */
    private String resource;

    /** Contains the Decision attribute */
    private DecisionTypeEnumeration decision;

    /** Contains the list of Action elements */
    private final XMLObjectChildrenList<Action> actions;

    /** Contains the (single) Evidence element */
    private Evidence evidence;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected AuthorizationDecisionStatementImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        actions = new XMLObjectChildrenList<Action>(this);
    }

    /** {@inheritDoc} */
    public String getResource() {
        return resource;
    }

    /** {@inheritDoc} */
    public void setResource(String resource) {
        this.resource = prepareForAssignment(this.resource, resource);
    }

    /** {@inheritDoc} */
    public DecisionTypeEnumeration getDecision() {
        return decision;
    }

    /** {@inheritDoc} */
    public void setDecision(DecisionTypeEnumeration decision) {
        this.decision = prepareForAssignment(this.decision, decision);
    }

    /** {@inheritDoc} */
    public List<Action> getActions() {
        return actions;
    }

    /** {@inheritDoc} */
    public Evidence getEvidence() {
        return evidence;
    }

    /** {@inheritDoc} */
    public void setEvidence(Evidence evidence) throws IllegalArgumentException {
        this.evidence = prepareForAssignment(this.evidence, evidence);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        List<XMLObject> list = new ArrayList<XMLObject>(actions.size() + 2);

        if (super.getOrderedChildren() != null) {
            list.addAll(super.getOrderedChildren());
        }
        list.addAll(actions);
        if (evidence != null) {
            list.add(evidence);
        }
        if (list.size() == 0) {
            return null;
        }
        return Collections.unmodifiableList(list);
    }
}