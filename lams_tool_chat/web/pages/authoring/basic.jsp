<%@ include file="/common/taglibs.jsp"%>

<c:set var="sessionMap" value="${sessionScope[authoringForm.sessionMapID]}" />
<c:set var="sessionMapID" value="${authoringForm.sessionMapID}" />

<!-- ========== Basic Tab ========== -->
<div class="form-group">
    <label for="title"><fmt:message key="label.authoring.basic.title"/></label>
    <input type="text" name="title" value="${authoringForm.title}" class="form-control">
</div>
<div class="form-group">
    <label for="instructions"><fmt:message key="label.authoring.basic.instructions" /></label>
    <lams:CKEditor id="instructions" value="${authoringForm.instructions}" contentFolderID="${sessionMap.contentFolderID}"></lams:CKEditor>
</div>
