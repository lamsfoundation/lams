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

import java.util.ArrayList;
import java.util.List;

import org.opensaml.xml.security.keyinfo.KeyInfoHelper;
import org.opensaml.xml.util.DatatypeHelper;

/**
 * Abstract class implementation for {@link EncryptedKeyResolver}.
 */
public abstract class AbstractEncryptedKeyResolver implements EncryptedKeyResolver {
    
    /** Recipient attribute criteria against which to match.*/
    private final List<String> recipients;
    
    /** Constructor. */
    public AbstractEncryptedKeyResolver() {
        recipients = new ArrayList<String>();
    }

    /** {@inheritDoc} */
    public List<String> getRecipients() {
        return recipients;
    }
    
    /**
     * Evaluate whether the specified recipient attribute value matches this resolver's
     * recipient criteria.
     * 
     * @param recipient the recipient value to evaluate
     * @return true if the recipient value matches the resolver's criteria, false otherwise
     */
    protected boolean matchRecipient(String recipient) {
        String trimmedRecipient = DatatypeHelper.safeTrimOrNullString(recipient);
        if (trimmedRecipient == null || recipients.isEmpty()) {
            return true;
        }
        
        return recipients.contains(trimmedRecipient);
    }
    
    /**
     * Evaluate whether an EncryptedKey's CarriedKeyName matches one of the KeyName values
     * from the EncryptedData context.
     * 
     * @param encryptedData the EncryptedData context
     * @param encryptedKey the candidate Encryptedkey to evaluate
     * @return true if the encrypted key's carried key name matches that of the encrytped data, 
     *          false otherwise
     */
    protected boolean matchCarriedKeyName(EncryptedData encryptedData, EncryptedKey encryptedKey) {
        if (encryptedKey.getCarriedKeyName() == null 
                || DatatypeHelper.isEmpty(encryptedKey.getCarriedKeyName().getValue()) ) {
            return true;
        }
        
        if (encryptedData.getKeyInfo() == null 
                || encryptedData.getKeyInfo().getKeyNames().isEmpty() ) {
            return false;
        }
        
        String keyCarriedKeyName = encryptedKey.getCarriedKeyName().getValue();
        List<String> dataKeyNames = KeyInfoHelper.getKeyNames(encryptedData.getKeyInfo());
        
        return dataKeyNames.contains(keyCarriedKeyName);
    }
    
    /**
     * Evaluate whether any of the EncryptedKey's DataReferences refer to the EncryptedData
     * context.
     * 
     * @param encryptedData the EncryptedData context
     * @param encryptedKey the candidate Encryptedkey to evaluate
     * @return true if any of the encrypted key's data references refer to the encrypted data context,
     *          false otherwise
     */
    protected boolean matchDataReference(EncryptedData encryptedData, EncryptedKey encryptedKey) {
        if (encryptedKey.getReferenceList() == null 
                || encryptedKey.getReferenceList().getDataReferences().isEmpty() ) {
            return true;
        }
        
        if (DatatypeHelper.isEmpty(encryptedData.getID())) {
            return false;
        }
        
        List<DataReference> drlist = encryptedKey.getReferenceList().getDataReferences();
        for (DataReference dr : drlist) {
            if (DatatypeHelper.isEmpty(dr.getURI()) || ! dr.getURI().startsWith("#") ) {
                continue;
            }
            if (dr.resolveIDFromRoot(dr.getURI().substring(1)) == encryptedData) {
                return true;
            }
        }
        return false;
    }
}
