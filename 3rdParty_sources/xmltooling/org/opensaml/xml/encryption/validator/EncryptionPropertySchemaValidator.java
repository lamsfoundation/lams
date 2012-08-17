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
import org.opensaml.xml.encryption.EncryptionProperty;
import org.opensaml.xml.util.XMLConstants;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;

/**
 * Checks {@link org.opensaml.xml.encryption.EncryptionProperty} for Schema compliance. 
 */
public class EncryptionPropertySchemaValidator implements Validator<EncryptionProperty> {

    /** {@inheritDoc} */
    public void validate(EncryptionProperty xmlObject) throws ValidationException {
        validateUnknownChildren(xmlObject);
        validateChildrenNamespaces(xmlObject);
        validateAttributeNamespaces(xmlObject);
    }

    /**
     * Validate the unknown children.
     * 
     * @param xmlObject the object to validate
     * @throws ValidationException  thrown if the object is invalid
     */
    protected void validateUnknownChildren(EncryptionProperty xmlObject) throws ValidationException {
        if (xmlObject.getUnknownXMLObjects().isEmpty()) {
            throw new ValidationException("No children were present in the EncryptionProperty object");
        }
    }
    
    /**
     * Validate that all children are from another namespace.
     * 
     * @param xmlObject the object to validate
     * @throws ValidationException thrown if the object is invalid
     */
    protected void validateChildrenNamespaces(EncryptionProperty xmlObject) throws ValidationException {
        // Validate that any children are from another namespace.
        for (XMLObject child : xmlObject.getUnknownXMLObjects()) {
            QName childName = child.getElementQName();
            if (XMLConstants.XMLENC_NS.equals(childName.getNamespaceURI())) {
                throw new ValidationException("EncryptionProperty contains an illegal child extension element: " + childName);
            }
        }
    }
    
    /**
     * Validate that any wildcard attributes are from the 
     * XML namespace <code>http://www.w3.org/XML/1998/namespace</code>.
     * 
     * @param xmlObject the object to validate
     * @throws ValidationException thrown if the object is invalid
     */
    protected void validateAttributeNamespaces(EncryptionProperty xmlObject) throws ValidationException {
        // Validate that any extension attribute are from the XML namespace
        for (QName attribName : xmlObject.getUnknownAttributes().keySet()) {
            if (! XMLConstants.XML_NS.equals(attribName.getNamespaceURI())) {
                throw new ValidationException("EncryptionProperty contains an illegal extension attribute: " + attribName);
            }
        }
    }

}
