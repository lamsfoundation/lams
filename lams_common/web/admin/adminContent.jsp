<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ page import="java.io.IOException" %>
<%@ page import="javax.servlet.jsp.JspWriter" %>
<%@ page import="javax.servlet.http.HttpServletRequest" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="com.lamsinternational.lams.usermanagement.service.UserManagementService" %>
<%@ page import="com.lamsinternational.lams.usermanagement.Organisation" %>
<%@ page import="com.lamsinternational.lams.usermanagement.Role" %>
<%@ page import="com.lamsinternational.lams.usermanagement.User" %>
<%@ taglib uri="/WEB-INF/jstl/c.tld" prefix="c" %>

<html>
<head>
<script language="JavaScript" type="text/javascript">
<!--
	function validateForm(){
		var error = "";
		if(document.forms[0].file.value  == "" || document.forms[0].file.value == null){
			error += "\n- Please choose a file to upload";
		}else {
			var filename = document.forms[0].file.value;
			ext = filename.substring(filename.lastIndexOf(".")+1,filename.length);
			if(ext!="xls")
				error +="\n- Please choose an Excel format file";
		}
		if(error == ""){
			document.forms[1].submit();
		}else{
			alert(error);
			return false;
		}
	}
//-->
</script>
</head>

<%!
	/* Display the user map */
	public void displayUsers( List users, Organisation org, JspWriter out, HttpServletRequest request, UserManagementService service)
	{
		if ( users == null)
			return;
			
		try {
			Iterator iter = users.iterator();
			while ( iter.hasNext() )
			{
				User user = (User) iter.next();
				displayUser(user, org, out, request,service);
			}
		} catch (IOException e ) {
			System.err.println("Internal Error: Unable to show user details");
			e.printStackTrace(System.err);
		}
	}
	
	/* Display the user details, in a single column table row */
	public void displayUser( User user,Organisation org, JspWriter out, HttpServletRequest request,UserManagementService service) throws IOException
	{
		out.println("<TR>");
	
		out.println("<TD class=\"body\">"+user.getLogin()+": "+user.getFirstName()+" "+user.getLastName());
		out.println("</TD>");
		out.print("<TD class=\"body\">");
		Iterator iter = service.getRolesForUserByOrganisation(user,org.getOrganisationId()).iterator();
		while ( iter.hasNext() )
		{
			Role role = (Role) iter.next();
			out.print(role.getName()+" &nbsp;");
		}
		out.println("</TD><TD align=\"center\">");
		out.println("<input name=\"updateUser\" type=\"button\" class=\"button\" id=\"updateUser\"");
		out.println("onClick=\"javascript:document.location='admin.do?method=getUserEdit&userid="+user.getUserId()+"&organisationid="+org.getOrganisationId()+"';\"");
		out.println("onMouseOver=\"changeStyle(this,'buttonover')\"");
		out.println("onMouseOut=\"changeStyle(this,'button')\"");
		out.println("value=\"Update\" />");
		out.println("</TD>");
		
		out.println("</TR>");
	}
