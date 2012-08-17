/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, JBoss Inc., and individual contributors as indicated
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


package org.jboss.web.rewrite;

import java.util.Calendar;

import org.apache.catalina.connector.Request;

import org.apache.naming.resources.CacheEntry;
import org.apache.naming.resources.ProxyDirContext;
import org.apache.tomcat.util.http.FastHttpDateFormat;

public class TomcatResolver extends Resolver {

    protected Request request = null;
    
    public TomcatResolver(Request request) {
        this.request = request;
    }
    
    /**
     * The following are not implemented:
     * - SERVER_ADMIN
     * - API_VERSION
     * - IS_SUBREQ
     */
    public String resolve(String key) {
        if (key.equals("HTTP_USER_AGENT")) {
            return request.getHeader("user-agent");
        } else if (key.equals("HTTP_REFERER")) {
            return request.getHeader("referer");
        } else if (key.equals("HTTP_COOKIE")) {
            return request.getHeader("cookie");
        } else if (key.equals("HTTP_FORWARDED")) {
            return request.getHeader("forwarded");
        } else if (key.equals("HTTP_HOST")) {
            String host = request.getHeader("host");
            int index = host.indexOf(':');
            if (index != -1)
                host = host.substring(0, index);
            return host;
        } else if (key.equals("HTTP_PROXY_CONNECTION")) {
            return request.getHeader("proxy-connection");
        } else if (key.equals("HTTP_ACCEPT")) {
            return request.getHeader("accept");
        } else if (key.equals("REMOTE_ADDR")) {
            return request.getRemoteAddr();
        } else if (key.equals("REMOTE_HOST")) {
            return request.getRemoteHost();
        } else if (key.equals("REMOTE_PORT")) {
            return String.valueOf(request.getRemotePort());
        } else if (key.equals("REMOTE_USER")) {
            return request.getRemoteUser();
        } else if (key.equals("REMOTE_IDENT")) {
            return request.getRemoteUser();
        } else if (key.equals("REQUEST_METHOD")) {
            return request.getMethod();
        } else if (key.equals("SCRIPT_FILENAME")) {
            return request.getRealPath(request.getServletPath()); //FIXME ?
        } else if (key.equals("REQUEST_PATH")) {
            return request.getRequestPathMB().toString();
        } else if (key.equals("CONTEXT_PATH")) {
            return request.getContextPath();
        } else if (key.equals("SERVLET_PATH")) {
            return emptyStringIfNull(request.getServletPath());
        } else if (key.equals("PATH_INFO")) {
            return emptyStringIfNull(request.getPathInfo());
        } else if (key.equals("QUERY_STRING")) {
            return emptyStringIfNull(request.getQueryString());
        } else if (key.equals("AUTH_TYPE")) {
            return request.getAuthType();
        } else if (key.equals("DOCUMENT_ROOT")) {
            return request.getRealPath("/");
        } else if (key.equals("SERVER_NAME")) {
            return request.getLocalName();
        } else if (key.equals("SERVER_ADDR")) {
            return request.getLocalAddr();
        } else if (key.equals("SERVER_PORT")) {
            return String.valueOf(request.getLocalPort());
        } else if (key.equals("SERVER_PROTOCOL")) {
            return request.getProtocol();
        } else if (key.equals("SERVER_SOFTWARE")) {
            return "tomcat";
        } else if (key.equals("THE_REQUEST")) {
            return request.getMethod() + " " + request.getRequestURI() 
            + " " + request.getProtocol();
        } else if (key.equals("REQUEST_URI")) {
            return request.getRequestURI();
        } else if (key.equals("REQUEST_FILENAME")) {
            return request.getPathTranslated();
        } else if (key.equals("HTTPS")) {
            return request.isSecure() ? "on" : "off";
        } else if (key.equals("TIME_YEAR")) {
            return String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        } else if (key.equals("TIME_MON")) {
            return String.valueOf(Calendar.getInstance().get(Calendar.MONTH));
        } else if (key.equals("TIME_DAY")) {
            return String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        } else if (key.equals("TIME_HOUR")) {
            return String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
        } else if (key.equals("TIME_MIN")) {
            return String.valueOf(Calendar.getInstance().get(Calendar.MINUTE));
        } else if (key.equals("TIME_SEC")) {
            return String.valueOf(Calendar.getInstance().get(Calendar.SECOND));
        } else if (key.equals("TIME_WDAY")) {
            return String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
        } else if (key.equals("TIME")) {
            return FastHttpDateFormat.getCurrentDate();
        }
        return null;
    }

    public String resolveEnv(String key) {
        return System.getProperty(key);
    }
    
    public String resolveSsl(String key) {
        // FIXME: Implement SSL environment variables
        return null;
    }

    public String resolveHttp(String key) {
        return request.getHeader(key);
    }
    
    public boolean resolveResource(int type, String name) {
        ProxyDirContext resources = (ProxyDirContext) request.getContext().getResources();
        CacheEntry cacheEntry = resources.lookupCache(name);
        if (!cacheEntry.exists) {
            return false;
        } else {
            switch (type) {
            case 0:
                return (cacheEntry.resource == null);
            case 1:
                return (cacheEntry.resource != null);
            case 2:
                return (cacheEntry.resource != null 
                        && cacheEntry.attributes.getContentLength() > 0);
            default:
                return false;
            }
        }
    }

    private static final String emptyStringIfNull(String value) {
        if (value == null) {
            return "";
        } else {
            return value;
        }
    }

}
