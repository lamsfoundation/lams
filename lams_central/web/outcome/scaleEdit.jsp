<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="<lams:LAMSURL/>css/outcome.css" type="text/css" media="screen" />
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/outcome.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/dialog.js"></script>
	<script type="text/javascript">
		<c:if test="${saved}">
			var scalesFrame = $('#dialogOutcomeScale iframe', window.parent.document);
			if (scalesFrame.length == 0) {
				window.parent.document.location.href = 
					'<lams:LAMSURL/>outcome/scaleManage.do${empty param.organisationID ? "" : "?organisationID=param.organisationID"}';
			} else {
				scalesFrame.attr('src', scalesFrame.attr('src'));
				$('#dialogOutcomeScaleEdit', window.parent.document).remove();
			}
		</c:if>
		
		var organisationId = '${param.organisationID}';
	</script>
</lams:head>
<body>

<form:form action="scaleSave.do" method="post" modelAttribute="scaleForm">
	<c:set var="formDisabled" value="${isDefaultScale or (not empty scaleForm.scaleId and empty scaleForm.organisationId and not canManageGlobal)}" />

	<form:hidden path="scaleId" />
	<form:hidden path="organisationId" />
	<form:hidden path="contentFolderId" />
	
	<div class="container">
		<div class="row vertical-center-row">
			<div
				class="col-xs-12 col-sm-8 col-sm-offset-2 col-md-6 col-md-offset-3">
				<div class="panel">
					<div class="panel-body">
					
						<lams:errors/>
						
						<div class="form-group">
							<label><fmt:message key="outcome.manage.add.name" />:
								<form:input path="name" size="50" maxlength="255" styleClass="form-control" disabled="${formDisabled}" />
							</label>
						</div>
						<div class="form-group">
							<label><fmt:message key="outcome.manage.add.code" />:
								<form:input path="code" size="50" maxlength="50" styleClass="form-control" disabled="${formDisabled}" />
							</label>
						</div>
						<div class="form-group">
							<fmt:message key="outcome.manage.scope" />:
							<c:choose>
								<c:when test="${empty scaleForm.organisationId}">
									<fmt:message key="outcome.manage.scope.global" />
								</c:when>
								<c:otherwise>
									<fmt:message key="outcome.manage.scope.course" />
								</c:otherwise>
							</c:choose>
						</div>
						<div class="form-group">
							<label><fmt:message key="scale.manage.add.value" />:<br />
								<form:textarea path="items" disabled="${formDisabled}" />
								<lams:Alert type="info" close="false">
							        <fmt:message key="scale.manage.add.value.info" />
								</lams:Alert>
							</label>
						</div>
						<div class="form-group">
							<fmt:message key="outcome.manage.add.description" />:
							<c:choose>
								<c:when test="${formDisabled}">
									<br />
									<c:out value="${scaleForm.description}" />
								</c:when>
								<c:otherwise>
									 <lams:CKEditor id="description" value="${scaleForm.description}" contentFolderID="${scaleForm.contentFolderId}"></lams:CKEditor>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</div>
			</div>
		</div>
		<c:if test="${not formDisabled}">
			<button id="addButton" type="submit" class="btn btn-primary" onClick="javascript:submitScale()">
				<fmt:message key="outcome.manage.add.save" />
			</button>
		</c:if>
	</div>
</form:form>
</body>
</lams:html>