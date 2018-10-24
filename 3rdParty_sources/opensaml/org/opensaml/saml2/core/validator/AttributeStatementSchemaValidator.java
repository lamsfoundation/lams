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

import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;

/**
 * Checks {@link org.opensaml.saml2.core.AttributeStatement} for Schema compliance.
 */
public class AttributeStatementSchemaValidator implements Validator<AttributeStatement> {

    /** Constructor */
    public AttributeStatementSchemaValidator() {

    }

    /** {@inheritDoc} */
    public void validate(AttributeStatement attributeStatement) throws ValidationException {
        validateAttributes(attributeStatement);
    }

    /**
     * Checks that at least one Attribute is present.
     * 
     * @param attributeStatement
     * @throws ValidationException
     */
    protected void validateAttributes(AttributeStatement attributeStatement) throws ValidationException {
        if (attributeStatement.getAttributes() == null || attributeStatement.getAttributes().size() == 0) {
            throw new ValidationException("Must contain one or more attributes");
        }
    }
}