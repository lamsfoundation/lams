/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.security;

import java.security.Principal;
import java.security.acl.Group;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.lang.reflect.Constructor;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.apache.log4j.Logger;

public class AbstractServerLoginModule   implements LoginModule
{
   protected Subject subject;
   protected CallbackHandler callbackHandler;
   protected Map sharedState;
   protected Map options;
   protected Logger log = Logger.getLogger(AbstractServerLoginModule.class);	
   /** Flag indicating if the shared credential should be used */
   protected boolean useFirstPass;
   /** Flag indicating if the login phase succeeded. Subclasses that override
	the login method must set this to true on successful completion of login
	*/
   protected boolean loginOk;
   /** An optional custom Principal class implementation */
   protected String principalClassName;
   /** the principal to use when a null username and password are seen */
   protected Principal unauthenticatedIdentity;

   /** Initialize the login module. This stores the subject, callbackHandler
	* and sharedState and options for the login session. Subclasses should override
	* if they need to process their own options. A call to super.initialize(...)
	* must be made in the case of an override.
	* <p>
	* @option password-stacking: If this is set to "useFirstPass", the login
	* identity will be taken from the <code>javax.security.auth.login.name</code>
	* value of the sharedState map, and the proof of identity from the
	* <code>javax.security.auth.login.password</code> value of the sharedState
	* map.
	* @option principalClass: A Principal implementation that support a ctor
	*   taking a String argument for the princpal name.
	* @option unauthenticatedIdentity: the name of the principal to asssign
	* and authenticate when a null username and password are seen.
	* 
	* @param subject the Subject to update after a successful login.
	* @param callbackHandler the CallbackHandler that will be used to obtain the
	*    the user identity and credentials.
	* @param sharedState a Map shared between all configured login module instances
	* @param options the parameters passed to the login module.
	*/
   public void initialize(Subject subject, CallbackHandler callbackHandler,
	  Map sharedState, Map options)
   {
	  this.subject = subject;
	  this.callbackHandler = callbackHandler;
	  this.sharedState = sharedState;
	  this.options = options;
	  log = Logger.getLogger(getClass());
	  log.info("initialize");
	 /* Check for password sharing options. Any non-null value for
		 password_stacking sets useFirstPass as this module has no way to
		 validate any shared password.
	  */
	  String passwordStacking = (String) options.get("password-stacking");
	  if( passwordStacking != null && passwordStacking.equalsIgnoreCase("useFirstPass") )
		 useFirstPass = true;

	  // Check for a custom Principal implementation
	  principalClassName = (String) options.get("principalClass");

	  // Check for unauthenticatedIdentity option.
	  String name = (String) options.get("unauthenticatedIdentity");
	  if( name != null )
	  {
		 try
		 {
			unauthenticatedIdentity = createIdentity(name);
			log.info("Saw unauthenticatedIdentity="+name);
		 }
		 catch(Exception e)
		 {
			log.warn("Failed to create custom unauthenticatedIdentity", e);
		 }
	  }
   }

   /** Looks for javax.security.auth.login.name and javax.security.auth.login.password
	values in the sharedState map if the useFirstPass option was true and returns
	true if they exist. If they do not or are null this method returns false.

	Note that subclasses that override the login method must set the loginOk
	ivar to true if the login succeeds in order for the commit phase to
	populate the Subject. This implementation sets loginOk to true if the
	login() method returns true, otherwise, it sets loginOk to false.
	*/
   public boolean login() throws LoginException
   {
	  log.info("login");
	  loginOk = false;
	  // If useFirstPass is true, look for the shared password
	  if( useFirstPass == true )
	  {
		 try
		 {
			Object identity = sharedState.get("javax.security.auth.login.name");
			Object credential = sharedState.get("javax.security.auth.login.password");
			if( identity != null && credential != null )
			{
			   loginOk = true;
			   return true;
			}
			// Else, fall through and perform the login
		 }
		 catch(Exception e)
		 {   // Dump the exception and continue
			log.error("login failed", e);
		 }
	  }
	  return false;
   }

