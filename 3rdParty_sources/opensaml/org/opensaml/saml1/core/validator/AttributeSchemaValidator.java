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

package org.opensaml.saml1.core.validator;

import org.opensaml.saml1.core.Attribute;
import org.opensaml.xml.validation.ValidationException;

/**
 * Checks {@link org.opensaml.saml1.core.Attribute} for Schema compliance.
 */
public class AttributeSchemaValidator extends AttributeDesignatorSchemaValidator<Attribute> {

    /** {@inheritDoc} */
    public void validate(Attribute attribute) throws ValidationException {
        super.validate(attribute);

        validateAttributeValue(attribute);
    }

    /**
     * Validates that the attribute has at least one attribute value.
     * 
     * @param attribute attribute to validate
     * 
     * @throws ValidationException thrown if the attribute does not have any values
     */
    protected void validateAttributeValue(Attribute attribute) throws ValidationException {
        if (attribute.getAttributeValues().size() == 0) {
            throw new ValidationException("No AttributeValue elements present");
        }
    }
}