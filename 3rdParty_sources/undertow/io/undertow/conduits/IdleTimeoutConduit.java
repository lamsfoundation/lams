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
package io.undertow.conduits;

import io.undertow.UndertowLogger;
import org.xnio.Buffers;
import org.xnio.XnioExecutor;
import org.xnio.XnioIoThread;
import org.xnio.XnioWorker;
import org.xnio.channels.StreamSinkChannel;
import org.xnio.channels.StreamSourceChannel;
import org.xnio.conduits.ReadReadyHandler;
import org.xnio.conduits.StreamSinkConduit;
import org.xnio.conduits.StreamSourceConduit;
import org.xnio.conduits.WriteReadyHandler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.FileChannel;
import java.util.concurrent.TimeUnit;

/**
 *  Conduit that adds support to close a channel once for a specified time no
 * reads and no writes were performed.
 *
 * @author <a href="mailto:nmaurer@redhat.com">Norman Maurer</a>
 */
public class IdleTimeoutConduit implements StreamSinkConduit, StreamSourceConduit {

    private static final int DELTA = 100;
    private volatile XnioExecutor.Key handle;
    private volatile long idleTimeout;
    private volatile long expireTime = -1;
    private volatile boolean timedOut = false;

    private final StreamSinkConduit sink;
    private final StreamSourceConduit source;

    private volatile WriteReadyHandler writeReadyHandler;
    private volatile ReadReadyHandler readReadyHandler;

    private final Runnable timeoutCommand = new Runnable() {
        @Override
        public void run() {
            handle = null;
            if(expireTime == -1) {
                return;
            }
            long current = System.currentTimeMillis();
            if(current  < expireTime) {
                //timeout has been bumped, re-schedule
                handle = sink.getWriteThread().executeAfter(timeoutCommand, (expireTime - current) + DELTA, TimeUnit.MILLISECONDS);
                return;
            }

            UndertowLogger.REQUEST_LOGGER.tracef("Timing out channel %s due to inactivity");
            timedOut = true;
            doClose();
            if (sink.isWriteResumed()) {
                if(writeReadyHandler != null) {
                    writeReadyHandler.writeReady();
                }
            }
            if (source.isReadResumed()) {
                if(readReadyHandler != null) {
                    readReadyHandler.readReady();
                }
            }
        }
    };

    protected void doClose() {
        safeClose(sink);
        safeClose(source);
    }

    public IdleTimeoutConduit(StreamSinkConduit sink, StreamSourceConduit source) {
        this.sink = sink;
        this.source = source;
    }

