<%@ include file="/common/taglibs.jsp"%>

<!-- Basic Tab Content -->
<div class="form-group">
    <label for="forum.title">
    	<fmt:message key="label.authoring.basic.title"/>
    </label>
    <form:input path="dokumaran.title" cssClass="form-control"/>
</div>

<div class="form-group">
    <label for="dokumaran.description"><fmt:message key="label.authoring.basic.description" />&nbsp;
        <i class="fa fa-question-circle" aria-hidden="true" data-toggle="tooltip" data-placement="right" title="" data-original-title="<fmt:message key="label.authoring.basic.description.tooltip"/>"></i>
    </label>
    <lams:CKEditor id="dokumaran.description" value="${authoringForm.dokumaran.description}" height="220" contentFolderID="${authoringForm.contentFolderID}"/>
</div>


<div class="form-group">
    <label for="dokumaran.instructions">
    	<fmt:message key="label.authoring.basic.instruction" />&nbsp;
        <i class="fa fa-question-circle" aria-hidden="true" data-toggle="tooltip" data-placement="right" title="" data-original-title="<fmt:message key="label.authoring.basic.instructions.tooltip"/>"></i>        
    </label>
    <lams:CKEditor id="dokumaran.instructions" value="${authoringForm.dokumaran.instructions}" contentFolderID="${authoringForm.contentFolderID}" height="220" toolbarSet="DefaultDoku" method="inline"/>
</div>
