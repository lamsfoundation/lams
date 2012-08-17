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

package org.opensaml.ws.wsfed.impl;

import org.opensaml.ws.wsfed.Address;
import org.opensaml.ws.wsfed.EndPointReference;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.AbstractXMLObjectUnmarshaller;
import org.w3c.dom.Attr;

/** A thread-safe unmarshaller for {@link EndPointReference} objects. */
public class EndPointReferenceUnmarshaller extends AbstractXMLObjectUnmarshaller {

    /** {@inheritDoc} */
    protected void processChildElement(XMLObject parentSAMLObject, XMLObject childSAMLObject) {
        EndPointReference endPointReference = (EndPointReference) parentSAMLObject;

        if (childSAMLObject instanceof Address) {
            endPointReference.setAddress((Address) childSAMLObject);
        }
    }

    /** {@inheritDoc} */
    protected void processAttribute(XMLObject fedObject, Attr attribute) {

    }

    /** {@inheritDoc} */
    protected void processElementContent(XMLObject fedObject, String content) {

    }
}