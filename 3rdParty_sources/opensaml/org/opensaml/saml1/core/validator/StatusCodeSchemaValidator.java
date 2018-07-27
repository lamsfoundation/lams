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

import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml1.core.StatusCode;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;

/**
 * Checks {@link org.opensaml.saml1.core.StatusCode} for Schema compliance.
 */
public class StatusCodeSchemaValidator implements Validator<StatusCode> {

    /** {@inheritDoc} */
    public void validate(StatusCode statusCode) throws ValidationException {
        validateValue(statusCode);
        validateValueContent(statusCode);
    }

    /**
     * Validates that the status code has a value.
     * 
     * @param statusCode status code to validate
     * 
     * @throws ValidationException thrown if the status code does not have a value
     */
    protected void validateValue(StatusCode statusCode) throws ValidationException {
        QName value = statusCode.getValue();
        if (value == null) {
            throw new ValidationException("No Value attribute present");
        }
    }

    /**
     * Validates that the status code local name is one of the allowabled values.
     * 
     * @param statusCode the status code to validate
     * 
     * @throws ValidationException thrown if the status code local name is not an allowed value
     */
    protected void validateValueContent(StatusCode statusCode) throws ValidationException {
        QName statusValue = statusCode.getValue();

        if (SAMLConstants.SAML10P_NS.equals(statusValue.getNamespaceURI())) {
            if (!(statusValue.equals(StatusCode.SUCCESS) 
                    || statusValue.equals(StatusCode.VERSION_MISMATCH)
                    || statusValue.equals(StatusCode.REQUESTER) 
                    || statusValue.equals(StatusCode.RESPONDER)
                    || statusValue.equals(StatusCode.REQUEST_VERSION_TOO_HIGH)
                    || statusValue.equals(StatusCode.REQUEST_VERSION_TOO_LOW)
                    || statusValue.equals(StatusCode.REQUEST_VERSION_DEPRICATED)
                    || statusValue.equals(StatusCode.TOO_MANY_RESPONSES)
                    || statusValue.equals(StatusCode.REQUEST_DENIED)
                    || statusValue.equals(StatusCode.RESOURCE_NOT_RECOGNIZED))) {
                throw new ValidationException(
                        "Status code value was in the SAML 1 protocol namespace but was not of an allowed value: "
                                + statusValue);
            }
        } else if (SAMLConstants.SAML1_NS.equals(statusValue.getNamespaceURI())) {
            throw new ValidationException(
                    "Status code value was in the SAML 1 assertion namespace, no values are allowed in that namespace");
        }
    }
}