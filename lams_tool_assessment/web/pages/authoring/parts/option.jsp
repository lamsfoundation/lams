<input type="hidden" name="optionSequenceId${status.index}" id="optionSequenceId${status.index}" value="${option.sequenceId}">

<div class="form-group">			
	<fmt:message key="label.authoring.basic.option.answer"/>&thinsp; ${status.index+1}
</div>

<div class="form-group">
	<lams:CKEditor id="optionString${status.index}" value="${option.optionString}" contentFolderID="${contentFolderID}" />
</div>

<div class="form-group">
	<label>
		<fmt:message key="label.authoring.basic.option.grade"></fmt:message>
	</label>
	<%@ include file="gradeselector.jsp"%>
</div>

<div class="form-group">
    <label for="optionFeedback${status.index}">
    	<fmt:message key="label.authoring.basic.option.feedback"></fmt:message>
    </label>
     <lams:CKEditor id="optionFeedback${status.index}" value="${option.feedback}" contentFolderID="${contentFolderID}" />
</div>
