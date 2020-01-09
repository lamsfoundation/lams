<%@ include file="/common/taglibs.jsp"%>

<input type="hidden" name="optionDisplayOrder${status.index}" value="${option.displayOrder}">
<input type="hidden" name="optionUid${status.index}" value="${option.uid}">

<div class="form-group">
	<c:set var="OPTION_LABEL">
		<fmt:message key="label.authoring.basic.option.answer"/>
	</c:set>
	<textarea name="optionName${status.index}" rows="3" class="form-control borderless-text-input"
		placeholder="${OPTION_LABEL}&thinsp; ${status.index+1}"
		oninput="autoGrowTextarea(this)"
		><c:out value='${option.name}' /></textarea>
</div>

<div class="option-settings-hidden" style="display: none;">
	<%@ include file="gradeselector.jsp"%>
	
	<div class="voffset5-bottom">
	   	<c:set var="FEEDBACK_LABEL"><fmt:message key="label.authoring.basic.option.feedback"/></c:set>
	   	<lams:CKEditor id="optionFeedback${status.index}" value="${option.feedback}" 
	     	placeholder="${FEEDBACK_LABEL}" contentFolderID="${contentFolderID}" height="50px"/>
	</div>
</div>

