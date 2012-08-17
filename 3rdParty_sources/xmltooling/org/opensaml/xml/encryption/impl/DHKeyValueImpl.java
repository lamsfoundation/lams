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

package org.opensaml.xml.encryption.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opensaml.xml.XMLObject;
import org.opensaml.xml.encryption.DHKeyValue;
import org.opensaml.xml.encryption.Generator;
import org.opensaml.xml.encryption.P;
import org.opensaml.xml.encryption.PgenCounter;
import org.opensaml.xml.encryption.Public;
import org.opensaml.xml.encryption.Q;
import org.opensaml.xml.encryption.Seed;
import org.opensaml.xml.validation.AbstractValidatingXMLObject;

/**
 * Concrete implementation of {@link org.opensaml.xml.encryption.DHKeyValue}.
 */
public class DHKeyValueImpl extends AbstractValidatingXMLObject implements DHKeyValue {
    
    /** P child element. */
    private P p;
    
    /** Q child element. */
    private Q q;
    
    /** Generator child element. */
    private Generator generator;
    
    /** Public element. */
    private Public publicChild;
    
    /** seed child element. */
    private Seed seed;
    
    /** pgenCounter child element. */
    private PgenCounter pgenCounter;

    /**
     * Constructor.
     *
     * @param namespaceURI namespace URI
     * @param elementLocalName local name
     * @param namespacePrefix namespace prefix
     */
    protected DHKeyValueImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
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
    public Generator getGenerator() {
        return this.generator;
    }

    /** {@inheritDoc} */
    public void setGenerator(Generator newGenerator) {
        this.generator = prepareForAssignment(this.generator, newGenerator);
    }

    /** {@inheritDoc} */
    public Public getPublic() {
        return this.publicChild;
    }

    /** {@inheritDoc} */
    public void setPublic(Public newPublic) {
        this.publicChild = prepareForAssignment(this.publicChild, newPublic);
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
        if (generator != null) {
            children.add(generator);
        }
        if (publicChild != null) {
            children.add(publicChild);
        }
        if (seed != null) {
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
