<%@ include file="/common/taglibs.jsp"%>

<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
<c:set var="sessionMap" value="${sessionScope[formBean.sessionMapID]}" />
<c:set var="sessionMapID" value="${formBean.sessionMapID}" />

<!-- ========== Basic Tab ========== -->
<div class="form-group">
    <label for="title">
    	<fmt:message key="label.authoring.basic.title"/>
    </label>
    <html:text property="title" styleClass="form-control"/>
</div>

<div class="form-group">
    <label for="instructions">
    	<fmt:message key="label.authoring.basic.instructions"/>
    </label>
	<lams:CKEditor id="instructions" value="${formBean.instructions}"
			contentFolderID="${sessionMap.contentFolderID}">
	</lams:CKEditor>
</div>

<%-- Page is reloaded after save and then disappears. Do not want to try to render the mindmap. --%>
<c:if test="${not empty mindmapId}">
	<c:set var="multiMode">false</c:set>
	<c:set var="contentEditable">true</c:set>
	<c:set var="url">author</c:set>			
	<c:set var="mindmapId">${mindmapId}</c:set>
	<%@ include file="/common/mapjs.jsp"%>
</c:if>
