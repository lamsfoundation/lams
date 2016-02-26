<!DOCTYPE html>
        

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<lams:head>
		<title><fmt:message key="admin.page.title" /></title>
		<lams:css/>
	</lams:head>
	
	<body class="stripes">

			<div id="content">

			<h1>
				<fmt:message key="admin.page.title" />
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
				</p>
			</c:if>
			</p>
			
			<html:form action="/admin" styleId="wookieadminform" method="post" enctype="multipart/form-data">
				
				<html:hidden property="dispatch" value="saveContent" />
				<table class="alternative-color">
					<tr>
						<td width="30%"><fmt:message key="admin.wookie.apikey" /></td>
						<td width="70%"><html:text property="apiKey" size="50" maxlength="511"></html:text></td>
					</tr>
					<tr>
						<td width="30%"><fmt:message key="admin.wookie.url" /></td>
						<td width="70%"><html:text property="wookieServerUrl" size="50" maxlength="511"></html:text></td>
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

</lams:html>
