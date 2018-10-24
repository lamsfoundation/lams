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

import org.opensaml.saml2.metadata.Endpoint;
import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;

/**
 * Checks {@link org.opensaml.saml2.metadata.Endpoint} for Schema compliance.
 */
public class EndpointSchemaValidator<EndpointType extends Endpoint> implements Validator<EndpointType> {

    /** Constructor */
    public EndpointSchemaValidator() {

    }

    /** {@inheritDoc} */
    public void validate(EndpointType endpoint) throws ValidationException {
        validateBinding(endpoint);
        validateLocation(endpoint);
    }

    /**
     * Checks that Binding is present.
     * 
     * @param endpoint
     * @throws ValidationException
     */
    protected void validateBinding(Endpoint endpoint) throws ValidationException {
        if (DatatypeHelper.isEmpty(endpoint.getBinding())) {
            throw new ValidationException("Binding required");
        }
    }

    /**
     * Checks that Location is present.
     * 
     * @param endpoint
     * @throws ValidationException
     */
    protected void validateLocation(Endpoint endpoint) throws ValidationException {
        if (DatatypeHelper.isEmpty(endpoint.getLocation())) {
            throw new ValidationException("Location required");
        }
    }
}