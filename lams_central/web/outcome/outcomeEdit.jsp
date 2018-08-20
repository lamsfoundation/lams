<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-function" prefix="fn" %>

<!DOCTYPE html>
<lams:html>
<lams:head>
	<lams:css/>
	<link rel="stylesheet" href="css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="css/outcome.css" type="text/css" media="screen" />
	
	<script type="text/javascript" src="includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="includes/javascript/outcome.js"></script>
	<script type="text/javascript" src="includes/javascript/dialog.js"></script>
	<script type="text/javascript">
		<c:if test="${saved}">
			var outcomesFrame = $('#dialogOutcome iframe', window.parent.document);
			if (outcomesFrame.length == 0) {
				window.parent.document.location.href = 
					'<lams:LAMSURL/>outcome.do?method=outcomeManage${empty param.organisationID ? "" : "&organisationID=param.organisationID"}';
			} else {
				outcomesFrame.attr('src', outcomesFrame.attr('src'));
				$('#dialogOutcomeEdit', window.parent.document).remove();
			}
		</c:if>
		
		var organisationId = '${param.organisationID}',
			LAMS_URL = '<lams:LAMSURL/>',
			decoderDiv = $('<div />'),
			LABELS = {
				<fmt:message key="outcome.manage.add" var="ADD_OUTCOME_TITLE_VAR"/>
				ADD_OUTCOME_TITLE : '<c:out value="${ADD_OUTCOME_TITLE_VAR}" />',
				<fmt:message key="outcome.manage.edit" var="EDIT_OUTCOME_TITLE_VAR"/>
				EDIT_OUTCOME_TITLE : '<c:out value="${EDIT_OUTCOME_TITLE_VAR}" />',
				<fmt:message key="outcome.manage.remove.confirm" var="REMOVE_OUTCOME_CONFIRM_LABEL_VAR"/>
				REMOVE_OUTCOME_CONFIRM_LABEL : decoderDiv.html('<c:out value="${REMOVE_OUTCOME_CONFIRM_LABEL_VAR}" />').text(),
				<fmt:message key="scale.title" var="OUTCOME_MANAGE_SCALE_TITLE_VAR"/>
				OUTCOME_SCALE_MANAGE_TITLE : '<c:out value="${OUTCOME_MANAGE_SCALE_TITLE_VAR}" />',
				<fmt:message key="scale.manage.title" var="OUTCOME_SCALE_COURSE_MANAGE_TITLE_VAR"/>
				OUTCOME_SCALE_COURSE_MANAGE_TITLE : '<c:out value="${OUTCOME_SCALE_COURSE_MANAGE_TITLE_VAR}" />'
			};
	</script>
</lams:head>
<body>

<html:form action="/outcome.do" method="post" styleId="outcomeForm">
	<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
	<c:set var="formDisabled" value="${not empty formBean.outcomeId and empty formBean.organisationId and not canManageGlobal}" />

	<input type="hidden" name="method" value="outcomeSave" />
	<html:hidden property="outcomeId" />
	<html:hidden property="organisationId" />
	<html:hidden property="contentFolderId" />
	
	<div class="container">
		<div class="row vertical-center-row">
			<div
				class="col-xs-12 col-sm-8 col-sm-offset-2 col-md-6 col-md-offset-3">
				<div class="panel">
					<div class="panel-body">
						<%-- Error Messages --%>
						<logic:messagesPresent>
							<lams:Alert type="danger" close="false">
							        <html:messages id="error">
							            <c:out value="${error}" escapeXml="false"/><br/>
							        </html:messages>
							</lams:Alert>
						</logic:messagesPresent>
						
						<div class="form-group">
							<label><fmt:message key="outcome.manage.add.name" />:
								<html:text property="name" size="50" maxlength="255" styleClass="form-control" disabled="${formDisabled}" />
							</label>
						</div>
						<div class="form-group">
							<label><fmt:message key="outcome.manage.add.code" />:
								<html:text property="code" size="50" maxlength="50" styleClass="form-control" disabled="${formDisabled}" />
							</label>
						</div>
						<div class="form-group">
							<fmt:message key="outcome.manage.scope" />:
							<c:choose>
								<c:when test="${empty formBean.organisationId}">
									<fmt:message key="outcome.manage.scope.global" />
								</c:when>
								<c:otherwise>
									<fmt:message key="outcome.manage.scope.course" />
								</c:otherwise>
							</c:choose>
						</div>
						<div class="form-group">
							<label><fmt:message key="outcome.manage.add.scale" />:
								<html:select property="scaleId"  styleClass="form-control" disabled="${formDisabled}">
									<c:forEach items="${scales}" var="scale">
										<html:option value="${scale.scaleId}">
											<c:out value="${scale.name}" /> (<c:out value="${scale.code}" />)
										</html:option>
									</c:forEach>
								</html:select>
							</label>
							<button type="button" class="btn btn-default" onClick="javascript:openOutcomeScaleDialog()">
								<fmt:message key="scale.manage" />
							</button>
						</div>
						<div class="form-group">
							<fmt:message key="outcome.manage.add.description" />:
							<c:choose>
								<c:when test="${formDisabled}">
									<br />
									<c:out value="${formBean.description}" />
								</c:when>
								<c:otherwise>
									 <lams:CKEditor id="description" value="${formBean.description}" contentFolderID="${formBean.contentFolderId}"></lams:CKEditor>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</div>
			</div>
		</div>
		<c:if test="${not formDisabled}">
			<button id="addButton" type="submit" class="btn btn-primary" onClick="javascript:submitOutcome()">
				<fmt:message key="outcome.manage.add.save" />
			</button>
		</c:if>
	</div>
</html:form>
</body>
</lams:html>