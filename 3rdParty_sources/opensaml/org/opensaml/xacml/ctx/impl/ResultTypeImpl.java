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

package org.opensaml.xacml.ctx.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opensaml.xacml.ctx.DecisionType;
import org.opensaml.xacml.ctx.ResultType;
import org.opensaml.xacml.ctx.StatusType;
import org.opensaml.xacml.impl.AbstractXACMLObject;
import org.opensaml.xacml.policy.ObligationsType;
import org.opensaml.xml.XMLObject;

/** Concrete implementation of {@link ResultType}. */
public class ResultTypeImpl extends AbstractXACMLObject implements ResultType {

    /** Attribute resource id. */
    private String resourceId;

    /** The decision of the result. */
    private DecisionType decision;

    /** List of the status of this result. */
    private StatusType status;

    /** The obligations in this Result. */
    private ObligationsType obligations;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected ResultTypeImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }

    /** {@inheritDoc} */
    public DecisionType getDecision() {
        return decision;
    }

    /** {@inheritDoc} */
    public ObligationsType getObligations() {
        return obligations;
    }

    /** {@inheritDoc} */
    public void setObligations(ObligationsType obligationsIn) {
        this.obligations = prepareForAssignment(this.obligations, obligationsIn);
    }

    /** {@inheritDoc} */
    public String getResourceId() {
        return resourceId;
    }

    /** {@inheritDoc} */
    public StatusType getStatus() {
        return status;
    }

    /** {@inheritDoc} */
    public void setStatus(StatusType statusIn) {
        this.status = prepareForAssignment(this.status, statusIn);
    }

    /** {@inheritDoc} */
    public void setDecision(DecisionType decisionIn) {
        this.decision = prepareForAssignment(this.decision, decisionIn);
    }

    /** {@inheritDoc} */
    public void setResourceId(String newResourceId) {
        resourceId = prepareForAssignment(this.resourceId, newResourceId);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();

        if (decision != null) {
            children.add(decision);
        }

        if (status != null) {
            children.add(status);
        }

        if (obligations != null) {
            children.add(obligations);
        }
        return Collections.unmodifiableList(children);
    }
}
