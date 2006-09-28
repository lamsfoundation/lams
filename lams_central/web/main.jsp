<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ page import="org.lamsfoundation.lams.security.JspRedirectStrategy"%>
<%@ page import="org.lamsfoundation.lams.web.util.HttpSessionManager"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration"%>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>

<%JspRedirectStrategy.welcomePageStatusUpdate(request, response);%>
<%HttpSessionManager.getInstance().updateHttpSessionByLogin(request.getSession(),request.getRemoteUser());%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<lams:html>
<head>
	<title>LAMS::<fmt:message key="index.welcome" /></title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="Expires" content="-1">
	<lams:css />
	<link rel="icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	<script language="JavaScript" type="text/javascript" src="includes/javascript/getSysInfo.js"></script>
	<script language="JavaScript" type="text/javascript" src="includes/javascript/openUrls.js"></script>
	<script>
		<!--
			function refresh(){
				document.location.reload();
			}
		//->
	</script>
</head>
<body class="my-courses">
<div id="page-mycourses">
	<div id="header-my-courses">
		<div id="nav-right">
			<div class="nav-box-right">
				<div class="tab-left-selected"></div>
				<div class="tab-middle-selected"><a class="tab-middle-link-selected" href="#"><fmt:message key="index.mycourses"/> </a></div>
				<div class="tab-right-selected"></div>
			</div>
			<c:forEach var="headerlink" items="${headerLinks}">
			<div class="nav-box-right">
				<div class="tab-left"></div>
				<div class="tab-middle">
					<a class="tab-middle-link" href="<c:out value='${headerlink.url}'/>">
						<fmt:message key="${headerlink.name}" />
					</a>
				</div>
				<div class="tab-right"></div>
			</div>
			</c:forEach>
		</div>
	</div>
	<div id="content">
		<div id="message" style="text-align: center;"></div>
		<div style="display: block;" class="box">
			<table cellpadding="0">
				<tbody>
					<tr>
						<td>
							<div class="welcome">
								<div class="float-left">
									<img src="images/css/user.gif" alt="" width="14" height="13" /> 
									<fmt:message key="index.welcome" /> 
									<lams:user property="firstName" />
								</div>
								<div class="float-right">
									<a title="<fmt:message key="index.refresh.hint"/>" href="javascript:refresh()">
										<fmt:message key="index.refresh" />
									</a> | <a href="home.do?method=logout" onClick="closeAllChildren()">
										<fmt:message key="index.logout" />
									</a>
								</div>
							</div>
							<c:import url="indexCommon.jsp" charEncoding="utf-8"/>						
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	<div id="footer">
		<p>
			<fmt:message key="msg.LAMS.version" /> <%=Configuration.get(ConfigurationKeys.VERSION)%>
			<a href="copyright.jsp" target='copyright' onClick="openCopyRight()">
				&copy; <fmt:message key="msg.LAMS.copyright.short" /> 
			</a>
		</p>
	</div>
</div>
</body>
</lams:html>

