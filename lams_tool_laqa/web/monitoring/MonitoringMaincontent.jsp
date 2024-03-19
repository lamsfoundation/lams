<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.qa.QaAppConstants"%>

<c:set var="title"><fmt:message key="activity.title" /></c:set>
<lams:PageMonitor title="${title}" 
	helpToolSignature="<%= QaAppConstants.MY_SIGNATURE %>"
	initialTabId=""
	tab1Label="label.summary"
	tab2Label="label.editActivity"
	tab3Label="label.stats"
	tab1Jsp="SummaryContent.jsp"
	tab2Jsp="Edit.jsp"
	tab3Jsp="Stats.jsp"/>
