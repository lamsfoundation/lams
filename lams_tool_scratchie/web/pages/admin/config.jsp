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
			
			<a href="<lams:LAMSURL/>/admin/sysadminstart.do" class="btn btn-default"><fmt:message key="admin.return" /></a>
			
			<%@ include file="/common/messages.jsp"%>
		
			<c:if test="${savedSuccess}">
				<lams:Alert type="info" id="admin-success" close="false">
					<fmt:message key="admin.success" />
				</lams:Alert>
			</c:if>
			
			<html:form action="/admin/saveContent" styleId="scratchieAdminForm" method="post" enctype="multipart/form-data">
				<div class="checkbox">
					<label for="enabledExtraPointOption">
					<html:checkbox property="enabledExtraPointOption"/>
					<fmt:message key="admin.extra.mark" /></label>
				</div>
				<div class="form-group">
					<label for="presetMarks"><fmt:message key="admin.preset.marks" /></label>
					<html:text property="presetMarks" size="50" maxlength="255" styleClass="form-control form-control-inline">
					</html:text>
				</div>
				<html:submit styleClass="btn btn-primary  pull-right"><fmt:message key="admin.button.save" /></html:submit>
			</html:form>
			
		<div id="footer"></div>
		</lams:Page>
	</body>
</head>