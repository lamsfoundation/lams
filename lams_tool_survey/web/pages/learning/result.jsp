<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>
	
	<lams:JSImport src="learning/includes/javascript/gate-check.js" />
	
	<script type="text/javascript">
		checkNextGateActivity('finishButton', '${sessionMap.toolSessionID}', '', finishSession);
		
		function finishSession(){
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}"/>';
		}
		
		function showOtherUsersAnswers(){
			disableButtons();
			document.location.href='<c:url value="/learning/showOtherUsersAnswers.do?sessionMapID=${sessionMapID}"/>';
		}
		
		function continueReflect(){
			disableButtons();
			document.location.href='<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
		}
		
		function retakeSurvey(questionSeqId){
			disableButtons();
			//retake for all questions
			if(questionSeqId == -1) {
				document.location.href='<c:url value="/learning/retake.do?sessionMapID=${sessionMapID}"/>';
			} else {
				document.location.href='<c:url value="/learning/retake.do?sessionMapID=${sessionMapID}&questionSeqID="/>' +questionSeqId ;
			}
		}
		function disableButtons() {
			$('.btn').prop('disabled', true);
		}
    </script>
</lams:head>
<body class="stripes">

	<lams:Page type="learner" title="${sessionMap.title}">
		<div class="panel">
			<c:out value="${sessionMap.instructions}" escapeXml="false" />
		</div>

		<c:if test="${sessionMap.lockOnFinish and sessionMap.mode != 'teacher'}">
			<div class="voffset5">
				<c:choose>
					<c:when test="${sessionMap.showOtherUsersAnswers}">
						<c:set var="buttonLabel">
							<fmt:message key="label.view.all.responses" />
						</c:set>
						<fmt:message key="message.warnLockOnFinish">
							<fmt:param>${buttonLabel}</fmt:param>
						</fmt:message>
					</c:when>

					<c:when test="${sessionMap.userFinished}">
						<lams:Alert id="activityLocked" type="info" close="true">
							<fmt:message key="message.activityLocked" />
						</lams:Alert>
					</c:when>

					<c:otherwise>
						<c:set var="buttonLabel">
							<c:choose>
								<c:when test="${sessionMap.isLastActivity}">
									<fmt:message key="label.submit" />
								</c:when>

								<c:otherwise>
									<fmt:message key="label.finished" />
								</c:otherwise>
							</c:choose>
						</c:set>
						<lams:Alert id="lockWhenFinished" type="info" close="true">
							<fmt:message key="message.warnLockOnFinish">
								<fmt:param>
									<strong>${buttonLabel}</strong>
								</fmt:param>
							</fmt:message>
						</lams:Alert>
					</c:otherwise>
				</c:choose>
			</div>
		</c:if>

		<c:forEach var="element" items="${sessionMap.questionList}">
			<div class="sbox voffset5">
				<div class="sbox-heading clearfix">
					<c:if test="${not question.optional}">
						<abbr class="pull-right" title="<fmt:message key='label.answer.required'/>"><i
							class="fa fa-xs fa-asterisk text-danger pull-right"></i></abbr>
					</c:if>

					<c:set var="question" value="${element.value}" />
					<c:out value="${question.description}" escapeXml="false" />
				</div>
				<div class="sbox-body">

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
								<c:out value="${option.description}" escapeXml="true" />
							</c:if>
						</div>
					</c:forEach>


					<c:if test="${question.type == 3}">
						<lams:out value="${answerText}" escapeHtml="false" />
					</c:if>

					<c:if test="${question.appendText && (not empty answerText)}">
						<fmt:message key="label.append.text" />
						<lams:out value="${answerText}" escapeHtml="true" />
					</c:if>

					<c:if test="${(not sessionMap.finishedLock) && (not sessionMap.showOnOnePage)}">
						<div class="">
							<a href="#" onclick="retakeSurvey(${question.sequenceId})"><small><fmt:message key="label.retake" /></small>
							</a>
						</div>
					</c:if>


				</div>
			</div>
		</c:forEach>

		<c:if test="${not sessionMap.finishedLock}">
			<div class="voffset5">
				<button property="RetakeButton" onclick="return retakeSurvey(-1)" class="btn btn-sm btn-default pull-left">
					<fmt:message key="label.retake.survey" />
				</button>
			</div>
		</c:if>

		<div class="voffset10">&nbsp;</div>

		<c:if test="${sessionMap.userFinished and sessionMap.reflectOn and !sessionMap.showOtherUsersAnswers}">
			<div class="panel panel-default voffset10">
				<div class="panel-heading panel-title">
					<fmt:message key="title.reflection" />
				</div>
				<div class="panel-body">
					<div class="panel">
						<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true" />
					</div>

					<c:choose>
						<c:when test="${empty sessionMap.reflectEntry}">
							<p>
								<fmt:message key="message.no.reflection.available" />
							</p>
						</c:when>
						<c:otherwise>
							<div class="panel-body bg-warning voffset5">
								<lams:out escapeHtml="true" value="${sessionMap.reflectEntry}" />
							</div>
						</c:otherwise>
					</c:choose>

					<button property="ContinueButton" onclick="return continueReflect()"
						class="btn voffset5 btn-sm btn-default pull-left">
						<fmt:message key="label.edit" />
					</button>
				</div>
			</div>
		</c:if>

		<c:if test="${sessionMap.mode != 'teacher'}">
			<div class="pull-right voffset10">
				<c:choose>
					<c:when test="${sessionMap.showOtherUsersAnswers}">
						<button property="otherUsersAnswersButton" onclick="return showOtherUsersAnswers()"
							class="btn btn-sm btn-default">
							<fmt:message key="label.view.all.responses" />
						</button>
					</c:when>

					<c:when test="${sessionMap.reflectOn}">
						<button property="ContinueButton" onclick="return continueReflect()"
							class="btn btn-sm btn-primary pull-right">
							<fmt:message key="label.continue" />
						</button>
					</c:when>

					<c:otherwise>
						<button type="submit" id="finishButton"
							class="btn btn-sm btn-primary pull-right na">
							<c:choose>
								<c:when test="${sessionMap.isLastActivity}">
									<fmt:message key="label.submit" />
								</c:when>

								<c:otherwise>
									<fmt:message key="label.finished" />
								</c:otherwise>
							</c:choose>
						</button>
					</c:otherwise>
				</c:choose>
			</div>
		</c:if>

		<div id="footer"></div>
		<!--closes footer-->
	</lams:Page>
</body>
</lams:html>


