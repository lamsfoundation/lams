<%@ include file="/common/taglibs.jsp"%>

<input type="hidden" name="optionDisplayOrder${status.index}" value="${option.displayOrder}">
<input type="hidden" name="optionUid${status.index}" value="${option.uid}">

<div class="option-ckeditor">
	<c:set var="OPTION_LABEL"><fmt:message key="label.authoring.basic.option.answer"/></c:set>
	<lams:CKEditor id="optionName${status.index}" value="${option.name}" 
		placeholder="${OPTION_LABEL}&thinsp; ${status.index+1}" contentFolderID="${contentFolderID}" height="50px"/>
</div>

<div class="option-settings-hidden" style="display: none;">
	<%@ include file="gradeselector.jsp"%>

	<div class="option-feedback voffset5-bottom">
    	<c:set var="FEEDBACK_LABEL"><fmt:message key="label.authoring.basic.option.feedback"/></c:set>
     	<lams:CKEditor id="optionFeedback${status.index}" value="${option.feedback}" 
	     	placeholder="${FEEDBACK_LABEL}" contentFolderID="${contentFolderID}" height="50px"/>
	</div>
</div>