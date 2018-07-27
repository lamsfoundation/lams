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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;
import org.opensaml.common.impl.AbstractSAMLObject;
import org.opensaml.saml2.core.BaseID;
import org.opensaml.saml2.core.EncryptedID;
import org.opensaml.saml2.core.NameID;
import org.opensaml.samlext.saml2delrestrict.Delegate;
import org.opensaml.xml.XMLObject;

/**
 * Implementation of {@link Delegate}.
 */
public class DelegateImpl extends AbstractSAMLObject implements Delegate {
    
    /** BaseID child element. */
    private BaseID baseID;
    
    /** NameID child element. */
    private NameID nameID;
    
    /** EncryptedID child element. */
    private EncryptedID encryptedID;
    
    /** DelegationInstant attribute. */
    private DateTime delegationInstant;
    
    /** ConfirmationMethod attribute. */
    private String confirmationMethod;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected DelegateImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }

    /** {@inheritDoc} */
    public BaseID getBaseID() {
        return baseID;
    }

    /** {@inheritDoc} */
    public String getConfirmationMethod() {
        return confirmationMethod;
    }

    /** {@inheritDoc} */
    public DateTime getDelegationInstant() {
        return delegationInstant;
    }

    /** {@inheritDoc} */
    public EncryptedID getEncryptedID() {
        return encryptedID;
    }

    /** {@inheritDoc} */
    public NameID getNameID() {
        return nameID;
    }

    /** {@inheritDoc} */
    public void setBaseID(BaseID newBaseID) {
        baseID = prepareForAssignment(baseID, newBaseID);
    }
    
    /** {@inheritDoc} */
    public void setConfirmationMethod(String newMethod) {
        confirmationMethod = prepareForAssignment(confirmationMethod, newMethod);
    }

    /** {@inheritDoc} */
    public void setDelegationInstant(DateTime newInstant) {
        delegationInstant = prepareForAssignment(delegationInstant, newInstant);
    }

    /** {@inheritDoc} */
    public void setEncryptedID(EncryptedID newEncryptedID) {
        encryptedID = prepareForAssignment(encryptedID, newEncryptedID);
    }

    /** {@inheritDoc} */
    public void setNameID(NameID newNameID) {
        nameID = prepareForAssignment(nameID, newNameID);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();

        if (baseID != null) {
            children.add(baseID);
        }
        if (nameID != null) {
            children.add(nameID);
        }
        if (encryptedID != null) {
            children.add(encryptedID);
        }
        return Collections.unmodifiableList(children);
    }

}
