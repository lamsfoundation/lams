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

import org.opensaml.saml2.metadata.RoleDescriptor;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;

/**
 * Checks {@link org.opensaml.saml2.metadata.RoleDescriptor} for Schema compliance.
 */
public class RoleDescriptorSchemaValidator<RoleDescriptorType extends RoleDescriptor> implements Validator<RoleDescriptorType> {

    /** Constructor */
    public RoleDescriptorSchemaValidator() {

    }

    /** {@inheritDoc} */
    public void validate(RoleDescriptorType roleDescriptor) throws ValidationException {
        validateProtocols(roleDescriptor);
    }

    /**
     * Checks that one or more Protocols are present.
     * 
     * @param roleDescriptor
     * @throws ValidationException
     */
    protected void validateProtocols(RoleDescriptor roleDescriptor) throws ValidationException {
        if (roleDescriptor.getSupportedProtocols() == null || roleDescriptor.getSupportedProtocols().size() == 0) {
            throw new ValidationException("Must have at least one Supported Protocol.");
        }
    }
}