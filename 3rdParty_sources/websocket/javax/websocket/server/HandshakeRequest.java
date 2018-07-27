/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012-2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package javax.websocket.server;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * The handshake request represents the web socket defined Http GET request 
 * for the opening handshake of a web socket session.
 *
 * @author dannycoward
 */
public interface HandshakeRequest {
    /**
     * The Sec-WebSocket-Key header name
     */
    static String SEC_WEBSOCKET_KEY = "Sec-WebSocket-Key";
    /**
     * The Sec-WebSocket-Protocol header name
     */
    static String SEC_WEBSOCKET_PROTOCOL = "Sec-WebSocket-Protocol";
    /**
     * The Sec-WebSocket-Version header name
     */
    static String SEC_WEBSOCKET_VERSION = "Sec-WebSocket-Version";
    /**
     * The Sec-WebSocket-Extensions header name
     */
    static String SEC_WEBSOCKET_EXTENSIONS = "Sec-WebSocket-Extensions";

    /**
     * Return the read only Map of Http Headers that came with the handshake request. The header names
     * are case insensitive.
     *
     * @return the list of headers.
     */
    Map<String, List<String>> getHeaders();

    /**
     * Return the authenticated user or {@code null} if no user is authenticated 
     * for this handshake.
     *
     * @return the user principal.
     */
    Principal getUserPrincipal();

    /**
     * Return the request URI of the handshake request.
     *
     * @return the request uri of the handshake request.
     */
    URI getRequestURI();

    /**
     * Checks whether the current user is in the given role.  Roles and role 
     * membership can be defined using deployment descriptors of the containing
     * WAR file, if running in a Java EE web container. If the user has 
     * not been authenticated, the method returns {@code false}. 
     *
     * @param role the role being checked.
     * @return whether the authenticated user is in the role, or false if the user has not
     * been authenticated.
     */
    boolean isUserInRole(String role);

    /**
     * Return a reference to the HttpSession that the web socket handshake that 
     * started this conversation was part of, if the implementation
     * is part of a Java EE web container.
     *
     * @return the http session or {@code null} if either the websocket
     * implementation is not part of a Java EE web container, or there is
     * no HttpSession associated with the opening handshake request.
     */
    Object getHttpSession();

    /**
     * Return the request parameters associated with the request.
     *
     * @return the unmodifiable map of the request parameters.
     */
    Map<String, List<String>> getParameterMap();

    /**
     * Return the query string associated with the request.
     *
     * @return the query string.
     */
    String getQueryString();
}
