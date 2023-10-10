<%@ tag body-content="scriptless"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %> 
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<%@ attribute name="toolSessionID" required="true" rtexprvalue="true"%>
<%@ attribute name="title" required="true" rtexprvalue="true"%>
<%@ attribute name="submissionDeadline" required="true" rtexprvalue="true" type="java.util.Date"%>
<%@ attribute name="finishSessionUrl" required="true" rtexprvalue="true"%>
<%@ attribute name="continueReflectUrl" required="true" rtexprvalue="true"%>
<%@ attribute name="isNotebookReeditEnabled" required="true" rtexprvalue="true"%>
<%@ attribute name="notebookInstructions" required="false" rtexprvalue="true"%>
<%@ attribute name="notebookEntry" required="false" rtexprvalue="true"%>
<%@ attribute name="isContinueReflectButtonEnabled" required="true" rtexprvalue="true"%>
<%@ attribute name="isLastActivity" required="true" rtexprvalue="true"%>

<%@ attribute name="deadlineAlertLabelKey" required="false" rtexprvalue="true"%>
<c:if test="${empty deadlineAlertLabelKey}">
	<c:set var="deadlineAlertLabelKey" value="authoring.info.teacher.set.restriction" />
</c:if>
<%@ attribute name="finishButtonLastActivityLabelKey" required="false" rtexprvalue="true"%>
<c:if test="${empty finishButtonLastActivityLabelKey}">
	<c:set var="finishButtonLastActivityLabelKey" value="label.submit" />
</c:if>
<%@ attribute name="finishButtonLabelKey" required="false" rtexprvalue="true"%>
<c:if test="${empty finishButtonLabelKey}">
	<c:set var="finishButtonLabelKey" value="label.finished" />
</c:if>
<%@ attribute name="continueReflectButtonLabelKey" required="false" rtexprvalue="true"%>
<c:if test="${empty continueReflectButtonLabelKey}">
	<c:set var="continueReflectButtonLabelKey" value="label.continue" />
</c:if>
<%@ attribute name="editButtonLabelKey" required="false" rtexprvalue="true"%>
<c:if test="${empty editButtonLabelKey}">
	<c:set var="editButtonLabelKey" value="label.edit" />
</c:if>

<lams:PageLearner title="${title}" toolSessionID="${toolSessionID}" >
	<script type="text/javascript">
		checkNextGateActivity('finish-button', '${toolSessionID}', '', finishSession);
		
		function finishSession(){
			document.location.href ='<c:url value="${finishSessionUrl}"/>';
		}
		
		function continueReflect(){
			document.location.href='<c:url value="${continueReflectUrl}"/>';
		}
    </script>

	<div id="container-main">
		<lams:Alert5 id="submission-deadline-alert" close="false" type="danger">
			<fmt:message key="${deadlineAlertLabelKey}">
				<fmt:param>
					<lams:Date value="${submissionDeadline}" />
				</fmt:param>
			</fmt:message>
		</lams:Alert5>

		<!-- Display reflections -->
		<c:if test="${isNotebookReeditEnabled}">
			<div class="card shadow-sm mt-4">
				<div class="card-header">
					<fmt:message key="monitor.summary.td.notebookInstructions" />
				</div>
				
				<div class="card-body">
					<div class="m-2">
						<lams:out escapeHtml="true" value="${notebookInstructions}" />
					</div>
					<hr/>

					<div class="m-2">
						<p>
							<c:choose>
								<c:when test="${empty notebookEntry}">
									<em> <fmt:message key="message.no.reflection.available" /></em>
								</c:when>
								<c:otherwise>
									<lams:out escapeHtml="true" value="${notebookEntry}" />
								</c:otherwise>
							</c:choose>
						</p>
						
						<div>
							<button type="button" name="FinishButton" onclick="continueReflect()" class="btn btn-sm btn-secondary">
								<fmt:message key="${editButtonLabelKey}" />
							</button>
						</div>
					</div>
				</div>
			</div>
		</c:if>

		<div class="activity-bottom-buttons">
			<c:choose>
				<c:when test="${isContinueReflectButtonEnabled}">
					<button type="button" name="FinishButton" onclick="continueReflect()" class="btn btn-primary na">
						<fmt:message key="${continueReflectButtonLabelKey}" />
					</button>
				</c:when>
				<c:otherwise>
					<button type="button" name="FinishButton" id="finish-button" class="btn btn-primary na">
						<c:choose>
							<c:when test="${isLastActivity}">
								<fmt:message key="${finishButtonLastActivityLabelKey}" />
							</c:when>
							<c:otherwise>
								<fmt:message key="${finishButtonLabelKey}" />
							</c:otherwise>
						</c:choose>
					</button>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</lams:PageLearner>
