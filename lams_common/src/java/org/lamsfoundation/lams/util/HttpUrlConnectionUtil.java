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
    
    public static final int STATUS_OK = 1;
    public static final int STATUS_ERROR = -1;
        
  
    /**
     * Mimics a browser connection and connects to the url <code>urlToConnectTo</code> and writes
     * the contents to the file with <code>filename</code> stored in <code>directoryToStoreFile</code>.
     * Also sets the necessary cookies needed in the form JSESSIONID=XXXX;JSESSIONIDSSO=XXXX;SYSSESSIONID=XXXX
     * If the Http Status-Code returned is 200, then it will proceed to write the contents to a file.
     * Otherwise it will return the value -1 to indicate that an error has occurred. It is up to the 
     * calling function on how this error is dealt with.
     * 
     * @param urlToConnectTo The url in which the HttpUrlConnection is to be made
     * @param directoryToStoreFile The directory to place the saved html file
     * @param filename	The name of the file, eg. export_main.html
     * @param cookies The Cookie objects which needs to be passed along with the request
     * @return int returns 1 if success, -1 otherwise.
     * @throws MalformedURLException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static int writeResponseToFile(String urlToConnectTo, String directoryToStoreFile, String filename, Cookie[] cookies) 
    	throws MalformedURLException, FileNotFoundException, IOException
    {
        int status;
        int statusCode;
        
        String absoluteFilePath = directoryToStoreFile + File.separator + filename;
		
			URL url = new URL(urlToConnectTo);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setRequestProperty("Cookie", getCookieString(cookies)); //send the necessary cookies along with the request
			
			if ( log.isDebugEnabled() ) {
				log.debug("A connection has been established with "+urlToConnectTo);
			}
			
			//get the code before retrieving the input stream
			statusCode = con.getResponseCode();
			status = getStatus(statusCode);
						
			if (status == STATUS_OK)
			{
			    InputStream inputStream = con.getInputStream(); //start reading the input stream
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
			else
			{
			    log.error("URL Connection Error: A problem has occurred while connecting to this url " + urlToConnectTo);
			}
			
			return status;
	
    }
    
    /**
     * Method used by the export service method. It will mimic a browser and
     * connects to the tools export url via a HttpUrlConnection.
     * If the Http Status-Code returned is 200, then it will proceed to read the 
     * main file name returned by the tool.
     * 
     * Otherwise this method will return null. It is up to the calling function to 
     * deal with this.  
     * 
     * @param toolsExportUrl The url in which the HttpUrlConnection is to be made
     * @param cookies The Cookie objects which needs to be passed along with the request
     * @return String The filename of the file that was exported by the tool, returns null if any error has occurred
     * @throws MalformedURLException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static String connectToToolExportURL(String toolsExportUrl, Cookie[] cookies)
    	throws MalformedURLException, FileNotFoundException, IOException
    {
        int status;
        int statusCode;
	    String mainFileName = null;
		
	    URL url = new URL(toolsExportUrl);
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setRequestProperty("Cookie", getCookieString(cookies)); //pass the cookies along as well. 
			
		statusCode = con.getResponseCode();
		status = getStatus(statusCode);
		
		if (status == STATUS_OK)
		{		
		    InputStream inputStream = con.getInputStream();
			BufferedReader input = new BufferedReader(new InputStreamReader(inputStream));
			
			mainFileName = input.readLine();
				
			input.close();
		
			return mainFileName;	
		}
		else
		{
		    String errorMsg = "A problem has occurred while connecting to the tools export url " + toolsExportUrl;
		    log.error(errorMsg);		   
		    return null;
		}
		
	
	}
    
    private static int getStatus(int statusCode)
    {
        int status = STATUS_ERROR; //default to -1, if status isnt 200, it would be an error anyway
        
        switch(statusCode)
        {
        	case HttpURLConnection.HTTP_OK:
        	    status = STATUS_OK;
        		break;
        	case HttpURLConnection.HTTP_INTERNAL_ERROR: //500
        	    status =  STATUS_ERROR;
        	    break;
        	
        	case HttpURLConnection.HTTP_NOT_FOUND: //404
        	    status = STATUS_ERROR;
        	    break;
        }
        
        return status;
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
