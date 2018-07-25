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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * The ClientEndpointConfig is a special kind of endpoint configuration object that contains
 * web socket configuration information specific only to client endpoints. Developers deploying 
 * programmatic client endpoints can create instances of this configuration by
 * using a {@link ClientEndpointConfig.Builder}. Developers can override some
 * of the configuration operations by providing an implementation of
 * {@link ClientEndpointConfig.Configurator}.
 *
 * @author dannycoward
 */
public interface ClientEndpointConfig extends EndpointConfig {


    /**
     * Return the ordered list of sub protocols a client endpoint would like to use,
     * in order of preference, favorite first that this client would
     * like to use for its sessions.
     * This list is used to generate the Sec-WebSocket-Protocol header in the opening
     * handshake for clients using this configuration. The first protocol name is the most preferred.
     * See <a href="http://tools.ietf.org/html/rfc6455#section-4.1">Client Opening Handshake</a>.
     *
     * @return the list of the preferred subprotocols, the empty list if there are none
     */
    List<String> getPreferredSubprotocols();

    /**
     * Return the extensions, in order of preference, favorite first, that this client would
     * like to use for its sessions. These are the extensions that will
     * be used to populate the Sec-WebSocket-Extensions header in the opening handshake for clients
     * using this configuration. The first extension in the list is the most preferred extension.
     * See <a href="http://tools.ietf.org/html/rfc6455#section-9.1">Negotiating Extensions</a>.
     *
     * @return the list of extensions, the empty list if there are none.
     */
    List<Extension> getExtensions();
    
    
    /**
     * Return the custom configurator for this configuration. If the developer
     * did not provide one, the platform default configurator is returned.
     * 
     * @return the configurator in use with this configuration.
     */
    public ClientEndpointConfig.Configurator getConfigurator();

    /**
     * The Configurator class may be extended by developers who want to
     * provide custom configuration algorithms, such as intercepting the opening handshake, or
     * providing arbitrary methods and algorithms that can be accessed from each endpoint
     * instance configured with this configurator.

     */
    public class Configurator {

        /**
         * This method is called by the implementation after it has formulated the handshake
         * request that will be used to initiate the connection to the server, but before it has
         * sent any part of the request. This allows the developer to inspect and modify the
         * handshake request headers prior to the start of the handshake interaction.
         *
         * @param headers the mutable map of handshake request headers the implementation is about to send to
         *                start the handshake interaction.
         */
        public void beforeRequest(Map<String, List<String>> headers) {

        }

        /**
         * This method is called by the implementation after it has received a handshake response
         * from the server as a result of a handshake interaction it initiated. The developer may implement
         * this method in order to inspect the returning handshake response.
         *
         * @param hr the handshake response sent by the server.
         */
        public void afterResponse(HandshakeResponse hr) {

        }
    }

    /**
    * The ClientEndpointConfig.Builder is a class used for creating
    * {@link ClientEndpointConfig} objects for the purposes of
    * deploying a client endpoint.
    * Here are some examples:
    * Building a plain configuration with no encoders, decoders, subprotocols or extensions.
    * <code>
    * ClientEndpointConfig cec = ClientEndpointConfig.Builder.create().build();
    * </code>
    * 
    * Building a configuration with no subprotocols and a custom configurator.
    * <pre><code>
    * ClientEndpointConfig customCec = ClientEndpointConfig.Builder.create()
    *         .preferredSubprotocols(mySubprotocols)
    *         .configurator(new MyClientConfigurator())
    *         .build();
    * </code></pre>
    * 
    * 
    * @author dannycoward
    */
   public final class Builder {
       private List<String> preferredSubprotocols = Collections.emptyList();
       private List<Extension> extensions = Collections.emptyList();
       private List<Class<? extends Encoder>> encoders = Collections.emptyList();
       private List<Class<? extends Decoder>> decoders = Collections.emptyList();
       private ClientEndpointConfig.Configurator clientEndpointConfigurator = new ClientEndpointConfig.Configurator() {

       };
       
       // use create()
       private Builder() {   
       }

       /**
        * Creates a new builder object with no subprotocols, extensions, encoders,
        * decoders and a {@code null} configurator.
        * 
        * @return a new builder object.
        */
       public static ClientEndpointConfig.Builder create() {
           return new ClientEndpointConfig.Builder();
       }

       /**
        * Builds a configuration object using the attributes set
        * on this builder.
        * 
        * @return a new configuration object.
        */
       public ClientEndpointConfig build() {
           return new DefaultClientEndpointConfig(
               Collections.unmodifiableList(this.preferredSubprotocols),
               Collections.unmodifiableList(this.extensions),
               Collections.unmodifiableList(this.encoders),
               Collections.unmodifiableList(this.decoders),
               this.clientEndpointConfigurator);
       }



       /**
        * Sets the configurator object for the configuration this builder will build.
        * 
        * @param clientEndpointConfigurator the configurator
        * @return the builder instance
        */
       public ClientEndpointConfig.Builder configurator(ClientEndpointConfig.Configurator clientEndpointConfigurator) {
           this.clientEndpointConfigurator = clientEndpointConfigurator;
           return this;
       }


       /**
        * Set the preferred sub protocols for the configuration this builder will build. The
        * list is treated in order of preference, favorite first, that this client would
        * like to use for its sessions.
        * 
        * @param preferredSubprotocols the preferred subprotocol names.
        * @return the builder instance
        */
       public ClientEndpointConfig.Builder preferredSubprotocols(List<String> preferredSubprotocols) {
           this.preferredSubprotocols = (preferredSubprotocols == null) ? new ArrayList<String>() : preferredSubprotocols;
           return this;
       }


       /**
        * Set the extensions for the configuration this builder will build. The 
        * list is treated in order of preference, favorite first, that the 
        * client would like to use for its sessions.
        * 
        * @param extensions the extensions
        * @return the builder instance
        */
       public ClientEndpointConfig.Builder extensions(List<Extension> extensions) {
           this.extensions = (extensions == null) ? new ArrayList<Extension>() : extensions;
           return this;
       }

       /**
        * Assign the list of encoder implementation classes the client will use.
        *
        * @param encoders the encoder implementation classes
        * @return the builder instance
        */
       public ClientEndpointConfig.Builder encoders(List<Class<? extends Encoder>> encoders) {
           this.encoders = (encoders == null) ? new ArrayList<Class<? extends Encoder>>() : encoders;
           return this;
       }

       /**
        * Assign the list of decoder implementation classes the client will use.
        *
        * @param decoders the decoder implementation classes
        * @return this builder instance
        */
       public ClientEndpointConfig.Builder decoders(List<Class<? extends Decoder>> decoders) {
           this.decoders = (decoders == null) ? new ArrayList<Class<? extends Decoder>>() : decoders;
           return this;
       }


   }

}


