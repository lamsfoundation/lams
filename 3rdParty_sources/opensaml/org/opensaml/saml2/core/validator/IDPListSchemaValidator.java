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

import org.opensaml.saml2.core.IDPList;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;

/**
 * Checks {@link org.opensaml.saml2.core.IDPList} for Schema compliance.
 */
public class IDPListSchemaValidator implements Validator<IDPList> {

    /**
     * Constructor
     *
     */
    public IDPListSchemaValidator() {
        super();
    }

    /** {@inheritDoc} */
    public void validate(IDPList list) throws ValidationException {
        validateIDPEntries(list);
    }

    /**
     *  Validates IDPEntry child elements.
     * 
     * @param list
     * @throws ValidationException
     */
    protected void validateIDPEntries(IDPList list) throws ValidationException {
        if (list.getIDPEntrys().size() < 1) {
            throw new ValidationException("IDPList must contain at least one IDPEntry");
        }
    }
}