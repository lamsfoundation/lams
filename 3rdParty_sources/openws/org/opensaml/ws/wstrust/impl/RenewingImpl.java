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

import java.util.List;

import org.opensaml.ws.wstrust.Renewing;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.schema.XSBooleanValue;

/**
 * RenewingImpl.
 * 
 */
public class RenewingImpl extends AbstractWSTrustObject implements Renewing {
    
    /** The Allow attribute value. */
    private XSBooleanValue allow;
    
    /** The OK attribute value. */
    private XSBooleanValue ok;

    /**
     * Constructor.
     * 
     * @param namespaceURI The namespace of the element
     * @param elementLocalName The local name of the element
     * @param namespacePrefix The namespace prefix of the element
     */
    public RenewingImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }

    /** {@inheritDoc} */
    public Boolean isAllow() {
        if (allow != null) {
            return allow.getValue();
        }

        // Note: Default is true here, rather than the more common default of false.
        return Boolean.TRUE;
    }

    /** {@inheritDoc} */
    public XSBooleanValue isAllowXSBoolean() {
        return allow;
    }

    /** {@inheritDoc} */
    public void setAllow(Boolean newAllow) {
        if (newAllow != null) {
            allow = prepareForAssignment(allow, new XSBooleanValue(newAllow, false));
        } else {
            allow = prepareForAssignment(allow, null);
        }        
    }

    /** {@inheritDoc} */
    public void setAllow(XSBooleanValue newAllow) {
        allow = prepareForAssignment(allow, newAllow);
    }

    /** {@inheritDoc} */
    public Boolean isOK() {
        if (ok != null) {
            return ok.getValue();
        }

        // Default is false.
        return Boolean.FALSE;
    }

    /** {@inheritDoc} */
    public XSBooleanValue isOKXSBoolean() {
        return ok;
    }

    /** {@inheritDoc} */
    public void setOK(Boolean newOK) {
        if (newOK != null) {
            ok = prepareForAssignment(ok, new XSBooleanValue(newOK, false));
        } else {
            ok = prepareForAssignment(ok, null);
        }        
    }

    /** {@inheritDoc} */
    public void setOK(XSBooleanValue newOK) {
        ok = prepareForAssignment(ok, newOK);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        return null;
    }

}
