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

import java.util.List;

import org.opensaml.xacml.impl.AbstractXACMLObject;
import org.opensaml.xacml.policy.AttributeDesignatorType;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.schema.XSBooleanValue;
import org.opensaml.xml.util.LazyList;

/**
 * Implementation of {@link AttributeDesignatorType}.
 */
public class AttributeDesignatorTypeImpl extends AbstractXACMLObject implements AttributeDesignatorType {

    /** Attribute Id. */
    private String attributeId;

    /** Datatype. */
    private String dataType;

    /** Issuer. */
    private String issuer;

    /** Must be present. */
    private XSBooleanValue mustBePresentXS = null;

    /**
     * Constructor.
     * 
     * @param namespaceURI
     *                the namespace the element is in
     * @param elementLocalName
     *                the local name of the XML element this Object represents
     * @param namespacePrefix
     *                the prefix for the given namespace
     */
    protected AttributeDesignatorTypeImpl(String namespaceURI,
	    String elementLocalName, String namespacePrefix) {
	super(namespaceURI, elementLocalName, namespacePrefix);
	mustBePresentXS = XSBooleanValue.valueOf("false");
    }

    /** {@inheritDoc} */
    public String getAttributeId() {
	return attributeId;
    }

    /** {@inheritDoc} */
    public String getDataType() {
	return dataType;
    }

    /** {@inheritDoc} */
    public String getIssuer() {
	return issuer;
    }

    /** {@inheritDoc} */
    public XSBooleanValue getMustBePresentXSBoolean() {
	return mustBePresentXS;
    }

    /** {@inheritDoc} */
    public Boolean getMustBePresent() {
	if (mustBePresentXS != null) {
	    return mustBePresentXS.getValue();
	}
	return Boolean.FALSE;
    }

    /** {@inheritDoc} */
    public void setAttribtueId(String id) {
	this.attributeId = prepareForAssignment(this.attributeId, id);
    }

    /** {@inheritDoc} */
    public void setDataType(String type) {
	this.dataType = prepareForAssignment(this.dataType, type);
    }

    /** {@inheritDoc} */
    public void setIssuer(String newIssuer) {
	this.issuer = prepareForAssignment(this.issuer, newIssuer);
    }

    /** {@inheritDoc} */
    public void setMustBePresentXSBoolean(XSBooleanValue present) {
	this.mustBePresentXS = prepareForAssignment(this.mustBePresentXS,
		present);
    }

    /** {@inheritDoc} */
    public void setMustBePresent(Boolean present) {
	if (present != null) {
	    this.mustBePresentXS = prepareForAssignment(this.mustBePresentXS,
		    new XSBooleanValue(present, false));
	} else {
	    this.mustBePresentXS = prepareForAssignment(this.mustBePresentXS,
		    null);
	}

    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        return new LazyList<XMLObject>();
    }

}
