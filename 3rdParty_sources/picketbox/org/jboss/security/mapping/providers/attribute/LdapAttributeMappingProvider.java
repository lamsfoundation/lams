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
package org.jboss.security.mapping.providers.attribute;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.management.ObjectName;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;

import org.jboss.security.PicketBoxLogger;
import org.jboss.security.PicketBoxMessages;
import org.jboss.security.SecurityConstants;
import org.jboss.security.identity.Attribute;
import org.jboss.security.identity.AttributeFactory;
import org.jboss.security.mapping.MappingProvider;
import org.jboss.security.mapping.MappingResult;

/**
 * Maps attributes from LDAP
 *
 * The options include whatever options your LDAP JNDI provider
 supports. Examples of standard property names are:

 * Context.INITIAL_CONTEXT_FACTORY = "java.naming.factory.initial"
 * Context.SECURITY_PROTOCOL = "java.naming.security.protocol"
 * Context.PROVIDER_URL = "java.naming.provider.url"
 * Context.SECURITY_AUTHENTICATION = "java.naming.security.authentication"
 *
 * Other Module Options:-
 *
 * bindDN:The DN used to bind against the ldap server for the user and
 roles queries. This is some DN with read/search permissions on the baseCtxDN and
 rolesCtxDN values.
 *
 * bindCredential: The password for the bindDN. This can be encrypted if the
 jaasSecurityDomain is specified.
 *
 * baseCtxDN: The fixed DN of the context to start the user search from.
 *
 * baseFilter:A search filter used to locate the context of the user to
 authenticate. The input username/userDN as obtained from the login module
 callback will be substituted into the filter anywhere a "{0}" expression is
 seen. This substituion behavior comes from the standard
 __DirContext.search(Name, String, Object[], SearchControls cons)__ method. An
 common example search filter is "(uid={0})".

 * searchTimeLimit:The timeout in milliseconds for the user/role searches.
 Defaults to 10000 (10 seconds).

 * attributeList: A comma-separated list of attributes for the user
 * (Example:  mail,cn,sn,employeeType,employeeNumber)
 *
 * jaasSecurityDomain: The JMX ObjectName of the JaasSecurityDomain to use
 to decrypt the java.naming.security.principal. The encrypted form of the
 password is that returned by the JaasSecurityDomain#encrypt64(byte[]) method.
 The org.jboss.security.plugins.PBEUtils can also be used to generate the
 encrypted form.
 *
 * @author Anil.Saldhana@redhat.com
 * @since August 5, 2009
 */
public class LdapAttributeMappingProvider implements MappingProvider<List<Attribute<String>>>
{
   private Map<String, Object> options;

   protected int searchTimeLimit = 10000;

   private static final String BIND_DN = "bindDN";

   private static final String BIND_CREDENTIAL = "bindCredential";

   private static final String BASE_CTX_DN = "baseCtxDN";

   private static final String BASE_FILTER_OPT = "baseFilter";

   private static final String SEARCH_TIME_LIMIT_OPT = "searchTimeLimit";

   private static final String ATTRIBUTE_LIST_OPT = "attributeList";

   private static final String SECURITY_DOMAIN_OPT = "jaasSecurityDomain";

   private MappingResult<List<Attribute<String>>> mappingResult;

   public void init(Map<String, Object> options)
   {
      this.options = options;
   }

