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

package org.opensaml.saml1.binding.artifact;

import java.util.HashMap;
import java.util.Map;

import org.opensaml.xml.util.Base64;

/**
 * Factory used to construct SAML 1 artifact builders.
 */
public class SAML1ArtifactBuilderFactory {

    /** Registered artifact builders. */
    private Map<String, SAML1ArtifactBuilder> artifactBuilders;

    /** Constructor. */
    public SAML1ArtifactBuilderFactory() {
        artifactBuilders = new HashMap<String, SAML1ArtifactBuilder>(2);
        artifactBuilders.put(new String(SAML1ArtifactType0001.TYPE_CODE), new SAML1ArtifactType0001Builder());
        artifactBuilders.put(new String(SAML1ArtifactType0002.TYPE_CODE), new SAML1ArtifactType0002Builder());
    }

    /**
     * Gets the currently registered artifact builders.
     * 
     * @return currently registered artifact builders
     */
    public Map<String, SAML1ArtifactBuilder> getArtifactBuilders() {
        return artifactBuilders;
    }

    /**
     * Gets the artifact builder for the given type.
     * 
     * @param type type of artifact to be built
     * 
     * @return artifact builder for the given type
     */
    public SAML1ArtifactBuilder getArtifactBuilder(byte[] type) {
        return artifactBuilders.get(new String(type));
    }
    
    /**
     * Convenience method for getting an artifact builder and parsing the given Base64 encoded artifact with it.
     * 
     * @param base64Artifact Base64 encoded artifact to parse
     * 
     * @return constructed artifact
     */
    public AbstractSAML1Artifact buildArtifact(String base64Artifact){
        return buildArtifact(Base64.decode(base64Artifact));
    }

    /**
     * Convenience method for getting an artifact builder and parsing the given artifact with it.
     * 
     * @param artifact artifact to parse
     * 
     * @return constructed artifact
     */
    public AbstractSAML1Artifact buildArtifact(byte[] artifact) {
        if(artifact == null){
            return null;
        }
        
        byte[] type = new byte[2];
        type[0] = artifact[0];
        type[1] = artifact[1];

        SAML1ArtifactBuilder<?> artifactBuilder = getArtifactBuilder(type);
        return artifactBuilder.buildArtifact(artifact);
    }
}