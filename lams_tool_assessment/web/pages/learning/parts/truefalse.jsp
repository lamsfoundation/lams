<%@ include file="/common/taglibs.jsp"%>

<div class="question-type">
	<fmt:message key="label.learning.short.answer.answer" />
</div>

<div class="table-responsive">
	<table class="table table-hover table-condensed">
		<tr>
			<td class="has-radio-button">
				<input type="radio" name="question${status.index}" value="${true}"
					   onclick="javascript:logLearnerInteractionEvent(1, ${question.uid}, 1)"
	 				<c:if test="${question.answerBoolean}">checked="checked"</c:if>
					<c:if test="${!hasEditRight}">disabled="disabled"</c:if>					 
				/>
			</td>
			<td>
				<fmt:message key="label.learning.true.false.true" />
			</td>
		</tr>
		<tr>
			<td class="has-radio-button">
				<input type="radio" name="question${status.index}" value="${false}"
					   onclick="javascript:logLearnerInteractionEvent(1, ${question.uid}, 2)" 
	 				<c:if test="${(!question.answerBoolean) and (question.answer != null)}">checked="checked"</c:if>
					<c:if test="${!hasEditRight}">disabled="disabled"</c:if>
				/>
			</td>
			<td>
				<fmt:message key="label.learning.true.false.false" />
			</td>
		</tr>
	</table>
</div>
