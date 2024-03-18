<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%-- This page/learning/listRecords.jsp page modifies its content depending on the page it was included from. --%>
<c:if test="${not empty param.includeMode}">
	<c:set var="includeMode" value="${param.includeMode}" />
</c:if>
<c:if test="${empty includeMode}">
	<c:set var="includeMode" value="monitoring" />
</c:if>
<c:if test="${not empty param.sessionMapID}">
	<c:set var="sessionMapID" value="${param.sessionMapID}" />
</c:if>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="userGroup" value="${sessionMap.monitoringSummary}" />

<c:set var="title"><fmt:message key="title.monitoring.recordlist" /></c:set>
<lams:PageMonitor title="${title}" hideHeader="true">
	<link href="<c:url value='/includes/css/daco.css'/>" rel="stylesheet" type="text/css">
			
	<lams:JSImport src="includes/javascript/dacoCommon.js" relative="true" />
	<script type="text/javascript">
		function checkCheckbox(checkboxName){
			var checkbox = document.getElementById(checkboxName);
			checkbox.checked=true;
		}	
	</script> 

	<div class="container-main">
		<h1 class="mb-4">
			${title}
		</h1>
	
		<c:if test="${sessionMap.isGroupedActivity}">
			<h2>
				<fmt:message key="label.monitoring.group" />: ${userGroup.sessionName}
			</h2>
		</c:if>
		
	 	<c:forEach var="user" items="${userGroup.users}">
	 		<h3 class="card-subheader fs-4">
	 			<fmt:message key="label.export.file.user" />:&nbsp;<c:out value="${user.fullName}" escapeXml="true"/>
	 		</h3>
			
	 		<c:if test="${not empty user.records}">
				<c:set var="recordList" value="${user.records}" />
	 			<%@ include file="/pages/learning/listRecords.jsp" %>
			</c:if>
		</c:forEach> 

		<div class="activity-bottom-buttons">
			<button type="button" onclick="window.close()" class="btn btn-primary">
				<i class="fa-regular fa-circle-xmark me-1"></i>
				<fmt:message key="label.close" />
			</button>

			<%-- Switch between the horizontal and vertical views --%>
			<c:url var="changeViewUrl" value='/monitoring/changeView.do'>
				<c:param name="sessionMapID" value="${sessionMapID}" />
				<c:param name="toolSessionID" value="${toolSessionID}" />
				<c:param name="userId" value="${userId}" />
				<c:param name="sort" value="${sort}" />
			</c:url>
		
			<button type="button" class="btn btn-secondary btn-sm me-2" onclick="document.location.href='${changeViewUrl}'">
				<i class="fa-solid ${sessionMap.learningView=='horizontal' ? 'fa-ellipsis' : 'fa-ellipsis-vertical'} me-2" 
						title="<fmt:message key="label.common.view.change" />" id="ellipsis"></i>
				<fmt:message key="label.common.view.change" />
			</button>
		</div>
	</div>
</lams:PageMonitor>
