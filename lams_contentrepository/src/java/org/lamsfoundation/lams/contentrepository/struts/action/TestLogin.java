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

package org.lamsfoundation.lams.contentrepository.struts.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lamsfoundation.lams.contentrepository.ICredentials;
import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.service.SimpleCredentials;


/** 
 * This standalone servlet is for performance testing. It acts
 * as an "interface" between JMeter and the Repository jar file.
 * 
 * All it does is logs in and puts the ticket in the session.
 * It is designed to be as bare bones as possible, so that the 
 * performance of the servlet doesn't affect the testing too much.
 * 
 * The user and the workspace must already exist.
 * 
 * Parameters w=<workspaceName>&t=<toolname>&id=<tool id / password>
 * e.g. /lamscr/testlogin?w=atoolworkspace&t=atool&id=atool
 * 
 * The login can be followed up with a call to download to get
 * a file
 * e.g. /lamscr/download/3/1/index.html, 
 * /lamscr/download/3/1/images/giralookout.jpg,
 * 
 */
public class TestLogin extends HttpServlet  {

	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			login(request);
			output(response);
		} catch ( RepositoryCheckedException e ) {
			outputError(response, e);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		try {
			login(request);
			output(response);
		} catch ( RepositoryCheckedException e ) {
			outputError(response, e);
		}
	}

	/* Get the repository bean and connect to the desired workspace. May need to create the
	 * workspace first - depends on createWorkspaceFirst flag.
	 * RepositoryCheckedException is the superclass for all the other repository checked exceptions
	 * thrown by this method.
	 * 
	 * Putting System.out.printlns and running some JMeter tests showed usually
	 * 0 ms between start and calling login, and 0 to 94 ms between the return
	 * of the login call and the end of the function. During those tests, logins
	 * took between 15 ms and 3641 ms, and putting of the ticket in the 
	 * request took at most 94ms. The test combined
	 * with downloading a large file, which creates variability. It was considered
	 * "verified" that most of the time is the login call, and this will be reasonably
	 * fast unless the system is overloaded.
	 */
	protected void login (HttpServletRequest request) throws RepositoryCheckedException {
		
		String callId = "login handleCall "+Math.random();
		long start = System.currentTimeMillis();
		
		String workspaceName = request.getParameter("w");
		String toolName = request.getParameter("t");
		char[] toolId = request.getParameter("id").toCharArray();

		ICredentials cred =  new SimpleCredentials(toolName, toolId); 
		ITicket ticket = Download.getRepository().login(cred, workspaceName);
		request.getSession().setAttribute(RepositoryDispatchAction.TICKET_NAME, ticket);
	}
		
	protected void output(HttpServletResponse response ) throws IOException {
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>Logged In</TITLE></HEAD>");
		out.println("  <BODY>");
		out.println("<P>We have logged in.</p>");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	protected void outputError(HttpServletResponse response, RepositoryCheckedException e ) throws IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>Error Getting Document</TITLE></HEAD>");
		out.println("  <BODY>");
		out.println("<P>An error occurred: "+e.getMessage()+"</p>");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
		
		System.err.println("Error thrown logging in.");
		e.printStackTrace(System.err);
	}

	

}