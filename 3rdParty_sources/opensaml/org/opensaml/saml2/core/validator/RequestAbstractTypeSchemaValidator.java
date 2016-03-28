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

import org.opensaml.common.SAMLVersion;
import org.opensaml.saml2.core.RequestAbstractType;
import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;

/**
 * Checks {@link org.opensaml.saml2.core.RequestAbstractType} for Schema compliance.
 * 
 * @param <RequestType> request type that will be validated
 */
public abstract class RequestAbstractTypeSchemaValidator<RequestType extends RequestAbstractType> implements
        Validator<RequestType> {

    /**
     * Constructor.
     */
    public RequestAbstractTypeSchemaValidator() {
    }

    /** {@inheritDoc} */
    public void validate(RequestType request) throws ValidationException {
        validateID(request);
        validateVersion(request);
        validateIssueInstant(request);

    }

    /**
     * Validates the ID attribute.
     * 
     * @param request request to validate
     * @throws ValidationException if invalid
     */
    protected void validateID(RequestAbstractType request) throws ValidationException {
        if (DatatypeHelper.isEmpty(request.getID())) {
            throw new ValidationException("ID attribute must not be empty");
        }
    }

    /**
     * Validates the Version attribute.
     * 
     * @param request request to validate
     * @throws ValidationException if invalid
     */
    protected void validateVersion(RequestAbstractType request) throws ValidationException {
        if (request.getVersion() == null) {
            throw new ValidationException("Version attribute must not be null");
        }
        if (!DatatypeHelper.safeEquals(request.getVersion().toString(), SAMLVersion.VERSION_20.toString())) {
            throw new ValidationException("Wrong SAML Version");
        }
    }

    /**
     * Validates the IsssueInstant attribute.
     * 
     * @param request request to validate
     * @throws ValidationException if invalid
     */
    protected void validateIssueInstant(RequestAbstractType request) throws ValidationException {
        if (request.getIssueInstant() == null) {
            throw new ValidationException("IssueInstant attribute must not be null");
        }
    }
}
