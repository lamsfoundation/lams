/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat Middleware LLC, and individual contributors
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
package org.picketbox.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

import org.jboss.security.PicketBoxMessages;


/**
 * Utility to handle Java Keystore
 * 
 * @author Anil.Saldhana@redhat.com
 * @author Peter Skopek (pskopek_at_redhat_dot_com)
 * @since Jan 12, 2009
 */
public class KeyStoreUtil
{
   /**
    * Get the KeyStore
    * @param keyStoreFile
    * @param storePass
    * @return
    * @throws GeneralSecurityException
    * @throws IOException
    */
   public static KeyStore getKeyStore(File keyStoreFile, char[] storePass) throws GeneralSecurityException, IOException
   {
      return getKeyStore(KeyStore.getDefaultType(), keyStoreFile, storePass);
   }

   /**
    * Get the Keystore given the url to the keystore file as a string
    * @param fileURL
    * @param storePass 
    * @return
    * @throws GeneralSecurityException
    * @throws IOException
    */
   public static KeyStore getKeyStore(String fileURL, char[] storePass) throws GeneralSecurityException, IOException
   {
      return getKeyStore(KeyStore.getDefaultType(), fileURL, storePass);
   }

   /**
    * Get the Keystore given the URL to the keystore
    * @param url
    * @param storePass
    * @return
    * @throws GeneralSecurityException
    * @throws IOException
    */
   public static KeyStore getKeyStore(URL url, char[] storePass) throws GeneralSecurityException, IOException
   {
      return getKeyStore(KeyStore.getDefaultType(), url, storePass);
   }

   /**
    * Get the Key Store
    * <b>Note:</b> This method wants the InputStream to be not null. 
    * @param ksStream
    * @param storePass
    * @return
    * @throws GeneralSecurityException
    * @throws IOException
    * @throws IllegalArgumentException if ksStream is null
    */
   public static KeyStore getKeyStore(InputStream ksStream, char[] storePass) throws GeneralSecurityException,
         IOException
   {
      return getKeyStore(KeyStore.getDefaultType(), ksStream, storePass);
   }

   /**
    * Get the KeyStore
    * @param keyStoreType or null for default
    * @param keyStoreFile
    * @param storePass
    * @return
    * @throws GeneralSecurityException
    * @throws IOException
    */
   public static KeyStore getKeyStore(String keyStoreType, File keyStoreFile, char[] storePass) throws GeneralSecurityException, IOException
   {
      FileInputStream fis = null;
      try
      {
         fis = new FileInputStream(keyStoreFile);
         return getKeyStore(keyStoreType, fis, storePass);  
      }
      finally
      {
         safeClose(fis);
      }
   }

