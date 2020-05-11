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
package org.jboss.security.plugins.auth;

import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.jboss.security.AuthenticationManager;
import org.jboss.security.AuthorizationManager;
import org.jboss.security.PicketBoxLogger;
import org.jboss.security.PicketBoxMessages;
import org.jboss.security.RealmMapping;
import org.jboss.security.SecurityConstants;
import org.jboss.security.SecurityContext;
import org.jboss.security.SecurityContextAssociation;
import org.jboss.security.SecurityUtil;
import org.jboss.security.SubjectSecurityManager;
import org.jboss.security.auth.callback.JBossCallbackHandler;
import org.jboss.security.auth.login.BaseAuthenticationInfo;
import org.jboss.security.config.ApplicationPolicy;
import org.jboss.security.config.SecurityConfiguration;
import org.jboss.security.plugins.ClassLoaderLocator;
import org.jboss.security.plugins.ClassLoaderLocatorFactory;

/** The JaasSecurityManager is responsible both for authenticating credentials
 associated with principals and for role mapping. This implementation relies
 on the JAAS LoginContext/LoginModules associated with the security
 domain name associated with the class for authentication,
 and the context JAAS Subject object for role mapping.
 
 @see #isValid(Principal, Object, Subject)
 @see #getPrincipal(Principal)
 @see #doesUserHaveRole(Principal, Set)
 
 @author <a href="on@ibis.odessa.ua">Oleg Nitz</a>
 @author Scott.Stark@jboss.org
 @author Anil.Saldhana@jboss.org
 @version $Revision: 62860 $
*/
public class JaasSecurityManagerBase 
   implements SubjectSecurityManager, RealmMapping
{
   /** The name of the domain this instance is securing. It is used as
    the appName into the SecurityPolicy.
    */
   private String securityDomain;
   /** A cache of DomainInfo objects keyd by Principal. This is now
    always set externally by our security manager service.
    */
   /** The JAAS callback handler to use in defaultLogin */
   private CallbackHandler handler;
   /** The setSecurityInfo(Principal, Object) method of the handler obj */
   private transient Method setSecurityInfo;
   /** The flag to indicate that the Subject sets need to be deep copied*/
   private boolean deepCopySubjectOption = false; 
   
   private AuthorizationManager authorizationManager;

   /** Creates a default JaasSecurityManager for with a securityDomain
    name of 'other'.
    */
   public JaasSecurityManagerBase()
   {
      this(SecurityConstants.DEFAULT_APPLICATION_POLICY, new JBossCallbackHandler());
   }
   /** Creates a JaasSecurityManager for with a securityDomain
    name of that given by the 'securityDomain' argument.
    @param securityDomain the name of the security domain
    @param handler the JAAS callback handler instance to use
    @exception UndeclaredThrowableException thrown if handler does not
      implement a setSecurityInfo(Princpal, Object) method
    */
   public JaasSecurityManagerBase(String securityDomain, CallbackHandler handler)
   {
      this.securityDomain = SecurityUtil.unprefixSecurityDomain( securityDomain );
      this.handler = handler;
      String categoryName = getClass().getName()+'.'+securityDomain;

      // Get the setSecurityInfo(Principal principal, Object credential) method
      Class<?>[] sig = {Principal.class, Object.class};
      try
      {
         setSecurityInfo = handler.getClass().getMethod("setSecurityInfo", sig);
      }
      catch (Exception e)
      {
         throw new UndeclaredThrowableException(e, PicketBoxMessages.MESSAGES.unableToFindSetSecurityInfoMessage());
      } 
   }

   /**
    * Flag to specify if deep copy of subject sets needs to be 
    * enabled
    * 
    * @param flag
    */
   public void setDeepCopySubjectOption(Boolean flag)
   {
      this.deepCopySubjectOption = flag ;
   } 
   
   /**
    * Set an AuthorizationManager
    * @param authorizationManager
    */
   public void setAuthorizationManager(AuthorizationManager authorizationManager)
   {
      this.authorizationManager = authorizationManager;
   }
   
   /** Get the name of the security domain associated with this security mgr.
    @return Name of the security manager security domain.
    */
   public String getSecurityDomain()
   {
      return securityDomain;
   }

   /** Get the currently authenticated Subject. This is a thread local
    property shared across all JaasSecurityManager instances.
    @return The Subject authenticated in the current thread if one
    exists, null otherwise.
    */
   public Subject getActiveSubject()
   {
      /* This does not use SubjectActions.getActiveSubject since the caller
         must have the correct permissions to access the
         SecurityAssociation.getSubject method.
      */
      //return SecurityAssociation.getSubject();
      Subject subj = null;
      SecurityContext sc = SecurityContextAssociation.getSecurityContext();
      if(sc != null)
      {
         subj = sc.getUtil().getSubject();
      }
      return subj;
   }

   /** Validate that the given credential is correct for principal. This
    returns the value from invoking isValid(principal, credential, null).
    @param principal - the security domain principal attempting access
    @param credential - the proof of identity offered by the principal
    @return true if the principal was authenticated, false otherwise.
    */
   public boolean isValid(Principal principal, Object credential)
   {
      return isValid(principal, credential, null);
   }

   /** Validate that the given credential is correct for principal. This first
    will check the current CachePolicy object if one exists to see if the
    user's cached credentials match the given credential. If there is no
    credential cache or the cache information is invalid or does not match,
    the user is authenticated against the JAAS login modules configured for
    the security domain.
    @param principal - the security domain principal attempting access
    @param credential  the proof of identity offered by the principal
    @param activeSubject - if not null, a Subject that will be populated with
      the state of the authenticated Subject.
    @return true if the principal was authenticated, false otherwise.
    */
   public boolean isValid(Principal principal, Object credential,
      Subject activeSubject)
   {
      PicketBoxLogger.LOGGER.traceBeginIsValid(principal, null);

      boolean isValid = false;
      if( isValid == false )
         isValid = authenticate(principal, credential, activeSubject);

      PicketBoxLogger.LOGGER.traceEndIsValid(isValid);
      return isValid;
   } 

   
   /** Map the argument principal from the deployment environment principal
    to the developer environment. This is called by the EJB context
    getCallerPrincipal() to return the Principal as described by
    the EJB developer domain.
    @return a Principal object that is valid in the deployment environment
    if one exists. If no Subject exists or the Subject has no principals
    then the argument principal is returned.
    */
   public Principal getPrincipal(Principal principal)
   {
      return principal;
   }

   /** Does the current Subject have a role(a Principal) that equates to one
    of the role names. This method obtains the Group named 'Roles' from
    the principal set of the currently authenticated Subject as determined
    by the SecurityAssociation.getSubject() method and then creates a
    SimplePrincipal for each name in roleNames. If the role is a member of the
    Roles group, then the user has the role. This requires that the caller
    establish the correct SecurityAssociation subject prior to calling this
    method. In the past this was done as a side-effect of an isValid() call,
    but this is no longer the case.

    @param principal - ignored. The current authenticated Subject determines
    the active user and assigned user roles.
    @param rolePrincipals - a Set of Principals for the roles to check.
    
    @see java.security.acl.Group;
    @see Subject#getPrincipals()
    */
   public boolean doesUserHaveRole(Principal principal, Set<Principal> rolePrincipals)
   { 
      if(this.authorizationManager == null)
      {
         this.authorizationManager = SecurityUtil.getAuthorizationManager(securityDomain, 
               SecurityConstants.JAAS_CONTEXT_ROOT); 
      }
      if(this.authorizationManager == null)
      {
         PicketBoxLogger.LOGGER.debugNullAuthorizationManager(securityDomain);
         return false;
      }
      return authorizationManager.doesUserHaveRole(principal, rolePrincipals); 
   } 

   /** Return the set of domain roles the current active Subject 'Roles' group
      found in the subject Principals set.

    @param principal - ignored. The current authenticated Subject determines
    the active user and assigned user roles.
    @return The Set<Principal> for the application domain roles that the
    principal has been assigned.
   */
   public Set<Principal> getUserRoles(Principal principal)
   {
      if(this.authorizationManager == null)
      {
         this.authorizationManager = SecurityUtil.getAuthorizationManager(securityDomain, 
               SecurityConstants.JAAS_CONTEXT_ROOT); 
      }
      if(this.authorizationManager == null)
      {
          PicketBoxLogger.LOGGER.debugNullAuthorizationManager(securityDomain);
          return null;
      }
      return authorizationManager.getUserRoles(principal);
   } 
   
   /**
    * @see AuthenticationManager#getTargetPrincipal(Principal,Map)
    */
   public Principal getTargetPrincipal(Principal anotherDomainPrincipal, 
         Map<String,Object> contextMap)
   {
      throw new UnsupportedOperationException();
   }

   /** Currently this simply calls defaultLogin() to do a JAAS login using the
    security domain name as the login module configuration name.
    
    * @param principal - the user id to authenticate
    * @param credential - an opaque credential.
    * @return false on failure, true on success.
    */
   private boolean authenticate(Principal principal, Object credential,
      Subject theSubject)
   {
	   ApplicationPolicy theAppPolicy = SecurityConfiguration.getApplicationPolicy(securityDomain);
	   if(theAppPolicy != null)
	   {
		   BaseAuthenticationInfo authInfo = theAppPolicy.getAuthenticationInfo();
		   List<String> jbossModuleNames = authInfo.getJBossModuleNames();
		   if(!jbossModuleNames.isEmpty())
		   {
			   ClassLoader currentTccl = SubjectActions.getContextClassLoader();
			   ClassLoaderLocator theCLL = ClassLoaderLocatorFactory.get();
			   if(theCLL != null)
			   {
				   ClassLoader newTCCL = theCLL.get(jbossModuleNames);
				   if(newTCCL != null)
				   {
					   try
					   {
						   SubjectActions.setContextClassLoader(newTCCL);
						   return proceedWithJaasLogin(principal, credential, theSubject);
					   }
					   finally
					   {
						   SubjectActions.setContextClassLoader(currentTccl);
					   }
				   }
			   }
		   }
	   }
	   return proceedWithJaasLogin(principal, credential, theSubject);
   }
   
   private boolean proceedWithJaasLogin(Principal principal, Object credential, Subject theSubject)
   {
		Subject subject = null;
		boolean authenticated = false;
		LoginException authException = null;
		try {

			// Validate the principal using the login configuration for this
			// domain
			LoginContext lc = defaultLogin(principal, credential);
			subject = lc.getSubject();

			// Set the current subject if login was successful
			if (subject != null) {
				// Copy the current subject into theSubject
				if (theSubject != null) {
					SubjectActions.copySubject(subject, theSubject, false,
							this.deepCopySubjectOption);
				} else {
					theSubject = subject;
				}

				authenticated = true;
			}
		} catch (LoginException e) {
			// Don't log anonymous user failures unless trace level logging is
			// on
			if (principal != null && principal.getName() != null)
                PicketBoxLogger.LOGGER.debugFailedLogin(e);
			authException = e;
		}
		// Set the security association thread context info exception
		SubjectActions.setContextInfo("org.jboss.security.exception",
				authException);

		return authenticated;
   }

   /** Pass the security info to the login modules configured for
    this security domain using our SecurityAssociationHandler.
    @return The authenticated Subject if successful.
    @exception LoginException throw if login fails for any reason.
    */
   private LoginContext defaultLogin(Principal principal, Object credential)
      throws LoginException
   {
      /* We use our internal CallbackHandler to provide the security info. A
      copy must be made to ensure there is a unique handler per active
      login since there can be multiple active logins.
      */
      Object[] securityInfo = {principal, credential};
      CallbackHandler theHandler = null;
      try
      {
         theHandler = (CallbackHandler) handler.getClass().newInstance();
         setSecurityInfo.invoke(theHandler, securityInfo);
      }
      catch (Throwable e)
      {
         LoginException le = new LoginException(PicketBoxMessages.MESSAGES.unableToFindSetSecurityInfoMessage());
         le.initCause(e);
         throw le;
      }
      Subject subject = new Subject();
      LoginContext lc = null;
      PicketBoxLogger.LOGGER.traceDefaultLoginPrincipal(principal);
      lc = SubjectActions.createLoginContext(securityDomain, subject, theHandler);
      lc.login();
      if (PicketBoxLogger.LOGGER.isDebugEnabled())
      {
         PicketBoxLogger.LOGGER.traceDefaultLoginSubject(lc.toString(), SubjectActions.toString(subject));
      }
      return lc;
   }

    /**
     * Performs the JAAS logout. The incoming {@code Subject} is used to create the {@code LoginContext}
     * and passed to the JAAS login modules so that proper cleanup can be performed by each module.
     *
     * @param principal the {@code Principal} being logged out.
     * @param subject the {@code Subject} associated with the principal being logged out.
     */
    public void logout(Principal principal, Subject subject) {

        if (subject == null)
            subject = new Subject();

        LoginContext context = null;

        // create the login context using the incoming principal and subject.
        Object[] securityInfo = {principal, null};
        CallbackHandler theHandler = null;
        try
        {
            theHandler = handler.getClass().newInstance();
            setSecurityInfo.invoke(theHandler, securityInfo);
            context = SubjectActions.createLoginContext(securityDomain, subject, theHandler);
        }
        catch (Throwable e)
        {
            LoginException le = new LoginException(PicketBoxMessages.MESSAGES.unableToInitializeLoginContext(e));
            le.initCause(e);
            SubjectActions.setContextInfo("org.jboss.security.exception", le);
            return;
        }

        // perform the JAAS logout.
        try {
            context.logout();
            if (PicketBoxLogger.LOGGER.isTraceEnabled())
            {
               PicketBoxLogger.LOGGER.traceLogoutSubject(context.toString(), SubjectActions.toString(subject));
            }
        }
        catch (LoginException le) {
            SubjectActions.setContextInfo("org.jboss.security.exception", le);
        }
    }
}