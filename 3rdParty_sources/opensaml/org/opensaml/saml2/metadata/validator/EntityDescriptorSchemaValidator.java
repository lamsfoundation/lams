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

import org.opensaml.saml2.metadata.EntityDescriptor;
import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;

/**
 * Checks {@link org.opensaml.saml2.metadata.EntityDescriptor} for Schema compliance.
 */
public class EntityDescriptorSchemaValidator implements Validator<EntityDescriptor> {

    /** Constructor */
    public EntityDescriptorSchemaValidator() {

    }

    /** {@inheritDoc} */
    public void validate(EntityDescriptor entityDescriptor) throws ValidationException {
        validateEntityID(entityDescriptor);
        validateDescriptors(entityDescriptor);
    }

    /**
     * Checks that EntityID is present and valid.
     * 
     * @param entityDescriptor
     * @throws ValidationException
     */
    protected void validateEntityID(EntityDescriptor entityDescriptor) throws ValidationException {
        if (DatatypeHelper.isEmpty(entityDescriptor.getEntityID())) {
            throw new ValidationException("Entity ID required.");
        } else if (entityDescriptor.getEntityID().length() > 1024) {
            throw new ValidationException("Max Entity ID length is 1024.");
        }
    }

    /**
     * Checks that an AffiliationDescriptor OR one or more RoleDescriptors are present.
     * 
     * @param entityDescriptor
     * @throws ValidationException
     */
    protected void validateDescriptors(EntityDescriptor entityDescriptor) throws ValidationException {
        if ((entityDescriptor.getRoleDescriptors() == null || entityDescriptor.getRoleDescriptors().size() < 1)
                && entityDescriptor.getAffiliationDescriptor() == null) {
            throw new ValidationException("Must have an AffiliationDescriptor or one or more RoleDescriptors.");
        }

        if (entityDescriptor.getAffiliationDescriptor() != null && entityDescriptor.getRoleDescriptors() != null
                && entityDescriptor.getRoleDescriptors().size() > 0) {
            throw new ValidationException("Cannot have an AffiliationDescriptor AND RoleDescriptors");
        }
    }
}