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

import org.opensaml.xml.XMLObject;
import org.opensaml.xml.signature.DSAKeyValue;
import org.opensaml.xml.signature.G;
import org.opensaml.xml.signature.J;
import org.opensaml.xml.signature.P;
import org.opensaml.xml.signature.PgenCounter;
import org.opensaml.xml.signature.Q;
import org.opensaml.xml.signature.Seed;
import org.opensaml.xml.signature.Y;
import org.opensaml.xml.validation.AbstractValidatingXMLObject;

/**
 * Concrete implementation of {@link org.opensaml.xml.signature.DSAKeyValue}
 */
public class DSAKeyValueImpl extends AbstractValidatingXMLObject implements DSAKeyValue {
    
    /** P child element */
    private P p;
    
    /** Q child element */
    private Q q;
    
    /** G child element */
    private G g;
    
    /** Y child element */
    private Y y;
    
    /** J child element */
    private J j;
    
    /** Seed child element */
    private Seed seed;
    
    /** PgenCounter child element */
    private PgenCounter pgenCounter;

    /**
     * Constructor
     *
     * @param namespaceURI
     * @param elementLocalName
     * @param namespacePrefix
     */
    protected DSAKeyValueImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }

    /** {@inheritDoc} */
    public P getP() {
        return this.p;
    }

    /** {@inheritDoc} */
    public void setP(P newP) {
        this.p = prepareForAssignment(this.p, newP);
    }

    /** {@inheritDoc} */
    public Q getQ() {
        return this.q;
    }

    /** {@inheritDoc} */
    public void setQ(Q newQ) {
        this.q = prepareForAssignment(this.q, newQ);
    }

    /** {@inheritDoc} */
    public G getG() {
        return this.g;
    }

    /** {@inheritDoc} */
    public void setG(G newG) {
        this.g = prepareForAssignment(this.g, newG);
    }

    /** {@inheritDoc} */
    public Y getY() {
        return this.y;
    }

    /** {@inheritDoc} */
    public void setY(Y newY) {
        this.y = prepareForAssignment(this.y, newY);
    }

    /** {@inheritDoc} */
    public J getJ() {
        return this.j;
    }

    /** {@inheritDoc} */
    public void setJ(J newJ) {
        this.j = prepareForAssignment(this.j, newJ);
    }

    /** {@inheritDoc} */
    public Seed getSeed() {
        return this.seed;
    }

    /** {@inheritDoc} */
    public void setSeed(Seed newSeed) {
        this.seed = prepareForAssignment(this.seed, newSeed);
    }

    /** {@inheritDoc} */
    public PgenCounter getPgenCounter() {
        return this.pgenCounter;
    }

    /** {@inheritDoc} */
    public void setPgenCounter(PgenCounter newPgenCounter) {
        this.pgenCounter = prepareForAssignment(this.pgenCounter, newPgenCounter);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();
        
        if (p != null) {
            children.add(p);
        }
        if (q!= null) {
            children.add(q);
        }
        if (g != null) {
            children.add(g);
        }
        if (y != null) {
            children.add(y);
        }
        if (j != null) {
            children.add(j);
        }
        if (seed!= null) {
            children.add(seed);
        }
        if (pgenCounter != null) {
            children.add(pgenCounter);
        }
        
        if (children.size() == 0) {
            return null;
        }
        
        return Collections.unmodifiableList(children);
    }

}
