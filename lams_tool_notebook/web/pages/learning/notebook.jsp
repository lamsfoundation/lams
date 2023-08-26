<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<lams:html>
	<lams:head>
		<lams:headItems/>
		<lams:JSImport src="learning/includes/javascript/gate-check.js" />
		<script type="text/javascript">
			checkNextGateActivity('finishButton', '${messageForm.toolSessionID}', '', function(){
				submitForm('finish');
			});

			var mode = "${mode}";
			var forceResponse = "${notebookDTO.forceResponse}";

			function disableFinishButton() {
				document.getElementById("finishButton").style.visibility = 'hidden';
			}

			function textAreaReady() {
				<c:if test="${contentEditable}">
					<c:choose>
					<c:when test="${notebookDTO.allowRichEditor}">
						CKEDITOR.instances["entryText"].focus();
					</c:when>		
					<c:otherwise>
						document.forms.messageForm.focusedInput.focus();
					</c:otherwise>
					</c:choose>
				</c:if>
				document.getElementById("finishButton").style.visibility = 'visible';
			}
			
			function submitForm(methodName) {
				disableFinishButton();
				<c:choose>
				<c:when test="${notebookDTO.allowRichEditor}">
			      	CKEDITOR.instances["entryText"].updateElement(); // update textarea
			      	var editorcontent = document.getElementById("entryText").value.replace(/<[^>]*>/gi, ''); // strip tags
			      	var isEmpty = editorcontent.length === 0;

					if (forceResponse =="true" && isEmpty ) {

						retValue = confirm("<spring:escapeBody javaScriptEscape='true'><fmt:message>message.learner.blank.alertforceResponse</fmt:message></spring:escapeBody>");
						textAreaReady();
						return retValue;
						
					} else if  (forceResponse =="false" && isEmpty && mode == "learner") {
			
						if (!confirm("<spring:escapeBody javaScriptEscape='true'><fmt:message>message.learner.blank.input</fmt:message></spring:escapeBody>")) {
							// otherwise, focus on the text area
							textAreaReady();
							return false;
						}
					}
				</c:when>		
				<c:otherwise>
					if (forceResponse =="true" && document.forms.messageForm.focusedInput.value == "") {

						retValue = confirm("<spring:escapeBody javaScriptEscape='true'><fmt:message>message.learner.blank.alertforceResponse</fmt:message></spring:escapeBody>");
						textAreaReady();
						return retValue;
						
					} else if  (forceResponse =="false" && document.forms.messageForm.focusedInput.value == "" && mode == "learner") {
			
						if (!confirm("<spring:escapeBody javaScriptEscape='true'><fmt:message>message.learner.blank.input</fmt:message></spring:escapeBody>")) {
							// otherwise, focus on the text area
							textAreaReady();
							return false;
						}
					}
				</c:otherwise>		
				</c:choose>
				
				var f = document.getElementById('messageForm');
				f.submit();
			}
		</script>
	</lams:head>
	<body class="stripes">

<form:form action="learning/finishActivity.do" method="post" modelAttribute="messageForm" id="messageForm">
	<form:hidden path="toolSessionID" />
	<form:hidden path="contentEditable" value="${contentEditable}" />

	<lams:Page type="learner" title="${notebookDTO.title}">
		<div class="panel">
			<c:out value="${notebookDTO.instructions}" escapeXml="false" />
		</div>

		<!-- Notifications and warnings -->
		<c:if test="${not empty notebookDTO.submissionDeadline}">
			<lams:Alert id="submissionDeadline" type="info" close="true">
				<fmt:message key="authoring.info.teacher.set.restriction">
					<fmt:param>
						<lams:Date value="${notebookDTO.submissionDeadline}" />
					</fmt:param>
				</fmt:message>
			</lams:Alert>
		</c:if>

		<c:if test="${notebookDTO.lockOnFinish and mode != 'teacher'}">
			<lams:Alert id="activityLocked" type="info" close="true">
				<c:choose>
					<c:when test="${finishedActivity}">
						<fmt:message key="message.activityLocked" />
					</c:when>
					<c:otherwise>
						<fmt:message key="message.warnLockOnFinish" />
					</c:otherwise>
				</c:choose>
			</lams:Alert>
		</c:if>
		
		<c:if test="${notebookDTO.forceResponse and notebookDTO.allowRichEditor}">
			<lams:Alert id="requiredWarning" type="info" close="true">
				<fmt:message>message.learner.blank.alertforceResponse</fmt:message>
			</lams:Alert>
		</c:if>
		
		<!-- End Notifications and warnings -->

		<!-- Form -->
		<div class="form-group">
			<c:choose>
				<c:when test="${contentEditable}">
					<c:choose>
						<c:when test="${notebookDTO.allowRichEditor}">
							<lams:CKEditor id="entryText" value="${messageForm.entryText}" height="200" contentFolderID="${learnerContentFolder}"
								toolbarSet="DefaultLearner">
							</lams:CKEditor>
						</c:when>

						<c:otherwise>
							<form:textarea rows="8" path="entryText" cssClass="form-control" id="focusedInput"/>
						</c:otherwise>
					</c:choose>
				</c:when>

				<c:otherwise>
					<div class="sbox sbox-body voffset10 bg-warning">
						<lams:out value="${messageForm.entryText}" />
					</div>
				</c:otherwise>
			</c:choose>

			<c:if test="${not empty teachersComment}">
				<div class="panel panel-default voffset5 roffset10">
					<div class="panel-heading">
						<fmt:message key="label.reply.comment" />
					</div>
					<div class="panel-body">
						<lams:out value="${teachersComment}" escapeHtml="true" />
					</div>
				</div>
			</c:if>

			<c:if test="${mode != 'teacher'}">
				<div class="activity-bottom-buttons">
					<button href="#nogo" class="btn btn-primary na" id="finishButton" type="button">
						<c:choose>
							<c:when test="${isLastActivity}">
								<fmt:message key="button.submit" />
							</c:when>
							<c:otherwise>
								<fmt:message key="button.finish" />
							</c:otherwise>
						</c:choose>
					</button>
				</div>
			</c:if>
		</div>
	</lams:Page>
</form:form>
<!-- end form -->

<script type="text/javascript">
	window.onload = function() {
		textAreaReady();
		<c:if test="${not notebookDTO.allowRichEditor}">
		if (forceResponse == 'true') {
			$('#focusedInput').attr('placeholder', '<fmt:message>message.learner.blank.alertforceResponse</fmt:message>');
		}
		</c:if>
	}
</script>
		<div class="footer"></div>					
	</body>
</lams:html>

