<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.web.session.SessionManager" %>
<%@ page import="org.lamsfoundation.lams.admin.service.IImportService" %>

<lams:html>
	<lams:head>
		<lams:css/>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	</lams:head>
	
	<script type="text/JavaScript">
		<% if (SessionManager.getSession().getAttribute("results") != null) { %>
				document.location = '<lams:LAMSURL/>admin/importuserresult.do';
		<% } %>
	
		$(document).ready(function(){
			document.getElementById('fileUpload_Busy').style.display = '';
		});
		
		function refresh() {
			document.location = '<lams:LAMSURL/>admin/import/status.jsp';
		}
		window.setInterval("refresh()",5000);
	</script>

	<c:set var="progressMessage">
	<%
		Integer importTotal = (Integer)SessionManager.getSession().getAttribute(IImportService.STATUS_IMPORT_TOTAL);
		Integer imported = (Integer)SessionManager.getSession().getAttribute(IImportService.STATUS_IMPORTED);
		String progress = "";
		try {
			float percent = imported.floatValue()/importTotal.floatValue() * 100;
			progress = (new Float(percent)).toString();
			progress = (progress.length() >= 5 ? progress.substring(0,5) : progress);
		} catch (Exception e) {}
		out.println((progress.length()>0?progress:"0")+"% completed...");
	%>
	</c:set>	

<body class="stripes">
	<lams:Page type="admin" title="${title}">
	
		<div align="center">	
			<h3><fmt:message key="msg.please.wait"/></h3>
			<p>${progressMessage}</p>
		</div>
			
		<lams:WaitingSpinner id="fileUpload_Busy"/> 
		
	</lams:Page>
</body>

</lams:html>