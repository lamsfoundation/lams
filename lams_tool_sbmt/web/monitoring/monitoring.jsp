<!DOCTYPE html>
<%@include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.sbmt.SbmtConstants"%>
<c:set var="tool"><lams:WebAppURL /></c:set>
<c:set var="lams"><lams:LAMSURL /></c:set>

<c:set var="title"><fmt:message key="activity.title" /></c:set>
<lams:PageMonitor title="${title}" 
	helpToolSignature="<%= SbmtConstants.TOOL_SIGNATURE %>"
	initialTabId="${monitoringDTO.currentTab}"
	tab1Label="label.monitoring.heading.userlist"
	tab2Label="label.monitoring.heading.edit.activity"
	tab3Label="label.monitoring.heading.stats"
	tab1Jsp="parts/summary.jsp"
	tab2Jsp="parts/activity.jsp"
	tab3Jsp="parts/statisticpart.jsp"/>
