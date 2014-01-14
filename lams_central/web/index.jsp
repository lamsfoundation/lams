<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
	"http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ page import="org.lamsfoundation.lams.security.JspRedirectStrategy"%>
<%@ page import="org.lamsfoundation.lams.web.util.HttpSessionManager"%>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c"%>

<%JspRedirectStrategy.welcomePageStatusUpdate(request, response);%>
<%HttpSessionManager.getInstance().updateHttpSessionByLogin(request.getSession(),request.getRemoteUser());%>

<lams:html>
<lams:head>
	<meta HTTP-EQUIV="Refresh" CONTENT="0;URL=index.do">
	<title><fmt:message key="index.welcome" /></title>
	<lams:css/>
</lams:head>

<body class="stripes">
	
		<div id="content"  valign="middle">
			<p align="center"><img src="images/loading.gif" /> <font color="gray" size="4"><fmt:message key="msg.loading"/></font></p>
		</div>
	   
		<div id="footer">
		</div><!--closes footer-->

</body>
</lams:html>

<c:if test="${logoutLamsCommunity}">
<iframe
	id="lamscommunityLogoutIframe" name="lamscommunityLogoutIframe"
	src="http://lamscommunity.org/logout"
	style="width:0px;height:0px;border:0px;display:none;overflow:auto" frameborder="no"
	>
</iframe>
</c:if>