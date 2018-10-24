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


import org.jboss.logging.Logger;
import org.jboss.security.*;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.lang.reflect.Constructor;
import java.security.Principal;
import java.security.acl.Group;
import java.util.*;

/**
 * This class implements the common functionality required for a JAAS
 * server side LoginModule and implements the JBossSX standard Subject usage
 * pattern of storing identities and roles. Subclass this module to create your
 * own custom LoginModule and override the login(), getRoleSets() and getIdentity()
 * methods.
 * <p>
 * You may also wish to override
 * <pre>
 *    public void initialize(Subject subject, CallbackHandler callbackHandler, Map sharedState, Map options)
 * </pre>
 * In which case the first line of your initialize() method should be:
 * <pre>
 *    super.initialize(subject, callbackHandler, sharedState, options);
 * </pre>
 * <p>
 * You may also wish to override
 * <pre>
 *    public boolean login() throws LoginException
 * </pre>
 * In which case the last line of your login() method should be
 * <pre>
 *    return super.login();
 * </pre>
 *
 *@author <a href="edward.kenworthy@crispgroup.co.uk">Edward Kenworthy</a>, 12th Dec 2000
 *@author Scott.Stark@jboss.org
 *@version $Revision$
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractServerLoginModule implements LoginModule
{
   // Parameter checking
   //   All valid options for a module should be listed similarly to this
   //   Each subclass should call addValidOptions(final String[] moduleValidOptions) from within
   //   their initialize method BEFORE calling super.initialize()
   private static final String PASSWORD_STACKING = "password-stacking";
   private static final String USE_FIRST_PASSWORD = "useFirstPass";
   private static final String PRINCIPAL_CLASS = "principalClass";
   private static final String PRINCIPAL_CLASS_MODULE = "principalClassModule";
   private static final String UNAUTHENTICATED_IDENTITY = "unauthenticatedIdentity";
   private static final String MODULE = "module";
  
   private static final String[] ALL_VALID_OPTIONS =
   {
	   PASSWORD_STACKING,PRINCIPAL_CLASS,PRINCIPAL_CLASS_MODULE,UNAUTHENTICATED_IDENTITY,MODULE,
	   SecurityConstants.SECURITY_DOMAIN_OPTION
   };
   
   private HashSet<String> validOptions;
   
   protected Subject subject;
   protected CallbackHandler callbackHandler; 
   protected Map sharedState; 
   protected Map options;

   /** Flag indicating if the shared credential should be used */
   protected boolean useFirstPass;
   /** Flag indicating if the login phase succeeded. Subclasses that override
    the login method must set this to true on successful completion of login
    */
   protected boolean loginOk;
   /** An optional custom Principal class implementation */
   protected String principalClassName;
   protected String principalClassModuleName;
   /** the principal to use when a null username and password are seen */
   protected Principal unauthenticatedIdentity;
   /** jboss module name to load Callback class etc */
   protected String jbossModuleName;

   protected Logger log = Logger.getLogger(AbstractServerLoginModule.class);

