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
import org.opensaml.xacml.policy.ApplyType;
import org.opensaml.xacml.policy.ExpressionType;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.util.DatatypeHelper;
import org.w3c.dom.Attr;

/**
 * Unmarshaller for {@link ApplyType}.
 */
public class ApplyTypeUnmarshaller extends AbstractXACMLObjectUnmarshaller {

    /** Constructor. */
    public ApplyTypeUnmarshaller() {
        super();
    }

    /** {@inheritDoc} */
    protected void processAttribute(XMLObject xmlObject, Attr attribute) throws UnmarshallingException {

        if (attribute.getLocalName().equals(ApplyType.FUNCTION_ID_ATTRIB_NAME)) {
            ApplyType applyType = (ApplyType) xmlObject;
            applyType.setFunctionId(DatatypeHelper.safeTrimOrNullString(attribute.getValue()));
        } else {
            super.processAttribute(xmlObject, attribute);
        }

    }

    /** {@inheritDoc} */
    protected void processChildElement(XMLObject parentXMLObject, XMLObject childXMLObject)
            throws UnmarshallingException {
        ApplyType applayType = (ApplyType) parentXMLObject;
        if (childXMLObject instanceof ExpressionType) {
            ExpressionType expression = (ExpressionType) childXMLObject;
            applayType.getExpressions().add(expression);
        } else {
            super.processChildElement(parentXMLObject, childXMLObject);
        }
    }

}
