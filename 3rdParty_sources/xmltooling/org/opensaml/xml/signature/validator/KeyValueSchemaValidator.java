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

import java.util.List;

import javax.xml.namespace.QName;

import org.opensaml.xml.XMLObject;
import org.opensaml.xml.signature.KeyValue;
import org.opensaml.xml.util.XMLConstants;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;

/**
 * Checks {@link org.opensaml.xml.signature.KeyValue} for Schema compliance. 
 */
public class KeyValueSchemaValidator implements Validator<KeyValue> {

    /** {@inheritDoc} */
    public void validate(KeyValue xmlObject) throws ValidationException {
        validateChildrenPresence(xmlObject);
        validateExtensionChildNamespace(xmlObject);
    }

    /**
     * Validate that exactly one child is present.
     * 
     * @param xmlObject the object to validate
     * @throws ValidationException  thrown if the object is invalid
     */
    protected void validateChildrenPresence(KeyValue xmlObject) throws ValidationException {
        List<XMLObject> children = xmlObject.getOrderedChildren();
        if (children == null || children.isEmpty()) {
            throw new ValidationException("No children were present in the KeyValue object");
        }
        if (children.size() > 1) {
            throw new ValidationException("Invalid number of children were present in the KeyValue object");
        }
    }
    
    /**
     * Validate that the extension child, if present, is from another namespace.
     * 
     * @param xmlObject the object to validate
     * @throws ValidationException thrown if the object is invalid
     */
    protected void validateExtensionChildNamespace(KeyValue xmlObject) throws ValidationException {
        // Validate that the unknown child is not from the dsig namespace
        // or are from another namespace.
        XMLObject unknownChild = xmlObject.getUnknownXMLObject();
        if (unknownChild == null) {
            return;
        }
        QName childName = unknownChild.getElementQName();
        if (XMLConstants.XMLSIG_NS.equals(childName.getNamespaceURI())) {
            throw new ValidationException("KeyValue contains an illegal child extension element: " + childName);
        }
    }
}
