<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>

<html>
	<lams:head>
		<title><fmt:message key="admin.page.title" /></title>
		<lams:css/>
	</lams:head>
	
	<body class="stripes">

		<c:set var="title"><fmt:message key="admin.page.title" /></c:set>
		<lams:Page type="admin" title="${title}">
			
			<a href="<lams:LAMSURL/>admin/sysadminstart.do" class="btn btn-default"><fmt:message key="admin.return" /></a>
			
			<lams:errors/>
		
			<c:if test="${savedSuccess}">
				<lams:Alert type="info" id="admin-success" close="false">
					<fmt:message key="admin.success" />
				</lams:Alert>
			</c:if>
			
			<form:form action="/lams/tool/lascrt11/admin/saveContent.do" modelAttribute="scratchieAdminForm" id="scratchieAdminForm" method="post" enctype="multipart/form-data">
				<div class="form-group">
					<label for="presetMarks"><fmt:message key="admin.preset.marks" /></label>
					<form:input path="presetMarks" size="50" maxlength="255" cssClass="form-control form-control-inline" />
				</div>
				<div class="checkbox">
					<label>
					<form:checkbox path="hideTitles"/>
					<fmt:message key="admin.hide.titles" /></label>
				</div>
				<button class="btn btn-primary  pull-right"><fmt:message key="admin.button.save" /></button>
			</form:form>
			
		<div id="footer"></div>
		</lams:Page>
	</body>
</head>