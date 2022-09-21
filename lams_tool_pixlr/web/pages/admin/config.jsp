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
			
			<a href="<lams:LAMSURL/>/admin/appadminstart.do" class="btn btn-default"><fmt:message key="admin.return" /></a>

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
			
			<form:form action="lapixl10admin/saveContent.do" modelAttribute="lapixl10AdminForm" id="lapixl10AdminForm" method="post">
				
				<form-group>
					<label for="languagesCSV"><fmt:message key="admin.pixlrLanguageCSV" /></label>
					<form:input path="languagesCSV" size="50" maxlength="511" cssClass="form-group"/>
				</form-group>
							
				<button  class="btn btn-primary pull-right"><fmt:message key="button.save" /></button>
			
			</form:form>
			
			<div id="footer">
			</div>
			<!--closes footer-->

		</lams:Page>
		
	</body>

</head>