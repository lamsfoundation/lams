/**
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
 */
package org.lamsfoundation.lams.tool.wiki.web;

import static org.lamsfoundation.lams.tool.wiki.util.WikiConstants.OLD_FORUM_STYLE;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.web.context.support.WebApplicationContextUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;

import org.lamsfoundation.lams.integration.service.IntegrationService;
import org.lamsfoundation.lams.tool.wiki.service.IWikiService;
import org.lamsfoundation.lams.tool.wiki.service.WikiService;
import org.lamsfoundation.lams.tool.wiki.service.WikiServiceProxy;
import org.lamsfoundation.lams.tool.wiki.util.WikiConstants;
import org.lamsfoundation.lams.tool.wiki.web.forms.MessageForm;
import org.lamsfoundation.lams.tool.wiki.web.forms.WikiForm;
import org.lamsfoundation.lams.tool.wiki.persistence.Attachment;
import org.lamsfoundation.lams.tool.wiki.persistence.Message;
import org.lamsfoundation.lams.tool.wiki.persistence.PersistenceException;
import org.lamsfoundation.lams.tool.wiki.persistence.Wiki;
import org.lamsfoundation.lams.tool.wiki.persistence.WikiUser;
import org.lamsfoundation.lams.tool.wiki.dto.MessageDTO;
import org.lamsfoundation.lams.util.CentralConstants;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.web.util.AttributeNames;
/**
 * 
 * @author Daniel Carlier
 */
@SuppressWarnings("serial")
public class WikiLinkHandlerServlet extends HttpServlet {

	private static Logger log = Logger.getLogger(WikiLinkHandlerServlet.class);

	private static final String JNDI_DATASOURCE = "java:/jdbc/lams-ds";

	private static final String PASSWORD_QUERY = "select password from lams_user where login=?";
	
	private static IWikiService wikiService = null;
	
	private String basePath;
	
	//See LDEV652 
	// For old style (Fiona's description): The oldest topic is at the top, does not depends the reply date etc.
	// For new style (Ernie's early desc): Most current popular wiki used style: latest topics 
	// should be at the top, reply date will descide the order etc.
	public static final boolean OLD_FORUM_STYLE  = true;
	

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	
		wikiService = WikiServiceProxy.getWikiService(getServletContext());
		
		
		response.setCharacterEncoding("UTF8");
		PrintWriter out = response.getWriter();
		
		// set the base response path
		basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();

		
		// get the request parameters
		String method = request.getParameter("method");
		String toolSessionId = request.getParameter("toolSessionId");
		String sessionMapID = WebUtil.readStrParam(request, WikiConstants.ATTR_SESSION_MAP_ID);
		Long wikiID = WebUtil.readLongParam(request, WikiConstants.ATTR_WIKI_ID);
		
		
		SessionMap sessionMap = getSessionMap(request, sessionMapID);
		if (method.equals("getWikis"))
		{
			response.setContentType("text/xml");
			try
			{
				Document document = generateWikiDataXML(wikiService.getRootTopics(Long.parseLong(toolSessionId)), sessionMapID, Long.parseLong(toolSessionId), request);
			
				// write out the xml document.
				DOMSource domSource = new DOMSource(document);
				StringWriter writer = new StringWriter();
				StreamResult result = new StreamResult(writer);
				TransformerFactory tf = TransformerFactory.newInstance();
				Transformer transformer = tf.newTransformer();
				transformer.transform(domSource, result);
				out.write(writer.toString());	
			} catch (TransformerConfigurationException e) {
				log.error("Can not convert XML document to string", e);
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} catch (TransformerException e) {
				log.error("Can not convert XML document to string", e);
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
			} catch (ParserConfigurationException e) {
				log.error("Can not build XML document", e);
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} catch (Exception e) {
				log.error("Problem loading learning manager servlet request", e);
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
		}
		else if (method.equals("createWiki"))
		{
			//response.setContentType("text/plain");
			String wikiName = request.getParameter("wikiName");
			
			out.write(createWiki(wikiService, sessionMap, wikiName, wikiID, Long.parseLong(toolSessionId)));
		}
	}
	
	/**
	 * Each wiki should have [url, wiki name, display text]
	 * @param wikiList
	 * @return
	 */
	public Document generateWikiDataXML(List wikiList, String sessionMapID, Long wikiID, HttpServletRequest request) throws ParserConfigurationException
	{
		Iterator it = wikiList.iterator();
		
		// Create xml document
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.newDocument();
		Element element = document.createElement("WikiLinks");
		while (it.hasNext())
		{
			MessageDTO wiki = (MessageDTO)it.next();
					
			Element wikiElement = document.createElement("Wiki");
			
			wikiElement.setAttribute("name", wiki.getMessage().getSubject());
			
			String wikiURL = createURL(sessionMapID, wiki.getMessage().getUid().toString(), wiki.getMessage().getUpdated().getTime());
			
			wikiElement.setAttribute("url", wikiURL);
			
			element.appendChild(wikiElement);
		}
		document.appendChild(element);
		
		return document;
	}
	
	
	public String createWiki(IWikiService wikiService, SessionMap sessionMap, String wikiName, Long wikiID, Long toolSessionID)
	{

		// Get user DTO
		HttpSession ss = SessionManager.getSession();
		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

		Message message = new Message();
		message.setIsAuthored(true);
		message.setCreated(new Date());
		message.setUpdated(new Date());
		message.setLastReplyDate(new Date());
		message.setSubject(wikiName);
		
		//check whether this user exist or not
		WikiUser wikiUser = wikiService.getUserByID(new Long(user.getUserID().intValue()));

		String contentFolderID = (String) sessionMap.get(AttributeNames.PARAM_CONTENT_FOLDER_ID);
		
		//check whether it is sysadmin:LDEV-906 
		if(wikiUser == null && !StringUtils.equals(contentFolderID,"-1" )){
			//if user not exist, create new one in database
			wikiUser = new WikiUser(user,null);
		}
		
		Message createdWiki;
		
		if(message != null){
			message.setCreatedBy(wikiUser);
			message.setModifiedBy(wikiUser);
			createdWiki = wikiService.createRootTopic(wikiID, toolSessionID, message);
			
			return createURL(sessionMap.getSessionID(), createdWiki.getUid().toString(), createdWiki.getCreated().getTime());
		}
		return null; // TODO: Handle null return value
	}
	
	public String createURL(String sessionID, String wikiID, long time) {
		return basePath + 
			"/learning/viewTopic.do?sessionMapID=" + sessionID + 
			"&topicID=" + wikiID +
			"&create=" + time;
			
	}
	
	
	/**
	 * Retrieve the SessionMap from the HttpSession.
	 * 
	 * @param request
	 * @param authForm
	 * @return
	 */
	private SessionMap<String, Object> getSessionMap(
			HttpServletRequest request, String sessionID) 
	{
		SessionMap<String, Object> sess = (SessionMap<String, Object>) request.getSession().getAttribute(sessionID);
		
		return sess;
	}


	
}
