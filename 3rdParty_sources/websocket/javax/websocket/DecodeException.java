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

import java.nio.ByteBuffer;

/**
 * A general exception that occurs when trying to decode a custom object from a text or binary message.
 *
 * @author dannycoward
 */
public class DecodeException extends Exception {
    private final ByteBuffer bb;
    private final String encodedString;
    private static final long serialVersionUID = 006;

    /**
     * Constructor with the binary data that could not be decoded, and the 
     * reason why it failed to be, and the cause. The buffer may represent the 
     * whole message, or the part of the message most relevant to the decoding
     * error, depending whether the application is using one
     * of the streaming methods or not.
     *
     * @param bb      the byte buffer containing the (part of) the message that 
     * could not be decoded.
     * @param message the reason for the failure.
     * @param cause   the cause of the error.
     */
    public DecodeException(ByteBuffer bb, String message, Throwable cause) {
        super(message, cause);
        this.encodedString = null;
        this.bb = bb;
    }

    /**
     * Constructor with the text data that could not be decoded, and the reason 
     * why it failed to be, and the cause. The encoded string may represent the whole message,
     * or the part of the message most relevant to the decoding error, depending 
     * whether the application is using one
     * of the streaming methods or not.
     *
     * @param encodedString the string representing the (part of) the message that could not be decoded.
     * @param message       the reason for the failure.
     * @param cause         the cause of the error.
     */
    public DecodeException(String encodedString, String message, Throwable cause) {
        super(message, cause);
        this.encodedString = encodedString;
        this.bb = null;
    }

    /**
     * Constructs a DecodedException with the given ByteBuffer that cannot
     * be decoded, and reason why. The buffer may represent the 
     * whole message, or the part of the message most relevant to the decoding
     * error, depending whether the application is using one
     * of the streaming methods or not.
     *
     * @param bb      the byte buffer containing the (part of) the message that 
     * could not be decoded.
     * @param message the reason for the failure.
     */
    public DecodeException(ByteBuffer bb, String message) {
        super(message);
        this.encodedString = null;
        this.bb = bb;
    }

    /**
     * Constructs a DecodedException with the given encoded string that cannot
     * be decoded, and reason why. The encoded string may represent the whole message,
     * or the part of the message most relevant to the decoding error, depending 
     * whether the application is using one
     * of the streaming methods or not.
     *
     * @param encodedString the string representing the (part of) the message that 
     * could not be decoded.
     * @param message       the reason for the failure.
     */
    public DecodeException(String encodedString, String message) {
        super(message);
        this.encodedString = encodedString;
        this.bb = null;
    }

    /**
     * Return the ByteBuffer containing either the whole message, or the partial message, that
     * could not be decoded, or {@code null} if
     * this exception arose from a failure to decode a text message.
     *
     * @return the binary data not decoded or {@code null} for text message failures.
     */
    public ByteBuffer getBytes() {
        return this.bb;
    }

    /**
     * Return the encoded string that is either the whole message, or the partial 
     * message that could not be decoded, or {@code null} if
     * this exception arose from a failure to decode a binary message..
     *
     * @return the text not decoded or {@code null} for binary message failures.
     */
    public String getText() {
        return this.encodedString;
    }
}
