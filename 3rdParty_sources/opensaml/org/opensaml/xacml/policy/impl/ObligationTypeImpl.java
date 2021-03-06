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

package org.opensaml.xacml.policy.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opensaml.xacml.impl.AbstractXACMLObject;
import org.opensaml.xacml.policy.AttributeAssignmentType;
import org.opensaml.xacml.policy.EffectType;
import org.opensaml.xacml.policy.ObligationType;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.XMLObjectChildrenList;

/** Implementation for {@link ObligationType}. */
public class ObligationTypeImpl extends AbstractXACMLObject implements ObligationType {

    /** List of the atrributeAssignments in the obligation. */
    private XMLObjectChildrenList<AttributeAssignmentType> attributeAssignments;

    /** The attribute fulfillOn. */
    private EffectType fulFillOn;

    /** Obligation Id. */
    private String obligationId;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected ObligationTypeImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        attributeAssignments = new XMLObjectChildrenList<AttributeAssignmentType>(this);
    }

    /** {@inheritDoc} */
    public List<AttributeAssignmentType> getAttributeAssignments() {
        return attributeAssignments;
    }

    /** {@inheritDoc} */
    public EffectType getFulfillOn() {
        return fulFillOn;
    }

    /** {@inheritDoc} */
    public String getObligationId() {
        return obligationId;
    }

    /** {@inheritDoc} */
    public void setFulfillOn(EffectType newFulfillOn) {
        fulFillOn = prepareForAssignment(this.fulFillOn, newFulfillOn);
    }

    /** {@inheritDoc} */
    public void setObligationId(String newObligationId) {
        obligationId = prepareForAssignment(this.obligationId, newObligationId);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();

        if (!attributeAssignments.isEmpty()) {
            children.addAll(attributeAssignments);
        }
        return Collections.unmodifiableList(children);
    }
}
