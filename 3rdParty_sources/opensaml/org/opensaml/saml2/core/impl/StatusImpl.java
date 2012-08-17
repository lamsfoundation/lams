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

/**
 * 
 */

package org.opensaml.saml2.core.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opensaml.common.impl.AbstractSAMLObject;
import org.opensaml.saml2.core.Status;
import org.opensaml.saml2.core.StatusCode;
import org.opensaml.saml2.core.StatusDetail;
import org.opensaml.saml2.core.StatusMessage;
import org.opensaml.xml.XMLObject;

/**
 * Concrete implementation of {@link org.opensaml.saml2.core.Status}.
 */
public class StatusImpl extends AbstractSAMLObject implements Status {

    /** StatusCode element. */
    private StatusCode statusCode;

    /** StatusMessage element. */
    private StatusMessage statusMessage;

    /** StatusDetail element. */
    private StatusDetail statusDetail;

    /**
     * Constructor.
     * 
     * @param namespaceURI namespace uri
     * @param elementLocalName element name
     * @param namespacePrefix namespace prefix
     */
    protected StatusImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }

    /** {@inheritDoc} */
    public StatusCode getStatusCode() {
        return this.statusCode;
    }

    /** {@inheritDoc} */
    public void setStatusCode(StatusCode newStatusCode) {
        this.statusCode = prepareForAssignment(this.statusCode, newStatusCode);

    }

    /** {@inheritDoc} */
    public StatusMessage getStatusMessage() {
        return this.statusMessage;
    }

    /** {@inheritDoc} */
    public void setStatusMessage(StatusMessage newStatusMessage) {
        this.statusMessage = prepareForAssignment(this.getStatusMessage(), newStatusMessage);
    }

    /** {@inheritDoc} */
    public StatusDetail getStatusDetail() {
        return this.statusDetail;
    }

    /** {@inheritDoc} */
    public void setStatusDetail(StatusDetail newStatusDetail) {
        this.statusDetail = prepareForAssignment(this.statusDetail, newStatusDetail);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();

        children.add(statusCode);
        if (statusMessage != null) {
            children.add(statusMessage);
        }
        if (statusDetail != null) {
            children.add(statusDetail);
        }
        return Collections.unmodifiableList(children);
    }
}