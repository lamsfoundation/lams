<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="java.util.LinkedHashSet" %>
<%@ page import="java.util.Set" %>
<%@ page import="org.lamsfoundation.lams.tool.vote.VoteAppConstants"%>

<c:set var="title"><fmt:message key="label.monitoring" /></c:set>
<lams:PageMonitor title="${title}" 
	helpToolSignature="<%= VoteAppConstants.MY_SIGNATURE %>"
	initialTabId="${voteGeneralMonitoringDTO.currentTab}"
	tab1Label="label.summary"
	tab2Label="label.editActivity"
	tab3Label="label.stats"
	tab1Jsp="SummaryContent.jsp"
	tab2Jsp="Edit.jsp"
	tab3Jsp="Stats.jsp"/>
