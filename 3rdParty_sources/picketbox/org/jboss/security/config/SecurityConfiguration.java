/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */ 
package org.jboss.security.config;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.security.PicketBoxMessages;

/**
 *  Class that provides the Configuration for authentication,
 *  authorization, mapping info etc
 *  It also holds the information like JSSE keystores, keytypes and
 *  other crypto configuration
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @version $Revision$
 *  @since  Aug 28, 2006
 */
public class SecurityConfiguration
{
   /**
    * Map of Application Policies keyed in by name
    */
   private static final Map<String,ApplicationPolicy> appPolicies = new ConcurrentHashMap<String, ApplicationPolicy>();
   private static String cipherAlgorithm;
   private static int iterationCount;
   private static String salt;
   private static String keyStoreType;
   private static String keyStoreURL;
   private static String keyStorePass;
   private static String trustStoreType;
   private static String trustStorePass;
   private static String trustStoreURL;
   private static Key cipherKey;
   private static AlgorithmParameterSpec cipherSpec;
   private static boolean deepCopySubjectMode;
   
   /**
    * Add an application policy
    * @param applicationPolicy Application Policy
    */
   public static void addApplicationPolicy(ApplicationPolicy applicationPolicy)
   { 
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".addApplicationPolicy"));
      }
      if(applicationPolicy == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("applicationPolicy");
      appPolicies.put(applicationPolicy.getName(), applicationPolicy);
   }
   
   /**
    * Remove the Application Policy
    * @param name Name of the Policy
    */
   public static void removeApplicationPolicy(String name)
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".removeApplicationPolicy"));
      }
      appPolicies.remove(name);
   }
   
   /**
    * Get an application policy 
    * @param policyName Name of the Policy (such as "other", "messaging")
    * @return
    */
   public static ApplicationPolicy getApplicationPolicy(String policyName)
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".getApplicationPolicy"));
      }
      return (ApplicationPolicy)appPolicies.get(policyName);
   } 
   
   public static String getCipherAlgorithm()
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".getCipherAlgorithm"));
      }
      return cipherAlgorithm;
   }
   
   public static void setCipherAlgorithm(String ca)
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".setCipherAlgorithm"));
      }
      cipherAlgorithm = ca;
   }
   
   public static Key getCipherKey()
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".getCipherKey"));
      }
      return cipherKey;
   }
   
   public static void setCipherKey(Key ca)
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".setCipherKey"));
      }
      cipherKey = ca;
   }
   
   public static AlgorithmParameterSpec getCipherSpec()
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".getCipherSpec"));
      }
      return cipherSpec;
   }
   
   public static void setCipherSpec(AlgorithmParameterSpec aps)
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".setCipherSpec"));
      }
      cipherSpec = aps;
   }
   
   public static int getIterationCount()
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".getIterationCount"));
      }
      return iterationCount;
   }

   /** Set the iteration count used with PBE based on the keystore password.
    * @param count - an iteration count randomization value
    */ 
   public static void setIterationCount(int count)
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".setIterationCount"));
      }
      iterationCount = count;
   }
   
   
   public static String getSalt()
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".getSalt"));
      }
      return salt;
   }
   /** Set the salt used with PBE based on the keystore password.
    * @param s - an 8 char randomization string
    */ 
   public static void setSalt(String s)
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".setSalt"));
      }
      salt = s;
   }

   
   /** KeyStore implementation type being used.
   @return the KeyStore implementation type being used.
   */
   public static String getKeyStoreType()
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".getKeyStoreType"));
      }
      return keyStoreType;
   }
   /** Set the type of KeyStore implementation to use. This is
   passed to the KeyStore.getInstance() factory method.
   */
   public static void setKeyStoreType(String type)
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".setKeyStoreType"));
      }
      keyStoreType = type;
   } 
   /** Get the KeyStore database URL string.
   */
   public static String getKeyStoreURL()
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".getKeyStoreURL"));
      }
      return keyStoreURL;
   }
   /** Set the KeyStore database URL string. This is used to obtain
   an InputStream to initialize the KeyStore.
   */
   public static void setKeyStoreURL(String storeURL)
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".setKeyStoreURL"));
      }
      keyStoreURL = storeURL;
   }
   
   /** Get the credential string for the KeyStore.
    */
    public static String getKeyStorePass()
    {
       SecurityManager sm = System.getSecurityManager();
       if (sm != null) {
          sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".getKeyStorePass"));
       }
       return keyStorePass ;
    }
   
   /** Set the credential string for the KeyStore.
   */
   public static void setKeyStorePass(String password)
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".setKeyStorePass"));
      }
      keyStorePass = password;
   }

  /** Get the type of the trust store
   * @return the type of the trust store
   */ 
  public static String getTrustStoreType()
  {
     SecurityManager sm = System.getSecurityManager();
     if (sm != null) {
        sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".getTrustStoreType"));
     }
     return trustStoreType;
  }
  
  /** Set the type of the trust store
   * @param type - the trust store implementation type
   */ 
  public static void setTrustStoreType(String type)
  {
     SecurityManager sm = System.getSecurityManager();
     if (sm != null) {
        sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".setTrustStoreType"));
     }
     trustStoreType = type;
  }
  
  /** Set the credential string for the trust store.
   */
   public static String getTrustStorePass()
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".getTrustStorePass"));
      }
      return trustStorePass;
   }
  
  /** Set the credential string for the trust store.
  */
  public static void setTrustStorePass(String password)
  {
     SecurityManager sm = System.getSecurityManager();
     if (sm != null) {
        sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".setTrustStorePass"));
     }
     trustStorePass = password;
  }
  
  /** Get the trust store database URL string.
   */
  public static String getTrustStoreURL()
  {
     SecurityManager sm = System.getSecurityManager();
     if (sm != null) {
        sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".getTrustStoreURL"));
     }
     return trustStoreURL;
  }
  
  /** Set the trust store database URL string. This is used to obtain
   an InputStream to initialize the trust store.
   */
  public static void setTrustStoreURL(String storeURL)
  {
     SecurityManager sm = System.getSecurityManager();
     if (sm != null) {
        sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".setTrustStoreURL"));
     }
     trustStoreURL = storeURL;
  }

  public static boolean isDeepCopySubjectMode()
  {
     SecurityManager sm = System.getSecurityManager();
     if (sm != null) {
        sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".isDeepCopySubjectMode"));
     }
     return deepCopySubjectMode;
  }

  public static void setDeepCopySubjectMode(boolean dcsm)
  {
     SecurityManager sm = System.getSecurityManager();
     if (sm != null) {
        sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".setDeepCopySubjectMode"));
     }
     deepCopySubjectMode = dcsm;
  }
}