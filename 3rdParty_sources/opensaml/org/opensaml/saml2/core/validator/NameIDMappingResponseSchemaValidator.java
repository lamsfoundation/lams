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

import org.opensaml.saml2.core.NameIDMappingResponse;
import org.opensaml.xml.validation.ValidationException;

/**
 * Checks {@link org.opensaml.saml2.core.NameIDMappingResponse} for Schema compliance.
 */
public class NameIDMappingResponseSchemaValidator extends StatusResponseTypeSchemaValidator<NameIDMappingResponse> {

    /**
     * Constructor
     *
     */
    public NameIDMappingResponseSchemaValidator() {
        super();
    }

    /** {@inheritDoc} */
    public void validate(NameIDMappingResponse response) throws ValidationException {
        super.validate(response);
        validateIdentifiers(response);
    }

    /**
     * Validate the identifier child elements (NameID, EncryptedID).
     * 
     * @param resp
     * @throws ValidationException 
     */
    protected void validateIdentifiers(NameIDMappingResponse resp) throws ValidationException {
        int idCount = 0;
        
        if (resp.getNameID() != null) {
            idCount++;
        }
        if (resp.getEncryptedID() != null) {
            idCount++;
        }
        
        if (idCount != 1) {
            throw new ValidationException("NameIDMappingResponse must contain exactly one of: NameID, EncryptedID");
        }
    }

}
