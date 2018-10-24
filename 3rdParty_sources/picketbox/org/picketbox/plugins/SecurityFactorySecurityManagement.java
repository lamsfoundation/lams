/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2015, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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

package org.picketbox.plugins;

import org.jboss.security.AuthenticationManager;
import org.jboss.security.AuthorizationManager;
import org.jboss.security.ISecurityManagement;
import org.jboss.security.JSSESecurityDomain;
import org.jboss.security.audit.AuditManager;
import org.jboss.security.identitytrust.IdentityTrustManager;
import org.jboss.security.mapping.MappingManager;
import org.picketbox.factories.SecurityFactory;

/**
 * A {@link ISecurityManagement} implementation that delegates to the instance registered with the {@link SecurityFactory}.
 *
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 */
public class SecurityFactorySecurityManagement implements ISecurityManagement {

    private static final long serialVersionUID = 4533649455497748886L;

    private transient ISecurityManagement delegate;

    private ISecurityManagement getDelegate() {
        return delegate != null ? delegate : (delegate = SecurityFactory.getSecurityManagement());
    }

    @Override
    public AuthenticationManager getAuthenticationManager(String securityDomain) {
        return getDelegate().getAuthenticationManager(securityDomain);
    }

    @Override
    public AuthorizationManager getAuthorizationManager(String securityDomain) {
        return getDelegate().getAuthorizationManager(securityDomain);
    }

    @Override
    public MappingManager getMappingManager(String securityDomain) {
        return getDelegate().getMappingManager(securityDomain);
    }

    @Override
    public AuditManager getAuditManager(String securityDomain) {
        return getDelegate().getAuditManager(securityDomain);
    }

    @Override
    public IdentityTrustManager getIdentityTrustManager(String securityDomain) {
        return getDelegate().getIdentityTrustManager(securityDomain);
    }

    @Override
    public JSSESecurityDomain getJSSE(String securityDomain) {
        return getDelegate().getJSSE(securityDomain);
    }

}
