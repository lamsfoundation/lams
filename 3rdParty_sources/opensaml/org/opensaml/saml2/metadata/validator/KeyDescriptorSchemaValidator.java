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

import org.opensaml.saml2.metadata.KeyDescriptor;
import org.opensaml.xml.security.credential.UsageType;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;

/**
 * Checks {@link org.opensaml.saml2.metadata.KeyDescriptor} for Schema compliance.
 */
public class KeyDescriptorSchemaValidator implements Validator<KeyDescriptor> {

    /** Constructor. */
    public KeyDescriptorSchemaValidator() {

    }

    /** {@inheritDoc} */
    public void validate(KeyDescriptor keyDescriptor) throws ValidationException {
        validateKeyInfo(keyDescriptor);
        validateUse(keyDescriptor);
    }

    /**
     * Checks that KeyInfo is present.
     * 
     * @param keyDescriptor the key descriptor to validate
     * @throws ValidationException thrown if KeyInfo is not present
     */
    protected void validateKeyInfo(KeyDescriptor keyDescriptor) throws ValidationException {
        if (keyDescriptor.getKeyInfo()==null) {
            throw new ValidationException("KeyInfo required");
        }
    }
    
    /**
     * Checks that use attribute has only one of allowed values.
     * 
     * @param keyDescriptor the key descriptor to validate
     * @throws ValidationException throw in use attribute does not have a legal value
     */
    protected void validateUse(KeyDescriptor keyDescriptor) throws ValidationException {
        UsageType use = keyDescriptor.getUse();
        if (use == null) {
            return;
        }
        if (       ! use.equals(UsageType.SIGNING) 
                && ! use.equals(UsageType.ENCRYPTION) 
                && ! use.equals(UsageType.UNSPECIFIED) ) {
            throw new ValidationException("Invalid value for use attribute: " + use.toString());
        }
    }
}