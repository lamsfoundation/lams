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

package org.opensaml.saml1.core.impl;

import org.opensaml.common.impl.AbstractSAMLObjectUnmarshaller;
import org.opensaml.saml1.core.AttributeDesignator;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;
import org.w3c.dom.Attr;

/** Unmarshaller for {@link AttributeDesignator} objects. */
public class AttributeDesignatorUnmarshaller extends AbstractSAMLObjectUnmarshaller {

    /** {@inheritDoc} */
    protected void processAttribute(XMLObject samlObject, Attr attribute) throws UnmarshallingException {

        AttributeDesignator designator = (AttributeDesignator) samlObject;

        if (AttributeDesignator.ATTRIBUTENAME_ATTRIB_NAME.equals(attribute.getLocalName())) {
            designator.setAttributeName(attribute.getValue());
        } else if (AttributeDesignator.ATTRIBUTENAMESPACE_ATTRIB_NAME.equals(attribute.getLocalName())) {
            designator.setAttributeNamespace(attribute.getValue());
        } else {
            super.processAttribute(samlObject, attribute);
        }
    }

}
