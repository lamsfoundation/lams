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

import org.opensaml.xml.signature.X509IssuerSerial;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;

/**
 * Checks {@link org.opensaml.xml.signature.X509IssuerSerial} for Schema compliance. 
 */
public class X509IssuerSerialSchemaValidator implements Validator<X509IssuerSerial> {

    /** {@inheritDoc} */
    public void validate(X509IssuerSerial xmlObject) throws ValidationException {
        validateChildrenPresence(xmlObject);
    }

    /**
     * Validate that exactly one child is present.
     * 
     * @param xmlObject the object to validate
     * @throws ValidationException  thrown if the object is invalid
     */
    protected void validateChildrenPresence(X509IssuerSerial xmlObject) throws ValidationException {
        if (xmlObject.getX509IssuerName() == null) {
            throw new ValidationException("X509IssuerSerial does not contain an X509IssuerName");
        }
        if (xmlObject.getX509SerialNumber() == null) {
            throw new ValidationException("X509IssuerSerial does not contain an X509SerialNumber");
        }
    }
    
}
