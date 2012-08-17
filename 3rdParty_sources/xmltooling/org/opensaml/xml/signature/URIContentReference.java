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

package org.opensaml.xml.signature;

import java.util.LinkedList;
import java.util.List;

import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A generic content reference that uses a URI to reference the content to be signed.
 */
public class URIContentReference implements ContentReference {

    /** Logger. */
    private final Logger log = LoggerFactory.getLogger(URIContentReference.class);

    /** Element reference ID. */
    private String referenceID;

    /** Algorithm used to digest the content . */
    private String digestAlgorithm;

    /** Transforms applied to the content. */
    private List<String> transforms;

    /**
     * Constructor. The anchor designator (#) must not be included in the ID.
     * 
     * @param referenceID the reference ID of the element to be signed
     */
    public URIContentReference(String referenceID) {
        this.referenceID = referenceID;
        transforms = new LinkedList<String>();
    }

    /**
     * Gets the transforms applied to the content prior to digest generation.
     * 
     * @return the transforms applied to the content prior to digest generation
     */
    public List<String> getTransforms() {
        return transforms;
    }

    /**
     * Gets the algorithm used to digest the content.
     * 
     * @return the algorithm used to digest the content
     */
    public String getDigestAlgorithm() {
        return digestAlgorithm;
    }

    /**
     * Sets the algorithm used to digest the content.
     * 
     * @param newAlgorithm the algorithm used to digest the content
     */
    public void setDigestAlgorithm(String newAlgorithm) {
        digestAlgorithm = newAlgorithm;
    }

    /** {@inheritDoc} */
    public void createReference(XMLSignature signature) {
        try {
            Transforms dsigTransforms = new Transforms(signature.getDocument());
            for (String transform : transforms) {
                dsigTransforms.addTransform(transform);
            }

            signature.addDocument(referenceID, dsigTransforms, digestAlgorithm);
        } catch (Exception e) {
            log.error("Error while adding content reference", e);
        }
    }
}