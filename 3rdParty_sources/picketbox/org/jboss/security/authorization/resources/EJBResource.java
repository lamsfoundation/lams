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
package org.jboss.security.authorization.resources;

import java.lang.reflect.Method;
import java.util.Map;

import org.jboss.security.authorization.Resource;
import org.jboss.security.authorization.ResourceType;
import org.jboss.security.identity.RoleGroup;

//$Id: EJBResource.java 61962 2007-04-01 04:45:57Z anil.saldhana@jboss.com $

/**
 *  Represents an EJB Resource
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @since  Jul 6, 2006 
 *  @version $Revision: 61962 $
 */
public class EJBResource extends JavaEEResource
{  
   private Method ejbMethod = null;
   private String ejbName = null;
   private String ejbMethodInterface = null;   
   private RoleGroup ejbMethodRoles = null;  
   
   public static final String EJB_VERSION_1_1 = "1.1";
   public static final String EJB_VERSION_2_0 = "2.0";
   public static final String EJB_VERSION_3_0 = "3.0";
   
   private String version = EJBResource.EJB_VERSION_2_0;
      
   /**
    * EJB 1.1 mandates that the security role in the
    * role ref checks has to be present in the descriptors
    */
   private boolean enforceEJBRestrictions = false;
   
   /**
    * Create a new EJBResource.
    * 
    * @param map
    */
   public EJBResource(Map<String,Object> map)
   {
     this.map = map;   
   }

   /**
    * @see Resource#getLayer()
    */
   public ResourceType getLayer()
   {
      return ResourceType.EJB;
   }

   /**
    * Get the EJB Name
    * @return
    */
   public String getEjbName()
   {
      return ejbName;
   }

   /**
    * Set the EJB Name
    * @param ejbName
    */
   public void setEjbName(String ejbName)
   {
      this.ejbName = ejbName;
   }

   /**
    * Get the EJB Method
    * @return
    */
   public Method getEjbMethod()
   {
      return ejbMethod;
   }

   /**
    * Set the EJB Method
    * @param ejbMethod
    */
   public void setEjbMethod(Method ejbMethod)
   {
      this.ejbMethod = ejbMethod;
   }
   
   /**
    * Get the EJB Method Interface as a String
    * @return
    */
   public String getEjbMethodInterface()
   {
      return ejbMethodInterface;
   }

   /**
    * Set the EJB Method Interface as a String
    * @param ejbMethodInterface
    */
   public void setEjbMethodInterface(String ejbMethodInterface)
   {
      this.ejbMethodInterface = ejbMethodInterface;
   } 

   /**
    * Get the Roles assigned to the EJB method
    * @return
    */
   public RoleGroup getEjbMethodRoles()
   {
      return ejbMethodRoles;
   }

   /**
    * Set the roles assigned to the EJB Method
    * @param ejbMethodRoles
    */
   public void setEjbMethodRoles(RoleGroup ejbMethodRoles)
   {
      this.ejbMethodRoles = ejbMethodRoles;
   } 

   /**
    * Specify the EJB1.1 role ref restriction that
    * the rolename has to be present in the DD
    * @return true if enforcement is needed
    */
   public boolean isEnforceEJBRestrictions()
   {
      return enforceEJBRestrictions;
   }

   /**
    * @see #isEnforceEJBRestrictions()
    * @param enforceEJBRestrictions
    */
   public void setEnforceEJBRestrictions(boolean enforceEJBRestrictions)
   {
      this.enforceEJBRestrictions = enforceEJBRestrictions;
   }
   
   /**
    * Get the version of EJB
    * @return
    */ 
   public String getEjbVersion()
   {
      return version;
   }

   /**
    * Set the version of EJB
    * @param version
    */
   public void setEjbVersion(String version)
   {
      this.version = version;
   } 

   public String toString()
   {
      StringBuffer buf = new StringBuffer();
      buf.append("[").append(getClass().getName()).append(":contextMap=").append(map)
      .append(":method=").append(this.ejbMethod)
      .append(":ejbMethodInterface=").append(this.ejbMethodInterface)
      .append(":ejbName=").append(this.ejbName)
      .append(":ejbPrincipal=").append(this.principal) 
      .append(":MethodRoles=").append(this.ejbMethodRoles)
      .append(":securityRoleReferences=").append(this.securityRoleReferences)
      .append(":callerSubject=").append(this.callerSubject)
      .append(":callerRunAs=").append(this.callerRunAsIdentity)
      .append(":callerRunAs=").append(this.callerRunAsIdentity)
      .append(":ejbRestrictionEnforcement=").append(this.enforceEJBRestrictions)
      .append(":ejbVersion=").append(this.version)
      .append("]");
      return buf.toString();
   }
}