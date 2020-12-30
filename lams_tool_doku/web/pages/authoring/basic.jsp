<%@ include file="/common/taglibs.jsp"%>

<!-- Basic Tab Content -->
<div class="form-group">
    <label for="forum.title">
    	<fmt:message key="label.authoring.basic.title"/>
    </label>
    <form:input path="dokumaran.title" cssClass="form-control"/>
</div>

<div class="form-group">
    <label for="dokumaran.description"><fmt:message key="label.authoring.basic.description" /></label>
    <lams:CKEditor id="dokumaran.description" value="${authoringForm.dokumaran.description}" contentFolderID="${authoringForm.contentFolderID}"/>
</div>


<div class="form-group">
    <label for="dokumaran.instructions">
    	<fmt:message key="label.authoring.basic.instruction" />
    </label>
    <lams:CKEditor id="dokumaran.instructions" value="${authoringForm.dokumaran.instructions}" contentFolderID="${authoringForm.contentFolderID}" height="400" toolbarSet="DefaultDoku" method="inline"/>
</div>
