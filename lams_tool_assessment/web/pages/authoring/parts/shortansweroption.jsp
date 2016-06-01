<input type="hidden" name="optionSequenceId${status.index}" id="optionSequenceId${status.index}" value="${option.sequenceId}">

<div class="form-group">
	<label for="optionString${status.index}">
		<fmt:message key="label.authoring.basic.option.answer"/> ${status.index+1}
	</label>
	<input type="text" name="optionString${status.index}" value="<c:out value='${option.optionString}' />" class="form-control input-sm"/>
</div>

<div class="form-group">
	<label>
		<fmt:message key="label.authoring.basic.option.grade"></fmt:message>
	</label>
	<%@ include file="gradeselector.jsp"%>
</div>

<div class="form-group">
    <label for="optionFeedback${status.index}">
    	<fmt:message key="label.authoring.basic.option.feedback"/>
    </label>
    
    <lams:CKEditor id="optionFeedback${status.index}" value="${option.feedback}" contentFolderID="${contentFolderID}"/>
</div>
