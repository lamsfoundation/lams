/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jasper.runtime;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;

import org.apache.jasper.Constants;

/**
 * Write text to a character-output stream, buffering characters so as
 * to provide for the efficient writing of single characters, arrays,
 * and strings. 
 *
 * Provide support for discarding for the output that has been buffered. 
 *
 * @author Brian Remmington
 */
public class BodyContentImpl extends BodyContent {

    private static final String LINE_SEPARATOR = 
        System.getProperty("line.separator");

    private boolean closed;
    private CharBuffer buffer;

    // Enclosed writer to which any output is written
    private Writer writer;

    /**
     * Constructor.
     */
    public BodyContentImpl(JspWriter enclosingWriter) {
        super(enclosingWriter);
        buffer = new CharBuffer(Constants.DEFAULT_TAG_BUFFER_SIZE);
        closed = false;
    }

    public void write(int i) throws IOException {
        if (writer != null) {
            writer.write(i);
        } else {
            ensureOpen();
            buffer.buffer((char) i);
        }
    }

    public void write(char ac[], int i, int j) throws IOException {
        if (writer != null) {
            writer.write(ac, i, j);
        } 
        else {
            ensureOpen();
            buffer.buffer(ac, i, j);
        }
    }

    public void write(char ac[]) throws IOException {
        if (writer != null) {
            writer.write(ac);
        }
        else {
            write(ac, 0, ac.length);
        }
    }

    public void write(String s, int i, int j) throws IOException {
        if (writer != null) {
            writer.write(s, i, j);
        } 
        else {
            ensureOpen();
            buffer.buffer(s, i, j);
        }
    }

    public void write(String s) throws IOException {
        if (writer != null) {
            writer.write(s);
        }
        else {
            write(s, 0, s.length());
        }
    }

    public void newLine() throws IOException {
        if (writer != null) {
            writer.write(LINE_SEPARATOR);
        }
        else {
            write(LINE_SEPARATOR);
        }
    }

    public void print(boolean flag) throws IOException {
        if (writer != null) {
            writer.write(flag ? "true" : "false");
        }
        else {
            write(flag ? "true" : "false");
        }
    }

    public void print(char c) throws IOException {
        if (writer != null) {
            writer.write(String.valueOf(c));
        }
        else {
            write(String.valueOf(c));
        }
    }

    public void print(int i) throws IOException {
        if (writer != null) {
            writer.write(String.valueOf(i));
        }
        else {
            write(String.valueOf(i));
        }
    }

    public void print(long l) throws IOException {
        if (writer != null) {
            writer.write(String.valueOf(l));
        } 
        else {
            write(String.valueOf(l));
        }
    }

    public void print(float f) throws IOException {
        if (writer != null) {
            writer.write(String.valueOf(f));
        }
        else {
            write(String.valueOf(f));
        }
    }

    public void print(double d) throws IOException {
        if (writer != null) {
            writer.write(String.valueOf(d));
        }
        else {
            write(String.valueOf(d));
        }
    }

    public void print(char ac[]) throws IOException {
        if (writer != null) {
            writer.write(ac);
        }
        else {
            write(ac);
        }
    }

    public void print(String s) throws IOException {
        if (s == null) {
            s = "null";
        }
        if (writer != null) {
            writer.write(s);
        }
        else {
            write(s);
        }
    }

    public void print(Object obj) throws IOException {
        if (writer != null)
            writer.write(String.valueOf(obj));
        else
            write(String.valueOf(obj));
    }

    public void println() throws IOException {
        newLine();
    }

    public void println(boolean flag) throws IOException {
        print(flag);
        println();
    }

    public void println(char c) throws IOException {
        print(c);
        println();
    }

    public void println(int i) throws IOException {
        print(i);
        println();
    }

    public void println(long l) throws IOException {
        print(l);
        println();
    }

    public void println(float f) throws IOException {
        print(f);
        println();
    }

    public void println(double d) throws IOException {
        print(d);
        println();
    }

    public void println(char ac[]) throws IOException {
        print(ac);
        println();
    }

    public void println(String s) throws IOException {
        print(s);
        println();
    }

    public void println(Object obj) throws IOException {
        print(obj);
        println();
    }

    public void clear() throws IOException {
        if (writer != null) {
            throw new IOException();
        } 
        else {
            buffer.clear();
            return;
        }
    }

    public void clearBuffer() throws IOException {
        if (writer == null) {
            clear();
        }
    }

    public void close() throws IOException {
        if (writer != null) {
            writer.close();
        } 
        else {
            buffer = null;
            closed = true;
        }
    }

    public int getRemaining() {
        return writer != null ? 0 : buffer.getCapacity();
    }

    public Reader getReader() {
        return writer != null ? null : new CharArrayReader(buffer.toArray());
    }

    public String getString() {
        return writer != null ? null : buffer.toString();
    }

    public void writeOut(Writer writer) throws IOException {
        if (this.writer == null) {
            buffer.writeOut(writer);
        }
        // Flush not called as the writer passed could be a BodyContent and
        // it doesn't allow to flush.
    }

    public int getBufferSize() {
        // According to the spec, the JspWriter returned by 
        // JspContext.pushBody(java.io.Writer writer) must behave as
        // though it were unbuffered. This means that its getBufferSize()
        // must always return 0. The base implementation of
        // JspWriter.getBufferSize() returns the value of JspWriter's
        // 'bufferSize' field. Override that method here to provide the correct
        // behaviour.
        return (this.writer == null) ? buffer.size() + buffer.getCapacity() : 0;
    }

    void setWriter(Writer writer) {
        this.writer = writer;
        closed = false;
        if (writer == null) {
            clearBody();
        }
    }

    private void ensureOpen() throws IOException {
        if (closed) {
            throw new IOException("Stream closed");
        }
        else {
            return;
        }
    }
}
