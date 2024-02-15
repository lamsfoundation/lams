<!DOCTYPE html>
<%@ page import="org.lamsfoundation.lams.tool.leaderselection.util.LeaderselectionConstants"%>
<%@ include file="/common/taglibs.jsp"%>

<c:set var="title"><fmt:message key="activity.title" /></c:set>
<lams:PageMonitor title="${title}" 
		helpToolSignature="<%= LeaderselectionConstants.TOOL_SIGNATURE %>"
		initialTabId="${leaderselectionDTO.currentTab}"
		tab1Label="button.summary"
		tab2Label="button.editActivity"
		tab3Label="button.statistics"
		tab1Jsp="summary.jsp"
		tab2Jsp="editActivity.jsp"
		tab3Jsp="statistics.jsp"/>
