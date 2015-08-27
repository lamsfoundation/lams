/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2014 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.undertow.servlet.spec;

import io.undertow.servlet.UndertowServletMessages;
import io.undertow.servlet.api.ThreadSetupAction;
import io.undertow.servlet.core.CompositeThreadSetupAction;
import org.xnio.Buffers;
import org.xnio.ChannelListener;
import org.xnio.IoUtils;
import org.xnio.Pool;
import org.xnio.Pooled;
import org.xnio.channels.Channels;
import org.xnio.channels.EmptyStreamSourceChannel;
import org.xnio.channels.StreamSourceChannel;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.xnio.Bits.allAreClear;
import static org.xnio.Bits.anyAreClear;
import static org.xnio.Bits.anyAreSet;

/**
 * Servlet input stream implementation. This stream is non-buffered, and is used for both
 * HTTP requests and for upgraded streams.
 *
 * @author Stuart Douglas
 */
public class ServletInputStreamImpl extends ServletInputStream {

    private final HttpServletRequestImpl request;
    private final StreamSourceChannel channel;
    private final Pool<ByteBuffer> bufferPool;

    private volatile ReadListener listener;
    private volatile ServletInputStreamChannelListener internalListener;

    /**
     * If this stream is ready for a read
     */
    private static final int FLAG_READY = 1;
    private static final int FLAG_CLOSED = 1 << 1;
    private static final int FLAG_FINISHED = 1 << 2;
    private static final int FLAG_ON_DATA_READ_CALLED = 1 << 3;

    private int state;
    private AsyncContextImpl asyncContext;
    private Pooled<ByteBuffer> pooled;

    public ServletInputStreamImpl(final HttpServletRequestImpl request) {
        this.request = request;
        if (request.getExchange().isRequestChannelAvailable()) {
            this.channel = request.getExchange().getRequestChannel();
        } else {
            this.channel = new EmptyStreamSourceChannel(request.getExchange().getIoThread());
        }
        this.bufferPool = request.getExchange().getConnection().getBufferPool();
    }


    @Override
    public boolean isFinished() {
        return anyAreSet(state, FLAG_FINISHED);
    }

    @Override
    public boolean isReady() {
        return anyAreSet(state, FLAG_READY) && !isFinished();
    }

