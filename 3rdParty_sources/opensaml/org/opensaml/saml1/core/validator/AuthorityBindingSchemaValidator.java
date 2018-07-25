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

package org.opensaml.saml1.core.validator;

import javax.xml.namespace.QName;

import org.opensaml.saml1.core.AuthorityBinding;
import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;

/**
 * Checks {@link org.opensaml.saml1.core.AuthorityBinding} for Schema compliance.
 */
public class AuthorityBindingSchemaValidator implements Validator<AuthorityBinding> {

    /** {@inheritDoc} */
    public void validate(AuthorityBinding authorityBinding) throws ValidationException {
        validateAuthorityKind(authorityBinding);
        validateBinding(authorityBinding);
        validateLocation(authorityBinding);
    }
    
    /**
     * Check that the AuthorityKind is valid
     * @param authorityBinding
     * @throws ValidationException
     */
    protected void validateAuthorityKind(AuthorityBinding authorityBinding) throws ValidationException {
        //
        // TODO may need to do more validation on the QName here - need to make sure
        // that the prefix is valid - cases of 
        // 1) no incoming XML validation was performed
        // 2) validating parser that doesn't properly validate this QName value.
        //
        QName authorityKind = authorityBinding.getAuthorityKind();    
        if (authorityKind == null) {
             throw new ValidationException("No AuthorityKind attribute present");
         }
    }
    
    /**
     * Check the location Attribute for validity
     * @param authorityBinding
     * @throws ValidationException
     */
    protected void validateLocation(AuthorityBinding authorityBinding) throws ValidationException {
        if (DatatypeHelper.isEmpty(authorityBinding.getLocation())) {
            throw new ValidationException("Location attribute not present or invalid ");
        }
    }

    /**
     * Check the binding Attribute for validity
     * @param authorityBinding
     * @throws ValidationException
     */
    protected void validateBinding(AuthorityBinding authorityBinding) throws ValidationException {
        if (DatatypeHelper.isEmpty(authorityBinding.getBinding())) {
            throw new ValidationException("Binding attribute not present or invalid ");
        }
    }
}