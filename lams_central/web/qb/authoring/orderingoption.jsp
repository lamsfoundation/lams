<%@ include file="/common/taglibs.jsp"%>

<input type="hidden" name="optionDisplayOrder${status.index}" value="${option.displayOrder}">
<input type="hidden" name="optionUid${status.index}" value="${option.uid}">

<div class="form-group option-ckeditor">
	<c:set var="OPTION_ANSWER_LABEL"><fmt:message key="label.authoring.basic.option.answer"/></c:set>      	
	<lams:CKEditor id="optionName${status.index}" value="${option.name}" contentFolderID="${contentFolderID}"
		placeholder="${OPTION_ANSWER_LABEL}&thinsp; ${status.index+1}" height="50px"/>
</div>
