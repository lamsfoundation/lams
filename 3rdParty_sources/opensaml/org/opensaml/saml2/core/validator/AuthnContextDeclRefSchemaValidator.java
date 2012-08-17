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

import org.opensaml.saml2.core.AuthnContextDeclRef;
import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;

/**
 * Checks {@link org.opensaml.saml2.core.AuthnContextDeclRef} for Schema compliance.
 */
public class AuthnContextDeclRefSchemaValidator implements Validator<AuthnContextDeclRef> {

    /** Constructor */
    public AuthnContextDeclRefSchemaValidator() {

    }

    /** {@inheritDoc} */
    public void validate(AuthnContextDeclRef authnCDR) throws ValidationException {
        validateDeclRef(authnCDR);
    }

    /**
     * Checks that the Declaration Reference is present.
     * 
     * @param authnCDR
     * @throws ValidationException
     */
    protected void validateDeclRef(AuthnContextDeclRef authnCDR) throws ValidationException {
        if (DatatypeHelper.isEmpty(authnCDR.getAuthnContextDeclRef())) {
            throw new ValidationException("AuthnContextDeclRef required");
        }
    }
}