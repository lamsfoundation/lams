/*
 * $HeadURL$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.http.impl.cookie;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.client.utils.Punycode;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;

/**
 * Wraps a CookieAttributeHandler and leverages its match method
 * to never match a suffix from a black list. May be used to provide
 * additional security for cross-site attack types by preventing
 * cookies from apparent domains that are not publicly available.
 * An uptodate list of suffixes can be obtained from
 * <a href="http://publicsuffix.org/">publicsuffix.org</a>
 *
 * @since 4.0
 */
public class PublicSuffixFilter implements CookieAttributeHandler {
    private final CookieAttributeHandler wrapped;
    private Set<String> exceptions;
    private Set<String> suffixes;

    public PublicSuffixFilter(CookieAttributeHandler wrapped) {
        this.wrapped = wrapped;
    }

    /**
     * Sets the suffix blacklist patterns.
     * A pattern can be "com", "*.jp"
     * TODO add support for patterns like "lib.*.us"
     * @param suffixes
     */
    public void setPublicSuffixes(Collection<String> suffixes) {
        this.suffixes = new HashSet<String>(suffixes);
    }

    /**
     * Sets the exceptions from the blacklist. Exceptions can not be patterns.
     * TODO add support for patterns
     * @param exceptions
     */
    public void setExceptions(Collection<String> exceptions) {
        this.exceptions = new HashSet<String>(exceptions);
    }

    /**
     * Never matches if the cookie's domain is from the blacklist.
     */
    public boolean match(Cookie cookie, CookieOrigin origin) {
        if (isForPublicSuffix(cookie)) return false;
        return wrapped.match(cookie, origin);
    }

    public void parse(SetCookie cookie, String value) throws MalformedCookieException {
        wrapped.parse(cookie, value);
    }

    public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
        wrapped.validate(cookie, origin);
    }

    private boolean isForPublicSuffix(Cookie cookie) {
        String domain = cookie.getDomain();
        if (domain.startsWith(".")) domain = domain.substring(1);
        domain = Punycode.toUnicode(domain);

        // An exception rule takes priority over any other matching rule.
        if (this.exceptions != null) {
            if (this.exceptions.contains(domain)) return false;
        }


        if (this.suffixes == null) return false;

        do {
            if (this.suffixes.contains(domain)) return true;
            // patterns
            if (domain.startsWith("*.")) domain = domain.substring(2);
            int nextdot = domain.indexOf('.');
            if (nextdot == -1) break;
            domain = "*" + domain.substring(nextdot);
        } while (domain.length() > 0);

        return false;
    }
}
