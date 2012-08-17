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

import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;

import org.opensaml.xml.XMLObject;
import org.opensaml.xml.signature.KeyInfoType;
import org.opensaml.xml.signature.KeyName;
import org.opensaml.xml.signature.KeyValue;
import org.opensaml.xml.signature.MgmtData;
import org.opensaml.xml.signature.PGPData;
import org.opensaml.xml.signature.RetrievalMethod;
import org.opensaml.xml.signature.SPKIData;
import org.opensaml.xml.signature.X509Data;
import org.opensaml.xml.util.XMLConstants;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;

/**
 * Checks {@link org.opensaml.xml.signature.KeyInfoType} for Schema compliance. 
 */
public class KeyInfoTypeSchemaValidator implements Validator<KeyInfoType> {
    
    /** QNames corresponding to the valid KeyInfo children. */
    private static final Set<QName> VALID_DS_CHILD_NAMES;

    /** {@inheritDoc} */
    public void validate(KeyInfoType xmlObject) throws ValidationException {
        validateChildrenPresence(xmlObject);
        validateChildrenNamespaces(xmlObject);
    }
    
    /**
     * Get the QNames corresponding to the valid KeyInfo children
     * defined in the XML Signature namespace.
     * 
     * @return list of valid child QNames
     */
    protected static Set<QName> getValidDSChildNames() {
        return VALID_DS_CHILD_NAMES;
    }

    /**
     * Validate that at least child is present.
     * 
     * @param xmlObject the object to validate
     * @throws ValidationException  thrown if the object is invalid
     */
    protected void validateChildrenPresence(KeyInfoType xmlObject) throws ValidationException {
        if (xmlObject.getXMLObjects().isEmpty()) {
            throw new ValidationException("No children were present in the KeyInfoType object");
        }
    }
    
    /**
     * Validate that all children are either ones defined within the XML Signature schema,
     * or are from another namespace.
     * 
     * @param xmlObject the object to validate
     * @throws ValidationException thrown if the object is invalid
     */
    protected void validateChildrenNamespaces(KeyInfoType xmlObject) throws ValidationException {
        // Validate that any children are either the ones from the dsig schema,
        // or are from another namespace.
        for (XMLObject child : xmlObject.getXMLObjects()) {
            QName childName = child.getElementQName();
            if (! getValidDSChildNames().contains(childName) 
                    && XMLConstants.XMLSIG_NS.equals(childName.getNamespaceURI())) {
                throw new ValidationException("KeyInfoType contains an illegal child extension element: " + childName);
            }
        }
    }
    
    static {
        VALID_DS_CHILD_NAMES = new HashSet<QName>(10);
        VALID_DS_CHILD_NAMES.add(KeyName.DEFAULT_ELEMENT_NAME);
        VALID_DS_CHILD_NAMES.add(KeyValue.DEFAULT_ELEMENT_NAME);
        VALID_DS_CHILD_NAMES.add(RetrievalMethod.DEFAULT_ELEMENT_NAME);
        VALID_DS_CHILD_NAMES.add(X509Data.DEFAULT_ELEMENT_NAME);
        VALID_DS_CHILD_NAMES.add(PGPData.DEFAULT_ELEMENT_NAME);
        VALID_DS_CHILD_NAMES.add(SPKIData.DEFAULT_ELEMENT_NAME);
        VALID_DS_CHILD_NAMES.add(MgmtData.DEFAULT_ELEMENT_NAME);
    }
}
