<%@ include file="/common/taglibs.jsp"%>
<c:set var="mcqInstructions">
	<c:choose>
		<c:when test="${question.multipleAnswersAllowed}">
			<fmt:message key="label.learning.choose.at.least.one.answer" />
		</c:when>
		<c:otherwise>
			<fmt:message key="label.learning.choose.one.answer" />
		</c:otherwise>
	</c:choose>
</c:set>

<c:if test="${not empty toolSessionID}">
	<div class="card-subheader">
		${mcqInstructions}
	</div>
</c:if>

<fieldset>
	<legend class="visually-hidden">
		${mcqInstructions}
	</legend>

	<div class="table-sm div-hover">
		<c:forEach var="option" items="${question.optionDtos}" varStatus="answerStatus">
			<c:set var="isCorrect" value="${(assessment.allowDiscloseAnswers 
				   			 ? question.correctAnswersDisclosed : assessment.allowRightAnswersAfterQuestion)
				    		 && (option.maxMark > 0)}" />
			<c:set var="isWrong" value="${(assessment.allowDiscloseAnswers 
				   			 ? question.correctAnswersDisclosed : assessment.allowWrongAnswersAfterQuestion)
				    		&& (option.maxMark <= 0)}" />
	
			<div class="row ${isCorrect ? 'bg-success bg-opacity-25 rounded' : '' }">
				<div class="complete-item-gif">
				    <c:if test="${option.answerBoolean && isCorrect}">
					    <i class="fa fa-solid fa-check fa-lg text-success"></i>
		            </c:if>
				    <c:if test="${option.answerBoolean && isWrong}">
					    <i class="fa fa-lg fa-xmark text-danger"></i>	
					</c:if>			
				</div>
	                
				<div class="col">
					<div class="form-check ms-3">
						<c:if test="${not empty toolSessionID}">
							<c:choose>
								<c:when test="${question.multipleAnswersAllowed}">
									<input type="checkbox" name="question${questionIndex}_${option.displayOrder}" id="option-${questionIndex}-${option.uid}" 
											class="form-check-input" 
											value="${true}"
											<c:if test="${option.answerBoolean}">checked="checked"</c:if>
											disabled="disabled"/>
								</c:when>
								<c:otherwise>
									<input type="radio" name="question${questionIndex}" id="option-${questionIndex}-${option.uid}"
											class="form-check-input" 
											value="${option.displayOrder}"
				 							<c:if test="${option.answerBoolean}">checked="checked"</c:if>
				 							disabled="disabled"/>
								</c:otherwise>
							</c:choose>
							
							<label class="form-check-label" for="option-${questionIndex}-${option.uid}">
						</c:if>

						<c:if test="${question.prefixAnswersWithLetters}">
					 		&nbsp;${option.formatPrefixLetter(answerStatus.index)}
			 	        </c:if>
			 	        <c:out value="${option.name}" escapeXml="false" />
			 	        
			 	        <c:if test="${not empty toolSessionID}">
		 	            	</label>
		 	            </c:if>
	 	            </div>
				</div>
					
				<c:if test="${assessment.allowQuestionFeedback}">
					<div style="width:30%;">
						<c:if test="${option.answerBoolean}">
							<c:out value="${option.feedback}" escapeXml="false" />
						</c:if>
					</div>		
				</c:if>
				
				<c:if test="${assessment.allowDiscloseAnswers && question.groupsAnswersDisclosed && fn:length(sessions) > 1}">
					<c:set var="teams" value="" />
					<c:forEach var="session" items="${sessions}" varStatus="sessionStatus">
						<%-- Now groups other than this one --%>
						<c:if test="${sessionMap.toolSessionID != session.sessionId}">
							<%-- Get the needed piece of information from a complicated questionSummaries structure --%>
							<c:set var="sessionResults" value="${questionSummaries[question.uid].questionResultsPerSession[sessionStatus.index]}" />
							<c:set var="sessionResults" value="${sessionResults[fn:length(sessionResults)-1]}" />
							<c:forEach var="sessionOption" items="${sessionResults.optionAnswers}">
								<c:if test="${sessionOption.answerBoolean && option.uid == sessionOption.optionUid}">
									<c:set var="teams">
										${teams}
										<lams:Portrait userId="${session.groupLeader.userId}"/>&nbsp;
										<c:out value="${session.sessionName}" escapeXml="true"/>&nbsp;
										<c:if test="${not empty sessionResults.justification}">
											<lams:Popover titleKey="label.answer.justification">
								          		<c:out value='${sessionResults.justification}' />
								          	</lams:Popover>
										</c:if>
									</c:set>
								</c:if>
							</c:forEach>
						</c:if>
					</c:forEach>
						
					<c:if test="${not empty teams}">
						<div class="selected-by-groups mt-2">
							<div class="fst-italic fw-semibold">
								<fmt:message key="label.learning.summary.selected.by" />
								&nbsp;${teams}
							</div>
						</div>
					</c:if>
				</c:if>
			</div>
		</c:forEach>
	</div>
</fieldset>

<c:if test="${assessment.allowQuestionFeedback}">
	<div class="feedback">
		<c:choose>
			<c:when test="${question.optionMaxMark >= 1}">
				<c:out value="${question.feedbackOnCorrect}" escapeXml="false" />
			</c:when>
			<c:when test="${question.optionMaxMark > 0}">
				<c:out value="${question.feedbackOnPartiallyCorrect}" escapeXml="false" />
			</c:when>
			<c:otherwise>
				<c:out value="${question.feedbackOnIncorrect}" escapeXml="false" />
			</c:otherwise>		
		</c:choose>
	</div>
</c:if>
