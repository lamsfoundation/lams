<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ page import="com.lamsinternational.lams.security.JspRedirectStrategy" %>
<%@ page import="com.lamsinternational.lams.web.HttpSessionManager" %>

<%JspRedirectStrategy.welcomePageStatusUpdate(request,response);%>
<%HttpSessionManager.getInstance().updateHttpSessionByLogin(request.getSession(),request.getRemoteUser()); %>

<html>
<head>
	<script language="JavaScript" type="text/javascript" src="getSysInfo.js"></script>
	<script language="JavaScript" type="text/javascript" src="openUrls.js"></script>
</head>

<tiles:insert page="template.jsp" flush="true">
	<tiles:put name="title" value="Welcome :: LAMS"/>
	<tiles:put name="pageHeader" value="Welcome"/>
	<tiles:put name="header" value="adminHeader.jsp"/>
	<tiles:put name="content" value="indexContent.jsp" />	
	<tiles:put name="footer" value="footer.jsp"/>	
</tiles:insert>
</html>