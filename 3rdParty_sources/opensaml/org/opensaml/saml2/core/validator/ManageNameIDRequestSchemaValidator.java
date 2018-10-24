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

package org.opensaml.saml2.core.validator;

import org.opensaml.saml2.core.ManageNameIDRequest;
import org.opensaml.xml.validation.ValidationException;

/**
 * Checks {@link org.opensaml.saml2.core.ManageNameIDRequest} for Schema compliance.
 */
public class ManageNameIDRequestSchemaValidator extends RequestAbstractTypeSchemaValidator<ManageNameIDRequest> {

    /**
     * Constructor
     *
     */
    public ManageNameIDRequestSchemaValidator() {
        super();
    }

    /** {@inheritDoc} */
    public void validate(ManageNameIDRequest request) throws ValidationException {
        super.validate(request);
        validateNameID(request);
        validateNewIDAndTerminate(request);
    }

    /**
     * Validates NameID/EncryptedID child element
     * 
     * @param request
     * @throws ValidationException 
     */
    protected void validateNameID(ManageNameIDRequest request) throws ValidationException {
        int idCount = 0;
        
        if (request.getNameID() != null) {
            idCount++;
        }
        if (request.getEncryptedID() != null) {
            idCount++;
        }
        
        if (idCount != 1) {
            throw new ValidationException("ManageNameIDRequest must contain exactly one of: NameID, EncryptedID");
        }
    }
    
    /**
     * Validates NewID/NewEncryptedID child element
     * 
     * @param request
     * @throws ValidationException 
     */
    protected void validateNewIDAndTerminate(ManageNameIDRequest request) throws ValidationException {
        int count = 0;
        
        if (request.getNewID() != null) {
            count++;
        }
        if (request.getNewEncryptedID() != null) {
            count++;
        }
        if (request.getTerminate() != null) {
            count++;
        }
        
        if (count != 1) {
            throw new ValidationException("ManageNameIDRequest must contain exactly one of: NewID, NewEncryptedID, Terminate");
        }
    }
    
}
