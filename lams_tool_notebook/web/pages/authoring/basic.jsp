<%@ include file="/common/taglibs.jsp"%>

<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
<c:set var="sessionMap" value="${sessionScope[formBean.sessionMapID]}" />

<!-- ========== Basic Tab ========== -->
<div class="form-group">
    <label for="title"><fmt:message key="label.authoring.basic.title"/></label>
    <html:text property="title" styleClass="form-control"></html:text>
</div>
<div class="form-group">
    <label for="instructions"><fmt:message key="label.authoring.basic.instructions" /></label>
    <lams:CKEditor id="instructions" value="${formBean.instructions}" contentFolderID="${sessionMap.contentFolderID}"></lams:CKEditor>
</div>
<div class="checkbox">
	<label for="forceResponse">
		<html:checkbox property="forceResponse" styleId="forceResponse" value="1" />
		<fmt:message key="basic.forceResponse" />
	</label>
</div>
