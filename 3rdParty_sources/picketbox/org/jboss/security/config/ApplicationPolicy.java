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
package org.jboss.security.config;

import java.security.Principal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.jboss.security.PicketBoxMessages;
import org.jboss.security.auth.login.AuthenticationInfo;
import org.jboss.security.auth.login.BaseAuthenticationInfo;
import org.jboss.security.auth.login.JASPIAuthenticationInfo;
import org.jboss.security.identity.RoleGroup;

// $Id$

/**
 * Application Policy Information Holder - Authentication - Authorization - Audit - Mapping
 * 
 * @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 * @author <a href="mailto:mmoyses@redhat.com">Marcus Moyses</a>
 * @since Jun 9, 2006
 * @version $Revision$
 */
public class ApplicationPolicy
{
   private final String name;

   private BaseAuthenticationInfo authenticationInfo;

   private ACLInfo aclInfo;

   private AuthorizationInfo authorizationInfo;

   private AuditInfo auditInfo;

   private final Map<String, MappingInfo> mappingInfos = new HashMap<String, MappingInfo>();

   private IdentityTrustInfo identityTrustInfo;

   // Base application policy (if any)
   private String baseApplicationPolicyName;

   // Parent PolicyConfig
   private PolicyConfig policyConfig = new PolicyConfig();

