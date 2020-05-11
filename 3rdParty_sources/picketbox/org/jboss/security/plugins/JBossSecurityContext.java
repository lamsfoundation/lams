/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */ 
package org.jboss.security.plugins;

import static org.jboss.security.SecurityConstants.ROLES_IDENTIFIER;

import java.security.Principal;
import java.security.acl.Group;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.callback.CallbackHandler;

import org.jboss.logging.Logger;
import org.jboss.security.AuthenticationManager;
import org.jboss.security.AuthorizationManager;
import org.jboss.security.ISecurityManagement;
import org.jboss.security.PicketBoxMessages;
import org.jboss.security.RunAs;
import org.jboss.security.SecurityConstants;
import org.jboss.security.SecurityContext;
import org.jboss.security.SecurityContextFactory;
import org.jboss.security.SecurityContextUtil;
import org.jboss.security.SecurityManagerLocator;
import org.jboss.security.SubjectInfo;
import org.jboss.security.audit.AuditManager;
import org.jboss.security.auth.callback.JBossCallbackHandler;
import org.jboss.security.identitytrust.IdentityTrustManager;
import org.jboss.security.mapping.MappingManager;

/**
 *  Implementation of the Security Context for the JBoss AS
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @version $Revision$
 *  @since  Aug 30, 2006
 */
public class JBossSecurityContext implements SecurityContext, SecurityManagerLocator
{   
   private static final long serialVersionUID = 1L;
   
   //Define Security Permissions
   
   private static final RuntimePermission getDataPermission
     = new RuntimePermission(JBossSecurityContext.class.getName() + ".getData");
   
   private static final RuntimePermission getSubjectInfoPermission
     = new RuntimePermission(JBossSecurityContext.class.getName() + ".getSubjectInfo");

   private static final RuntimePermission setRolesPermission
     = new RuntimePermission(JBossSecurityContext.class.getName() + ".setRolesPermission");
   
   private static final RuntimePermission setRunAsPermission
     = new RuntimePermission(JBossSecurityContext.class.getName() + ".setRunAsPermission");
   
   private static final RuntimePermission setSubjectInfoPermission
   = new RuntimePermission(JBossSecurityContext.class.getName() + ".setSubjectInfo");
 
   private static final RuntimePermission getSecurityManagementPermission
     = new RuntimePermission(JBossSecurityContext.class.getName() + ".getSecurityManagement");
   
   private static final RuntimePermission setSecurityManagementPermission
     = new RuntimePermission(JBossSecurityContext.class.getName() + ".setSecurityManagement");
   
   private static final RuntimePermission setSecurityDomainPermission
     = new RuntimePermission(JBossSecurityContext.class.getName() + ".setSecurityDomain");

   protected static final Logger log = Logger.getLogger(JBossSecurityContext.class); 
   protected boolean trace = log.isTraceEnabled();  
   
   protected Map<String,Object> contextData = new HashMap<String,Object>();
   
   protected String securityDomain = SecurityConstants.DEFAULT_APPLICATION_POLICY;

   protected SubjectInfo subjectInfo = null;
    
   protected RunAs incomingRunAs = null;
   protected RunAs outgoingRunAs = null;
   
   protected ISecurityManagement iSecurityManagement;
   
   protected transient CallbackHandler callbackHandler = new JBossCallbackHandler(); 
   
   protected transient SecurityContextUtil util = null;
   
   public JBossSecurityContext(String securityDomain)
   {
      this.securityDomain = securityDomain;
      if(this.callbackHandler == null)
         this.callbackHandler = new JBossCallbackHandler();
      
      iSecurityManagement = new DefaultSecurityManagement(this.callbackHandler);
      util = getUtil();
      //Create a null subjectinfo as default
      util.createSubjectInfo(null, null, null);
   }
   

   /**
    * @see SecurityContext#getSecurityManagement()
    * @throws SecurityException  Under a security manager, caller does not have
    *  RuntimePermission("org.jboss.security.plugins.JBossSecurityContext.getSecurityManagement")
    */
   public ISecurityManagement getSecurityManagement()
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
         sm.checkPermission(getSecurityManagementPermission);
      
