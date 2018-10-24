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

package org.opensaml.xml.encryption.validator;

import javax.xml.namespace.QName;

import org.opensaml.xml.XMLObject;
import org.opensaml.xml.encryption.ReferenceType;
import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.util.XMLConstants;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;

/**
 * Checks {@link org.opensaml.xml.encryption.ReferenceType} for Schema compliance. 
 */
public class ReferenceTypeSchemaValidator implements Validator<ReferenceType> {

    /** {@inheritDoc} */
    public void validate(ReferenceType xmlObject) throws ValidationException {
        validateURI(xmlObject);
        validateChildrenNamespaces(xmlObject);
    }

    /**
     * Validate the URI.
     * 
     * @param xmlObject the object to validate
     * @throws ValidationException  thrown if the object is invalid
     */
    protected void validateURI(ReferenceType xmlObject) throws ValidationException {
        if (DatatypeHelper.isEmpty(xmlObject.getURI())) {
            throw new ValidationException("ReferenceType URI was empty");
        }
    }
    
    /**
     * Validate that all children are from another namespace.
     * 
     * @param xmlObject the object to validate
     * @throws ValidationException thrown if the object is invalid
     */
    protected void validateChildrenNamespaces(ReferenceType xmlObject) throws ValidationException {
        // Validate that any children are from another namespace.
        for (XMLObject child : xmlObject.getUnknownXMLObjects()) {
            QName childName = child.getElementQName();
            if (XMLConstants.XMLENC_NS.equals(childName.getNamespaceURI())) {
                throw new ValidationException("ReferenceType contains an illegal child extension element: " + childName);
            }
        }
    }

}
