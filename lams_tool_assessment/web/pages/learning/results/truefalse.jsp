<%@ include file="/common/taglibs.jsp"%>
<c:set var="truefalseInstructions">
	<fmt:message key="label.learning.short.answer.answer" />
</c:set>

<c:if test="${not empty toolSessionID}">
	<div class="card-subheader" id="instructions-${questionIndex}">
		${truefalseInstructions}
	</div>
</c:if>

<fieldset>
	<legend class="visually-hidden">
		${truefalseInstructions}
	</legend>
		
	<div class="table-responsive">
		<div class="table div-hover table-sm">
			<div class="row">
				<div class="complete-item-gif">
					<c:if test="${(question.answer != null) 
							&& (assessment.allowRightAnswersAfterQuestion && (question.answerBoolean == question.correctAnswer) 
							|| assessment.allowWrongAnswersAfterQuestion && (question.answerBoolean != question.correctAnswer)) }">
						
						<c:choose>
							<c:when test="${question.correctAnswer}">
								<i class="fa fa-check text-success"></i>
							</c:when>
							<c:otherwise>
								<i class="fa fa-times text-danger"></i>	
							</c:otherwise>		
						</c:choose>
					</c:if>	
				</div>
				
				<div class="col">
					<div class="form-check ms-3">
						<c:if test="${not empty toolSessionID}">
							<input type="radio" name="question${questionIndex}" id="option-${questionIndex}-true" class="form-check-input me-2"
								value="${true}"
				 				<c:if test="${question.answerBoolean}">checked="checked"</c:if>
								disabled="disabled"	
								aria-labelledby="instructions-${questionIndex} label-${questionIndex}-true"
							/>
						</c:if>
					
						<label for="option-${questionIndex}-true" id="label-${questionIndex}-true" class="form-check-label">
							<fmt:message key="label.learning.true.false.true" />
						</label>
					</div>
				</div>
			</div>
			
			<div class="row">
				<div class="complete-item-gif">
					<c:if test="${(question.answer != null) 
							&& (assessment.allowRightAnswersAfterQuestion && (question.answerBoolean == question.correctAnswer) 
							|| assessment.allowWrongAnswersAfterQuestion && (question.answerBoolean != question.correctAnswer)) }">
						
						<c:choose>
							<c:when test="${!question.correctAnswer}">
								<i class="fa fa-check text-success"></i>	
							</c:when>
							<c:otherwise>
								<i class="fa fa-times text-danger"></i>	
							</c:otherwise>		
						</c:choose>					
					</c:if>		
				</div>
				
				<div class="col">
					<div class="form-check ms-3">
						<c:if test="${not empty toolSessionID}">
							<input type="radio" name="question${questionIndex}" id="option-${questionIndex}-false" class="form-check-input me-2"
								value="${false}"
				 				<c:if test="${(!question.answerBoolean) and (question.answer != null)}">checked="checked"</c:if>
								disabled="disabled"
								aria-labelledby="instructions-${questionIndex} label-${questionIndex}-false"
							/>
						</c:if>
						
						<label for="option-${questionIndex}-false" id="label-${questionIndex}-false" class="form-check-label">
							<fmt:message key="label.learning.true.false.false" />
						</label>
					</div>
				</div>
			</div>
		</div>
	</div>
</fieldset>

<div class="feedback">
	<c:choose>
		<c:when test="${question.answerBoolean}">
			<c:out value="${question.feedbackOnCorrect}" escapeXml="false" />
		</c:when>
		<c:when test="${!question.answerBoolean}">
			<c:out value="${question.feedbackOnIncorrect}" escapeXml="false" />	
		</c:when>		
	</c:choose>
</div>
