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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opensaml.ws.wsaddressing.Action;
import org.opensaml.ws.wsaddressing.ProblemAction;
import org.opensaml.ws.wsaddressing.SoapAction;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.AttributeMap;

/**
 * Implementation of {@link ProblemAction}.
 */
public class ProblemActionImpl extends AbstractWSAddressingObject implements ProblemAction {
    
    /** Action child element. */
    private Action action;
    
    /** SoapAction child element. */
    private SoapAction soapAction;
    
    /** Wildcard attributes. */
    private AttributeMap unknownAttributes;

    /**
     * Constructor.
     * 
     * @param namespaceURI The namespace of the element
     * @param elementLocalName The local name of the element
     * @param namespacePrefix The namespace prefix of the element
     */
    public ProblemActionImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        unknownAttributes = new AttributeMap(this);
    }

    /** {@inheritDoc} */
    public Action getAction() {
        return action;
    }

    /** {@inheritDoc} */
    public SoapAction getSoapAction() {
        return soapAction;
    }

    /** {@inheritDoc} */
    public void setAction(Action newAction) {
        action = prepareForAssignment(action, newAction);
    }

    /** {@inheritDoc} */
    public void setSoapAction(SoapAction newSoapAction) {
        soapAction = prepareForAssignment(soapAction, newSoapAction);
    }

    /** {@inheritDoc} */
    public AttributeMap getUnknownAttributes() {
        return unknownAttributes;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();
        if (action != null) {
            children.add(action);
        }
        if (soapAction != null) {
            children.add(soapAction);
        }

        return Collections.unmodifiableList(children);
    }

}
