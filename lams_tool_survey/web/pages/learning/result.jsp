<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<html:html locale="true">
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>

	<script type="text/javascript">
	<!--
		
		function finishSession(){
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}"/>';
			return false;
		}
		function continueReflect(){
			document.location.href='<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
		}
		function retakeSurvey(questionSeqId){
			//retake for all questions
			if(questionSeqId == -1)
				document.location.href='<c:url value="/learning/start.do?mode=${sessionMap.mode}&toolSessionID=${sessionMap.toolSessionID}"/>';
			else
				document.location.href='<c:url value="/learning/retake.do?sessionMapID=${sessionMapID}&questionSeqID="/>' +questionSeqId ;
		}
	-->        
    </script>

</head>
<body class="stripes">


	<div id="content">
		<h1>
			${sessionMap.title}
		</h1>
		<p>
			<c:out value="${sessionMap.instructions}" escapeXml="false" />
		</p>

		<c:forEach var="element" items="${sessionMap.questionList}">
			<div class="shading-bg">
				<c:set var="question" value="${element.value}" />

				<c:if test="${not question.optional}">
					<span style="color: #FF0000">*</span>
				</c:if>

				<c:out value="${question.description}" escapeXml="false" />

				<c:if
					test="${(not sessionMap.finishedLock) && (not sessionMap.showOnOnePage)}">
					<a href="#" onclick="retakeSurvey(${question.sequenceId})"><fmt:message
							key="label.retake" /> </a>
				</c:if>

				<p>
					<c:set var="answerText" value="" />
					<c:if test="${not empty question.answer}">
						<c:set var="answerText" value="${question.answer.answerText}" />
					</c:if>
					<c:forEach var="option" items="${question.options}">
						<div>
							<c:set var="checked" value="false" />

							<c:if test="${not empty question.answer}">
								<c:forEach var="choice" items="${question.answer.choices}">
									<c:if test="${choice == option.uid}">
										<c:set var="checked" value="true" />
									</c:if>
								</c:forEach>
							</c:if>

							<c:if test="${checked}">
								${option.description}
							</c:if>
						</div>
					</c:forEach>

				</p>
				<c:if test="${question.type == 3}">
					<lams:out value="${answerText}" />
				</c:if>

				<c:if test="${question.appendText && (not empty answerText)}">
					<fmt:message key="label.append.text" />
					<lams:out value="${answerText}" />
				</c:if>
			</div>
		</c:forEach>
		
		<c:if test="${not sessionMap.finishedLock}">
			 <html:button
				property="RetakeButton" onclick="return retakeSurvey(-1)"
				styleClass="button">
				<fmt:message key="label.retake.survey" />
			</html:button> 
		</c:if>
		
		<c:if test="${sessionMap.userFinished and sessionMap.reflectOn}">
			<div class="small-space-top">
				<h2>${sessionMap.reflectInstructions}</h2>
			
				<c:choose>
					<c:when test="${empty sessionMap.reflectEntry}">
						<em>
							<fmt:message key="no.reflection.available" />
						</em>
					</c:when>
					<c:otherwise>
						<p> <lams:out escapeXml="true" value="${sessionMap.reflectEntry}" />  </p>				
					</c:otherwise>
				</c:choose>
				
				<html:button property="ContinueButton"
					onclick="return continueReflect()" styleClass="button">
					<fmt:message key="label.edit" />
				</html:button>											
			</div>
		</c:if>
		
		<table>
			<tr>
				<td>
					<span class="right-buttons"> <c:choose>
							<c:when
								test="${sessionMap.reflectOn && mode != 'teacher' && (not sessionMap.userFinished)}">
								<html:button property="ContinueButton"
									onclick="return continueReflect()" styleClass="button">
									<fmt:message key="label.continue" />
								</html:button>
							</c:when>
							<c:otherwise>
								<html:button property="FinishButton"
									onclick="return finishSession()" styleClass="button">
									<fmt:message key="label.finished" />
								</html:button>
							</c:otherwise>
						</c:choose> </span>
				</td>
			</tr>
		</table>
	</div>
	<!--closes content-->

	<div id="footer"></div>
	<!--closes footer-->

</body>
</html:html>


