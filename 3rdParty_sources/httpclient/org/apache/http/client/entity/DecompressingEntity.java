/*
 * ====================================================================
 *
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */
package org.apache.http.client.entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;

/**
 * Common base class for decompressing {@link HttpEntity} implementations.
 *
 * @since 4.1
 */
abstract class DecompressingEntity extends HttpEntityWrapper {

    /**
     * Default buffer size.
     */
    private static final int BUFFER_SIZE = 1024 * 2;

    /**
     * DecompressingEntities are not repeatable, so they will return the same
     * InputStream instance when {@link #getContent()} is called.
     */
    private InputStream content;

    /**
     * Creates a new {@link DecompressingEntity}.
     *
     * @param wrapped
     *            the non-null {@link HttpEntity} to be wrapped
     */
    public DecompressingEntity(final HttpEntity wrapped) {
        super(wrapped);
    }

    abstract InputStream getDecompressingInputStream(final InputStream wrapped) throws IOException;

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream getContent() throws IOException {
        if (wrappedEntity.isStreaming()) {
            if (content == null) {
                content = getDecompressingInputStream(wrappedEntity.getContent());
            }
            return content;
        } else {
            return getDecompressingInputStream(wrappedEntity.getContent());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeTo(OutputStream outstream) throws IOException {
        if (outstream == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }
        InputStream instream = getContent();
        try {
            byte[] buffer = new byte[BUFFER_SIZE];

            int l;

            while ((l = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, l);
            }
        } finally {
            instream.close();
        }
    }

}
