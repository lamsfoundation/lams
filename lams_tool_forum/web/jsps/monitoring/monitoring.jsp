<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.forum.ForumConstants"%>

<c:set var="title"><fmt:message key="activity.title" /></c:set>
<lams:PageMonitor title="${title}" 
		helpToolSignature="<%= ForumConstants.TOOL_SIGNATURE %>"
		initialTabId="${initialTabId}">
</lams:PageMonitor>

