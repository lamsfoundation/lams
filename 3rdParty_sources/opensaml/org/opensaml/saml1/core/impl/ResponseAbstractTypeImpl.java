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

package org.opensaml.saml1.core.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;
import org.opensaml.common.SAMLVersion;
import org.opensaml.common.impl.AbstractSignableSAMLObject;
import org.opensaml.saml1.core.ResponseAbstractType;
import org.opensaml.xml.XMLObject;

/**
 * Abstract implementation of {@link org.opensaml.saml1.core.ResponseAbstractType} Object
 */
public abstract class ResponseAbstractTypeImpl extends AbstractSignableSAMLObject implements ResponseAbstractType {

    /** Contains the ID */
    private String id;

    private SAMLVersion version;

    /** Contents of the InResponseTo attribute */
    private String inResponseTo = null;

    /** Contents of the Date attribute */
    private DateTime issueInstant = null;

    /** Contents of the recipient attribute */
    private String recipient = null;

    /**
     * Constructor
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected ResponseAbstractTypeImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        version = SAMLVersion.VERSION_11;
    }

    /** {@inheritDoc} */
    public String getID() {
        return id;
    }

    /** {@inheritDoc} */
    public void setID(String id) {
        String oldID = this.id;
        this.id = prepareForAssignment(this.id, id);
        registerOwnID(oldID, this.id);
    }

    /** {@inheritDoc} */
    public String getInResponseTo() {
        return inResponseTo;
    }

    /** {@inheritDoc} */
    public void setInResponseTo(String inResponseTo) {
        this.inResponseTo = prepareForAssignment(this.inResponseTo, inResponseTo);
    }

    /** {@inheritDoc} */
    public int getMinorVersion() {
        return version.getMinorVersion();
    }

    /** {@inheritDoc} */
    public int getMajorVersion() {
        return version.getMajorVersion();
    }

    /** {@inheritDoc} */
    public void setVersion(SAMLVersion newVersion) {
        version = prepareForAssignment(version, newVersion);
    }

    /** {@inheritDoc} */
    public DateTime getIssueInstant() {

        return issueInstant;
    }

    /** {@inheritDoc} */
    public void setIssueInstant(DateTime date) {
        this.issueInstant = prepareForAssignment(this.issueInstant, date);
    }

    /** {@inheritDoc} */
    public String getRecipient() {
        return recipient;
    }

    /** {@inheritDoc} */
    public void setRecipient(String recipient) {
        this.recipient = prepareForAssignment(this.recipient, recipient);
    }
    
    /** {@inheritDoc} */
    public String getSignatureReferenceID(){
        return id;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        List<XMLObject> children = new ArrayList<XMLObject>();
        
        if(getSignature() != null){
            children.add(getSignature());
        }
        
        return Collections.unmodifiableList(children);
    }
}