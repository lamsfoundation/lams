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
package org.picketbox.plugins.vault;

import org.jboss.security.PicketBoxLogger;
import org.jboss.security.PicketBoxMessages;
import org.jboss.security.Util;
import org.jboss.security.plugins.PBEUtils;
import org.jboss.security.vault.SecurityVault;
import org.jboss.security.vault.SecurityVaultException;
import org.picketbox.plugins.vault.SecurityVaultData;
import org.picketbox.util.EncryptionUtil;
import org.picketbox.util.KeyStoreUtil;
import org.picketbox.util.StringUtil;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.security.*;
import java.security.KeyStore.Entry;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * An instance of {@link SecurityVault} that uses
 * a {@link KeyStore} 
 * The shared key just uses a concatenation of a {@link java.util.UUID}
 * and a keystore alias.
 * 
 * The following options are expected in the {@link SecurityVault#init(Map)} call:
 * ENC_FILE_DIR: the location where the encoded files will be kept. End with "/" or "\" based on your platform
 * KEYSTORE_URL: location where your keystore is located
 * KEYSTORE_PASSWORD: keystore password.
 * 'plain text' masked password (has to be prepended with MASK-)
 * '{EXT}...' where the '...' is the exact command
 * '{EXTC[:expiration_in_millis]}...' where the '...' is the exact command
 * line that will be passed to the Runtime.exec(String) method to execute a
 * platform command. The first line of the command output is used as the
 * password.
 * EXTC variant will cache the passwords for expiration_in_millis milliseconds.
 * Default cache expiration is 0 = infinity.
 * '{CMD}...' or '{CMDC}...' for a general command to execute. The general
 * command is a string delimited by ',' where the first part is the actual
 * command and further parts represents its parameters. The comma can be
 * backslashed in order to keep it as the part of a parameter.
 * '{CLASS[@modulename]}classname[:ctorargs]' where the '[:ctorargs]' is an optional
 * string delimited by the ':' from the classname that will be passed to the
 * classname ctor. The ctorargs itself is a comma delimited list of strings.
 * The password is obtained from classname by invoking a
 * 'char[] toCharArray()' method if found, otherwise, the 'String toString()'
 * KEYSTORE_ALIAS: Alias where the keypair is located
 * SALT: salt of the masked password. Ensured it is 8 characters in length
 * ITERATION_COUNT: Iteration Count of the masked password.
 * KEY_SIZE: Key size of encryption. Default is 128 bytes.
 * CREATE_KEYSTORE: Whether PicketBox Security Vault has to create missing key store in time of initialization. Default is "FALSE". Implies KEYSTORE_TYPE "JCEKS".  
 * KEYSTORE_TYPE: Key store type. Default is JCEKS. 
 * 
 * @author Anil.Saldhana@redhat.com
 * @author Peter Skopek (pskopek_at_redhat_dot_com)
 * @since Aug 12, 2011
 */
public class PicketBoxSecurityVault implements SecurityVault
{
   protected boolean finishedInit = false;

   protected KeyStore keystore = null;
   
   protected String encryptionAlgorithm = "AES";
   
   protected int keySize = 128;
   
   private char[] keyStorePWD = null;
   
   private String alias = null;
   
   private SecurityVaultData vaultContent = null;
   
   private SecretKey adminKey;

   private String decodedEncFileDir;
   
   private boolean createKeyStore = false;
   
   private String keyStoreType = defaultKeyStoreType;
   
   // options
   public static final String ENC_FILE_DIR = "ENC_FILE_DIR";
   
   public static final String KEYSTORE_URL = "KEYSTORE_URL";
   
   public static final String KEYSTORE_PASSWORD = "KEYSTORE_PASSWORD";
   
   public static final String KEYSTORE_ALIAS = "KEYSTORE_ALIAS";
   
   public static final String SALT = "SALT";
   
   public static final String ITERATION_COUNT = "ITERATION_COUNT";
   
   public static final String PASS_MASK_PREFIX = "MASK-";
   
   public static final String PUBLIC_CERT = "PUBLIC_CERT";
   
   public static final String KEY_SIZE = "KEY_SIZE"; 

   public static final String CREATE_KEYSTORE = "CREATE_KEYSTORE";
   
   public static final String KEYSTORE_TYPE = "KEYSTORE_TYPE";

   // backward compatibility constants 
   private static final String ENCODED_FILE = "ENC.dat";
   private static final String SHARED_KEY_FILE = "Shared.dat";
   private static final String ADMIN_KEY = "ADMIN_KEY";
   
   protected static final String VAULT_CONTENT_FILE = "VAULT.dat"; // versioned vault data file
   protected static final String defaultKeyStoreType = "JCEKS";
   
   
   /*
    * @see org.jboss.security.vault.SecurityVault#init(java.util.Map)
    */
   public void init(Map<String, Object> options) throws SecurityVaultException
   {
      if(options == null || options.isEmpty())
         throw PicketBoxMessages.MESSAGES.invalidNullOrEmptyOptionMap("options");

      String keystoreURL = (String) options.get(KEYSTORE_URL);
      if(keystoreURL == null)
         throw new SecurityVaultException(PicketBoxMessages.MESSAGES.invalidNullOrEmptyOptionMessage(KEYSTORE_URL));

      if (keystoreURL.contains("${")){
          keystoreURL = keystoreURL.replaceAll(":", StringUtil.PROPERTY_DEFAULT_SEPARATOR);  // replace single ":" with PL default
      }
      keystoreURL = StringUtil.getSystemPropertyAsString(keystoreURL);

      String password = (String) options.get(KEYSTORE_PASSWORD);
      if(password == null)
         throw new SecurityVaultException(PicketBoxMessages.MESSAGES.invalidNullOrEmptyOptionMessage(KEYSTORE_PASSWORD));
      if(password.startsWith(PASS_MASK_PREFIX) == false
            && Util.isPasswordCommand(password) == false)
         throw new SecurityVaultException(PicketBoxMessages.MESSAGES.invalidKeystorePasswordFormatMessage());

      String salt = (String) options.get(SALT);
      if(salt == null)
         throw new SecurityVaultException(PicketBoxMessages.MESSAGES.invalidNullOrEmptyOptionMessage(SALT));

      String iterationCountStr = (String) options.get(ITERATION_COUNT);
      if(iterationCountStr == null)
         throw new SecurityVaultException(PicketBoxMessages.MESSAGES.invalidNullOrEmptyOptionMessage(ITERATION_COUNT));
      int iterationCount = Integer.parseInt(iterationCountStr);
      
      this.alias = (String) options.get(KEYSTORE_ALIAS);
      if(alias == null)
         throw new SecurityVaultException(PicketBoxMessages.MESSAGES.invalidNullOrEmptyOptionMessage(KEYSTORE_ALIAS));
      
      String keySizeStr = (String) options.get(KEY_SIZE);
      if(keySizeStr != null)
      {
         keySize = Integer.parseInt(keySizeStr);
      }
      
      String encFileDir = (String) options.get(ENC_FILE_DIR);
      if(encFileDir == null)
         throw new SecurityVaultException(PicketBoxMessages.MESSAGES.invalidNullOrEmptyOptionMessage(ENC_FILE_DIR));

      
      createKeyStore = (options.get(CREATE_KEYSTORE) != null ? Boolean.parseBoolean((String) options.get(CREATE_KEYSTORE))
            : false);
      keyStoreType = (options.get(KEYSTORE_TYPE) != null ? (String) options.get(KEYSTORE_TYPE) : defaultKeyStoreType);

      try {
         keyStorePWD = loadKeystorePassword(password, salt, iterationCount);
         keystore = getKeyStore(keystoreURL);
         
         checkAndConvertKeyStoreToJCEKS(keystoreURL);
         
      } catch (Exception e) {
         throw new SecurityVaultException(e);
      }

      // read and possibly convert vault content
      readVaultContent(keystoreURL, encFileDir);

      PicketBoxLogger.LOGGER.infoVaultInitialized();
      finishedInit = true;     

      
   }

   /*
    * @see org.jboss.security.vault.SecurityVault#isInitialized()
    */
   public boolean isInitialized()
   {
      return finishedInit;
   }

   /*
    * @see org.jboss.security.vault.SecurityVault#handshake(java.util.Map)
    */
   public byte[] handshake(Map<String, Object> handshakeOptions) throws SecurityVaultException {
       return new byte[keySize];
   }
   
   /*
    * @see org.jboss.security.vault.SecurityVault#keyList()
    */
    public Set<String> keyList() throws SecurityVaultException {
        return vaultContent.getVaultDataKeys();
    }

   /*
    * @see org.jboss.security.vault.SecurityVault#store(java.lang.String, java.lang.String, char[], byte[])
    */
   public void store(String vaultBlock, String attributeName, char[] attributeValue, byte[] sharedKey)
         throws SecurityVaultException
   {
      if(StringUtil.isNullOrEmpty(vaultBlock))
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("vaultBlock");
      if(StringUtil.isNullOrEmpty(attributeName))
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("attributeName");

      String av = new String(attributeValue);
      
      EncryptionUtil util = new EncryptionUtil(encryptionAlgorithm, keySize);
      try
      {
         SecretKeySpec sKeySpec = new SecretKeySpec(adminKey.getEncoded(), encryptionAlgorithm);
         byte[] encryptedData = util.encrypt(av.getBytes(), sKeySpec);
         vaultContent.addVaultData(alias, vaultBlock, attributeName, encryptedData);
      }
      catch (Exception e1)
      { 
         throw new SecurityVaultException(PicketBoxMessages.MESSAGES.unableToEncryptDataMessage(),e1);
      }
      
      try {
         writeVaultData();
      }
      catch (IOException e) { 
         throw new SecurityVaultException(PicketBoxMessages.MESSAGES.unableToWriteVaultDataFileMessage(VAULT_CONTENT_FILE), e);
      }
   }

   /*
    * @see org.jboss.security.vault.SecurityVault#retrieve(java.lang.String, java.lang.String, byte[])
    */
   public char[] retrieve(String vaultBlock, String attributeName, byte[] sharedKey) throws SecurityVaultException
   {
      if(StringUtil.isNullOrEmpty(vaultBlock))
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("vaultBlock");
      if(StringUtil.isNullOrEmpty(attributeName))
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("attributeName");

      byte[] encryptedValue = vaultContent.getVaultData(alias, vaultBlock, attributeName);
       
      SecretKeySpec secretKeySpec = new SecretKeySpec(adminKey.getEncoded(), encryptionAlgorithm);
      EncryptionUtil encUtil = new EncryptionUtil(encryptionAlgorithm, keySize);
      try
      {
         return (new String(encUtil.decrypt(encryptedValue, secretKeySpec))).toCharArray();
      }
      catch (Exception e)
      { 
         throw new SecurityVaultException(e);
      } 
   }

   /**
    * @see org.jboss.security.vault.SecurityVault#exists(String, String)
    */
   public boolean exists(String vaultBlock, String attributeName) throws SecurityVaultException { 
      return vaultContent.getVaultData(alias, vaultBlock, attributeName) != null;
   }
   
   /*
    * @see org.jboss.security.vault.SecurityVault#remove(java.lang.String, java.lang.String, byte[])
    */
   public boolean remove(String vaultBlock, String attributeName, byte[] sharedKey)
		   throws SecurityVaultException 
   {
      if(StringUtil.isNullOrEmpty(vaultBlock))
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("vaultBlock");
      if(StringUtil.isNullOrEmpty(attributeName))
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("attributeName");
      
      try {
         if (vaultContent.deleteVaultData(alias, vaultBlock, attributeName)) {
            writeVaultData();
            return true;
         }
         return false;
      }
      catch (IOException e) { 
         throw new SecurityVaultException(PicketBoxMessages.MESSAGES.unableToWriteVaultDataFileMessage(VAULT_CONTENT_FILE), e);
      }
	   catch(Exception e) {
		   throw new SecurityVaultException(e);
	   }
	}

   private char[] loadKeystorePassword(String passwordDef, String salt, int iterationCount) throws Exception
   {
      final char[] password;

      if( passwordDef.startsWith(PASS_MASK_PREFIX) ){
         String keystorePass = decode(passwordDef, salt, iterationCount);
         password = keystorePass.toCharArray();
      }
      else
         password = Util.loadPassword(passwordDef);

      return password;
   }

   private String decode(String maskedString, String salt, int iterationCount) throws Exception
   {
      String pbeAlgo = "PBEwithMD5andDES";
      if (maskedString.startsWith(PASS_MASK_PREFIX))
      {
         // Create the PBE secret key 
         SecretKeyFactory factory = SecretKeyFactory.getInstance(pbeAlgo);

         char[] password = "somearbitrarycrazystringthatdoesnotmatter".toCharArray();
         PBEParameterSpec cipherSpec = new PBEParameterSpec(salt.getBytes(), iterationCount);
         PBEKeySpec keySpec = new PBEKeySpec(password);
         SecretKey cipherKey = factory.generateSecret(keySpec);

         maskedString = maskedString.substring(PASS_MASK_PREFIX.length());
         String decodedValue = PBEUtils.decode64(maskedString, pbeAlgo, cipherKey, cipherSpec);
         maskedString = decodedValue;
      }
      return maskedString;
   }
   
   private void setUpVault(String keystoreURL, String decodedEncFileDir) throws NoSuchAlgorithmException, IOException
   { 
      vaultContent = new SecurityVaultData();
      writeVaultData();
      
      SecretKey sk = getAdminKey();
      if (sk != null) {
          adminKey = sk; 
      }
      else {
          if (!createKeyStore) {
              throw PicketBoxMessages.MESSAGES.vaultDoesnotContainSecretKey(alias);
          }
          // try to generate new admin key and store it under specified alias
          EncryptionUtil util = new EncryptionUtil(encryptionAlgorithm, keySize);
          sk = util.generateKey();
          KeyStore.SecretKeyEntry skEntry = new KeyStore.SecretKeyEntry(sk);
          try {
              keystore.setEntry(alias, skEntry, new KeyStore.PasswordProtection(keyStorePWD));
              adminKey = sk;
              saveKeyStoreToFile(keystoreURL);
          }
          catch (KeyStoreException e) {
             throw PicketBoxMessages.MESSAGES.noSecretKeyandAliasAlreadyUsed(alias);
          }
          catch (Exception e) {
             throw PicketBoxMessages.MESSAGES.unableToStoreKeyStoreToFile(e, keystoreURL); 
          }
      }
   }
   
   private void writeVaultData() throws IOException
   {
	  FileOutputStream fos = null;
	  ObjectOutputStream oos = null;
	  try
	  {
	      fos = new FileOutputStream(decodedEncFileDir + VAULT_CONTENT_FILE);
	      oos = new ObjectOutputStream(fos);
	      oos.writeObject(vaultContent);
	  }
	  finally
	  {
		  safeClose(oos);
		  safeClose(fos);
	  }
   }
   
   private boolean vaultFileExists(String fileName)
   {
      File file = new File(this.decodedEncFileDir + fileName);
      return file != null && file.exists();
   }
   
   private boolean directoryExists(String dir)
   {
      File file = new File(dir);
      return file != null && file.exists();
   }
   
   private void safeClose(InputStream fis)
   {
      try
      {
         if(fis != null)
         {
            fis.close();
         }
      }
      catch(Exception e)
      {}
   }

   private void safeClose(OutputStream os)
   {
      try
      {
         if(os != null)
         {
            os.close();
         }
      }
      catch(Exception e)
      {}
   }

    private void readVaultContent(String keystoreURL, String encFileDir) throws SecurityVaultException {

        try {
            if (encFileDir.contains("${)")) {
                encFileDir = encFileDir.replaceAll(":", StringUtil.PROPERTY_DEFAULT_SEPARATOR);
            }
            decodedEncFileDir = StringUtil.getSystemPropertyAsString(encFileDir); // replace single ":" with PL default

            if (directoryExists(decodedEncFileDir) == false)
                throw new SecurityVaultException(
                        PicketBoxMessages.MESSAGES.fileOrDirectoryDoesNotExistMessage(decodedEncFileDir));

            if (!(decodedEncFileDir.endsWith("/") || decodedEncFileDir.endsWith("\\"))) {
                decodedEncFileDir = decodedEncFileDir + File.separator;
            }

            if (vaultFileExists(ENCODED_FILE)) {
                if (vaultFileExists(VAULT_CONTENT_FILE)) {
                    PicketBoxLogger.LOGGER.mixedVaultDataFound(VAULT_CONTENT_FILE, ENCODED_FILE, decodedEncFileDir
                            + ENCODED_FILE);
                    throw PicketBoxMessages.MESSAGES.mixedVaultDataFound(VAULT_CONTENT_FILE, ENCODED_FILE);
                } else {
                    convertVaultContent(keystoreURL, alias);
                }
            } else {
                if (vaultFileExists(VAULT_CONTENT_FILE)) {
                    readVersionedVaultContent();
                } else {
                    setUpVault(keystoreURL, decodedEncFileDir);
                }
            }

        } catch (Exception e) {
            throw new SecurityVaultException(e);
        }

    }

   @SuppressWarnings("unchecked")
   private void convertVaultContent(String keystoreURL, String alias) throws Exception {
       FileInputStream fis = null;
       ObjectInputStream ois = null;
       Map<String, byte[]> theContent;
       
       try {
           fis = new FileInputStream(decodedEncFileDir + ENCODED_FILE);
           ois = new ObjectInputStream(fis);
           theContent = (Map<String, byte[]>) ois.readObject();
       } finally {
           safeClose(fis);
           safeClose(ois);
       }
        
       // create new SecurityVaultData object for transformed vault data
       vaultContent = new SecurityVaultData();
       
       adminKey = null;
       for (String key: theContent.keySet()) {
           if (key.equals(ADMIN_KEY)) {
               byte[] admin_key = theContent.get(key);
               adminKey = new SecretKeySpec(admin_key, encryptionAlgorithm);
           }
           else {
               if (key.contains("_")) {
                   StringTokenizer tokenizer = new StringTokenizer(key, "_");
                   String vaultBlock = tokenizer.nextToken();
                   String attributeName = tokenizer.nextToken();
                   if (tokenizer.hasMoreTokens()) {
                       attributeName = key.substring(vaultBlock.length() + 1);
                       PicketBoxLogger.LOGGER.ambiguosKeyForSecurityVaultTransformation("_", vaultBlock, attributeName);
                   }
                   byte[] encodedAttributeValue = theContent.get(key);
                   vaultContent.addVaultData(alias, vaultBlock, attributeName, encodedAttributeValue);
               }
           }
       }
       if (adminKey == null) {
           throw PicketBoxMessages.MESSAGES.missingAdminKeyInOriginalVaultData();
       }
       
       // add secret key (admin_key) to keystore 
       KeyStore.SecretKeyEntry skEntry = new KeyStore.SecretKeyEntry(adminKey);
       KeyStore.PasswordProtection p = new KeyStore.PasswordProtection(keyStorePWD);
       Entry e = keystore.getEntry(alias, p);
       if (e != null) {
          // rename the old entry
          String originalAlias = alias + "-original";
          keystore.setEntry(originalAlias, e, p);
          keystore.deleteEntry(alias);
       }
       keystore.setEntry(alias, skEntry, new KeyStore.PasswordProtection(keyStorePWD));

       // save the current keystore
       saveKeyStoreToFile(keystoreURL);
    
       // backup original vault file (shared key file cannot be saved for obvious reasons
       copyFile(new File(decodedEncFileDir + ENCODED_FILE), new File(decodedEncFileDir + ENCODED_FILE + ".original"));

       // save vault data file
       writeVaultData();
       
       // delete original vault files
       File f = new File(decodedEncFileDir + ENCODED_FILE);
       if (!f.delete()) {
           PicketBoxLogger.LOGGER.cannotDeleteOriginalVaultFile(f.getCanonicalPath());
       }
       f = new File(decodedEncFileDir + SHARED_KEY_FILE);
       if (!f.delete()) {
           PicketBoxLogger.LOGGER.cannotDeleteOriginalVaultFile(f.getCanonicalPath());
       }
       
   }

   private void saveKeyStoreToFile(String keystoreURL) throws Exception {
       keystore.store(new FileOutputStream(new File(keystoreURL)), keyStorePWD);
   }
   
   private void checkAndConvertKeyStoreToJCEKS(String keystoreURL) throws Exception {
      if (keystore.getType().equalsIgnoreCase("JKS")) {

         // backup original keystore file
         copyFile(new File(keystoreURL), new File(keystoreURL + ".original"));

         KeyStore jceks = KeyStoreUtil.createKeyStore("JCEKS", keyStorePWD);
         
         Enumeration<String> aliases = keystore.aliases();
         while (aliases.hasMoreElements()) {
            String entryAlias = aliases.nextElement();
            KeyStore.PasswordProtection p = new KeyStore.PasswordProtection(keyStorePWD);
            KeyStore.Entry e = keystore.getEntry(entryAlias, p);
            jceks.setEntry(entryAlias, e, p);
         }
         keystore = jceks;
         keyStoreType = "JCEKS"; // after conversion we have to change keyStoreType to the one we really have
         saveKeyStoreToFile(keystoreURL);
         PicketBoxLogger.LOGGER.keyStoreConvertedToJCEKS(KEYSTORE_URL);
      }
   }
   

   
    private void readVersionedVaultContent() throws Exception {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(decodedEncFileDir + VAULT_CONTENT_FILE);
            ois = new ObjectInputStream(fis);
            vaultContent = (SecurityVaultData) ois.readObject();
        } finally {
            safeClose(fis);
            safeClose(ois);
        }
        
        adminKey = getAdminKey();
        if (adminKey == null) {
            throw PicketBoxMessages.MESSAGES.vaultDoesnotContainSecretKey(alias);
        }    
    }
   
    /**
     * Returns SecretKey stored in defined keystore under defined alias.
     * If no such SecretKey exists returns null.
     * @return
     */
    private SecretKey getAdminKey() {
        try {
            Entry e = keystore.getEntry(alias, new KeyStore.PasswordProtection(keyStorePWD));
            if (e instanceof KeyStore.SecretKeyEntry) {
                return ((KeyStore.SecretKeyEntry)e).getSecretKey();
            }
        }
        catch (Exception e) {
            PicketBoxLogger.LOGGER.vaultDoesnotContainSecretKey(alias);
            return null;
        }
        return null;
    }
    
   /**
    * Copy file method.
    * 
    * @param sourceFile
    * @param destFile
    * @throws IOException
    */
    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }
        FileInputStream fIn = null;
        FileOutputStream fOut = null;
        FileChannel source = null;
        FileChannel destination = null;
        try {
            fIn = new FileInputStream(sourceFile);
            source = fIn.getChannel();
            fOut = new FileOutputStream(destFile);
            destination = fOut.getChannel();
            long transfered = 0;
            long bytes = source.size();
            while (transfered < bytes) {
                transfered += destination.transferFrom(source, 0, source.size());
                destination.position(transfered);
            }
        } finally {
            if (source != null) {
                source.close();
            } else if (fIn != null) {
                fIn.close();
            }
            if (destination != null) {
                destination.close();
            } else if (fOut != null) {
                fOut.close();
            }
        }
    }
    
    /**
     * Get key store based on options passed to PicketBoxSecurityVault.
     * @return
     */
    private KeyStore getKeyStore(String keystoreURL) {

        try {
            if (createKeyStore) {
                return KeyStoreUtil.createKeyStore(keyStoreType, keyStorePWD);
            }
        }
        catch (Throwable e) {
            throw PicketBoxMessages.MESSAGES.unableToGetKeyStore(e, keystoreURL);
        }

        try {
            return KeyStoreUtil.getKeyStore(keyStoreType, keystoreURL, keyStorePWD);
        }
        catch (IOException e) {
            throw PicketBoxMessages.MESSAGES.unableToGetKeyStore(e, keystoreURL);
        }
        catch (GeneralSecurityException e) {
            throw PicketBoxMessages.MESSAGES.unableToGetKeyStore(e, keystoreURL);
        }
    }

}
