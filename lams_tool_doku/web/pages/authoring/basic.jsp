<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

<!-- Basic Tab Content -->
<div class="form-group">
    <label for="forum.title">
    	<fmt:message key="label.authoring.basic.title"/>
    </label>
    <html:text property="dokumaran.title" styleClass="form-control"/>
</div>

<div class="form-group">
    <label for="dokumaran.instructions">
    	<fmt:message key="label.authoring.basic.instruction" />
    </label>
    <lams:CKEditor id="dokumaran.instructions" value="${formBean.dokumaran.instructions}" contentFolderID="${formBean.contentFolderID}" height="400"/>
</div>
