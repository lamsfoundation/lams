<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.assessment.AssessmentConstants"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="sessionDtos" value="${sessionMap.sessionDtos}"/>
<c:set var="assessment" value="${sessionMap.assessment}"/>

<c:set var="title"><fmt:message key="label.author.title" /></c:set>
<lams:PageMonitor title="${title}" 
		helpToolSignature="<%= AssessmentConstants.TOOL_SIGNATURE %>"
		initialTabId="${initialTabId}"/>