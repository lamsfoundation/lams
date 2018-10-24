/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
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

package org.jboss.security.config;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum for the elements of a security domain configuration
 *
 * @author <a href="mailto:mmoyses@redhat.com">Marcus Moyses</a>
 */
public enum Element {
    // must be first
    UNKNOWN(null),
    
    POLICY("policy"),

    APPLICATION_POLICY("application-policy"),
    
    SECURITY_DOMAIN("security-domain"),

    AUTHENTICATION("authentication"),
    
    AUTHENTICATION_JASPI("authentication-jaspi"),

    AUTHORIZATION("authorization"),
    
    ACL("acl"),
    
    ROLE_MAPPING("rolemapping"),
    
    MAPPING("mapping"),
    
    AUDIT("audit"),
    
    IDENTITY_TRUST("identity-trust"),
    
    ACL_MODULE("acl-module"),

    LOGIN_MODULE("login-module"),
    
    LOGIN_MODULE_STACK("login-module-stack"),
    
    AUTH_MODULE("auth-module"),
    
    PROVIDER_MODULE("provider-module"),
    
    POLICY_MODULE("policy-module"),
    
    TRUST_MODULE("trust-module"),
    
    MAPPING_MODULE("mapping-module"),

    MODULE_OPTION("module-option");

    private final String name;

    Element(final String name) {
        this.name = name;
    }

    /**
     * Get the local name of this element.
     *
     * @return the local name
     */
    public String getLocalName() {
        return name;
    }

    private static final Map<String, Element> MAP;

    static {
        final Map<String, Element> map = new HashMap<String, Element>();
        for (Element element : values()) {
            final String name = element.getLocalName();
            if (name != null)
                map.put(name, element);
        }
        MAP = map;
    }

    public static Element forName(String localName) {
        final Element element = MAP.get(localName);
        return element == null ? UNKNOWN : element;
    }

}
