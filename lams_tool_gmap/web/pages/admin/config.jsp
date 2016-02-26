<!DOCTYPE html>
        

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
			<c:if test="${savedSuccess}">
				<p class="info">
					<fmt:message key="admin.success" />
				<p>
			</c:if>
			
			<html:form action="/lagmap10admin" styleId="lagmap10AdminForm" method="post" enctype="multipart/form-data">
				
				<html:hidden property="dispatch" value="saveContent" />
				<table class="alternative-color">
					<tr>
						<td width="30%"><fmt:message key="admin.gmapKey" /></td>
						<td width="70%"><html:text property="gmapKey" size="50" maxlength="255"></html:text></td>
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