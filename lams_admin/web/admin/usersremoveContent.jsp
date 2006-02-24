<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ page import="javax.servlet.jsp.JspWriter" %>
<%@ page import="javax.servlet.http.HttpServletRequest" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="org.lamsfoundation.lams.usermanagement.service.UserManagementService" %>
<%@ page import="org.lamsfoundation.lams.usermanagement.Role" %>
<%@ page import="org.lamsfoundation.lams.usermanagement.User" %>
<%@ page import="org.lamsfoundation.lams.usermanagement.Organisation" %>
<%@ page import="org.lamsfoundation.lams.admin.web.UsersRemoveActionForm" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-core" prefix="c" %>

<%!
	/* Display ther user map, for a particular right */
	public void displayUsers( List users, Organisation org, javax.servlet.jsp.JspWriter out, UserManagementService service )
	{
		if ( users == null )
			return;
			
		try {
			out.println("<table border=\"0\" width=\"100%\" class=\"lightTableBorders\">");
			out.println("<tr bgcolor=\"#333366\" class=\"tableHeader\"><th width=\"10%\">Remove?</th><th width=\"30%\">User</th><th>LAMS Roles</th></tr>");
			Iterator iter = users.iterator();
			while ( iter.hasNext() )
			{
				displayUser((User)iter.next(),org,out,service );
			}
			out.println("</TABLE>");
		} catch ( java.io.IOException e ) {
			System.err.println("Internal Error: Unable to show user details");
			e.printStackTrace(System.err);
		}

	}
	
	/* Display the user details, in a single column table row */
	public void displayUser( User user, Organisation org, javax.servlet.jsp.JspWriter out, UserManagementService service) throws java.io.IOException
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

<html:form action="/usersremove" method="post">
	<table width="100%" height="177" border="0" cellpadding="5" cellspacing="0" bgcolor="#FFFFFF">
		<tr> 
			<td valign="top">
				<bean:define id="orgId" name="<%=UsersRemoveActionForm.formName%>" property="orgId" type="Integer"/>
				<% 
					WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext()); 
					UserManagementService service = (UserManagementService)ctx.getBean("userManagementServiceTarget");
					Organisation org = service.getOrganisationById(orgId);
				%>
				<H2>Remove Users From <%=org.getName()%></H2>
				<p>Select the users that you wish to remove from the organisation.</p>
				<html:errors/>							
				<hr>
				<bean:define id="users" name="<%=UsersRemoveActionForm.formName%>" property="users" type="List"/>
				<%	
					displayUsers(users,org,out,service); 
				%>
				<hr>
				<p>
					<html:submit>Remove Users</html:submit> &nbsp; 	
					<html:cancel>Cancel</html:cancel> &nbsp;
				</p>
			</td>
		</tr>
	</table>
</html:form>
