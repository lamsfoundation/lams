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

import org.opensaml.saml2.core.AuthenticatingAuthority;
import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;

/**
 * Checks {@link org.opensaml.saml2.core.AuthenticatingAuthority} for Schema compliance.
 */
public class AuthenticatingAuthoritySchemaValidator implements Validator<AuthenticatingAuthority> {

    /** Constructor */
    public AuthenticatingAuthoritySchemaValidator() {

    }

    /** {@inheritDoc} */
    public void validate(AuthenticatingAuthority authenAuthority) throws ValidationException {
        validateURI(authenAuthority);
    }

    /**
     * Checks that the URI is present.
     * 
     * @param authenAuthority
     * @throws ValidationException
     */
    protected void validateURI(AuthenticatingAuthority authenAuthority) throws ValidationException {
        if (DatatypeHelper.isEmpty(authenAuthority.getURI())) {
            throw new ValidationException("URI required");
        }
    }
}