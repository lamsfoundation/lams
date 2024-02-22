<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.scratchie.ScratchieConstants"%>

<c:set var="title"><fmt:message key="activity.title" /></c:set>
<lams:PageMonitor title="${title}" 
		helpToolSignature="<%= ScratchieConstants.TOOL_SIGNATURE %>"
		initialTabId="${initialTabId}"/>
