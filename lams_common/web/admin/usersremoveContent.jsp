<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ page import="javax.servlet.jsp.JspWriter" %>
<%@ page import="javax.servlet.http.HttpServletRequest" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="com.lamsinternational.lams.usermanagement.service.UserManagementService" %>
<%@ page import="com.lamsinternational.lams.usermanagement.Role" %>
<%@ page import="com.lamsinternational.lams.usermanagement.User" %>
<%@ taglib uri="/WEB-INF/jstl/c.tld" prefix="c" %>

<%!
	/* Display ther user map, for a particular right */
	public void displayUsers( List users, javax.servlet.jsp.JspWriter out, UserManagementService service )
	{
		if ( userMap == null )
			return;
			
		try {
			out.println("<table border=\"1\" width=\"100%\">");
			out.println("<tr><td width=\"10%\">Remove?</td><td width=\"30%\">User</td><td>LAMS Roles</td></tr>");
			Iterator iter = users.iterator();
			while ( iter.hasNext() )
			{
				displayUser((User)iter.next(), out,service );
			}
			out.println("</TABLE>");
		} catch ( java.io.IOException e ) {
			System.err.println("Internal Error: Unable to show user details");
			e.printStackTrace(System.err);
		}

	}
	
	/* Display the user details, in a single column table row */
	public void displayUser( User user, javax.servlet.jsp.JspWriter out, UserManagementService service) throws java.io.IOException
	{
		out.println("<TR>");

		out.println("<TD class=\"body\"><input type=\"checkbox\" name=\"toRemove\" value=\""
			+ user.getUserId()	+"\"></TD>");	

		out.println("<TD class=\"body\">"+user.getLogin()+": "+user.getFirstName()+" "+user.getLastName()+"</TD>");
		
		out.print("<TD class=\"body\">");
		Iterator iter = service.getRolesForUserByOrganisation(user,org.getOrganisationId()).iterator();
		while ( iter.hasNext() )
		{
			Role role = (Role) iter.next();
			out.print(role.getName()+" &nbsp;");
		}
		out.println("</TD>");
		out.println("</TR>");
	}
%>

	<html:form action="/usersremove" name="<%=UsersRemoveActionForm.formName%>" type="com.webmcq.ld.usermanagement.sysadmin.web.UsersRemoveActionForm">

          <table width="100%" height="177" border="0" cellpadding="5" cellspacing="0" bgcolor="#FFFFFF">
            <tr> 
              <td valign="top">
				<H2>Remove Users: <bean:write name="<%=UsersRemoveActionForm.formName%>" property="name"/></H2>

				<p>Select the users that you wish to remove from the organisation.</p>
				
				<html:errors/>							
				
				<hr>
				
				<bean:define id="users" name="<%=UsersRemoveActionForm.formName%>" property="users" type="List"/>
				<% 
				WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext()); 
				UserManagementService service = (UserManagementService)ctx.getBean("userManagementServiceTarget");
				displayUsers(users,out,service); 
				%>

				<hr>
				
				<p><html:cancel>Cancel</html:cancel> &nbsp;
					<html:submit>Remove Users</html:submit> &nbsp; 	
			</p>
		   </td>
			 </tr>
			</table>
         
  </html:form>
