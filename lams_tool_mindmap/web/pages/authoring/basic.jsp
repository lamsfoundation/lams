<%@ include file="/common/taglibs.jsp"%>

<c:set var="sessionMap" value="${sessionScope[authoringForm.sessionMapID]}" />
<c:set var="sessionMapID" value="${authoringForm.sessionMapID}" />

<!-- ========== Basic Tab ========== -->
<div class="form-group">
    <label for="title">
    	<fmt:message key="label.authoring.basic.title"/>
    </label>
    <input type="text" name="title" value="${authoringForm.title}" class="form-control"/>
</div>

<div class="form-group">
    <label for="instructions">
    	<fmt:message key="label.authoring.basic.instructions"/>
    </label>
	<lams:CKEditor id="instructions" value="${authoringForm.instructions}"
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
