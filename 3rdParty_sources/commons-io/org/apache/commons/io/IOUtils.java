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
package org.apache.commons.io;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.CharArrayWriter;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.io.output.StringBuilderWriter;

/**
 * General IO stream manipulation utilities.
 * <p>
 * This class provides static utility methods for input/output operations.
 * <ul>
 * <li>closeQuietly - these methods close a stream ignoring nulls and exceptions
 * <li>toXxx/read - these methods read data from a stream
 * <li>write - these methods write data to a stream
 * <li>copy - these methods copy all the data from one stream to another
 * <li>contentEquals - these methods compare the content of two streams
 * </ul>
 * <p>
 * The byte-to-char methods and char-to-byte methods involve a conversion step.
 * Two methods are provided in each case, one that uses the platform default
 * encoding and the other which allows you to specify an encoding. You are
 * encouraged to always specify an encoding because relying on the platform
 * default can lead to unexpected results, for example when moving from
 * development to production.
 * <p>
 * All the methods in this class that read a stream are buffered internally.
 * This means that there is no cause to use a <code>BufferedInputStream</code>
 * or <code>BufferedReader</code>. The default buffer size of 4K has been shown
 * to be efficient in tests.
 * <p>
 * Wherever possible, the methods in this class do <em>not</em> flush or close
 * the stream. This is to avoid making non-portable assumptions about the
 * streams' origin and further use. Thus the caller is still responsible for
 * closing streams after use.
 * <p>
 * Origin of code: Excalibur.
 *
 * @author Peter Donald
 * @author Jeff Turner
 * @author Matthew Hawthorne
 * @author Stephen Colebourne
 * @author Gareth Davis
 * @author Ian Springer
 * @author Sandy McArthur
 *
 */
public class IOUtils {
    // NOTE: This class is focussed on InputStream, OutputStream, Reader and
    // Writer. Each method should take at least one of these as a parameter,
    // or return one of them.

    /**
     * The Unix directory separator character.
     */
    public static final char DIR_SEPARATOR_UNIX = '/';
    /**
     * The Windows directory separator character.
     */
    public static final char DIR_SEPARATOR_WINDOWS = '\\';
    /**
     * The system directory separator character.
     */
    public static final char DIR_SEPARATOR = File.separatorChar;
    /**
     * The Unix line separator string.
     */
    public static final String LINE_SEPARATOR_UNIX = "\n";
    /**
     * The Windows line separator string.
     */
    public static final String LINE_SEPARATOR_WINDOWS = "\r\n";
    /**
     * The system line separator string.
     */
    public static final String LINE_SEPARATOR;
    static {
        // avoid security issues
        StringBuilderWriter buf = new StringBuilderWriter(4);
        PrintWriter out = new PrintWriter(buf);
        out.println();
        LINE_SEPARATOR = buf.toString();
        out.close();
    }

    /**
     * The default buffer size to use for 
     * {@link #copyLarge(InputStream, OutputStream)}
     * and
     * {@link #copyLarge(Reader, Writer)}
     */
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    /**
     * The default buffer size to use for the skip() methods.
     */
    private static final int SKIP_BUFFER_SIZE = 2048;
    
    // Allocated in the skip method if necessary.
    private static char[] SKIP_CHAR_BUFFER;
    private static byte[] SKIP_BYTE_BUFFER;

    /**
     * Instances should NOT be constructed in standard programming.
     */
    public IOUtils() {
        super();
    }

    //-----------------------------------------------------------------------
    /**
     * Unconditionally close an <code>Reader</code>.
     * <p>
     * Equivalent to {@link Reader#close()}, except any exceptions will be ignored.
     * This is typically used in finally blocks.
     * <p>
     * Example code:
     * <pre>
     *   char[] data = new char[1024];
     *   Reader in = null;
     *   try {
     *       in = new FileReader("foo.txt");
     *       in.read(data);
     *       in.close(); //close errors are handled
     *   } catch (Exception e) {
     *       // error handling
     *   } finally {
     *       IOUtils.closeQuietly(in);
     *   }
     * </pre>
     * 
     * @param input  the Reader to close, may be null or already closed
     */
    public static void closeQuietly(Reader input) {
        closeQuietly((Closeable)input);
    }

    /**
     * Unconditionally close a <code>Writer</code>.
     * <p>
     * Equivalent to {@link Writer#close()}, except any exceptions will be ignored.
     * This is typically used in finally blocks.
     * <p>
     * Example code:
     * <pre>
     *   Writer out = null;
     *   try {
     *       out = new StringWriter();
     *       out.write("Hello World");
     *       out.close(); //close errors are handled
     *   } catch (Exception e) {
     *       // error handling
     *   } finally {
     *       IOUtils.closeQuietly(out);
     *   }
     * </pre>
     *
     * @param output  the Writer to close, may be null or already closed
     */
    public static void closeQuietly(Writer output) {
        closeQuietly((Closeable)output);
    }

    /**
     * Unconditionally close an <code>InputStream</code>.
     * <p>
     * Equivalent to {@link InputStream#close()}, except any exceptions will be ignored.
     * This is typically used in finally blocks.
     * <p>
     * Example code:
     * <pre>
     *   byte[] data = new byte[1024];
     *   InputStream in = null;
     *   try {
     *       in = new FileInputStream("foo.txt");
     *       in.read(data);
     *       in.close(); //close errors are handled
     *   } catch (Exception e) {
     *       // error handling
     *   } finally {
     *       IOUtils.closeQuietly(in);
     *   }
     * </pre>
     *
     * @param input  the InputStream to close, may be null or already closed
     */
    public static void closeQuietly(InputStream input) {
        closeQuietly((Closeable)input);
    }

