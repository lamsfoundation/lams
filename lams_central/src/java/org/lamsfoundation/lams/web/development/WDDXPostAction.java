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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.web.development;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;


/**
 * Takes a action ( URL ) and a file containing a WDDX packet
 * and forwards the contents of the WDDX packet to the action.
 * <p>
 * This is used to simulate Flash sending a WDDX packet to
 * the server.
 * <p>
 * The error handling just throws a RuntimeException if something
 * goes wrong. Not nice but this is only run during development....
 * <p>
 * @struts:action name="WDDXPostActionForm"
 * 				  path="/WDDXPost"
 * 				  validate="false"
 * 				  parameter="method"
 */
public class WDDXPostAction extends Action {
	
	private static Logger log = Logger.getLogger(WDDXPostAction.class);
	
	/**
	 * Process the request
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, 
			HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {

		WDDXPostActionForm postForm = (WDDXPostActionForm) form;
		
		String action = postForm.getUrlAction();
		if ( action == null ) {
			RuntimeException e = new RuntimeException("Unable to process WDDX file, action is missing. Should be a URL");
			log.error(e);
			throw e;
		}
		
		FormFile file = postForm.getWddxFile();
		if ( file == null ) {
			RuntimeException e = new RuntimeException("Unable to process WDDX file, file is missing.");
			log.error(e);
			throw e;
		}
		Cookie[] cookies = req.getCookies();
		// we've got the URL action and the WDDX input. Now try sending the WDDX to the URL
	  	URL url = new URL(action);
	  	HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
		urlConn.setRequestMethod("POST");
		urlConn.setDoInput (true);
		urlConn.setDoOutput (true);
		urlConn.setUseCaches (false);
		urlConn.setAllowUserInteraction(false);
	    urlConn.setInstanceFollowRedirects(true);
	    urlConn.setRequestProperty("Cookie", getCookieString(cookies)); 
	    
	    // Get packet from input file  
	    ByteArrayOutputStream byteStream = new ByteArrayOutputStream(512); // Grows if necessary
	    PrintWriter urlStreamWriter = new PrintWriter(byteStream, true);
	    BufferedReader wddxPacketStream = new BufferedReader(new InputStreamReader(file.getInputStream()));
	    String line = null;
	    while ((line = wddxPacketStream.readLine()) != null) {
	    	urlStreamWriter.print(line);
	    } 
	    urlStreamWriter.flush();
	    wddxPacketStream.close();
	    String packet = byteStream.toString();
	    String packetStart = packet != null ? packet.substring(0,11): "";
	    if ( ! packetStart.equalsIgnoreCase("<wddxPacket") ) {
	    	throw new ServletException("Input file did not start with <wddxPacket> tag. Invalid format.");
	    }
	    log.debug("Sending packet "+packet);

	    // POST requests are required to have Content-Length
	    String lengthString = String.valueOf(byteStream.size());
	    urlConn.setRequestProperty("Content-Length", lengthString);
	      
	    // Write POST data to real output stream
	    byteStream.writeTo(urlConn.getOutputStream());
	    urlConn.getOutputStream().flush();
	    urlConn.connect();
	    
	    // pass the response from the action back to the calling form.
	    BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
    	Writer writer = res.getWriter();
	    while ((line = in.readLine()) != null) {
	    	writer.write(line);
	     }
	    in.close();
	    
	    urlConn.disconnect();

		return null;
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