    private void handleIdleTimeout() throws ClosedChannelException {
        if(timedOut) {
            return;
        }
        long idleTimeout = this.idleTimeout;
        if(idleTimeout <= 0) {
            return;
        }
        long currentTime = System.currentTimeMillis();
        long expireTimeVar = expireTime;
        if(expireTimeVar != -1 && currentTime > expireTimeVar) {
            timedOut = true;
            doClose();
            throw new ClosedChannelException();
        }
        expireTime = currentTime + idleTimeout;
        XnioExecutor.Key key = handle;
        if (key == null) {
            handle = sink.getWriteThread().executeAfter(timeoutCommand, idleTimeout, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public int write(ByteBuffer src) throws IOException {
        handleIdleTimeout();
        int w = sink.write(src);
        return w;
    }

    @Override
    public long write(ByteBuffer[] srcs, int offset, int length) throws IOException {
        handleIdleTimeout();
        long w = sink.write(srcs, offset, length);
        return w;
    }

    @Override
    public int writeFinal(ByteBuffer src) throws IOException {
        handleIdleTimeout();
        int w = sink.writeFinal(src);
        if(source.isReadShutdown() && !src.hasRemaining()) {
            if(handle != null) {
                handle.remove();
                handle = null;
            }
        }
        return w;
    }

    @Override
    public long writeFinal(ByteBuffer[] srcs, int offset, int length) throws IOException {
        handleIdleTimeout();
        long w = sink.writeFinal(srcs, offset, length);
        if(source.isReadShutdown() && !Buffers.hasRemaining(srcs, offset, length)) {
            if(handle != null) {
                handle.remove();
                handle = null;
            }
        }
        return w;
    }

    @Override
    public long transferTo(long position, long count, FileChannel target) throws IOException {
        handleIdleTimeout();
        long w = source.transferTo(position, count, target);
        if(sink.isWriteShutdown() && w == -1) {
            if(handle != null) {
                handle.remove();
                handle = null;
            }
        }
        return w;
    }

    @Override
    public long transferTo(long count, ByteBuffer throughBuffer, StreamSinkChannel target) throws IOException {
        handleIdleTimeout();
        long w = source.transferTo(count, throughBuffer, target);
        if(sink.isWriteShutdown() && w == -1) {
            if(handle != null) {
                handle.remove();
                handle = null;
            }
        }
        return w;
    }

    @Override
    public long read(ByteBuffer[] dsts, int offset, int length) throws IOException {
        handleIdleTimeout();
        long r = source.read(dsts, offset, length);
        if(sink.isWriteShutdown() && r == -1) {
            if(handle != null) {
                handle.remove();
                handle = null;
            }
        }
        return r;
    }

    @Override
    public int read(ByteBuffer dst) throws IOException {
        handleIdleTimeout();
        int r = source.read(dst);
        if(sink.isWriteShutdown() && r == -1) {
            if(handle != null) {
                handle.remove();
                handle = null;
            }
        }
        return r;
    }

    @Override
    public long transferFrom(FileChannel src, long position, long count) throws IOException {
        handleIdleTimeout();
        long r = sink.transferFrom(src, position, count);
        return r;
    }

    @Override
    public long transferFrom(StreamSourceChannel source, long count, ByteBuffer throughBuffer) throws IOException {
        handleIdleTimeout();
        long r = sink.transferFrom(source, count, throughBuffer);

        return r;
    }

    @Override
    public void suspendReads() {
        source.suspendReads();
    }

    @Override
    public void terminateReads() throws IOException {
        source.terminateReads();
        if(sink.isWriteShutdown()) {
            if(handle != null) {
                handle.remove();
                handle = null;
            }
        }
    }

    @Override
    public boolean isReadShutdown() {
        return source.isReadShutdown();
    }

    @Override
    public void resumeReads() {
        source.resumeReads();
    }

    @Override
    public boolean isReadResumed() {
        return source.isReadResumed();
    }

    @Override
    public void wakeupReads() {
        source.wakeupReads();
    }
    @Override
    public void awaitReadable() throws IOException {
        source.awaitReadable();
    }

    @Override
    public void awaitReadable(long time, TimeUnit timeUnit) throws IOException {
        source.awaitReadable(time, timeUnit);
    }

    @Override
    public XnioIoThread getReadThread() {
        return source.getReadThread();
    }

    @Override
    public void setReadReadyHandler(ReadReadyHandler handler) {
        this.readReadyHandler = handler;
        source.setReadReadyHandler(handler);
    }

    private static void safeClose(final StreamSourceConduit sink) {
        try {
            sink.terminateReads();
        } catch (IOException e) {
        }
    }

    private static void safeClose(final StreamSinkConduit sink) {
        try {
            sink.truncateWrites();
        } catch (IOException e) {
        }
    }

    @Override
    public void terminateWrites() throws IOException {
        sink.terminateWrites();
        if(source.isReadShutdown()) {
            if(handle != null) {
                handle.remove();
                handle = null;
            }
        }
    }

    @Override
    public boolean isWriteShutdown() {
        return sink.isWriteShutdown();
    }

    @Override
    public void resumeWrites() {
        sink.resumeWrites();
    }

    @Override
    public void suspendWrites() {
        sink.suspendWrites();
    }

    @Override
    public void wakeupWrites() {
        sink.wakeupWrites();
    }

    @Override
    public boolean isWriteResumed() {
        return sink.isWriteResumed();
    }

    @Override
    public void awaitWritable() throws IOException {
        sink.awaitWritable();
    }

    @Override
    public void awaitWritable(long time, TimeUnit timeUnit) throws IOException {
        sink.awaitWritable();
    }

    @Override
    public XnioIoThread getWriteThread() {
        return sink.getWriteThread();
    }

    @Override
    public void setWriteReadyHandler(WriteReadyHandler handler) {
        this.writeReadyHandler = handler;
        sink.setWriteReadyHandler(handler);
    }

    @Override
    public void truncateWrites() throws IOException {
        sink.truncateWrites();
        if(source.isReadShutdown()) {
            if(handle != null) {
                handle.remove();
                handle = null;
            }
        }
    }


    @Override
    public boolean flush() throws IOException {
        return sink.flush();
    }

    @Override
    public XnioWorker getWorker() {
        return sink.getWorker();
    }

    public long getIdleTimeout() {
        return idleTimeout;
    }

    public void setIdleTimeout(long idleTimeout) {
        this.idleTimeout = idleTimeout;
        if(idleTimeout > 0) {
            expireTime = System.currentTimeMillis() + idleTimeout;
        } else {
            expireTime = -1;
        }
        if (idleTimeout > 0 && handle == null) {
            handle = sink.getWriteThread().executeAfter(timeoutCommand, idleTimeout + DELTA, TimeUnit.MILLISECONDS);
        }
    }
}
