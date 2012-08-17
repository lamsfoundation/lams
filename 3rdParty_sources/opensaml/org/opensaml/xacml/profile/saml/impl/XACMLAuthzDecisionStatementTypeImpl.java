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

package org.opensaml.xacml.profile.saml.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opensaml.common.impl.AbstractSAMLObject;
import org.opensaml.xacml.ctx.RequestType;
import org.opensaml.xacml.ctx.ResponseType;
import org.opensaml.xacml.profile.saml.XACMLAuthzDecisionStatementType;
import org.opensaml.xml.XMLObject;

/** A concrete implementation of {@link org.opensaml.xacml.profile.saml.XACMLAuthzDecisionStatementType}. */
public class XACMLAuthzDecisionStatementTypeImpl extends AbstractSAMLObject implements XACMLAuthzDecisionStatementType {

    /** The request of the authorization request. */
    private RequestType request;

    /** The response of the authorization request. */
    private ResponseType response;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected XACMLAuthzDecisionStatementTypeImpl(String namespaceURI, String elementLocalName,
            String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }

    /** {@inheritDoc} */
    public RequestType getRequest() {
        return request;
    }

    /** {@inheritDoc} */
    public ResponseType getResponse() {
        return response;
    }

    /** {@inheritDoc} */
    public void setRequest(RequestType request) {
        this.request = prepareForAssignment(this.request, request);
    }

    /** {@inheritDoc} */
    public void setResponse(ResponseType response) {
        this.response = prepareForAssignment(this.response, response);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();

        if (request != null) {
            children.add(request);
        }
        if (response != null) {
            children.add(response);
        }

        return Collections.unmodifiableList(children);
    }
}
