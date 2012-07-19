<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/common/taglibs.jsp"%>

<c:set var="sessionMapID" value="${param.sessionMapID}"/>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="mode" value="${sessionMap.mode}"/>

<c:choose>
	<c:when test="${mode == 'learner'}">
		<%@ include file="parts/exportLearner.jsp"%>
	</c:when>
										
	<c:when test="${mode == 'teacher'}">
		<%@ include file="parts/exportTeacher.jsp"%>
	</c:when>
</c:choose>
