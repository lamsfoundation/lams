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


package org.apache.tomcat.util.buf;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.UnsupportedCharsetException;

/**
 * NIO based character decoder.
 * 
 * @author Remy Maucherat
 */
public class B2CConverter {

    protected static org.jboss.logging.Logger log =
        org.jboss.logging.Logger.getLogger(B2CConverter.class);

    protected CharsetDecoder decoder = null;
    protected ByteBuffer bb = null;
    protected CharBuffer cb = null;

    /**
     * Leftover buffer used for incomplete characters.
     */
    protected ByteBuffer leftovers = null;

    /**
     * Create a decoder for the specified charset.
     */
    public B2CConverter(String charset)
        throws IOException {
        try {
            decoder = Charset.forName(charset).newDecoder();
        } catch (UnsupportedCharsetException e) {
            throw new UnsupportedEncodingException(charset);
        }
        byte[] left = new byte[4];
        leftovers = ByteBuffer.wrap(left);
    }

    /**
     * Reset the decoder state, and empty the leftover buffer.
     */
    public void recycle() {
        decoder.reset();
        leftovers.position(0);
    }

    /**
     * Convert the given bytes to characters.
     * 
     * @param bc byte input
     * @param cc char output
     */
    public void convert(ByteChunk bc, CharChunk cc) 
        throws IOException {
        if ((bb == null) || (bb.array() != bc.getBuffer())) {
            // Create a new byte buffer if anything changed
            bb = ByteBuffer.wrap(bc.getBuffer(), bc.getStart(), bc.getLength());
        } else {
            // Initialize the byte buffer
            bb.position(bc.getStart());
            bb.limit(bc.getEnd());
        }
        if ((cb == null) || (cb.array() != cc.getBuffer())) {
            // Create a new char buffer if anything changed
            cb = CharBuffer.wrap(cc.getBuffer(), cc.getEnd(), 
                    cc.getBuffer().length - cc.getEnd());
        } else {
            // Initialize the char buffer
            cb.position(cc.getEnd());
            cb.limit(cc.getBuffer().length);
        }
        CoderResult result = null;
        // Parse leftover if any are present
        if (leftovers.position() > 0) {
            int pos = cb.position();
            // Loop until one char is decoded or there is a decoder error
            do {
                leftovers.put(bc.substractB());
                leftovers.flip();
                result = decoder.decode(leftovers, cb, false);
                leftovers.position(leftovers.limit());
                leftovers.limit(leftovers.array().length);
            } while (result.isUnderflow() && (cb.position() == pos));
            if (result.isError() || result.isMalformed()) {
                result.throwException();
            }
            bb.position(bc.getStart());
            leftovers.position(0);
        }
        // Do the decoding and get the results into the byte chunk and the char chunk
        result = decoder.decode(bb, cb, false);
        if (result.isError() || result.isMalformed()) {
            result.throwException();
        } else if (result.isOverflow()) {
            // Propagate current positions to the byte chunk and char chunk, if this
            // continues the char buffer will get resized
            bc.setOffset(bb.position());
            cc.setEnd(cb.position());
        } else if (result.isUnderflow()) {
            // Propagate current positions to the byte chunk and char chunk
            bc.setOffset(bb.position());
            cc.setEnd(cb.position());
            // Put leftovers in the leftovers byte buffer
            if (bc.getLength() > 0) {
                leftovers.position(bc.getLength());
                leftovers.limit(leftovers.array().length);
                bc.substract(leftovers.array(), 0, bc.getLength());
            }
        }
    }

}
