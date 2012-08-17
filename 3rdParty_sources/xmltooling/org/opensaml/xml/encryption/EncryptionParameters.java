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

package org.opensaml.xml.encryption;

import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.keyinfo.KeyInfoGenerator;

/**
 * Parameters for encrypting XMLObjects.
 */
public class EncryptionParameters {

    /** Credential used to encrypt. */
    private Credential encryptionCredential;

    /** XML Encryption algorithm URI used to encrypt. */
    private String algorithm;

    /** Generator for dynamically generating a KeyInfo instance containing information
     * from the encryption credential. */
    private KeyInfoGenerator keyInfoGenerator;

    /**
     * Constructor.
     */
    public EncryptionParameters() {
        // This will be the default for auto encryption key generation
        setAlgorithm(EncryptionConstants.ALGO_ID_BLOCKCIPHER_AES256);
    }

    /**
     * Gets the XML Encryption algorithm URI used to encrypt.
     * 
     * @return the algorithm URI used to encrypt
     */
    public String getAlgorithm() {
        return this.algorithm;
    }

    /**
     * Sets the XML Encryption algorithm URI used to encrypt.
     * 
     * @param newAlgorithm the algorithm URI used to encrypt
     */
    public void setAlgorithm(String newAlgorithm) {
        this.algorithm = newAlgorithm;
    }

    /**
     * Gets the credential used to encrypt.
     * 
     * @return the credential used to encrypt
     */
    public Credential getEncryptionCredential() {
        return this.encryptionCredential;
    }

    /**
     * Sets the credential used to encrypt.
     * 
     * @param newEncryptionCredential the credential used to encrypt
     */
    public void setEncryptionCredential(Credential newEncryptionCredential) {
        this.encryptionCredential = newEncryptionCredential;
    }

    /**
     * Gets the instance which will be used to generate a KeyInfo
     * object from the encryption credential.
     * 
     * @return the generator instance
     */
    public KeyInfoGenerator getKeyInfoGenerator() {
        return this.keyInfoGenerator;
    }

    /**
     * Sets the instance which will be used to generate a KeyInfo
     * object from the encryption credential.
     * 
     * @param newKeyInfoGenerator the new generator instance
     */
    public void setKeyInfoGenerator(KeyInfoGenerator newKeyInfoGenerator) {
        this.keyInfoGenerator = newKeyInfoGenerator;
    }
}