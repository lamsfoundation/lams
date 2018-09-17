<%@ include file="/common/taglibs.jsp"%>

<c:set var="sessionMapID" value="${authoringForm.sessionMapID}" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />


<!-- ========== Basic Tab ========== -->
<div class="form-group">
	<label for="title"><fmt:message key="label.authoring.basic.title" /></label>
	<form:input path="title" style="width: 99%;" />
</div>

<div class="form-group">
		<label for="instructions"><fmt:message key="label.authoring.basic.instructions"/></label>
		<lams:CKEditor id="instructions" value="${authoringForm.instructions}" contentFolderID="${sessionMap.contentFolderID}"></lams:CKEditor>
</div>
