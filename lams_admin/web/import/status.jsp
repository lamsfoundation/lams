<%@ include file="/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.web.session.SessionManager" %>
<%@ page import="org.lamsfoundation.lams.admin.service.IImportService" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<lams:html>
<lams:head>
	<html:base/>
	<lams:css style="learner"/>
</lams:head>

<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/prototype.js"></script>
<script language="javascript" type="text/JavaScript">
	<% if (SessionManager.getSession().getAttribute("results") != null) { %>
			document.location = '<lams:LAMSURL/>/admin/importuserresult.do';
	<% } %>
	function refresh() {
		document.location = '<lams:LAMSURL/>/admin/import/status.jsp';
	}
	window.setInterval("refresh()",5000);
</script>

<body class="stripes">
<div id="page">
	<div id="content">
		<h2>
			<a href="<lams:LAMSURL/>/admin/sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a>
			: <fmt:message key="admin.user.import" />
		</h2>

		<lams:help style="no-tabs" page="<%= IImportService.IMPORT_HELP_PAGE %>"/>

		<p>&nbsp;</p>

		<div align="center">	
			<h3><fmt:message key="msg.please.wait"/></h3>
			<p><img src="<lams:LAMSURL/>/images/loading.gif" alt="loading..." /></p>
<%
	Integer importTotal = (Integer)SessionManager.getSession().getAttribute(IImportService.STATUS_IMPORT_TOTAL);
	Integer imported = (Integer)SessionManager.getSession().getAttribute(IImportService.STATUS_IMPORTED);
	String progress = "";
	try {
		float percent = imported.floatValue()/importTotal.floatValue() * 100;
		progress = (new Float(percent)).toString();
		progress = (progress.length() >= 5 ? progress.substring(0,5) : progress);
	} catch (Exception e) {}
	if (progress.length()>0) out.println(progress+" % completed...");
%>
		</div>
		
	</div>
</div>
</body>

</lams:html>