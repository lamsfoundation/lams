<!DOCTYPE html>
<%@page import="org.lamsfoundation.lams.tool.mindmap.MindmapConstants"%>
<%@ include file="/common/taglibs.jsp"%>

<c:set var="title"><fmt:message key="pageTitle.monitoring" /></c:set>
<lams:PageMonitor title="${title}" 
	helpToolSignature="<%= MindmapConstants.TOOL_SIGNATURE %>"
	initialTabId="${mindmapDTO.currentTab}"
	tab1Label="button.summary"
	tab2Label="button.editActivity"
	tab3Label="button.statistics"
	tab1Jsp="summary.jsp"
	tab2Jsp="editActivity.jsp"
	tab3Jsp="statistics.jsp"/>