      return this.iSecurityManagement;
   }

   /**
    * @see SecurityContext#setSecurityManagement(ISecurityManagement)
    * 
    * @throws SecurityException  Under a security manager, caller does not have
    *  RuntimePermission("org.jboss.security.plugins.JBossSecurityContext.setSecurityManagement")
    */
   public void setSecurityManagement(ISecurityManagement securityManagement)
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
         sm.checkPermission(setSecurityManagementPermission);
      
      if(securityManagement == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("securityManagement");
      this.iSecurityManagement = securityManagement;
   }
  
   /**
    * @see SecurityContext#getData()
    * 
    * @throws SecurityException  Under a security manager, caller does not have
    *  RuntimePermission("org.jboss.security.plugins.JBossSecurityContext.getData")
    */ 
   public Map<String,Object> getData()
   { 
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
         sm.checkPermission(getDataPermission);
    
      return contextData;
   }
 
   /**
    * Get the security domain name
    */
   public String getSecurityDomain()
   { 
      return securityDomain;
   }
 
   /*
    * (non-Javadoc)
    * @see org.jboss.security.SecurityContext#setSecurityDomain(java.lang.String)
    */
   public void setSecurityDomain(String securityDomain)
   {
      SecurityManager manager = System.getSecurityManager();
      if(manager != null)
         manager.checkPermission(setSecurityDomainPermission);
      
      if (securityDomain == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("securityDomain");
      this.securityDomain = securityDomain;
   }

   /**
    * @see SecurityContext#getSubjectInfo()
    * 
    * @throws SecurityException  Under a security manager, caller does not have
    *  RuntimePermission("org.jboss.security.plugins.JBossSecurityContext.getSubjectInfo")
    */
   public SubjectInfo getSubjectInfo()
   { 
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
         sm.checkPermission(getSubjectInfoPermission);
      
      return subjectInfo;
   } 
   
   /**
    * @see SecurityContext#getOutgoingRunAs()
    */
   public RunAs getIncomingRunAs()
   { 
      return this.incomingRunAs;
   }

   /**
    * @see SecurityContext#setOutgoingRunAs(RunAs)
    * 
    * @throws SecurityException  Under a security manager, caller does not have
    *  RuntimePermission("org.jboss.security.plugins.JBossSecurityContext.setRunAsPermission")
    * 
    */
   public void setIncomingRunAs(RunAs runAs)
   { 
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
         sm.checkPermission(setRunAsPermission);
      
      this.incomingRunAs = runAs;
   } 

   /**
    * @see SecurityContext#getOutgoingRunAs()
    */
   public RunAs getOutgoingRunAs()
   { 
      return this.outgoingRunAs;
   }

   /**
    * @see SecurityContext#setOutgoingRunAs(RunAs)
    * 
    * @throws SecurityException  Under a security manager, caller does not have
    *  RuntimePermission("org.jboss.security.plugins.JBossSecurityContext.setRunAsPermission")
    */
   public void setOutgoingRunAs(RunAs runAs)
   { 
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
         sm.checkPermission(setRunAsPermission);
     
      this.outgoingRunAs = runAs;
   } 
   
   /**
    * @see SecurityContext#getUtil()
    */
   public SecurityContextUtil getUtil()
   {  
      if(util == null)
      {
         try
         {
            util = SecurityContextFactory.createUtil(this);
         }
         catch (Exception e)
         {
            throw new IllegalStateException(e);
         } 
      } 
      return util;
   }
   


   public AuditManager getAuditManager()
   {
      return this.iSecurityManagement.getAuditManager(this.securityDomain);
   }


   public AuthenticationManager getAuthenticationManager()
   {
      return this.iSecurityManagement.getAuthenticationManager(this.securityDomain);
   }


   public AuthorizationManager getAuthorizationManager()
   {
      return this.iSecurityManagement.getAuthorizationManager(this.securityDomain);
   }


   public IdentityTrustManager getIdentityTrustManager()
   {
      return this.iSecurityManagement.getIdentityTrustManager(this.securityDomain);
   }


   public MappingManager getMappingManager()
   {
      return this.iSecurityManagement.getMappingManager(this.securityDomain);
   }     
   
   
   //Value Added Methods
   /**
    * 
    * @throws SecurityException  Under a security manager, caller does not have
    *  RuntimePermission("org.jboss.security.plugins.JBossSecurityContext.setSubjectInfo")
    */
   public void setSubjectInfo(SubjectInfo si)
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
         sm.checkPermission(setSubjectInfoPermission);
      
      this.subjectInfo = si;
   }
   
   /**
    * 
    * @param roles
    * @param replace
    * 
    * @throws SecurityException  Under a security manager, caller does not have
    *  RuntimePermission("org.jboss.security.plugins.JBossSecurityContext.setRolesPermission")
    */
   public void setRoles(Group roles, boolean replace)
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
         sm.checkPermission(setRolesPermission);
    
      Group mergedRoles = roles;
      if(!replace)
      {
         mergedRoles = mergeGroups( (Group)contextData.get(ROLES_IDENTIFIER), roles); 
      } 
      contextData.put(ROLES_IDENTIFIER, mergedRoles);
   }
   
   private Group mergeGroups(Group a, Group b)
   {
      Group newGroup = b;
      if(a != null)
      {
         Enumeration<? extends Principal> en = a.members();
         while(en.hasMoreElements())
         {
            newGroup.addMember(en.nextElement());
         } 
      } 
      return newGroup; 
   } 
   
   
   /**
    * Set the CallbackHandler for the Managers in the SecurityContext
    * @param callbackHandler
    */
   public void setCallbackHandler(CallbackHandler callbackHandler)
   {
      this.callbackHandler = callbackHandler;
   }

   
   @Override
   public String toString()
   {
      StringBuilder builder = new StringBuilder();
      builder.append("[").append(getClass().getCanonicalName()).append("()");
      builder.append(this.securityDomain).append(")]"); 
      return builder.toString();
   }


   @SuppressWarnings("unchecked")
   @Override
   public Object clone() throws CloneNotSupportedException
   { 
      JBossSecurityContext jsc = (JBossSecurityContext) super.clone();
      if(jsc != null)
      {
         HashMap<String,Object> cmap = (HashMap<String,Object>)contextData;
         jsc.contextData = (Map<String, Object>) (cmap).clone();
      }
      return jsc;
   }
} 