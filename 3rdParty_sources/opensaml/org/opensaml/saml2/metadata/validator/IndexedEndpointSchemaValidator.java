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

import org.opensaml.saml2.metadata.IndexedEndpoint;
import org.opensaml.xml.validation.ValidationException;

/**
 * Checks {@link org.opensaml.saml2.metadata.IndexedEndpoint} for Schema compliance.
 */
public class IndexedEndpointSchemaValidator<EndpointType extends IndexedEndpoint> extends EndpointSchemaValidator<EndpointType> {

    /** Constructor */
    public IndexedEndpointSchemaValidator() {

    }

    /** {@inheritDoc} */
    public void validate(EndpointType indexedEndpoint) throws ValidationException {
        super.validate(indexedEndpoint);
        validateIndex(indexedEndpoint);
    }

    /**
     * Checks that Index is non-negative.
     * 
     * @param indexedEndpoint
     * @throws ValidationException
     */
    protected void validateIndex(IndexedEndpoint indexedEndpoint) throws ValidationException {
        if (indexedEndpoint.getIndex() < 0) {
            throw new ValidationException("Index must be non-negative");
        }
    }
}