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
import org.opensaml.saml1.core.RequestAbstractType;
import org.opensaml.saml1.core.RespondWith;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.XMLObjectChildrenList;

/**
 * Implementation of {@link org.opensaml.saml1.core.RequestAbstractType}.
 */
public abstract class RequestAbstractTypeImpl extends AbstractSignableSAMLObject implements RequestAbstractType {

    /** Contains the ID */
    private String id;

    /** Containt the IssueInstant */
    private DateTime issueInstant;

    /** Version of this SAML message */
    private SAMLVersion version;

    /** Contains the respondWiths */
    public final XMLObjectChildrenList<RespondWith> respondWiths;

    /**
     * Constructor
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected RequestAbstractTypeImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        version = SAMLVersion.VERSION_11;
        respondWiths = new XMLObjectChildrenList<RespondWith>(this);
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
    public int getMajorVersion() {
        return version.getMajorVersion();
    }

    /** {@inheritDoc} */
    public int getMinorVersion() {
        return version.getMinorVersion();
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
    public void setIssueInstant(DateTime instant) {
        this.issueInstant = prepareForAssignment(this.issueInstant, instant);
    }

    /** {@inheritDoc} */
    public List<RespondWith> getRespondWiths() {
        return respondWiths;
    }
    
    /** {@inheritDoc} */
    public String getSignatureReferenceID(){
        return id;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        List<XMLObject> children = new ArrayList<XMLObject>();

        children.addAll(respondWiths);
        
        if(getSignature() != null){
            children.add(getSignature());
        }
        
        return Collections.unmodifiableList(children);
    }
}