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

import org.opensaml.xacml.policy.AttributeAssignmentType;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.util.DatatypeHelper;
import org.w3c.dom.Element;

/** Marshaller for {@link AttributeAssignmentType}. */
public class AttributeAssignmentTypeMarshaller extends AttributeValueTypeMarshaller {

    /** Constructor. */
    public AttributeAssignmentTypeMarshaller() {
        super();
    }

    /** {@inheritDoc} */
    protected void marshallAttributes(XMLObject samlElement, Element domElement) throws MarshallingException {
        AttributeAssignmentType attributeAssignment = (AttributeAssignmentType) samlElement;

        if (!DatatypeHelper.isEmpty(attributeAssignment.getAttributeId())) {
            domElement.setAttributeNS(null, AttributeAssignmentType.ATTR_ID_ATTRIB_NAME, attributeAssignment
                    .getAttributeId());
        }
        if(!DatatypeHelper.isEmpty(attributeAssignment.getDataType())){
        	super.marshallAttributes(samlElement, domElement);
        }
        
    }
}