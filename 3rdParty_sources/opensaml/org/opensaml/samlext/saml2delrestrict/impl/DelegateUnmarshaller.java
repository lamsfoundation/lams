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

package org.opensaml.samlext.saml2delrestrict.impl;

import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;
import org.opensaml.common.impl.AbstractSAMLObjectUnmarshaller;
import org.opensaml.saml2.core.BaseID;
import org.opensaml.saml2.core.EncryptedID;
import org.opensaml.saml2.core.NameID;
import org.opensaml.samlext.saml2delrestrict.Delegate;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;
import org.w3c.dom.Attr;

/**
 * Unmarshaller for instances of {@link Delegate}.
 */
public class DelegateUnmarshaller extends AbstractSAMLObjectUnmarshaller {

    /** {@inheritDoc} */
    protected void processAttribute(XMLObject samlObject, Attr attribute) throws UnmarshallingException {
        Delegate delegate = (Delegate) samlObject;
        
        String attrName = attribute.getLocalName();
        if (Delegate.CONFIRMATION_METHOD_ATTRIB_NAME.equals(attrName)) {
            delegate.setConfirmationMethod(attribute.getValue());
        } else if (Delegate.DELEGATION_INSTANT_ATTRIB_NAME.equals(attrName)) {
            delegate.setDelegationInstant(new DateTime(attribute.getValue(), ISOChronology.getInstanceUTC()));
        } else {
            super.processAttribute(samlObject, attribute);
        }
    }

    /** {@inheritDoc} */
    protected void processChildElement(XMLObject parentSAMLObject, XMLObject childSAMLObject)
            throws UnmarshallingException {
        Delegate delegate = (Delegate) parentSAMLObject;
        
        if (childSAMLObject instanceof BaseID) {
            delegate.setBaseID((BaseID) childSAMLObject);
        } else if (childSAMLObject instanceof NameID) {
            delegate.setNameID((NameID) childSAMLObject);
        } else if (childSAMLObject instanceof EncryptedID) {
            delegate.setEncryptedID((EncryptedID) childSAMLObject);
        } else {
            super.processChildElement(parentSAMLObject, childSAMLObject);
        }
    }

}
