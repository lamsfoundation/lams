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

package org.opensaml.xml.schema.validator;

import org.opensaml.xml.schema.XSString;
import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;

/**
 * Checks {@link org.opensaml.xml.schema.XSString} for Schema compliance. 
 * 
 * @param <T> the type to be validated
 */
public class XSStringSchemaValidator<T extends XSString> implements Validator<T> {
    
    /** Flag specifying whether empty element content should be allowed. */
    private boolean allowEmptyContent;
    
    /**
     * Constructor.
     *
     * @param allowEmptyElementContent flag indicated whether empty content should be allowed
     */
    public XSStringSchemaValidator(boolean allowEmptyElementContent) {
        allowEmptyContent = allowEmptyElementContent;
    }
    
    /**
     * Constructor.
     * 
     * Empty content is not allowed.
     *
     */
    public XSStringSchemaValidator() {
        allowEmptyContent = false;
    }

    /** {@inheritDoc} */
    public void validate(T xmlObject) throws ValidationException {
        validateStringContent(xmlObject);
    }
    
    /**
     * Get the flag which determines whether empty content should be allowed.
     * 
     * @return true if empty content should be allowed, false otherwise
     */
    protected boolean isAllowEmptyContent() {
        return allowEmptyContent;
    }

    /**
     * Validates the content of the XSBase64Binary object.
     * 
     * @param xmlObject the object to evaluate
     * @throws ValidationException thrown if the content of the Base64Binary object is invalid
     */
    protected void validateStringContent(T xmlObject) throws ValidationException {
        if (! isAllowEmptyContent()) {
            if (DatatypeHelper.isEmpty(xmlObject.getValue())) {
                throw new ValidationException("String content may not be empty");
            }
        }
    }

}
