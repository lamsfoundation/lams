<%@ include file="/common/taglibs.jsp"%>

<div class="question-type">
	<fmt:message key="label.learning.short.answer.answer" />
</div>

<div class="table-responsive">
	<table class="table table-hover table-condensed">
		<tr>
			<td>
				<input type="text" autocomplete="off" name="question${status.index}" onkeydown="return event.key != 'Enter';"
					class="form-control 
					<c:if test="${question.autocompleteEnabled}">ui-autocomplete-input</c:if>" 
					value="<c:out value='${question.answer}' />" data-question-uid="${question.uid}"  
					<c:if test="${!hasEditRight}">disabled="disabled"</c:if>
				/>
			</td>	
		</tr>
	</table>
</div>
