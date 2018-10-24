/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.picketbox.plugins.vault;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.security.PicketBoxLogger;
import org.jboss.security.PicketBoxMessages;
import org.picketbox.util.StringUtil;

/**
 * Security vault data store with version serialized data storage.
 *  
 * @author Peter Skopek (pskopek_at_redhat_dot_com)
 *
 */
public class SecurityVaultData implements Serializable {

    /**
     *  Do not change this suid, it is used for handling different versions of serialized data.
     */
    private static final long serialVersionUID = 1L;

    /**
     *  Version to denote actual version of SecurityVaultData object.
     */
    private static final int VERSION = 1;
    
    private transient Map<String, byte[]> vaultData = new ConcurrentHashMap<String,byte[]>();
    

    /**
     * Default constructor.
     */
    public SecurityVaultData() {
    }

    /**
     * Writes object to the ObjectOutputSteream.
     * 
     * @param oos
     * @throws IOException
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.writeObject(new Integer(VERSION));
        oos.writeObject(vaultData);
    }
    
    /**
     * Reads object from the ObjectInputStream. This method needs to be changed when implementing 
     * changes in data and {@link VERSION} is changed.
     *  
     * @param ois
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        int version = (Integer) ois.readObject();
        
        if (PicketBoxLogger.LOGGER.isDebugEnabled()) {
            PicketBoxLogger.LOGGER.securityVaultContentVersion(String.valueOf(version), String.valueOf(VERSION));
        }
        
        if (version == 1) {
            this.vaultData = (Map<String, byte[]>)ois.readObject();
        }
        else {
            throw PicketBoxMessages.MESSAGES.unrecognizedVaultContentVersion(String.valueOf(version), "1", String.valueOf(VERSION));
        }
    }

    /**
     * Retrieves the data stored in vault storage.
     * 
     * @param keyAlias - currently not used (for possible future extension)
     * @param vaultBlock
     * @param attributeName
     * @return
     */
    byte[] getVaultData(String keyAlias, String vaultBlock, String attributeName) {
       return vaultData.get(dataKey(keyAlias, vaultBlock, attributeName));
    }

    /**
     * 
     * @param keyAlias
     * @param vaultBlock
     * @param attributeName
     * @param encryptedData
     */
    void addVaultData(String keyAlias, String vaultBlock, String attributeName, byte[] encryptedData) {
       vaultData.put(dataKey(keyAlias, vaultBlock, attributeName), encryptedData);
    }
    
    /**
     * Removes data stored in vault storage.
     * @param keyAlias
     * @param vaultBlock
     * @param attributeName
     * @return true when vault data has been removed successfully, otherwise false
     */
    boolean deleteVaultData(String keyAlias, String vaultBlock, String attributeName) {
       if (vaultData.remove(dataKey(keyAlias, vaultBlock, attributeName)) == null) {
          return false;
       }
       return true;
    }

    /**
     * Returns mapping keys for all stored data.
     * @return
     */
    Set<String> getVaultDataKeys() {
       return vaultData.keySet();
    }
    
    /**
     * Creates new format for data key in vault. All parameters has to be non-null.
     * 
     * @param keyAlias - currently not used (for possible future extension) 
     * @param vaultBlock
     * @param attributeName
     * @param alias
     * @return
     */
    private static String dataKey(String keyAlias, String vaultBlock, String attributeName) {
       return vaultBlock + StringUtil.PROPERTY_DEFAULT_SEPARATOR + attributeName; 
    }
    
}
