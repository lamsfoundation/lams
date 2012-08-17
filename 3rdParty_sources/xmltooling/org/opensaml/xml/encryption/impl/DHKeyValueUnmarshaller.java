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

package org.opensaml.xml.encryption.impl;

import org.opensaml.xml.XMLObject;
import org.opensaml.xml.encryption.DHKeyValue;
import org.opensaml.xml.encryption.Generator;
import org.opensaml.xml.encryption.P;
import org.opensaml.xml.encryption.PgenCounter;
import org.opensaml.xml.encryption.Public;
import org.opensaml.xml.encryption.Q;
import org.opensaml.xml.encryption.Seed;
import org.opensaml.xml.io.UnmarshallingException;

/**
 * A thread-safe Unmarshaller for {@link org.opensaml.xml.encryption.DHKeyValue} objects.
 */
public class DHKeyValueUnmarshaller extends AbstractXMLEncryptionUnmarshaller {

    /** {@inheritDoc} */
    protected void processChildElement(XMLObject parentXMLObject, XMLObject childXMLObject)
            throws UnmarshallingException {
        DHKeyValue keyValue = (DHKeyValue) parentXMLObject;

        if (childXMLObject instanceof P) {
            keyValue.setP((P) childXMLObject);
        } else if (childXMLObject instanceof Q) {
            keyValue.setQ((Q) childXMLObject);
        } else if (childXMLObject instanceof Generator) {
            keyValue.setGenerator((Generator) childXMLObject);
        } else if (childXMLObject instanceof Public) {
            keyValue.setPublic((Public) childXMLObject);
        } else if (childXMLObject instanceof Seed) {
            keyValue.setSeed((Seed) childXMLObject);
        } else if (childXMLObject instanceof PgenCounter) {
            keyValue.setPgenCounter((PgenCounter) childXMLObject);
        } else {
            super.processChildElement(parentXMLObject, childXMLObject);
        }
    }

}