    /**
     * Unconditionally close an <code>OutputStream</code>.
     * <p>
     * Equivalent to {@link OutputStream#close()}, except any exceptions will be ignored.
     * This is typically used in finally blocks.
     * <p>
     * Example code:
     * <pre>
     * byte[] data = "Hello, World".getBytes();
     *
     * OutputStream out = null;
     * try {
     *     out = new FileOutputStream("foo.txt");
     *     out.write(data);
     *     out.close(); //close errors are handled
     * } catch (IOException e) {
     *     // error handling
     * } finally {
     *     IOUtils.closeQuietly(out);
     * }
     * </pre>
     *
     * @param output  the OutputStream to close, may be null or already closed
     */
    public static void closeQuietly(OutputStream output) {
        closeQuietly((Closeable)output);
    }
    
    /**
     * Unconditionally close a <code>Closeable</code>.
     * <p>
     * Equivalent to {@link Closeable#close()}, except any exceptions will be ignored.
     * This is typically used in finally blocks.
     * <p>
     * Example code:
     * <pre>
     *   Closeable closeable = null;
     *   try {
     *       closeable = new FileReader("foo.txt");
     *       // process closeable
     *       closeable.close();
     *   } catch (Exception e) {
     *       // error handling
     *   } finally {
     *       IOUtils.closeQuietly(closeable);
     *   }
     * </pre>
     *
     * @param closeable the object to close, may be null or already closed
     * @since Commons IO 2.0
     */
    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }

    /**
     * Unconditionally close a <code>Socket</code>.
     * <p>
     * Equivalent to {@link Socket#close()}, except any exceptions will be ignored.
     * This is typically used in finally blocks.
     * <p>
     * Example code:
     * <pre>
     *   Socket socket = null;
     *   try {
     *       socket = new Socket("http://www.foo.com/", 80);
     *       // process socket
     *       socket.close();
     *   } catch (Exception e) {
     *       // error handling
     *   } finally {
     *       IOUtils.closeQuietly(socket);
     *   }
     * </pre>
     *
     * @param sock the Socket to close, may be null or already closed
     * @since Commons IO 2.0
     */
    public static void closeQuietly(Socket sock){
        if (sock != null){
            try {
                sock.close();
            } catch (IOException ioe) {
                // ignored
            }
        }
    }

    /**
     * Fetches entire contents of an <code>InputStream</code> and represent
     * same data as result InputStream.
     * <p>
     * This method is useful where,
     * <ul>
     * <li>Source InputStream is slow.</li>
     * <li>It has network resources associated, so we cannot keep it open for
     * long time.</li>
     * <li>It has network timeout associated.</li>
     * </ul>
     * It can be used in favor of {@link #toByteArray(InputStream)}, since it
     * avoids unnecessary allocation and copy of byte[].<br>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     * 
     * @param input Stream to be fully buffered.
     * @return A fully buffered stream.
     * @throws IOException if an I/O error occurs
     * @since Commons IO 2.0
     */
    public static InputStream toBufferedInputStream(InputStream input) throws IOException {
        return ByteArrayOutputStream.toBufferedInputStream(input);
    }

    // read toByteArray
    //-----------------------------------------------------------------------
    /**
     * Get the contents of an <code>InputStream</code> as a <code>byte[]</code>.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     * 
     * @param input  the <code>InputStream</code> to read from
     * @return the requested byte array
     * @throws NullPointerException if the input is null
     * @throws IOException if an I/O error occurs
     */
    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output);
        return output.toByteArray();
    }

    /**
     * Get contents of an <code>InputStream</code> as a <code>byte[]</code>.
     * Use this method instead of <code>toByteArray(InputStream)</code>
     * when <code>InputStream</code> size is known.
     * <b>NOTE:</b> the method checks that the length can safely be cast to an int without truncation
     * before using {@link IOUtils#toByteArray(java.io.InputStream, int)} to read into the byte array.
     * (Arrays can have no more than Integer.MAX_VALUE entries anyway)
     * 
     * @param input the <code>InputStream</code> to read from
     * @param size the size of <code>InputStream</code>
     * @return the requested byte array
     * @throws IOException if an I/O error occurs or <code>InputStream</code> size differ from parameter size
     * @throws IllegalArgumentException if size is less than zero or size is greater than Integer.MAX_VALUE
     * @see IOUtils#toByteArray(java.io.InputStream, int)
     * @since Commons IO 2.1
     */
    public static byte[] toByteArray(InputStream input, long size) throws IOException {

      if(size > Integer.MAX_VALUE) {
          throw new IllegalArgumentException("Size cannot be greater than Integer max value: " + size);
      }

      return toByteArray(input, (int) size);
    }

    /** 
     * Get the contents of an <code>InputStream</code> as a <code>byte[]</code>.
     * Use this method instead of <code>toByteArray(InputStream)</code>
     * when <code>InputStream</code> size is known
     * @param input the <code>InputStream</code> to read from
     * @param size the size of <code>InputStream</code>
     * @return the requested byte array
     * @throws IOException if an I/O error occurs or <code>InputStream</code> size differ from parameter size
     * @throws IllegalArgumentException if size is less than zero
     * @since Commons IO 2.1
     */
    public static byte[] toByteArray(InputStream input, int size) throws IOException {

        if(size < 0) {
            throw new IllegalArgumentException("Size must be equal or greater than zero: " + size);
        }
        
        if(size == 0) {
            return new byte[0];
        }

        byte[] data = new byte[size];
        int offset = 0;
        int readed;

        while(offset < size && (readed = input.read(data, offset, (size - offset))) != -1) {
            offset += readed;
        }

        if(offset != size) {
            throw new IOException("Unexpected readed size. current: " + offset + ", excepted: " + size);
        }

        return data;
    }

    /**
     * Get the contents of a <code>Reader</code> as a <code>byte[]</code>
     * using the default character encoding of the platform.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedReader</code>.
     * 
     * @param input  the <code>Reader</code> to read from
     * @return the requested byte array
     * @throws NullPointerException if the input is null
     * @throws IOException if an I/O error occurs
     */
    public static byte[] toByteArray(Reader input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output);
        return output.toByteArray();
    }

    /**
     * Get the contents of a <code>Reader</code> as a <code>byte[]</code>
     * using the specified character encoding.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedReader</code>.
     * 
     * @param input  the <code>Reader</code> to read from
     * @param encoding  the encoding to use, null means platform default
     * @return the requested byte array
     * @throws NullPointerException if the input is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 1.1
     */
    public static byte[] toByteArray(Reader input, String encoding)
            throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output, encoding);
        return output.toByteArray();
    }

    /**
     * Get the contents of a <code>String</code> as a <code>byte[]</code>
     * using the default character encoding of the platform.
     * <p>
     * This is the same as {@link String#getBytes()}.
     * 
     * @param input  the <code>String</code> to convert
     * @return the requested byte array
     * @throws NullPointerException if the input is null
     * @throws IOException if an I/O error occurs (never occurs)
     * @deprecated Use {@link String#getBytes()}
     */
    @Deprecated
    public static byte[] toByteArray(String input) throws IOException {
        return input.getBytes();
    }

    // read char[]
    //-----------------------------------------------------------------------
    /**
     * Get the contents of an <code>InputStream</code> as a character array
     * using the default character encoding of the platform.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     * 
     * @param is  the <code>InputStream</code> to read from
     * @return the requested character array
     * @throws NullPointerException if the input is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 1.1
     */
    public static char[] toCharArray(InputStream is) throws IOException {
        CharArrayWriter output = new CharArrayWriter();
        copy(is, output);
        return output.toCharArray();
    }

    /**
     * Get the contents of an <code>InputStream</code> as a character array
     * using the specified character encoding.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     * 
     * @param is  the <code>InputStream</code> to read from
     * @param encoding  the encoding to use, null means platform default
     * @return the requested character array
     * @throws NullPointerException if the input is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 1.1
     */
    public static char[] toCharArray(InputStream is, String encoding)
            throws IOException {
        CharArrayWriter output = new CharArrayWriter();
        copy(is, output, encoding);
        return output.toCharArray();
    }

    /**
     * Get the contents of a <code>Reader</code> as a character array.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedReader</code>.
     * 
     * @param input  the <code>Reader</code> to read from
     * @return the requested character array
     * @throws NullPointerException if the input is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 1.1
     */
    public static char[] toCharArray(Reader input) throws IOException {
        CharArrayWriter sw = new CharArrayWriter();
        copy(input, sw);
        return sw.toCharArray();
    }

    // read toString
    //-----------------------------------------------------------------------
    /**
     * Get the contents of an <code>InputStream</code> as a String
     * using the default character encoding of the platform.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     * 
     * @param input  the <code>InputStream</code> to read from
     * @return the requested String
     * @throws NullPointerException if the input is null
     * @throws IOException if an I/O error occurs
     */
    public static String toString(InputStream input) throws IOException {
        return toString(input, null);
    }

    /**
     * Get the contents of an <code>InputStream</code> as a String
     * using the specified character encoding.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     * 
     * @param input  the <code>InputStream</code> to read from
     * @param encoding  the encoding to use, null means platform default
     * @return the requested String
     * @throws NullPointerException if the input is null
     * @throws IOException if an I/O error occurs
     */
    public static String toString(InputStream input, String encoding)
            throws IOException {
        StringBuilderWriter sw = new StringBuilderWriter();
        copy(input, sw, encoding);
        return sw.toString();
    }

    /**
     * Get the contents of a <code>Reader</code> as a String.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedReader</code>.
     * 
     * @param input  the <code>Reader</code> to read from
     * @return the requested String
     * @throws NullPointerException if the input is null
     * @throws IOException if an I/O error occurs
     */
    public static String toString(Reader input) throws IOException {
        StringBuilderWriter sw = new StringBuilderWriter();
        copy(input, sw);
        return sw.toString();
    }

    /**
     * Gets the contents at the given URI.
     * 
     * @param uri
     *            The URI source.
     * @return The contents of the URL as a String.
     * @throws IOException if an I/O exception occurs.
     * @since 2.1.
     */
    public static String toString(URI uri) throws IOException {
        return toString(uri, null);
    }

    /**
     * Gets the contents at the given URI.
     * 
     * @param uri
     *            The URI source.
     * @param encoding
     *            The encoding name for the URL contents.
     * @return The contents of the URL as a String.
     * @throws IOException if an I/O exception occurs.
     * @since 2.1.
     */
    public static String toString(URI uri, String encoding) throws IOException {
        return toString(uri.toURL(), encoding);
    }

    /**
     * Gets the contents at the given URL.
     * 
     * @param url
     *            The URL source.
     * @return The contents of the URL as a String.
     * @throws IOException if an I/O exception occurs.
     * @since 2.1.
     */
    public static String toString(URL url) throws IOException {
        return toString(url, null);
    }

    /**
     * Gets the contents at the given URL.
     * 
     * @param url
     *            The URL source.
     * @param encoding
     *            The encoding name for the URL contents.
     * @return The contents of the URL as a String.
     * @throws IOException if an I/O exception occurs.
     * @since 2.1.
     */
    public static String toString(URL url, String encoding) throws IOException {
        InputStream inputStream = url.openStream();
        try {
            return toString(inputStream, encoding);
        } finally {
            inputStream.close();
        }
    }

    /**
     * Get the contents of a <code>byte[]</code> as a String
     * using the default character encoding of the platform.
     * 
     * @param input the byte array to read from
     * @return the requested String
     * @throws NullPointerException if the input is null
     * @throws IOException if an I/O error occurs (never occurs)
     * @deprecated Use {@link String#String(byte[])}
     */
    @Deprecated
    public static String toString(byte[] input) throws IOException {
        return new String(input);
    }

    /**
     * Get the contents of a <code>byte[]</code> as a String
     * using the specified character encoding.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     * 
     * @param input the byte array to read from
     * @param encoding  the encoding to use, null means platform default
     * @return the requested String
     * @throws NullPointerException if the input is null
     * @throws IOException if an I/O error occurs (never occurs)
     * @deprecated Use {@link String#String(byte[],String)}
     */
    @Deprecated
    public static String toString(byte[] input, String encoding)
            throws IOException {
        if (encoding == null) {
            return new String(input);
        } else {
            return new String(input, encoding);
        }
    }

    // readLines
    //-----------------------------------------------------------------------
    /**
     * Get the contents of an <code>InputStream</code> as a list of Strings,
     * one entry per line, using the default character encoding of the platform.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     *
     * @param input  the <code>InputStream</code> to read from, not null
     * @return the list of Strings, never null
     * @throws NullPointerException if the input is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 1.1
     */
    public static List<String> readLines(InputStream input) throws IOException {
        InputStreamReader reader = new InputStreamReader(input);
        return readLines(reader);
    }

    /**
     * Get the contents of an <code>InputStream</code> as a list of Strings,
     * one entry per line, using the specified character encoding.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     *
     * @param input  the <code>InputStream</code> to read from, not null
     * @param encoding  the encoding to use, null means platform default
     * @return the list of Strings, never null
     * @throws NullPointerException if the input is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 1.1
     */
    public static List<String> readLines(InputStream input, String encoding) throws IOException {
        if (encoding == null) {
            return readLines(input);
        } else {
            InputStreamReader reader = new InputStreamReader(input, encoding);
            return readLines(reader);
        }
    }

    /**
     * Get the contents of a <code>Reader</code> as a list of Strings,
     * one entry per line.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedReader</code>.
     *
     * @param input  the <code>Reader</code> to read from, not null
     * @return the list of Strings, never null
     * @throws NullPointerException if the input is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 1.1
     */
    public static List<String> readLines(Reader input) throws IOException {
        BufferedReader reader = new BufferedReader(input);
        List<String> list = new ArrayList<String>();
        String line = reader.readLine();
        while (line != null) {
            list.add(line);
            line = reader.readLine();
        }
        return list;
    }

    // lineIterator
    //-----------------------------------------------------------------------
    /**
     * Return an Iterator for the lines in a <code>Reader</code>.
     * <p>
     * <code>LineIterator</code> holds a reference to the open
     * <code>Reader</code> specified here. When you have finished with the
     * iterator you should close the reader to free internal resources.
     * This can be done by closing the reader directly, or by calling
     * {@link LineIterator#close()} or {@link LineIterator#closeQuietly(LineIterator)}.
     * <p>
     * The recommended usage pattern is:
     * <pre>
     * try {
     *   LineIterator it = IOUtils.lineIterator(reader);
     *   while (it.hasNext()) {
     *     String line = it.nextLine();
     *     /// do something with line
     *   }
     * } finally {
     *   IOUtils.closeQuietly(reader);
     * }
     * </pre>
     *
     * @param reader  the <code>Reader</code> to read from, not null
     * @return an Iterator of the lines in the reader, never null
     * @throws IllegalArgumentException if the reader is null
     * @since Commons IO 1.2
     */
    public static LineIterator lineIterator(Reader reader) {
        return new LineIterator(reader);
    }

    /**
     * Return an Iterator for the lines in an <code>InputStream</code>, using
     * the character encoding specified (or default encoding if null).
     * <p>
     * <code>LineIterator</code> holds a reference to the open
     * <code>InputStream</code> specified here. When you have finished with
     * the iterator you should close the stream to free internal resources.
     * This can be done by closing the stream directly, or by calling
     * {@link LineIterator#close()} or {@link LineIterator#closeQuietly(LineIterator)}.
     * <p>
     * The recommended usage pattern is:
     * <pre>
     * try {
     *   LineIterator it = IOUtils.lineIterator(stream, "UTF-8");
     *   while (it.hasNext()) {
     *     String line = it.nextLine();
     *     /// do something with line
     *   }
     * } finally {
     *   IOUtils.closeQuietly(stream);
     * }
     * </pre>
     *
     * @param input  the <code>InputStream</code> to read from, not null
     * @param encoding  the encoding to use, null means platform default
     * @return an Iterator of the lines in the reader, never null
     * @throws IllegalArgumentException if the input is null
     * @throws IOException if an I/O error occurs, such as if the encoding is invalid
     * @since Commons IO 1.2
     */
    public static LineIterator lineIterator(InputStream input, String encoding) 
                     throws IOException {
        Reader reader = null;
        if (encoding == null) {
            reader = new InputStreamReader(input);
        } else {
            reader = new InputStreamReader(input, encoding);
        }
        return new LineIterator(reader);
    }

    //-----------------------------------------------------------------------
    /**
     * Convert the specified CharSequence to an input stream, encoded as bytes
     * using the default character encoding of the platform.
     *
     * @param input the CharSequence to convert
     * @return an input stream
     * @since Commons IO 2.0
     */
    public static InputStream toInputStream(CharSequence input) {
        return toInputStream(input.toString());
    }

    /**
     * Convert the specified CharSequence to an input stream, encoded as bytes
     * using the specified character encoding.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     *
     * @param input the CharSequence to convert
     * @param encoding the encoding to use, null means platform default
     * @throws IOException if the encoding is invalid
     * @return an input stream
     * @since Commons IO 2.0
     */
    public static InputStream toInputStream(CharSequence input, String encoding) throws IOException {
        return toInputStream(input.toString(), encoding);
    }

    //-----------------------------------------------------------------------
    /**
     * Convert the specified string to an input stream, encoded as bytes
     * using the default character encoding of the platform.
     *
     * @param input the string to convert
     * @return an input stream
     * @since Commons IO 1.1
     */
    public static InputStream toInputStream(String input) {
        byte[] bytes = input.getBytes();
        return new ByteArrayInputStream(bytes);
    }

    /**
     * Convert the specified string to an input stream, encoded as bytes
     * using the specified character encoding.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     *
     * @param input the string to convert
     * @param encoding the encoding to use, null means platform default
     * @throws IOException if the encoding is invalid
     * @return an input stream
     * @since Commons IO 1.1
     */
    public static InputStream toInputStream(String input, String encoding) throws IOException {
        byte[] bytes = encoding != null ? input.getBytes(encoding) : input.getBytes();
        return new ByteArrayInputStream(bytes);
    }

    // write byte[]
    //-----------------------------------------------------------------------
    /**
     * Writes bytes from a <code>byte[]</code> to an <code>OutputStream</code>.
     * 
     * @param data  the byte array to write, do not modify during output,
     * null ignored
     * @param output  the <code>OutputStream</code> to write to
     * @throws NullPointerException if output is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 1.1
     */
    public static void write(byte[] data, OutputStream output)
            throws IOException {
        if (data != null) {
            output.write(data);
        }
    }

    /**
     * Writes bytes from a <code>byte[]</code> to chars on a <code>Writer</code>
     * using the default character encoding of the platform.
     * <p>
     * This method uses {@link String#String(byte[])}.
     * 
     * @param data  the byte array to write, do not modify during output,
     * null ignored
     * @param output  the <code>Writer</code> to write to
     * @throws NullPointerException if output is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 1.1
     */
    public static void write(byte[] data, Writer output) throws IOException {
        if (data != null) {
            output.write(new String(data));
        }
    }

    /**
     * Writes bytes from a <code>byte[]</code> to chars on a <code>Writer</code>
     * using the specified character encoding.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     * <p>
     * This method uses {@link String#String(byte[], String)}.
     * 
     * @param data  the byte array to write, do not modify during output,
     * null ignored
     * @param output  the <code>Writer</code> to write to
     * @param encoding  the encoding to use, null means platform default
     * @throws NullPointerException if output is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 1.1
     */
    public static void write(byte[] data, Writer output, String encoding)
            throws IOException {
        if (data != null) {
            if (encoding == null) {
                write(data, output);
            } else {
                output.write(new String(data, encoding));
            }
        }
    }

    // write char[]
    //-----------------------------------------------------------------------
    /**
     * Writes chars from a <code>char[]</code> to a <code>Writer</code>
     * using the default character encoding of the platform.
     * 
     * @param data  the char array to write, do not modify during output,
     * null ignored
     * @param output  the <code>Writer</code> to write to
     * @throws NullPointerException if output is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 1.1
     */
    public static void write(char[] data, Writer output) throws IOException {
        if (data != null) {
            output.write(data);
        }
    }

    /**
     * Writes chars from a <code>char[]</code> to bytes on an
     * <code>OutputStream</code>.
     * <p>
     * This method uses {@link String#String(char[])} and
     * {@link String#getBytes()}.
     * 
     * @param data  the char array to write, do not modify during output,
     * null ignored
     * @param output  the <code>OutputStream</code> to write to
     * @throws NullPointerException if output is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 1.1
     */
    public static void write(char[] data, OutputStream output)
            throws IOException {
        if (data != null) {
            output.write(new String(data).getBytes());
        }
    }

    /**
     * Writes chars from a <code>char[]</code> to bytes on an
     * <code>OutputStream</code> using the specified character encoding.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     * <p>
     * This method uses {@link String#String(char[])} and
     * {@link String#getBytes(String)}.
     * 
     * @param data  the char array to write, do not modify during output,
     * null ignored
     * @param output  the <code>OutputStream</code> to write to
     * @param encoding  the encoding to use, null means platform default
     * @throws NullPointerException if output is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 1.1
     */
    public static void write(char[] data, OutputStream output, String encoding)
            throws IOException {
        if (data != null) {
            if (encoding == null) {
                write(data, output);
            } else {
                output.write(new String(data).getBytes(encoding));
            }
        }
    }

    // write CharSequence
    //-----------------------------------------------------------------------
    /**
     * Writes chars from a <code>CharSequence</code> to a <code>Writer</code>.
     * 
     * @param data  the <code>CharSequence</code> to write, null ignored
     * @param output  the <code>Writer</code> to write to
     * @throws NullPointerException if output is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 2.0
     */
    public static void write(CharSequence data, Writer output) throws IOException {
        if (data != null) {
            write(data.toString(), output);
        }
    }

    /**
     * Writes chars from a <code>CharSequence</code> to bytes on an
     * <code>OutputStream</code> using the default character encoding of the
     * platform.
     * <p>
     * This method uses {@link String#getBytes()}.
     * 
     * @param data  the <code>CharSequence</code> to write, null ignored
     * @param output  the <code>OutputStream</code> to write to
     * @throws NullPointerException if output is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 2.0
     */
    public static void write(CharSequence data, OutputStream output)
            throws IOException {
        if (data != null) {
            write(data.toString(), output);
        }
    }

    /**
     * Writes chars from a <code>CharSequence</code> to bytes on an
     * <code>OutputStream</code> using the specified character encoding.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     * <p>
     * This method uses {@link String#getBytes(String)}.
     * 
     * @param data  the <code>CharSequence</code> to write, null ignored
     * @param output  the <code>OutputStream</code> to write to
     * @param encoding  the encoding to use, null means platform default
     * @throws NullPointerException if output is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 2.0
     */
    public static void write(CharSequence data, OutputStream output, String encoding)
            throws IOException {
        if (data != null) {
            write(data.toString(), output, encoding);
        }
    }

    // write String
    //-----------------------------------------------------------------------
    /**
     * Writes chars from a <code>String</code> to a <code>Writer</code>.
     * 
     * @param data  the <code>String</code> to write, null ignored
     * @param output  the <code>Writer</code> to write to
     * @throws NullPointerException if output is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 1.1
     */
    public static void write(String data, Writer output) throws IOException {
        if (data != null) {
            output.write(data);
        }
    }

    /**
     * Writes chars from a <code>String</code> to bytes on an
     * <code>OutputStream</code> using the default character encoding of the
     * platform.
     * <p>
     * This method uses {@link String#getBytes()}.
     * 
     * @param data  the <code>String</code> to write, null ignored
     * @param output  the <code>OutputStream</code> to write to
     * @throws NullPointerException if output is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 1.1
     */
    public static void write(String data, OutputStream output)
            throws IOException {
        if (data != null) {
            output.write(data.getBytes());
        }
    }

    /**
     * Writes chars from a <code>String</code> to bytes on an
     * <code>OutputStream</code> using the specified character encoding.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     * <p>
     * This method uses {@link String#getBytes(String)}.
     * 
     * @param data  the <code>String</code> to write, null ignored
     * @param output  the <code>OutputStream</code> to write to
     * @param encoding  the encoding to use, null means platform default
     * @throws NullPointerException if output is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 1.1
     */
    public static void write(String data, OutputStream output, String encoding)
            throws IOException {
        if (data != null) {
            if (encoding == null) {
                write(data, output);
            } else {
                output.write(data.getBytes(encoding));
            }
        }
    }

    // write StringBuffer
    //-----------------------------------------------------------------------
    /**
     * Writes chars from a <code>StringBuffer</code> to a <code>Writer</code>.
     * 
     * @param data  the <code>StringBuffer</code> to write, null ignored
     * @param output  the <code>Writer</code> to write to
     * @throws NullPointerException if output is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 1.1
     * @deprecated replaced by write(CharSequence, Writer)
     */
    @Deprecated
    public static void write(StringBuffer data, Writer output)
            throws IOException {
        if (data != null) {
            output.write(data.toString());
        }
    }

    /**
     * Writes chars from a <code>StringBuffer</code> to bytes on an
     * <code>OutputStream</code> using the default character encoding of the
     * platform.
     * <p>
     * This method uses {@link String#getBytes()}.
     * 
     * @param data  the <code>StringBuffer</code> to write, null ignored
     * @param output  the <code>OutputStream</code> to write to
     * @throws NullPointerException if output is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 1.1
     * @deprecated replaced by write(CharSequence, OutputStream)
     */
    @Deprecated
    public static void write(StringBuffer data, OutputStream output)
            throws IOException {
        if (data != null) {
            output.write(data.toString().getBytes());
        }
    }

    /**
     * Writes chars from a <code>StringBuffer</code> to bytes on an
     * <code>OutputStream</code> using the specified character encoding.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     * <p>
     * This method uses {@link String#getBytes(String)}.
     * 
     * @param data  the <code>StringBuffer</code> to write, null ignored
     * @param output  the <code>OutputStream</code> to write to
     * @param encoding  the encoding to use, null means platform default
     * @throws NullPointerException if output is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 1.1
     * @deprecated replaced by write(CharSequence, OutputStream, String)
     */
    @Deprecated
    public static void write(StringBuffer data, OutputStream output,
            String encoding) throws IOException {
        if (data != null) {
            if (encoding == null) {
                write(data, output);
            } else {
                output.write(data.toString().getBytes(encoding));
            }
        }
    }

    // writeLines
    //-----------------------------------------------------------------------
    /**
     * Writes the <code>toString()</code> value of each item in a collection to
     * an <code>OutputStream</code> line by line, using the default character
     * encoding of the platform and the specified line ending.
     *
     * @param lines  the lines to write, null entries produce blank lines
     * @param lineEnding  the line separator to use, null is system default
     * @param output  the <code>OutputStream</code> to write to, not null, not closed
     * @throws NullPointerException if the output is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 1.1
     */
    public static void writeLines(Collection<?> lines, String lineEnding,
            OutputStream output) throws IOException {
        if (lines == null) {
            return;
        }
        if (lineEnding == null) {
            lineEnding = LINE_SEPARATOR;
        }
        for (Object line : lines) {
            if (line != null) {
                output.write(line.toString().getBytes());
            }
            output.write(lineEnding.getBytes());
        }
    }

    /**
     * Writes the <code>toString()</code> value of each item in a collection to
     * an <code>OutputStream</code> line by line, using the specified character
     * encoding and the specified line ending.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     *
     * @param lines  the lines to write, null entries produce blank lines
     * @param lineEnding  the line separator to use, null is system default
     * @param output  the <code>OutputStream</code> to write to, not null, not closed
     * @param encoding  the encoding to use, null means platform default
     * @throws NullPointerException if the output is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 1.1
     */
    public static void writeLines(Collection<?> lines, String lineEnding,
            OutputStream output, String encoding) throws IOException {
        if (encoding == null) {
            writeLines(lines, lineEnding, output);
        } else {
            if (lines == null) {
                return;
            }
            if (lineEnding == null) {
                lineEnding = LINE_SEPARATOR;
            }
            for (Object line : lines) {
                if (line != null) {
                    output.write(line.toString().getBytes(encoding));
                }
                output.write(lineEnding.getBytes(encoding));
            }
        }
    }

    /**
     * Writes the <code>toString()</code> value of each item in a collection to
     * a <code>Writer</code> line by line, using the specified line ending.
     *
     * @param lines  the lines to write, null entries produce blank lines
     * @param lineEnding  the line separator to use, null is system default
     * @param writer  the <code>Writer</code> to write to, not null, not closed
     * @throws NullPointerException if the input is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 1.1
     */
    public static void writeLines(Collection<?> lines, String lineEnding,
            Writer writer) throws IOException {
        if (lines == null) {
            return;
        }
        if (lineEnding == null) {
            lineEnding = LINE_SEPARATOR;
        }
        for (Object line : lines) {
            if (line != null) {
                writer.write(line.toString());
            }
            writer.write(lineEnding);
        }
    }

    // copy from InputStream
    //-----------------------------------------------------------------------
    /**
     * Copy bytes from an <code>InputStream</code> to an
     * <code>OutputStream</code>.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     * <p>
     * Large streams (over 2GB) will return a bytes copied value of
     * <code>-1</code> after the copy has completed since the correct
     * number of bytes cannot be returned as an int. For large streams
     * use the <code>copyLarge(InputStream, OutputStream)</code> method.
     * 
     * @param input  the <code>InputStream</code> to read from
     * @param output  the <code>OutputStream</code> to write to
     * @return the number of bytes copied, or -1 if &gt; Integer.MAX_VALUE
     * @throws NullPointerException if the input or output is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 1.1
     */
    public static int copy(InputStream input, OutputStream output) throws IOException {
        long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

    /**
     * Copy bytes from a large (over 2GB) <code>InputStream</code> to an
     * <code>OutputStream</code>.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     * 
     * @param input  the <code>InputStream</code> to read from
     * @param output  the <code>OutputStream</code> to write to
     * @return the number of bytes copied
     * @throws NullPointerException if the input or output is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 1.3
     */
    public static long copyLarge(InputStream input, OutputStream output)
            throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    /**
     * Copy bytes from an <code>InputStream</code> to chars on a
     * <code>Writer</code> using the default character encoding of the platform.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     * <p>
     * This method uses {@link InputStreamReader}.
     *
     * @param input  the <code>InputStream</code> to read from
     * @param output  the <code>Writer</code> to write to
     * @throws NullPointerException if the input or output is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 1.1
     */
    public static void copy(InputStream input, Writer output)
            throws IOException {
        InputStreamReader in = new InputStreamReader(input);
        copy(in, output);
    }

    /**
     * Copy bytes from an <code>InputStream</code> to chars on a
     * <code>Writer</code> using the specified character encoding.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     * <p>
     * This method uses {@link InputStreamReader}.
     *
     * @param input  the <code>InputStream</code> to read from
     * @param output  the <code>Writer</code> to write to
     * @param encoding  the encoding to use, null means platform default
     * @throws NullPointerException if the input or output is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 1.1
     */
    public static void copy(InputStream input, Writer output, String encoding)
            throws IOException {
        if (encoding == null) {
            copy(input, output);
        } else {
            InputStreamReader in = new InputStreamReader(input, encoding);
            copy(in, output);
        }
    }

    // copy from Reader
    //-----------------------------------------------------------------------
    /**
     * Copy chars from a <code>Reader</code> to a <code>Writer</code>.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedReader</code>.
     * <p>
     * Large streams (over 2GB) will return a chars copied value of
     * <code>-1</code> after the copy has completed since the correct
     * number of chars cannot be returned as an int. For large streams
     * use the <code>copyLarge(Reader, Writer)</code> method.
     *
     * @param input  the <code>Reader</code> to read from
     * @param output  the <code>Writer</code> to write to
     * @return the number of characters copied, or -1 if &gt; Integer.MAX_VALUE
     * @throws NullPointerException if the input or output is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 1.1
     */
    public static int copy(Reader input, Writer output) throws IOException {
        long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

    /**
     * Copy chars from a large (over 2GB) <code>Reader</code> to a <code>Writer</code>.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedReader</code>.
     *
     * @param input  the <code>Reader</code> to read from
     * @param output  the <code>Writer</code> to write to
     * @return the number of characters copied
     * @throws NullPointerException if the input or output is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 1.3
     */
    public static long copyLarge(Reader input, Writer output) throws IOException {
        char[] buffer = new char[DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    /**
     * Copy chars from a <code>Reader</code> to bytes on an
     * <code>OutputStream</code> using the default character encoding of the
     * platform, and calling flush.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedReader</code>.
     * <p>
     * Due to the implementation of OutputStreamWriter, this method performs a
     * flush.
     * <p>
     * This method uses {@link OutputStreamWriter}.
     *
     * @param input  the <code>Reader</code> to read from
     * @param output  the <code>OutputStream</code> to write to
     * @throws NullPointerException if the input or output is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 1.1
     */
    public static void copy(Reader input, OutputStream output)
            throws IOException {
        OutputStreamWriter out = new OutputStreamWriter(output);
        copy(input, out);
        // XXX Unless anyone is planning on rewriting OutputStreamWriter, we
        // have to flush here.
        out.flush();
    }

    /**
     * Copy chars from a <code>Reader</code> to bytes on an
     * <code>OutputStream</code> using the specified character encoding, and
     * calling flush.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedReader</code>.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     * <p>
     * Due to the implementation of OutputStreamWriter, this method performs a
     * flush.
     * <p>
     * This method uses {@link OutputStreamWriter}.
     *
     * @param input  the <code>Reader</code> to read from
     * @param output  the <code>OutputStream</code> to write to
     * @param encoding  the encoding to use, null means platform default
     * @throws NullPointerException if the input or output is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 1.1
     */
    public static void copy(Reader input, OutputStream output, String encoding)
            throws IOException {
        if (encoding == null) {
            copy(input, output);
        } else {
            OutputStreamWriter out = new OutputStreamWriter(output, encoding);
            copy(input, out);
            // XXX Unless anyone is planning on rewriting OutputStreamWriter,
            // we have to flush here.
            out.flush();
        }
    }

    // content equals
    //-----------------------------------------------------------------------
    /**
     * Compare the contents of two Streams to determine if they are equal or
     * not.
     * <p>
     * This method buffers the input internally using
     * <code>BufferedInputStream</code> if they are not already buffered.
     *
     * @param input1  the first stream
     * @param input2  the second stream
     * @return true if the content of the streams are equal or they both don't
     * exist, false otherwise
     * @throws NullPointerException if either input is null
     * @throws IOException if an I/O error occurs
     */
    public static boolean contentEquals(InputStream input1, InputStream input2)
            throws IOException {
        if (!(input1 instanceof BufferedInputStream)) {
            input1 = new BufferedInputStream(input1);
        }
        if (!(input2 instanceof BufferedInputStream)) {
            input2 = new BufferedInputStream(input2);
        }

        int ch = input1.read();
        while (-1 != ch) {
            int ch2 = input2.read();
            if (ch != ch2) {
                return false;
            }
            ch = input1.read();
        }

        int ch2 = input2.read();
        return (ch2 == -1);
    }

    /**
     * Compare the contents of two Readers to determine if they are equal or
     * not.
     * <p>
     * This method buffers the input internally using
     * <code>BufferedReader</code> if they are not already buffered.
     *
     * @param input1  the first reader
     * @param input2  the second reader
     * @return true if the content of the readers are equal or they both don't
     * exist, false otherwise
     * @throws NullPointerException if either input is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 1.1
     */
    public static boolean contentEquals(Reader input1, Reader input2)
            throws IOException {
        if (!(input1 instanceof BufferedReader)) {
            input1 = new BufferedReader(input1);
        }
        if (!(input2 instanceof BufferedReader)) {
            input2 = new BufferedReader(input2);
        }

        int ch = input1.read();
        while (-1 != ch) {
            int ch2 = input2.read();
            if (ch != ch2) {
                return false;
            }
            ch = input1.read();
        }

        int ch2 = input2.read();
        return (ch2 == -1);
    }

    /**
     * Skip bytes from an input byte stream.
     * This implementation guarantees that it will read as many bytes
     * as possible before giving up; this may not always be the case for
     * subclasses of {@link Reader}.
     *   
     * @param input byte stream to skip
     * @param toSkip number of bytes to skip.
     * @return number of bytes actually skipped.
     * 
     * @see InputStream#skip(long)
     * 
     * @throws IOException if there is a problem reading the file
     * @throws IllegalArgumentException if toSkip is negative
     * @since Commons IO 2.0
     */
    public static long skip(InputStream input, long toSkip) throws IOException {
        if (toSkip < 0) {
            throw new IllegalArgumentException("Skip count must be non-negative, actual: "+toSkip);
        }
        /*
         * N.B. no need to synchronize this because:
         * - we don't care if the buffer is created multiple times (the data is ignored)
         * - we always use the same size buffer, so if it it is recreated it will still be OK
         * (if the buffer size were variable, we would need to synch. to ensure some other thread
         * did not create a smaller one)
         */
        if (SKIP_BYTE_BUFFER == null){
            SKIP_BYTE_BUFFER = new byte[SKIP_BUFFER_SIZE];
        }
        long remain=toSkip;
        while(remain > 0) {
            long n = input.read(SKIP_BYTE_BUFFER, 0, (int) Math.min(remain, SKIP_BUFFER_SIZE));
            if (n < 0) { // EOF
                break;
            }
            remain -= n;
        }
        return toSkip - remain;   
    }

    /**
     * Skip characters from an input character stream.
     * This implementation guarantees that it will read as many characters
     * as possible before giving up; this may not always be the case for
     * subclasses of {@link Reader}.
     *   
     * @param input character stream to skip
     * @param toSkip number of characters to skip.
     * @return number of characters actually skipped.
     * 
     * @see Reader#skip(long)
     * 
     * @throws IOException if there is a problem reading the file
     * @throws IllegalArgumentException if toSkip is negative
     * @since Commons IO 2.0
     */
    public static long skip(Reader input, long toSkip) throws IOException {
        if (toSkip < 0) {
            throw new IllegalArgumentException("Skip count must be non-negative, actual: "+toSkip);
        }
        /*
         * N.B. no need to synchronize this because:
         * - we don't care if the buffer is created multiple times (the data is ignored)
         * - we always use the same size buffer, so if it it is recreated it will still be OK
         * (if the buffer size were variable, we would need to synch. to ensure some other thread
         * did not create a smaller one)
         */
        if (SKIP_CHAR_BUFFER == null){
            SKIP_CHAR_BUFFER = new char[SKIP_BUFFER_SIZE];
        }
        long remain=toSkip;
        while(remain > 0) {
            long n = input.read(SKIP_CHAR_BUFFER, 0, (int) Math.min(remain, SKIP_BUFFER_SIZE));
            if (n < 0) { // EOF
                break;
            }
            remain -= n;
        }
        return toSkip - remain;   
    }

    /**
     * Skip the requested number of bytes or fail if there are not enough left.
     * <p>
     * This allows for the possibility that {@link InputStream#skip(long)} may
     * not skip as many bytes as requested (most likely because of reaching EOF).
     * 
     * @param input stream to skip
     * @param toSkip the number of bytes to skip
     * @see InputStream#skip(long)
     * 
     * @throws IOException if there is a problem reading the file
     * @throws IllegalArgumentException if toSkip is negative
     * @throws EOFException if the number of bytes skipped was incorrect 
     * @since Commons IO 2.0
     */
    public static void skipFully(InputStream input, long toSkip) throws IOException {
        if (toSkip < 0){
            throw new IllegalArgumentException("Bytes to skip must not be negative: "+toSkip);
        }
        long skipped = skip(input, toSkip);
        if (skipped != toSkip) {
            throw new EOFException("Bytes to skip: "+toSkip+" actual: "+skipped);
        }
    }

    /**
     * Skip the requested number of characters or fail if there are not enough left.
     * <p>
     * This allows for the possibility that {@link Reader#skip(long)} may
     * not skip as many characters as requested (most likely because of reaching EOF).
     * 
     * @param input stream to skip
     * @param toSkip the number of characters to skip
     * @see Reader#skip(long)
     * 
     * @throws IOException if there is a problem reading the file
     * @throws IllegalArgumentException if toSkip is negative
     * @throws EOFException if the number of characters skipped was incorrect
     * @since Commons IO 2.0
     */
    public static void skipFully(Reader input, long toSkip) throws IOException {
        long skipped = skip(input, toSkip);
        if (skipped != toSkip) {
            throw new EOFException("Bytes to skip: "+toSkip+" actual: "+skipped);
        }
    }
}
