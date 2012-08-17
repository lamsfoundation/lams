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

package org.opensaml.xml;

import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.util.XMLConstants;

/** Data structure for representing XML namespace attributes. */
public class Namespace {

    /** URI of the namespace. */
    private String namespaceURI;

    /** Prefix of the namespace. */
    private String namespacePrefix;

    /** Always declare this namespace while marshalling? */
    private boolean alwaysDeclare;

    /** String representation of this namespace. */
    private String nsStr;

    /** Constructor. */
    public Namespace() {

    }

    /**
     * Constructor.
     * 
     * @param uri the URI of the namespace
     * @param prefix the prefix of the namespace
     */
    public Namespace(String uri, String prefix) {
        namespaceURI = DatatypeHelper.safeTrimOrNullString(uri);
        namespacePrefix = DatatypeHelper.safeTrimOrNullString(prefix);
        nsStr = null;
    }

    /**
     * Gets the prefix of the namespace.
     * 
     * @return the prefix of the namespace, may be null if this is a default namespace
     */
    public String getNamespacePrefix() {
        return namespacePrefix;
    }

    /**
     * Sets the prefix of the namespace.
     * 
     * @param newPrefix the prefix of the namespace
     */
    public void setNamespacePrefix(String newPrefix) {
        namespacePrefix = DatatypeHelper.safeTrimOrNullString(newPrefix);
        nsStr = null;
    }

    /**
     * Gets the URI of the namespace.
     * 
     * @return the URI of the namespace
     */
    public String getNamespaceURI() {
        return namespaceURI;
    }

    /**
     * Sets the URI of the namespace.
     * 
     * @param newURI the URI of the namespace
     */
    public void setNamespaceURI(String newURI) {
        namespaceURI = DatatypeHelper.safeTrimOrNullString(newURI);
        nsStr = null;
    }

    /**
     * Gets wether this namespace should always be declared when marshalling, even if it was already declared on an
     * ancestral element.
     * 
     * @return true if this namespace should always be declared, false if not
     * 
     * @deprecated use appropriate methods on the XMLObject's {@link NamespaceManager}.
     */
    public boolean alwaysDeclare() {
        return alwaysDeclare;
    }

    /**
     * Sets wether this namespace should always be declared when marshalling, even if it was already declared on an
     * ancestral element.
     * 
     * @param shouldAlwaysDeclare true if this namespace should always be declared, false if not
     * 
     * @deprecated use appropriate methods on the XMLObject's {@link NamespaceManager}.
     */
    public void setAlwaysDeclare(boolean shouldAlwaysDeclare) {
        alwaysDeclare = shouldAlwaysDeclare;
    }

    /** {@inheritDoc} */
    public String toString() {
        if (nsStr == null) {
            constructStringRepresentation();
        }

        return nsStr;
    }

    /** {@inheritDoc} */
    public int hashCode() {
        int hash = 1;
        hash = hash * 31 + toString().hashCode();
        hash = hash * 31 + (alwaysDeclare ? 0 : 1);
        return hash;
    }

    /**
     * Checks if the given object is the same as this Namespace. This is true if:
     * <ul>
     * <li>The given object is of type {@link Namespace}</li>
     * <li>The given object's namespace URI is the same as this object's namespace URI</li>
     * <li>The given object's namespace prefix is the same as this object's namespace prefix</li>
     * </ul>
     * 
     * @param obj {@inheritDoc}
     * 
     * @return {@inheritDoc}
     */
    public boolean equals(Object obj) {    
        if(obj == this){
            return true;
        }
        
        if (obj instanceof Namespace) {
            Namespace otherNamespace = (Namespace) obj;
            if (DatatypeHelper.safeEquals(otherNamespace.getNamespaceURI(), getNamespaceURI())){
                if (DatatypeHelper.safeEquals(otherNamespace.getNamespacePrefix(), getNamespacePrefix())){
                    return otherNamespace.alwaysDeclare() == alwaysDeclare();
                }
            }
        }

        return false;
    }

    /** Constructs an XML namespace declaration string representing this namespace. */
    protected void constructStringRepresentation() {
        StringBuffer stringRep = new StringBuffer();

        stringRep.append(XMLConstants.XMLNS_PREFIX);

        if (namespacePrefix != null) {
            stringRep.append(":");
            stringRep.append(namespacePrefix);
        }

        stringRep.append("=\"");

        if (namespaceURI != null) {
            stringRep.append(namespaceURI);
        }

        stringRep.append("\"");

        nsStr = stringRep.toString();
    }
}