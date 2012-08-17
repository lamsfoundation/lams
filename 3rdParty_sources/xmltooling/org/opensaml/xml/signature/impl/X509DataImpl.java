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

package org.opensaml.xml.signature.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

import org.opensaml.xml.XMLObject;
import org.opensaml.xml.signature.X509CRL;
import org.opensaml.xml.signature.X509Certificate;
import org.opensaml.xml.signature.X509Data;
import org.opensaml.xml.signature.X509IssuerSerial;
import org.opensaml.xml.signature.X509SKI;
import org.opensaml.xml.signature.X509SubjectName;
import org.opensaml.xml.util.IndexedXMLObjectChildrenList;
import org.opensaml.xml.validation.AbstractValidatingXMLObject;

/** Concrete implementation of {@link X509Data}. */
public class X509DataImpl extends AbstractValidatingXMLObject implements X509Data {

    /** The list of XMLObject child elements. */
    private final IndexedXMLObjectChildrenList indexedChildren;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected X509DataImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        indexedChildren = new IndexedXMLObjectChildrenList(this);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getXMLObjects() {
        return (List<XMLObject>) this.indexedChildren;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getXMLObjects(QName typeOrName) {
        return (List<XMLObject>) this.indexedChildren.subList(typeOrName);
    }

    /** {@inheritDoc} */
    public List<X509IssuerSerial> getX509IssuerSerials() {
        return (List<X509IssuerSerial>) this.indexedChildren.subList(X509IssuerSerial.DEFAULT_ELEMENT_NAME);
    }

    /** {@inheritDoc} */
    public List<X509SKI> getX509SKIs() {
        return (List<X509SKI>) this.indexedChildren.subList(X509SKI.DEFAULT_ELEMENT_NAME);
    }

    /** {@inheritDoc} */
    public List<X509SubjectName> getX509SubjectNames() {
        return (List<X509SubjectName>) this.indexedChildren.subList(X509SubjectName.DEFAULT_ELEMENT_NAME);
    }

    /** {@inheritDoc} */
    public List<X509Certificate> getX509Certificates() {
        return (List<X509Certificate>) this.indexedChildren.subList(X509Certificate.DEFAULT_ELEMENT_NAME);
    }

    /** {@inheritDoc} */
    public List<X509CRL> getX509CRLs() {
        return (List<X509CRL>) this.indexedChildren.subList(X509CRL.DEFAULT_ELEMENT_NAME);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();

        children.addAll((List<XMLObject>) indexedChildren);

        if (children.size() == 0) {
            return null;
        }

        return Collections.unmodifiableList(children);
    }

}
