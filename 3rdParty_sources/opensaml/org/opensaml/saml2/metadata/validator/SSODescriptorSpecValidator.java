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

import org.opensaml.saml2.metadata.SSODescriptor;
import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.validation.ValidationException;

/**
 * Checks {@link org.opensaml.saml2.metadata.SSODescriptor} for Spec compliance.
 */
public class SSODescriptorSpecValidator<SSODescriptorType extends SSODescriptor> extends RoleDescriptorSpecValidator<SSODescriptorType> {

    /** Constructor */
    public SSODescriptorSpecValidator() {

    }

    /** {@inheritDoc} */
    public void validate(SSODescriptorType ssoDescriptor) throws ValidationException {
        validateResponseLocation(ssoDescriptor);
        super.validate(ssoDescriptor);
    }

    /**
     * Checks that Response Location of Artifact Resolution Services is omitted.
     * 
     * @param ssoDescriptor
     * @throws ValidationException
     */
    protected void validateResponseLocation(SSODescriptor ssoDescriptor) throws ValidationException {
        if (ssoDescriptor.getArtifactResolutionServices() != null
                && ssoDescriptor.getArtifactResolutionServices().size() > 0) {
            for (int i = 0; i < ssoDescriptor.getArtifactResolutionServices().size(); i++) {
                if (!DatatypeHelper.isEmpty(ssoDescriptor.getArtifactResolutionServices().get(i).getResponseLocation())) {
                    throw new ValidationException("ResponseLocation of all ArtificatResolutionServices must be null");
                }
            }
        }
    }
}