<%@include file="../sharing/share.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
  <head>    
    <title>Monitoring Statistic</title>
    <html:base/>
  	<link href="<%=LAMS_WEB_ROOT%>/css/aqua.css" rel="stylesheet" type="text/css">
  </head>  
  <body>
		<h1><fmt:message key="monitoring.statistic.title"/></h1>
		<div class="datatablecontainer">
		<table width="100%"  border="0" cellspacing="0" cellpadding="0">
		  <tr>
		    <th scope="col">Title</th>
		    <th scope="col">Count</th>
		  </tr>
		  <tr>
		    <td><fmt:message key="monitoring.statistic.marked"/></td>
		    <td><c:out value="${statistic.markedCount}"/></td>
		  </tr>
		  <tr>
		    <td><fmt:message key="monitoring.statistic.not.marked"/></td>
		    <td><c:out value="${statistic.notMarkedCount}"/></td>
		  </tr>
		  <tr>
		    <td><fmt:message key="monitoring.statistic.total.uploaded.file"/></td>
		    <td><c:out value="${statistic.totalUploadedFiles}"/></td>
		  </tr>
		</table>
		</div>						
  </body>
</html:html>

