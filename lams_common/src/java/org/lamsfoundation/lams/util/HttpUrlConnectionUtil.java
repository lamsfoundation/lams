/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
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

package org.lamsfoundation.lams.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.Cookie;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * @author mtruong
 *
 */
public class HttpUrlConnectionUtil {
    private static Logger log = Logger.getLogger(HttpUrlConnectionUtil.class);

    public static final int STATUS_OK = 1;
    public static final int STATUS_ERROR = -1;

    // environment variables
    public static final String JBOSS_BIND_ADDRESS_KEY = "jboss.bind.address";
    // if JBoss bind port is different than 8080, this must be set using -Dlams.port=XXXX as a command line parameter
    public static final String LAMS_PORT_KEY = "lams.port";

    private static boolean defaultTrustManagerSet = false;
    private static String lamsLocalAddress;

    /**
     * Mimics a browser connection and connects to the url <code>urlToConnectTo</code> and writes the contents to the
     * file with <code>filename</code> stored in <code>directoryToStoreFile</code>. Also sets the necessary cookies
     * needed in the form JSESSIONID=XXXX;JSESSIONIDSSO=XXXX;SYSSESSIONID=XXXX If the Http Status-Code returned is 200,
     * then it will proceed to write the contents to a file. Otherwise it will return the value -1 to indicate that an
     * error has occurred. It is up to the calling function on how this error is dealt with.
     *
     * @param urlToConnectTo
     *            The url in which the HttpUrlConnection is to be made
     * @param directoryToStoreFile
     *            The directory to place the saved html file
     * @param filename
     *            The name of the file, eg. export_main.html
     * @param cookies
     *            The Cookie objects which needs to be passed along with the request
     * @return int returns 1 if success, -1 otherwise.
     * @throws MalformedURLException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static int writeResponseToFile(String urlToConnectTo, String directoryToStoreFile, String filename,
	    Cookie[] cookies) throws MalformedURLException, FileNotFoundException, IOException {
	int status;
	int statusCode;

	String absoluteFilePath = directoryToStoreFile + File.separator + filename;

	HttpURLConnection con = HttpUrlConnectionUtil.getConnection(urlToConnectTo);
	con.setRequestProperty("Cookie", HttpUrlConnectionUtil.getCookieString(cookies)); // send the necessary cookies
											  // along with the request

	if (HttpUrlConnectionUtil.log.isDebugEnabled()) {
	    HttpUrlConnectionUtil.log.debug("A connection has been established with " + urlToConnectTo);
	}

	// get the code before retrieving the input stream
	statusCode = con.getResponseCode();
	status = HttpUrlConnectionUtil.getStatus(statusCode);

	if (status == HttpUrlConnectionUtil.STATUS_OK) {
	    InputStream inputStream = con.getInputStream(); // start reading the input stream
	    OutputStream outStream = new BufferedOutputStream(new FileOutputStream(absoluteFilePath));

	    IOUtils.copy(inputStream, outStream);

	    inputStream.close();
	    outStream.close();
	    if (HttpUrlConnectionUtil.log.isDebugEnabled()) {
		HttpUrlConnectionUtil.log.debug("A connection to " + urlToConnectTo + " has been closed");
	    }

	} else {
	    HttpUrlConnectionUtil.log.error(
		    "URL Connection Error: A problem has occurred while connecting to this url " + urlToConnectTo);
	}

	return status;

    }

    private static int getStatus(int statusCode) {
	int status = HttpUrlConnectionUtil.STATUS_ERROR; // default to -1, if status isnt 200, it would be an error
							 // anyway

	switch (statusCode) {
	    case HttpURLConnection.HTTP_OK:
		status = HttpUrlConnectionUtil.STATUS_OK;
		break;
	    case HttpURLConnection.HTTP_INTERNAL_ERROR: // 500
		status = HttpUrlConnectionUtil.STATUS_ERROR;
		break;

	    case HttpURLConnection.HTTP_NOT_FOUND: // 404
		status = HttpUrlConnectionUtil.STATUS_ERROR;
		break;
	}

	return status;
    }

    /**
     * This helper method sets up the string which is passed as a parameter to conn.setRequestProperty("Cookie"
     * cookeString). It formulates a string of the form JSESSIONID=XXXX;JSESSIONIDSSO=XXXX;SYSSESSIONID=XXXX
     *
     * @param cookies
     * @return
     */
    private static String getCookieString(Cookie[] cookies) {

	StringBuffer cookieString = new StringBuffer();
	for (int i = 0; i < cookies.length; i++) {
	    cookieString.append(cookies[i].getName()).append("=").append(cookies[i].getValue());
	    if (i != (cookies.length - 1)) {
		cookieString.append(";");
	    }
	}

	return cookieString.toString();
    }

    public static HttpURLConnection getConnection(String urlToConnectTo) throws IOException {
	URL url = new URL(urlToConnectTo);
	HttpURLConnection con = null;
	if (urlToConnectTo.toLowerCase().startsWith("https")) {
	    if (!HttpUrlConnectionUtil.defaultTrustManagerSet) {
		TrustManager defaultTrustManager = new X509TrustManager() {
		    @Override
		    public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		    }

		    @Override
		    public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		    }

		    @Override
		    public X509Certificate[] getAcceptedIssuers() {
			return null;
		    }
		};

		try {
		    SSLContext ctx = SSLContext.getInstance("TLS");
		    ctx.init(new KeyManager[0], new TrustManager[] { defaultTrustManager }, new SecureRandom());
		    SSLContext.setDefault(ctx);
		    HttpUrlConnectionUtil.defaultTrustManagerSet = true;
		} catch (NoSuchAlgorithmException e) {
		    HttpUrlConnectionUtil.log.error(e);
		} catch (KeyManagementException e) {
		    HttpUrlConnectionUtil.log.error(e);
		}
	    }

	    HttpsURLConnection httpsCon = (HttpsURLConnection) url.openConnection();
	    httpsCon.setHostnameVerifier(new HostnameVerifier() {
		@Override
		public boolean verify(String arg0, SSLSession arg1) {
		    return true;
		}
	    });
	    con = httpsCon;
	} else {
	    con = (HttpURLConnection) url.openConnection();
	}

	return con;
    }

    public static String getLamsLocalAddress() {
	if (HttpUrlConnectionUtil.lamsLocalAddress == null) {
	    // get address where JBoss is running for localhost calls, otherwise there is a problem with load balancing
	    String port = System.getProperty(HttpUrlConnectionUtil.LAMS_PORT_KEY);
	    // if there is no port given explicitly as parameter, fall back to SERVER_URL
	    if (port == null) {
		HttpUrlConnectionUtil.lamsLocalAddress = Configuration.get(ConfigurationKeys.SERVER_URL);
	    } else {
		HttpUrlConnectionUtil.lamsLocalAddress = "http://"
			+ System.getProperty(HttpUrlConnectionUtil.JBOSS_BIND_ADDRESS_KEY) + ":" + port + "/"
			+ Configuration.get(ConfigurationKeys.SERVER_URL_CONTEXT_PATH);
	    }
	}
	return HttpUrlConnectionUtil.lamsLocalAddress;
    }
}