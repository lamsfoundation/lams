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
			
			<html:form action="/ladoku11admin" styleId="ladoku11adminForm" method="post" enctype="multipart/form-data">
				
				<html:hidden property="dispatch" value="saveContent" />
				
				<div class="form-group voffset5">
					<label for="etherpadUrl">
						<fmt:message key="admin.etherpad.url" />
					</label>
					<html:text property="etherpadUrl" size="50" maxlength="255" styleClass="form-control form-control-inline"/>
				</div>
				
				<div class="form-group voffset5">
					<label for="apiKey">
						<fmt:message key="admin.apiKey" />
					</label>
					<html:text property="apiKey" size="50" maxlength="255" styleClass="form-control form-control-inline"/>
				</div>
			
				<html:submit styleClass="btn btn-primary pull-right">
					<fmt:message key="label.save"/>
				</html:submit>
			
			</html:form>
			
			<div id="footer">
			</div>
			
		</lams:Page>

	</body>

</head>