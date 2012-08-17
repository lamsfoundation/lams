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

import org.opensaml.xacml.impl.AbstractXACMLObjectUnmarshaller;
import org.opensaml.xacml.policy.AttributeDesignatorType;
import org.opensaml.xacml.policy.AttributeSelectorType;
import org.opensaml.xacml.policy.AttributeValueType;
import org.opensaml.xacml.policy.SubjectMatchType;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.util.DatatypeHelper;
import org.w3c.dom.Attr;

/** Unmarshaller of {@link SubjectMatchType} objects. */
public class SubjectMatchTypeUnmarshaller extends AbstractXACMLObjectUnmarshaller {

    /** Constructor. */
    public SubjectMatchTypeUnmarshaller() {
        super();
    }

    /** {@inheritDoc} */
    protected void processAttribute(XMLObject xmlObject, Attr attribute) throws UnmarshallingException {
        if (attribute.getLocalName().equals(SubjectMatchType.MATCH_ID_ATTRIB_NAME)) {
            SubjectMatchType matchType = (SubjectMatchType) xmlObject;
            matchType.setMatchId(DatatypeHelper.safeTrimOrNullString(attribute.getValue()));
        } else {
            super.processAttribute(xmlObject, attribute);
        }
    }

    /** {@inheritDoc} */
    protected void processChildElement(XMLObject parentXMLObject, XMLObject childXMLObject)
            throws UnmarshallingException {
        SubjectMatchType matchType = (SubjectMatchType) parentXMLObject;
    
        if (childXMLObject instanceof AttributeValueType) {
            matchType.setAttributeValue((AttributeValueType) childXMLObject);
        } else if (childXMLObject.getElementQName().equals(
                AttributeDesignatorType.SUBJECT_ATTRIBUTE_DESIGNATOR_ELEMENT_NAME)) {
            matchType.setSubjectAttributeDesignator((AttributeDesignatorType) childXMLObject);
        } else if (childXMLObject instanceof AttributeSelectorType) {
            matchType.setAttributeSelector((AttributeSelectorType) childXMLObject);
        } else {
            super.processChildElement(parentXMLObject, childXMLObject);
        }
    }

}