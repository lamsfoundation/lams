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

package org.opensaml.saml2.binding.artifact;

import org.opensaml.common.SAMLObject;
import org.opensaml.common.binding.SAMLMessageContext;
import org.opensaml.saml2.core.NameID;

/**
 * Builder of typed SAML 2 artifacts.
 * 
 * Builders must be thread safe and reusable.
 * 
 * @param <ArtifactType> type of artifact built by this builder
 */
public interface SAML2ArtifactBuilder<ArtifactType extends AbstractSAML2Artifact> {

    /**
     * Builds an artifact, for the given assertion, destined for the outbound message recipient.
     * 
     * @param requestContext request context
     * 
     * @return constructed artifcate
     */
    public ArtifactType buildArtifact(SAMLMessageContext<SAMLObject, SAMLObject, NameID> requestContext);

    /**
     * Builds a populated artifact given the artifact's byte-array representation.
     * 
     * @param artifact the byte representation of the artifact
     * 
     * @return populated artifact
     */
    public ArtifactType buildArtifact(byte[] artifact);

}
