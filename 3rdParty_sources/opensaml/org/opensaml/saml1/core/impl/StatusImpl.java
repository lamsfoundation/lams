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

import org.opensaml.common.impl.AbstractSAMLObject;
import org.opensaml.saml1.core.Status;
import org.opensaml.saml1.core.StatusCode;
import org.opensaml.saml1.core.StatusDetail;
import org.opensaml.saml1.core.StatusMessage;
import org.opensaml.xml.XMLObject;

/**
 * Concrete Implementation {@link org.opensaml.saml1.core.Status}
 */
public class StatusImpl extends AbstractSAMLObject implements Status {

    /** Representation of the StatusMessage element. */
    private StatusMessage statusMessage;

    /** Representation of the StatusCode element. */
    private StatusCode statusCode;

    /** Representation of the StatusDetail element. */
    private StatusDetail statusDetail;

    /**
     * Constructor
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected StatusImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }

    /** {@inheritDoc} */
    public StatusMessage getStatusMessage() {
        return statusMessage;
    }

    /** {@inheritDoc} */
    public void setStatusMessage(StatusMessage statusMessage) throws IllegalArgumentException {
        this.statusMessage = prepareForAssignment(this.statusMessage, statusMessage);
    }

    /** {@inheritDoc} */
    public StatusCode getStatusCode() {
        return statusCode;
    }

    /** {@inheritDoc} */
    public void setStatusCode(StatusCode statusCode) throws IllegalArgumentException {
        this.statusCode = prepareForAssignment(this.statusCode, statusCode);
    }

    /** {@inheritDoc} */
    public StatusDetail getStatusDetail() {
        return statusDetail;
    }

    /** {@inheritDoc} */
    public void setStatusDetail(StatusDetail statusDetail) throws IllegalArgumentException {
        this.statusDetail = prepareForAssignment(this.statusDetail, statusDetail);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>(3);

        if (statusCode != null) {
            children.add(statusCode);
        }

        if (statusMessage != null) {
            children.add(statusMessage);
        }

        if (statusDetail != null) {
            children.add(statusDetail);
        }

        if (children.size() == 0) {
            return null;
        }

        return Collections.unmodifiableList(children);
    }
}