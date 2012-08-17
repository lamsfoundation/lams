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

package org.apache.tomcat.util.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

import org.apache.tomcat.util.buf.ByteChunk;
import org.apache.tomcat.util.buf.CharChunk;
import org.apache.tomcat.util.buf.MessageBytes;
import org.apache.tomcat.util.buf.UDecoder;

/**
 * 
 * @author Costin Manolache
 * @author Remy Maucherat
 */
public final class Parameters {

    protected static org.jboss.logging.Logger log = org.jboss.logging.Logger
            .getLogger(Parameters.class);

    protected static final int NEED_NEXT = -2;
    protected static final int LAST = -1;
    public static final int INITIAL_SIZE = 8;
    protected static final String[] ARRAY_TYPE = new String[0];

    protected class Field {
        MessageBytes name = MessageBytes.newInstance();
        MessageBytes value = MessageBytes.newInstance();

        // Extra info for speed

        // multiple fields with same name - a linked list will
        // speed up multiple name enumerations and search.
        int nextPos;

        // hashkey
        int hash;
        Field nextSameHash;

        Field() {
            nextPos = NEED_NEXT;
        }

        void recycle() {
            name.recycle();
            value.recycle();
            nextPos = NEED_NEXT;
        }
    }

    /**
     * Enumerate the distinct header names. Each nextElement() is O(n) ( a
     * comparation is done with all previous elements ).
     * 
     * This is less frequesnt than add() - we want to keep add O(1).
     */
    protected class NamesEnumeration implements Enumeration {
        int pos;
        String next;

        // toString and unique options are not implemented -
        // we allways to toString and unique.

        /**
         * Create a new multi-map enumeration.
         * 
         * @param headers
         *            the collection to enumerate
         * @param toString
         *            convert each name to string
         * @param unique
         *            return only unique names
         */
        public NamesEnumeration() {
            pos = 0;
            findNext();
        }

        private void findNext() {
            next = null;
            for (; pos < count; pos++) {
                next = getName(pos).toString();
                for (int j = 0; j < pos; j++) {
                    if (getName(j).equals(next)) {
                        // duplicate.
                        next = null;
                        break;
                    }
                }
                if (next != null) {
                    // it's not a duplicate
                    break;
                }
            }
            // next time findNext is called it will try the
            // next element
            pos++;
        }

        public boolean hasMoreElements() {
            return next != null;
        }

        public Object nextElement() {
            String current = next;
            findNext();
            return current;
        }
    }

    protected Field[] fields;
    // fields in use
    protected int count;

    protected boolean didQueryParameters = false;
    protected boolean didMerge = false;

    protected MessageBytes queryMB;

    protected UDecoder urlDec;
    protected MessageBytes decodedQuery = MessageBytes.newInstance();

    protected String encoding = null;
    protected String queryStringEncoding = null;

    /**
     * 
     */
    public Parameters() {
        fields = new Field[INITIAL_SIZE];
    }

    public void setQuery(MessageBytes queryMB) {
        this.queryMB = queryMB;
    }

    public void setHeaders(MimeHeaders headers) {
        // Not used anymore at the moment
    }

    public void setEncoding(String s) {
        encoding = s;
    }

    public void setURLDecoder(UDecoder u) {
        urlDec = u;
    }

    public void setQueryStringEncoding(String s) {
        queryStringEncoding = s;
    }

    public void recycle() {
        for (int i = 0; i < count; i++) {
            fields[i].recycle();
        }
        count = 0;
        didQueryParameters = false;
        didMerge = false;
        encoding = null;
        decodedQuery.recycle();
    }

    /**
     * Returns the current number of header fields.
     */
    protected int size() {
        return count;
    }

    /**
     * Returns the Nth header name This may be used to iterate through all
     * header fields.
     * 
     * An exception is thrown if the index is not valid ( <0 or >size )
     */
    protected MessageBytes getName(int n) {
        // n >= 0 && n < count ? headers[n].getName() : null
        return fields[n].name;
    }

    /**
     * Returns the Nth header value This may be used to iterate through all
     * header fields.
     */
    protected MessageBytes getValue(int n) {
        return fields[n].value;
    }

    /**
     * Create a new, unitialized entry.
     */
    protected int addField() {
        int len = fields.length;
        int pos = count;
        if (count >= len) {
            // expand header list array
            Field tmp[] = new Field[pos * 2];
            System.arraycopy(fields, 0, tmp, 0, len);
            fields = tmp;
        }
        if (fields[pos] == null) {
            fields[pos] = new Field();
        }
        count++;
        return pos;
    }

    protected int findFirst(String name) {
        for (int i = 0; i < count; i++) {
            if (fields[i].name.equals(name)) {
                return i;
            }
        }
        return -1;
    }

    protected int findNext(int startPos) {
        int next = fields[startPos].nextPos;
        if (next != NEED_NEXT) {
            return next;
        }

        // next==NEED_NEXT, we never searched for this header
        MessageBytes name = fields[startPos].name;
        for (int i = (startPos + 1); i < count; i++) {
            if (fields[i].name.equals(name)) {
                // cache the search result
                fields[startPos].nextPos = i;
                return i;
            }
        }
        fields[startPos].nextPos = LAST;
        return -1;
    }

    // -------------------- Data access --------------------
    // Access to the current name/values, no side effect ( processing ).
    // You must explicitely call handleQueryParameters and the post methods.

    // This is the original data representation ( hash of String->String[])

    public void addParameterValues(String name, String[] values) {
        if (name == null || values == null) {
            return;
        }
        for (int i = 0; i < values.length; i++) {
            String value = values[i];
            int pos = addField();
            getName(pos).setString(name);
            getValue(pos).setString(value);
        }
    }

