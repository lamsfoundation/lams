<%@ include file="/common/taglibs.jsp"%>

<input type="hidden" name="optionDisplayOrder${status.index}" value="${option.displayOrder}">
<input type="hidden" name="optionUid${status.index}" value="${option.uid}">

<div class="voffset5-bottom option-ckeditor">
	<c:set var="QUESTION_LABEL"><fmt:message key="label.authoring.basic.option.question"/></c:set>      	
	<lams:CKEditor id="matchingPair${status.index}" value="${option.matchingPair}" contentFolderID="${contentFolderID}"
		placeholder="${QUESTION_LABEL}&thinsp; ${status.index+1}" height="40px"/>
</div>

<div class="form-group">
    <c:set var="OPTION_LABEL"><fmt:message key="label.authoring.basic.option.answer"/></c:set>
    <input type="text" name="optionName${status.index}" id="optionName${status.index}" class="borderless-text-input"  
    	value="${option.name}" placeholder="${OPTION_LABEL}&thinsp; ${status.index+1}">
</div>
