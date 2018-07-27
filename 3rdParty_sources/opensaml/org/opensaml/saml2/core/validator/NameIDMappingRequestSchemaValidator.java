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

/**
 * 
 */
package org.opensaml.saml2.core.validator;

import org.opensaml.saml2.core.NameIDMappingRequest;
import org.opensaml.xml.validation.ValidationException;

/**
 * Checks {@link org.opensaml.saml2.core.NameIDMappingRequest} for Schema compliance.
 */
public class NameIDMappingRequestSchemaValidator extends RequestAbstractTypeSchemaValidator<NameIDMappingRequest> {

    /**
     * Constructor
     *
     */
    public NameIDMappingRequestSchemaValidator() {
        super();
    }

    /** {@inheritDoc} */
    public void validate(NameIDMappingRequest request) throws ValidationException {
        super.validate(request);
        validateIdentifiers(request);
        validateNameIDPolicy(request);
    }

    /**
     * Validates the identifier child types (BaseID, NameID, EncryptedID).
     * 
     * @param request
     * @throws ValidationException 
     */
    protected void validateIdentifiers(NameIDMappingRequest request) throws ValidationException {
        int idCount = 0;
        
        if (request.getBaseID() != null) {
            idCount++;
        }
        if (request.getNameID() != null) {
            idCount++;
        }
        if (request.getEncryptedID() != null) {
            idCount++;
        }
        
        if (idCount != 1) {
            throw new ValidationException("NameIDMappingRequest must contain exactly one of: BaseID, NameID, EncryptedID");
        }
    }
    
    /**
     * Validates the NameIDPolicy child element.
     * 
     * @param request
     * @throws ValidationException 
     */
    private void validateNameIDPolicy(NameIDMappingRequest request) throws ValidationException {
        if(request.getNameIDPolicy() == null) {
            throw new ValidationException("NameIDPolicy is required");
        }
    }
}
