/* 
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
USA

http://www.gnu.org/licenses/gpl.txt 
*/
package org.lamsfoundation.lams.web.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.util.wddx.FlashMessage;

/**
 * Base servlet for handling WDDX packets sent by Flash.
 * This servlet takes care of reading the packet from the
 * POST body. A subclass must implement the process() method
 * and the getMessageKey() method.
 * <p>
 * If an error occurs during processing, the process method
 * should throw an exception. The base class will log the exception 
 * and return an error message to Flash.
 * <p>
 * We cannot use Struts actions as the WDDX packet is the
 * complete contents POST body and Struts consumes the body
 * before we can get to it.
 * <p>
 * If this servlet receives a GET rather than a POST, then an 
 * error packet (in wddx format) will be returned.
 * <P>
 * If the log level is set to debug, then the both the received packet 
 * and the reply packet will be logged.
 * 
 * @author Fiona Malikoff
 */
public abstract class AbstractStoreWDDXPacketServlet extends HttpServlet {

	private static Logger log = Logger.getLogger(AbstractStoreWDDXPacketServlet.class);
	/**
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest, HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		Writer writer = response.getWriter();
		FlashMessage flashMessage = FlashMessage.getWDDXPacketGetReceived(getMessageKey(null,request));
		writer.write(flashMessage.serializeMessage());
	}

	/**
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws IOException
	{

		PrintWriter writer = null;
		String packet = null;
		String replyPacket = null;
		try {
			writer = response.getWriter();

			packet = getBody(request);
			if ( log.isDebugEnabled() ) {
				log.debug("Request "+request.getRequestURI()+" received packet "+packet);
			}

			replyPacket = process(packet, request);

			if ( log.isDebugEnabled() ) {
				log.debug("Request "+request.getRequestURI()+" sending back packet "+replyPacket);
			}
			
		} catch ( Exception e ) {
			// Don't want exceptions flowing back to Flash if we can help it.
			String uri = request.getRequestURI();
			log.error(uri+" request triggered exception ",e);
			
			FlashMessage flashMessage = FlashMessage.getExceptionOccured(getMessageKey(packet,request),
					e.getMessage()!=null?e.getMessage():"");
			writer.write(flashMessage.serializeMessage());
			return;
		}
		
		// can't inform Flash if an exception occurs now...
		if ( writer != null )
			writer.println(replyPacket);
	}
	
	/* Get the post body */
  	private String getBody(HttpServletRequest req)
  		throws IOException
  	{
		BufferedReader  tempReader  = req.getReader();
		int tempContentLength = req.getContentLength();
		StringBuffer tempStrBuf = new StringBuffer( tempContentLength>0 ? tempContentLength : 200 );
		String tempStr;
		tempStr = tempReader.readLine(); 
		while ( tempStr != null )
		{
			tempStrBuf.append(tempStr);
			tempStr = tempReader.readLine();
		}

		return(tempStrBuf.toString());
  	}

  	/** 
  	 * Process the packet received from Flash.
  	 * <p>
  	 * If an error occurs, this method should throw an exception
  	 * so that the base class will log the exception and return an error message 
  	 * to Flash.
  	 * @return A string which is a WDDX packet containing a FlashMessage.
  	 */
  	abstract protected String process(String packet, HttpServletRequest request) 
  		throws Exception;

  	/** 
  	 * Get the "name" of the servlet. This must match the name
  	 * that is returned in the messageKey section of the FlashMessage 
  	 * when the packet is successfully stored.
  	 * <p>
  	 * If an exception is thrown by process(), the base class
  	 * generates an error packet to send back to Flash. It will include
  	 * this messageKey in the error message, so that the Flash client
  	 * can match the error to the call.
  	 * @return messageKey
  	 */
  	abstract protected String getMessageKey(String packet, HttpServletRequest request);
}
