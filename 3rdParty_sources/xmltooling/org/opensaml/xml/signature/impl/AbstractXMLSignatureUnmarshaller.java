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

package org.opensaml.xml.signature.impl;

import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.AbstractXMLObjectUnmarshaller;
import org.opensaml.xml.io.UnmarshallingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;

/**
 * An abstract unmarshaller implementation for XMLObjects from {@link org.opensaml.xml.signature}.
 */
public abstract class AbstractXMLSignatureUnmarshaller extends AbstractXMLObjectUnmarshaller {

    /**
     * Logger.
     */
    private final Logger log = LoggerFactory.getLogger(AbstractXMLSignatureUnmarshaller.class);

    /**
     * {@inheritDoc}
     */
    protected void processChildElement(XMLObject parentXMLObject, XMLObject childXMLObject)
            throws UnmarshallingException {
        log.debug("Ignoring unknown element {}", childXMLObject.getElementQName());
    }

    /**
     * {@inheritDoc}
     */
    protected void processAttribute(XMLObject xmlObject, Attr attribute) throws UnmarshallingException {
        log.debug("Ignorning unknown attribute {}", attribute.getLocalName());
    }

    /**
     * {@inheritDoc}
     */
    protected void processElementContent(XMLObject xmlObject, String elementContent) {
        log.debug("Ignoring element content {}", elementContent);
    }
}
