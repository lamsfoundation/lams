/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.security.vault;

import java.util.Map;
import java.util.Set;

/**
 * Vault for secure storage of attributes
 * @author Anil.Saldhana@redhat.com
 * @since Aug 12, 2011
 */
public interface SecurityVault
{
   /**
    * Initialize the vault
    * @param options
    */
   void init(Map<String,Object> options) throws SecurityVaultException;
   
   /**
    * Determine if the vault is initialized
    * @return
    */
   boolean isInitialized();

   /**
    * Retrieve the shared key from the vault
    * @param handshakeOptions a set of options that the vault identifies for handshake
    * @return
    * @throws SecurityVaultException
    */
   byte[] handshake(Map<String,Object> handshakeOptions) throws SecurityVaultException;
   
   /**
    * Get the currently vaulted VaultBlock_attribute Names
    * @return
    * @throws SecurityVaultException
    */
   Set<String> keyList() throws SecurityVaultException;
   
   /**
    * Check whether an attribute value exists in the vault
    * @param vaultBlock
    * @param attributeName
    * @return
    * @throws SecurityVaultException
    * @since v4.0.3
    */
   boolean exists(String vaultBlock, String attributeName) throws SecurityVaultException;
   
   /**
    * Store an attribute value
    * @param vaultBlock a string value that brings in the uniqueness
    * @param attributeName name of the attribute
    * @param attributeValue
    * @param sharedKey
    * @throws SecurityVaultException
    */
   void store(String vaultBlock, String attributeName,char[] attributeValue, byte[] sharedKey) throws SecurityVaultException;

   /**
    * Retrieve the attribute value
    * @param vaultBlock
    * @param attributeName
    * @param sharedKey
    * @return
    * @throws SecurityVaultException
    */
   char[] retrieve(String vaultBlock, String attributeName, byte[] sharedKey) throws SecurityVaultException;
   
   /**
    * Remove an existing attribute value
    * @param vaultBlock
    * @param attributeName
    * @param sharedKey
    * @return true if remove was successful, false otherwise
    * @throws SecurityVaultException
    * @since v4.0.4.final
    */
   boolean remove(String vaultBlock, String attributeName, byte[] sharedKey) throws SecurityVaultException;
}