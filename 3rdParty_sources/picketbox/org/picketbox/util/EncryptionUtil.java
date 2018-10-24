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
package org.picketbox.util;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Encryption/Decryption utility
 * @author Anil.Saldhana@redhat.com
 * @since Aug 12, 2011
 */
public class EncryptionUtil
{
   private String encryptionAlgorithm;
   private int keySize;

   public EncryptionUtil(String encryptionAlgorithm, int keySize)
   {
      this.encryptionAlgorithm = encryptionAlgorithm;
      this.keySize = keySize;
   }
   
   public SecretKey generateKey() throws NoSuchAlgorithmException
   {
      KeyGenerator kgen = KeyGenerator.getInstance(encryptionAlgorithm);
      kgen.init(keySize);
      SecretKey key = kgen.generateKey();
      return key;
   }
   
   public byte[] encrypt(byte[] data, PublicKey publicKey, SecretKey key) throws Exception
   {
     // Get the KeyGenerator
      KeyGenerator kgen = KeyGenerator.getInstance(this.encryptionAlgorithm);
      kgen.init(keySize);
 
      byte[] publicKeyEncoded = publicKey.getEncoded();

      SecretKeySpec skeySpec = new SecretKeySpec(key.getEncoded(), encryptionAlgorithm);


      // Instantiate the cipher 
      Cipher cipher = Cipher.getInstance(encryptionAlgorithm);

      cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

      byte[] encrypted =
        cipher.doFinal( data);
      return encrypted;
   }
   
   public byte[] decrypt(byte[] encryptedData, KeyPair keypair, SecretKeySpec keySpec ) throws Exception
   {
      // Get the KeyGenerator
      KeyGenerator kgen = KeyGenerator.getInstance(this.encryptionAlgorithm);
      kgen.init(keySize);
 
      byte[] publicKeyEncoded = keypair.getPrivate().getEncoded();
 

      // Instantiate the cipher 
      Cipher cipher = Cipher.getInstance(encryptionAlgorithm);
    
      cipher.init(Cipher.DECRYPT_MODE, keySpec);
      byte[] original = cipher.doFinal(encryptedData); 
      return original;
   }
   
   public byte[] decrypt(byte[] encryptedData, KeyPair keypair, SecretKey key ) throws Exception
   {
      // Get the KeyGenerator
      KeyGenerator kgen = KeyGenerator.getInstance(this.encryptionAlgorithm);
      kgen.init(keySize);
 
      byte[] publicKeyEncoded = keypair.getPrivate().getEncoded();

      SecretKeySpec skeySpec = new SecretKeySpec(key.getEncoded(), encryptionAlgorithm);

      // Instantiate the cipher 
      Cipher cipher = Cipher.getInstance(encryptionAlgorithm);
    
      cipher.init(Cipher.DECRYPT_MODE, skeySpec);
      byte[] original = cipher.doFinal(encryptedData); 
      return original;
   }
   
   public byte[] encrypt(byte[] data, SecretKey key) throws Exception
   {
      SecretKeySpec skeySpec = new SecretKeySpec(key.getEncoded(), encryptionAlgorithm);

      // Instantiate the cipher 
      Cipher cipher = Cipher.getInstance(encryptionAlgorithm);

      cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

      byte[] encrypted =
        cipher.doFinal( data);
      return encrypted;
   }

   public byte[] decrypt(byte[] encryptedData, SecretKeySpec keySpec ) throws Exception
   {

      // Instantiate the cipher 
      Cipher cipher = Cipher.getInstance(encryptionAlgorithm);

      cipher.init(Cipher.DECRYPT_MODE, keySpec);
      byte[] original = cipher.doFinal(encryptedData);
      return original;
   }

}