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

import java.util.Arrays;

import org.opensaml.xml.util.DatatypeHelper;

/**
 * SAML 1 Type 0x0002 Artifact. SAML 1, type 2, artifacts contains a 2 byte type code with a value of 1 followed by a 20
 * byte assertion handle followed by an unspecified number of bytes which are a UTF-8 encoded source location string.
 */
public class SAML1ArtifactType0002 extends AbstractSAML1Artifact {

    /** Artifact type code (0x0002). */
    public static final byte[] TYPE_CODE = { 0, 2 };

    /** 20 byte assertion handle. */
    private byte[] assertionHandle;

    /** Artifact source location component. */
    private String sourceLocation;

    /** Constructor. */
    public SAML1ArtifactType0002() {
        super(TYPE_CODE);
    }

    /**
     * Constructor.
     * 
     * @param handle 20 byte assertion handle artifact component
     * @param location source location artifact component
     * 
     * @throws IllegalArgumentException thrown if the given assertion handle is not 20 bytes or the source location is
     *             null or empty
     */
    public SAML1ArtifactType0002(byte[] handle, String location) {
        super(TYPE_CODE);

        setAssertionHandle(handle);
        setSourceLocation(location);
    }

    /**
     * Constructs a SAML 1 artifact from its byte representation.
     * 
     * @param artifact the byte array representing the artifact
     * @return the artifact parsed from the byte representation
     * 
     * @throws IllegalArgumentException thrown if the artifact type is not 0x0002
     */
    public static SAML1ArtifactType0002 parseArtifact(byte[] artifact) {
        byte[] typeCode = { artifact[0], artifact[1] };
        if (!Arrays.equals(typeCode, TYPE_CODE)) {
            throw new IllegalArgumentException("Artifact is not of appropriate type.");
        }

        byte[] assertionHandle = new byte[20];
        System.arraycopy(artifact, 2, assertionHandle, 0, 20);

        int locationLength = artifact.length - 22;
        byte[] sourceLocation = new byte[locationLength];
        System.arraycopy(artifact, 22, sourceLocation, 0, locationLength);

        return new SAML1ArtifactType0002(assertionHandle, new String(sourceLocation));
    }

    /**
     * Gets the artifiact's 20 byte assertion handle.
     * 
     * @return artifiact's 20 byte assertion handle
     */
    public byte[] getAssertionHandle() {
        return assertionHandle;
    }

    /**
     * Sets the artifiact's 20 byte assertion handle.
     * 
     * @param handle artifiact's 20 byte assertion handle
     */
    public void setAssertionHandle(byte[] handle) {
        if (handle.length != 20) {
            throw new IllegalArgumentException("Artifact assertion handle must be 20 bytes long");
        }
        assertionHandle = handle;
    }

    /**
     * Gets the source location component of this artifact.
     * 
     * @return source location component of this artifact
     */
    public String getSourceLocation() {
        return sourceLocation;
    }

    /**
     * Sets source location component of this artifact.
     * 
     * @param newLocation source location component of this artifact
     * 
     * @throws IllegalArgumentException thrown if the given location is empty or null
     */
    protected void setSourceLocation(String newLocation) {
        String location = DatatypeHelper.safeTrimOrNullString(newLocation);
        if (location == null) {
            throw new IllegalArgumentException("Artifact source location may not be a null or empty string");
        }

        sourceLocation = location;
    }

    /** {@inheritDoc} */
    public byte[] getRemainingArtifact() {
        byte[] location = getSourceLocation().getBytes();
        byte[] remainingArtifact = new byte[20 + location.length];

        System.arraycopy(getAssertionHandle(), 0, remainingArtifact, 0, 20);
        System.arraycopy(location, 0, remainingArtifact, 20, location.length);

        return remainingArtifact;
    }
}