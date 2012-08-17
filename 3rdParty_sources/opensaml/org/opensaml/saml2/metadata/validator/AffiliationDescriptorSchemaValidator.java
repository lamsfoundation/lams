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

import org.opensaml.saml2.metadata.AffiliationDescriptor;
import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;

/**
 * Checks {@link org.opensaml.saml2.metadata.AffiliationDescriptor} for Schema compliance.
 */
public class AffiliationDescriptorSchemaValidator implements Validator<AffiliationDescriptor> {

    /** Constructor */
    public AffiliationDescriptorSchemaValidator() {

    }

    /** {@inheritDoc} */
    public void validate(AffiliationDescriptor affiliationDescriptor) throws ValidationException {
        validateOwner(affiliationDescriptor);
        validateMember(affiliationDescriptor);
    }

    /**
     * Checks that OwnerID is present and valid.
     * 
     * @param affiliationDescriptor
     * @throws ValidationException
     */
    protected void validateOwner(AffiliationDescriptor affiliationDescriptor) throws ValidationException {
        if (DatatypeHelper.isEmpty(affiliationDescriptor.getOwnerID())) {
            throw new ValidationException("Owner ID required.");
        } else if (affiliationDescriptor.getOwnerID().length() > 1024) {
            throw new ValidationException("Max Owner ID length is 1024.");
        }
    }

    /**
     * Checks that at least one Member is present.
     * 
     * @param affiliationDescriptor
     * @throws ValidationException
     */
    protected void validateMember(AffiliationDescriptor affiliationDescriptor) throws ValidationException {
        if (affiliationDescriptor.getMembers() == null || affiliationDescriptor.getMembers().size() < 1) {
            throw new ValidationException("Must have one or more Affiliation Members.");
        }
    }
}