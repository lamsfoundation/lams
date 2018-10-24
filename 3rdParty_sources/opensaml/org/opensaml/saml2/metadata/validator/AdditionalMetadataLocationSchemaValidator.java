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

import org.opensaml.saml2.metadata.AdditionalMetadataLocation;
import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;

/**
 * Checks {@link org.opensaml.saml2.metadata.AdditionalMetadataLocation} for Schema compliance.
 */
public class AdditionalMetadataLocationSchemaValidator implements Validator<AdditionalMetadataLocation> {

    /** Constructor */
    public AdditionalMetadataLocationSchemaValidator() {

    }

    /** {@inheritDoc} */
    public void validate(AdditionalMetadataLocation aml) throws ValidationException {
        validateLocation(aml);
        validateNamespace(aml);
    }

    /**
     * Checks that Location is present.
     * 
     * @param aml
     * @throws ValidationException
     */
    protected void validateLocation(AdditionalMetadataLocation aml) throws ValidationException {
        if (DatatypeHelper.isEmpty(aml.getLocationURI())) {
            throw new ValidationException("Location required");
        }
    }

    /**
     * Checks that Namespace is present.
     * 
     * @param aml
     * @throws ValidationException
     */
    protected void validateNamespace(AdditionalMetadataLocation aml) throws ValidationException {
        if (DatatypeHelper.isEmpty(aml.getNamespaceURI())) {
            throw new ValidationException("Namespace required");
        }
    }
}