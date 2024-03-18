<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<c:set var="title"><fmt:message key="label.common.summary" /></c:set>
<lams:PageMonitor title="${title}" hideHeader="true">
	<link href="<c:url value='/includes/css/daco.css'/>" rel="stylesheet" type="text/css">
	
	<lams:JSImport src="includes/javascript/dacoCommon.js" relative="true" />
	
	<div class="container-main">
		<h1 class="mb-4">
			${title}
		</h1>
		
		<c:if test="${not empty param.includeMode}">
			<c:set var="includeMode" value="${param.includeMode}" />
		</c:if>
		<c:if test="${empty includeMode}">
			<c:set var="includeMode" value="monitoring" />
		</c:if>
		<%@ include file="/pages/learning/questionSummaries.jsp" %>
		
		<div class="activity-bottom-buttons">
			<button type="button" onclick="window.close()" class="btn btn-primary">
				<i class="fa-regular fa-circle-xmark me-1"></i>
				<fmt:message key="label.close" />
			</button>
		</div>
	</div>
</lams:PageMonitor>
