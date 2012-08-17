/*
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
 */

package org.apache.coyote.http11;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.apache.tomcat.jni.Socket;
import org.apache.tomcat.util.buf.ByteChunk;
import org.apache.tomcat.util.buf.CharChunk;
import org.apache.tomcat.util.buf.MessageBytes;
import org.apache.tomcat.util.http.HttpMessages;
import org.apache.tomcat.util.http.MimeHeaders;
import org.apache.tomcat.util.net.AprEndpoint;
import org.apache.tomcat.util.res.StringManager;

import org.apache.coyote.ActionCode;
import org.apache.coyote.OutputBuffer;
import org.apache.coyote.Response;

/**
 * Output buffer.
 * 
 * @author <a href="mailto:remm@apache.org">Remy Maucherat</a>
 */
public class InternalAprOutputBuffer 
    implements OutputBuffer {


    // -------------------------------------------------------------- Constants


    // ----------------------------------------------------------- Constructors


    /**
     * Alternate constructor.
     */
    public InternalAprOutputBuffer(Response response, int headerBufferSize, AprEndpoint endpoint) {

        this.response = response;
        this.endpoint = endpoint;
        headers = response.getMimeHeaders();

        buf = new byte[headerBufferSize];
        if (headerBufferSize < (8 * 1024)) {
            bbuf = ByteBuffer.allocateDirect(6 * 1500);
        } else {
            bbuf = ByteBuffer.allocateDirect((headerBufferSize / 1500 + 1) * 1500);
        }

        outputStreamOutputBuffer = new SocketOutputBuffer();

        filterLibrary = new OutputFilter[0];
        activeFilters = new OutputFilter[0];
        lastActiveFilter = -1;

        committed = false;
        finished = false;

        leftover = new ByteChunk();
        nonBlocking = false;
        
        // Cause loading of HttpMessages
        HttpMessages.getMessage(200);

    }


    // -------------------------------------------------------------- Variables


    /**
     * The string manager for this package.
     */
    protected static StringManager sm =
        StringManager.getManager(Constants.Package);


    // ----------------------------------------------------- Instance Variables


    /**
     * Associated Coyote response.
     */
    protected Response response;


    /**
     * Headers of the associated request.
     */
    protected MimeHeaders headers;


    /**
     * Committed flag.
     */
    protected boolean committed;


    /**
     * Finished flag.
     */
    protected boolean finished;


    /**
     * Pointer to the current write buffer.
     */
    protected byte[] buf;


    /**
     * Position in the buffer.
     */
    protected int pos;


    /**
     * Underlying socket.
     */
    protected long socket;


    /**
     * Underlying output buffer.
     */
    protected OutputBuffer outputStreamOutputBuffer;


    /**
     * Filter library.
     * Note: Filter[0] is always the "chunked" filter.
     */
    protected OutputFilter[] filterLibrary;


    /**
     * Active filter (which is actually the top of the pipeline).
     */
    protected OutputFilter[] activeFilters;


    /**
     * Index of the last active filter.
     */
    protected int lastActiveFilter;


    /**
     * Direct byte buffer used for writing.
     */
    protected ByteBuffer bbuf = null;

    
    /**
     * Leftover bytes which could not be written during a non blocking write. 
     */
    protected ByteChunk leftover = null;

    
    /**
     * Non blocking mode.
     */
    protected boolean nonBlocking = false;
    

    /**
     * Apr endpoint.
     */
    protected AprEndpoint endpoint = null;
    

    // ------------------------------------------------------------- Properties


    /**
     * Set the underlying socket.
     */
    public void setSocket(long socket) {
        this.socket = socket;
        Socket.setsbb(this.socket, bbuf);
    }


    /**
     * Get the underlying socket input stream.
     */
    public long getSocket() {
        return socket;
    }


    /**
     * Set the non blocking flag.
     */
    public void setNonBlocking(boolean nonBlocking) {
        this.nonBlocking = nonBlocking;
    }


    /**
     * Get the non blocking flag value.
     */
    public boolean getNonBlocking() {
        return nonBlocking;
    }


    /**
     * Add an output filter to the filter library.
     */
    public void addFilter(OutputFilter filter) {

        OutputFilter[] newFilterLibrary = 
            new OutputFilter[filterLibrary.length + 1];
        for (int i = 0; i < filterLibrary.length; i++) {
            newFilterLibrary[i] = filterLibrary[i];
        }
        newFilterLibrary[filterLibrary.length] = filter;
        filterLibrary = newFilterLibrary;

        activeFilters = new OutputFilter[filterLibrary.length];

    }


    /**
     * Get filters.
     */
    public OutputFilter[] getFilters() {

        return filterLibrary;

    }


    /**
     * Clear filters.
     */
    public void clearFilters() {

        filterLibrary = new OutputFilter[0];
        lastActiveFilter = -1;

    }


    /**
     * Add an output filter to the filter library.
     */
    public void addActiveFilter(OutputFilter filter) {

        if (lastActiveFilter == -1) {
            filter.setBuffer(outputStreamOutputBuffer);
        } else {
            for (int i = 0; i <= lastActiveFilter; i++) {
                if (activeFilters[i] == filter)
                    return;
            }
            filter.setBuffer(activeFilters[lastActiveFilter]);
        }

        activeFilters[++lastActiveFilter] = filter;

        filter.setResponse(response);

    }


    // --------------------------------------------------------- Public Methods


    /**
     * Flush the response.
     * 
     * @throws IOException an undelying I/O error occured
     */
    public void flush()
        throws IOException {

        if (!committed) {

            // Send the connector a request for commit. The connector should
            // then validate the headers, send them (using sendHeader) and 
            // set the filters accordingly.
            response.action(ActionCode.ACTION_COMMIT, null);

        }

        // Flush the current buffer
        flushBuffer();

    }


    /**
     * Recycle the output buffer. This should be called when closing the 
     * connection.
     */
    public void recycle() {

        // Recycle Request object
        response.recycle();
        bbuf.clear();

        socket = 0;
        pos = 0;
        lastActiveFilter = -1;
        committed = false;
        finished = false;

    }


    /**
     * End processing of current HTTP request.
     * Note: All bytes of the current request should have been already 
     * consumed. This method only resets all the pointers so that we are ready
     * to parse the next HTTP request.
     */
    public void nextRequest() {

        // Recycle Request object
        response.recycle();

        // Recycle filters
        for (int i = 0; i <= lastActiveFilter; i++) {
            activeFilters[i].recycle();
        }

        // Reset pointers
        leftover.recycle();
        pos = 0;
        lastActiveFilter = -1;
        committed = false;
        finished = false;
        nonBlocking = false;

    }


    /**
     * End request.
     * 
     * @throws IOException an undelying I/O error occured
     */
    public void endRequest()
        throws IOException {

        if (!committed) {

            // Send the connector a request for commit. The connector should
            // then validate the headers, send them (using sendHeader) and 
            // set the filters accordingly.
            response.action(ActionCode.ACTION_COMMIT, null);

        }

        if (finished)
            return;

        if (lastActiveFilter != -1)
            activeFilters[lastActiveFilter].end();

        flushBuffer();

        finished = true;

    }


    // ------------------------------------------------ HTTP/1.1 Output Methods


    /**
     * Send an acknoledgement.
     */
    public void sendAck()
        throws IOException {

        if (!committed) {
            if (Socket.send(socket, Constants.ACK_BYTES, 0, Constants.ACK_BYTES.length) < 0)
                throw new IOException(sm.getString("oob.failedwrite"));
        }

    }


    /**
     * Send the response status line.
     */
    public void sendStatus() {

        // Write protocol name
        write(Constants.HTTP_11_BYTES);
        buf[pos++] = Constants.SP;

        // Write status code
        int status = response.getStatus();
        switch (status) {
        case 200:
            write(Constants._200_BYTES);
            break;
        case 400:
            write(Constants._400_BYTES);
            break;
        case 404:
            write(Constants._404_BYTES);
            break;
        default:
            write(status);
        }

        buf[pos++] = Constants.SP;

        // Write message
        String message = null;
        if (org.apache.coyote.Constants.USE_CUSTOM_STATUS_MSG_IN_HEADER) {
            message = response.getMessage();
        }
        if (message == null) {
            write(HttpMessages.getMessage(status));
        } else {
            write(message.replace('\n', ' ').replace('\r', ' '));
        }

        // End the response status line
        buf[pos++] = Constants.CR;
        buf[pos++] = Constants.LF;

    }


    /**
     * Send a header.
     * 
     * @param name Header name
     * @param value Header value
     */
    public void sendHeader(MessageBytes name, MessageBytes value) {

        write(name);
        buf[pos++] = Constants.COLON;
        buf[pos++] = Constants.SP;
        write(value);
        buf[pos++] = Constants.CR;
        buf[pos++] = Constants.LF;

    }


    /**
     * Send a header.
     * 
     * @param name Header name
     * @param value Header value
     */
    public void sendHeader(ByteChunk name, ByteChunk value) {

        write(name);
        buf[pos++] = Constants.COLON;
        buf[pos++] = Constants.SP;
        write(value);
        buf[pos++] = Constants.CR;
        buf[pos++] = Constants.LF;

    }


    /**
     * Send a header.
     * 
     * @param name Header name
     * @param value Header value
     */
    public void sendHeader(String name, String value) {

        write(name);
        buf[pos++] = Constants.COLON;
        buf[pos++] = Constants.SP;
        write(value);
        buf[pos++] = Constants.CR;
        buf[pos++] = Constants.LF;

    }


    /**
     * End the header block.
     */
    public void endHeaders() {

        buf[pos++] = Constants.CR;
        buf[pos++] = Constants.LF;

    }


    // --------------------------------------------------- OutputBuffer Methods


    /**
     * Write the contents of a byte chunk.
     * 
     * @param chunk byte chunk
     * @return number of bytes written
     * @throws IOException an undelying I/O error occured
     */
    public int doWrite(ByteChunk chunk, Response res) 
        throws IOException {

        if (!committed) {

            // Send the connector a request for commit. The connector should
            // then validate the headers, send them (using sendHeaders) and 
            // set the filters accordingly.
            response.action(ActionCode.ACTION_COMMIT, null);

        }

        // If non blocking (comet) and there are leftover bytes, 
        // and lastWrite was 0 -> error
        if (leftover.getLength() > 0 && !(Http11AprProcessor.containerThread.get() == Boolean.TRUE)) {
            throw new IOException(sm.getString("oob.backlog"));
        }

        if (lastActiveFilter == -1)
            return outputStreamOutputBuffer.doWrite(chunk, res);
        else
            return activeFilters[lastActiveFilter].doWrite(chunk, res);

    }


    // ------------------------------------------------------ Protected Methods


    /**
     * Commit the response.
     * 
     * @throws IOException an undelying I/O error occured
     */
    protected void commit()
        throws IOException {

        // The response is now committed
        committed = true;
        response.setCommitted(true);

        if (pos > 0) {
            // Sending the response header buffer
            bbuf.put(buf, 0, pos);
        }

    }


    /**
     * This method will write the contents of the specyfied message bytes 
     * buffer to the output stream, without filtering. This method is meant to
     * be used to write the response header.
     * 
     * @param mb data to be written
     */
    protected void write(MessageBytes mb) {

        if (mb.getType() == MessageBytes.T_BYTES) {
            ByteChunk bc = mb.getByteChunk();
            write(bc);
        } else if (mb.getType() == MessageBytes.T_CHARS) {
            CharChunk cc = mb.getCharChunk();
            write(cc);
        } else {
            write(mb.toString());
        }

    }


    /**
     * This method will write the contents of the specyfied message bytes 
     * buffer to the output stream, without filtering. This method is meant to
     * be used to write the response header.
     * 
     * @param bc data to be written
     */
    protected void write(ByteChunk bc) {

        // Writing the byte chunk to the output buffer
        int length = bc.getLength();
        System.arraycopy(bc.getBytes(), bc.getStart(), buf, pos, length);
        pos = pos + length;

    }


    /**
     * This method will write the contents of the specyfied char 
     * buffer to the output stream, without filtering. This method is meant to
     * be used to write the response header.
     * 
     * @param cc data to be written
     */
    protected void write(CharChunk cc) {

        int start = cc.getStart();
        int end = cc.getEnd();
        char[] cbuf = cc.getBuffer();
        for (int i = start; i < end; i++) {
            char c = cbuf[i];
            // Note:  This is clearly incorrect for many strings,
            // but is the only consistent approach within the current
            // servlet framework.  It must suffice until servlet output
            // streams properly encode their output.
            if ((c <= 31) && (c != 9)) {
                c = ' ';
            } else if (c == 127) {
                c = ' ';
            }
            buf[pos++] = (byte) c;
        }

    }


    /**
     * This method will write the contents of the specyfied byte 
     * buffer to the output stream, without filtering. This method is meant to
     * be used to write the response header.
     * 
     * @param b data to be written
     */
    public void write(byte[] b) {

        // Writing the byte chunk to the output buffer
        System.arraycopy(b, 0, buf, pos, b.length);
        pos = pos + b.length;

    }


    /**
     * This method will write the contents of the specyfied String to the 
     * output stream, without filtering. This method is meant to be used to 
     * write the response header.
     * 
     * @param s data to be written
     */
    protected void write(String s) {

        if (s == null)
            return;

        // From the Tomcat 3.3 HTTP/1.0 connector
        int len = s.length();
        for (int i = 0; i < len; i++) {
            char c = s.charAt (i);
            // Note:  This is clearly incorrect for many strings,
            // but is the only consistent approach within the current
            // servlet framework.  It must suffice until servlet output
            // streams properly encode their output.
            if ((c <= 31) && (c != 9)) {
                c = ' ';
            } else if (c == 127) {
                c = ' ';
            }
            buf[pos++] = (byte) c;
        }

    }


    /**
     * This method will print the specified integer to the output stream, 
     * without filtering. This method is meant to be used to write the 
     * response header.
     * 
     * @param i data to be written
     */
    protected void write(int i) {

        write(String.valueOf(i));

    }

    
    /**
     * Flush leftover bytes.
     * 
     * @return true if all leftover bytes have been flushed
     */
    public boolean flushLeftover()
        throws IOException {
        int len = leftover.getLength();
        int start = leftover.getStart();
        byte[] b = leftover.getBuffer();
        
        while (len > 0) {
            int thisTime = len;
            if (bbuf.position() == bbuf.capacity()) {
                int pos = 0;
                int end = bbuf.position();
                int res = 0;
                while (pos < end) {
                    res = Socket.sendibb(socket, pos, bbuf.position());
                    if (res > 0) {
                        pos += res;
                    } else {
                        break;
                    }
                }
                if (res < 0) {
                    throw new IOException(sm.getString("oob.failedwrite"));
                }
                response.setLastWrite(res);
                if (pos < end) {
                    // Could not write all leftover data: put back to write poller
                    leftover.setOffset(start);
                    bbuf.position(pos);
                    return false;
                } else {
                    bbuf.clear();
                }
            }
            if (thisTime > bbuf.capacity() - bbuf.position()) {
                thisTime = bbuf.capacity() - bbuf.position();
            }
            bbuf.put(b, start, thisTime);
            len = len - thisTime;
            start = start + thisTime;
        }
        
        int pos = 0;
        int end = bbuf.position();
        int res = 0;
        while (pos < end) {
            res = Socket.sendibb(socket, pos, bbuf.position());
            if (res > 0) {
                pos += res;
            } else {
                break;
            }
        }
        if (res < 0) {
            throw new IOException(sm.getString("oob.failedwrite"));
        }
        response.setLastWrite(res);
        if (pos < end) {
            leftover.allocate(end - pos, -1);
            bbuf.position(pos);
            bbuf.limit(end);
            bbuf.get(leftover.getBuffer(), 0, end - pos);
            leftover.setEnd(end - pos);
            bbuf.clear();
            return false;
        }
        bbuf.clear();
        leftover.recycle();

        return true;
    }
    

    /**
     * Callback to write data from the buffer.
     */
    protected void flushBuffer()
        throws IOException {

        int res = 0;
        
        // If there are still leftover bytes here, this means the user did a direct flush:
        // - If the call is asynchronous, throw an exception
        // - If the call is synchronous, make regular blocking writes to flush the data
        if (leftover.getLength() > 0) {
            if (Http11AprProcessor.containerThread.get() == Boolean.TRUE) {
                Socket.timeoutSet(socket, endpoint.getSoTimeout() * 1000);
                // Send leftover bytes
                res = Socket.send(socket, leftover.getBuffer(), leftover.getOffset(), leftover.getEnd());
                leftover.recycle();
                // Send current buffer
                if (res > 0 && bbuf.position() > 0) {
                    res = Socket.sendbb(socket, 0, bbuf.position());
                }
                bbuf.clear();
                Socket.timeoutSet(socket, 0);
            } else {
                throw new IOException(sm.getString("oob.backlog"));
            }
        }
        
        if (bbuf.position() > 0) {
            if (nonBlocking) {
                // Perform non blocking writes until all data is written, or the result
                // of the write is 0
                int pos = 0;
                int end = bbuf.position();
                while (pos < end) {
                    res = Socket.sendibb(socket, pos, end);
                    if (res > 0) {
                        pos += res;
                    } else {
                        break;
                    }
                }
                if (pos < end) {
                    if (response.getFlushLeftovers() && (Http11AprProcessor.containerThread.get() == Boolean.TRUE)) {
                        // Switch to blocking mode and write the data
                        Socket.timeoutSet(socket, endpoint.getSoTimeout() * 1000);
                        res = Socket.sendbb(socket, 0, end);
                        Socket.timeoutSet(socket, 0);
                    } else {
                        // Put any leftover bytes in the leftover byte chunk
                        leftover.allocate(end - pos, -1);
                        bbuf.position(pos);
                        bbuf.limit(end);
                        bbuf.get(leftover.getBuffer(), 0, end - pos);
                        leftover.setEnd(end - pos);
                        // Call for a write event because it is possible that no further write
                        // operations are made
                        if (!response.getFlushLeftovers()) {
                            response.action(ActionCode.ACTION_COMET_WRITE, null);
                        }
                    }
                }
            } else {
                res = Socket.sendbb(socket, 0, bbuf.position());
            }
            response.setLastWrite(res);
            bbuf.clear();
            if (res < 0) {
                throw new IOException(sm.getString("oob.failedwrite"));
            }
        }
        
    }


    // ----------------------------------- OutputStreamOutputBuffer Inner Class


    /**
     * This class is an output buffer which will write data to an output
     * stream.
     */
    protected class SocketOutputBuffer 
        implements OutputBuffer {


        /**
         * Write chunk.
         */
        public int doWrite(ByteChunk chunk, Response res) 
            throws IOException {

            // If non blocking (comet) and there are leftover bytes, 
            // put all remaining bytes in the leftover buffer (they are
            // part of the same write operation)
            if (leftover.getLength() > 0) {
                leftover.append(chunk);
                return chunk.getLength();
            }
            
            int len = chunk.getLength();
            int start = chunk.getStart();
            byte[] b = chunk.getBuffer();
            while (len > 0) {
                int thisTime = len;
                if (bbuf.position() == bbuf.capacity()) {
                    flushBuffer();
                    if (leftover.getLength() > 0) {
                        // If non blocking (comet) and there are leftover bytes, 
                        // put all remaining bytes in the leftover buffer (they are
                        // part of the same write operation)
                        int oldStart = chunk.getOffset();
                        chunk.setOffset(start);
                        leftover.append(chunk);
                        chunk.setOffset(oldStart);
                        // After that, all content has been "written"
                        return chunk.getLength();
                    }
                }
                if (thisTime > bbuf.capacity() - bbuf.position()) {
                    thisTime = bbuf.capacity() - bbuf.position();
                }
                bbuf.put(b, start, thisTime);
                len = len - thisTime;
                start = start + thisTime;
            }
            return chunk.getLength();

        }


    }


}
