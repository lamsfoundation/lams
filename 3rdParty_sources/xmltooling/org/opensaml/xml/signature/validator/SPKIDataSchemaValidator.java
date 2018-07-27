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

package org.opensaml.xml.signature.validator;

import javax.xml.namespace.QName;

import org.opensaml.xml.XMLObject;
import org.opensaml.xml.signature.SPKIData;
import org.opensaml.xml.signature.SPKISexp;
import org.opensaml.xml.util.XMLConstants;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;

/**
 * Checks {@link org.opensaml.xml.signature.SPKIData} for Schema compliance. 
 */
public class SPKIDataSchemaValidator implements Validator<SPKIData> {

    /** {@inheritDoc} */
    public void validate(SPKIData xmlObject) throws ValidationException {
        validateChildrenPresence(xmlObject);
        validateChildrenNamespaces(xmlObject);
    }

    /**
     * Validate that at least SPKISexp child is present.
     * 
     * @param xmlObject the object to validate
     * @throws ValidationException  thrown if the object is invalid
     */
    protected void validateChildrenPresence(SPKIData xmlObject) throws ValidationException {
        if (xmlObject.getSPKISexps().isEmpty()) {
            throw new ValidationException("SPKIData does not contain at least one SPKISexp child");
        }
    }
    
    /**
     * Validate that all children are either ones defined within the XML Signature schema,
     * or are from another namespace.
     * 
     * @param xmlObject the object to validate
     * @throws ValidationException thrown if the object is invalid
     */
    protected void validateChildrenNamespaces(SPKIData xmlObject) throws ValidationException {
        // Validate that any children are either the ones from the dsig schema,
        // or are from another namespace.
        for (XMLObject child : xmlObject.getXMLObjects()) {
            QName childName = child.getElementQName();
            if (! SPKISexp.DEFAULT_ELEMENT_NAME.equals(childName) 
                    && XMLConstants.XMLSIG_NS.equals(childName.getNamespaceURI())) {
                throw new ValidationException("PGPData contains an illegal child extension element: " + childName);
            }
        }
    }
    
}
