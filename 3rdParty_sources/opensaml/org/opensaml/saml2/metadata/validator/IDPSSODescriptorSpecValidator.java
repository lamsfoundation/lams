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

import org.opensaml.saml2.metadata.IDPSSODescriptor;
import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.validation.ValidationException;

/**
 * Checks {@link org.opensaml.saml2.metadata.IDPSSODescriptor} for Spec compliance.
 */
public class IDPSSODescriptorSpecValidator extends SSODescriptorSpecValidator<IDPSSODescriptor> {

    /** Constructor */
    public IDPSSODescriptorSpecValidator() {

    }

    public void validate(IDPSSODescriptor idpssoDescriptor) throws ValidationException {
        super.validate(idpssoDescriptor);
        validateSingleSign(idpssoDescriptor);
        validateNameIDMapping(idpssoDescriptor);
    }

    protected void validateSingleSign(IDPSSODescriptor idpssoDescriptor) throws ValidationException {
        if (idpssoDescriptor.getSingleSignOnServices() != null && idpssoDescriptor.getSingleSignOnServices().size() > 0) {
            for (int i = 0; i < idpssoDescriptor.getSingleSignOnServices().size(); i++) {
                if (!DatatypeHelper.isEmpty(idpssoDescriptor.getSingleSignOnServices().get(i).getResponseLocation())) {
                    throw new ValidationException("ResponseLocation of all SingleSignOnServices must be null");
                }
            }
        }
    }

    protected void validateNameIDMapping(IDPSSODescriptor idpssoDescriptor) throws ValidationException {
        if (idpssoDescriptor.getNameIDMappingServices() != null
                && idpssoDescriptor.getNameIDMappingServices().size() > 0) {
            for (int i = 0; i < idpssoDescriptor.getNameIDMappingServices().size(); i++) {
                if (!DatatypeHelper.isEmpty(idpssoDescriptor.getNameIDMappingServices().get(i).getResponseLocation())) {
                    throw new ValidationException("ResponseLocation of all NameIDMappingServices must be null");
                }
            }
        }
    }
}