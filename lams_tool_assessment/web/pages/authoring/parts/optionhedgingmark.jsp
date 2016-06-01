<input type="hidden" name="optionSequenceId${status.index}" id="optionSequenceId${status.index}" value="${option.sequenceId}">

<div class="form-group">
	<label for="optionString${status.index}">
		<fmt:message key="label.authoring.basic.option.answer"/> ${status.index+1}
	</label>
	<lams:CKEditor id="optionString${status.index}" value="${option.optionString}" contentFolderID="${contentFolderID}" />
</div>

<div class="radio">
	<label for="option-correct">
		<input type="radio" alt="${status.index}" name="optionCorrect" value="${option.sequenceId}" id="option-correct"
				<c:if test="${option.correct}">checked="checked"</c:if> >
					
		<fmt:message key="label.option.correct" />
	</label>
</div>

<div class="form-group form-inline">
	<label for="optionGrade${status.index}">
		<fmt:message key="label.authoring.basic.option.grade"></fmt:message>
	</label>
	<%@ include file="gradeselector.jsp"%>
</div>

<div class="form-group">
    <label for="optionFeedback${status.index}">
    	<fmt:message key="label.authoring.basic.option.feedback"/>
    </label>
    
    <lams:STRUTS-textarea property="optionFeedback${status.index}" rows="3" value="${option.feedback}" styleClass="form-control"/>
</div>

