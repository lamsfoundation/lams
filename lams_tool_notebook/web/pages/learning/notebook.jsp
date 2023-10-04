<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<lams:PageLearner title="${notebookDTO.title}" toolSessionID="${messageForm.toolSessionID}" >
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
						document.forms.messageForm.entryText.focus();
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
					if (forceResponse =="true" && document.forms.messageForm.entryText.value == "") {

						retValue = confirm("<spring:escapeBody javaScriptEscape='true'><fmt:message>message.learner.blank.alertforceResponse</fmt:message></spring:escapeBody>");
						textAreaReady();
						return retValue;
						
					} else if  (forceResponse =="false" && document.forms.messageForm.entryText.value == "" && mode == "learner") {
			
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

			window.onload = function() {
				textAreaReady();
				<c:if test="${not notebookDTO.allowRichEditor}">
					if (forceResponse == 'true') {
						$('#entryText').attr('placeholder', '<fmt:message>message.learner.blank.alertforceResponse</fmt:message>');
					}
				</c:if>
			}
		</script>

	<div class="container-lg">
	<form:form action="learning/finishActivity.do" method="post" modelAttribute="messageForm" id="messageForm">
		<form:hidden path="toolSessionID" />
		<form:hidden path="contentEditable" value="${contentEditable}" />

		<div id="instructions" class="instructions">
			<c:out value="${notebookDTO.instructions}" escapeXml="false" />
		</div>

		<!-- Notifications and warnings -->
		<c:if test="${not empty notebookDTO.submissionDeadline}">
			<lams:Alert5 id="submissionDeadline" type="info" close="true">
				<fmt:message key="authoring.info.teacher.set.restriction">
					<fmt:param>
						<lams:Date value="${notebookDTO.submissionDeadline}" />
					</fmt:param>
				</fmt:message>
			</lams:Alert5>
		</c:if>

		<c:if test="${notebookDTO.lockOnFinish and mode != 'teacher'}">
			<lams:Alert5 id="activityLocked" type="info" close="true">
				<c:choose>
					<c:when test="${finishedActivity}">
						<fmt:message key="message.activityLocked" />
					</c:when>
					<c:otherwise>
						<fmt:message key="message.warnLockOnFinish" />
					</c:otherwise>
				</c:choose>
			</lams:Alert5>
		</c:if>
		
		<c:if test="${notebookDTO.forceResponse and notebookDTO.allowRichEditor}">
			<lams:Alert5 id="requiredWarning" type="info" close="true">
				<fmt:message>message.learner.blank.alertforceResponse</fmt:message>
			</lams:Alert5>
		</c:if>

		<!-- Form -->
		<div class="mb-4 shadow">
			<c:choose>
				<c:when test="${contentEditable}">
					<c:choose>
						<c:when test="${notebookDTO.allowRichEditor}">
							<lams:CKEditor id="entryText" value="${messageForm.entryText}" height="200" contentFolderID="${learnerContentFolder}"
								toolbarSet="DefaultLearner"
								ariaLabelledby="instructions">
							</lams:CKEditor>
						</c:when>

						<c:otherwise>
							<form:textarea rows="8" path="entryText" cssClass="form-control" id="entryText" aria-labelledby="instructions"/>
						</c:otherwise>
					</c:choose>
				</c:when>

				<c:otherwise>
					<div class="sbox sbox-body mt-2 bg-warning">
						<lams:out value="${messageForm.entryText}" />
					</div>
				</c:otherwise>
			</c:choose>

			<c:if test="${not empty teachersComment}">
				<div class="panel panel-default mt-2 me-2">
					<div class="panel-heading">
						<fmt:message key="label.reply.comment" />
					</div>
					<div class="panel-body">
						<lams:out value="${teachersComment}" escapeHtml="true" />
					</div>
				</div>
			</c:if>
		</div>

		<c:if test="${mode != 'teacher'}">
			<div class="activity-bottom-buttons">
				<button type="button" class="btn btn-primary na" id="finishButton">
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
	</form:form>	
	</div>
</lams:PageLearner>