<input type="hidden" name="optionSequenceId${status.index}" value="${option.sequenceId}">
<input type="hidden" name="optionUid${status.index}" value="${option.uid}">

<div class="form-group">
	<label for="optionQuestion${status.index}">
		<fmt:message key="label.authoring.basic.option.question"/>&nbsp;${status.index+1}
	</label>
		            	
	<lams:CKEditor id="optionQuestion${status.index}" value="${option.question}" contentFolderID="${contentFolderID}"/>
</div>

<div class="form-group">
    <label for="optionFeedback${status.index}">
    	<fmt:message key="label.authoring.basic.option.answer"></fmt:message>
    </label>
    <input type="text" name="optionString${status.index}" value="${option.optionString}" id="optionString${status.index}" class="form-control">
</div>
