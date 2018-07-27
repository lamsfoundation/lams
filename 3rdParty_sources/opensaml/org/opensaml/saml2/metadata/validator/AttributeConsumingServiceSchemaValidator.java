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

import org.opensaml.saml2.metadata.AttributeConsumingService;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;

/**
 * Checks {@link org.opensaml.saml2.metadata.AttributeConsumingService} for Schema compliance.
 */
public class AttributeConsumingServiceSchemaValidator implements Validator<AttributeConsumingService> {

    /** Constructor */
    public AttributeConsumingServiceSchemaValidator() {

    }

    /** {@inheritDoc} */
    public void validate(AttributeConsumingService attributeConsumingService) throws ValidationException {
        validateIndex(attributeConsumingService);
        validateServiceNames(attributeConsumingService);
        validateRequestedAttributes(attributeConsumingService);
    }

    /**
     * Checks that Index is positive.
     * 
     * @param attributeConsumingService
     * @throws ValidationException
     */
    protected void validateIndex(AttributeConsumingService attributeConsumingService) throws ValidationException {
        if (attributeConsumingService.getIndex() < 0) {
            throw new ValidationException("Index must be positive integer");
        }
    }

    /**
     * Checks that one or more Service Names are present.
     * 
     * @param attributeConsumingService
     * @throws ValidationException
     */
    protected void validateServiceNames(AttributeConsumingService attributeConsumingService) throws ValidationException {
        if (attributeConsumingService.getNames() == null || attributeConsumingService.getNames().size() == 0) {
            throw new ValidationException("Must have one or more Service Names.");
        }
    }

    /**
     * Checks that one or more Requested Attributes are present.
     * 
     * @param attributeConsumingService
     * @throws ValidationException
     */
    protected void validateRequestedAttributes(AttributeConsumingService attributeConsumingService)
            throws ValidationException {
        if (attributeConsumingService.getRequestAttributes() == null
                || attributeConsumingService.getRequestAttributes().size() == 0) {
            throw new ValidationException("Must have one or more Requested Attributes.");
        }
    }
}