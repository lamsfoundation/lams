<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>

<html>
	<lams:head>
		<title><fmt:message key="pageTitle.admin" /></title>
		<lams:css/>
		<style>
			table {
				margin-bottom: 20px;
			}
			
			table td {
				padding: 10px;
			}
		</style>
	</lams:head>
	
	<body class="stripes">

		<c:set var="title"><fmt:message key="pageTitle.admin" /></c:set>
		<lams:Page type="admin" title="${title}">
			
			<a href="<lams:LAMSURL/>admin/appadminstart.do" class="btn btn-default"><fmt:message key="admin.return" /></a>
			
			<lams:errors/>
		
			<c:if test="${savedSuccess}">
				<lams:Alert type="info" id="admin-success" close="false">
					<fmt:message key="admin.success" />
				</lams:Alert>
			</c:if>
			
			<form:form action="/lams/tool/lawhiteboard11/admin/saveContent.do" modelAttribute="whiteboardAdminForm"
					   id="whiteboardAdminForm" method="post">
				<table>
					<tr>
						<td>
							<label for="whiteboardServerUrl"><fmt:message key="admin.whiteboard.url" /></label>
						</td>
						<td>
							<form:input path="whiteboardServerUrl" size="50" maxlength="255" cssClass="form-control form-control-inline" />
						</td>
					</tr>
					<tr>
						<td>
							<label for="whiteboardAccessToken"><fmt:message key="admin.access.token" /></label>
						</td>
						<td>
							<form:input path="whiteboardAccessToken" size="50" maxlength="255" cssClass="form-control form-control-inline" />
						</td>
					</tr>
					<tr>
						<td>
							<label for="whiteboardIdPrefix"><fmt:message key="admin.id.prefix" /></label>
						</td>
						<td>
							<form:input path="whiteboardIdPrefix" size="50" maxlength="255" cssClass="form-control form-control-inline" />
						</td>
					</tr>
				</table>
				<button class="btn btn-primary  pull-right"><fmt:message key="label.save" /></button>
			</form:form>
			
		<div id="footer"></div>
		</lams:Page>
	</body>
</head>