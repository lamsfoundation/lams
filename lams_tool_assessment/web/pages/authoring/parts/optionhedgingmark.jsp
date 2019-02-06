<input type="hidden" name="optionSequenceId${status.index}" value="${option.sequenceId}">
<input type="hidden" name="optionUid${status.index}" value="${option.uid}">

<div class="option-ckeditor">
	<c:set var="OPTION_ANSWER_LABEL"><fmt:message key="label.authoring.basic.option.answer"/></c:set>
	<lams:CKEditor id="optionString${status.index}" value="${option.optionString}" contentFolderID="${contentFolderID}" 
		placeholder="${OPTION_ANSWER_LABEL}&thinsp; ${status.index+1}" height="50px"/>
</div>

<div class="settings-on-hover-hidden">
	<div class="radio">
		<label for="option-correct">
			<input type="radio" alt="${status.index}" name="optionCorrect" value="${option.sequenceId}" id="option-correct"
					<c:if test="${option.correct}">checked="checked"</c:if> >
			<span class="greyed-out-label">
				<fmt:message key="label.option.correct" />
			</span>
		</label>
	</div>
	
	<div class="voffset5-bottom">
	    <c:set var="FEEDBACK_LABEL"><fmt:message key="label.authoring.basic.option.feedback"/></c:set>
	    <lams:CKEditor id="optionFeedback${status.index}" value="${option.feedback}"
	    	placeholder="${FEEDBACK_LABEL}" contentFolderID="${contentFolderID}" height="50px"/>
	</div>
</div>