   /** Method to commit the authentication process (phase 2). If the login
	method completed successfully as indicated by loginOk == true, this
	method adds the getIdentity() value to the subject getPrincipals() Set.
	It also adds the members of each Group returned by getRoleSets()
	to the subject getPrincipals() Set.
    
	@see javax.security.auth.Subject;
	@see java.security.acl.Group;
	@return true always.
	*/
   public boolean commit() throws LoginException
   {
	  log.info("commit, loginOk="+loginOk);
	  if( loginOk == false )
		 return false;

	  Set principals = subject.getPrincipals();
	  Principal identity = getIdentity();
	  principals.add(identity);
	  Group[] roleSets = getRoleSets();
	  for(int g = 0; g < roleSets.length; g ++)
	  {
		 Group group = roleSets[g];
		 String name = group.getName();
		 Group subjectGroup = createGroup(name, principals);
		 if( subjectGroup instanceof NestableGroup )
		 {
			/* A NestableGroup only allows Groups to be added to it so we
			need to add a SimpleGroup to subjectRoles to contain the roles
			*/
			SimpleGroup tmp = new SimpleGroup("Roles");
			subjectGroup.addMember(tmp);
			subjectGroup = tmp;
		 }
		 // Copy the group members to the Subject group
		 Enumeration members = group.members();
		 while( members.hasMoreElements() )
		 {
			Principal role = (Principal) members.nextElement();
			subjectGroup.addMember(role);
		 }
	  }
	  return true;
   }

   /** Method to abort the authentication process (phase 2).
	@return true alaways
	*/
   public boolean abort() throws LoginException
   {
	  log.info("abort");
	  return true;
   }
   
   /** Remove the user identity and roles added to the Subject during commit.
	@return true always.
	*/
   public boolean logout() throws LoginException
   {
	  log.info("logout");
	  // Remove the user identity
	  Principal identity = getIdentity();
	  Set principals = subject.getPrincipals();
	  principals.remove(identity);
	  // Remove any added Groups...
	  return true;
   }
   //--- End LoginModule interface methods
   
   // --- Protected methods
   
   /** Overriden by subclasses to return the Principal that corresponds to
	the user primary identity.
	*/
   protected Principal getIdentity()
   {
   		return null;
   }
   /** Overriden by subclasses to return the Groups that correspond to the
	to the role sets assigned to the user. Subclasses should create at
	least a Group named "Roles" that contains the roles assigned to the user.
	A second common group is "CallerPrincipal" that provides the application
	identity of the user rather than the security domain identity.
	@return Group[] containing the sets of roles
	*/
   protected Group[] getRoleSets() throws LoginException
   {
   		return null;
   }
   
   protected boolean getUseFirstPass()
   {
	  return useFirstPass;
   }
   protected Principal getUnauthenticatedIdentity()
   {
	  return unauthenticatedIdentity;
   }

   /** Find or create a Group with the given name. Subclasses should use this
	method to locate the 'Roles' group or create additional types of groups.
	@return A named Group from the principals set.
	*/
   protected Group createGroup(String name, Set principals)
   {
	  Group roles = null;
	  Iterator iter = principals.iterator();
	  while( iter.hasNext() )
	  {
		 Object next = iter.next();
		 if( (next instanceof Group) == false )
			continue;
		 Group grp = (Group) next;
		 if( grp.getName().equals(name) )
		 {
			roles = grp;
			break;
		 }
	  }
	  // If we did not find a group create one
	  if( roles == null )
	  {
		 roles = new SimpleGroup(name);
		 principals.add(roles);
	  }
	  return roles;
   }

   /** Utility method to create a Principal for the given username. This
	* creates an instance of the principalClassName type if this option was
	* specified using the class constructor matching: ctor(String). If
	* principalClassName was not specified, a SimplePrincipal is created.
	*
	* @param username the name of the principal
	* @return the principal instance
	* @throws java.lang.Exception thrown if the custom principal type cannot be created.
	*/ 
   protected Principal createIdentity(String username)
	  throws Exception
   {
	  Principal p = null;
	  if( principalClassName == null )
	  {
		 p = new SimplePrincipal(username);
	  }
	  else
	  {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			Class clazz = loader.loadClass(principalClassName);
			Class[] ctorSig = {String.class};
			Constructor ctor = clazz.getConstructor(ctorSig);
			Object[] ctorArgs = {username};
			p = (Principal) ctor.newInstance(ctorArgs);
	  }
	  return p;
   }
}