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
package org.lamsfoundation.lams.contentrepository.struts.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.AccessDeniedException;
import org.lamsfoundation.lams.contentrepository.FileException;
import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.contentrepository.IValue;
import org.lamsfoundation.lams.contentrepository.IVersionedNode;
import org.lamsfoundation.lams.contentrepository.ItemNotFoundException;
import org.lamsfoundation.lams.contentrepository.NodeType;
import org.lamsfoundation.lams.contentrepository.PropertyName;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.ValueFormatException;
import org.lamsfoundation.lams.contentrepository.service.IRepositoryService;
import org.springframework.beans.BeansException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


/**
 * This servlet must load on startup as it contains the link
 * to the repository required for the other servlets.
 * 
 * Specialised servlet that supports the rendering of packages.
 * It has a rather odd format - you can call it initially with 
 * the package id and uuid (and optional version) using 
 * download?uuid=&lt;uuid&gt;&version=&lt;version&gt;
 * but then it redirects to download/&lt;uuid&gt;/&lt;version&gt;/relPath 
 * where the &lt;uuid&gt; and &lt;version&gt; are the uuid and version
 * of the package node.
 * 
 * The download/&lt;uuid&gt;/&lt;version&gt;/relPath should only be used 
 * internally - the servlet should be called with the parameter
 * version initially.
 * 
 * It also supports the fetching of ordinary file nodes - in 
 * this case it doesn't need to do the mucking around with 
 * redirecting.
 * 
 * This / format allows the relative pathed links
 * within an html file to work properly. 
 * 
 * @author Fiona Malikoff
 */

/* A package node could be handled by either getting the
 stream from the package node - this is the first file
 in the package - or by using the property in the node
 that specifies the path to the first file and go back
 to the repository and get that node. In a roundabout 
 way, this servlet uses the second method - it redirects
 to the path for the first file.
 
 method 1: the package node returns a stream which is the first file.
  InputStream = node.getFile();
  set up any header variables
  <set up any header variables>

  method 2: get initial path node and download that 
  IValue value = node.getProperty(PropertyName.INITIALPATH);
  String initialPath = value != null ? value.getString() : null;
  IVersionedNode childNode = getRepository().getFileItem(ticket,uuid, initialPath, null);
  InputStream = node.getFile();
  <set up any header variables>
  <copy input stream to page output stream>
*/

public class Download extends HttpServlet {

	private static IRepositoryService repository = null;

	protected static Logger log = Logger.getLogger(Download.class);