   public void performMapping(Map<String, Object> map, List<Attribute<String>> mappedObject)
   {
      List<Attribute<String>> attributeList = new ArrayList<Attribute<String>>();

      Principal principal = (Principal) map.get(SecurityConstants.PRINCIPAL_IDENTIFIER);
      if(principal != null)
      {
         String user = principal.getName();

         String bindDN = (String) options.get(BIND_DN);
         if(bindDN == null || bindDN.length() == 0)
         {
            PicketBoxLogger.LOGGER.traceBindDNNotFound();
            return;
         }
         String bindCredential = (String) options.get(BIND_CREDENTIAL);
         if (org.jboss.security.Util.isPasswordCommand(bindCredential))
            try
            {
               bindCredential = new String(org.jboss.security.Util.loadPassword(bindCredential));
            }
            catch (Exception e1)
            {
               PicketBoxLogger.LOGGER.errorDecryptingBindCredential(e1);
               return;
            }
         String securityDomain = (String) options.get(SECURITY_DOMAIN_OPT);
         if (securityDomain != null)
         {
            try
            {
               ObjectName serviceName = new ObjectName(securityDomain);
               char[] tmp = MappingProvidersDecodeAction.decode(bindCredential, serviceName);
               bindCredential = new String(tmp);
            }
            catch (Exception e)
            {
               PicketBoxLogger.LOGGER.errorDecryptingBindCredential(e);
               return;
            }
         }

         InitialLdapContext ctx = null;
         ClassLoader currentTCCL = SecurityActions.getContextClassLoader();
         try
         {
            if (currentTCCL != null)
               SecurityActions.setContextClassLoader(null);
            ctx = this.constructInitialLdapContext(bindDN, bindCredential);
         }
         catch (NamingException e)
         {
            if (ctx != null) {
               try {
                  ctx.close();
               }
               catch (NamingException ne){
                  PicketBoxLogger.LOGGER.debugIgnoredException(ne);
               }
            }
            throw new RuntimeException(e);
         }

         String timeLimit = (String) options.get(SEARCH_TIME_LIMIT_OPT);
         if (timeLimit != null)
         {
            try
            {
               searchTimeLimit = Integer.parseInt(timeLimit);
            }
            catch (NumberFormatException e)
            {
               PicketBoxLogger.LOGGER.debugFailureToParseNumberProperty(SEARCH_TIME_LIMIT_OPT, searchTimeLimit);
            }
         }
         if(searchTimeLimit == 0)
            searchTimeLimit = 10000;

         String baseDN = (String) options.get(BASE_CTX_DN);
         String baseFilter = (String) options.get(BASE_FILTER_OPT);

         SearchControls constraints = new SearchControls();
         constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);

         constraints.setTimeLimit(searchTimeLimit);

         String attributePattern = (String) options.get(ATTRIBUTE_LIST_OPT);

         //Take care of the attributes we want
         String neededAttributes[] = getNeededAttributes(attributePattern);

         constraints.setReturningAttributes(neededAttributes);

         NamingEnumeration<SearchResult> results = null;

         Object[] filterArgs = {user};
         try
         {
            if(baseDN == null)
               throw PicketBoxMessages.MESSAGES.invalidNullArgument(BASE_CTX_DN);
            results = ctx.search(baseDN, baseFilter, filterArgs, constraints);
            if (results.hasMore() == false)
            {
               results.close();
               throw PicketBoxMessages.MESSAGES.failedToFindBaseContextDN(baseDN);
            }
            SearchResult sr = results.next();
            String name = sr.getName();
            String userDN = null;
            if (sr.isRelative() == true)
               userDN = name + "," + baseDN;
            else
               throw PicketBoxMessages.MESSAGES.unableToFollowReferralForAuth(name);

            results.close();

            //Finished Authentication.  Lets look for the attributes
            filterArgs = new Object[]{user, userDN};
            results = ctx.search(userDN, baseFilter, filterArgs, constraints);
            while (results.hasMore())
            {
            sr = (SearchResult) results.next();
            Attributes attributes = sr.getAttributes();
            NamingEnumeration<? extends javax.naming.directory.Attribute> ne = attributes.getAll();

            while(ne != null && ne.hasMoreElements())
            {
               javax.naming.directory.Attribute ldapAtt = ne.next();
                  if("mail".equalsIgnoreCase(ldapAtt.getID()))
                  {
                     attributeList.add(AttributeFactory.createEmailAddress((String) ldapAtt.get()));
                  }
                  else if( ldapAtt.size() > 1 ) {
                     for (int i = 0; i < ldapAtt.size(); i++) {
                        attributeList.add(AttributeFactory.createAttribute(ldapAtt.getID(),
                             (String) ldapAtt.get(i)));
                     }
                  }
                  else
                     attributeList.add(AttributeFactory.createAttribute(ldapAtt.getID(),
                          (String) ldapAtt.get()));
               }
            }

         }
         catch(NamingException ne)
         {
            PicketBoxLogger.LOGGER.debugIgnoredException(ne);
            return;
         }
         finally
         {
            try
            {
               if (results != null)
                  results.close();
               if (ctx != null)
                  ctx.close();
            }
            catch (NamingException namingException)
            {
               PicketBoxLogger.LOGGER.debugIgnoredException(namingException);
            }
            if (currentTCCL != null)
               SecurityActions.setContextClassLoader(currentTCCL);
         }

         results = null;
      }

