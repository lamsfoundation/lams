<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>

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
</lams:head>
<body class="stripes">
	<html:form action="/learning/doSurvey" method="post" styleId="surveyForm" onsubmit="disableButtons();">
		<c:set var="formBean" value="<%=request
							.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
		<html:hidden property="questionSeqID" />
		<html:hidden property="sessionMapID" />
		<html:hidden property="position" />
		<html:hidden property="currentIdx" />
		<c:set var="sessionMapID" value="${formBean.sessionMapID}" />
		<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
		<c:set var="position" value="${formBean.position}" />
		<c:set var="questionSeqID" value="${formBean.questionSeqID}" />
		<c:set var="currentIdx" value="${formBean.currentIdx}" />

		<lams:Page type="learner" title="${sessionMap.title}">

			<div class="panel">
				<c:out value="${sessionMap.instructions}" escapeXml="false" />
			</div>
			<c:if test="${not empty sessionMap.submissionDeadline}">
				<lams:Alert id="submissionDeadline" type="info" close="true">
					<fmt:message>
						<fmt:param>
							<lams:Date value="${sessionMap.submissionDeadline}" />
						</fmt:param>
					</fmt:message>
				</lams:Alert>
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
					<fmt:message key="label.question" /> ${currentIdx} <fmt:message key="label.of" /> ${sessionMap.totalQuestions}
						<%@ include file="/pages/learning/question.jsp"%>
				</c:otherwise>
			</c:choose>

			<%-- Display button according to different situation --%>
			<%--POSITION: 0=middle of survey, 1=first question, 2=last question, 3=Only one question available--%>
			<c:choose>
				<c:when test="${(sessionMap.showOnOnePage && (empty questionSeqID or questionSeqID == 0)) or position == 3}">
					<div class="right-buttons voffset10">
						<html:submit property="doSurvey" disabled="${sessionMap.finishedLock}"
							styleClass="btn btn-sm btn-primary pull-right">
							<fmt:message key="label.submit.survey" />
						</html:submit>
					</div>
				</c:when>
				<c:otherwise>
					<c:set var="preChecked" value="true" />
					<c:if test="${position == 2 || position == 0}">
						<c:set var="preChecked" value="false" />
					</c:if>
					<c:set var="nextChecked" value="true" />
					<c:if test="${position == 1 || position == 0}">
						<c:set var="nextChecked" value="false" />
					</c:if>
					<div class="pull-left voffset10"> <html:button property="PreviousButton" onclick="previousQuestion()"
							styleClass="btn btn-sm btn-default" disabled="${preChecked}">
							<fmt:message key="label.previous" />
						</html:button>
					</div>
					<div class="pull-right voffset10"> <c:if test="${position != 2}">
							<html:button property="NextButton" onclick="nextQuestion()" styleClass="btn btn-sm btn-default" disabled="${nextChecked}">
								<fmt:message key="label.next" />
							</html:button>
						</c:if> <c:if test="${position == 2}">
							<html:submit property="doSurvey" disabled="${sessionMap.finishedLock}" styleClass="btn btn-sm btn-primary">
								<fmt:message key="label.submit.survey" />
							</html:submit>
						</c:if>
					</div>
				</c:otherwise>
			</c:choose>
			<div id="footer"></div>
			<!--closes footer-->
		</lams:Page>
	</html:form>
</body>
</lams:html>
