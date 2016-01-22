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

import javax.websocket.*;
/**
 * The ServerContainer is the specialized view of the WebSocketContainer available
 * in server-side deployments. There is one ServerContainer instance per
 * websocket application. The ServerContainer holds the methods to be able to
 * register server endpoints during the initialization phase of the application.
 * <p>For websocket enabled web containers, developers may
 * obtain a reference to the ServerContainer instance by retrieving it as an
 * attribute named <code>javax.websocket.server.ServerContainer</code> on the 
 * ServletContext. This way, the registration methods held on this interface
 * may be called to register server endpoints from a ServletContextListener 
 * during the deployment of the WAR file containing the endpoint. 
 * </p>
 * <p>WebSocket
 * implementations that run outside the web container may have other means
 * by which to provide a ServerContainer instance to the developer at application
 * deployment time. 
 * </p>
 * <p>Once the 
 * application deployment phase is complete, and the websocket application has
 * begun accepting incoming connections, the registration methods may no
 * longer be called.
 * 
 * @author dannycoward 
 */
public interface ServerContainer extends WebSocketContainer {
    
    /**
     * Deploys the given annotated endpoint into this ServerContainer during the
     * initialization phase of deploying the application. 
     * 
     * @param endpointClass the class of the annotated endpoint
     * @throws DeploymentException if the annotated endpoint was badly formed.
     * @throws IllegalStateException if the containing websocket application has already
     * been deployed.
     */
    public void addEndpoint(Class<?> endpointClass) throws DeploymentException;
    /**
     * 
     * @param serverConfig the configuration instance representing the logical endpoint
     * that will be registered.
     * @throws DeploymentException if the endpoint was badly formed.
     * @throws IllegalStateException if the containing websocket application has already
     * been deployed.
     */
    public void addEndpoint(ServerEndpointConfig serverConfig) throws DeploymentException;
    
}
