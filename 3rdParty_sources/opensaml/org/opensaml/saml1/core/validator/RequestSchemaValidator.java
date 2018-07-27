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

import org.opensaml.saml1.core.Request;
import org.opensaml.xml.validation.ValidationException;

/**
 * Checks {@link org.opensaml.saml1.core.Request} for Schema compliance.
 */
public class RequestSchemaValidator extends RequestAbstractTypeSchemaValidator<Request> {

    /** {@inheritDoc} */
    public void validate(Request request) throws ValidationException {
        super.validate(request);
        validateAssertion(request);
    }

    /**
     * Validates thats the request has an some form of assertion (directly, reference, or artifact) or query, but not
     * both.
     * 
     * @param request the request to validate
     * 
     * @throws ValidationException thrown if the request has more than one assertion or both an assertion and a query
     */
    protected void validateAssertion(Request request) throws ValidationException {
        if (request.getQuery() != null) {
            if (request.getAssertionArtifacts().size() != 0) {
                throw new ValidationException("Both Query and one or more AssertionAtrifacts present");
            }
            if (request.getAssertionIDReferences().size() != 0) {
                throw new ValidationException("Both  Query and one ore more AsertionIDReferences present");
            }
        } else if (request.getAssertionArtifacts().size() != 0) {
            if (request.getAssertionIDReferences().size() != 0) {
                throw new ValidationException(
                        "Both one or more AssertionAtrifacts and one ore more AsertionIDReferences present");
            }
        } else if (request.getAssertionIDReferences().size() == 0) {
            throw new ValidationException("No AssertionAtrifacts, No Query, and No AsertionIDReferences present");
        }
    }
}