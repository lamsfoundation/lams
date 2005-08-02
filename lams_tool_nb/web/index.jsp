<%@ page language="java" import="java.util.*" %>
<%@ page import="org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants" %>
<%@ taglib uri="/WEB-INF/struts/struts-html-el.tld" prefix="html" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

String authorUrl = "/starter/authoring?toolContentId="+355;
String learnerUrl = "/starter/learner?userId="+555+"&toolSessionId="+455+"&toolContentId="+355+"&mode=learner";
String monitoringUrl = "/starter/monitor?toolContentId="+355;
%>

<%@ taglib uri="/WEB-INF/jstl/c.tld" prefix="c" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
  <head>
	   <title>Noticeboard Starter Page</title>
  </head>
  
  <body>
  	<table width="100%">
	  	<tr>
		  	<td>
		  		<html:form action="<%=authorUrl%>" method="post" target="_blank" >
		  		<html:submit property="toolButton" value="Authoring URL" />
		  		</html:form>
		  	</td>
	  	</tr>
	  	
	  	<tr>
	  		<td>
	  			<html:form action="<%=monitoringUrl%>" method="post" target="_blank" >
		  		<html:submit property="toolButton" value="Monitoring URL" />
		  		</html:form>
	  		</td>
	  	</tr>
	  	<tr>
	  		<td>
	  			<html:form action="<%=learnerUrl%>" method="post" target="_blank" >
		  		<html:submit property="toolButton" value="Learner URL" />
		  		</html:form>
	  		</td>
	  	</tr>
  	</table>
  </body>
</html:html>
