<input type="hidden" name="optionSequenceId${status.index}" value="${option.sequenceId}">
<input type="hidden" name="optionUid${status.index}" value="${option.uid}">

<div class="form-group option-ckeditor">
	<c:set var="OPTION_ANSWER_LABEL"><fmt:message key="label.authoring.basic.option.answer"/></c:set>      	
	<lams:CKEditor id="optionString${status.index}" value="${option.optionString}" contentFolderID="${contentFolderID}"
		placeholder="${OPTION_ANSWER_LABEL}&thinsp; ${status.index+1}" height="50px"/>
</div>
