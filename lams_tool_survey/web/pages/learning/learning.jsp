<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<html:html locale="true">
<head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>

	<script type="text/javascript">
	<!--
		
		function previousQuestion(sessionMapID){
			$("surveyForm").action = '<c:url value="/learning/previousQuestion.do"/>';
			$("surveyForm").submit();
		}
		function nextQuestion(sessionMapID){
			$("surveyForm").action = '<c:url value="/learning/nextQuestion.do"/>';
			$("surveyForm").submit();
		}
		
	-->        
    </script>
</head>
<body>
	<html:form action="/learning/doSurvey" method="post" styleId="surveyForm">
	<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
	<html:hidden property="questionSeqID"/>
	<html:hidden property="sessionMapID"/>
	<html:hidden property="position"/>
	<c:set var="sessionMapID" value="${formBean.sessionMapID}"/>
	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
	<c:set var="position" value="${formBean.position}"/>
	<c:set var="questionSeqID" value="${formBean.questionSeqID}"/>
	<div id="page-learner">
		<h1 class="no-tabs-below">
			${sessionMap.title}
		</h1>
		<div id="header-no-tabs-learner">
		</div>
		<!--closes header-->

		<div id="content-learner">
			<h2>
				${sessionMap.instructions}
			</h2>
			<BR><BR>
				<table cellpadding="0" cellspacing="0" class="alternative-color">
					<c:choose>
						<%-- Show on one page or when learner does not choose edit one question --%>
						<c:when test="${sessionMap.showOnOnePage && (empty questionSeqID or questionSeqID == 0)}">
							<c:forEach var="element" items="${sessionMap.questionList}">
								<c:set var="question" value="${element.value}" />
								<%@ include file="/pages/learning/question.jsp"%>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<c:set var="question" value="${sessionMap.questionList[questionSeqID]}"/>
							<%@ include file="/pages/learning/question.jsp"%>
						</c:otherwise>
					</c:choose>
				</table>
				<%-- Display button according to different situation --%>
				<%--POSITION: 0=middle of survey, 1=first question, 2=last question, 3=Only one question available--%>
					<c:choose>
						<c:when test="${(sessionMap.showOnOnePage && (empty questionSeqID or questionSeqID == 0)) or position == 3}">
							<div class="right-buttons">
								<html:submit property="doSurvey"  disabled="${sessionMap.finishedLock}"  styleClass="button">
									<fmt:message key="label.submit.survey" />
								</html:submit>
							</div>
						</c:when>
						<c:otherwise>
							<c:set var="preChecked" value="true"/>
							<c:if test="${position == 2 || position == 0}">
								<c:set var="preChecked" value="false"/>
							</c:if>
							<c:set var="nextChecked" value="true"/>
							<c:if test="${position == 1 || position == 0}">
								<c:set var="nextChecked" value="false"/>
							</c:if>
							<span class="left-buttons">
								<html:button property="PreviousButton"  onclick="previousQuestion()" styleClass="button" disabled="${preChecked}">
									<fmt:message key="label.previous" />
								</html:button> 
							</span>
							<span class="right-buttons">
								<html:button property="NextButton"  onclick="nextQuestion()" styleClass="button"  disabled="${nextChecked}">
									<fmt:message key="label.next" />
								</html:button>
								<c:if test="${position == 2}">
									<html:submit property="doSurvey"  disabled="${sessionMap.finishedLock}"  styleClass="button">
										<fmt:message key="label.submit.survey" />
									</html:submit>
								</c:if>
							</span>
						</c:otherwise>
					</c:choose>
		
		</div>  <!--closes content-->
		
		<div id="footer-learner">
		</div>
		<!--closes footer-->

	</div><!--closes page-->
</html:form>	
</body>
</html:html>

