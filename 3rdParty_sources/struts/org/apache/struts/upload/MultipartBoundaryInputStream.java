/*
 *
 *
 * Copyright 1999-2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.struts.upload;

import java.io.InputStream;
import java.io.IOException;
import java.io.File;

/**
 * This class encapsulates parsing functionality for RFC1867, multipart/form-data. See MultipartBoundaryInputStreamTest
 * and MultipartIterator for usage examples.
 *
 *
 * @deprecated Use the Commons FileUpload based multipart handler instead. This
 *             class will be removed after Struts 1.2.
 */
public class MultipartBoundaryInputStream extends InputStream
{
    private static final byte NEWLINE_BYTE = ((byte) '\n');

    private static final byte CARRIAGE_RETURN = ((byte) '\r');

    private static final byte[] CRLF = new byte[] {CARRIAGE_RETURN, NEWLINE_BYTE};

    private static final String DOUBLE_DASH_STRING = "--";

    private static final int DEFAULT_LINE_SIZE = 4096;

    private static final String TOKEN_EQUALS = "=";

    private static final char TOKEN_QUOTE = '\"';

    private static final char TOKEN_COLON = ':';

    private static final char TOKEN_SEMI_COLON = ';';

    private static final char TOKEN_SPACE = ' ';

    private static final String DEFAULT_CONTENT_DISPOSITION = "form-data";

    private static final String PARAMETER_NAME = "name";

    private static final String PARAMETER_FILENAME = "filename";

    private static final String PARAMETER_CHARSET = "charset";

    private static final String CONTENT_TYPE_TEXT_PLAIN = "text/plain";

    private static final String CONTENT_TYPE_APPLICATION_OCTET_STREAM = "application/octet-stream";

    private static final String MESSAGE_INVALID_START = "Multipart data doesn't start with boundary";

    /**
     * The InputStream to read from.
     */
    protected InputStream inputStream;

    /**
     * The boundary.
     */
    protected String boundary;

    /**
     * Whether or not the boundary has been encountered.
     */
    protected boolean boundaryEncountered;

    /**
     * Whether or not the final boundary has been encountered.
     */
    protected boolean finalBoundaryEncountered;

    /**
     * Whether or not the end of the stream has been read.
     */
    protected boolean endOfStream;

    /**
     * The Content-Disposition for the current form element being read.
     */
    protected String elementContentDisposition;

    /**
     * The name of the current form element being read.
     */
    protected String elementName;

    /**
     * The Content-Type of the current form element being read.
     */
    protected String elementContentType;

    /**
     * The filename of the current form element being read, <code>null</code> if the current form element is
     * text data.
     */
    protected String elementFileName;

    /**
     * The character encoding of the element, specified in the element's Content-Type header.
     */
    protected String elementCharset;

    /**
     * The maximum length in bytes to read from the stream at a time, or -1 for unlimited length.
     */
    protected long maxLength;

    /**
     * Whether or not the maximum length has been met.
     */
    protected boolean maxLengthMet;

    /**
     * The total number of bytes read so far.
     */
    protected long bytesRead;

    private byte[] boundaryBytes;

    private byte[] finalBoundaryBytes;

    private byte[] line;

    private int lineSize;

    private int lineLength;

    private boolean lineHasNewline;

    private boolean lineHasCarriage;

    private int lineIndex;

    public MultipartBoundaryInputStream()
    {
        this.lineSize  = DEFAULT_LINE_SIZE;
        this.maxLength = -1;
        resetStream();
    }

    /**
     * Sets the boundary that terminates the data for the stream, after adding the prefix "--"
     */
    public void setBoundary(String boundary)
    {
        this.boundary           = DOUBLE_DASH_STRING + boundary;
        this.boundaryBytes      = this.boundary.getBytes();
        this.finalBoundaryBytes = (this.boundary + DOUBLE_DASH_STRING).getBytes();
    }

    /**
     * Resets this stream for use with the next element, to be used after a boundary is encountered.
     */
    public void resetForNextBoundary() throws IOException
    {
        if (!this.finalBoundaryEncountered)
        {
            this.boundaryEncountered = false;
            resetCrlf();
            fillLine();
            readElementHeaders();
        }
    }

    /**
     * Sets the input stream used to read multipart data. For efficiency purposes, make sure that the stream
     * you set on this class is buffered. The way this class reads lines is that it continually calls the read()
     * method until it reaches a newline character. That would be terrible if you were to set a socket's input stream
     * here, but not as bad on a buffered stream.
     */
    public void setInputStream(InputStream stream) throws IOException
    {
        this.inputStream = stream;
        resetStream();
        readFirstElement();
    }

