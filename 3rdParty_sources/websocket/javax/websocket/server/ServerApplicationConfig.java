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

import java.util.Set;
import javax.websocket.Endpoint;

/**
 * Developers include implementations of ServerApplicationConfig in an archive containing
 * websocket endpoints (WAR file, or JAR file within the WAR file) in order to specify the websocket 
 * endpoints within the archive the implementation must deploy. There is a separate
 * method for programmatic endpoints and for annotated endpoints.
 *
 * @author dannycoward
 */
public interface ServerApplicationConfig {

    /**
     * Return a set of ServerEndpointConfig instances that the server container
     * will use to deploy the programmatic endpoints. The set of Endpoint classes passed in to this method is
     * the set obtained by scanning the archive containing the implementation
     * of this ServerApplicationConfig. This set passed in
     * may be used the build the set of ServerEndpointConfig instances
     * to return to the container for deployment.
     *
     * @param endpointClasses the set of all the Endpoint classes in the archive containing
     *                the implementation of this interface.
     * @return the non-null set of ServerEndpointConfig s to deploy on the server, using the empty set to
     * indicate none.
     */
    public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> endpointClasses);

    /**
     * Return a set of annotated endpoint classes that the server container
     * must deploy. The set of classes passed in to this method is
     * the set obtained by scanning the archive containing the implementation
     * of this interface. Therefore, this set passed in contains all the annotated endpoint classes
     * in the JAR or WAR file containing the implementation of this interface. This set passed in
     * may be used the build the set to return to the container for deployment.
     *
     * @param scanned the set of all the annotated endpoint classes in the archive containing
     *                the implementation of this interface.
     * @return the non-null set of annotated endpoint classes to deploy on the server, using the empty
     * set to indicate none.
     */
    Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> scanned);
}
