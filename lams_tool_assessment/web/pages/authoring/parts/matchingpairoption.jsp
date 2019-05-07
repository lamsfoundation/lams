<input type="hidden" name="optionSequenceId${status.index}" value="${option.sequenceId}">
<input type="hidden" name="optionUid${status.index}" value="${option.uid}">

<div class="voffset5-bottom option-ckeditor">
	<c:set var="QUESTION_LABEL"><fmt:message key="label.authoring.basic.option.question"/></c:set>      	
	<lams:CKEditor id="optionQuestion${status.index}" value="${option.question}" contentFolderID="${contentFolderID}"
		placeholder="${QUESTION_LABEL}&thinsp; ${status.index+1}" height="40px"/>
</div>

<div class="form-group">
    <c:set var="OPTION_LABEL"><fmt:message key="label.authoring.basic.option.answer"/></c:set>
    <input type="text" name="optionString${status.index}" id="optionString${status.index}" class="borderless-text-input"  
    	value="${option.optionString}" placeholder="${OPTION_LABEL}&thinsp; ${status.index+1}">
</div>
