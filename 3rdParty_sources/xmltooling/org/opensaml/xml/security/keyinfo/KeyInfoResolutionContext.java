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

package org.opensaml.xml.security.keyinfo;

import java.security.Key;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.signature.KeyInfo;
import org.opensaml.xml.util.LazyMap;
import org.opensaml.xml.util.LazySet;


/**
 *  Resolution context class that is used to supply state information to, and to share information
 *  amongst, {@link KeyInfoProvider}s.
 *  
 *  <p>
 *  The extensible properties map available from {@link #getProperties()} may for example used to communicate
 *  state between two or more providers, or between a provider and custom logic in a particular implementation
 *  of {@link KeyInfoCredentialResolver}. It is recommended that providers and/or resolvers define
 *  and use property names in such a way as to avoid collisions with those used by other providers and resolvers,
 *  and to also clearly define the data type stored for each propery name.
 *  </p>
 *  
 */
public class KeyInfoResolutionContext {
    
    /** The KeyInfo being processed. */
    private KeyInfo keyInfo;
    
    /** Key names which are known to be associated with the KeyInfo being processed.
     * These may have for example been extracted from KeyName elements present,
     * or may have been inferred from the context in which the KeyInfo exists or
     * is being used. */
    private Set<String> keyNames;
    
    /** Get the key currently known to be represented by the KeyInfo. */
    private Key key;
    
    /** This list provides KeyInfo resolvers and providers in a particular processing
     * environment access to credentials that may have already been previously resolved. */
    private Collection<Credential> resolvedCredentials;
    
    /** Extensible map of properties used to share state amongst providers and/or resolver logic. */
    private final Map<String, Object> properties;
    
    /**
     * Constructor.
     * 
     * @param credentials a reference to the collection in which credentials previously
     *          resolved in a processing flow are being stored
     */
    public KeyInfoResolutionContext(Collection<Credential> credentials) {
        resolvedCredentials = Collections.unmodifiableCollection(credentials);
        properties = new LazyMap<String, Object>();
        keyNames = new LazySet<String>();
    }
    
    /**
     * Gets the KeyInfo being processed.
     * 
     * @return Returns the keyInfo.
     */
    public KeyInfo getKeyInfo() {
        return keyInfo;
    }
    
    /**
     * Sets the KeyInfo being processed.
     * 
     * @param newKeyInfo The keyInfo to set.
     */
    public void setKeyInfo(KeyInfo newKeyInfo) {
        keyInfo = newKeyInfo;
    }
    
    /**
     * The key names which are currently known.
     * 
     * These key names should be those which are known to be associated with the
     * key represented by the KeyInfo being processed. These may have for example
     * been directly extracted from KeyName elements present, or may have been
     * inferred from the context in which the KeyInfo exists or is being used. 
     * 
     * @return the set of key names
     * 
     * */
    public Set<String> getKeyNames() {
        return keyNames;
    }
    
    /**
     * Get the key currently known to be represented by the KeyInfo.
     * 
     * @return the key currently known to be represented by the KeyInfo
     *          or null if not currently known
     */
    public Key getKey() {
        return key;
    }
    
    /**
     * Set the key currently known to be represented by the KeyInfo.
     * 
     * @param newKey the new Key
     */
    public void setKey(Key newKey) {
        key = newKey;
    }
    
    /**
     * Get the set of credentials previously resolved.
     * 
     * @return Returns the keyValueCredential.
     */
    public Collection<Credential> getResolvedCredentials() {
        return resolvedCredentials;
    }
    
    /**
     * Get the extensible properties map.
     * 
     * @return Returns the properties.
     */
    public Map<String, Object> getProperties() {
        return properties;
    }
}
