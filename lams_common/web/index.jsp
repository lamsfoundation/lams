<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="com.lamsinternational.lams.util.JspRedirectStrategy" %>
<%@ page import="com.lamsinternational.lams.usermanagement.service.UserManagementService" %>
<%@ page import="com.lamsinternational.lams.usermanagement.*" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c_rt" %>
<%@ page session="true" %>

<html>

<head>
	<title>Welcome :: LAMS</title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
	<script language="JavaScript" type="text/javascript">
	<!--
		var isWin = false;
		var isMac = false;

		function Its()
		{
			var n = navigator;
			// string comparisons are much easier if we lowercase everything now.
			// to make indexOf() tests more compact/readable, we prepend a space 
			// to the userAgent string (to get around '-1' indexOf() comparison)
			var ua = ' ' + n.userAgent.toLowerCase();
			var pl = n.platform.toLowerCase(); // not supported in NS3.0
			var an = n.appName.toLowerCase();
	
			// browser version
			this.version = n.appVersion;
			// debug
			//	document.writeln("ua="+ua+"<br/>");
			//	document.writeln("pl="+pl+"<br/>");
			//	document.writeln("an="+an+"<br/>");
	
			this.nn = ua.indexOf('mozilla') > 0;
	
			// 'compatible' versions of mozilla aren't navigator
			if(ua.indexOf('compatible') > 0)
			{
				this.nn = false;
			}
	
			this.opera = ua.indexOf('opera') > 0;
			this.webtv = ua.indexOf('webtv') > 0;
			this.ie = ua.indexOf('msie') > 0;
			this.aol = ua.indexOf('aol') > 0;
			this.omniweb = ua.indexOf('omniweb') > 0;
			this.galeon = ua.indexOf('galeon') > 0;
	
			this.major = parseInt( this.version );
			this.minor = parseFloat( this.version );
	
			// workarounds
			// - IE5 & 6 reports itself as version 4.0
			if(this.ie)
			{
				if(ua.indexOf("msie 5") > 0)
				{
					this.major = 5;
					var actual_index = ua.indexOf("msie 5");
					//alert("indexOf msie 5:"+actual_index);
					var actual_major = ua.substring(actual_index + 5, actual_index + 8);
					//alert("actual_major:"+actual_major);
					this.minor = parseFloat(actual_major);	
				}
				else if (ua.indexOf("msie 6") > 0)
				{
					this.major = 6;
					var actual_index = ua.indexOf("msie 6");
					//alert("indexOf msie 6:"+actual_index);
					var actual_major = ua.substring(actual_index + 5, actual_index + 8);
					//alert("actual_major:"+actual_major);
					this.minor = parseFloat(actual_major);
				}
			}
	
			this.screenheight=screen.height;
			this.screenwidth=screen.width;
			
			if (ua.indexOf("mac") != -1)
			{
				isMac = true;
			}
			
			if (ua.indexOf("win") != -1)
			{
				isWin = true;
			}
		
			this.platformDetected = "";
			
			if(ua.indexOf("16")  != -1  && isWin){
				this.platformDetected = "win16";
			}else if(ua.indexOf("95") != -1  && isWin){
				this.platformDetected = "win95";
			}else if(ua.indexOf("98")!= -1  && isWin){
				this.platformDetected = "win98";
			}else if(ua.indexOf("win 9x 4.90") != -1  && isWin){
				this.platformDetected = "winme";
			}else if(ua.indexOf("nt 5.1")!= -1  && isWin){
				this.platformDetected = "winxp";
			}else if(ua.indexOf("nt 5") != -1  && isWin){
				this.platformDetected = "win2000";
			}else if(ua.indexOf("nt") != -1  && isWin){
				this.platformDetected = "winnt";
			}else if((ua.indexOf("68k") != -1 || ua.indexOf("68000") != -1) && isMac){
				this.platformDetected = "mac68000";
			}else if((ua.indexOf("ppc") != -1 || ua.indexOf("powerpc") != -1) && isMac){
				this.platformDetected = "macppc";
			}else{
				this.platformDetected = "unknown";
			}
		
			//alert( "platform=" + this.platformDetected );
			
			  return this;
			}
	
			var its = new Its();
			/*for LAMS min spec is:
			*		Platform:	WIndows 	98 / NT4 / NT5 (2K) / XP
			*					Macintosh	PPC
			*		Browser:	Internet Explorer 5 +
			*		Flash:		6,0,47,0 (Version 6 Relase 47)
			*/
		//-->
	</script>

	<script language="JavaScript" type="text/javascript">
	<!--
		// resolution checking
		var belowMinRes;
		if(screen.width <= 800 && screen.height <= 600){
			belowMinRes = true;
		}else{
			belowMinRes = false;
		}
		
		var authorWin = null;
		var learnWin = null;
		var teachWin = null;
		var adminWin = null;
		
		function openAuthor( )
		{
			//alert( "open author" );
			
			//cannot check if window is still open on macIE 5
			if(isMac)
			{
				authorWin = window.open('home.do?method=author','aWindow','width=796,height=570,resizable');
			}
			else
			{
				if(authorWin && authorWin.open && !authorWin.closed)
				{
					authorWin.focus();
				}
				else
				{
					authorWin = window.open('home.do?method=author','aWindow','width=796,height=570,resizable');
					authorWin.focus();
					//parent.close();
				}
			}
		}
		
		function openStaff( )
		{
			if(isMac)
			{
				if(belowMinRes)
				{
					teachWin = window.open('home.do?method=staff','tWindow','width=796,height=575,resizable,scrollbars');
				}
				else
				{
					teachWin = window.open('home.do?method=staff','tWindow','width=779,height=575');
				}
			}
			else
			{
				if(teachWin && teachWin.open && !teachWin.closed)
				{
					teachWin.focus();
				}
				else
				{
					teachWin = window.open('home.do?method=staff','tWindow','width=779,height=575,resizable');
				}
			}
		}
		
		function openLearner()
		{
			if(isMac)
			{
				learnWin = window.open('home.do?method=learner','lWindow','width=796,height=570,resizable,status=yes');
			}
			else
			{
				if(learnWin && learnWin.open && !learnWin.closed )
				{
					learnWin.focus();
				}
				else
				{
					learnWin = window.open('home.do?method=learner','lWindow','width=796,height=570,resizable,status=yes');
				}
			}
		}
		
		function openAdmin()
		{
			if(isMac)
			{
				adminWin = window.open('admin/adminMenu.jsp','adWindow','width=796,height=570,resizable,location,menubar,scrollbars,dependent,status,toolbar');
			}
			else
			{
				if(adminWin && adminWin.open && !adminWin.closed )
				{
					adminWin.focus();
				}
				else
				{
					adminWin = window.open('admin/adminMenu.jsp','adWindow','width=796,height=570,resizable,location,menubar,scrollbars,dependent,status,toolbar');
					adminWin.focus();
				}
			}
		}
			
		function pviiClassNew(obj, new_style)
		{ 
		  obj.className=new_style;
		}
	//-->
	</script>
	
	<link href="style.css" rel="stylesheet" type="text/css">