      mappedObject.addAll(attributeList);
      mappingResult.setMappedObject(mappedObject);
   }

   public void setMappingResult(MappingResult<List<Attribute<String>>> result)
   {
      this.mappingResult = result;
   }

   public boolean supports(Class<?> clazz)
   {
      if(Attribute.class.isAssignableFrom(clazz))
        return true;

      return false;
   }


   private InitialLdapContext constructInitialLdapContext(String dn, Object credential) throws NamingException
   {
      Properties env = new Properties();
      Iterator<Entry<String, Object>> iter = options.entrySet().iterator();
      while (iter.hasNext())
      {
         Entry<String, Object> entry = iter.next();
         env.put(entry.getKey(), entry.getValue());
      }

      // Set defaults for key values if they are missing
      String factoryName = env.getProperty(Context.INITIAL_CONTEXT_FACTORY);
      if (factoryName == null)
      {
         factoryName = "com.sun.jndi.ldap.LdapCtxFactory";
         env.setProperty(Context.INITIAL_CONTEXT_FACTORY, factoryName);
      }
      String authType = env.getProperty(Context.SECURITY_AUTHENTICATION);
      if (authType == null)
         env.setProperty(Context.SECURITY_AUTHENTICATION, "simple");
      String protocol = env.getProperty(Context.SECURITY_PROTOCOL);
      String providerURL = (String) options.get(Context.PROVIDER_URL);
      if (providerURL == null)
         providerURL = "ldap://localhost:" + ((protocol != null && protocol.equals("ssl")) ? "636" : "389");

      env.setProperty(Context.PROVIDER_URL, providerURL);
      // JBAS-3555, allow anonymous login with no bindDN and bindCredential
      if (dn != null)
         env.setProperty(Context.SECURITY_PRINCIPAL, dn);
      if (credential != null)
         env.put(Context.SECURITY_CREDENTIALS, credential);
      this.traceLDAPEnv(env);
      return new InitialLdapContext(env, null);
   }

   /**
    * <p>
    * Logs the specified LDAP env, masking security-sensitive information (passwords).
    * </p>
    *
    * @param env the LDAP env to be logged.
    */
   private void traceLDAPEnv(Properties env)
   {
      Properties tmp = new Properties();
      tmp.putAll(env);
      if (tmp.containsKey(Context.SECURITY_CREDENTIALS))
         tmp.setProperty(Context.SECURITY_CREDENTIALS, "******");
      if (tmp.containsKey(BIND_CREDENTIAL))
         tmp.setProperty(BIND_CREDENTIAL, "******");
      PicketBoxLogger.LOGGER.traceLDAPConnectionEnv(tmp);
   }

    private String[] getNeededAttributes(String commaSeparatedList)
   {
      ArrayList<String> arrayList = new ArrayList<String>();
      if (commaSeparatedList != null)
      {
         StringTokenizer st = new StringTokenizer(commaSeparatedList,",");
         while(st.hasMoreTokens())
         {
            arrayList.add(st.nextToken());
         }
      }
      String[] strArr = new String[arrayList.size()];
      return arrayList.toArray(strArr);
   }
}
