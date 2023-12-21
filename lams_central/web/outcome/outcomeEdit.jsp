<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	
	<link rel="stylesheet" href="<lams:LAMSURL/>css/components.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/outcome.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>includes/font-awesome6/css/all.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme5.css" type="text/css" media="screen">
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap5.bundle.min.js"></script>
	<lams:JSImport src="includes/javascript/outcome.js" />
	<lams:JSImport src="includes/javascript/dialog5.js" />
	<script type="text/javascript">
		<c:if test="${saved}">
			var outcomesFrame = $('#dialogOutcome iframe', window.parent.document);
			if (outcomesFrame.length == 0) {
				window.parent.document.location.href = '<lams:LAMSURL/>outcome/outcomeManage.do';
			} else {
				outcomesFrame.attr('src', outcomesFrame.attr('src'));
				$('#dialogOutcomeEdit', window.parent.document).remove();
			}
		</c:if>
		
		var LAMS_URL = '<lams:LAMSURL/>',
			LABELS = {
				<fmt:message key="outcome.manage.add" var="ADD_OUTCOME_TITLE_VAR"/>
				ADD_OUTCOME_TITLE : '<spring:escapeBody javaScriptEscape="true">${ADD_OUTCOME_TITLE_VAR}</spring:escapeBody>',
				<fmt:message key="outcome.manage.edit" var="EDIT_OUTCOME_TITLE_VAR"/>
				EDIT_OUTCOME_TITLE : '<spring:escapeBody javaScriptEscape="true">${EDIT_OUTCOME_TITLE_VAR}</spring:escapeBody>',
				<fmt:message key="outcome.manage.remove.confirm" var="REMOVE_OUTCOME_CONFIRM_LABEL_VAR"/>
				REMOVE_OUTCOME_CONFIRM_LABEL : '<spring:escapeBody javaScriptEscape="true">${REMOVE_OUTCOME_CONFIRM_LABEL_VAR}</spring:escapeBody>',
				<fmt:message key="scale.title" var="OUTCOME_MANAGE_SCALE_TITLE_VAR"/>
				OUTCOME_SCALE_MANAGE_TITLE : '<spring:escapeBody javaScriptEscape="true">${OUTCOME_MANAGE_SCALE_TITLE_VAR}</spring:escapeBody>'

			};
	</script>
</lams:head>
<body class="component bg-white p-3">
<form:form action="outcomeSave.do" method="post" modelAttribute="outcomeForm">
	<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
	<form:hidden path="outcomeId" />
	
	<lams:errors/>
	
	<div class="mb-3">
		<label class="form-label"><fmt:message key="outcome.manage.add.name" />:
			<form:input path="name" size="50" maxlength="255" cssClass="form-control" />
		</label>
	</div>
	<div class="mb-3">
		<label class="form-label"><fmt:message key="outcome.manage.add.code" />:
			<form:input path="code" size="50" maxlength="50" cssClass="form-control" />
		</label>
	</div>
	<div class="mb-3">
		<label class="form-label"><fmt:message key="outcome.manage.add.scale" />:
			<form:select path="scaleId"  cssClass="form-control">
				<c:forEach items="${scales}" var="scale">
					<form:option value="${scale.scaleId}">
						<c:out value="${scale.name}" /> (<c:out value="${scale.code}" />)
					</form:option>
				</c:forEach>
			</form:select>
		</label>
		<button type="button" class="btn btn-secondary" onClick="javascript:openOutcomeScaleDialog()">
			<fmt:message key="scale.manage" />
		</button>
	</div>
	<div class="mb-3">
		<fmt:message key="outcome.manage.add.description" />:
		<lams:CKEditor id="description" value="${outcomeForm.description}" contentFolderID="${outcomeForm.contentFolderId}"></lams:CKEditor>
	</div>

	<button id="addButton" type="submit" class="btn btn-primary" onClick="javascript:submitOutcome()">
		<fmt:message key="outcome.manage.add.save" />
	</button>
</form:form>
</body>
</lams:html>