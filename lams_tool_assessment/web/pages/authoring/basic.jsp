<%@ include file="/common/taglibs.jsp"%>

<div class="form-group">
    <label for="assessment.title">
    	<fmt:message key="label.authoring.basic.title"/>
    </label>
    <form:input path="assessment.title" cssClass="form-control" maxlength="255"/>
</div>

<div class="form-group">
    <label for="assessment.instructions">
    	<fmt:message key="label.authoring.basic.instruction"/>
    </label>
	<lams:CKEditor id="assessment.instructions"
				   value="${assessmentForm.assessment.instructions}"
				   contentFolderID="${assessmentForm.contentFolderID}"
				   classes="form-control">
	</lams:CKEditor>
</div>
