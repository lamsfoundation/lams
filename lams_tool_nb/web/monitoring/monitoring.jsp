<!DOCTYPE html>
<%@ include file="/includes/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants"%>

<c:set var="title"><fmt:message key="activity.title" /></c:set>
<lams:PageMonitor title="${title}" 
	helpToolSignature="<%= NoticeboardConstants.TOOL_SIGNATURE %>"
	initialTabId="${monitoringDTO.currentTab}"
	tab1Label="titleHeading.summary"
	tab2Label="titleHeading.editActivity"
	tab3Label="titleHeading.statistics"
	tab1Jsp="m_Summary.jsp"
	tab2Jsp="m_EditActivity.jsp"
	tab3Jsp="m_Statistics.jsp">
</lams:PageMonitor>