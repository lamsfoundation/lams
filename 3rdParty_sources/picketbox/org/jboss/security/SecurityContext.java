/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */ 
package org.jboss.security;
 
import java.io.Serializable;
import java.util.Map; 

/**
 *  Encapsulation of Authentication, Authorization, Mapping and other
 *  security aspects at the level of a security domain
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @version $Revision$
 *  @since  Aug 24, 2006
 */
public interface SecurityContext extends SecurityManagerLocator, Serializable,Cloneable
{  
   /**
    * Get the SecurityManagement object to get hold of the various
    * managers
    * @return
    */
   public ISecurityManagement getSecurityManagement();
   
   /**
    * Set the SecurityManagement holder
    * @param ism
    */
   public void setSecurityManagement(ISecurityManagement ism);
   
   /**
    * Context Map 
    */
   public Map<String,Object> getData();
   
   /**
    * Return the Security Domain
    */
   public String getSecurityDomain();
   
   /**
    * <p>
    * Sets the security domain.
    * </p>
    * 
    * @param domain a {@code String} representing the security domain.
    */
   public void setSecurityDomain(String domain);
   
   /**
    * Subject Info
    * 
    * @see SecurityContextUtil#getSubject()
    * @see SecurityContextUtil#createSubjectInfo(java.security.Principal, Object, javax.security.auth.Subject)
    */
   SubjectInfo getSubjectInfo(); 
   
   /**
    * Subject Info
    * 
    * @see SecurityContextUtil#getSubject()
    * @see SecurityContextUtil#createSubjectInfo(java.security.Principal, Object, javax.security.auth.Subject)
    */
   void setSubjectInfo(SubjectInfo si); 
   
   /**
    * RunAs that is being propagated into this context
    * by an external context
    * {@link #setIncomingRunAs(RunAs)}
    */
   public RunAs getIncomingRunAs();
   
   /**
    * Set the RunAs that is propagating into this
    * context.
    * @param runAs The RunAs 
    */
   public void setIncomingRunAs(RunAs runAs);
   
   /**
    * RunAs Representation
    * 
    * {@link #setOutgoingRunAs(RunAs)}
    */
   public RunAs getOutgoingRunAs();
   
   /**
    * Set the current RunAs for the security context that will be
    * propagated out to other security context.
    * 
    * RunAs coming into this security context needs to be done
    * from SecurityContextUtil.getCallerRunAs/setCallerRunAs
    * 
    * @param runAs
    */
   public void setOutgoingRunAs(RunAs runAs);
   
   /**
    * Return a utility that is a facade to the internal 
    * storage mechanism of the Security Context
    * 
    * This utility can be used to store information like
    * roles etc in an implementation specific way
    * @return
    */
   public SecurityContextUtil getUtil(); 
}
