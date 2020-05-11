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

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.ByteBuffer;

/**
 * The Encoder interface defines how developers can provide a way to convert
 * their custom objects into web socket messages. The Encoder interface contains
 * subinterfaces that allow encoding algorithms to encode custom objects to: text,
 * binary data, character stream and write to an output stream.
 * The websocket implementation creates a new instance of the encoder per 
 * endpoint instance per connection. This means that each encoder instance has 
 * at most one calling thread at a time.
 * The lifecycle of the Encoder instance is governed by the container calls to the
 * {@link Encoder#init(javax.websocket.EndpointConfig)} and {@link Encoder#destroy() }
 * methods.
 *
 * @author dannycoward
 */
public interface Encoder {

    /**
     * This method is called with the endpoint configuration object of the
     * endpoint this encoder is intended for when
     * it is about to be brought into service.
     * 
     * @param config the endpoint configuration object when being brought into 
     * service
     */
     void init(EndpointConfig config);
     
     /**
      * This method is called when the encoder is about to be removed
      * from service in order that any resources the encoder used may 
      * be closed gracefully.
      */
     void destroy();
     
    /**
     * This interface defines how to provide a way to convert a custom
     * object into a text message.
     *
     * @param <T> The type of the custom developer object that this Encoder can encode into a String.
     */
    interface Text<T> extends Encoder {
        /**
         * Encode the given object into a String.
         *
         * @param object the object being encoded.
         * @return the encoded object as a string.
         */
        String encode(T object) throws EncodeException;
        
    }

    /**
     * This interface may be implemented by encoding algorithms
     * that want to write the encoded object to a character stream.
     *
     * @param <T> the type of the object this encoder can encode to a CharacterStream.
     */
    interface TextStream<T> extends Encoder {
        /**
         * Encode the given object to a character stream writing it
         * to the supplied Writer. Implementations of this method may use the EncodeException
         * to indicate a failure to convert the supplied object to an encoded form, and may
         * use the IOException to indicate a failure to write the data to the supplied
         * stream.
         *
         * @param object the object to be encoded.
         * @param writer the writer provided by the web socket runtime to write the encoded data.
         * @throws EncodeException if there was an error encoding the object due to its state.
         * @throws IOException     if there was an exception writing to the writer.
         */
        void encode(T object, Writer writer) throws EncodeException, IOException;
    }

    /**
     * This interface defines how to provide a way to convert a custom
     * object into a binary message.
     *
     * @param <T> The type of the custom object that this Encoder can encoder to a ByteBuffer.
     */
    interface Binary<T> extends Encoder {
        /**
         * Encode the given object into a byte array.
         *
         * @param object the object being encoded.
         * @return the binary data.
         */
        ByteBuffer encode(T object) throws EncodeException;
    }

    /**
     * This interface may be implemented by encoding algorithms
     * that want to write the encoded object to a binary stream.
     *
     * @param <T> the type of the object this encoder can encode.
     */
    interface BinaryStream<T> extends Encoder {
        /**
         * Encode the given object into a binary stream written to the
         * implementation provided OutputStream.
         *
         * @param object the object being encoded.
         * @param os     the output stream where the encoded data is written.
         */
        void encode(T object, OutputStream os) throws EncodeException, IOException;
    }
}
