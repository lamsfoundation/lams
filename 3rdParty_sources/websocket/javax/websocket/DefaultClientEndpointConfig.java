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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The DefaultClientEndpointConfig is a concrete implementation of a client configuration. 
 *
 * @author dannycoward
 */
 final class DefaultClientEndpointConfig implements ClientEndpointConfig {
    private List<String> preferredSubprotocols;
    private List<Extension> extensions;
    private List<Class<? extends Encoder>> encoders;
    private List<Class<? extends Decoder>> decoders;
    private Map<String, Object> userProperties = new HashMap<String, Object>();
    private ClientEndpointConfig.Configurator clientEndpointConfigurator;

    
    DefaultClientEndpointConfig(
            List<String> preferredSubprotocols,
            List<Extension> extensions,
            List<Class<? extends Encoder>> encoders,
            List<Class<? extends Decoder>> decoders,
            ClientEndpointConfig.Configurator clientEndpointConfigurator) {
        this.preferredSubprotocols = Collections.unmodifiableList(preferredSubprotocols);
        this.extensions = Collections.unmodifiableList(extensions);
        this.encoders = Collections.unmodifiableList(encoders);
        this.decoders = Collections.unmodifiableList(decoders);
        this.clientEndpointConfigurator = clientEndpointConfigurator;
    }

    /**
     * Return the protocols, in order of preference, favorite first, that this client would
     * like to use for its sessions.
     *
     * @return the preferred subprotocols.
     */
     @Override
    public List<String> getPreferredSubprotocols() {
        return this.preferredSubprotocols;
    }



    /**
     * Return the extensions, in order of preference, favorite first, that this client would
     * like to use for its sessions.
     *
     * @return the (unmodifiable) extension list.
     */
     @Override
    public List<Extension> getExtensions() {
        return this.extensions;
    }



    /**
     * Return the (unmodifiable) list of encoders this client will use.
     *
     * @return the encoder list.
     */
     @Override
    public List<Class<? extends Encoder>> getEncoders() {
        return this.encoders;
    }



    /**
     * Return the (unmodifiable) list of decoders this client will use.
     *
     * @return the decoders to use.
     */
     @Override
    public List<Class<? extends Decoder>> getDecoders() {
        return this.decoders;
    }


    /**
     * Editable map of user properties.
     */
     @Override
    public final Map<String, Object> getUserProperties() {
        return this.userProperties;
    }
    
     @Override
    public ClientEndpointConfig.Configurator getConfigurator() {
        return this.clientEndpointConfigurator;
    }
 

}
