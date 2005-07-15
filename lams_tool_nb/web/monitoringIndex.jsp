<%@ page language="java" import="java.util.*" %>
<%@ page import="org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants" %>
<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

String url = "/tool/nb/starter/monitor?toolContentId="+355;
%>

<%@ taglib uri="/WEB-INF/jstl/c.tld" prefix="c" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
  <head>
	   <title>Noticeboard Monitor Starter Page</title>
  </head>
  
  <body>
  		<html:form action="<%=url%>" method="post" target="_blank" >
  		<html:submit property="toolButton" value="NB Tool Button" />
  		</html:form>
  </body>
</html:html>