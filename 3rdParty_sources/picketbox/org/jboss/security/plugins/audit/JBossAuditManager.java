/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */ 
package org.jboss.security.plugins.audit;

import java.security.PrivilegedActionException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.jboss.security.PicketBoxLogger;
import org.jboss.security.SecurityUtil;
import org.jboss.security.audit.AuditContext;
import org.jboss.security.audit.AuditEvent;
import org.jboss.security.audit.AuditManager;
import org.jboss.security.audit.AuditProvider;
import org.jboss.security.audit.config.AuditProviderEntry;
import org.jboss.security.audit.providers.LogAuditProvider;
import org.jboss.security.config.ApplicationPolicy;
import org.jboss.security.config.AuditInfo;
import org.jboss.security.config.SecurityConfiguration; 
import org.jboss.security.plugins.ClassLoaderLocator;
import org.jboss.security.plugins.ClassLoaderLocatorFactory;

/**
 *  Manages a set of AuditContext
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @version $Revision$
 *  @since  Aug 22, 2006
 */ 
public class JBossAuditManager implements AuditManager
{
   private static ConcurrentMap<String,AuditContext> contexts = new ConcurrentHashMap<String,AuditContext>();
   
   private static AuditContext defaultContext = null;
   
   static
   {
      defaultContext = new JBossAuditContext("Default_Context");
      defaultContext.addProvider(new LogAuditProvider()); 
   }

   private String securityDomain;
   
   public JBossAuditManager(String secDomain)
   {
      this.securityDomain = SecurityUtil.unprefixSecurityDomain(secDomain);  
   }
   
   public AuditContext getAuditContext() throws PrivilegedActionException
   {
	  ClassLoader moduleCL = null;
      AuditContext ac = (AuditContext)contexts.get(securityDomain);
      if(ac == null)
      {
    	  ac = new JBossAuditContext(securityDomain);
    	  ApplicationPolicy ap = SecurityConfiguration.getApplicationPolicy(securityDomain);
    	  if(ap != null)
    	  {
    		  AuditInfo ai = ap.getAuditInfo();
    		  if(ai != null)
    		  {  
    			  List<String> jbossModuleNames = ai.getJBossModuleNames();
    			  if(!jbossModuleNames.isEmpty())
    			  {
    				  ClassLoaderLocator cll = ClassLoaderLocatorFactory.get();
    				   if(cll != null)
    				   {
    					   moduleCL = cll.get(jbossModuleNames);
    				   }
    			  }
				  ac = instantiate(moduleCL, ai);
    		  }
    	  }
      }
      if(ac == null)
      {
         PicketBoxLogger.LOGGER.traceNoAuditContextFoundForDomain(securityDomain);
         ac = defaultContext;
      }
      return ac;
   }
   
   public static AuditContext getAuditContext(String securityDomain)
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(JBossAuditManager.class.getName() + ".getAuditContext"));
      }
      AuditContext ac = (AuditContext)contexts.get(securityDomain);
      if(ac == null)
         ac = defaultContext;
      return ac;
   } 
   
   public static void addAuditContext(String securityDomain, AuditContext ac)
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(JBossAuditManager.class.getName() + ".addAuditContext"));
      }
      contexts.put(securityDomain, ac);
   }

   public void audit(AuditEvent ae)
   {
      AuditContext ac = null;
      try
      {
         ac = getAuditContext();
      }
      catch (PrivilegedActionException e)
      {
        throw new RuntimeException(e);
      }
      ac.audit(ae); 
      //Provide default JBoss trace logging
      if(ac !=  defaultContext)
      {
         defaultContext.audit(ae);
      }
   }

   public String getSecurityDomain()
   { 
      return this.securityDomain;
   } 
   
   private AuditContext instantiate(ClassLoader cl, AuditInfo ai)
   {
       AuditContext ac = new JBossAuditContext(securityDomain);
       AuditProviderEntry[] apeArr = ai.getAuditProviderEntry();
       List<AuditProviderEntry> list = Arrays.asList(apeArr);
       for(AuditProviderEntry ape:list)
       {
          String pname = ape.getName();
          try
          {
             Class<?> clazz = SecurityActions.loadClass(cl, pname);
             ac.addProvider((AuditProvider) clazz.newInstance());
          }
          catch (Exception e)
          {
             throw new RuntimeException(e);
          } 
       }
       return ac;
   }
}