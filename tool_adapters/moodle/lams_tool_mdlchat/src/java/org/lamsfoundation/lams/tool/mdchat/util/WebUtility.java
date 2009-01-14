/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
package org.lamsfoundation.lams.tool.mdchat.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.exception.ToolException;

public class WebUtility {
    static Logger logger = Logger.getLogger(WebUtility.class.getName());

    /**
     * Uploads a file for importing sequences Uses a multi-part http post to
     * post the file as well as the user, course, and hash server-authentication
     * strings.
     * 
     * Some of the java multipart posting libraries clashed with existing jboss
     * libraries So instead, the multipart post is put together manually
     * 
     * @param f
     * @param urlString
     * @param timestamp
     * @param extUsername
     * @param extCourseId
     * @param hash
     * @return
     * @throws IOException
     */
    public static InputStream performMultipartPost(File f, String fileParamName, String urlString,
	    HashMap<String, String> params) throws MalformedURLException, IOException {
	HttpURLConnection conn = null;
	DataOutputStream dos = null;
	DataInputStream inStream = null;
	String lineEnd = "\r\n";
	String twoHyphens = "--";
	String boundary = "*****";
	int bytesRead, bytesAvailable, bufferSize;
	byte[] buffer;
	int maxBufferSize = 1 * 1024 * 1024;

	//------------------ CLIENT REQUEST
	FileInputStream fileInputStream = new FileInputStream(f);

	// open a URL connection to the Servlet 
	URL url = new URL(urlString);

	// Open a HTTP connection to the URL
	conn = (HttpURLConnection) url.openConnection();

	// Allow Inputs
	conn.setDoInput(true);

	// Allow Outputs
	conn.setDoOutput(true);

	// Don't use a cached copy.
	conn.setUseCaches(false);

	String httpRequest = "";
	for (Entry<String, String> entry : params.entrySet()) {
	    httpRequest += twoHyphens + boundary + lineEnd;
	    httpRequest += "Content-Disposition: form-data; name=\"" + entry.getKey() + "\";" + lineEnd;
	    httpRequest += lineEnd;
	    httpRequest += entry.getValue() + lineEnd;
	}
	httpRequest += twoHyphens + boundary + lineEnd;
	httpRequest += "Content-Disposition: form-data; name=\"" + fileParamName + "\";" + " filename=\"" + f.getName()
		+ "\"" + lineEnd;
	httpRequest += lineEnd;

	// Use a post method.
	conn.setRequestMethod("POST");
	conn.setRequestProperty("Connection", "Keep-Alive");
	conn.setRequestProperty("Content-Length", new Long(f.length() + httpRequest.getBytes().length + 64).toString());
	logger.debug(f.length());
	logger.debug(httpRequest.getBytes().length);
	logger.debug("" + f.length() + httpRequest.getBytes().length + 64);
	logger.debug(httpRequest);

	conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

	dos = new DataOutputStream(conn.getOutputStream());
	dos.writeBytes(httpRequest);

	// create a buffer of maximum size
	bytesAvailable = fileInputStream.available();
	bufferSize = Math.min(bytesAvailable, maxBufferSize);
	buffer = new byte[bufferSize];

	// read file and write it into form...
	bytesRead = fileInputStream.read(buffer, 0, bufferSize);
	while (bytesRead > 0) {
	    dos.write(buffer, 0, bufferSize);
	    bytesAvailable = fileInputStream.available();
	    bufferSize = Math.min(bytesAvailable, maxBufferSize);
	    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
	}

	// send multipart form data necessary after file data...
	dos.writeBytes(lineEnd);
	dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
	// Write the file part  into the post-------------------------------

	// close streams
	fileInputStream.close();
	dos.flush();
	dos.close();

	return conn.getInputStream();
    }

    public static InputStream getResponseInputStreamFromExternalServer(String urlStr, HashMap<String, String> params)
	    throws ToolException, IOException {
	if (!urlStr.endsWith("?"))
	    urlStr += "?";

	for (Entry<String, String> entry : params.entrySet()) {
	    urlStr += "&" + entry.getKey() + "=" + entry.getValue();
	}

	logger.debug("Making request to external servlet: " + urlStr);

	URL url = new URL(urlStr);
	URLConnection conn = url.openConnection();
	if (!(conn instanceof HttpURLConnection)) {
	    logger.error("Fail to connect to external server though url:  " + urlStr);
	    throw new ToolException("Fail to connect to external server though url:  " + urlStr);
	}

	HttpURLConnection httpConn = (HttpURLConnection) conn;
	if (httpConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
	    logger.error("Fail to fetch data from external server, response code:  " + httpConn.getResponseCode()
		    + " Url: " + urlStr);
	    throw new ToolException("Fail to fetch data from external server, response code:  "
		    + httpConn.getResponseCode() + " Url: " + urlStr);
	}

	InputStream is = url.openConnection().getInputStream();
	if (is == null) {
	    logger.error("Fail to fetch data from external server, return InputStream null:  " + urlStr);
	    throw new ToolException("Fail to fetch data from external server, return inputStream null:  " + urlStr);
	}

	return is;
    }

}
