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

import org.opensaml.xml.signature.ECKeyValue;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;

/**
 * Checks {@link ECKeyValue} for Schema compliance. 
 */
public class ECKeyValueSchemaValidator implements Validator<ECKeyValue> {

    /** {@inheritDoc} */
    public void validate(ECKeyValue xmlObject) throws ValidationException {
        validateChildrenPresence(xmlObject);
    }

    /**
     * Validate that all children are present.
     * 
     * @param xmlObject the object to validate
     * @throws ValidationException  thrown if the object is invalid
     */
    protected void validateChildrenPresence(ECKeyValue xmlObject) throws ValidationException {
        if (xmlObject.getPublicKey() == null) {
            throw new ValidationException("ECKeyValue did not contain a required PublicKey value");
        } else if (xmlObject.getNamedCurve() == null && xmlObject.getECParameters() == null) {
            throw new ValidationException("ECKeyValue did not contain a required NamedCurve or ECParameters value");
        }
    }
}