    /**
     * Reads from the stream. Returns -1 if it's the end of the stream or if a boundary is encountered.
     */
    public int read() throws IOException
    {
        if (!this.maxLengthMet)
        {
            if (!this.boundaryEncountered)
            {
                return readFromLine();
            }
        }
        return -1;
    }

    public int read(byte[] buffer) throws IOException
    {
        return read(buffer, 0, buffer.length);
    }

    public int read(byte[] buffer, int offset, int length) throws IOException
    {
        if (length > 0)
        {
            int read = read();
            if ((read == -1) && (this.endOfStream || this.boundaryEncountered))
            {
                return -1;
            }
            int bytesRead = 1;
            buffer[offset++] = (byte) read;

            while ((bytesRead < length) && (((read = read())!= -1) || ((read == -1) &&
                    (!this.boundaryEncountered))) && !this.maxLengthMet)
            {
                buffer[offset++] = (byte) read;
                bytesRead++;
            }
            return bytesRead;
        }
        return -1;
    }

    /**
     * Marks the underlying stream.
     */
    public synchronized void mark(int i)
    {
        this.inputStream.mark(i);
    }

    /**
     * Resets the underlying input stream.
     */
    public synchronized void reset() throws IOException
    {
        this.inputStream.reset();
    }

    /**
     * Set the maximum length in bytes to read, or -1 for an unlimited length.
     */
    public void setMaxLength(long maxLength)
    {
        this.maxLength = maxLength;
    }

    public long getMaxLength()
    {
        return maxLength;
    }

    /**
     * Whether or not the maximum length has been met.
     */
    public boolean isMaxLengthMet()
    {
        return maxLengthMet;
    }

    /**
     * Gets the value for the "Content-Dispositio" header for the current multipart element.
     * Usually "form-data".
     */
    public String getElementContentDisposition()
    {
        return this.elementContentDisposition;
    }

    /**
     * Gets the name of the current element. The name corresponds to the value of
     * the "name" attribute of the form element.
     */
    public String getElementName()
    {
        return this.elementName;
    }

    /**
     * Gets the character encoding of the current element. The character encoding would have been specified
     * in the Content-Type header for this element, if it wasn't this is null.
     */
    public String getElementCharset()
    {
        return this.elementCharset;
    }

    /**
     * Gets the "Content-Type" of the current element. If this is a text element,
     * the content type will probably be "text/plain", otherwise it will be the
     * content type of the file element.
     */
    public String getElementContentType()
    {
        return this.elementContentType;
    }

    /**
     * Gets the filename of the current element, which will be null if the current element
     * isn't a file.
     */
    public String getElementFileName()
    {
        return this.elementFileName;
    }

    /**
     * Gets whether or not the current form element being read is a file.
     */
    public boolean isElementFile()
    {
        return (this.elementFileName != null);
    }

    /**
     * Returns whether or not the boundary has been encountered while reading data.
     */
    public boolean isBoundaryEncountered()
    {
        return this.boundaryEncountered;
    }

    /**
     * Returns whether or not the final boundary has been encountered.
     */
    public boolean isFinalBoundaryEncountered()
    {
        return this.finalBoundaryEncountered;
    }

    /**
     * Whether or not an EOF has been read on the stream.
     */
    public boolean isEndOfStream()
    {
        return this.endOfStream;
    }

    public void setLineSize(int size)
    {
        this.lineSize = size;
    }

    public long getBytesRead()
    {
        return this.bytesRead;
    }

    private final void readFirstElement() throws IOException
    {
        fillLine();
        if (!this.boundaryEncountered)
        {
            throw new IOException(MESSAGE_INVALID_START);
        }
        fillLine();
        readElementHeaders();
    }

    private final void readElementHeaders() throws IOException
    {
        readContentDisposition();
        resetCrlf();
        boolean hadContentType = readContentType();
        resetCrlf();
        if (hadContentType)
        {
            skipCurrentLineIfBlank();
        }
    }

    private final void readContentDisposition() throws IOException
    {
        String line = readLine();
        if (line != null)
        {
            int colonIndex = line.indexOf(TOKEN_COLON);
            if (colonIndex != -1)
            {
                int firstSemiColonIndex = line.indexOf(TOKEN_SEMI_COLON);
                if (firstSemiColonIndex != -1)
                {
                    this.elementContentDisposition = line.substring(colonIndex+1, firstSemiColonIndex).trim();
                }
            }
            else
            {
                this.elementContentDisposition = DEFAULT_CONTENT_DISPOSITION;
            }
            this.elementName = parseForParameter(PARAMETER_NAME, line);
            this.elementFileName = parseForParameter(PARAMETER_FILENAME, line);
            //do platform-specific checking
            if (this.elementFileName != null)
            {
                this.elementFileName = checkAndFixFilename(this.elementFileName);
            }
        }
    }

