<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMapID" value="${surveyForm.sessionMapID}" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="position" value="${surveyForm.position}" />
<c:set var="questionSeqID" value="${surveyForm.questionSeqID}" />
<c:set var="currentIdx" value="${surveyForm.currentIdx}" />

<lams:PageLearner title="${sessionMap.title}" toolSessionID="${sessionMap.toolSessionID}">
	<script type="text/javascript">
		function previousQuestion(sessionMapID) {
			$("#surveyForm").attr('action', '<c:url value="/learning/previousQuestion.do"/>');
			$("#surveyForm").submit();
		}

		function nextQuestion(sessionMapID) {
			$("#surveyForm").attr('action', '<c:url value="/learning/nextQuestion.do"/>');
			$("#surveyForm").submit();
		}

		function singleChoice(choiceName) {
			var rs = document.getElementsByName(choiceName);
			for (idx = 0; idx < rs.length; idx++)
				rs[idx].checked = false;
		}
		
		function disableButtons() {
			$('.btn').prop('disabled', true);
		}
	</script>
	
	<div id="container-main">
		<form:form action="doSurvey.do" method="post" modelAttribute="surveyForm" id="surveyForm" onsubmit="disableButtons();">
			<form:hidden path="questionSeqID" />
			<form:hidden path="sessionMapID" />
			<form:hidden path="position" />
			<form:hidden path="currentIdx" />

			<div id="instructions" class="instructions">
				<c:out value="${sessionMap.instructions}" escapeXml="false" />
			</div>
			
			<c:if test="${not empty sessionMap.submissionDeadline}">
				<lams:Alert5 id="submissionDeadline" type="info" close="true">
					<fmt:message>
						<fmt:param>
							<lams:Date value="${sessionMap.submissionDeadline}" />
						</fmt:param>
					</fmt:message>
				</lams:Alert5>
			</c:if>
			
			<c:choose>
				<%-- Show on one page or when learner does not choose edit one question --%>
				<c:when test="${sessionMap.showOnOnePage && (empty questionSeqID or questionSeqID == 0)}">
					<c:forEach var="element" items="${sessionMap.questionList}">
						<c:set var="question" value="${element.value}" />
						<%@ include file="/pages/learning/question.jsp"%>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<c:set var="question" value="${sessionMap.questionList[questionSeqID]}" />
					<c:set var="questionSequenceNumber">
						<fmt:message key="label.question" />&nbsp;${currentIdx}&nbsp;<fmt:message key="label.of" />&nbsp;${sessionMap.totalQuestions}
					</c:set>
					<%@ include file="/pages/learning/question.jsp"%>
				</c:otherwise>
			</c:choose>

			<%-- Display button according to different situation --%>
			<%--POSITION: 0=middle of survey, 1=first question, 2=last question, 3=Only one question available--%>
			<div class="activity-bottom-buttons">
				<c:choose>
					<c:when test="${(sessionMap.showOnOnePage && (empty questionSeqID or questionSeqID == 0)) or position == 3}">
						<form:button onclick="submit" path="doSurvey" value="Done" disabled="${sessionMap.finishedLock}"
							class="btn btn-primary na">
							<fmt:message key="label.submit.survey" />
						</form:button>
					</c:when>
				
					<c:otherwise>
						 <c:if test="${position == 2}">
							<form:button onclick="submit" path="doSurvey" disabled="${sessionMap.finishedLock}" class="btn btn-primary na ms-2">
								<fmt:message key="label.submit.survey" />
							</form:button>
						</c:if>
						
						<c:set var="preChecked" value="true" />
						<c:if test="${position == 2 || position == 0}">
							<c:set var="preChecked" value="false" />
						</c:if>
						<c:set var="nextChecked" value="true" />
						<c:if test="${position == 1 || position == 0}">
							<c:set var="nextChecked" value="false" />
						</c:if>

						<c:if test="${position != 2}">
							<form:button path="NextButton" onclick="nextQuestion()" class="btn btn-secondary btn-icon-next ms-2" disabled="${nextChecked}">
								<fmt:message key="label.next" />
							</form:button>
						</c:if>
						
						<div class="float-start"> 
							<form:button path="PreviousButton" onclick="previousQuestion()"
								class="btn btn-secondary btn-icon-previous" disabled="${preChecked}">
								<fmt:message key="label.previous" />
							</form:button>
						</div>
					</c:otherwise>
				</c:choose>
			</div>

		</form:form>
	</div>
</lams:PageLearner>