    @Override
    public void setReadListener(final ReadListener readListener) {
        if (readListener == null) {
            throw UndertowServletMessages.MESSAGES.listenerCannotBeNull();
        }
        if (listener != null) {
            throw UndertowServletMessages.MESSAGES.listenerAlreadySet();
        }
        if (!request.isAsyncStarted()) {
            throw UndertowServletMessages.MESSAGES.asyncNotStarted();
        }

        asyncContext = request.getAsyncContext();
        listener = readListener;
        channel.getReadSetter().set(internalListener = new ServletInputStreamChannelListener());

        //we resume from an async task, after the request has been dispatched
        asyncContext.addAsyncTask(new Runnable() {
            @Override
            public void run() {
                channel.getIoThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        channel.resumeReads();
                        internalListener.handleEvent(channel);
                    }
                });
            }
        });
    }

    @Override
    public int read() throws IOException {
        byte[] b = new byte[1];
        int read = read(b);
        if (read == -1) {
            return -1;
        }
        return b[0] & 0xff;
    }

    @Override
    public int read(final byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        if (anyAreSet(state, FLAG_CLOSED)) {
            throw UndertowServletMessages.MESSAGES.streamIsClosed();
        }
        if (listener != null) {
            if (anyAreClear(state, FLAG_READY)) {
                throw UndertowServletMessages.MESSAGES.streamNotReady();
            }
        } else {
            readIntoBuffer();
        }
        if (anyAreSet(state, FLAG_FINISHED)) {
            return -1;
        }
        if (len == 0) {
            return 0;
        }
        ByteBuffer buffer = pooled.getResource();
        int copied = Buffers.copy(ByteBuffer.wrap(b, off, len), buffer);
        if (!buffer.hasRemaining()) {
            pooled.free();
            pooled = null;
            if (listener != null) {
                readIntoBufferNonBlocking();
            }
        }
        return copied;
    }

    private void readIntoBuffer() throws IOException {
        if (pooled == null && !anyAreSet(state, FLAG_FINISHED)) {
            pooled = bufferPool.allocate();

            int res = Channels.readBlocking(channel, pooled.getResource());
            pooled.getResource().flip();
            if (res == -1) {
                state |= FLAG_FINISHED;
                pooled.free();
                pooled = null;
            }
        }
    }

    private void readIntoBufferNonBlocking() throws IOException {
        if (pooled == null && !anyAreSet(state, FLAG_FINISHED)) {
            pooled = bufferPool.allocate();
            if (listener == null) {
                int res = channel.read(pooled.getResource());
                if (res == 0) {
                    pooled.free();
                    pooled = null;
                    return;
                }
                pooled.getResource().flip();
                if (res == -1) {
                    state |= FLAG_FINISHED;
                    pooled.free();
                    pooled = null;
                }
            } else {
                if (anyAreClear(state, FLAG_READY)) {
                    throw UndertowServletMessages.MESSAGES.streamNotReady();
                }
                int res = channel.read(pooled.getResource());
                pooled.getResource().flip();
                if (res == -1) {
                    state |= FLAG_FINISHED;
                    pooled.free();
                    pooled = null;
                } else if (res == 0) {
                    state &= ~FLAG_READY;
                    pooled.free();
                    pooled = null;
                    channel.getIoThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            if(!channel.isReadResumed()) {
                                channel.resumeReads();
                            }
                        }
                    });
                }
            }
        }
    }

    @Override
    public int available() throws IOException {
        if (anyAreSet(state, FLAG_CLOSED)) {
            throw UndertowServletMessages.MESSAGES.streamIsClosed();
        }
        readIntoBufferNonBlocking();
        if (anyAreSet(state, FLAG_FINISHED)) {
            return 0;
        }
        if (pooled == null) {
            return 0;
        }
        return pooled.getResource().remaining();
    }

    @Override
    public void close() throws IOException {
        if (anyAreSet(state, FLAG_CLOSED)) {
            return;
        }
        this.state = state | FLAG_CLOSED;
        try {
            while (allAreClear(state, FLAG_FINISHED)) {
                readIntoBuffer();
                if (pooled != null) {
                    pooled.free();
                    pooled = null;
                }
            }
        } finally {
            state |= FLAG_FINISHED;
            if (pooled != null) {
                pooled.free();
                pooled = null;
            }
            channel.shutdownReads();
        }
    }

    private class ServletInputStreamChannelListener implements ChannelListener<StreamSourceChannel> {
        @Override
        public void handleEvent(final StreamSourceChannel channel) {
            if (asyncContext.isDispatched()) {
                //this is no longer an async request
                //we just return
                //TODO: what do we do here? Revert back to blocking mode?
                channel.suspendReads();
                return;
            }
            if (anyAreSet(state, FLAG_FINISHED)) {
                channel.suspendReads();
                return;
            }
            state |= FLAG_READY;
            try {
                readIntoBufferNonBlocking();
                if (pooled != null) {
                    state |= FLAG_READY;
                    if (!anyAreSet(state, FLAG_FINISHED)) {
                        CompositeThreadSetupAction action = request.getServletContext().getDeployment().getThreadSetupAction();
                        ThreadSetupAction.Handle handle = action.setup(request.getExchange());
                        try {
                            listener.onDataAvailable();
                        } finally {
                            handle.tearDown();
                        }
                        if(pooled != null) {
                            //they did not consume all the data
                            channel.suspendReads();
                        }
                    }
                }
            } catch (Exception e) {
                CompositeThreadSetupAction action = request.getServletContext().getDeployment().getThreadSetupAction();
                ThreadSetupAction.Handle handle = action.setup(request.getExchange());
                try {
                    listener.onError(e);
                } finally {
                    handle.tearDown();
                }
                IoUtils.safeClose(channel);
            }
            if (anyAreSet(state, FLAG_FINISHED)) {
                if (anyAreClear(state, FLAG_ON_DATA_READ_CALLED)) {
                    try {
                        state |= FLAG_ON_DATA_READ_CALLED;
                        channel.shutdownReads();
                        CompositeThreadSetupAction action = request.getServletContext().getDeployment().getThreadSetupAction();
                        ThreadSetupAction.Handle handle = action.setup(request.getExchange());
                        try {
                            listener.onAllDataRead();
                        } finally {
                            handle.tearDown();
                        }
                    } catch (IOException e) {
                        listener.onError(e);
                        IoUtils.safeClose(channel);
                    }
                }
            }

        }
    }
}