    private final String checkAndFixFilename(String filename)
    {
        filename = new File(filename).getName();

        //check for windows filenames,
        //from linux jdk's the entire filepath
        //isn't parsed correctly from File.getName()
        int colonIndex = filename.indexOf(":");
        if (colonIndex == -1) {
            //check for Window's SMB server file paths
            colonIndex = filename.indexOf("\\\\");
        }
        int slashIndex = filename.lastIndexOf("\\");

        if ((colonIndex > -1) && (slashIndex > -1)) {
            //then consider this filename to be a full
            //windows filepath, and parse it accordingly
            //to retrieve just the file name
            filename = filename.substring(slashIndex+1, filename.length());
        }
        return filename;
    }

    private final String parseForParameter(String parameter, String parseString)
    {
        int nameIndex = parseString.indexOf(parameter + TOKEN_EQUALS);
        if (nameIndex != -1)
        {
            nameIndex += parameter.length() + 1;
            int startIndex = -1;
            int endIndex   = -1;
            if (parseString.charAt(nameIndex) == TOKEN_QUOTE)
            {
                startIndex = nameIndex + 1;
                int endQuoteIndex = parseString.indexOf(TOKEN_QUOTE, startIndex);
                if (endQuoteIndex != -1)
                {
                    endIndex = endQuoteIndex;
                }
            }
            else
            {
                startIndex = nameIndex;
                int spaceIndex = parseString.indexOf(TOKEN_SPACE, startIndex);
                if (spaceIndex != -1)
                {
                    endIndex = spaceIndex;
                }
                else
                {
                    int carriageIndex = parseString.indexOf(CARRIAGE_RETURN, startIndex);
                    if (carriageIndex != -1)
                    {
                        endIndex = carriageIndex;
                    }
                    else
                    {
                        endIndex = parseString.length();
                    }
                }
            }
            if ((startIndex != -1) && (endIndex != -1))
            {
                return parseString.substring(startIndex, endIndex);
            }
        }
        return null;
    }

    private final boolean readContentType() throws IOException
    {
        String line = readLine();
        if (line != null)
        {
            //if it's not a blank line (a blank line has just a CRLF)
            if (line.length() > 2)
            {
                this.elementContentType = parseHeaderValue(line);
                if (this.elementContentType == null)
                {
                    this.elementContentType = CONTENT_TYPE_APPLICATION_OCTET_STREAM;
                }
                this.elementCharset = parseForParameter(PARAMETER_CHARSET, line);
                return true;
            }
            //otherwise go to the default content type
            this.elementContentType = CONTENT_TYPE_TEXT_PLAIN;
        }
        return false;
    }

    private final String parseHeaderValue(String headerLine)
    {
        //get the index of the colon
        int colonIndex = headerLine.indexOf(TOKEN_COLON);
        if (colonIndex != -1)
        {
            int endLineIndex;
            //see if there's a semi colon
            int semiColonIndex = headerLine.indexOf(TOKEN_SEMI_COLON, colonIndex);
            if (semiColonIndex != -1)
            {
                endLineIndex = semiColonIndex;
            }
            else
            {
                //get the index of where the carriage return is, past the colon
                endLineIndex = headerLine.indexOf(CARRIAGE_RETURN, colonIndex);
            }
            if (endLineIndex == -1)
            {
                //make index the last character in the line
                endLineIndex = headerLine.length();
            }
            //and return a substring representing everything after the "headerName: "
            return headerLine.substring(colonIndex+1, endLineIndex).trim();
        }
        return null;
    }

    private final void skipCurrentLineIfBlank() throws IOException
    {
        boolean fill = false;
        if (this.lineLength == 1)
        {
            if (this.line[0] == NEWLINE_BYTE)
            {
                fill = true;
            }
        }
        else if (this.lineLength == 2)
        {
            if (equals(this.line, 0, 2, CRLF))
            {
                fill = true;
            }
        }
        if (fill && !this.endOfStream)
        {
            fillLine();
        }
    }

    private final void resetCrlf()
    {
        this.lineHasCarriage = false;
        this.lineHasNewline  = false;
    }

