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

package org.opensaml.ws.wstrust.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opensaml.ws.wstrust.Code;
import org.opensaml.ws.wstrust.Reason;
import org.opensaml.ws.wstrust.Status;
import org.opensaml.xml.XMLObject;

/**
 * StatusImpl.
 * 
 */
public class StatusImpl extends AbstractWSTrustObject implements Status {

    /** The Code child element. */
    private Code code;

    /** The Reason child element. */
    private Reason reason;

    /**
     * Constructor.
     * 
     * @param namespaceURI The namespace of the element
     * @param elementLocalName The local name of the element
     * @param namespacePrefix The namespace prefix of the element
     */
    public StatusImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }

    /** {@inheritDoc} */
    public Code getCode() {
        return code;
    }

    /** {@inheritDoc} */
    public Reason getReason() {
        return reason;
    }

    /** {@inheritDoc} */
    public void setCode(Code newCode) {
        code = prepareForAssignment(code, newCode);
    }

    /** {@inheritDoc} */
    public void setReason(Reason newReason) {
        reason = prepareForAssignment(reason, newReason);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();
        if (code != null) {
            children.add(code);
        }
        if (reason != null) {
            children.add(reason);
        }
        return Collections.unmodifiableList(children);
    }

}
