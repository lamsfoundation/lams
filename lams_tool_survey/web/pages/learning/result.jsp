<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>

	<script type="text/javascript">
		function finishSession(){
			document.getElementById("finishButton").disabled = true;
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}"/>';
			return false;
		}
		
		function showOtherUsersAnswers(){
			document.location.href='<c:url value="/learning/showOtherUsersAnswers.do?sessionMapID=${sessionMapID}"/>';
		}
		
		function continueReflect(){
			document.location.href='<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
		}
		
		function retakeSurvey(questionSeqId){
			//retake for all questions
			if(questionSeqId == -1) {
				document.location.href='<c:url value="/learning/retake.do?sessionMapID=${sessionMapID}"/>';
			} else {
				document.location.href='<c:url value="/learning/retake.do?sessionMapID=${sessionMapID}&questionSeqID="/>' +questionSeqId ;
			}
		}
    </script>
</lams:head>
<body class="stripes">

	<div id="content">
		<h1>
			<c:out value="${sessionMap.title}" escapeXml="true"/>
		</h1>
		<p>
			<c:out value="${sessionMap.instructions}" escapeXml="false" />
		</p>

		<c:if test="${sessionMap.lockOnFinish and sessionMap.mode != 'teacher'}">
			<div class="info space-bottom">
				<c:choose>
					<c:when test="${sessionMap.showOtherUsersAnswers}">
						<c:set var="buttonLabel"><fmt:message key="label.view.all.responses" /></c:set>
						<fmt:message key="message.warnLockOnFinish" >
							<fmt:param>${buttonLabel}</fmt:param>
						</fmt:message>
					</c:when>
								
					<c:when test="${sessionMap.userFinished}">
						<fmt:message key="message.activityLocked" />
					</c:when>
					
					<c:otherwise>
						<c:set var="buttonLabel">
							<c:choose>
							 	<c:when test="${sessionMap.activityPosition.last}">
							 		<fmt:message key="label.submit" />
							 	</c:when>
							 					
							 	<c:otherwise>
							 		 <fmt:message key="label.finished" />
							 	</c:otherwise>
							 </c:choose>
						</c:set>
						<fmt:message key="message.warnLockOnFinish" >
							<fmt:param>${buttonLabel}</fmt:param>
						</fmt:message>
					</c:otherwise>
				</c:choose>
			</div>
		</c:if>
		
		<c:forEach var="element" items="${sessionMap.questionList}">
			<div class="shading-bg">
				<c:set var="question" value="${element.value}" />

				<c:if test="${not question.optional}">
					<span style="color: #FF0000">*</span>
				</c:if>

				<c:out value="${question.description}" escapeXml="false" />

				<c:if test="${(not sessionMap.finishedLock) && (not sessionMap.showOnOnePage)}">
					<a href="#" onclick="retakeSurvey(${question.sequenceId})">
						<fmt:message key="label.retake" /> 
					</a>
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
								<c:out value="${option.description}" escapeXml="true"/>
							</c:if>
						</div>
					</c:forEach>

				</p>
				<c:if test="${question.type == 3}">
					<lams:out value="${answerText}" escapeHtml="false"/>
				</c:if>

				<c:if test="${question.appendText && (not empty answerText)}">
					<fmt:message key="label.append.text" />
					<lams:out value="${answerText}" escapeHtml="true" />
				</c:if>
			</div>
		</c:forEach>

		<c:if test="${not sessionMap.finishedLock}">
			<html:button property="RetakeButton" onclick="return retakeSurvey(-1)" styleClass="button">
				<fmt:message key="label.retake.survey" />
			</html:button>
		</c:if>

		<c:if test="${sessionMap.userFinished and sessionMap.reflectOn and !sessionMap.showOtherUsersAnswers}">
			<div class="small-space-top">
				<h2><fmt:message key="title.reflection" /></h2>
				<strong>
					<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true"/>
				</strong>

				<c:choose>
					<c:when test="${empty sessionMap.reflectEntry}">
						<p>
							<em> <fmt:message key="message.no.reflection.available" /></em>
						</p>
					</c:when>
					<c:otherwise>
						<p>
							<lams:out escapeHtml="true" value="${sessionMap.reflectEntry}" />
						</p>
					</c:otherwise>
				</c:choose>

				<html:button property="ContinueButton" onclick="return continueReflect()" styleClass="button">
					<fmt:message key="label.edit" />
				</html:button>
			</div>
		</c:if>

		<c:if test="${sessionMap.mode != 'teacher'}">
			<table>
				<tr>
					<td>
						<span class="right-buttons">
							<c:choose>
								<c:when test="${sessionMap.showOtherUsersAnswers}">
									<html:button property="otherUsersAnswersButton" onclick="return showOtherUsersAnswers()" styleClass="button">
										<fmt:message key="label.view.all.responses" />
									</html:button>
								</c:when>
							
								<c:when test="${sessionMap.reflectOn}">
									<html:button property="ContinueButton" onclick="return continueReflect()" styleClass="button">
										<fmt:message key="label.continue" />
									</html:button>
								</c:when>
								
								<c:otherwise>
									<html:link href="#nogo" property="FinishButton" styleId="finishButton"
										onclick="return finishSession()" styleClass="button">
										<span class="nextActivity">
											<c:choose>
							 					<c:when test="${sessionMap.activityPosition.last}">
							 						<fmt:message key="label.submit" />
							 					</c:when>
							 					
							 					<c:otherwise>
							 		 				<fmt:message key="label.finished" />
							 					</c:otherwise>
							 				</c:choose>
							 			</span>
									</html:link>
								</c:otherwise>
							</c:choose> 
						</span>
					</td>
				</tr>
			</table>
		</c:if>
	</div>
	<!--closes content-->

	<div id="footer"></div>
	<!--closes footer-->

</body>
</lams:html>


