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

package org.lamsfoundation.lams.web.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.util.audit.IAuditService;
import org.lamsfoundation.lams.util.wddx.FlashMessage;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Base servlet for handling WDDX packets sent by Flash. This servlet takes care of reading the packet from the POST
 * body. A subclass must implement the process() method and the getMessageKey() method.
 * <p>
 * If an error occurs during processing, the process method should throw an exception. The base class will log the
 * exception and return an error message to Flash.
 * <p>
 * We cannot use Struts actions as the WDDX packet is the complete contents POST body and Struts consumes the body
 * before we can get to it.
 * <p>
 * If this servlet receives a GET rather than a POST, then an error packet (in wddx format) will be returned.
 * <P>
 * If the log level is set to debug, then the both the received packet and the reply packet will be logged.
 *
 * @author Fiona Malikoff
 */
public abstract class AbstractStoreWDDXPacketServlet extends HttpServlet {

    private static Logger log = Logger.getLogger(AbstractStoreWDDXPacketServlet.class);
    private static IAuditService auditService;

    /**
     * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest, HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	Writer writer = response.getWriter();
	FlashMessage flashMessage = FlashMessage.getWDDXPacketGetReceived(getMessageKey(null, request));
	writer.write(flashMessage.serializeMessage());
    }

    /**
     * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest, HttpServletResponse)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
	AbstractStoreWDDXPacketServlet.auditService = getAuditService();
	PrintWriter writer = null;
	String packet = null;
	String replyPacket = null;
	try {
	    writer = response.getWriter();

	    packet = AbstractStoreWDDXPacketServlet.getBody(request);
	    if (AbstractStoreWDDXPacketServlet.log.isDebugEnabled()) {
		AbstractStoreWDDXPacketServlet.log
			.debug("Request " + request.getRequestURI() + " received packet length " + packet);
	    }

	    if (containsNulls(packet)) {
		FlashMessage flashMessage = new FlashMessage(getMessageKey(packet, request), "WDDXPacket contains null",
			FlashMessage.ERROR);
		writer.write(flashMessage.serializeMessage());
	    }

	    replyPacket = process(packet, request);

	    if (AbstractStoreWDDXPacketServlet.log.isDebugEnabled()) {
		AbstractStoreWDDXPacketServlet.log
			.debug("Request " + request.getRequestURI() + " sending back packet " + replyPacket);
	    }

	} catch (Exception e) {
	    // Don't want exceptions flowing back to Flash if we can help it.
	    String uri = request.getRequestURI();
	    AbstractStoreWDDXPacketServlet.log.error(uri + " request triggered exception ", e);

	    FlashMessage flashMessage = FlashMessage.getExceptionOccured(getMessageKey(packet, request),
		    e.getMessage() != null ? e.getMessage() : e.getClass().getName());
	    writer.write(flashMessage.serializeMessage());

	    AbstractStoreWDDXPacketServlet.auditService.log(AbstractStoreWDDXPacketServlet.class.getName(),
		    "URL:" + uri + " triggered exception" + e.toString());
	    return;
	}

	// can't inform Flash if an exception occurs now...
	if (writer != null) {
	    writer.println(replyPacket);
	}
    }

    /**
     * Checks whether the WDDX packet contains any invalid "<null/>". It returns true if there exists any such null
     */
    protected boolean containsNulls(String packet) {
	if (packet.indexOf("<null />") != -1) {
	    return true;
	} else {
	    return false;
	}
    }

    /**
     * Method to get the post body. Retrieves the body of the post as binary data, and then converts back to a character
     * stream. The reason why we didn't use getReader() which retrieves the body of the post as character data, is
     * because for very large learning designs, the wddx packet read in gets corrupted or truncated. The reason for
     * this, is still unknown, however using getInputStream() doesn't seem to have this problem. TODO: investigate why
     * getReader() doesn't work for large learning designs. The original code has been commented out below.
     *
     * @param req
     * @return
     * @throws IOException
     */
    public static String getBody(HttpServletRequest req) throws IOException {
	int tempContentLength = req.getContentLength();
	InputStream sis = req.getInputStream();
	/*
	 * byte[] content = new byte[1024*4];
	 * OutputStream bos = new ByteArrayOutputStream(tempContentLength>0 ? tempContentLength : 200);
	 * int len;
	 * while((len = sis.read(content)) != -1){
	 * bos.write(content,0,len);
	 * }
	 * return bos.toString();
	 */
	BufferedReader buff = new BufferedReader(new InputStreamReader(sis, "UTF-8"));

	StringBuffer tempStrBuf = new StringBuffer(tempContentLength > 0 ? tempContentLength : 200);
	String tempStr;
	tempStr = buff.readLine();
	while (tempStr != null) {
	    tempStrBuf.append(tempStr);
	    tempStr = buff.readLine();
	}

	return (tempStrBuf.toString());

	/*
	 * BufferedReader tempReader = req.getReader();
	 * int tempContentLength = req.getContentLength();
	 * 
	 * StringBuffer tempStrBuf = new StringBuffer( tempContentLength>0 ? tempContentLength : 200 );
	 * String tempStr;
	 * tempStr = tempReader.readLine();
	 * while ( tempStr != null )
	 * {
	 * tempStrBuf.append(tempStr);
	 * tempStr = tempReader.readLine();
	 * }
	 * 
	 * return(tempStrBuf.toString());
	 */

    }

    /**
     * Process the packet received from Flash.
     * <p>
     * If an error occurs, this method should throw an exception so that the base class will log the exception and
     * return an error message to Flash.
     *
     * @return A string which is a WDDX packet containing a FlashMessage.
     */
    abstract protected String process(String packet, HttpServletRequest request) throws Exception;

    /**
     * Get the "name" of the servlet. This must match the name that is returned in the messageKey section of the
     * FlashMessage when the packet is successfully stored.
     * <p>
     * If an exception is thrown by process(), the base class generates an error packet to send back to Flash. It will
     * include this messageKey in the error message, so that the Flash client can match the error to the call.
     *
     * @return messageKey
     */
    abstract protected String getMessageKey(String packet, HttpServletRequest request);

    /**
     * Get AuditService bean.
     *
     * @return
     */
    private IAuditService getAuditService() {
	if (AbstractStoreWDDXPacketServlet.auditService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(this.getServletContext());
	    AbstractStoreWDDXPacketServlet.auditService = (IAuditService) ctx.getBean("auditService");
	}
	return AbstractStoreWDDXPacketServlet.auditService;
    }
}
