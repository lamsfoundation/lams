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
package org.lamsfoundation.lams.tool.rsrc.web.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.AccessDeniedException;
import org.lamsfoundation.lams.contentrepository.FileException;
import org.lamsfoundation.lams.contentrepository.IValue;
import org.lamsfoundation.lams.contentrepository.IVersionedNode;
import org.lamsfoundation.lams.contentrepository.ItemNotFoundException;
import org.lamsfoundation.lams.contentrepository.NodeType;
import org.lamsfoundation.lams.contentrepository.PropertyName;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.ValueFormatException;
import org.lamsfoundation.lams.tool.rsrc.service.IResourceService;
import org.lamsfoundation.lams.tool.rsrc.service.ResourceServiceProxy;
import org.springframework.beans.BeansException;


/**
* Specialised servlet that supports the rendering of packages.
* It has a rather odd format - you can call it with 
* the uuid, version id and path of the content as
* download/&lt;uuid&gt;/&lt;version&gt;/relPath. 
* 
* This / format allows the relative pathed links
* within an html file to work properly. 
*
* Based on the sample Download servlet in the content repository
* package.
*  
* @author Fiona Malikoff
*/

public class Download extends HttpServlet {

	protected static Logger log = Logger.getLogger(Download.class);

	private static final String expectedFormat = 
			"Expected format /download/&lt;packageId&gt;/&lt;relPath&gt;";
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
		
		String pathString =  request.getPathInfo();
		String[] strings = deriveIdFile(pathString);
		Long packageId = NumberUtils.createLong(strings[0]);
		String relPathString = strings[1];
		
		if ( packageId == null ) { 
			errorInContent(request, response, "Package ID value is missing. "+expectedFormat,null);
			return;
		}

		if ( relPathString == null ) {
			errorInContent(request, response, "Filename is missing. "+expectedFormat,null);
			return;
		}

		IVersionedNode node = null;
		
		try { 
			
			IResourceService service = getService(this.getServletContext());
			node = service.getFileNode(packageId, relPathString);
			if ( ! node.isNodeType(NodeType.FILENODE) ) {
				errorInContent(request, response,"Unexpected type of node "
						+node.getNodeType()+" Expected File node. Data is "+node,null);
				return;
			}
			handleFileNode(request, response, node);

		} catch ( Exception e ) {
			
			log.error("Download servlet: Exception occured "+e.getMessage());
			errorInContent(request, response, "Exception occurred getting file from content repository. ", e );
			
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
		String[] result = new String[2];
		
		if ( pathInfo != null ) {
		
			String[] strings = pathInfo.split("/",3);
			
			for ( int i=0, j=0; i<strings.length && j < 2 ; i++ ) {
				// splitting sometimes results in empty strings, so skip them!
				if ( strings[i].length() > 0 ) {
					result[j++] = strings[i];
				}
			}
			
		}
		log.debug("Split path into following strings: '"
					+result[0]
                  +"' '"+result[1]);

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
		return "<TABLE summary=\"This table is being used for layout purposes only\">"
		 +"<TR><TD>getContextPath</TD><TD>"+request.getContextPath()+"</TD><TR>"
		 + "<TR><TD>getPathInfo</TD><TD>"+request.getPathInfo()+"</TD><TR>"
		 + "<TR><TD>getPathTranslated</TD><TD>"+request.getPathTranslated()+"</TD><TR>"
		 + "<TR><TD>getQueryString</TD><TD>"+request.getQueryString()+"</TD><TR>"
		 + "<TR><TD>getRequestURI</TD><TD>"+request.getRequestURI()+"</TD><TR>"
		 + "<TR><TD>getRequestURL</TD><TD>"+request.getRequestURL()+"</TD><TR>"
		 + "<TR><TD>getServletPath</TD><TD>"+request.getServletPath()+"</TD><TR>"
		 + "</TABLE> ";
	}

	protected  static IResourceService getService(ServletContext servletContext) {
	   		return ResourceServiceProxy.getResourceService(servletContext);
	}
}
