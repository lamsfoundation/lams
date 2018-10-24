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

			<a href="<lams:LAMSURL/>/admin/sysadminstart.do" class="btn btn-default">
				<fmt:message key="admin.return" />
			</a>

			<c:if test="${error}">
				<lams:Alert type="warn" id="no-edit" close="false">
					<fmt:message key="admin.formError" />
				</lams:Alert>
			</c:if>
			<c:if test="${savedSuccess}">
				<lams:Alert type="info" id="no-edit" close="false">
					<fmt:message key="admin.success" />
				</lams:Alert>
			</c:if>
			
			<form:form action="/lams/tool/ladoku11/ladoku11admin/saveContent.do" modelAttribute="ladoku11adminForm" id="ladoku11adminForm" method="post" enctype="multipart/form-data">
				
				<div class="form-group voffset5">
					<label for="etherpadUrl">
						<fmt:message key="admin.etherpad.url" />
					</label>
					<form:input path="etherpadUrl" size="50" maxlength="255" cssClass="form-control form-control-inline"/>
				</div>
				
				<div class="form-group voffset5">
					<label for="apiKey">
						<fmt:message key="admin.apiKey" />
					</label>
					<form:input path="apiKey" size="50" maxlength="255" cssClass="form-control form-control-inline"/>
				</div>
			
				<button class="btn btn-primary pull-right">
					<fmt:message key="label.save"/>
				</button>
			
			</form:form>
			
			<div id="footer">
			</div>
			
		</lams:Page>

	</body>

</head>