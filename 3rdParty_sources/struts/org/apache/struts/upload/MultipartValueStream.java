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

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;

/**
 * This class implements an inputStream that reads another stream until 
 * a multipart boundary is found. The class reports eof when boundary found.
 * The undelying stream is not closed.
 *
 * <p>
 * See RFC 1867 (http://info.internet.isi.edu:80/in-notes/rfc/files/rfc1867.txt)
 * for details about the protocol.
 * <p>
 *
 *
 * @deprecated Use the Commons FileUpload based multipart handler instead. This
 *             class will be removed after Struts 1.2.
 */

class MultipartValueStream extends InputStream {

    public static final String HEADER_ENCODING = "iso-8859-1";

    /** the underlying stream */
    private InputStream in;
    
    /** byte buffer with the boundary */
    private byte boundaryBytes[];

    /** how many curretly matched boundary bytes? */
    private int matchedBoundaryBytes;

    /** the read ahead buffer (cyclic) */
    private byte readAheadBytes[];

    /** The start index for the read ahead cyclic buffer (points to the first byte) */
    private int readAheadBufferStartI;

    /** The end index for the read ahead cyclic buffer (points to the last byte) */
    private int readAheadBufferEndI;

    /** have we reached the boundary? */
    private boolean boundaryReached = false;

    /** is the boundary found a final boundary? */
    private boolean finalBoundaryReached = false;


    /**
     * Create a stream that stops reading at the boundary
     *
     * NOTE: the boundary parameter is without the trailing dashes "--".
     */
    public MultipartValueStream(InputStream in, String boundary) 
	throws IOException
    {
	this.in = in;
	this.boundaryBytes = ("\r\n" + boundary).getBytes(HEADER_ENCODING);
	this.matchedBoundaryBytes = 0;
	this.readAheadBytes = new byte[this.boundaryBytes.length];

	/* Fill read ahead buffer */
	if (in.read(readAheadBytes, 0, readAheadBytes.length) != readAheadBytes.length) {
	    throw new IOException("end of stream before boundary found!");
	}

	/* Count the number of matched chars */
	for (int i = 0; i < readAheadBytes.length; i++) {
	    if (readAheadBytes[i] == boundaryBytes[matchedBoundaryBytes]) {
		matchedBoundaryBytes++;
	    } else {
		matchedBoundaryBytes = 0;
		if (readAheadBytes[i] == boundaryBytes[0]) {
		    matchedBoundaryBytes = 1;
		}
	    }
	}

	readAheadBufferStartI = 0;
	readAheadBufferEndI = readAheadBytes.length - 1;
    }
    
    
    /**
     * Read the next byte
     *
     * @return -1 on boundary reached
     * @exception IOException if the ending boundary is never found
     *
     */

    public int read() throws IOException {
	if (boundaryReached) {
	    return -1;
	}
	if (matchedBoundaryBytes == boundaryBytes.length) {

	    boundaryReached = true;

	    /* 
	     * Boundary found...
	     *
	     * Read two more bytes:
	     * 1. the bytes are "--":          this is the last parameter value (then read "\r\n" too)
	     * 2. the bytes are "\r\n":        this is not the last value
	     * 3. the bytes are somthing else: Exception
	     */

	    byte buf[] = new byte[2];
	    if (in.read(buf) != 2) {
		throw new IOException("end of stream before boundary found!");
	    }

	    String readStr = new String(buf, HEADER_ENCODING);
	    if (readStr.equals("--")) {

		if (in.read(buf) != 2) {
		    throw new IOException("invalid end of final boundary found!");
		}
		readStr = new String(buf, HEADER_ENCODING);
		if (!readStr.equals("\r\n")) {
		    throw new IOException("invalid end of final boundary found!");
		}
		finalBoundaryReached = true;

	    } else if (readStr.equals("\r\n")) {
		finalBoundaryReached = false;
	    } else {
		throw new IOException("invalid end of boundary found!");
	    }

	    return -1;
	}
	
	/* 
	 * Might seem odd, but we are supposed to return 
	 * a byte as an int in range 0 - 255, and the byte type
	 * is signed (-128 to 127) 
	 *
	 */
	int returnByte = (int)(char) readAheadBytes[readAheadBufferStartI];

	/* Move cyclic-buffers start pointer */
	readAheadBufferStartI++;
	if (readAheadBufferStartI == readAheadBytes.length) {
	    readAheadBufferStartI = 0;
	}

	/* read from the underlying stream */
	int underlyingRead = in.read();
	if (underlyingRead == -1) {
	    throw new IOException("end of stream before boundary found!");
	}

	/* Move cyclic-buffers end pointer */
	readAheadBufferEndI++;
	if (readAheadBufferEndI == readAheadBytes.length) {
	    readAheadBufferEndI = 0;
	}
	readAheadBytes[readAheadBufferEndI] = (byte) underlyingRead;

	if (readAheadBytes[readAheadBufferEndI] == boundaryBytes[matchedBoundaryBytes]) {
	    matchedBoundaryBytes++;
	} else {
	    matchedBoundaryBytes = 0;
    	    if (readAheadBytes[readAheadBufferEndI] == boundaryBytes[0]) {
		matchedBoundaryBytes = 1;
	    }
	}
	return returnByte;
    }

    /**
     * @return true if we are the last stream, ie. we encountered a final boundary
     * @return false otherwise
     *
     * @exception ServletException if the boundary has not yet been reached
     */

    public boolean encounteredFinalBoundary() 
	throws ServletException 
    {
	if (!boundaryReached) {
	    throw new ServletException("have not reached boundary yet!");
	}
	return finalBoundaryReached;
    }
}
