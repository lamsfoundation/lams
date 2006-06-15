<%@ taglib uri="tags-tiles" prefix="tiles" %>
<%@ page import="org.lamsfoundation.lams.security.JspRedirectStrategy" %>
<%@ page import="org.lamsfoundation.lams.web.util.HttpSessionManager" %>

<%JspRedirectStrategy.welcomePageStatusUpdate(request,response);%>
<%HttpSessionManager.getInstance().updateHttpSessionByLogin(request.getSession(),request.getRemoteUser()); %>

<tiles:insert page="template.jsp" flush="true">
	<tiles:put name="title" value="Welcome :: LAMS"/>
	<tiles:put name="pageHeader" value="Welcome"/>
	<tiles:put name="header" value="adminHeader.jsp"/>
	<tiles:put name="content" value="indexContent.jsp" />	
	<tiles:put name="footer" value="footer.jsp"/>	
</tiles:insert>
