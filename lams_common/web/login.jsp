<?xml version="1.0" encoding="iso-8859-1"?>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ page import="com.lamsinternational.lams.security.JspRedirectStrategy" %>
<%	
	if (JspRedirectStrategy.loginPageRedirected(request,response))
	{
		return;
	}		

	String webAuthUser = (String) session.getAttribute("WEBAUTH_USER");
	if (webAuthUser != null)
	{
		response.sendRedirect("j_security_check?j_username=" + webAuthUser + "&j_password=Dummy");	
	}
%>

<html xmlns="http://www.w3.org/1999/xhtml">
<!--
flash is searching for this string, so leave it!:
j_security_login_page
-->
<tiles:insert page="template.jsp" flush="true">
	<tiles:put name="title" value="Login - LAMS :: Learning Activity Management System"/>
	<tiles:put name="pageHeader" value="Welcome"/>
	<tiles:put name="header" value="loginHeader.jsp"/>
	<tiles:put name="content" value="loginContent.jsp" />	
	<tiles:put name="footer" value="footer.jsp"/>	
</tiles:insert>

</html>
