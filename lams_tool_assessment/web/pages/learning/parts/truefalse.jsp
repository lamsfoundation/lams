<%@ include file="/common/taglibs.jsp"%>
<c:set var="truefalseInstructions">
	<fmt:message key="label.learning.short.answer.answer" />
</c:set>

<div class="card-subheader" id="instructions-${questionIndex}">
	${truefalseInstructions}
</div>

<fieldset>
	<legend class="visually-hidden">
		${truefalseInstructions}
	</legend>
	
	<div class="table-sm div-hover mx-3">	
		<div class="row">
			<div class="col">
				<div class="form-check ms-3">
					<input type="radio" name="question${questionIndex}" id="option-${questionIndex}-true" class="form-check-input me-2"
							value="${true}"
						    onclick="javascript:logLearnerInteractionEvent(1, ${question.uid}, 1)"
						<c:if test="${question.answerBoolean}">checked="checked"</c:if>
						<c:if test="${!hasEditRight}">disabled="disabled"</c:if>	
						aria-labelledby="instructions-${questionIndex} option-name-true"				 
					/>
						
					<label for="option-${questionIndex}-true" id="option-name-true" class="form-check-label">
						<fmt:message key="label.learning.true.false.true" />
					</label>
				</div>
			</div>
		</div>
		
		<div class="row">
			<div class="col">
				<div class="form-check ms-3">
					<input type="radio" name="question${questionIndex}" id="option-${questionIndex}-false" class="form-check-input me-2"
							value="${false}"
						    onclick="javascript:logLearnerInteractionEvent(1, ${question.uid}, 2)" 
						<c:if test="${(!question.answerBoolean) and (question.answer != null)}">checked="checked"</c:if>
						<c:if test="${!hasEditRight}">disabled="disabled"</c:if>
						aria-labelledby="instructions-${questionIndex} option-name-false"
					/>
		
					<label for="option-${questionIndex}-false" id="option-name-false" class="form-check-label">
						<fmt:message key="label.learning.true.false.false" />
					</label>
				</div>
			</div>
		</div>
	</div>
</fieldset>