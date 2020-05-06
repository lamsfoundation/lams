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
package org.jboss.security;


//$Id: SecurityConstants.java 58038 2006-11-03 04:39:59Z anil.saldhana@jboss.com $

/**
 *  Defines Constants for usage in the Security Layer
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @since  Dec 30, 2005 
 *  @version $Revision: 58038 $
 */
public interface SecurityConstants
{
    /**
     * Default Application Policy
     */
    String DEFAULT_APPLICATION_POLICY = "other";

    /**
     * Default JAAS based Security Domain Context
     */
    String JAAS_CONTEXT_ROOT = "java:jboss/jaas/";

    /**
     * Default JASPI based Security Domain Context
     */
    String JASPI_CONTEXT_ROOT = "java:jboss/jbsx/";

    /**
     * The String option name used to pass in the security-domain
     * name the LoginModule was configured in.
     */
    String SECURITY_DOMAIN_OPTION = "jboss.security.security_domain";

    /**
     * System Property that disables the addition of security domain
     * in the module options passed to login module
     */
    String DISABLE_SECDOMAIN_OPTION = "jboss.security.disable.secdomain.option";

    /**
     * Default Authorization Manager context
     */
    String AUTHORIZATION_PATH = "java:/authorizationMgr";

    /**
     * Default ServerAuthModule that delegates to a Login Module Stack
     */
    String JASPI_DELEGATING_MODULE = "org.jboss.security.auth.container.modules.DelegatingServerAuthModule";

    /**
     * Default JASPI based secutity manager
     */
    String JASPI_AUTHENTICATION_MANAGER = "org.jboss.security.plugins.JASPISecurityManager";

    /**
     * Default AuthorizationManager implementation, the AuthorizationManager service uses
     *
     */
    String DEFAULT_AUTHORIZATION_CLASS = "org.jboss.security.plugins.JBossAuthorizationManager";

    /**
     * Message Layers
     */
    String SERVLET_LAYER = "HttpServlet";

    String CONTEXT_ID = "jboss.contextid";

    /**
     * Application Policy driving the web authorization layer
     */
    String DEFAULT_WEB_APPLICATION_POLICY = "jboss-web-policy";

    /**
     * Application Policy driving the ejb authorization layer
     */
    String DEFAULT_EJB_APPLICATION_POLICY = "jboss-ejb-policy";


    /** Policy Context Constants **/
    String SUBJECT_CONTEXT_KEY = "javax.security.auth.Subject.container";
    String WEB_REQUEST_KEY = "javax.servlet.http.HttpServletRequest";
    String CALLBACK_HANDLER_KEY = "org.jboss.security.auth.spi.CallbackHandler";

    /**
     * Identifier that specifies the last RAI set
     */
    String RUNAS_IDENTITY_IDENTIFIER = "RunAsIdentity";

    /**
     * Identifier that specifies the caller run-as identifier for usage
     * in the PolicyContext Subject context handler. This is one level
     * prior to the current RAI
     */
    String CALLER_RAI_IDENTIFIER = "CallerRunAsIdentity";

    String ROLES_IDENTIFIER = "Roles";
    String PRINCIPAL_IDENTIFIER = "Principal";
    String PRINCIPALS_SET_IDENTIFIER = "PrincipalsSet";
    String DEPLOYMENT_PRINCIPAL_ROLES_MAP = "deploymentPrincipalRolesMap";

    String SECURITY_CONTEXT = "SecurityContext";

    String CREDENTIAL = "Credential";
    String SUBJECT = "Subject";

    /**
     * Identity Trust Constants
     */
    String JAVAEE = "JavaEE";

    String CALLER_PRINCIPAL_GROUP = "CallerPrincipal";
}
