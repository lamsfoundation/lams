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

import org.opensaml.saml2.metadata.EntitiesDescriptor;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;

/**
 * Checks {@link org.opensaml.saml2.metadata.EntitiesDescriptor} for Schema compliance.
 */
public class EntitiesDescriptorSchemaValidator implements Validator<EntitiesDescriptor> {

    /** Constructor */
    public EntitiesDescriptorSchemaValidator() {

    }

    /** {@inheritDoc} */
    public void validate(EntitiesDescriptor entitiesDescriptor) throws ValidationException {
        validateEntityDescriptors(entitiesDescriptor);
    }

    /**
     * Checks that at least one EntitiesDescriptor or EntityDescriptor is present.
     * 
     * @param entitiesDescriptor
     * @throws ValidationException
     */
    protected void validateEntityDescriptors(EntitiesDescriptor entitiesDescriptor) throws ValidationException {
        if ((entitiesDescriptor.getEntitiesDescriptors() == null || entitiesDescriptor.getEntitiesDescriptors().size() < 1)
                && (entitiesDescriptor.getEntityDescriptors() == null || entitiesDescriptor.getEntityDescriptors()
                        .size() < 1)) {
            throw new ValidationException("Must have one or more EntitiesDescriptor or EntityDescriptor.");
        }
    }
}