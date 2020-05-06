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
 * Developers implement MessageHandlers in order to receive incoming messages
 * during a web socket conversation.
 * Each web socket session uses no more than one thread at a time to call its MessageHandlers. This means
 * that, provided each message handler instance is used to handle messages for one web socket session, at most
 * one thread at a time can be calling any of its methods. Developers who wish to handle messages from multiple
 * clients within the same message handlers may do so by adding the same instance as a handler on each of the Session
 * objects for the clients. In that case, they will need to code with the possibility of their MessageHandler
 * being called concurrently by multiple threads, each one arising from a different client session.
 *
 * <p>See {@link Endpoint} for a usage example.
 *
 * @author dannycoward
 */
public interface MessageHandler {

    /**
     * This kind of handler is notified by the container on arrival of a complete message. If the message is received in parts,
     * the container buffers it until it is has been fully received before this method is called.
     *
     * <p>For handling incoming text messages, the allowed types for T are
     * <ul>
     * <li>{@link java.lang.String}</li>
     * <li>{@link java.io.Reader} </li>
     * <li>any developer object for which there is a corresponding {@link Decoder.Text} or
     * {@link Decoder.TextStream} configured</li>
     * </ul>
     *
     * <p>For handling incoming binary messages, the allowed types for T are
     * <ul>
     * <li>{@link java.nio.ByteBuffer} </li>
     * <li>byte[] </li>
     * <li>{@link java.io.InputStream} </li>
     * <li>any developer object for which there is a corresponding {@link Decoder.Binary} or
     * {@link Decoder.BinaryStream} configured
     * </ul>
     *
     * <p>For handling incoming pong messages, the type of T is {@link PongMessage}
     *
     * <p>Developers should not continue to reference message objects of type {@link java.io.Reader}, {@link java.nio.ByteBuffer}
     * or {@link java.io.InputStream} after the completion of the onMessage() call, since they
     * may be recycled by the implementation.
     *
     * @param <T> The type of the message object that this MessageHandler will consume.
     */
    interface Whole<T> extends MessageHandler {

        /**
         * Called when the message has been fully received.
         *
         * @param message the message data.
         */
        void onMessage(T message);
    }

    /**
     * This kind of handler is notified by the implementation as it becomes ready
     * to deliver parts of a whole message.
     *
     * <p>For handling parts of text messages, the type T is {@link java.lang.String}
     *
     * <p>For handling parts of binary messages, the allowable types for T are
     * <ul>
     * <li>{@link java.nio.ByteBuffer} </li>
     * <li>byte[] </li>
     * </ul>
     *
     * <p>Developers should not continue to reference message objects of type {@link java.nio.ByteBuffer}
     * after the completion of the onMessage() call, since they
     * may be recycled by the implementation.
     *
     * <p>Note: Implementations may choose their own schemes for delivering large messages in smaller parts through this API. These
     * schemes may or may not bear a relationship to the underlying websocket dataframes in which the message
     * is received off the wire.
     *
     * @param <T> The type of the object that represent pieces of the incoming message that this MessageHandler will consume.
     */
    interface Partial<T> extends MessageHandler {

        /**
         * Called when the next part of a message has been fully received.
         *
         * @param partialMessage the partial message data.
         * @param last           flag to indicate if this partialMessage is the last of the whole message being delivered.
         */
        void onMessage(T partialMessage, boolean last);
    }
}
