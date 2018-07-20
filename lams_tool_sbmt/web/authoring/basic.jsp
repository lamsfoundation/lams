<%@ include file="/common/taglibs.jsp"%>

<c:set var="sessionMap" value="${sessionScope[authoringForm.sessionMapID]}" />

<!-- ========== Basic Tab ========== -->
<div class="form-group">
    <label for="title"><fmt:message key="label.authoring.basic.title"/></label>
    <form:input type="text" path="title" cssClass="form-control"></form:input>
</div>
<div class="form-group">
    <label for="instructions"><fmt:message key="label.authoring.basic.instruction" /></label>
    <lams:CKEditor id="instructions" value="${authoringForm.instructions}" contentFolderID="${sessionMap.contentFolderID}"></lams:CKEditor>
</div>