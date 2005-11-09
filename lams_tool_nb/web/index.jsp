<%@ page import="org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants" %>
<%@ taglib uri="tags-html-el" prefix="html" %>
<%

String authorUrl = "/starter/authoring?toolContentID="+355;
String learnerUrl = "/starter/learner?toolSessionID="+455+"&mode=learner";
String learnerUrlTeacher = "/starter/learner?userId="+555+"&toolSessionID="+455+"&mode=teacher";
String learnerUrlAuthor = "/starter/learner?&toolSessionID="+455+"&mode=author";
String monitoringUrl = "/starter/monitor?toolContentID="+355;	
String exportPortfolioTeacher ="/exportPortfolio.do?mode=teacher&toolContentID=355";
String exportPortfolioStudent = "/exportPortfolio.do?mode=learner&toolSessionID=455&userID=555";
%>

<%@ taglib uri="tags-core" prefix="c" %>

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
