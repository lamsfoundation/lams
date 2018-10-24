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
import org.opensaml.xacml.policy.AttributeValueType;
import org.opensaml.xacml.policy.CombinerParameterType;
import org.opensaml.xml.XMLObject;

/**
 *Implementation of {@link CombinerParameterType}.
 */
public class CombinerParameterTypeImpl extends AbstractXACMLObject implements CombinerParameterType {

    /**Parameter name. */
    private String name;
    
    /**Values.*/
    private AttributeValueType value;
    
    /**
     * Constructor.
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected CombinerParameterTypeImpl(String namespaceURI, String elementLocalName, String namespacePrefix){
        super(namespaceURI,elementLocalName,namespacePrefix);
    }
    
    /** {@inheritDoc} */
    public AttributeValueType getAttributeValue() {
        return value;
    }

    /** {@inheritDoc} */
    public String getParameterName() {
        return name;
    }

    /** {@inheritDoc} */
    public void setAttributeValue(AttributeValueType newValue) {
        this.value = prepareForAssignment(this.value,newValue);
    }

    /** {@inheritDoc} */
    public void setParameterName(String newName){
        this.name = prepareForAssignment(this.name,newName);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();        
        
        if(value != null){
            children.add(value);
        }
                       
        return Collections.unmodifiableList(children);
    }

}
