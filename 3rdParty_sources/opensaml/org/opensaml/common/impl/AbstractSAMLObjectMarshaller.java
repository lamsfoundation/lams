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

package org.opensaml.common.impl;

import org.opensaml.common.SAMLObjectHelper;
import org.opensaml.common.SignableSAMLObject;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.AbstractXMLObjectMarshaller;
import org.opensaml.xml.io.MarshallingException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A thread safe, abstract implementation of the {@link org.opensaml.xml.io.Marshaller} interface that handles most of
 * the boilerplate code for Marshallers.
 */
public abstract class AbstractSAMLObjectMarshaller extends AbstractXMLObjectMarshaller {

    /**
     * No-op method. Extending implementations should override this method if they have attributes to marshall into the
     * Element.
     * 
     * {@inheritDoc}
     */
    protected void marshallAttributes(XMLObject xmlObject, Element domElement) throws MarshallingException {

    }

    /**
     * No-op method. Extending implementations should override this method if they have text content to marshall into
     * the Element.
     * 
     * {@inheritDoc}
     */
    protected void marshallElementContent(XMLObject xmlObject, Element domElement) throws MarshallingException {

    }

    /** {@inheritDoc} */
    public Element marshall(XMLObject xmlObject, Document document) throws MarshallingException {
        if (xmlObject instanceof SignableSAMLObject) {
            SAMLObjectHelper.declareNonVisibleNamespaces((SignableSAMLObject) xmlObject);
        }
        return super.marshall(xmlObject, document);
    }

    /** {@inheritDoc} */
    public Element marshall(XMLObject xmlObject, Element parentElement) throws MarshallingException {
        if (xmlObject instanceof SignableSAMLObject) {
            SAMLObjectHelper.declareNonVisibleNamespaces((SignableSAMLObject) xmlObject);
        }
        return super.marshall(xmlObject, parentElement);
    }
    
    
}