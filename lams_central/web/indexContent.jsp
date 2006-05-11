<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>
<%@ page import="java.util.*" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="org.lamsfoundation.lams.usermanagement.service.UserManagementService" %>
<%@ page import="org.lamsfoundation.lams.usermanagement.Role" %>
<%@ page import="org.lamsfoundation.lams.usermanagement.User" %>
<%@ page import="org.lamsfoundation.lams.usermanagement.dto.OrganisationDTO" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<html>
<head>
	<script language="JavaScript" type="text/javascript" src="includes/javascript/getSysInfo.js"></script>
	<script language="JavaScript" type="text/javascript" src="includes/javascript/openUrls.js"></script>
</head>
<form name="form" method="post">
<table width="98%" height="100%" border="0" align="center" cellpadding="2" cellspacing="0">
	<tr>
		<td align="center" valign="middle"><img height="7" src="images/spacer.gif" width="10" alt="spacer.gif"/></td>
	</tr>
	<tr>
		<td height="100%" valign="top">
			<%String login = request.getRemoteUser();
			WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext()); 
			UserManagementService service = (UserManagementService)ctx.getBean("userManagementServiceTarget");
			User user = service.getUserByLogin(login);
			if ( login==null ){%>
				<P class="error">An error has occured. You have tried to log
				in but we didn't get the username. Try closing your browser and starting
				again.</p>
			<%}%>
			<table width="70%" height="100%" border="0" align="center" cellpadding="2" cellspacing="0">
				<tr>
					<td width="50%" align="left" valign="top" >
						<p class="mainHeader">Welcome <%=user.getFirstName()%></p>
						<p class="body">
							You are logged into LAMS.Please choose a workspace from the buttons on the right or
							<a href="home.do?method=passwordChange"> Change Password </a>here.			
						</p>
						<script language="JavaScript" type="text/javascript">
						<!--
							// if it's a mac i can't just focus as i don't know if the window is open or not,
							// need to give this warning.
							if(isMac)
							{
								document.writeln(
								'<p class="note">Note: If your workspace is already open' +
								' clicking the button will re-load the window and unsaved work' +
								' may be lost.</p>' );
							}
						//-->
						</script>
					</td>
					<td width="50%" align="right" valign="top">
						<table border="0" cellpadding="0" cellspacing="3">
							<!-- If we are a sysadmin for any org, then we are sysadmin for everything -->
							<% ArrayList roleList = new ArrayList();
							   roleList.add(Role.SYSADMIN);
							   OrganisationDTO orgDTO = service.getOrganisationsForUserByRole(user,roleList);
								if(orgDTO!=null){%>
								<tr>
									<td align="left" colspan="4">
										<input name="sysadmin" type="button" id="sysadmin" onClick="openSysadmin(1);" value="System Adminstration"/>
									</td>
								</tr>
							<%}%>
							<%orgDTO = service.getOrganisationsForUserByRole(user,null);
	  						  if(orgDTO!=null){
	  						  		Vector courses = orgDTO.getNodes();
	  						  		Iterator courseIter = courses.iterator();
	  						  		while ( courseIter.hasNext() ) {
	  						  		
										OrganisationDTO course = (OrganisationDTO)courseIter.next();%>
	
										<tr><td align="left">Course: <%=course.getName()%>:</td>
										<td align="left" >
										<% Vector roleNames	= course.getRoleNames();
											if ( roleNames.contains(Role.AUTHOR) ) {%>
											    <input name="author" type="button" id="author" onClick="openAuthor(<%=course.getOrganisationID()%>);" value="Author"/> 
										<% } %> 
										</td>
										<td align="left" >
										<% if ( roleNames.contains(Role.STAFF) || roleNames.contains(Role.TEACHER) ) {%>
												<input name="staff" type="button" id="staff" onClick="openStaff(<%=course.getOrganisationID()%>);" value="Staff"/>
	 							    	<% } %> 
		 							    </td>
										<td align="left" >
										<% if ( roleNames.contains(Role.LEARNER) ) {%>
												<input name="learner" type="button" id="learner" onClick="openLearner(<%=course.getOrganisationID()%>);" value="Learner"/>
	 								    <% } %> 
		 							    </td>
		 							    </tr>
		 							    
		 							    <% 
		 							    Vector classes = course.getNodes();
		  						  		Iterator classIter = classes.iterator();
		  						  		while ( classIter.hasNext() ) {
		  									OrganisationDTO courseClass = (OrganisationDTO)classIter.next(); %>
		  											  		
											<tr><td align="left">Class: <%=courseClass.getName()%>:</td>
											<td align="left" >
											<% Vector classRoleNames	= courseClass.getRoleNames();
												if ( classRoleNames.contains(Role.AUTHOR) ) {%>
												    <input name="author" type="button" id="author" onClick="openAuthor(<%=courseClass.getOrganisationID()%>);" value="Author"/> 
											<% } %> 
											</td>
											<td align="left" >
											<% if ( classRoleNames.contains(Role.STAFF) || classRoleNames.contains(Role.TEACHER) ) {%>
													<input name="staff" type="button" id="staff" onClick="openStaff(<%=courseClass.getOrganisationID()%>);" value="Staff"/>
		 							    	<% } %> 
			 							    </td>
											<td align="left" >
											<% if ( classRoleNames.contains(Role.LEARNER) ) {%>
													<input name="learner" type="button" id="learner" onClick="openLearner(<%=courseClass.getOrganisationID()%>);" value="Learner"/>
		 								    <% } %> 
			 							    </td>
											</tr>
		  						  		<% }%>
		  						  <% } %>
							<%}%>
						</table>
					</td>
				</tr>
				<tr align="center" valign="bottom">
					<td colspan="2" ><img height="274" src="images/launch_page_graphic.jpg" width="587" alt="launch_page_graphic.jpg"></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr valign="bottom">
		<td>	
			<table width="100%" border="0" cellpadding="0" cellspacing="0" class="lightNote">
				<tr valign="bottom">
					<td height="12">
						<a href="javascript:alert('LAMS&#8482; &copy; 2002-2005 LAMS Foundation. 
							\nAll rights reserved.
							\n\nLAMS is a trademark of LAMS Foundation.
							\nDistribution of this software is prohibited.');" 
							class="lightNoteLink">&copy; 2002-2004 LAMS Foundation.
						</a>
					</td>
					<td align="center">
						This copy of LAMS&#8482; is authorised for use by the registered users only.
					</td>
					<td align="right">Version 1.1</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</form>
</html>