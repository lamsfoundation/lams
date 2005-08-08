<%@ page language="java" import="java.util.*" %>
<%@ page import="org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants" %>
<%@ taglib uri="/WEB-INF/struts/struts-html-el.tld" prefix="html" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

String authorUrl = "/starter/authoring?toolContentId="+355;
String learnerUrl = "/starter/learner?userId="+555+"&toolSessionId="+455+"&toolContentId="+355+"&mode=learner";
String learnerUrlTeacher = "/starter/learner?userId="+555+"&toolSessionId="+455+"&toolContentId="+355+"&mode=teacher";
String learnerUrlAuthor = "/starter/learner?userId="+555+"&toolSessionId="+455+"&toolContentId="+355+"&mode=author";
String monitoringUrl = "/starter/monitor?toolContentId="+355;
String exportPortfolioTeacher ="/exportPortfolio.do?mode=teacher&toolContentId=355";
String exportPortfolioStudent = "/exportPortfolio.do?mode=learner&toolSessionId=455&userId=555";
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
		  		<html:submit property="toolButton" value="Learner URL learner mode" />
		  		</html:form>
	  		</td>
	  	</tr>
	  	<tr>
	  		<td>
	  			<html:form action="<%=learnerUrlTeacher%>" method="post" target="_blank" >
		  		<html:submit property="toolButton" value="Learner URL teacher mode" />
		  		</html:form>
	  		</td>
	  	</tr>
	  	<tr>
	  		<td>
	  			<html:form action="<%=learnerUrlAuthor%>" method="post" target="_blank" >
		  		<html:submit property="toolButton" value="Learner URL author mode" />
		  		</html:form>
	  		</td>
	  	</tr>
	  	<tr>
	  		<td>
	  			<html:form action="<%=exportPortfolioTeacher%>" method="post" target="_blank" >
		  		<html:submit property="toolButton" value="Export Portfolio Teacher" />
		  		</html:form>
	  		</td>
	  	</tr>
	  	<tr>
	  		<td>
	  			<html:form action="<%=exportPortfolioStudent%>" method="post" target="_blank" >
		  		<html:submit property="toolButton" value="Export Portfolio Learner" />
		  		</html:form>
	  		</td>
	  	</tr>
  	</table>
  </body>
</html:html>
