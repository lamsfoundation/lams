<input type="hidden" name="optionSequenceId${status.index}" value="${option.sequenceId}">
<input type="hidden" name="optionUid${status.index}" value="${option.uid}">

<div class="form-group">
	<c:set var="OPTION_LABEL"><fmt:message key="label.authoring.basic.option.answer"/></c:set>
	<input type="text" name="optionString${status.index}" class="borderless-text-input"
		value="<c:out value='${option.optionString}' />" placeholder="${OPTION_LABEL}&thinsp; ${status.index+1}"/>
</div>

<div class="option-settings-hidden" style="display: none;">
	<%@ include file="gradeselector.jsp"%>
	
	<div class="voffset5-bottom">
	   	<c:set var="FEEDBACK_LABEL"><fmt:message key="label.authoring.basic.option.feedback"/></c:set>
	   	<lams:CKEditor id="optionFeedback${status.index}" value="${option.feedback}" 
	     	placeholder="${FEEDBACK_LABEL}" contentFolderID="${contentFolderID}" height="50px"/>
	</div>
</div>

