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

package org.opensaml.xml.signature.validator;

import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;

/**
 * Checks {@link org.opensaml.xml.signature.Signature} for Schema compliance. 
 */
public class SignatureSchemaValidator implements Validator<Signature> {

    /** {@inheritDoc} */
    public void validate(Signature xmlObject) throws ValidationException {
        validateSignatureMethod(xmlObject);
        validateCanonicalizationMethod(xmlObject);
    }

    /**
     * Check the canonicalization method.
     * 
     * @param xmlObject the object to be validated
     * @throws ValidationException thrown if object is invalid
     */
    protected void validateCanonicalizationMethod(Signature xmlObject) throws ValidationException {
        if (DatatypeHelper.isEmpty(xmlObject.getCanonicalizationAlgorithm())) {
            throw new ValidationException("The CanonicalizationMethod value was empty");
        }
    }

    /**
     * Check the signature method.
     * 
     * @param xmlObject the object to be validated
     * @throws ValidationException thrown if object is invalid
     */
    protected void validateSignatureMethod(Signature xmlObject) throws ValidationException {
        if (DatatypeHelper.isEmpty(xmlObject.getSignatureAlgorithm())) {
            throw new ValidationException("The SignatureMethod value was empty");
        }
    }

}
