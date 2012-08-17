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

import org.opensaml.xacml.ctx.AttributeValueType;
import org.opensaml.xacml.ctx.MissingAttributeDetailType;
import org.opensaml.xacml.impl.AbstractXACMLObject;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.XMLObjectChildrenList;

/** Concrete implementation of {@link MissingAttributeDetailType}. */
public class MissingAttributeDetailTypeImpl extends AbstractXACMLObject implements MissingAttributeDetailType {

    /** Lists of the attribute values in details. */
    private XMLObjectChildrenList<AttributeValueType> attributeValues;

    /** ID of the attribute. */
    private String attributeId;

    /** Data type of the attribute. */
    private String dataType;

    /** Issuer of the attribute. */
    private String issuer;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected MissingAttributeDetailTypeImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        attributeValues = new XMLObjectChildrenList<AttributeValueType>(this);
    }

    /** {@inheritDoc} */
    public String getAttributeId() {
        return attributeId;
    }

    /** {@inheritDoc} */
    public List<AttributeValueType> getAttributeValues() {
        return attributeValues;
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
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();
        
        children.addAll(attributeValues);
        
        return Collections.unmodifiableList(children);
    }

    /** {@inheritDoc} */
    public void setAttributeId(String id) {
        attributeId = prepareForAssignment(attributeId, id);
    }

    /** {@inheritDoc} */
    public void setDataType(String type) {
        dataType = prepareForAssignment(dataType, type);
    }

    /** {@inheritDoc} */
    public void setIssuer(String issuer) {
        this.issuer = prepareForAssignment(this.issuer, issuer);
    }
}