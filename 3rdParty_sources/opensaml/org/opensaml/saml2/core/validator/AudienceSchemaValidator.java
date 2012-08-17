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

import org.opensaml.saml2.core.Audience;
import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;

/**
 * Checks {@link org.opensaml.saml2.core.Audience} for Schema compliance.
 */
public class AudienceSchemaValidator implements Validator<Audience> {

    /** Constructor */
    public AudienceSchemaValidator() {

    }

    /** {@inheritDoc} */
    public void validate(Audience audience) throws ValidationException {
        validateAudienceURI(audience);
    }

    /**
     * Checks that the AudienceURI is present.
     * 
     * @param audience
     * @throws ValidationException
     */
    protected void validateAudienceURI(Audience audience) throws ValidationException {
        if (DatatypeHelper.isEmpty(audience.getAudienceURI())) {
            throw new ValidationException("AudienceURI required");
        }
    }
}