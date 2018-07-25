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

import org.opensaml.xml.AbstractXMLObjectBuilder;
import org.opensaml.xml.signature.RetrievalMethod;
import org.opensaml.xml.signature.XMLSignatureBuilder;
import org.opensaml.xml.util.XMLConstants;

/**
 * Builder of {@link org.opensaml.xml.signature.RetrievalMethod}
 */
public class RetrievalMethodBuilder extends AbstractXMLObjectBuilder<RetrievalMethod> implements XMLSignatureBuilder<RetrievalMethod> {

    /**
     * Constructor
     *
     */
    public RetrievalMethodBuilder() {
    }

    /** {@inheritDoc} */
    public RetrievalMethod buildObject(String namespaceURI, String localName, String namespacePrefix) {
        return new RetrievalMethodImpl(namespaceURI, localName, namespacePrefix);
    }

    /** {@inheritDoc} */
    public RetrievalMethod buildObject() {
        return buildObject(XMLConstants.XMLSIG_NS, RetrievalMethod.DEFAULT_ELEMENT_LOCAL_NAME, XMLConstants.XMLSIG_PREFIX);
    }

}
