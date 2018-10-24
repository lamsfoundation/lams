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

package org.opensaml.common;

import java.util.List;

import org.opensaml.common.impl.SAMLObjectContentReference;
import org.opensaml.xml.Namespace;
import org.opensaml.xml.NamespaceManager;
import org.opensaml.xml.signature.ContentReference;
import org.opensaml.xml.signature.SignatureConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A helper class for working with SAMLObjects.
 */
public final class SAMLObjectHelper {
    
    /** Constructor. */
    private SAMLObjectHelper() { }
    
    /**
     * Examines the {@link SignableSAMLObject} for the need to declare non-visible namespaces 
     * before marshalling and signing, and if required, performs the declarations.
     * 
     * <p>
     * If the object does not already have a cached DOM, does have a signature attached,
     * and the signature contains a {@link SAMLObjectContentReference} with a transform of either 
     * {@link SignatureConstants#TRANSFORM_C14N_EXCL_OMIT_COMMENTS}
     * or {@link SignatureConstants#TRANSFORM_C14N_EXCL_WITH_COMMENTS}, 
     * it declares on the object all non-visible namespaces
     * as determined by {@link NamespaceManager#getNonVisibleNamespaces()}.
     * </p>
     * 
     * @param signableObject the signable SAML object to evaluate
     */
    public static void declareNonVisibleNamespaces(SignableSAMLObject signableObject) {
        Logger log = getLogger();
        if (signableObject.getDOM() == null && signableObject.getSignature() != null) {
            log.debug("Examing signed object for content references with exclusive canonicalization transform");
            boolean sawExclusive = false;
            for (ContentReference cr : signableObject.getSignature().getContentReferences()) {
                if (cr instanceof SAMLObjectContentReference) {
                    List<String> transforms = ((SAMLObjectContentReference)cr).getTransforms();
                    if (transforms.contains(SignatureConstants.TRANSFORM_C14N_EXCL_WITH_COMMENTS) 
                            || transforms.contains(SignatureConstants.TRANSFORM_C14N_EXCL_OMIT_COMMENTS)) {
                        sawExclusive = true;
                        break;
                    }
                }
            }
            
            if (sawExclusive) {
                log.debug("Saw exclusive transform, declaring non-visible namespaces on signed object");
                for (Namespace ns : signableObject.getNamespaceManager().getNonVisibleNamespaces()) {
                    signableObject.getNamespaceManager().registerNamespaceDeclaration(ns);
                }
            }
        }
    }
    
    /**
     * Get an SLF4J Logger.
     * 
     * @return a Logger instance
     */
    private static Logger getLogger() {
        return LoggerFactory.getLogger(SAMLObjectHelper.class);
    }

}
