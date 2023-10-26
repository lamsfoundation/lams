<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<lams:PageLearner title="${sessionMap.title}" toolSessionID="${sessionMap.toolSessionID}">
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

	<div id="container-main">
		<div id="instructions" class="instructions">
			<c:out value="${sessionMap.instructions}" escapeXml="false" />
		</div>

		<c:if test="${sessionMap.lockOnFinish and sessionMap.mode != 'teacher'}">
			<c:choose>
				<c:when test="${sessionMap.showOtherUsersAnswers}">
					<lams:Alert5 id="warnLockOnFinish" type="info" close="true">
						<c:set var="buttonLabel">
							<fmt:message key="label.view.all.responses" />
						</c:set>
						<fmt:message key="message.warnLockOnFinish">
							<fmt:param>${buttonLabel}</fmt:param>
						</fmt:message>
					</lams:Alert5>
				</c:when>

				<c:when test="${sessionMap.userFinished}">
					<lams:Alert5 id="activityLocked" type="info" close="true">
						<fmt:message key="message.activityLocked" />
					</lams:Alert5>
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
					
					<lams:Alert5 id="lockWhenFinished" type="info" close="true">
						<fmt:message key="message.warnLockOnFinish">
							<fmt:param>
								<strong>${buttonLabel}</strong>
							</fmt:param>
						</fmt:message>
					</lams:Alert5>
				</c:otherwise>
			</c:choose>
		</c:if>

		<c:forEach var="element" items="${sessionMap.questionList}">
			<div class="card lcard">
				<div class="card-header">
					<c:if test="${not question.optional}">
						<abbr class="float-end" title="<fmt:message key='label.answer.required'/>">
							<i class="fa fa-xs fa-asterisk text-danger float-end"></i>
						</abbr>
					</c:if>

					<c:set var="question" value="${element.value}" />
					<c:out value="${question.description}" escapeXml="false" />
				</div>
				
				<div class="card-body">
					<c:set var="answerText" value="" />
					<c:if test="${not empty question.answer}">
						<c:set var="answerText" value="${question.answer.answerText}" />
					</c:if>
					<div class="div-hover">
						<c:forEach var="option" items="${question.options}">
							<c:set var="checked" value="false" />

							<c:if test="${not empty question.answer}">
								<c:forEach var="choice" items="${question.answer.choices}">
									<c:if test="${choice == option.uid}">
										<c:set var="checked" value="true" />
									</c:if>
								</c:forEach>
							</c:if>

							<c:if test="${checked}">
								<div>
									<c:out value="${option.description}" escapeXml="true" />
								</div>
							</c:if>
						</c:forEach>

						<c:if test="${question.appendText && (not empty answerText)}">
							<div>
								<fmt:message key="label.open.response" />:&nbsp;
								
								<lams:out value="${answerText}" escapeHtml="true" />
							</div>
						</c:if>

						<c:if test="${question.type == 3}">
							<div>
								<lams:out value="${answerText}" escapeHtml="false" />
							</div>
						</c:if>
					</div>

					<c:if test="${(not sessionMap.finishedLock) && (not sessionMap.showOnOnePage)}">
						<div>
							<button type="button" onclick="retakeSurvey(${question.sequenceId})" class="btn btn-sm btn-secondary btn-icon-return float-end mt-1">
								<small><fmt:message key="label.retake" /></small>
							</button>
						</div>
					</c:if>

				</div>
			</div>
		</c:forEach>

		<c:if test="${sessionMap.userFinished and sessionMap.reflectOn and !sessionMap.showOtherUsersAnswers}">
			<lams:NotebookReedit
				reflectInstructions="${sessionMap.reflectInstructions}"
				reflectEntry="${sessionMap.reflectEntry}"
				isEditButtonEnabled="true"
				notebookHeaderLabelKey="title.reflection"/>
		</c:if>

		<c:if test="${sessionMap.mode != 'teacher'}">
			<div class="activity-bottom-buttons">
				<c:choose>
					<c:when test="${sessionMap.showOtherUsersAnswers}">
						<button property="otherUsersAnswersButton" onclick="return showOtherUsersAnswers()"
							class="btn btn-primary na">
							<fmt:message key="label.view.all.responses" />
						</button>
					</c:when>

					<c:when test="${sessionMap.reflectOn}">
						<button property="ContinueButton" onclick="return continueReflect()"
							class="btn btn-primary na">
							<fmt:message key="label.continue" />
						</button>
					</c:when>

					<c:otherwise>
						<button type="submit" id="finishButton" class="btn btn-primary na">
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

				<c:if test="${not sessionMap.finishedLock}">
					<button type="button" onclick="return retakeSurvey(-1)" class="btn btn-secondary btn-icon-return me-2">
						<fmt:message key="label.retake.survey" />
					</button>
				</c:if>
			</div>
		</c:if>

	</div>
</lams:PageLearner>
