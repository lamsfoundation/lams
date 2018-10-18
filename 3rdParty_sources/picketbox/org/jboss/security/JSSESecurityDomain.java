/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
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
package org.jboss.security;

import java.lang.SecurityException;
import java.security.Key;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.util.Properties;

import javax.net.ssl.KeyManager;
import javax.net.ssl.TrustManager;

/**
 * Security domain used for configuring SSL.
 * 
 * @author <a href="mmoyses@redhat.com">Marcus Moyses</a>
 */
public interface JSSESecurityDomain extends BaseSecurityManager
{
   /** 
    * Get the keystore associated with the security domain
    * 
    * @return the keystore
    */
   public KeyStore getKeyStore() throws SecurityException;

   /**
    * Get the KeyManagers created by the configured KeyManagerFactory
    * 
    * @return the initialized KeyManagers
    */
   public KeyManager[] getKeyManagers() throws SecurityException;

   /**
    * Get the truststore associated with the security domain. This may be the same as the keystore
    * 
    * @return the truststore
    */
   public KeyStore getTrustStore() throws SecurityException;

   /**
    * Get the TrustManagers created by the configured TrustManagerFactory
    * 
    * @return the initialized TrustManagers
    */
   public TrustManager[] getTrustManagers() throws SecurityException;
   
   /**
    * Reload/initialize keystore and truststore using the attributes set in the security domain
    * 
    * @throws Exception if an error occurs
    */
   public void reloadKeyAndTrustStore() throws Exception;
   
   /**
    * Get the preferred server alias name
    * 
    * @return the preferred server alias, in case the underlying keystore contains multiple server
    *         aliases that can be used, and we wish to have more control over picking a specific
    *         one. Will return null if no preferred server alias is configured.
    */
   public String getServerAlias();

   /**
    * Get the preferred client alias name
    * 
    * @return the preferred client alias, in case the underlying keystore contains multiple client
    *         aliases that can be used, and we wish to have more control over picking a specific
    *         one. Will return null if no preferred client alias is configured.
    */
   public String getClientAlias();

   /**
    * Get the client auth flag
    * 
    * @return true to instruct callers into the implementations of this interface to require
    *         client authentication during the SSL handshake. If this flag is "true", the SSL
    *         handshake is supposed to fail if a client does not provide a valid certificate.
    */
   public boolean isClientAuth();

   /**
    * Returns the key with the given alias from the key store this security domain delegates to.
    * All keys except public keys require a service authentication token. In case of a public key
    * the authentication token will be ignored, and it can be safely null.
    *
    * @param alias - the alias corresponding to the key to be retrieved.
    * @param serviceAuthToken - the authentication token that establishes whether the calling
    *        service has the permission to retrieve the key. If no authentication token provided,
    *        or invalid authentication token is provided, the method will throw SecurityException
    *
    * @return the requested key, or null if the given alias does not exist or does not identify
    *         a key-related entry.
    *
    * @throws SecurityException for missing or invalid serviceAuthToken.
    *
    * @throws IllegalStateException if sensitive information is requested, but no service
    *         authorization token is configured on security domain.
    *
    * @see KeyStore#getKey(String, char[])
    */
   public Key getKey(String alias, String serviceAuthToken) throws Exception;
   
   /**
    * Returns the certificate with the given alias or null if no such certificate exists, from the
    * trust store this security domain delegates to.
    *
    * @param alias - the alias corresponding to the certificate to be retrieved.
    *
    * @return the requested certificate, or null if the given alias does not exist or does not
    *         identify a certificate-related entry.
    *
    * @see KeyStore#getKey(String, char[])
    */
   public Certificate getCertificate(String alias) throws Exception;
   
   /**
    * Returns the cipher suites that should be enabled on SSLSockets
    * 
    * @return array of cipher suite names
    */
   public String[] getCipherSuites();
   
   /**
    * Returns the protocols that should be enabled on SSLSockets
    * 
    * @return array of protocol names
    */
   public String[] getProtocols();
   
   /**
    * Returns the additional properties map
    * 
    * @return map with additional properties
    */
   public Properties getAdditionalProperties();
   
}
