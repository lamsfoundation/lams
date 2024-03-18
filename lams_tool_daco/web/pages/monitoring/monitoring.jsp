<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.daco.DacoConstants"%>

<c:set var="title"><fmt:message key="label.common.heading" /></c:set>
<lams:PageMonitor title="${title}" 
	helpToolSignature="<%= DacoConstants.TOOL_SIGNATURE %>"
	initialTabId="${vmonitoringCurrentTab}"
	tab1Label="label.common.summary"
	tab2Label="tab.monitoring.edit.activity"
	tab3Label="tab.monitoring.statistics"
	tab1Jsp="summary.jsp"
	tab2Jsp="editactivity.jsp"
	tab3Jsp="statistics.jsp"/>

