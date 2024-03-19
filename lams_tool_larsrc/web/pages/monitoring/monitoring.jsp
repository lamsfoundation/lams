<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.rsrc.ResourceConstants"%>

<c:set var="title"><fmt:message key="activity.title" /></c:set>
<lams:PageMonitor title="${title}" 
	helpToolSignature="<%= ResourceConstants.TOOL_SIGNATURE %>"
	initialTabId="${initialTabId}" />
	 