    private final void resetStream()
    {
        this.line                     = new byte[this.lineSize];
        this.lineIndex                = 0;
        this.lineLength               = 0;
        this.lineHasCarriage          = false;
        this.lineHasNewline           = false;
        this.boundaryEncountered      = false;
        this.finalBoundaryEncountered = false;
        this.endOfStream              = false;
        this.maxLengthMet             = false;
        this.bytesRead                = 0;
    }

    private final String readLine() throws IOException
    {
        String line = null;
        if (availableInLine() > 0)
        {
            line = new String(this.line, 0, this.lineLength);
            if (!this.endOfStream)
            {
                fillLine();
            }
        }
        else
        {
            if (!this.endOfStream)
            {
                fillLine();
                line = readLine();
            }
        }
        return line;
    }

    private final int readFromLine() throws IOException
    {
        if (!this.boundaryEncountered)
        {
            if (availableInLine() > 0)
            {
                return this.line[this.lineIndex++];
            }
            else
            {
                if (!this.endOfStream)
                {
                    fillLine();
                    return readFromLine();
                }
            }
        }
        return -1;
    }

    private final int availableInLine()
    {
        return (this.lineLength - this.lineIndex);
    }

    private final void fillLine() throws IOException
    {
        resetLine();
        if (!this.finalBoundaryEncountered && !this.endOfStream)
        {
            fillLineBuffer();
            checkForBoundary();
        }
    }

    private final void resetLine()
    {
        this.lineIndex = 0;
    }

    private final void fillLineBuffer() throws IOException
    {
        int read = 0;
        int index = 0;

        //take care of any CRLF's from the previous read
        if (this.lineHasCarriage)
        {
            this.line[index++] = CARRIAGE_RETURN;
            this.lineHasCarriage = false;
        }
        if (this.lineHasNewline)
        {
            this.line[index++] = NEWLINE_BYTE;
            this.lineHasNewline = false;
        }
        while ((index < this.line.length) && (!this.maxLengthMet))
        {
            read = this.inputStream.read();
            byteRead();
            if ((read != -1) || ((read == -1) && (this.inputStream.available() > 0)) && !this.maxLengthMet)
            {
                this.line[index++] = (byte) read;
                if (read == NEWLINE_BYTE)
                {
                    //flag the newline, but don't put in buffer
                    this.lineHasNewline= true;
                    index--;
                    if (index > 0)
                    {
                        //flag the carriage return, but don't put in buffer
                        if (this.line[index - 1] == CARRIAGE_RETURN)
                        {
                            this.lineHasCarriage = true;
                            index--;
                        }
                    }
                    break;
                }
            }
            else
            {
                this.endOfStream = true;
                break;
            }
        }
        this.lineLength = index;
    }

    private final void byteRead()
    {
        this.bytesRead++;
        if (this.maxLength > -1)
        {
            if (this.bytesRead >= this.maxLength)
            {
                this.maxLengthMet = true;
                this.endOfStream  = true;
            }
        }
    }

    private final void checkForBoundary()
    {
        this.boundaryEncountered = false;
        int actualLength = this.lineLength;
        int startIndex;
        if ((this.line[0] == CARRIAGE_RETURN) || (this.line[0] == NEWLINE_BYTE))
        {
            actualLength--;
        }
        if (this.line[1] == NEWLINE_BYTE)
        {
            actualLength--;
        }
        startIndex = (this.lineLength - actualLength);
        if (actualLength == this.boundaryBytes.length)
        {
            if (equals(this.line, startIndex, this.boundaryBytes.length, this.boundaryBytes))
            {
                this.boundaryEncountered = true;
            }
        }
        else if (actualLength == (this.boundaryBytes.length + 2))
        {
            if (equals(this.line, startIndex, this.finalBoundaryBytes.length, this.finalBoundaryBytes))
            {
                this.boundaryEncountered = true;
                this.finalBoundaryEncountered = true;
                this.endOfStream = true;
            }
        }
    }

    /**
     * Checks bytes for equality.  Two byte arrays are equal if each of their elements are
     * the same.  This method checks comp[offset] with source[0] to source[length-1] with
     * comp[offset + length - 1]
     * @param comp The byte to compare to <code>source</code>
     * @param offset The offset to start at in <code>comp</code>
     * @param length The length of <code>comp</code> to compare to
     * @param source The reference byte array to test for equality
     */
    private final boolean equals(byte[] comp, int offset, int length, byte[] source)
    {
        if ((length != source.length) || (comp.length - offset < length))
        {
            return false;
        }
        for (int i = 0; i < length; i++)
        {
            if (comp[offset+i] != source[i])
            {
                return false;
            }
        }
        return true;
    }









}
