<%@ include file="/common/taglibs.jsp"%>

<!-- Basic Tab Content -->
<div class="form-group">
    <label for="forum.title">
    	<fmt:message key="label.authoring.basic.title"/>
    </label>
    <form:input path="whiteboard.title" cssClass="form-control"/>
</div>

<div class="form-group">
    <label for="whiteboard.instructions">
    	<fmt:message key="label.authoring.basic.instruction" />&nbsp;
        <i class="fa fa-question-circle" aria-hidden="true" data-toggle="tooltip" data-placement="right" title="" data-original-title="<fmt:message key="label.authoring.basic.instructions.tooltip"/>"></i>        
    </label>
    <lams:CKEditor id="whiteboard.instructions" value="${authoringForm.whiteboard.instructions}" contentFolderID="${authoringForm.contentFolderID}" />
</div>