	private static final String expectedFormat = 
			"Expected format /download?"
			+RepositoryDispatchAction.UUID_NAME
			+"<num>&"
			+RepositoryDispatchAction.VERSION_NAME
			+"=<num> (version number optional) or /download/<uuid>/<version>/<relPath>";
	/**
	 * Constructor of the object.
	 */
	public Download() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			handleCall(request, response);
		} catch (RepositoryCheckedException e) {
			errorInContent(request, response,"Repository threw an exception ",e);
		}
	}

	private void handleCall(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, BeansException, AccessDeniedException, ItemNotFoundException, FileException, ValueFormatException, IOException{
		
		long start = System.currentTimeMillis();

		ITicket ticket = RepositoryDispatchAction.getTicket(request);
		if ( ticket == null ) {
			errorInContent(request, response,"No ticket found in session. Unable to access .",null);
			return;
		}

		Long uuid = RepositoryDispatchAction.getLong(request.getParameter(RepositoryDispatchAction.UUID_NAME));
		Long version = null;

		String callId = null;

		if ( uuid != null ) {

			callId = "download "+Math.random()+" "+uuid;
			
			version = RepositoryDispatchAction.getLong(request.getParameter(RepositoryDispatchAction.VERSION_NAME));
			
			IVersionedNode node = getFileItem(ticket, uuid, version,null);
			log.debug(callId+" getFileItem1 "+(System.currentTimeMillis()-start));
			
			// update versionId in case it was null and we got the latest version...
			version = node.getVersion();
			
			if ( node.isNodeType(NodeType.PACKAGENODE) ) {
			
				// now get the path of the initial page in the package
				IValue value = node.getProperty(PropertyName.INITIALPATH);
				String initialPage = value != null ? value.getString() : null;
				if ( initialPage == null || initialPage.length() ==0 ) {
					errorInContent(request, response,"No initial page found for this set of content. Node Data is "+node.toString(),null);
					return;
				}

				// redirect to the initial path
				// prepend with servlet and id - initial call doesn't include the id
				// and depending on "/"s, the servlet name is sometimes lost by the redirect.
				initialPage = request.getRequestURL() + "/" + uuid 
					+ "/" + version + "/" + initialPage;
				log.debug("Request url was "+request.getRequestURL());
				log.debug("Servlet path was "+request.getServletPath());
				log.debug("Attempting to redirect to initial page "+initialPage);
				response.sendRedirect(initialPage);
				
			} else if ( node.isNodeType(NodeType.FILENODE) ) {

				handleFileNode(request, response, node);

			} else {
				errorInContent(request, response,"Unsupported node type "
						+node.getNodeType()+". Node Data is "+node.toString(),null);
				return;
			}
			
		} else {
			
			// using the /download/<id>/<filename> format - must be a file node!
			String pathString =  request.getPathInfo();
			String[] strings = deriveIdFile(pathString);
			uuid = RepositoryDispatchAction.getLong(strings[0]);
			version = RepositoryDispatchAction.getLong(strings[1]);
			String relPathString = strings[2];
			
			callId = "download "+Math.random()+" "+uuid;
			
			if ( uuid == null ) { 
				errorInContent(request, response, "UUID value is missing. "+expectedFormat,null);
				return;
			}
		
			if ( version == null ) {
				errorInContent(request, response, "Version value is missing. "+expectedFormat,null);
				return;
			}

			if ( relPathString == null ) {
				errorInContent(request, response, "Filename is missing. "+expectedFormat,null);
				return;
			}

			log.debug(callId+" beforeGetFileItem2 "+(System.currentTimeMillis()-start));
			IVersionedNode node = getFileItem(ticket, uuid, version, relPathString);
			log.debug(callId+" getFileItem2 "+(System.currentTimeMillis()-start));
			if ( ! node.isNodeType(NodeType.FILENODE) ) {
				errorInContent(request, response,"Unexpected type of node "
						+node.getNodeType()+" Expected File node. Data is "+node,null);
				return;
			}
			handleFileNode(request, response, node);

		}
		
		log.debug(callId+" handleFileNode "+(System.currentTimeMillis()-start));
		
	}

	/**
	 * The call getFileItem was throwing a runtime hibernate/jdbc error when being
	 * thrash tested, and I couldn't work out the context, so I've wrapped
	 * the call here so it can be debugged.
	 */
	private IVersionedNode getFileItem(ITicket ticket, Long uuid, Long version, String relPathString) 
				throws AccessDeniedException, ItemNotFoundException, FileException {
		try { 
			IVersionedNode node = null;
			if ( relPathString != null ) {
				// get file in package
				node = getRepository().getFileItem(ticket,uuid, version, relPathString);
			} else {
				// get node
				node = getRepository().getFileItem(ticket,uuid, version);
			}
			return node;
		} catch ( RuntimeException e ) {
			log.error("Exception thrown calling repository.getFileItem(<ticket>,"
					+uuid+","+version+","+relPathString+"). "+e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * @param response
	 * @param aNode
	 * @throws IOException
	 */
	protected void handleFileNode(HttpServletRequest request, HttpServletResponse response, IVersionedNode fileNode) 
		throws IOException, FileException, ValueFormatException {

		IValue prop = fileNode.getProperty(PropertyName.MIMETYPE);
		String mimeType = prop != null ? prop.getString() : null;

		if ( mimeType == null ) {
			prop = fileNode.getProperty(PropertyName.FILENAME);
			if ( prop != null ) {
				mimeType = getServletContext().getMimeType(prop.getString());
			}
		}

		if ( mimeType != null ) {
			response.setContentType(mimeType);
		}
		
		OutputStream os = response.getOutputStream();
		InputStream is = fileNode.getFile(); 
		for ( int c = is.read(); c != -1; c=is.read() ) {
			os.write(c);
		}
		os.close();
		is.close();
	}

	// Expect format /<id>/<version>/<relPath>. No parts are optional. Filename may be a path.
	// returns an array of three strings.
	private String[] deriveIdFile(String pathInfo) {
		String[] result = new String[3];
		
		if ( pathInfo != null ) {
		
			String[] strings = pathInfo.split("/",4);
			
			for ( int i=0, j=0; i<strings.length && j < 3 ; i++ ) {
				// splitting sometimes results in empty strings, so skip them!
				if ( strings[i].length() > 0 ) {
					result[j++] = strings[i];
				}
			}
			
		}
		log.debug("Split path into following strings: '"
					+result[0]
                    +"' '"+result[1]
					+"' '"+result[2]);

		return result;
	}
	

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			handleCall(request, response);
		} catch (RepositoryCheckedException e) {
			errorInContent(request, response,"Repository threw an exception ",e);
		}
	}

	public IRepositoryService getRepository() {
	    if ( repository == null ) {
	    	System.err.println("Repository Demo calling context and getting repository singleton.");
	        WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
	    	/*ApplicationContext context = new ClassPathXmlApplicationContext(IRepositoryService.REPOSITORY_CONTEXT_PATH); */
	    	repository = (IRepositoryService)wac.getBean(IRepositoryService.REPOSITORY_SERVICE_ID);
	    }
		return repository;
	}


	protected void errorInContent(HttpServletRequest request, HttpServletResponse response, String errMsg, Exception e ) throws IOException {

		log.error(errMsg,e);

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>Error Getting Document</TITLE></HEAD>");
		out.println("  <BODY>");
		out.println("<P>An error occurred: "+errMsg+"</p>");
		if ( e != null ) {
			out.println("<P>"+e.getMessage()+"</p>");
		}
		out.println("<P>Path details:</p>");
		out.println(dumpPath(request));
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	protected String dumpPath(HttpServletRequest request)
	{
		return "<TABLE>"
		 +"<TR><TD>getContextPath</TD><TD>"+request.getContextPath()+"</TD><TR>"
		 + "<TR><TD>getPathInfo</TD><TD>"+request.getPathInfo()+"</TD><TR>"
		 + "<TR><TD>getPathTranslated</TD><TD>"+request.getPathTranslated()+"</TD><TR>"
		 + "<TR><TD>getQueryString</TD><TD>"+request.getQueryString()+"</TD><TR>"
		 + "<TR><TD>getRequestURI</TD><TD>"+request.getRequestURI()+"</TD><TR>"
		 + "<TR><TD>getRequestURL</TD><TD>"+request.getRequestURL()+"</TD><TR>"
		 + "<TR><TD>getServletPath</TD><TD>"+request.getServletPath()+"</TD><TR>"
		 + "</TABLE> ";
	}

}
