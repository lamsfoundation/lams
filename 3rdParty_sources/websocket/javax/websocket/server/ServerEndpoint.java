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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.websocket.Decoder;
import javax.websocket.Encoder;

/**
 * This class level annotation declares that the class it decorates
 * is a web socket endpoint that will be deployed and made available in the URI-space
 * of a web socket server. The annotation allows the developer to
 * define the URL (or URI template) which this endpoint will be published, and other
 * important properties of the endpoint to the websocket runtime, such as the encoders
 * it uses to send messages.
 *
 * <p>The annotated class
 * must have a public no-arg constructor.
 *
 * <p>For example:
 * <pre><code>
 * &#64;ServerEndpoint("/hello");
 * public class HelloServer {
 *
 *     &#64;OnMessage
 *     public void processGreeting(String message, Session session) {
 *         System.out.println("Greeting received:" + message);
 *     }
 *
 * }
 * </code></pre>
 *
 * @author dannycoward
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ServerEndpoint {

    /**
     * The URI or URI-template, level-1 (<a href="http://http://tools.ietf.org/html/rfc6570">See RFC 6570</a>) where the endpoint will be deployed. The URI us relative to the
     * root of the web socket container and must begin with a leading "/". Trailing "/"'s are ignored. Examples:
     * <pre><code>
     * &#64;ServerEndpoint("/chat")
     * &#64;ServerEndpoint("/chat/{user}")
     * &#64;ServerEndpoint("/booking/{privilege-level}")
     * </code></pre>
     *
     * @return the URI or URI-template
     */
    public String value();

    /**
     * The ordered array of web socket protocols this endpoint supports.
     * For example, {"superchat", "chat"}.
     *
     * @return the subprotocols.
     */
    public String[] subprotocols() default {};

    /**
     * The ordered array of decoder classes this endpoint will use. For example,
     * if the developer has provided a MysteryObject decoder, this endpoint will be able to
     * receive MysteryObjects as web socket messages. The websocket runtime will use the first
     * decoder in the list able to decode a message, ignoring the remaining decoders.
     *
     * @return the decoders.
     */
    public Class<? extends Decoder>[] decoders() default {};

    /**
     * The ordered array of encoder classes this endpoint will use. For example,
     * if the developer has provided a MysteryObject encoder, this class will be able to
     * send web socket messages in the form of MysteryObjects. The websocket runtime will use the first
     * encoder in the list able to encode a message, ignoring the remaining encoders.
     *
     * @return the encoders.
     */
    public Class<? extends Encoder>[] encoders() default {};


    /**
     * The optional custom configurator class that the developer would like to use
     * to further configure new instances of this endpoint. If no configurator
     * class is provided, the implementation uses its own.  The implementation
     * creates a new instance of the configurator per logical endpoint.
     * 
     * @return the custom configuration class, or ServerEndpointConfig.Configurator.class
     * if none was set in the annotation.
     */
    public Class<? extends ServerEndpointConfig.Configurator> configurator() default ServerEndpointConfig.Configurator.class;
}
