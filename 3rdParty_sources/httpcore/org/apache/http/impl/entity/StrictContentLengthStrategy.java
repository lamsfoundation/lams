/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.http.impl.entity;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolException;
import org.apache.http.annotation.Immutable;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.protocol.HTTP;

/**
 * The strict implementation of the content length strategy. This class
 * will throw {@link ProtocolException} if it encounters an unsupported
 * transfer encoding or a malformed <code>Content-Length</code> header
 * value.
 * <p>
 * This class recognizes "chunked" and "identitiy" transfer-coding only.
 *
 * @since 4.0
 */
@Immutable
public class StrictContentLengthStrategy implements ContentLengthStrategy {

    private final int implicitLen;
    
    /**
     * Creates <tt>StrictContentLengthStrategy</tt> instance with the given length used per default
     * when content length is not explicitly specified in the message.
     * 
     * @param implicitLen implicit content length.
     * 
     * @since 4.2
     */
    public StrictContentLengthStrategy(int implicitLen) {
        super();
        this.implicitLen = implicitLen;
    }

    /**
     * Creates <tt>StrictContentLengthStrategy</tt> instance. {@link ContentLengthStrategy#IDENTITY} 
     * is used per default when content length is not explicitly specified in the message.
     */
    public StrictContentLengthStrategy() {
        this(IDENTITY);
    }

    public long determineLength(final HttpMessage message) throws HttpException {
        if (message == null) {
            throw new IllegalArgumentException("HTTP message may not be null");
        }
        // Although Transfer-Encoding is specified as a list, in practice
        // it is either missing or has the single value "chunked". So we
        // treat it as a single-valued header here.
        Header transferEncodingHeader = message.getFirstHeader(HTTP.TRANSFER_ENCODING);
        if (transferEncodingHeader != null) {
            String s = transferEncodingHeader.getValue();
            if (HTTP.CHUNK_CODING.equalsIgnoreCase(s)) {
                if (message.getProtocolVersion().lessEquals(HttpVersion.HTTP_1_0)) {
                    throw new ProtocolException(
                            "Chunked transfer encoding not allowed for " +
                            message.getProtocolVersion());
                }
                return CHUNKED;
            } else if (HTTP.IDENTITY_CODING.equalsIgnoreCase(s)) {
                return IDENTITY;
            } else {
                throw new ProtocolException(
                        "Unsupported transfer encoding: " + s);
            }
        }
        Header contentLengthHeader = message.getFirstHeader(HTTP.CONTENT_LEN);
        if (contentLengthHeader != null) {
            String s = contentLengthHeader.getValue();
            try {
                long len = Long.parseLong(s);
                if (len < 0) {
                    throw new ProtocolException("Negative content length: " + s);
                }
                return len;
            } catch (NumberFormatException e) {
                throw new ProtocolException("Invalid content length: " + s);
            }
        }
        return this.implicitLen;
    }

}
