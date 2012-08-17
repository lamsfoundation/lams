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

import org.opensaml.xml.util.XMLConstants;

/**
 * Constants defined in or related to the XML Encryption specification, version 20021210.
 */
public class EncryptionConstants {
    
    // *********************************************************
    // URI values which represent type attribute values
    // *********************************************************
    /** URI for Content. */
    public static final String TYPE_CONTENT                = XMLConstants.XMLENC_NS + "Content";
    
    /** URI for Element. */
    public static final String TYPE_ELEMENT                = XMLConstants.XMLENC_NS + "Element";
    
    /** URI for EncryptionProperties. */
    public static final String TYPE_ENCRYPTION_PROPERTIES =  XMLConstants.XMLENC_NS + "EncryptionProperties";
    
    /** URI for EncryptedKey. */
    public static final String TYPE_ENCRYPTED_KEY          = XMLConstants.XMLENC_NS + "EncryptedKey";
    
    /** URI for DHKeyValue. */
    public static final String TYPE_KEYINFO_DH_KEYVALUE =  XMLConstants.XMLENC_NS + "DHKeyValue";
    

    // *************************************************
    // Block encryption algorithms
    // *************************************************
    /** Block Encryption - REQUIRED TRIPLEDES. */
    public static final String ALGO_ID_BLOCKCIPHER_TRIPLEDES = XMLConstants.XMLENC_NS + "tripledes-cbc";
    
    /** Block Encryption - REQUIRED AES-128. */
    public static final String ALGO_ID_BLOCKCIPHER_AES128 = XMLConstants.XMLENC_NS + "aes128-cbc";
    
    /** Block Encryption - REQUIRED AES-256. */
    public static final String ALGO_ID_BLOCKCIPHER_AES256 = XMLConstants.XMLENC_NS + "aes256-cbc";
    
    /** Block Encryption - OPTIONAL AES-192. */
    public static final String ALGO_ID_BLOCKCIPHER_AES192 = XMLConstants.XMLENC_NS + "aes192-cbc";

    // *************************************************
    // Key Transport
    // *************************************************
    /** Key Transport - REQUIRED RSA-v1.5. */
    public static final String ALGO_ID_KEYTRANSPORT_RSA15 = XMLConstants.XMLENC_NS + "rsa-1_5";
    
    /** Key Transport - REQUIRED RSA-OAEP.  */
    public static final String ALGO_ID_KEYTRANSPORT_RSAOAEP = XMLConstants.XMLENC_NS + "rsa-oaep-mgf1p";

    // *************************************************
    // Key Agreement
    // *************************************************
    /** Key Agreement - OPTIONAL Diffie-Hellman. */
    public static final String ALGO_ID_KEYAGREEMENT_DH = XMLConstants.XMLENC_NS + "dh";

    // *************************************************
    // Symmetric Key Wrap
    // *************************************************
    /** Symmetric Key Wrap - REQUIRED TRIPLEDES KeyWrap. */
    public static final String ALGO_ID_KEYWRAP_TRIPLEDES = XMLConstants.XMLENC_NS + "kw-tripledes";
    
    /** Symmetric Key Wrap - REQUIRED AES-128 KeyWrap. */
    public static final String ALGO_ID_KEYWRAP_AES128 = XMLConstants.XMLENC_NS + "kw-aes128";
    
    /** Symmetric Key Wrap - REQUIRED AES-256 KeyWrap. */
    public static final String ALGO_ID_KEYWRAP_AES256 = XMLConstants.XMLENC_NS + "kw-aes256";
    
    /** Symmetric Key Wrap - OPTIONAL AES-192 KeyWrap. */
    public static final String ALGO_ID_KEYWRAP_AES192 = XMLConstants.XMLENC_NS + "kw-aes192";

    // *************************************************
    // Message Digest
    // *************************************************
    /** Message Digest - RECOMMENDED SHA256. */
    public static final String ALGO_ID_DIGEST_SHA256 = XMLConstants.XMLENC_NS + "sha256";
    
    /** Message Digest - OPTIONAL SHA512. */
    public static final String ALGO_ID_DIGEST_SHA512 = XMLConstants.XMLENC_NS + "sha512";
    
    /** Message Digest - OPTIONAL RIPEMD-160. */
    public static final String ALGO_ID_DIGEST_RIPEMD160 = XMLConstants.XMLENC_NS + "ripemd160";

}
