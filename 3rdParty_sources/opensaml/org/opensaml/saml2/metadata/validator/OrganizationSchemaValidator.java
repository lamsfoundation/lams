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

package org.opensaml.saml2.metadata.validator;

import org.opensaml.saml2.metadata.Organization;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;

/**
 * Checks {@link org.opensaml.saml2.metadata.Organization} for Schema compliance.
 */
public class OrganizationSchemaValidator implements Validator<Organization> {

    /** Constructor */
    public OrganizationSchemaValidator() {

    }

    /** {@inheritDoc} */
    public void validate(Organization organization) throws ValidationException {
        validateName(organization);
        validateDisplayName(organization);
        validateURL(organization);
    }

    /**
     * Checks that at least one Organization Name is present.
     * 
     * @param organization
     * @throws ValidationException
     */
    protected void validateName(Organization organization) throws ValidationException {
        if (organization.getOrganizationNames() == null || organization.getOrganizationNames().size() < 1) {
            throw new ValidationException("Must have one or more Organization Names.");
        }
    }

    /**
     * Checks that at least one Display Name is present.
     * 
     * @param organization
     * @throws ValidationException
     */
    protected void validateDisplayName(Organization organization) throws ValidationException {
        if (organization.getDisplayNames() == null || organization.getDisplayNames().size() < 1) {
            throw new ValidationException("Must have one or more Display Names.");
        }
    }

    /**
     * Checks that at least one Organization URL is present.
     * 
     * @param organization
     * @throws ValidationException
     */
    protected void validateURL(Organization organization) throws ValidationException {
        if (organization.getURLs() == null || organization.getURLs().size() < 1) {
            throw new ValidationException("Must have one or more Organization URLs.");
        }
    }
}