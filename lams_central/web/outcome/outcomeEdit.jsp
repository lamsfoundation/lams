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
			outcomesFrame.attr('src', outcomesFrame.attr('src'));
	       	window.parent.closeDialog('dialogOutcomeEdit');
		</c:if>
		
		var organisationId = '${param.organisationID}',
			outcomeId = '${param.outcomeId}',
			LAMS_URL = '<lams:LAMSURL/>',
			
			decoderDiv = $('<div />'),
			LABELS = {
			
			};

		function submitOutcome(){
			CKEDITOR.instances.description.updateElement();
			document.getElementById("outcomeForm");
		}   
	</script>
</lams:head>
<body>

<html:form action="/outcome.do" method="post" styleId="outcomeForm">
	<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
	<c:set var="formDisabled" value="${not empty formBean.outcomeId and empty formBean.organisationId and not canManageGlobal}" />

	<input type="hidden" name="method" value="outcomeSave" />
	<html:hidden property="outcomeId" />
	<html:hidden property="organisationId" />
	
	<div class="container">
		<div class="row vertical-center-row">
			<div
				class="col-xs-12 col-sm-8 col-sm-offset-2 col-md-6 col-md-offset-3">
				<div class="panel">
					<div class="panel-body">
						<%-- Error Messages --%>
						<logic:messagesPresent>
							<lams:Alert id="error" type="danger" close="false">
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
									<html:option value="0">
										<fmt:message key="outcome.manage.add.scale.none" />
									</html:option>
									<c:forEach items="${scales}" var="scale">
										<html:option value="${scale.scaleId}">
											<c:out value="${scale.name}" /> (<c:out value="${scale.code}" />)
										</html:option>
									</c:forEach>
								</html:select>
							</label>
						</div>
						<div class="form-group">
							<fmt:message key="outcome.manage.add.description" />:
							<c:choose>
								<c:when test="${formDisabled}">
									<br />
									<c:out value="${formBean.description}" />
								</c:when>
								<c:otherwise>
									 <lams:CKEditor id="description" value="${formBean.description}" contentFolderID="${contentFolderID}"></lams:CKEditor>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<c:if test="${not formDisabled}">
		<button id="addOutcomeButton" type="submit" class="btn btn-primary" onClick="javascript:submitOutcome()">
			<fmt:message key="outcome.manage.add.save" />
		</button>
	</c:if>
</html:form>
</body>
</lams:html>