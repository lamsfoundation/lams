/*
 * ====================================================================
 *
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.http.client.params;

import org.apache.http.annotation.Immutable;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/**
 * An adaptor for manipulating HTTP client parameters in {@link HttpParams}.
 *
 * @since 4.0
 */
@Immutable
public class HttpClientParams {

    private HttpClientParams() {
        super();
    }

    public static boolean isRedirecting(final HttpParams params) {
        if (params == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        }
        return params.getBooleanParameter
            (ClientPNames.HANDLE_REDIRECTS, true);
    }

    public static void setRedirecting(final HttpParams params, boolean value) {
        if (params == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        }
        params.setBooleanParameter
            (ClientPNames.HANDLE_REDIRECTS, value);
    }

    public static boolean isAuthenticating(final HttpParams params) {
        if (params == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        }
        return params.getBooleanParameter
            (ClientPNames.HANDLE_AUTHENTICATION, true);
    }

    public static void setAuthenticating(final HttpParams params, boolean value) {
        if (params == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        }
        params.setBooleanParameter
            (ClientPNames.HANDLE_AUTHENTICATION, value);
    }

    public static String getCookiePolicy(final HttpParams params) {
        if (params == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        }
        String cookiePolicy = (String)
            params.getParameter(ClientPNames.COOKIE_POLICY);
        if (cookiePolicy == null) {
            return CookiePolicy.BEST_MATCH;
        }
        return cookiePolicy;
    }

    public static void setCookiePolicy(final HttpParams params, final String cookiePolicy) {
        if (params == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        }
        params.setParameter(ClientPNames.COOKIE_POLICY, cookiePolicy);
    }

    /**
     * Set the parameter {@code ClientPNames.CONN_MANAGER_TIMEOUT}.
     *
     * @since 4.2
     */
    public static void setConnectionManagerTimeout(final HttpParams params, long timeout) {
        if (params == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        }
        params.setLongParameter(ClientPNames.CONN_MANAGER_TIMEOUT, timeout);
    }

    /**
     * Get the connectiion manager timeout value.
     * This is defined by the parameter {@code ClientPNames.CONN_MANAGER_TIMEOUT}.
     * Failing that it uses the parameter {@code CoreConnectionPNames.CONNECTION_TIMEOUT}
     * which defaults to 0 if not defined.
     *
     * @since 4.2
     * @return the timeout value
     */
    public static long getConnectionManagerTimeout(final HttpParams params) {
        if (params == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        }
        Long timeout = (Long) params.getParameter(ClientPNames.CONN_MANAGER_TIMEOUT);
        if (timeout != null) {
            return timeout.longValue();
        }
        return HttpConnectionParams.getConnectionTimeout(params);
    }

}
