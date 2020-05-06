/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */ 
package org.jboss.security.mapping.providers;
  
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

import org.jboss.security.identity.Role;
import org.jboss.security.identity.RoleGroup;
import org.jboss.security.identity.plugins.SimpleRole;
import org.jboss.security.mapping.MappingProvider;
import org.jboss.security.mapping.MappingResult;


/**
 *  Role Mapping Provider that picks up the roles from the
 *  options and then appends them to the passed Group
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @version $Revision$
 *  @since  Aug 24, 2006
 */
public class OptionsRoleMappingProvider implements MappingProvider<RoleGroup> 
{   
   //Standard Strings
   private static final String REPLACE_ROLES_STRING = "replaceRoles";
   private static final String ROLES_MAP = "rolesMap";
   
   private MappingResult<RoleGroup> result;

   private Map<String,Object> options = null;
   
   private Properties roleMapProperties = new Properties();
   
   /**
    * Specifies
    */
   private boolean REPLACE_ROLES = false;
   
   public void init(Map<String,Object> opt)
   {
     this.options = opt;
     if(options != null)
     {
        if(options.containsKey(REPLACE_ROLES_STRING))
        {
           REPLACE_ROLES = "true".equalsIgnoreCase((String)options.get(REPLACE_ROLES_STRING)); 
        }
        if(options.containsKey(ROLES_MAP))
        {
           roleMapProperties = (Properties)options.get(ROLES_MAP);
        } 
     } 
   }
   
   public void setMappingResult(MappingResult<RoleGroup> res)
   { 
      result = res;
   }
   
   public void performMapping(Map<String,Object> contextMap, RoleGroup mappedObject)
   {   
      ArrayList<Role> removeMembers = new ArrayList<Role>();
      ArrayList<Role> addMembers = new ArrayList<Role>(); 

      Collection<Role> rolesList = mappedObject.getRoles();
      if(rolesList != null)
      {
         for(Role r: rolesList)
         {
            String commaSeparatedRoles = roleMapProperties.getProperty(r.getRoleName());
            if(commaSeparatedRoles != null)
            {
               String[] tokens = MappingProviderUtil.getRolesFromCommaSeparatedString(commaSeparatedRoles);
               int len = tokens != null ? tokens.length : 0;
               for(int i = 0; i < len; i++)
               {
                  if(this.REPLACE_ROLES)
                     removeMembers.add(r); 
                  addMembers.add(new SimpleRole(tokens[i])); 
               }
            }  
         }
      } 
      
      //Go through  the remove list
      for(Role p:removeMembers)
      {
         mappedObject.removeRole(p);
      }
      //Go through the add list
      for(Role p:addMembers)
      {
         mappedObject.addRole(p);
      }
      
      result.setMappedObject(mappedObject);
   }   
   
   /**
    * @see MappingProvider#supports(Class)
    */
   public boolean supports(Class<?> p)
   {
      if(RoleGroup.class.isAssignableFrom(p))
         return true;

      return false;
   }
}