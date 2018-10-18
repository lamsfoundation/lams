/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
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
package org.jboss.security.auth.spi;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.Principal;
import java.security.acl.Group;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;

import org.jboss.security.JSSESecurityDomain;
import org.jboss.security.PicketBoxLogger;
import org.jboss.security.PicketBoxMessages;
import org.jboss.security.SecurityConstants;
import org.jboss.security.SecurityDomain;
import org.jboss.security.SecurityUtil;
import org.jboss.security.auth.callback.ObjectCallback;
import org.jboss.security.auth.certs.X509CertificateVerifier;

/**
 * Base Login Module that uses X509Certificates as credentials for
 * authentication.
 *
 * This login module uses X509Certificates as a
 * credential. It takes the cert as an object and checks to see if the alias in
 * the truststore/keystore contains the same certificate. Subclasses of this
 * module should implement the getRoleSets() method defined by
 * AbstractServerLoginModule. Much of this module was patterned after the
 * UserNamePasswordLoginModule.
 *
 * @author <a href="mailto:jasone@greenrivercomputing.com">Jason Essington</a>
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class BaseCertLoginModule extends AbstractServerLoginModule
{
   // see AbstractServerLoginModule
   private static final String SECURITY_DOMAIN = "securityDomain";
   private static final String VERIFIER = "verifier";
   
   private static final String[] ALL_VALID_OPTIONS =
   {
	   SECURITY_DOMAIN,VERIFIER
   };
   
   /** A principal derived from the certificate alias */
   private Principal identity;
   /** The client certificate */
   private X509Certificate credential;
   /** The SecurityDomain to obtain the KeyStore/TrustStore from */
   private Object domain = null;
   /** An option certificate verifier */
   private X509CertificateVerifier verifier; 

   /** Override the super version to pickup the following options after first
    * calling the super method.
    *
    * option: securityDomain - the name of the SecurityDomain to obtain the
    *    trust and keystore from.
    * option: verifier - the class name of the X509CertificateVerifier to use
    *    for verification of the login certificate
    *
    * @see SecurityDomain
    * @see X509CertificateVerifier
    *
    * @param subject the Subject to update after a successful login.
    * @param callbackHandler the CallbackHandler that will be used to obtain the
    *    the user identity and credentials.
    * @param sharedState a Map shared between all configured login module instances
    * @param options the parameters passed to the login module.
    */
   public void initialize(Subject subject, CallbackHandler callbackHandler,
      Map<String,?> sharedState, Map<String,?> options)
   {
      addValidOptions(ALL_VALID_OPTIONS);
      super.initialize(subject, callbackHandler, sharedState, options);

      // Get the security domain and default to "other"
      String sd = (String) options.get(SECURITY_DOMAIN);
      sd = SecurityUtil.unprefixSecurityDomain(sd);
      if (sd == null)
         sd = "other";

      try
      {
         Object tempDomain = new InitialContext().lookup(SecurityConstants.JAAS_CONTEXT_ROOT + sd);
         if (tempDomain instanceof SecurityDomain)
         {
            domain = tempDomain;
            if (PicketBoxLogger.LOGGER.isTraceEnabled())
            {
               PicketBoxLogger.LOGGER.traceSecurityDomainFound(domain.getClass().getName());
            }
         }
         else {
            tempDomain = new InitialContext().lookup(SecurityConstants.JAAS_CONTEXT_ROOT + sd + "/jsse");
            if (tempDomain instanceof JSSESecurityDomain) {
               domain = tempDomain;
               if (PicketBoxLogger.LOGGER.isTraceEnabled())
               {
                  PicketBoxLogger.LOGGER.traceSecurityDomainFound(domain.getClass().getName());
               }
            }
            else
            {
               PicketBoxLogger.LOGGER.errorGettingJSSESecurityDomain(sd);
            }
         }
      }
      catch (NamingException e)
      {
         PicketBoxLogger.LOGGER.errorFindingSecurityDomain(sd, e);
      }

      String option = (String) options.get(VERIFIER);
      if( option != null )
      {
         try
         {
            ClassLoader loader = SecurityActions.getContextClassLoader();
            Class<?> verifierClass = loader.loadClass(option);
            verifier = (X509CertificateVerifier) verifierClass.newInstance();
         }
         catch(Throwable e)
         {
            PicketBoxLogger.LOGGER.errorCreatingCertificateVerifier(e);
         }
      }
      PicketBoxLogger.LOGGER.traceEndInitialize();
   }

   /**
    * Perform the authentication of the username and password.
    */
   @SuppressWarnings("unchecked")
   public boolean login() throws LoginException
   {
      PicketBoxLogger.LOGGER.traceBeginLogin();

      // See if shared credentials exist
      if (super.login() == true)
      {
         // Setup our view of the user
         Object username = sharedState.get("javax.security.auth.login.name");
         if( username instanceof Principal )
            identity = (Principal) username;
         else
         {
            String name = username.toString();
            try
            {
               identity = createIdentity(name);
            }
            catch(Exception e)
            {
               throw PicketBoxMessages.MESSAGES.failedToCreatePrincipal(e.getLocalizedMessage());
            }
         }

         Object password = sharedState.get("javax.security.auth.login.password");
         if (password instanceof X509Certificate)
            credential = (X509Certificate) password;
         else if (password != null)
         {
            PicketBoxLogger.LOGGER.debugPasswordNotACertificate();
            super.loginOk = false;
            return false;
         }
         return true;
      }

      super.loginOk = false;
      Object[] info = getAliasAndCert();
      String alias = (String) info[0];
      credential = (X509Certificate) info[1];

      if (alias == null && credential == null)
      {
         identity = unauthenticatedIdentity;
         if (PicketBoxLogger.LOGGER.isTraceEnabled())
         {
            PicketBoxLogger.LOGGER.traceUsingUnauthIdentity(identity.toString());
         }
      }

      if (identity == null)
      {
         try
         {
            identity = createIdentity(alias);
         }
         catch(Exception e)
         {
            PicketBoxLogger.LOGGER.debugFailureToCreateIdentityForAlias(alias, e);
         }

         if (!validateCredential(alias, credential))
         {
            throw PicketBoxMessages.MESSAGES.failedToMatchCredential(alias);
         }
      }

      if (getUseFirstPass() == true)
      {
         // Add authentication info to shared state map
         sharedState.put("javax.security.auth.login.name", alias);
         sharedState.put("javax.security.auth.login.password", credential);
      }
      super.loginOk = true;

      PicketBoxLogger.LOGGER.traceEndLogin(super.loginOk);
      return true;
   }

   /** Override to add the X509Certificate to the public credentials
    * @return
    * @throws LoginException
    */
   public boolean commit() throws LoginException
   {
      boolean ok = super.commit();
      if( ok == true )
      {
         // Add the cert to the public credentials
         if (credential != null)
         {
            subject.getPublicCredentials().add(credential);
         }
      }
      return ok;
   }

   /** Subclasses need to override this to provide the roles for authorization
    * @return
    * @throws LoginException
    */
   protected Group[] getRoleSets() throws LoginException
   {
      return new Group[0];
   }

   protected Principal getIdentity()
   {
      return identity;
   }
   protected Object getCredentials()
   {
      return credential;
   }
   protected String getUsername()
   {
      String username = null;
      if (getIdentity() != null)
         username = getIdentity().getName();
      return username;
   }

   protected Object[] getAliasAndCert() throws LoginException
   {
      PicketBoxLogger.LOGGER.traceBeginGetAliasAndCert();
      Object[] info = { null, null };
      // prompt for a username and password
      if (callbackHandler == null)
      {
         throw PicketBoxMessages.MESSAGES.noCallbackHandlerAvailable();
      }
      NameCallback nc = new NameCallback("Alias: ");
      ObjectCallback oc = new ObjectCallback("Certificate: ");
      Callback[] callbacks = { nc, oc };
      String alias = null;
      X509Certificate cert = null;
      X509Certificate[] certChain;
      try
      {
         callbackHandler.handle(callbacks);
         alias = nc.getName();
         Object tmpCert = oc.getCredential();
         if (tmpCert != null)
         {
            if (tmpCert instanceof X509Certificate)
            {
               cert = (X509Certificate) tmpCert;
               if (PicketBoxLogger.LOGGER.isTraceEnabled())
               {
                  PicketBoxLogger.LOGGER.traceCertificateFound(cert.getSerialNumber().toString(16), cert.getSubjectDN().getName());
               }
            }
            else if( tmpCert instanceof X509Certificate[] )
            {
               certChain = (X509Certificate[]) tmpCert;
               if( certChain.length > 0 )
                  cert = certChain[0];
            }
            else
            {
               throw PicketBoxMessages.MESSAGES.unableToGetCertificateFromClass(tmpCert != null ? tmpCert.getClass() : null);
            }
         }
         else
         {
            PicketBoxLogger.LOGGER.warnNullCredentialFromCallbackHandler();
         }
      }
      catch (IOException e)
      {
         LoginException le = PicketBoxMessages.MESSAGES.failedToInvokeCallbackHandler();
         le.initCause(e);
         throw le;
      }
      catch (UnsupportedCallbackException uce)
      {
         LoginException le = new LoginException();
         le.initCause(uce);
         throw le;
      }

      info[0] = alias;
      info[1] = cert;
      PicketBoxLogger.LOGGER.traceEndGetAliasAndCert();
      return info;
   }

   protected boolean validateCredential(String alias, X509Certificate cert)
   {
      PicketBoxLogger.LOGGER.traceBeginValidateCredential();
      boolean isValid = false;

      // if we don't have a trust store, we'll just use the key store.
      KeyStore keyStore = null;
      KeyStore trustStore = null;
      if( domain != null )
      {
         if (domain instanceof SecurityDomain)
         {
            keyStore = ((SecurityDomain) domain).getKeyStore();
            trustStore = ((SecurityDomain) domain).getTrustStore();
         }
         else
            if (domain instanceof JSSESecurityDomain)
            {
               keyStore = ((JSSESecurityDomain) domain).getKeyStore();
               trustStore = ((JSSESecurityDomain) domain).getTrustStore();
            }
      }
      if( trustStore == null )
         trustStore = keyStore;

      if( verifier != null )
      {
         // Have the verifier validate the cert
         PicketBoxLogger.LOGGER.traceValidatingUsingVerifier(verifier.getClass());
         isValid = verifier.verify(cert, alias, keyStore, trustStore);
      }
      else if (trustStore != null && cert != null)
      {
         // Look for the cert in the truststore using the alias
         X509Certificate storeCert = null;
         try
         {
            storeCert = (X509Certificate) trustStore.getCertificate(alias);
            if(PicketBoxLogger.LOGGER.isTraceEnabled())
            {
               StringBuffer buf = new StringBuffer("\n\t");
               buf.append(PicketBoxMessages.MESSAGES.suppliedCredentialMessage());
               buf.append(cert.getSerialNumber().toString(16));
               buf.append("\n\t\t");
               buf.append(cert.getSubjectDN().getName());
               buf.append("\n\n\t");
               buf.append(PicketBoxMessages.MESSAGES.existingCredentialMessage());
               if( storeCert != null )
               {
                  buf.append(storeCert.getSerialNumber().toString(16));
                  buf.append("\n\t\t");
                  buf.append(storeCert.getSubjectDN().getName());
                  buf.append("\n");
               }
               else
               {
                  ArrayList<String> aliases = new ArrayList<String>();
                  Enumeration<String> en = trustStore.aliases();
                  while (en.hasMoreElements())
                  {
                     aliases.add(en.nextElement());
                  }
                  buf.append(PicketBoxMessages.MESSAGES.noMatchForAliasMessage(alias, aliases));
               }
               PicketBoxLogger.LOGGER.trace(buf.toString());
            }
         }
         catch (KeyStoreException e)
         {
            PicketBoxLogger.LOGGER.warnFailureToFindCertForAlias(alias, e);
         }
         // Ensure that the two certs are equal
         if (cert.equals(storeCert))
            isValid = true;
      }
      else
      {
         PicketBoxLogger.LOGGER.warnFailureToValidateCertificate();
      }

      PicketBoxLogger.LOGGER.traceEndValidateCredential(isValid);
      return isValid;
   }

}