   public ApplicationPolicy(String theName)
   {
      if (theName == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("theName");
      this.name = theName;
   }

   public ApplicationPolicy(String theName, BaseAuthenticationInfo info)
   {
      this(theName);
      authenticationInfo = info;
   }

   public ApplicationPolicy(String theName, AuthorizationInfo info)
   {
      this(theName);
      authorizationInfo = info;
   }

   public ApplicationPolicy(String theName, BaseAuthenticationInfo info, AuthorizationInfo info2)
   {
      this(theName);
      authenticationInfo = info;
      authorizationInfo = info2;
   }

   public ACLInfo getAclInfo()
   {
      ACLInfo info = null;
      ApplicationPolicy basePolicy = this.getBaseApplicationPolicy();
      if (basePolicy != null)
         info = basePolicy.getAclInfo();
      if (info != null && this.aclInfo == null)
         return info;
      else if (info != null)
         return (ACLInfo) this.aclInfo.merge(info);
      else
         return aclInfo;
   }

   public void setAclInfo(ACLInfo aclInfo)
   {
      this.aclInfo = aclInfo;
   }

   public BaseAuthenticationInfo getAuthenticationInfo()
   {
      BaseAuthenticationInfo bai = null;
      ApplicationPolicy ap = this.getBaseApplicationPolicy();
      if (ap != null)
         bai = ap.getAuthenticationInfo();
      if (bai != null && authenticationInfo == null)
         return bai;
      else if (bai != null)
         return (BaseAuthenticationInfo) authenticationInfo.merge(bai);
      else
         return authenticationInfo;
   }

   public void setAuthenticationInfo(BaseAuthenticationInfo authenticationInfo)
   {
      this.authenticationInfo = authenticationInfo;
   }

   public AuthorizationInfo getAuthorizationInfo()
   {
      AuthorizationInfo bai = null;
      ApplicationPolicy ap = this.getBaseApplicationPolicy();
      if (ap != null)
         bai = ap.getAuthorizationInfo();
      if (bai != null && authorizationInfo == null)
         return bai;
      else if (bai != null)
         return (AuthorizationInfo) authorizationInfo.merge(bai);
      else
         return authorizationInfo;
   }

   public void setAuthorizationInfo(AuthorizationInfo authorizationInfo)
   {
      this.authorizationInfo = authorizationInfo;
   }

   /**
    * <p>
    * Gets the {@code MappingInfo} object that contains the entries that will be used to map roles.
    * </p>
    * 
    * @return the {@code MappingInfo} that must be used when mapping roles.
    * @deprecated use {@link ApplicationPolicy#getMappingInfo(String)} instead.
    */
   @Deprecated
   public MappingInfo getRoleMappingInfo()
   {
      return this.getMappingInfo("role");
   }

   /**
    * <p>
    * Sets the {@code MappingInfo} object that must be used when mapping roles.
    * </p>
    * 
    * @param roleMappingInfo the {@code MappingInfo} instance to be set.
    * @deprecated use {@link ApplicationPolicy#setMappingInfo(String, MappingInfo)} instead.
    */
   @Deprecated
   public void setRoleMappingInfo(MappingInfo roleMappingInfo)
   {
      this.setMappingInfo("role", roleMappingInfo);
   }

   /**
    * <p>
    * Gets the {@code MappingInfo} object that contains the entries that will be used to map principals.
    * </p>
    * 
    * @return the {@code MappingInfo} that must be used when mapping principals.
    * @deprecated use {@link ApplicationPolicy#getMappingInfo(String)} instead.
    */
   @Deprecated
   public MappingInfo getPrincipalMappingInfo()
   {
      return this.getMappingInfo("principal");
   }

   /**
    * <p>
    * Sets the {@code MappingInfo} object that must be used when mapping principals.
    * </p>
    * 
    * @param principalMappingInfo the {@code MappingInfo} instance to be set.
    * @deprecated use {@link ApplicationPolicy#setMappingInfo(String, MappingInfo)} instead.
    */
   @Deprecated
   public void setPrincipalMappingInfo(MappingInfo principalMappingInfo)
   {
      this.setMappingInfo("principal", principalMappingInfo);
   }

   /**
    * <p>
    * Gets the {@code MappingInfo} instance that can map objects of the specified class. 
    * </p>
    * 
    * @param t the class of the objects that are to be mapped.
    * @return the {@code MappingInfo} instance that must be used to map objects of the specified class.
    * @deprecated use {@link ApplicationPolicy#getMappingInfo(String)} instead.
    */
   @Deprecated
   public <T> MappingInfo getMappingInfo(Class<T> t)
   {
      if (t == RoleGroup.class)
         return this.getRoleMappingInfo();
      if (t == Principal.class)
         return this.getPrincipalMappingInfo();

      throw PicketBoxMessages.MESSAGES.invalidType(RoleGroup.class.getName() + "/" + Principal.class.getName());
   }

   /**
    * <p>
    * Gets the {@code MappingInfo} instance that can perform the mappings of the specified type.
    * </p>
    * 
    * @param mappingType a {@code String} representing the type of the mappings that are to be performed. This
    *            {@code String} must match the value of the {@code type} attribute of the {@code mapping-module} that
    *            has been configured in the application policy. For example, consider the following mapping policy:
    * 
    * <pre>
    * &lt;application-policy name=&quot;test&quot;&gt;
    *    &lt;authentication&gt;
    *    ...
    *    &lt;/authentication&gt;
    *    &lt;mapping&gt;
    *       &lt;mapping-module code = &quot;org.jboss.test.mapping.MappingModule1&quot; type=&quot;role&quot;&gt;
    *          &lt;module-option name = &quot;option1&quot;&gt;value1&lt;/module-option&gt;
    *       &lt;/mapping-module&gt;
    *       &lt;mapping-module code = &quot;org.jboss.test.mapping.MappingModule2&quot; type=&quot;principal&quot;&gt;
    *          &lt;module-option name = &quot;option2&quot;&gt;value2&lt;/module-option&gt;
    *       &lt;/mapping-module&gt;
    *    &lt;/mapping&gt; while a
    * &lt;/application-policy&gt;
    * </pre>
    * 
    * Executing this method with {@code "role"} as parameter would return a {@code MappingInfo} that is capable of
    * mapping roles using the {@code MappingModule1}. Likewise, executing this method with {@code "principal"} as
    * parameter would return a {@code MappingInfo} that can map principals using the {@code MappingModule2}.
    * @return the {@code MappingInfo} instance that can perform the mappings of the specified type, or {@code null} if
    *         no suitable {@code MappingInfo} can be found.
    */
   public MappingInfo getMappingInfo(String mappingType)
   {
      mappingType = mappingType.toLowerCase(Locale.ENGLISH);
      MappingInfo bai = null;
      ApplicationPolicy ap = this.getBaseApplicationPolicy();
      if (ap != null)
         bai = ap.getMappingInfo(mappingType);

      MappingInfo mappings = this.mappingInfos.get(mappingType);
      if (bai != null && mappings == null)
         return bai;
      else if (bai != null)
         return (MappingInfo) mappings.merge(bai);
      else
         return mappings;
   }

   /**
    * <p>
    * Sets the {@code MappingInfo} that must be used to perform the mappings of the specified type.
    * </p>
    * 
    * @param mappingType the type of mappings that can be performed by the {@code MappingInfo}.
    * @param info a reference to the {@code MappingInfo} instance to be set.
    */
   public void setMappingInfo(String mappingType, MappingInfo info)
   {
      mappingType = mappingType.toLowerCase(Locale.ENGLISH);
      // if there is a registered info for the specified type, merge the modules.
      if(this.mappingInfos.containsKey(mappingType))
         this.mappingInfos.get(mappingType).add(info.getModuleEntries());
      else
         this.mappingInfos.put(mappingType, info);
   }

   public AuditInfo getAuditInfo()
   {
      AuditInfo bai = null;
      ApplicationPolicy ap = this.getBaseApplicationPolicy();
      if (ap != null)
         bai = ap.getAuditInfo();

      if (bai != null && auditInfo == null)
         return bai;
      else if (bai != null)
         return (AuditInfo) auditInfo.merge(bai);
      else
         return auditInfo;
   }

   public void setAuditInfo(AuditInfo auditInfo)
   {
      this.auditInfo = auditInfo;
   }

   public IdentityTrustInfo getIdentityTrustInfo()
   {
      IdentityTrustInfo bai = null;
      ApplicationPolicy ap = this.getBaseApplicationPolicy();
      if (ap != null)
         bai = ap.getIdentityTrustInfo();

      if (bai != null && identityTrustInfo == null)
         return bai;
      else if (bai != null)
         return (IdentityTrustInfo) identityTrustInfo.merge(bai);
      else
         return identityTrustInfo;
   }

   public void setIdentityTrustInfo(IdentityTrustInfo identityTrustInfo)
   {
      this.identityTrustInfo = identityTrustInfo;
   }

   public String getBaseApplicationPolicyName()
   {
      return baseApplicationPolicyName;
   }

   public void setBaseApplicationPolicyName(String baseApplicationPolicy)
   {
      this.baseApplicationPolicyName = baseApplicationPolicy;
   }

   public String getName()
   {
      return name;
   }

   public PolicyConfig getPolicyConfig()
   {
      return policyConfig;
   }

   public void setPolicyConfig(PolicyConfig policyConfig)
   {
      this.policyConfig = policyConfig;
   }

   private ApplicationPolicy getBaseApplicationPolicy()
   {
      ApplicationPolicy ap = null;
      if (this.baseApplicationPolicyName != null)
      {
         ap = this.policyConfig.get(this.baseApplicationPolicyName);
         // The base application policy may exist in a different location
         if (ap == null)
            ap = SecurityConfiguration.getApplicationPolicy(this.baseApplicationPolicyName);
      }
      return ap;
   }
   
   /**
    * Write element content.
    * 
    * @param writer
    * @throws XMLStreamException
    */
   public void writeContent(XMLStreamWriter writer) throws XMLStreamException
   {
      writer.writeStartElement(Element.SECURITY_DOMAIN.getLocalName());
      writer.writeAttribute(Attribute.NAME.getLocalName(), name);
      if (baseApplicationPolicyName != null)
      {
         writer.writeAttribute(Attribute.EXTENDS.getLocalName(), baseApplicationPolicyName);
      }
      if (authenticationInfo != null)
      {
         if (authenticationInfo instanceof AuthenticationInfo)
         {
            writer.writeStartElement(Element.AUTHENTICATION.getLocalName());
            ((AuthenticationInfo) authenticationInfo).writeContent(writer);
         }
         else
         {
            writer.writeStartElement(Element.AUTHENTICATION_JASPI.getLocalName());
            ((JASPIAuthenticationInfo) authenticationInfo).writeContent(writer);
         }
      }
      if (aclInfo != null)
      {
         writer.writeStartElement(Element.ACL.getLocalName());
         aclInfo.writeContent(writer);
      }
      if (authorizationInfo != null)
      {
         writer.writeStartElement(Element.AUTHORIZATION.getLocalName());
         authorizationInfo.writeContent(writer);
      }
      if (auditInfo != null)
      {
         writer.writeStartElement(Element.AUDIT.getLocalName());
         auditInfo.writeContent(writer);
      }
      if (identityTrustInfo != null)
      {
         writer.writeStartElement(Element.IDENTITY_TRUST.getLocalName());
         identityTrustInfo.writeContent(writer);
      }
      if (mappingInfos != null && mappingInfos.size() > 0)
      {
         writer.writeStartElement(Element.MAPPING.getLocalName());
         for (Entry<String, MappingInfo> entry : mappingInfos.entrySet())
         {
            entry.getValue().writeContent(writer);
         }
      }
      writer.writeEndElement();
   }
}