<%@ page language="java" import="java.util.*" %>
<%@ page import="org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants" %>
<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

String url = "/tool/nb/starter/learner?userId="+555+"&toolSessionId="+455+"&toolContentId="+355+"&mode=learner";
%>

<%@ taglib uri="/WEB-INF/jstl/c.tld" prefix="c" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
  <head>
	   <title>Noticeboard Learner Starter Page</title>
  </head>
  
  <body>
  		<html:form action="<%=url%>" method="post" target="_blank" >
  		<html:submit property="toolButton" value="NB Tool Button" />
  		</html:form>
  </body>
</html:html>
