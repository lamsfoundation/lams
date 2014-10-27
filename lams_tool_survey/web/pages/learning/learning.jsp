<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" />
	</title>
	<%@ include file="/common/header.jsp"%>

	<script type="text/javascript">
		function previousQuestion(sessionMapID){
			$("surveyForm").action = '<c:url value="/learning/previousQuestion.do"/>';
			$("surveyForm").submit();
		}
		
		function nextQuestion(sessionMapID){
			$("surveyForm").action = '<c:url value="/learning/nextQuestion.do"/>';
			$("surveyForm").submit();
		}
		
		function singleChoice(choiceName){
			var rs = document.getElementsByName(choiceName);
			for(idx=0;idx<rs.length;idx++)
				rs[idx].checked=false;
		}  
    </script>
</lams:head>
<body class="stripes">
	<html:form action="/learning/doSurvey" method="post" styleId="surveyForm">
		<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
		<html:hidden property="questionSeqID" />
		<html:hidden property="sessionMapID" />
		<html:hidden property="position" />
		<html:hidden property="currentIdx" />
		<html:hidden property="userID" />
		<c:set var="sessionMapID" value="${formBean.sessionMapID}" />
		<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
		<c:set var="position" value="${formBean.position}" />
		<c:set var="questionSeqID" value="${formBean.questionSeqID}" />
		<c:set var="currentIdx" value="${formBean.currentIdx}" />
		<c:set var="userID" value="${formBean.userID}" />

		<div id="content">
			<h1>
				<c:out value="${sessionMap.title}" escapeXml="true"/>
			</h1>
			<p>
				<c:out value="${sessionMap.instructions}" escapeXml="false" />
			</p>
			<c:if test="${not empty sessionMap.submissionDeadline}">
				<div class="info">
				 	<fmt:message>
						<fmt:param><lams:Date value="${sessionMap.submissionDeadline}" /></fmt:param>
					</fmt:message>
				</div>
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
					<fmt:message key="label.question" /> ${currentIdx} <fmt:message
						key="label.of" /> ${sessionMap.totalQuestions}
						<%@ include file="/pages/learning/question.jsp"%>
				</c:otherwise>
			</c:choose>

			<%-- Display button according to different situation --%>
			<%--POSITION: 0=middle of survey, 1=first question, 2=last question, 3=Only one question available--%>
			<table>
				<tr>
					<td>
						<c:choose>
							<c:when
								test="${(sessionMap.showOnOnePage && (empty questionSeqID or questionSeqID == 0)) or position == 3}">
								<div class="right-buttons">
									<html:submit property="doSurvey"
										disabled="${sessionMap.finishedLock}" styleClass="button">
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
								<span class="left-buttons"> <html:button
										property="PreviousButton" onclick="previousQuestion()"
										styleClass="button" disabled="${preChecked}">
										<fmt:message key="label.previous" />
									</html:button> </span>
								<span class="right-buttons"> <c:if
										test="${position != 2}">
										<html:button property="NextButton" onclick="nextQuestion()"
											styleClass="button" disabled="${nextChecked}">
											<fmt:message key="label.next" />
										</html:button>
									</c:if> <c:if test="${position == 2}">
										<html:submit property="doSurvey"
											disabled="${sessionMap.finishedLock}" styleClass="button">
											<fmt:message key="label.submit.survey" />
										</html:submit>
									</c:if> </span>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</table>
		</div>
		<!--closes content-->

		<div id="footer"></div>
		<!--closes footer-->
	</html:form>
</body>
</lams:html>
