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

package org.opensaml.saml2.metadata.provider;

import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Validator;

import org.opensaml.common.xml.SAMLSchemaBuilder;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.DatatypeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 * A metadata filter that schema validates an incoming metadata file.
 */
public class SchemaValidationFilter implements MetadataFilter {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(SchemaValidationFilter.class);

    /**
     * Constructor.
     * 
     * @param extensionSchemas classpath location of metadata extension schemas, may be null
     */
    public SchemaValidationFilter(String[] extensionSchemas) {
        if (extensionSchemas != null) {
            for (String extension : extensionSchemas) {
                extension = DatatypeHelper.safeTrimOrNullString(extension);
                if (extension != null) {
                    SAMLSchemaBuilder.addExtensionSchema(extension);
                }
            }
        }
    }

    /** {@inheritDoc} */
    public void doFilter(XMLObject metadata) throws FilterException {
        Validator schemaValidator = null;
        try {
            schemaValidator = SAMLSchemaBuilder.getSAML11Schema().newValidator();
        } catch (SAXException e) {
            log.error("Unable to build metadata validation schema", e);
            throw new FilterException("Unable to build metadata validation schema", e);
        }

        try {
            schemaValidator.validate(new DOMSource(metadata.getDOM()));
        } catch (Exception e) {
            log.error("Incoming metadata was not schema valid.", e);
            throw new FilterException("Incoming metadata was not schema valid.", e);
        }
    }
}