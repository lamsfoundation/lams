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

import org.opensaml.xml.signature.DSAKeyValue;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;

/**
 * Checks {@link org.opensaml.xml.signature.DSAKeyValue} for Schema compliance. 
 */
public class DSAKeyValueSchemaValidator implements Validator<DSAKeyValue> {

    /** {@inheritDoc} */
    public void validate(DSAKeyValue xmlObject) throws ValidationException {
        validateChildrenPresence(xmlObject);
    }

    /**
     * Validate that all children are present.
     * 
     * @param xmlObject the object to validate
     * @throws ValidationException  thrown if the object is invalid
     */
    protected void validateChildrenPresence(DSAKeyValue xmlObject) throws ValidationException {
        if (xmlObject.getY() == null) {
            throw new ValidationException("DSAKeyValue did not contain a required Y value");
        }
        
        if (xmlObject.getP() != null && xmlObject.getQ() == null) {
            throw new ValidationException("RSAKeyValue did contained a P value without a Q value");
        } else if (xmlObject.getQ() != null && xmlObject.getP() == null) {
            throw new ValidationException("RSAKeyValue did contained a Q value without a P value");
        }
        
        if (xmlObject.getPgenCounter() != null && xmlObject.getSeed() == null) {
            throw new ValidationException("RSAKeyValue did contained a PgenCounter value without a Seed value");
        } else if (xmlObject.getSeed() != null && xmlObject.getPgenCounter() == null) {
            throw new ValidationException("RSAKeyValue did contained a Seed value without a PgenCounter value");
        }
        
    }
}