%>
<table width="100%" height="177" border="0" cellpadding="5" cellspacing="0" bgcolor="#FFFFFF">
	<tr> 
		<td valign="top">
			<span class="heading">Maintain Organisations and Users</span>
			<table width="80%"  border="0" cellpadding="5" cellspacing="0" bgcolor="#FFFFFF">
				<tr>
					<td class="body" align="left"><strong>Organisation: </strong> <c:out value="${organisation.name}"/></td>
				</tr>
				<tr>	
					<td class="body" align="left"><strong>Creation Date: </strong> <c:out value="${organisation.createDate}"/></td>
				</tr>
				<tr>	
					<td class="body" align="left">
						<strong>Organisation Description:</strong> <c:out value="${organisation.description}"/>
					</td>
				</tr>
				<tr>				
					<td class="body" align="left">
						<strong>Organisation Type:</strong> <c:out value="${organisation.organisationType.name}"/>
					</td>				
				</tr>
				<tr>	
					<td class="body" align="left">
						<strong>Organisation Type Description:</strong> <c:out value="${organisation.organisationType.description}"/>
					</td>				
				</tr>	
			</table>
			<HR>
	
			<p>
				<input name="update" type="button" class="extendingButton" id="update" 
					onClick="javascript:document.location='admin.do?method=getOrganisationEdit&orgId=<c:out value="${organisation.organisationId}"/>';" 		
					onMouseOver="changeStyle(this,'extendingButtonover')"
					onMouseOut="changeStyle(this,'extendingButton')" 
					value="Update this Organisation" /> 
				&nbsp;			
				<input name="createOrg" type="button" class="extendingButton" id="createOrg" 
					onClick="javascript:document.location='admin.do?method=getOrganisationEdit&orgId=-1&parentOrgId=<c:out value="${organisation.organisationId}"/>';" 		
					onMouseOver="changeStyle(this,'extendingButtonover')"
					onMouseOut="changeStyle(this,'extendingButton')" 
					value="Create child organisation" />
			</p>
	
			<HR>				
			
			<span class="subHeader">
				Create records for all the people in your organisation who will use LAMS. Add each person only once.
			</span>
			<br><br>
		
			<form action="admin.do?method=importUsersFromFile&orgId=<c:out value="${organisation.organisationId}"/>" onsubmit="return validateForm()" method="post" ENCTYPE='multipart/form-data' name="form1" id="form1">										
				<c:if test="${!empty errormsg}">
					<p align="center"  class="error"><c:out value="${errormsg}"/></p>
				</c:if>	
				<p align="center" class="body">If you are not sure of the file format, please <a href="..\lams\admin\file\tmpl_admin.xls">download template file</a> </p>										
				<table width="95" border="0" align="center">
					<tr>
						<td class="body"> <span class="bodyBold">Import Users From File:</span><br>
							<input type="file" name="file" size="50" /> 
						</td>
					</tr>
					<tr>
						<td class="body">&nbsp;</td>
					</tr>
					<tr>
						<td align="center" valign="top">
							<input name="addUsers" 
								type="submit" size="100" 
								class="button" 
								id="addUsers" 
								onMouseOver="changeStyle(this,'buttonover')" 
								onMouseOut="changeStyle(this,'button')" 
								value="Add Users">
							<input name="checkbox" 
								type="checkbox" 
								value="existing users only"><span class="body">existing users only</span>
						</td>
					</tr>
				</table>
				<br>
				<br>
			</form>
	
			<p>
				<input name="createUser" type="button" class="extendingButton" id="createUser" 
					onClick="javascript:document.location='admin.do?method=getUserEdit&organisationid=<c:out value="${organisation.organisationId}"/>';" 		
					onMouseOver="changeStyle(this,'extendingButtonover')"
					onMouseOut="changeStyle(this,'extendingButton')" 
					value="Create new user" /></input>
				&nbsp;			
				<input name="addUsers" type="button" class="extendingButton" id="addUsers" 
					onClick="javascript:document.location='admin.do?method=getOrganisationAddUsers&organisationid=<c:out value="${organisation.organisationId}"/>';" 		
					onMouseOver="changeStyle(this,'extendingButtonover')"
					onMouseOut="changeStyle(this,'extendingButton')" 
					value="Add existing user" />
				&nbsp;			
				<input name="removeUsers" type="button" class="extendingButton" id="removeUsers" 
					onClick="javascript:document.location='admin.do?method=getOrganisationRemoveUsers&organisationid=<c:out value="${organisation.organisationId}"/>';" 		
					onMouseOver="changeStyle(this,'extendingButtonover')"
					onMouseOut="changeStyle(this,'extendingButton')" 
					value="Remove Users" />
			</p>
				
			<p class="body"></p>
		
			<table width="95%" border="0" align="center" class="lightTableBorders">
				<tr bgcolor="#333366">
					<td height="24" colspan="2" class="subHeader"><font color="#FFFFFF">Users</td>
					<td width="12%">&nbsp;</td>
					<!-- Comment out temporarily for beta 6 release -->
					<!-- <td width="8%">&nbsp;</td> -->
				</tr>
				<tr bgcolor="#669999" class="tableHeader">
					<td width="40%">User</td>
					<td width="40%">Roles</td>
					<td width="12%">&nbsp;</td>
					<!-- Comment out temporarily for beta 6 release -->
					<!-- <td width="8%">&nbsp;</td> -->
				</tr>

				<jsp:useBean id="organisation" type="Organisation" scope="request"/>
				<%
				WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext()); 
				UserManagementService service = (UserManagementService)ctx.getBean("userManagementServiceTarget");
				List users = service.getUsersFromOrganisation(organisation.getOrganisationId()); 
				displayUsers(users, organisation, out, request,service); 
				%>
			</table>
		
			<br><br>
				
			<table width="100%" border="0">
				<tr>
					<td class="body" colspan="2" align="right">
						<input name="button4" type="button" class="longButton" id="button4" value="Help!"
							onClick="window.open('help','helpWindow','resizable,width=796,height=570,scrollbars');" 
							onMouseOver="changeStyle(this,'longButtonover')"
							onMouseOut="changeStyle(this,'longButton')"  />
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</html>