<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>

<html>
	<lams:head>
		<title><fmt:message key="admin.page.title" /></title>
		<lams:css/>
	</lams:head>
	
	<body class="stripes">

		<c:set var="title"><fmt:message key="admin.page.title" /></c:set>
		<lams:Page type="admin" title="${title}" formID="adminForm">
			
			<a href="<lams:LAMSURL/>/admin/sysadminstart.do" class="btn btn-default"><fmt:message key="admin.return" /></a>
			
			<lams:errors/>
		
			<c:if test="${savedSuccess}">
				<lams:Alert type="info" id="admin-success" close="false">
					<fmt:message key="admin.success" />
				</lams:Alert>
			</c:if>
			
			<form:form action="saveContent.do" id="adminForm" modelAttribute="adminForm" method="post" enctype="multipart/form-data">
				<div class="checkbox">
					<label for="keepLearnerContent">
						<form:checkbox path="keepLearnerContent" id="keepLearnerContent"/>
						<fmt:message key="admin.keep.learner.content" />
					</label>
				</div>
				<button type="submit" class="btn btn-primary pull-right" ><fmt:message key="admin.button.save" /></button>
			</form:form>
			
		<div id="footer"></div>
		</lams:Page>
	</body>
</head>