</head>

<body bgcolor="#9DC5EC" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
	<table width="95%" height="95%" border="0" align="center" cellpadding="0" cellspacing="0">
		<tr>
			<td>
				<jsp:include page="/adminHeader.jsp" flush="true">
					<jsp:param name="title" value="Welcome"/>
				</jsp:include> 
			</td>
		</tr>	
		<tr> 
			<td height="100%" align="center" valign="middle" bgcolor="#FFFFFF">
				<table width="98%" height="100%" border="0" align="center" cellpadding="2" cellspacing="0">
					<tr>
						<td align="center" valign="middle"><img height="7" src="images/spacer.gif" width="10" /></td>
					</tr>
					<tr>
						<td height="100%" valign="top">
							<%JspRedirectStrategy.welcomePageStatusUpdate(request,response);%>
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
										<p class="body">You are logged into LAMS.Please choose a workspace from the buttons on the right.</p>
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
													<td align="center">
														<input name="sysadmin" type="button" id="sysadmin" onClick="openAuthor();" value="SysAdmin" style="width:100" />
													</td>
													<td align="left">
														<select name="selectedOrgId">
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
													<td align="center">
														<input name="admin" type="button" id="admin" onClick="openAuthor();" value="Admin" style="width:100" />
													</td>
													<td align="center">
														<select name="selectedOrgId">
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
													<td align="center">
														<input name="staff" type="button" id="staff" onClick="openAuthor();" value="Staff" style="width:100" />
													</td>
													<td align="center">
														<select name="selectedOrgId">
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
													<td align="center">
														<input name="author" type="button" id="author" onClick="openAuthor();" value="Author" style="width:100" />
													</td>
													<td align="center">
														<select name="selectedOrgId">
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
													<td align="center">
														<input name="learner" type="button" id="learner" onClick="openAuthor();" value="Learner" style="width:100" />
													</td>
													<td align="center">
														<select name="selectedOrgId">
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
										<a href="javascript:alert('LAMS&#8482; &copy; 2002-2004 LAMS Foundation. 
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
			</td>
		</tr>
		<tr> 
			<td>
				<jsp:include page="/footer.jsp" flush="true"></jsp:include>   
			</td>
		</tr>
	</table>
</body>

</html>