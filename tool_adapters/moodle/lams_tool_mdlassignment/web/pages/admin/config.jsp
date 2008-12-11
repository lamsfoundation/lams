<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<html>
	<lams:head>
		<title><fmt:message key="pageTitle.admin" /></title>
		<lams:css/>
	</lams:head>
	
	<body class="stripes">

			<div id="content">

			<h1>
				<fmt:message key="pageTitle.admin" />
			</h1>
			
			<a href="<lams:LAMSURL/>/admin/sysadminstart.do"><fmt:message key="admin.return" /></a>

			<p>
			<c:choose>
			<c:when test="${error}">
				<p class="warning">
					<fmt:message key="admin.formError" />
				</p>
			</c:when>
			</c:choose>
			<html:form action="/mdasgm10admin" styleId="mdasgm10AdminForm" method="post" enctype="multipart/form-data">
				
				<html:hidden property="dispatch" value="saveContent" />
				<table class="alternative-color">
					<tr>
						<td width="30%"><fmt:message key="admin.extServerId" /></td>
						<td width="70%"><html:text property="serverIdMapping" size="50" maxlength="255"></html:text></td>
					</tr>
					<tr>
						<td width="30%"><fmt:message key="admin.extServerUrl" /></td>
						<td width="70%"><html:text property="extServerUrl" size="50" maxlength="255"></html:text></td>
					</tr>
					<tr>
						<td width="30%"><fmt:message key="admin.extToolAdapterServletUrl" /></td>
						<td width="70%"><html:text property="toolAdapterServlet" size="50" maxlength="255"></html:text></td>
					</tr>
				</table>
				
				<html:submit><fmt:message key="button.save" /></html:submit>
			
			</html:form>
			
			
				
			</div>
			<!--closes content-->

			<div id="footer">
			</div>
			<!--closes footer-->

	</body>

</head>
