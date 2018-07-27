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
import java.util.List;

import org.opensaml.xacml.XACMLObject;
import org.opensaml.xacml.impl.AbstractXACMLObject;
import org.opensaml.xacml.policy.AttributeDesignatorType;
import org.opensaml.xacml.policy.AttributeSelectorType;
import org.opensaml.xacml.policy.AttributeValueType;
import org.opensaml.xacml.policy.ResourceMatchType;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.IndexedXMLObjectChildrenList;

/** Concrete implementation of {@link ResourceMatchType}. */
public class ResourceMatchTypeImpl extends AbstractXACMLObject implements ResourceMatchType {

    /** Match's attribute value. */
    private AttributeValueType attributeValue;

    /** Match's choice of attribute elements. */
    private IndexedXMLObjectChildrenList<XACMLObject> attributeChoice;

    /** Gets the ID of this match. */
    private String matchId;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    public ResourceMatchTypeImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        attributeChoice = new IndexedXMLObjectChildrenList<XACMLObject>(this);
    }

    /** {@inheritDoc} */
    public AttributeSelectorType getAttributeSelector() {
        List<XACMLObject> selectors = (List<XACMLObject>) attributeChoice.subList(AttributeSelectorType.DEFAULT_ELEMENT_NAME);
        if (selectors != null && !selectors.isEmpty()) {
            return (AttributeSelectorType) selectors.get(0);
        }

        return null;
    }

    /** {@inheritDoc} */
    public AttributeValueType getAttributeValue() {
        return attributeValue;
    }

    /** {@inheritDoc} */
    public AttributeDesignatorType getResourceAttributeDesignator() {
        List<XACMLObject> selectors = (List<XACMLObject>) attributeChoice
                .subList(AttributeDesignatorType.RESOURCE_ATTRIBUTE_DESIGNATOR_ELEMENT_NAME);
        if (selectors != null && !selectors.isEmpty()) {
            return (AttributeDesignatorType) selectors.get(0);
        }

        return null;
    }

    /** {@inheritDoc} */
    public String getMatchId() {
        return matchId;
    }

    /** {@inheritDoc} */
    public void setAttributeSelector(AttributeSelectorType selector) {
        AttributeSelectorType currentSelector = getAttributeSelector();
        if (currentSelector != null) {
            attributeChoice.remove(currentSelector);
        }

        attributeChoice.add(selector);
    }

    /** {@inheritDoc} */
    public void setAttributeValue(AttributeValueType value) {
        attributeValue = prepareForAssignment(attributeValue, value);
    }

    /** {@inheritDoc} */
    public void setResourceAttributeDesignator(AttributeDesignatorType attribute) {
        AttributeDesignatorType currentDesignator = getResourceAttributeDesignator();
        if (currentDesignator != null) {
            attributeChoice.remove(currentDesignator);
        }

        attributeChoice.add(attribute);
    }

    /** {@inheritDoc} */
    public void setMatchId(String id) {
        matchId = prepareForAssignment(matchId, id);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();
        children.add(attributeValue);
        if (!attributeChoice.isEmpty()) {
            children.addAll(attributeChoice);
        }

        return children;
    }
}