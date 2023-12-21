<%@ include file="/common/taglibs.jsp"%>

<div class="card-subheader" id="instructions-${questionIndex}">
	<label for="question${questionIndex}">
		<fmt:message key="label.learning.short.answer.answer" />
	</label>
</div>

<input type="text" autocomplete="off" name="question${questionIndex}"
	id="question${questionIndex}" onkeydown="return event.key != 'Enter';"
	class="form-control <c:if test="${question.autocompleteEnabled}">ui-autocomplete-input</c:if>"
	value="<c:out value='${question.answer}' />"
	data-question-uid="${question.uid}"
	<c:if test="${!hasEditRight}">disabled="disabled"</c:if>
	aria-labelledby="question-title-${questionIndex} instructions-${questionIndex}"
	${question.answerRequired? 'aria-required="true" required="true" ' : ''}
/>
