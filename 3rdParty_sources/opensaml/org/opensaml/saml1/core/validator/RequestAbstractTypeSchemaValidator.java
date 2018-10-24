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

package org.opensaml.saml1.core.validator;

import org.opensaml.saml1.core.RequestAbstractType;
import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;

/**
 * Checks {@link org.opensaml.saml1.core.RequestAbstractType} for Schema compliance.
 */
public class RequestAbstractTypeSchemaValidator<RequestType extends RequestAbstractType> implements Validator<RequestType> {

    /** {@inheritDoc} */
    public void validate(RequestType requestAbstractType) throws ValidationException {
        validateVersion(requestAbstractType);

        validateID(requestAbstractType);

        validateIssueInstant(requestAbstractType);
    }

    /**
     * Validates that this is SAML1.0 or SAML 1.1
     * 
     * @param request
     * @throws ValidationException
     */
    protected void validateVersion(RequestAbstractType request) throws ValidationException {
        if ((request.getMajorVersion() != 1) && (request.getMinorVersion() != 0 || request.getMinorVersion() != 1)) {
            throw new ValidationException("Invalid Version");
        }
    }

    /**
     * Validate that the ID is present and valid
     * 
     * @param request
     * @throws ValidationException
     */
    protected void validateID(RequestAbstractType request) throws ValidationException {
        if (DatatypeHelper.isEmpty(request.getID())) {
            throw new ValidationException("RequestID is null, empty or whitespace");
        }
    }

    /**
     * Validate that the IssueInstant is present.
     * 
     * @param request
     * @throws ValidationException
     */
    protected void validateIssueInstant(RequestAbstractType request) throws ValidationException {
        if (request.getIssueInstant() == null) {
            throw new ValidationException("No IssueInstant attribute present");
        }
    }
}