    public String[] getParameterValues(String name) {
        handleQueryParameters();
        int pos = findFirst(name);
        if (pos >= 0) {
            ArrayList<String> result = new ArrayList<String>();
            while (pos >= 0) {
                result.add(getValue(pos).toString());
                pos = findNext(pos);
            }
            return result.toArray(ARRAY_TYPE);
        } else {
            return null;
        }
    }

    public Enumeration getParameterNames() {
        handleQueryParameters();
        return new NamesEnumeration();
    }

    // Shortcut.
    public String getParameter(String name) {
        handleQueryParameters();
        int pos = findFirst(name);
        if (pos >= 0) {
            return getValue(pos).toString();
        } else {
            return null;
        }
    }

    // -------------------- Processing --------------------
    /** Process the query string into parameters
     */
    public void handleQueryParameters() {
        if (didQueryParameters)
            return;

        didQueryParameters = true;

        if (queryMB == null || queryMB.isNull())
            return;

        if (debug > 0)
            log("Decoding query " + decodedQuery + " " + queryStringEncoding);

        try {
            decodedQuery.duplicate(queryMB);
        } catch (IOException e) {
            // Can't happen, as decodedQuery can't overflow
            e.printStackTrace();
        }
        processParameters(decodedQuery, queryStringEncoding);
    }

    protected void addParam(String name, String value) {
        if (name == null) {
            return;
        }
        int pos = addField();
        getName(pos).setString(name);
        getValue(pos).setString(value);
    }

    // -------------------- Parameter parsing --------------------

    // we are called from a single thread - we can do it the hard way
    // if needed
    protected ByteChunk tmpName = new ByteChunk();
    protected ByteChunk tmpValue = new ByteChunk();
    protected CharChunk tmpNameC = new CharChunk(32);
    protected CharChunk tmpValueC = new CharChunk(128);

    public void processParameters(MessageBytes data) {
        processParameters(data, encoding);
    }

    public void processParameters(MessageBytes data, String encoding) {
        if (data == null || data.isNull() || data.getLength() <= 0)
            return;

        if (data.getType() != MessageBytes.T_BYTES) {
            data.toBytes();
        }
        ByteChunk bc = data.getByteChunk();
        processParameters(bc.getBytes(), bc.getOffset(), bc.getLength(),
                encoding);
    }

    public void processParameters(byte bytes[], int start, int len) {
        processParameters(bytes, start, len, encoding);
    }

    public void processParameters(byte bytes[], int start, int len, String enc) {
        int end = start + len;
        int pos = start;

        if (debug > 0)
            log("Bytes: " + new String(bytes, start, len));

        do {
            boolean noEq = false;
            int valStart = -1;
            int valEnd = -1;

            int nameStart = pos;
            int nameEnd = ByteChunk.indexOf(bytes, nameStart, end, '=');
            // Workaround for a&b&c encoding
            int nameEnd2 = ByteChunk.indexOf(bytes, nameStart, end, '&');
            if ((nameEnd2 != -1) && (nameEnd == -1 || nameEnd > nameEnd2)) {
                nameEnd = nameEnd2;
                noEq = true;
                valStart = nameEnd;
                valEnd = nameEnd;
                if (debug > 0)
                    log("no equal " + nameStart + " " + nameEnd + " "
                            + new String(bytes, nameStart, nameEnd - nameStart));
            }
            if (nameEnd == -1)
                nameEnd = end;

            if (!noEq) {
                valStart = (nameEnd < end) ? nameEnd + 1 : end;
                valEnd = ByteChunk.indexOf(bytes, valStart, end, '&');
                if (valEnd == -1)
                    valEnd = (valStart < end) ? end : valStart;
            }

            pos = valEnd + 1;

            if (nameEnd <= nameStart) {
                log.warn("Parameters: Invalid chunk ignored.");
                continue;
                // invalid chunk - it's better to ignore
            }
            tmpName.setBytes(bytes, nameStart, nameEnd - nameStart);
            tmpValue.setBytes(bytes, valStart, valEnd - valStart);

            try {
                addParam(urlDecode(tmpName, enc), urlDecode(tmpValue, enc));
            } catch (IOException e) {
                // Exception during character decoding: skip parameter
                log.warn("Parameters: Character decoding failed. "
                        + "Parameter skipped.", e);
            }

            tmpName.recycle();
            tmpValue.recycle();

        } while (pos < end);
    }

    protected String urlDecode(ByteChunk bc, String enc) throws IOException {
        if (urlDec == null) {
            urlDec = new UDecoder();
        }
        urlDec.convert(bc);
        String result = null;
        if (enc != null) {
            bc.setEncoding(enc);
            result = bc.toString();
        } else {
            CharChunk cc = tmpNameC;
            int length = bc.getLength();
            cc.allocate(length, -1);
            // Default encoding: fast conversion
            byte[] bbuf = bc.getBuffer();
            char[] cbuf = cc.getBuffer();
            int start = bc.getStart();
            for (int i = 0; i < length; i++) {
                cbuf[i] = (char) (bbuf[i + start] & 0xff);
            }
            cc.setChars(cbuf, 0, length);
            result = cc.toString();
            cc.recycle();
        }
        return result;
    }

    /** Debug purpose
     */
    public String paramsAsString() {
        StringBuffer sb = new StringBuffer();
        Enumeration en = getParameterNames();
        while (en.hasMoreElements()) {
            String k = (String) en.nextElement();
            sb.append(k).append("=");
            String v[] = (String[]) getParameterValues(k);
            for (int i = 0; i < v.length; i++)
                sb.append(v[i]).append(",");
            sb.append("\n");
        }
        return sb.toString();
    }

    private static int debug = 0;

    private void log(String s) {
        if (log.isDebugEnabled())
            log.debug("Parameters: " + s);
    }

}
