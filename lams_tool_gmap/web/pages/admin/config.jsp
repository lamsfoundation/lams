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

			<c:choose>
			<c:when test="${error}">
				<lams:Alert type="warn" id="no-edit" close="false">
					<fmt:message key="admin.formError" />
				</lams:Alert>
			</c:when>
			</c:choose>
			<c:if test="${savedSuccess}">
				<lams:Alert type="info" id="no-edit" close="false">
					<fmt:message key="admin.success" />
				</lams:Alert>
			</c:if>
			
			<html:form action="/lagmap10admin" styleId="lagmap10AdminForm" method="post" enctype="multipart/form-data">
				
				<html:hidden property="dispatch" value="saveContent" />
				
				<div class="form-group voffset5">
					<label for="gmapKey"><fmt:message key="admin.gmapKey" /></label>
					<html:text property="gmapKey" size="50" maxlength="255" styleClass="form-control form-control-inline"></html:text>
				</div>
			
				<html:submit styleClass="btn btn-primary pull-right"><fmt:message key="button.save"/></html:submit>
			
			</html:form>
			
			<div id="footer">
			</div>
			
		</lams:Page>

	</body>

</head>