//--- Begin LoginModule interface methods
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
      Map<String,?> sharedState, Map<String,?> options)
   {
      this.subject = subject;
      this.callbackHandler = callbackHandler;
      this.sharedState = sharedState;
      this.options = options;

      PicketBoxLogger.LOGGER.traceBeginInitialize();

      // if the set is null, the subclass did not implement checking for valid options, so skip altogether to avoid false alarms
      if (validOptions != null)
      {
    	  // otherwise, add our own and check all options against the "valid" list
         addValidOptions(ALL_VALID_OPTIONS);
         checkOptions();
      }      
      /* Check for password sharing options. Any non-null value for
         password_stacking sets useFirstPass as this module has no way to
         validate any shared password.
      */
      String passwordStacking = (String) options.get(PASSWORD_STACKING);
      if( passwordStacking != null && passwordStacking.equalsIgnoreCase(USE_FIRST_PASSWORD) )
         useFirstPass = true;

      // Check for a custom Principal implementation
      principalClassName = (String) options.get(PRINCIPAL_CLASS);
      principalClassModuleName = (String) options.get(PRINCIPAL_CLASS_MODULE);

      // Check for unauthenticatedIdentity option.
      String name = (String) options.get(UNAUTHENTICATED_IDENTITY);
      if( name != null )
      {
         try
         {
            unauthenticatedIdentity = createIdentity(name);
            PicketBoxLogger.LOGGER.traceUnauthenticatedIdentity(name);
         }
         catch(Exception e)
         {
            PicketBoxLogger.LOGGER.warnFailureToCreateUnauthIdentity(e);
         }
      }

      jbossModuleName = (String)options.get(MODULE);
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
      PicketBoxLogger.LOGGER.traceBeginLogin();
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
            PicketBoxLogger.LOGGER.debugFailedLogin(e);
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
      PicketBoxLogger.LOGGER.traceBeginCommit(loginOk);
      if( loginOk == false )
         return false;

      Set<Principal> principals = subject.getPrincipals();
      Principal identity = getIdentity();
      principals.add(identity);
      // add role groups returned by getRoleSets.
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
         Enumeration<? extends Principal> members = group.members();
         while( members.hasMoreElements() )
         {
            Principal role = (Principal) members.nextElement();
            subjectGroup.addMember(role);
         }
      }
       // add the CallerPrincipal group if none has been added in getRoleSets
       Group callerGroup = getCallerPrincipalGroup(principals);
       if (callerGroup == null)
       {
           callerGroup = new SimpleGroup(SecurityConstants.CALLER_PRINCIPAL_GROUP);
           callerGroup.addMember(identity);
           principals.add(callerGroup);
       }
       return true;
   }

   /** Method to abort the authentication process (phase 2).
    @return true always
    */
   public boolean abort() throws LoginException
   {
      PicketBoxLogger.LOGGER.traceBeginAbort(loginOk);
      return loginOk;
   }
   
   /** Remove the user identity and roles added to the Subject during commit.
    @return true always.
    */
   public boolean logout() throws LoginException
   {
      PicketBoxLogger.LOGGER.traceBeginLogout();
      // Remove the user identity
      Principal identity = getIdentity();
      Set<Principal> principals = subject.getPrincipals();
      principals.remove(identity);
      Group callerGroup = getCallerPrincipalGroup(principals);
      if (callerGroup != null)
         principals.remove(callerGroup);
      // Remove any added Groups...
      return true;
   }
   //--- End LoginModule interface methods
   
   // --- Protected methods
   
   /** Overriden by subclasses to return the Principal that corresponds to
    the user primary identity.
    */
   abstract protected Principal getIdentity();
   /** Overriden by subclasses to return the Groups that correspond to the
    to the role sets assigned to the user. Subclasses should create at
    least a Group named "Roles" that contains the roles assigned to the user.
    A second common group is "CallerPrincipal" that provides the application
    identity of the user rather than the security domain identity.
    @return Group[] containing the sets of roles
    */
   abstract protected Group[] getRoleSets() throws LoginException;
   
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
   protected Group createGroup(String name, Set<Principal> principals)
   {
      Group roles = null;
      Iterator<Principal> iter = principals.iterator();
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
   @SuppressWarnings("unchecked")
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
            Class clazz = SecurityActions.loadClass(principalClassName, principalClassModuleName);
            Class[] ctorSig = {String.class};
            Constructor ctor = clazz.getConstructor(ctorSig);
            Object[] ctorArgs = {username};
            p = (Principal) ctor.newInstance(ctorArgs);
      }
      return p;
   }
   
   protected Group getCallerPrincipalGroup(Set<Principal> principals)
   {
      Group callerGroup = null;
      for (Principal principal : principals)
      {
         if (principal instanceof Group)
         {
            Group group = Group.class.cast(principal);
            if (group.getName().equals(SecurityConstants.CALLER_PRINCIPAL_GROUP))
            {
               callerGroup = group;
               break;
            }
         }
      }
      return callerGroup;
   }

   /**
    * Each subclass should call this from within their initialize method BEFORE calling super.initialize()
    * The base class will then check the options
    * 
    * @param moduleValidOptions : the list of options the subclass supports
    */
   protected void addValidOptions(final String[] moduleValidOptions)
   {
      if (validOptions == null)
      {
         validOptions = new HashSet<String>(moduleValidOptions.length);
      }
     validOptions.addAll(Arrays.asList(moduleValidOptions));
   }
   
   /**
    * checks the collected valid options against the options passed in
    * Override when there are special needs like for the SimpleUsersLoginModule
    */
   protected void checkOptions()
   {
      for (Object key : options.keySet())
      {
         if (!validOptions.contains(key))
         {
            PicketBoxLogger.LOGGER.warnInvalidModuleOption((String)key);
         }
      }
   }
}
