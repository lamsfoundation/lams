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
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.signature.RetrievalMethod;
import org.w3c.dom.Element;

/**
 * A thread-safe Marshaller for {@link org.opensaml.xml.signature.RetrievalMethod} objects.
 */
public class RetrievalMethodMarshaller extends AbstractXMLSignatureMarshaller {

    /** {@inheritDoc} */
    protected void marshallAttributes(XMLObject xmlObject, Element domElement) throws MarshallingException {
        RetrievalMethod rm = (RetrievalMethod) xmlObject;

        if (rm.getURI() != null) {
            domElement.setAttributeNS(null, RetrievalMethod.URI_ATTRIB_NAME, rm.getURI());
        }
        if (rm.getType() != null) {
            domElement.setAttributeNS(null, RetrievalMethod.TYPE_ATTRIB_NAME, rm.getType());
        }
    }

}
