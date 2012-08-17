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

import org.opensaml.xacml.ctx.AttributeType;
import org.opensaml.xacml.ctx.AttributeValueType;
import org.opensaml.xacml.impl.AbstractXACMLObject;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.XMLObjectChildrenList;

/** Concrete implementation of {@link AttributeType}. */
public class AttributeTypeImpl extends AbstractXACMLObject implements AttributeType {

    /** Issuer of the attribute. */
    private String issuer;

    /** AttributeID of the attribute. */
    private String attributeID;

    /** Datatype of the attribute. */
    private String datatype;

    /** List of values for this attribute. */
    private final XMLObjectChildrenList<AttributeValueType> attributeValues;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected AttributeTypeImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        attributeValues = new XMLObjectChildrenList<AttributeValueType>(this);
    }

    /** {@inheritDoc} */
    public String getAttributeID() {
        return attributeID;
    }

    /** {@inheritDoc} */
    public String getDataType() {
        return datatype;
    }

    /** {@inheritDoc} */
    public String getIssuer() {
        return issuer;
    }

    /** {@inheritDoc} */
    public void setAttributeID(String attributeId) {
        this.attributeID = prepareForAssignment(this.attributeID, attributeId);
    }

    /** {@inheritDoc} */
    public void setDataType(String datatype) {
        this.datatype = prepareForAssignment(this.datatype, datatype);
    }

    /** {@inheritDoc} */
    public void setIssuer(String issuer) {
        this.issuer = prepareForAssignment(this.issuer, issuer);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();

        children.addAll(attributeValues);

        return Collections.unmodifiableList(children);
    }

    /** {@inheritDoc} */
    public List<AttributeValueType> getAttributeValues() {
        return attributeValues;
    }
}
