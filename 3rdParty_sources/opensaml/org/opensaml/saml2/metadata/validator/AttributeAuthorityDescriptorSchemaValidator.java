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

import org.opensaml.saml2.metadata.AttributeAuthorityDescriptor;
import org.opensaml.xml.validation.ValidationException;

/**
 * Checks {@link org.opensaml.saml2.metadata.AttributeAuthorityDescriptor} for Schema compliance.
 */
public class AttributeAuthorityDescriptorSchemaValidator extends RoleDescriptorSchemaValidator<AttributeAuthorityDescriptor> {

    /** Constructor */
    public AttributeAuthorityDescriptorSchemaValidator() {

    }

    /** {@inheritDoc} */
    public void validate(AttributeAuthorityDescriptor attributeAuthorityDescriptor) throws ValidationException {
        super.validate(attributeAuthorityDescriptor);
        validateAttributeServices(attributeAuthorityDescriptor);
    }

    /**
     * Checks that at least one AttributeService is present.
     * 
     * @param attributeAuthorityDescriptor
     * @throws ValidationException
     */
    protected void validateAttributeServices(AttributeAuthorityDescriptor attributeAuthorityDescriptor)
            throws ValidationException {
        if (attributeAuthorityDescriptor.getAttributeServices() == null
                || attributeAuthorityDescriptor.getAttributeServices().size() == 0) {
            throw new ValidationException("Must have one or more AttributeServices.");
        }
    }

}