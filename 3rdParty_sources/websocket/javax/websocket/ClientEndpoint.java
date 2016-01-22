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
package javax.websocket;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The ClientEndpoint annotation a class level annotation is used to denote that a POJO
 * is a web socket client and can be deployed as such. Similar to
 * {@link javax.websocket.server.ServerEndpoint}, POJOs that are
 * annotated with this annotation can have methods that, using the web socket method level annotations,
 * are web socket lifecycle methods.
 * <p>
 * For example:
 * <pre><code>
 * &#64;ClientEndpoint(subprotocols="chat")
 * public class HelloServer {
 *
 *     &#64;OnMessage
 *     public void processMessageFromServer(String message, Session session) {
 *         System.out.println("Message came from the server ! " + message);
 *     }
 *
 * }
 * </code></pre>
 *
 * @author dannycoward
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ClientEndpoint {

    /**
     * The names of the subprotocols this client supports.
     *
     * @return the array of names of the subprotocols.
     */
    String[] subprotocols() default {};

    /**
     * The array of Java classes that are to act as Decoders for messages coming into
     * the client.
     *
     * @return the array of decoders.
     */
    Class<? extends Decoder>[] decoders() default {};

    /**
     * The array of Java classes that are to act as Encoders for messages sent by the client.
     *
     * @return the array of decoders.
     */
    Class<? extends Encoder>[] encoders() default {};
    
    /**
     * An optional custom configurator class that the developer would like to use
     * to provide custom configuration of new instances of this endpoint. The implementation
     * creates a new instance of the configurator per logical endpoint.
     *
     * @return the custom configurator class, or ClientEndpointConfigurator.class
     * if none was provided in the annotation.
     */
    public Class<? extends ClientEndpointConfig.Configurator> configurator() default ClientEndpointConfig.Configurator.class;
}
