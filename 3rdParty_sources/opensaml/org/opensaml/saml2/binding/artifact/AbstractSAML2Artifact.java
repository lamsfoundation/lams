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

import org.opensaml.common.binding.artifact.AbstractSAMLArtifact;

/**
 * SAML 2 Artifact base class. SAML 2 artifacts contains a 2 byte type code followed by a 2 byte endpoint index followed
 * by remaining artifact data.
 */
public abstract class AbstractSAML2Artifact extends AbstractSAMLArtifact {

    /** 2 byte artifact endpoint index. */
    private byte[] endpointIndex;

    /**
     * Constructor.
     * 
     * @param artifactType artifact type code
     */
    protected AbstractSAML2Artifact(byte[] artifactType) {
        super(artifactType);
    }

    /**
     * Constructor.
     * 
     * @param artifactType artifact type code
     * @param index 2 byte endpoint index of the artifact
     * 
     * @throws IllegalArgumentException thrown if the endpoint index, source ID, or message handle arrays are not of the
     *             right size
     */
    public AbstractSAML2Artifact(byte[] artifactType, byte[] index) {
        super(artifactType);
        setEndpointIndex(index);
    }

    /**
     * Gets the bytes for the artifact.
     * 
     * @return the bytes for the artifact
     */
    public byte[] getArtifactBytes() {
        byte[] remainingArtifact = getRemainingArtifact();
        byte[] artifact = new byte[4 + remainingArtifact.length];

        System.arraycopy(getTypeCode(), 0, artifact, 0, 2);
        System.arraycopy(getEndpointIndex(), 0, artifact, 2, 2);
        System.arraycopy(remainingArtifact, 0, artifact, 4, remainingArtifact.length);

        return artifact;
    }

    /**
     * Gets the 2 byte endpoint index for this artifact.
     * 
     * @return 2 byte endpoint index for this artifact
     */
    public byte[] getEndpointIndex() {
        return endpointIndex;
    }

    /**
     * Sets the 2 byte endpoint index for this artifact.
     * 
     * @param newIndex 2 byte endpoint index for this artifact
     * 
     * @throws IllegalArgumentException thrown if the given index is not 2 bytes
     */
    public void setEndpointIndex(byte[] newIndex) {
        if (newIndex.length != 2) {
            throw new IllegalArgumentException("Artifact endpoint index must be two bytes long");
        }

        endpointIndex = newIndex;
    }
}