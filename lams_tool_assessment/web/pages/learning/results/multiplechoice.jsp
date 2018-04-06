<%@ include file="/common/taglibs.jsp"%>

<div class="question-type">
	<c:choose>
		<c:when test="${question.multipleAnswersAllowed}">
			<fmt:message key="label.learning.choose.at.least.one.answer" />
		</c:when>
		<c:otherwise>
			<fmt:message key="label.learning.choose.one.answer" />
		</c:otherwise>
	</c:choose>
</div>

<div class="table-responsive">
	<table class="table table-hover table-condensed">
		<c:forEach var="option" items="${question.optionDtos}">
			<tr>
				<td class="complete-item-gif">
				    <c:if test="${(assessment.allowDiscloseAnswers ? question.correctAnswersDisclosed : assessment.allowRightAnswersAfterQuestion)
				    			   && option.answerBoolean && (option.grade > 0)}">
					    <i class="fa fa-check text-success"></i>
		            </c:if>
				    <c:if test="${(assessment.allowDiscloseAnswers ? question.correctAnswersDisclosed : assessment.allowWrongAnswersAfterQuestion)
				    			  && option.answerBoolean && (option.grade <= 0)}">
					    <i class="fa fa-times text-danger"></i>	
				    </c:if>			
                </td>
				<td class="has-radio-button">
					<c:choose>
						<c:when test="${question.multipleAnswersAllowed}">
							<input type="checkbox" name="question${status.index}_${option.sequenceId}" value="${true}"
		 						<c:if test="${option.answerBoolean}">checked="checked"</c:if>
								disabled="disabled"
							/>
						</c:when>
						<c:otherwise>
							<input type="radio" name="question${status.index}" value="${option.sequenceId}"
		 						<c:if test="${option.answerBoolean}">checked="checked"</c:if>
		 						disabled="disabled"
							/>
						</c:otherwise>
					</c:choose>
				</td>
				
				<td>
					<c:out value="${option.optionString}" escapeXml="false" />
				</td>
				
				<c:if test="${assessment.allowQuestionFeedback}">
	
					<td width=30%;">
						<c:if test="${option.answerBoolean}">
							<c:out value="${option.feedback}" escapeXml="false" />
						</c:if>
					</td>		
				</c:if>
				
			</tr>
		</c:forEach>
	</table>
</div>	

<c:if test="${assessment.allowQuestionFeedback}">
	<div class="feedback">
		<c:choose>
			<c:when test="${question.answerTotalGrade >= 1}">
				<c:out value="${question.feedbackOnCorrect}" escapeXml="false" />
			</c:when>
			<c:when test="${question.answerTotalGrade > 0}">
				<c:out value="${question.feedbackOnPartiallyCorrect}" escapeXml="false" />
			</c:when>
			<c:otherwise>
				<c:out value="${question.feedbackOnIncorrect}" escapeXml="false" />
			</c:otherwise>		
		</c:choose>
	</div>
</c:if>
