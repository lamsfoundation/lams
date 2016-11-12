<input type="hidden" name="optionSequenceId${status.index}" id="optionSequenceId${status.index}" value="${option.sequenceId}">

<div class="form-group">
	<label for="optionString${status.index}">
		<fmt:message key="label.authoring.basic.option.answer"/>&nbsp;${status.index+1}
	</label>
		            	
	<lams:CKEditor id="optionString${status.index}" value="${option.optionString}" contentFolderID="${contentFolderID}"/>
</div>
