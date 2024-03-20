<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<c:set var="title"><fmt:message key="activity.title" /></c:set>
<lams:PageMonitor title="${title}" hideHeader="true">
	<link rel="stylesheet" type="text/css" href="${lams}css/jquery.minicolors.css"></link>
	<link rel="stylesheet" type="text/css" href="${tool}includes/css/mapjs.css"></link>
	<link rel="stylesheet" type="text/css" href="${tool}includes/css/mindmap.css"></link>
		
	<script src="${lams}includes/javascript/jquery.minicolors.min.js"></script>
	<script src="${tool}includes/javascript/jquery.timer.js"></script>
	<script src="${lams}includes/javascript/fullscreen.js"></script>
	<script src="${tool}includes/javascript/mapjs/main.js"></script>
	<script src="${tool}includes/javascript/mapjs/underscore-min.js"></script>

	<div class="main-container">
		<h1 class="fs-3 mb-3">
			<c:choose>
				<c:when test="${isMultiUserMode}">
					<fmt:message key="label.multimode" />
				</c:when>
				
				<c:otherwise>
					<c:out value="${userDTO.firstName} ${userDTO.lastName}" escapeXml="true"/>
				</c:otherwise>
			</c:choose>
		</h1>
				
		<%--  Monitor cannot edit mindmaps. mapjs.jsp also uses sessionId and mindmapId --%>
		<c:set var="multiMode">${isMultiUserMode}</c:set>
		<c:set var="contentEditable">false</c:set>				
		<%@ include file="/common/mapjs.jsp"%>
				
		<div class="activity-bottom-buttons">
			<button class="btn btn-primary btn-icon-return" name="backButton" onclick="history.go(-1)" type="button">
				<fmt:message>button.back</fmt:message>
			</button>
		</div>
	</div>
</lams:PageMonitor>
