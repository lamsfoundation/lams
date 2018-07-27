<%@ include file="/common/taglibs.jsp"%>

<c:set var="sessionMap" value="${sessionScope[authoringForm.sessionMapID]}" />

<!-- ========== Basic Tab ========== -->
<div class="form-group">
    <label for="title"><fmt:message key="label.authoring.basic.title"/></label>
    <form:input path="title" cssClass="form-control"/>
</div>
<div class="form-group">
    <label for="instructions"><fmt:message key="label.authoring.basic.instructions" /></label>
    <lams:CKEditor id="instructions" value="${authoringForm.instructions}" contentFolderID="${sessionMap.contentFolderID}"></lams:CKEditor>
</div>
<div class="checkbox">
	<label for="forceResponse">
		<form:checkbox path="forceResponse" id="forceResponse" value="1" />
		<fmt:message key="basic.forceResponse" />
	</label>
</div>
