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

import java.util.HashSet;
import java.util.List;

import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeQuery;
import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.util.Pair;
import org.opensaml.xml.validation.ValidationException;

/**
 * Checks {@link org.opensaml.saml2.core.AttributeQuery} for Schema compliance.
 */
public class AttributeQuerySchemaValidator extends SubjectQuerySchemaValidator<AttributeQuery> {

    /**
     * Constructor.
     */
    public AttributeQuerySchemaValidator() {
        super();
    }

    /** {@inheritDoc} */
    public void validate(AttributeQuery query) throws ValidationException {
        super.validate(query);
        validateUniqueAttributeIdentifiers(query);
    }

    /**
     * Checks that all the attributes have a unique Name/NameFormat pair.
     * 
     * @param query the attribute query to validate
     * 
     * @throws ValidationException thrown if more than on Name/NameFormat pair is found in the list of attributes in
     *             this query
     */
    protected void validateUniqueAttributeIdentifiers(AttributeQuery query) throws ValidationException {
        List<Attribute> attributes = query.getAttributes();

        HashSet<Pair<String, String>> encounteredNames = new HashSet<Pair<String, String>>();
        String attributeName;
        String attributeNameFormat;
        for (Attribute attribute : attributes) {
            attributeName = attribute.getName();
            attributeNameFormat = attribute.getNameFormat();
            if (DatatypeHelper.isEmpty(attributeNameFormat)) {
                // SAML 2 core, sec. 2.7.3.1, if no format is specified,
                // unspecified is in effect. This avoids bug in processing null value.
                attributeNameFormat = Attribute.UNSPECIFIED;
            }
            
            Pair<String, String> pair = new Pair<String, String>(attributeName, attributeNameFormat);
            if (encounteredNames.contains(pair)) {
                throw new ValidationException(
                        "Attribute query contains more than one attribute with the same Name and NameFormat");
            } else {
                encounteredNames.add(pair);
            }
        }
    }
}