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


/** 
 * The Web Socket Endpoint represents an object that can handle websocket conversations. 
 * Developers may extend this class in order to implement a programmatic websocket 
 * endpoint. The Endpoint class holds lifecycle methods that may be 
 * overridden to intercept websocket open, error and close events. By implementing 
 * the {@link Endpoint#onOpen(javax.websocket.Session, javax.websocket.EndpointConfig) onOpen} method, the programmatic endpoint gains access to the {@link Session} object, 
 * to which the developer may add {@link MessageHandler} implementations in order to 
 * intercept incoming websocket messages. Each instance 
 * of a websocket endpoint is guaranteed not to be called by more than one thread 
 * at a time per active connection.
 *
 * <p>If deployed as a client endpoint, it will be instantiated once for the 
 * single connection to the server. 
 *
 * <p>When deployed as a server endpoint, the implementation uses the 
 * {@link javax.websocket.server.ServerEndpointConfig.Configurator#getEndpointInstance} 
 * method to obtain the 
 * endpoint instance it will use for each new client connection.  If the developer uses 
 * the default {@link javax.websocket.server.ServerEndpointConfig.Configurator}, 
 * there will be precisely one 
 * endpoint instance per active client connection. Consequently, in this typical 
 * case, when implementing/overriding the methods of Endpoint, the developer is 
 * guaranteed that there will be at most one thread calling each endpoint instance
 * at a time.
 * 
 * <p>If the developer provides a custom {@link javax.websocket.server.ServerEndpointConfig.Configurator}
 * which overrides the default policy for endpoint instance creation, for example, 
 * using a single Endpoint instance for multiple client connections, the developer
 * may need to write code that can execute concurrently.
 *
 * <p>Here is an example of a simple endpoint that echoes any incoming text message back to the sender.
 * <pre><code>
 * public class EchoServer extends Endpoint {
 *
 *     public void onOpen(Session session, EndpointConfig config) {
 *         final RemoteEndpoint remote = session.getBasicRemote();
 *         session.addMessageHandler(String.class, new MessageHandler.Whole&lt;String>() {
 *             public void onMessage(String text) {
 *                 try {
 *                     remote.sendString("Got your message (" + text + "). Thanks !");
 *                 } catch (IOException ioe) {
 *                     // handle send failure here
 *                 }
 *             }
 *         });
 *     }
 *
 * }
 * </code></pre>
 *
 * @author dannycoward
 */
public abstract class Endpoint {

    /**
     * Developers must implement this method to be notified when a new conversation has
     * just begun.
     *
     * @param session the session that has just been activated.
     * @param config  the configuration used to configure this endpoint.
     */
    public abstract void onOpen(Session session, EndpointConfig config);

    /**
     * This method is called immediately prior to the session with the remote 
     * peer being closed. It is called whether the session is being closed 
     * because the remote peer initiated a close and sent a close frame, or 
     * whether the local websocket container or this endpoint requests to close 
     * the session. The developer may take this last opportunity to retrieve
     * session attributes such as the ID, or any application data it holds before 
     * it becomes unavailable after the completion of the method. Developers should
     * not attempt to modify the session from within this method, or send new 
     * messages from this call as the underlying 
     * connection will not be able to send them at this stage.
     *
     * @param session     the session about to be closed.
     * @param closeReason the reason the session was closed.
     */
    public void onClose(Session session, CloseReason closeReason) {
    }

    /**
     * Developers may implement this method when the web socket session
     * creates some kind of error that is not modeled in the web socket protocol. This may for example
     * be a notification that an incoming message is too big to handle, or that the incoming message could not be encoded.
     *
     * <p>There are a number of categories of exception that this method is (currently) defined to handle:
     * <ul>
     * <li>connection problems, for example, a socket failure that occurs before
     * the web socket connection can be formally closed. These are modeled as 
     * {@link SessionException}s</li>
     * <li>runtime errors thrown by developer created message handlers calls.</li>
     * <li>conversion errors encoding incoming messages before any message handler has been called. These
     * are modeled as {@link DecodeException}s</li>
     * </ul>
     *
     * @param session the session in use when the error occurs.
     * @param thr     the throwable representing the problem.
     */
    public void onError(Session session, Throwable thr) {
    }
}
