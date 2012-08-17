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

package org.opensaml.common.binding.artifact;

import java.util.Arrays;

import org.bouncycastle.util.encoders.Hex;
import org.opensaml.xml.util.Base64;

/**
 * Base class for SAML artifacts.
 */
public abstract class AbstractSAMLArtifact {

    /** 2 byte artifact type code. */
    private byte[] typeCode;

    /**
     * Constructor.
     * 
     * @param code the artifact type code
     * 
     * @throws IllegalArgumentException thrown if the given type code is not two bytes in length
     */
    protected AbstractSAMLArtifact(byte[] code) {
        if(code.length != 2){
            throw new IllegalArgumentException("Type code was not 2-bytes in size");
        }
        typeCode = code;
    }

    /**
     * Gets the bytes for the artifact.
     * 
     * @return the bytes for the artifact
     */
    public byte[] getArtifactBytes() {
        byte[] remainingArtifact = getRemainingArtifact();
        byte[] artifact = new byte[2 + remainingArtifact.length];

        System.arraycopy(getTypeCode(), 0, artifact, 0, 2);
        System.arraycopy(remainingArtifact, 0, artifact, 2, remainingArtifact.length);

        return artifact;
    }

    /**
     * Gets the 2 byte type code for this artifact.
     * 
     * @return the type code for this artifact
     */
    public byte[] getTypeCode() {
        return typeCode;
    }

    /**
     * Sets the 2 byte type code for this artifact.
     * 
     * @param newTypeCode 2 byte type code for this artifact
     * 
     * @throws IllegalArgumentException thrown if the given type code is not two bytes
     */
    protected void setTypeCode(byte[] newTypeCode) {
        typeCode = newTypeCode;
    }

    /**
     * Gets the artifact bytes minus the type code.
     * 
     * @return artifact bytes minus the type code
     */
    public abstract byte[] getRemainingArtifact();

    /**
     * Gets the Base64 encoded artifact.
     * 
     * @return Base64 encoded artifact.
     */
    public String base64Encode() {
        return new String(Base64.encodeBytes(getArtifactBytes()));
    }

    /**
     * Gets the hex encoded artifact.
     * 
     * @return hex encoded artifact
     */
    public String hexEncode() {
        return new String(Hex.encode(getArtifactBytes()));
    }

    /** {@inheritDoc} */
    public boolean equals(Object o) {
        if(o == this){
            return true;
        }
        
        if (o instanceof AbstractSAMLArtifact) {
            AbstractSAMLArtifact otherArtifact = (AbstractSAMLArtifact) o;
            return Arrays.equals(getArtifactBytes(), otherArtifact.getArtifactBytes());
        }

        return false;
    }

    /** {@inheritDoc} */
    public int hashCode() {
        return Arrays.hashCode(getArtifactBytes());
    }
    
    /** {@inheritDoc} */
    public String toString(){
        return base64Encode();
    }
}