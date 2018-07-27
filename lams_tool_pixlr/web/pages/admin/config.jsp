<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<html>
	<lams:head>
		<title><fmt:message key="pageTitle.admin" /></title>
		<lams:css/>
	</lams:head>
	
	<body class="stripes">

		<c:set var="title"><fmt:message key="pageTitle.admin" /></c:set>
		<lams:Page type="admin" title="${title}">
			
			<a href="<lams:LAMSURL/>/admin/sysadminstart.do" class="btn btn-default"><fmt:message key="admin.return" /></a>

			<p>
			<c:choose>
			<c:when test="${error}">
				<lams:Alert type="danger" id="form-error" close="false">
					<fmt:message key="admin.formError" />
				</lams:Alert>
			</c:when>
			</c:choose>
			<c:if test="${savedSuccess}">
				<lams:Alert type="info" id="admin-success" close="false">
					<fmt:message key="admin.success" />
				</lams:Alert>
			</c:if>
			
			<html:form action="/lapixl10admin" styleId="lapixl10AdminForm" method="post" enctype="multipart/form-data">
				
				<html:hidden property="dispatch" value="saveContent" />
				<form-group>
					<label for="languagesCSV"><fmt:message key="admin.pixlrLanguageCSV" /></label>
					<html:text property="languagesCSV" size="50" maxlength="511" styleClass="form-group">
					</html:text>
				</form-group>
							
				<html:submit styleClass="btn btn-primary pull-right"><fmt:message key="button.save" /></html:submit>
			
			</html:form>
			
			<div id="footer">
			</div>
			<!--closes footer-->

		</lams:Page>
		
	</body>

</head>