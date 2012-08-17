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

import java.util.Collections;
import java.util.List;

import org.opensaml.xml.XMLObject;
import org.opensaml.xml.signature.X509Certificate;
import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.util.IndexingObjectStore;
import org.opensaml.xml.validation.AbstractValidatingXMLObject;

/** Concrete implementation of {@link X509Certificate}. */
public class X509CertificateImpl extends AbstractValidatingXMLObject implements X509Certificate {

    /** Class-level index of Base64 encoded cert values. */
    private static final IndexingObjectStore<String> B64_CERT_STORE = new IndexingObjectStore<String>();

    /** Index to a stored Base64 encoded cert. */
    private String b64CertIndex;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected X509CertificateImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }

    /** {@inheritDoc} */
    public String getValue() {
        return B64_CERT_STORE.get(b64CertIndex);
    }

    /** {@inheritDoc} */
    public void setValue(String newValue) {
        // Dump our cached DOM if the new value really is new
        String currentCert = B64_CERT_STORE.get(b64CertIndex);
        String b64Cert = prepareForAssignment(currentCert, newValue);

        // This is a new value, remove the old one, add the new one
        if (!DatatypeHelper.safeEquals(currentCert, b64Cert)) {
            B64_CERT_STORE.remove(b64CertIndex);
            b64CertIndex = B64_CERT_STORE.put(b64Cert);
        }
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        return Collections.EMPTY_LIST;
    }
    
    /** {@inheritDoc} */
    protected void finalize() throws Throwable {
        super.finalize();
        B64_CERT_STORE.remove(b64CertIndex);
    }
}