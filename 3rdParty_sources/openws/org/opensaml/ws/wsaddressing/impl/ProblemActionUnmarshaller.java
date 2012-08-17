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

package org.opensaml.ws.wsaddressing.impl;

import org.opensaml.ws.wsaddressing.Action;
import org.opensaml.ws.wsaddressing.ProblemAction;
import org.opensaml.ws.wsaddressing.SoapAction;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Attr;

/**
 * Unmarshaller for instances of {@link ProblemActionUnmarshaller}.
 */
public class ProblemActionUnmarshaller extends AbstractWSAddressingObjectUnmarshaller {

    /** {@inheritDoc} */
    protected void processAttribute(XMLObject xmlObject, Attr attribute) throws UnmarshallingException {
        ProblemAction pa = (ProblemAction) xmlObject;
        XMLHelper.unmarshallToAttributeMap(pa.getUnknownAttributes(), attribute);
    }

    /** {@inheritDoc} */
    protected void processChildElement(XMLObject parentXMLObject, XMLObject childXMLObject)
            throws UnmarshallingException {
        ProblemAction pa = (ProblemAction) parentXMLObject;
        
        if (childXMLObject instanceof Action) {
            pa.setAction((Action) childXMLObject);
        } else if (childXMLObject instanceof SoapAction) {
            pa.setSoapAction((SoapAction) childXMLObject);
        } else {
            super.processChildElement(parentXMLObject, childXMLObject);
        }
    }

}
