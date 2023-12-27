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

<lams:PageLearner title="${title}" toolSessionID="${toolSessionID}" >
	<script type="text/javascript">
		checkNextGateActivity('finish-button', '${toolSessionID}', '', finishSession);
		
		function finishSession(){
			document.location.href ='<c:url value="${finishSessionUrl}"/>';
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

		<div class="activity-bottom-buttons">
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
		</div>
	</div>
</lams:PageLearner>
