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
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.signature.DSAKeyValue;
import org.opensaml.xml.signature.G;
import org.opensaml.xml.signature.J;
import org.opensaml.xml.signature.P;
import org.opensaml.xml.signature.PgenCounter;
import org.opensaml.xml.signature.Q;
import org.opensaml.xml.signature.Seed;
import org.opensaml.xml.signature.Y;

/**
 * A thread-safe Unmarshaller for {@link org.opensaml.xml.signature.DSAKeyValue} objects.
 */
public class DSAKeyValueUnmarshaller extends AbstractXMLSignatureUnmarshaller {

    /** {@inheritDoc} */
    protected void processChildElement(XMLObject parentXMLObject, XMLObject childXMLObject)
            throws UnmarshallingException {
        DSAKeyValue keyValue = (DSAKeyValue) parentXMLObject;

        if (childXMLObject instanceof P) {
            keyValue.setP((P) childXMLObject);
        } else if (childXMLObject instanceof Q) {
            keyValue.setQ((Q) childXMLObject);
        } else if (childXMLObject instanceof G) {
            keyValue.setG((G) childXMLObject);
        } else if (childXMLObject instanceof Y) {
            keyValue.setY((Y) childXMLObject);
        } else if (childXMLObject instanceof J) {
            keyValue.setJ((J) childXMLObject);
        } else if (childXMLObject instanceof Seed) {
            keyValue.setSeed((Seed) childXMLObject);
        } else if (childXMLObject instanceof PgenCounter) {
            keyValue.setPgenCounter((PgenCounter) childXMLObject);
        } else {
            super.processChildElement(parentXMLObject, childXMLObject);
        }
    }

}