   /**
    * Get the Keystore given the url to the keystore file as a string
    * @param keyStoreType or null for default
    * @param fileURL
    * @param storePass 
    * @return
    * @throws GeneralSecurityException
    * @throws IOException
    */
   public static KeyStore getKeyStore(String keyStoreType, String fileURL, char[] storePass) throws GeneralSecurityException, IOException
   {
      if (fileURL == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("fileURL");

      File file = new File(fileURL);
      FileInputStream fis = null;
      try
      {
         fis = new FileInputStream(file);
         return getKeyStore(keyStoreType, fis, storePass);
      }
      finally
      {
         safeClose(fis);
      }
   }

   /**
    * Get the Keystore given the URL to the keystore
    * @param keyStoreType or null for default
    * @param url
    * @param storePass
    * @return
    * @throws GeneralSecurityException
    * @throws IOException
    */
   public static KeyStore getKeyStore(String keyStoreType, URL url, char[] storePass) throws GeneralSecurityException, IOException
   {
      if (url == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("url");

      InputStream is = null;
      try
      {
         is = url.openStream();
         return getKeyStore(keyStoreType, is, storePass);
      }
      finally
      {
         safeClose(is);
      }      
   }

   /**
    * Get the Key Store
    * <b>Note:</b> This method wants the InputStream to be not null. 
    * @param keyStoreType or null for default
    * @param ksStream
    * @param storePass
    * @return
    * @throws GeneralSecurityException
    * @throws IOException
    * @throws IllegalArgumentException if ksStream is null
    */
   public static KeyStore getKeyStore(String keyStoreType, InputStream ksStream, char[] storePass) throws GeneralSecurityException, IOException
   {
      if (ksStream == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("ksStream");
      KeyStore ks = KeyStore.getInstance((keyStoreType == null ? KeyStore.getDefaultType() : keyStoreType));
      ks.load(ksStream, storePass);
      return ks;
   }

   /**
    * Generate a Key Pair
    * @param algo (RSA, DSA etc)
    * @return
    * @throws GeneralSecurityException 
    */
   public static KeyPair generateKeyPair(String algo) throws GeneralSecurityException
   {
      KeyPairGenerator kpg = KeyPairGenerator.getInstance(algo);
      return kpg.genKeyPair();
   }

   /**
    * Get the Public Key from the keystore
    * @param ks
    * @param alias
    * @param password
    * @return 
    * @throws GeneralSecurityException  
    */
   public static PublicKey getPublicKey(KeyStore ks, String alias, char[] password) throws KeyStoreException,
         NoSuchAlgorithmException, GeneralSecurityException
   {
      PublicKey publicKey = null;

      // Get private key
      Key key = ks.getKey(alias, password);
      if (key instanceof PrivateKey)
      {
         // Get certificate of public key
         Certificate cert = ks.getCertificate(alias);

         // Get public key
         publicKey = cert.getPublicKey();
      }
      // if alias is a certificate alias, get the public key from the certificate.
      if (publicKey == null)
      {
         Certificate cert = ks.getCertificate(alias);
         if (cert != null)
            publicKey = cert.getPublicKey();
      }
      return publicKey;
   }

   /**
    * Add a certificate to the KeyStore
    * @param keystoreFile
    * @param storePass
    * @param alias
    * @param cert
    * @throws GeneralSecurityException
    * @throws IOException
    */
   public static void addCertificate(File keystoreFile, char[] storePass, String alias, Certificate cert)
         throws GeneralSecurityException, IOException
   {
      addCertificate(KeyStore.getDefaultType(), keystoreFile, storePass, alias, cert);
   }

   /**
    * Add a certificate to the KeyStore
    * @param keystoreFile
    * @param storePass
    * @param alias
    * @param cert
    * @throws GeneralSecurityException
    * @throws IOException
    */
   public static void addCertificate(String keyStoreType, File keystoreFile, char[] storePass, String alias, Certificate cert)
         throws GeneralSecurityException, IOException
   {
      KeyStore keystore = getKeyStore(keyStoreType, keystoreFile, storePass);

      // Add the certificate
      keystore.setCertificateEntry(alias, cert);

      // Save the new keystore contents
      FileOutputStream out = null;
      try
      {
         out = new FileOutputStream(keystoreFile);
         keystore.store(out, storePass);
         out.close();
      }
      finally
      {
         safeClose(out);
      }
   }

   /**
    * Get the key pair from the keystore
    * @param keystore
    * @param alias
    * @param password
    * @return
    * @throws Exception
    */
   public static KeyPair getPrivateKey(KeyStore keystore, String alias, char[] password) throws Exception
   { 
      // Get private key
      Key key = keystore.getKey(alias, password);
      if (key instanceof PrivateKey) 
      {
         // Get certificate of public key
         java.security.cert.Certificate cert = keystore.getCertificate(alias);

         // Get public key
         PublicKey publicKey = cert.getPublicKey();

         // Return a key pair
         return new KeyPair(publicKey, (PrivateKey)key);
      }
      return null;
   }

   /**
    * Create new empty keystore with specified keyStoreType and keyStorePWD
    * @param keyStoreType - key store type
    * @param keyStorePWD - key store password
    * @return
    * @throws Exception
    */
   public static KeyStore createKeyStore(String keyStoreType, char[] keyStorePWD) throws Exception {
      KeyStore ks = KeyStore.getInstance(keyStoreType);
      ks.load(null, keyStorePWD);
      return ks;
   }

   
   private static void safeClose(InputStream fis)
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

   private static void safeClose(OutputStream os)
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
  
}