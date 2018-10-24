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

import org.opensaml.saml2.core.RequestedAuthnContext;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;

/**
 * Checks {@link org.opensaml.saml2.core.RequestedAuthnContext} for Schema compliance.
 */
public class RequestedAuthnContextSchemaValidator implements Validator<RequestedAuthnContext> {

    /**
     * Constructor
     *
     */
    public RequestedAuthnContextSchemaValidator() {
        super();
    }

    /** {@inheritDoc} */
    public void validate(RequestedAuthnContext requestedAuthnContext) throws ValidationException {
        validateChildren(requestedAuthnContext);
    }

    /**
     * Validates the presence and combination of child elements.
     * 
     * @param rac
     * @throws ValidationException 
     */
    protected void validateChildren(RequestedAuthnContext rac) throws ValidationException {
        int classRefCount = rac.getAuthnContextClassRefs().size();
        int declRefCount = rac.getAuthnContextDeclRefs().size();
        
        if (classRefCount == 0 && declRefCount == 0){
            throw new ValidationException("At least one of either AuthnContextClassRef or AuthnContextDeclRef is required");
        }
        
        if (classRefCount > 0 && declRefCount > 0) {
            throw new ValidationException("AuthnContextClassRef and AuthnContextDeclRef are mutually exclusive");
        }
    }

}
