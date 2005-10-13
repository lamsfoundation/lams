/*
 * Created on Oct 13, 2005
 *
 */
package org.lamsfoundation.lams.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.Cookie;

import org.apache.log4j.Logger;


/**
 * @author mtruong
 *
 */
public class HttpUrlConnectionUtil {
    
    private static Logger log = Logger.getLogger(HttpUrlConnectionUtil.class);
    
    /**
     * Mimics a browser connection and connects to the url <code>urlToConnectTo</code> and writes
     * the contents to the file with <code>filename</code> stored in <code>directoryToStoreFile</code>.
     * Also sets the necessary cookies needed in the form JSESSIONID=XXXX;JSESSIONIDSSO=XXXX;SYSSESSIONID=XXXX
     * 
     * @param urlToConnectTo The url in which the HttpUrlConnection is to be made
     * @param directoryToStoreFile The directory to place the saved html file
     * @param filename	The name of the file, eg. export_main.html
     * @param cookies The Cookie objects which needs to be passed along with the request
     * @throws MalformedURLException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void writeResponseToFile(String urlToConnectTo, String directoryToStoreFile, String filename, Cookie[] cookies) 
    	throws MalformedURLException, FileNotFoundException, IOException
    {
        String absoluteFilePath = directoryToStoreFile + File.separator + filename;
		
			URL url = new URL(urlToConnectTo);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setRequestProperty("Cookie", getCookieString(cookies)); //send the necessary cookies along with the request
			
			if ( log.isDebugEnabled() ) {
				log.debug("A connection has been established with "+urlToConnectTo);
			}
			InputStream inputStream = con.getInputStream();
			OutputStream outStream = new FileOutputStream(absoluteFilePath);
			   
			int c = -1;
			while ((c = inputStream.read())!= -1)
			{
				outStream.write(c);
			}
			   
			inputStream.close();
			outStream.close(); 
			if ( log.isDebugEnabled() ) {
				log.debug("A connection to "+urlToConnectTo + " has been closed");
			}
	
    }
    
    /**
     * Method used by the export service method. It will mimic a browsers and
     * connects to the tools export url via a HttpUrlConnection. The tool's
     * export page will return the name of the main file being exported, and this
     * method reads the name and returns it to the calling code.
     * @param toolsExportUrl The url in which the HttpUrlConnection is to be made
     * @param cookies The Cookie objects which needs to be passed along with the request
     * @return String the main file that was exported by the tool
     * @throws MalformedURLException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static String connectToToolExportURL(String toolsExportUrl, Cookie[] cookies)
    	throws MalformedURLException, FileNotFoundException, IOException
    {
	    String mainFileName = null;
		
	    URL url = new URL(toolsExportUrl);
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setRequestProperty("Cookie", getCookieString(cookies));
			
		BufferedReader input = new BufferedReader(new InputStreamReader(con.getInputStream()));
	
		mainFileName = input.readLine();
			
		input.close();
	
		return mainFileName;	
		
	
	}
    
    
    /**
     * This helper method sets up the string which is passed as a parameter to
     * conn.setRequestProperty("Cookie" cookeString). It formulates a string
     * of the form JSESSIONID=XXXX;JSESSIONIDSSO=XXXX;SYSSESSIONID=XXXX
     * @param cookies
     * @return
     */
    private static String getCookieString(Cookie[] cookies)
	{
	    	    
	   StringBuffer cookieString = new StringBuffer();
	   for (int i=0; i< cookies.length; i++)
	   {
	   		cookieString.append(cookies[i].getName()).append("=").append(cookies[i].getValue());
	   		if (i != (cookies.length-1))
	   		{
	   		    cookieString.append(";");
	   		}
	   }
	   
	   return cookieString.toString();
	}

}
