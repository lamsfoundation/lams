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
import org.apache.http.HeaderElement;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.ParseException;
import org.apache.http.ProtocolException;
import org.apache.http.annotation.Immutable;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.params.HttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;

/**
 * The lax implementation of the content length strategy. This class will ignore
 * unrecognized transfer encodings and malformed <code>Content-Length</code>
 * header values if the {@link CoreProtocolPNames#STRICT_TRANSFER_ENCODING}
 * parameter of the given message is not set or set to <code>false</code>.
 * <p>
 * This class recognizes "chunked" and "identitiy" transfer-coding only.
 * <p>
 * The following parameters can be used to customize the behavior of this class:
 * <ul>
 *  <li>{@link org.apache.http.params.CoreProtocolPNames#STRICT_TRANSFER_ENCODING}</li>
 * </ul>
 *
 * @since 4.0
 */
@Immutable
public class LaxContentLengthStrategy implements ContentLengthStrategy {

    private final int implicitLen;
    
    /**
     * Creates <tt>LaxContentLengthStrategy</tt> instance with the given length used per default
     * when content length is not explicitly specified in the message.
     * 
     * @param implicitLen implicit content length.
     * 
     * @since 4.2
     */
    public LaxContentLengthStrategy(int implicitLen) {
        super();
        this.implicitLen = implicitLen;
    }

    /**
     * Creates <tt>LaxContentLengthStrategy</tt> instance. {@link ContentLengthStrategy#IDENTITY} 
     * is used per default when content length is not explicitly specified in the message.
     */
    public LaxContentLengthStrategy() {
        this(IDENTITY);
    }

    public long determineLength(final HttpMessage message) throws HttpException {
        if (message == null) {
            throw new IllegalArgumentException("HTTP message may not be null");
        }

        HttpParams params = message.getParams();
        boolean strict = params.isParameterTrue(CoreProtocolPNames.STRICT_TRANSFER_ENCODING);

        Header transferEncodingHeader = message.getFirstHeader(HTTP.TRANSFER_ENCODING);
        // We use Transfer-Encoding if present and ignore Content-Length.
        // RFC2616, 4.4 item number 3
        if (transferEncodingHeader != null) {
            HeaderElement[] encodings = null;
            try {
                encodings = transferEncodingHeader.getElements();
            } catch (ParseException px) {
                throw new ProtocolException
                    ("Invalid Transfer-Encoding header value: " +
                     transferEncodingHeader, px);
            }
            if (strict) {
                // Currently only chunk and identity are supported
                for (int i = 0; i < encodings.length; i++) {
                    String encoding = encodings[i].getName();
                    if (encoding != null && encoding.length() > 0
                        && !encoding.equalsIgnoreCase(HTTP.CHUNK_CODING)
                        && !encoding.equalsIgnoreCase(HTTP.IDENTITY_CODING)) {
                        throw new ProtocolException("Unsupported transfer encoding: " + encoding);
                    }
                }
            }
            // The chunked encoding must be the last one applied RFC2616, 14.41
            int len = encodings.length;
            if (HTTP.IDENTITY_CODING.equalsIgnoreCase(transferEncodingHeader.getValue())) {
                return IDENTITY;
            } else if ((len > 0) && (HTTP.CHUNK_CODING.equalsIgnoreCase(
                    encodings[len - 1].getName()))) {
                return CHUNKED;
            } else {
                if (strict) {
                    throw new ProtocolException("Chunk-encoding must be the last one applied");
                }
                return IDENTITY;
            }
        }
        Header contentLengthHeader = message.getFirstHeader(HTTP.CONTENT_LEN);
        if (contentLengthHeader != null) {
            long contentlen = -1;
            Header[] headers = message.getHeaders(HTTP.CONTENT_LEN);
            if (strict && headers.length > 1) {
                throw new ProtocolException("Multiple content length headers");
            }
            for (int i = headers.length - 1; i >= 0; i--) {
                Header header = headers[i];
                try {
                    contentlen = Long.parseLong(header.getValue());
                    break;
                } catch (NumberFormatException e) {
                    if (strict) {
                        throw new ProtocolException("Invalid content length: " + header.getValue());
                    }
                }
                // See if we can have better luck with another header, if present
            }
            if (contentlen >= 0) {
                return contentlen;
            } else {
                return IDENTITY;
            }
        }
        return this.implicitLen;
    }

}
