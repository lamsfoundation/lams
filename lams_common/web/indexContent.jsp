<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="com.lamsinternational.lams.usermanagement.service.UserManagementService" %>
<%@ page import="com.lamsinternational.lams.usermanagement.*" %>
<%@ taglib uri="/WEB-INF/jstl/c.tld" prefix="c" %>
<html>
<head>
	<script language="JavaScript" type="text/javascript" src="getSysInfo.js"></script>
	<script language="JavaScript" type="text/javascript" src="openUrls.js"></script>
</head>
<form name="form" method="post">
<table width="98%" height="100%" border="0" align="center" cellpadding="2" cellspacing="0">
	<tr>
		<td align="center" valign="middle"><img height="7" src="images/spacer.gif" width="10" /></td>
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
							<%List list = service.getOrganisationsForUserByRole(user,Role.SYSADMIN);
							if(list.size()>0){%>
								<tr>
									<td align="left">
										<input name="sysadmin" type="button" id="sysadmin" onClick="openSysAdmin();" value="SysAdmin" style="width:100" />
									</td>
									<td align="left">
										<select name="orgIdForSysAdmin">
										<%for(int i=0;i<list.size();i++){
											Organisation org = (Organisation)list.get(i);%>
											<option value="<%=org.getOrganisationId()%>"><%=org.getName()%></option>
										<%}%>
										</select> 
									</td>
								</tr>
							<%}%>
							<%list = service.getOrganisationsForUserByRole(user,Role.ADMIN);
								if(list.size()>0){%>
								<tr>
									<td align="left">
										<input name="admin" type="button" id="admin" onClick="openAdmin();" value="Admin" style="width:100" />
									</td>
									<td align="left">
										<select name="orgIdForAdmin">
										<%for(int i=0;i<list.size();i++){
											Organisation org = (Organisation)list.get(i);%>
											<option value="<%=org.getOrganisationId()%>"><%=org.getName()%></option>
										<%}%>
										</select> 
									</td>
								</tr>
							<%}%>
							<%list = service.getOrganisationsForUserByRole(user,Role.STAFF);
								if(list.size()>0){%>
								<tr>
									<td align="left">
										<input name="staff" type="button" id="staff" onClick="openStaff();" value="Staff" style="width:100" />
									</td>
									<td align="left">
										<select name="orgIdForStaff">
										<%for(int i=0;i<list.size();i++){
											Organisation org = (Organisation)list.get(i);%>
											<option value="<%=org.getOrganisationId()%>"><%=org.getName()%></option>
										<%}%>
										</select> 
									</td>
								</tr>
							<%}%>
							<%list = service.getOrganisationsForUserByRole(user,Role.AUTHOR);
								if(list.size()>0){%>
								<tr>
									<td align="left">
										<input name="author" type="button" id="author" onClick="openAuthor();" value="Author" style="width:100" />
									</td>
									<td align="left">
										<select name="orgIdForAuthor">
										<%for(int i=0;i<list.size();i++){
											Organisation org = (Organisation)list.get(i);%>
											<option value="<%=org.getOrganisationId()%>"><%=org.getName()%></option>
										<%}%>
									</select> 
									</td>
								</tr>
							<%}%>
							<%list = service.getOrganisationsForUserByRole(user,Role.LEARNER);
							if(list.size()>0){%>
								<tr>
									<td align="left">
										<input name="learner" type="button" id="learner" onClick="openLearner();" value="Learner" style="width:100" />
									</td>
									<td align="left">
										<select name="orgIdForLearner">
										<%for(int i=0;i<list.size();i++){
											Organisation org = (Organisation)list.get(i);%>
											<option value="<%=org.getOrganisationId()%>"><%=org.getName()%></option>
										<%}%>
										</select> 
									</td>
								</tr>
							<%}%>
						</table>
					</td>
				</tr>
				<tr align="center" valign="bottom">
					<td colspan="2" ><img height="274" src="images/launch_page_graphic.jpg" width="587"></td>
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