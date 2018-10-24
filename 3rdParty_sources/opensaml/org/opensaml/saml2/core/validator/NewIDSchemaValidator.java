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

import org.opensaml.saml2.core.NewID;
import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;

/**
 * Checks {@link org.opensaml.saml2.core.NewID} for Schema compliance.
 */
public class NewIDSchemaValidator implements Validator<NewID> {

    /**
     * Constructor
     *
     */
    public NewIDSchemaValidator() {
        super();
    }

    /** {@inheritDoc} */
    public void validate(NewID newid) throws ValidationException {
        validateNewID(newid);
    }

    /**
     * Validate the element content.
     * 
     * @param newid
     * @throws ValidationException
     */
    protected void validateNewID(NewID newid) throws ValidationException {
        if (DatatypeHelper.isEmpty(newid.getNewID())) {
            throw new ValidationException("NewID must be non-empty");
        }
    }
}
