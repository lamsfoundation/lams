<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.daco.DacoConstants"%>
<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	
	<%@ include file="/common/tabbedheader.jsp"%>
	<c:if test="${not empty param.sessionMapID}">
		<c:set var="sessionMapID" value="${param.sessionMapID}" />
	</c:if>
	
	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
	<c:set var="mode" value="${sessionMap.mode}" />
	<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />
	<c:set var="daco" value="${sessionMap.daco}" />
	<c:set var="finishedLock" value="${sessionMap.finishedLock}" />
	<c:if test="${empty displayedRecordNumber}">
		<c:set var="displayedRecordNumber" value="1" />
	</c:if>
	
	<c:choose>
		<c:when test="${not empty sessionMap.learningCurrentTab}">
			<c:set var="learningCurrentTab" value="${sessionMap.learningCurrentTab}" />
		</c:when>
		<c:otherwise>
			<c:set var="learningCurrentTab" value="1" />
		</c:otherwise>
	</c:choose>
	
	<script type="text/javascript">
	 	var currentTab = ${learningCurrentTab};
	 	var changeViewUrl = "<html:rewrite page='/learning/changeView.do' />";
	 	var finishUrl = '<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}"/>';
	 	var continueReflectUrl = '<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
	 	var refreshQuestionSummariesUrl = '<c:url value="/learning/refreshQuestionSummaries.do"/>';
	 	var questionListLength	=  ${fn:length(daco.dacoQuestions)};
    </script>
    <script type="text/javascript" src="<html:rewrite page='/includes/javascript/dacoLearning.js'/>"></script>
</lams:head>

<body class="stripes" onLoad="init()" id="body">
<div id="page">

<h1><fmt:message key="label.learning.title" /></h1>
<div id="header">
<lams:Tabs useKey="true" control="true">
	<lams:Tab id="1" key="label.learning.heading.add" />
	<lams:Tab id="2" key="label.learning.heading.list" />
	<lams:Tab id="3" key="label.learning.heading.summary" />
</lams:Tabs></div>

<div id="content">
<div style="float: right; margin-left: 10px; padding-top: 4px" class="help">
	<img src="${tool}includes/images/uparrow.gif" title="<fmt:message key="label.learning.view.change" />"
	 onclick="javascript:changeView('${sessionMapID}',${displayedRecordNumber})" />
</div>
<lams:help toolSignature="<%= DacoConstants.TOOL_SIGNATURE %>" module="learning" />



<c:if test="${sessionMap.lockOnFinish and mode != 'teacher'}">
	<div class="info"><c:choose>
		<c:when test="${sessionMap.userFinished}">
			<fmt:message key="message.activityLocked" />
		</c:when>
		<c:otherwise>
			<fmt:message key="message.warnLockOnFinish" />
		</c:otherwise>
	</c:choose></div>
</c:if>

<lams:TabBody id="1" titleKey="label.learning.heading.add" page="addRecord.jsp?displayedRecordNumber=${displayedRecordNumber}" />
<lams:TabBody id="2" titleKey="label.learning.heading.list" page="listRecords.jsp" />
<lams:TabBody id="3" titleKey="label.learning.heading.summary" page="questionSummaries.jsp" />
		
<c:if test="${sessionMap.userFinished and sessionMap.reflectOn}">
	<div class="small-space-top">
	<h2>${sessionMap.reflectInstructions}</h2>

	<c:choose>
		<c:when test="${empty sessionMap.reflectEntry}">
			<p><em> <fmt:message key="message.no.reflection.available" /> </em></p>
		</c:when>
		<c:otherwise>
			<p><lams:out escapeHtml="true" value="${sessionMap.reflectEntry}" /></p>
		</c:otherwise>
	</c:choose>
	
	<c:if test="${mode != 'teacher'}">
		<html:button property="FinishButton" onclick="return continueReflect()" styleClass="button">
			<fmt:message key="label.edit" />
		</html:button>
	</c:if></div>
</c:if>

<c:if test="${mode != 'teacher'}">
	<div class="space-bottom-top align-right space-right"><c:choose>
		<c:when test="${sessionMap.reflectOn && (not sessionMap.userFinished)}">
			<html:button property="FinishButton" onclick="return continueReflect()" styleClass="button">
				<fmt:message key="label.continue" />
			</html:button>
		</c:when>
		<c:otherwise>
			<html:button property="FinishButton" styleId="finishButton" onclick="return finishSession()" styleClass="button">
				<fmt:message key="label.finished" />
			</html:button>
		</c:otherwise>
	</c:choose></div>
</c:if>

</div>

<div id="footer"></div>
<!-- end page div --></div>
</body>
</lams:html>