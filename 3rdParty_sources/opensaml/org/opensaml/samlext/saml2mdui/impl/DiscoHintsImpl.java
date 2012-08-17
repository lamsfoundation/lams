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

package org.opensaml.samlext.saml2mdui.impl;

import java.util.ArrayList;
import java.util.List;

import org.opensaml.common.impl.AbstractSAMLObject;
import org.opensaml.samlext.saml2mdui.DiscoHints;
import org.opensaml.samlext.saml2mdui.DomainHint;
import org.opensaml.samlext.saml2mdui.GeolocationHint;
import org.opensaml.samlext.saml2mdui.IPHint;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.XMLObjectChildrenList;

/** Concrete implementation of {@link org.opensaml.samlext.saml2mdui.DiscoHints}. */
public class DiscoHintsImpl extends AbstractSAMLObject implements DiscoHints {

    /** DNS Domain hints. */
    private final XMLObjectChildrenList<DomainHint> domainHints;

    /** IP Address hints. */
    private final XMLObjectChildrenList<IPHint> iPHints;

    /** GeoLocation hints. */
    private final XMLObjectChildrenList<GeolocationHint> geoHints;

    /**
     * Constructor.
     * 
     * @param namespaceURI namespaceURI
     * @param elementLocalName elementLocalName
     * @param namespacePrefix namespacePrefix
     */
    protected DiscoHintsImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        domainHints = new XMLObjectChildrenList<DomainHint>(this);
        iPHints = new XMLObjectChildrenList<IPHint>(this);
        geoHints = new XMLObjectChildrenList<GeolocationHint>(this);
    }

    /** {@inheritDoc} */
    public List<DomainHint> getDomainHints() {
        return domainHints;
    }

    /** {@inheritDoc} */
    public List<GeolocationHint> getGeolocationHints() {
        return geoHints;
    }

    /** {@inheritDoc} */
    public List<IPHint> getIPHints() {
        return iPHints;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();

        children.addAll(domainHints);
        children.addAll(iPHints);
        children.addAll(geoHints);
        return children;
    }
}
