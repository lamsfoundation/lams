<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.peerreview.PeerreviewConstants"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>

<c:set var="title"><fmt:message key="activity.title" /></c:set>
<lams:PageMonitor title="${title}" 
		helpToolSignature="<%= PeerreviewConstants.TOOL_SIGNATURE %>"
		initialTabId="${initialTabId}"/>

