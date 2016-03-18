<!DOCTYPE html>
        

<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.daco.DacoConstants"%>
<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>
	
	<c:if test="${not empty param.sessionMapID}">
		<c:set var="sessionMapID" value="${param.sessionMapID}" />
	</c:if>
	
	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
	<c:set var="mode" value="${sessionMap.mode}" />
	<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />
	<c:set var="daco" value="${sessionMap.daco}" />
	
	<c:if test="${empty displayedRecordNumber}">
		<c:set var="displayedRecordNumber" value="1" />
	</c:if>
	
	<c:choose>
		<%-- To return to the previous tab.  --%>
		<c:when test="${not empty sessionMap.learningCurrentTab}">
			<c:set var="learningCurrentTab" value="${sessionMap.learningCurrentTab}" />
		</c:when>
		<c:otherwise>
			<c:set var="learningCurrentTab" value="1" />
		</c:otherwise>
	</c:choose>
	
	<script type="text/javascript">
	 	var currentTab = ${learningCurrentTab};
	 	var changeViewUrl = "<c:url value='/learning/changeView.do' />";
	 	var finishUrl = '<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}&displayedRecordNumber=${displayedRecordNumber}"/>';
	 	var continueReflectUrl = '<c:url value="/learning/startReflection.do?sessionMapID=${sessionMapID}&displayedRecordNumber=${displayedRecordNumber}"/>';
	 	var refreshQuestionSummariesUrl = '<c:url value="/learning/refreshQuestionSummaries.do"/>';
    </script>
    <script type="text/javascript" src="<html:rewrite page='/includes/javascript/dacoLearning.js'/>"></script>
</lams:head>

<body class="stripes" id="body">
<!-- <body class="stripes" onLoad="init()">
<c:set var="title"><fmt:message key="label.learning.title" /></c:set>
-->
<lams:Page type="learner" title="${daco.title}">

<span class="pull-right voffset5">
<lams:help toolSignature="<%= DacoConstants.TOOL_SIGNATURE %>" module="learning" />

<%-- To switch between the vertical/horizontal view --%>
<c:choose>
	<c:when test="${sessionMap.learningView=='horizontal'}">
	<i class="fa fa-ellipsis-h" title="<fmt:message key="label.common.view.change" />"
	 onclick="javascript:changeView('${sessionMapID}',${displayedRecordNumber})" id="ellipsis"></i>
	</c:when>
	<c:otherwise>
	<i class="fa fa-ellipsis-v" title="<fmt:message key="label.common.view.change" />"
	 onclick="javascript:changeView('${sessionMapID}',${displayedRecordNumber})" id="ellipsis"></i>
	</c:otherwise>
</c:choose>
</span>

<%-- <div id="header">
<lams:Tabs useKey="true" control="true">
	<lams:Tab id="1" key="label.learning.heading.add" />
	<lams:Tab id="2" key="label.learning.heading.list" />
	<lams:Tab id="3" key="label.learning.heading.summary" />
</lams:Tabs></div>
<lams:TabBody id="1" titleKey="label.learning.heading.add" page="addRecord.jsp?displayedRecordNumber=${displayedRecordNumber}" />
<lams:TabBody id="2" titleKey="label.learning.heading.list" page="listRecords.jsp?includeMode=learning" />
<lams:TabBody id="3" titleKey="label.learning.heading.summary" page="questionSummaries.jsp" />
 --%>

<!-- Nav tabs -->
  <ul class="nav nav-tabs" role="tablist">
    <li role="presentation" class="active"><a href="#add" aria-controls="add" role="tab" data-toggle="tab">
    		<fmt:message key="label.learning.heading.add"/></a></li>
    <li role="presentation"><a href="#list" aria-controls="list" role="tab" data-toggle="tab">
			<fmt:message key="label.learning.heading.list"/></a></li>
    <li role="presentation"><a href="#summary" aria-controls="summary" role="tab" data-toggle="tab">
    		<fmt:message key="label.learning.heading.summary"/></a></li>
  </ul>

<!-- Tab panes -->
  	<div class="tab-content">
    <div role="tabpanel" class="tab-pane active" id="add"><jsp:include page="addRecord.jsp?displayedRecordNumber=${displayedRecordNumber}"/></div>
    <div role="tabpanel" class="tab-pane" id="list"><jsp:include page="listRecords.jsp?includeMode=learning"/></div>
    <div role="tabpanel" class="tab-pane" id="summary"><jsp:include page="questionSummaries.jsp"/></div>
  </div> 


<c:if test="${mode != 'teacher'}">
		<c:choose>
			<c:when test="${daco.reflectOnActivity && (not sessionMap.userFinished)}">
				<html:button property="FinishButton" onclick="javascript:continueReflect()" styleClass="btn btn-default voffset5 pull-right">
					<fmt:message key="label.learning.continue" />
				</html:button>
			</c:when>
			<c:otherwise>
				<html:link href="#nogo" property="FinishButton" styleId="finishButton" onclick="javascript:finishSession()" styleClass="btn btn-primary voffset5 pull-right na">
					<span class="nextActivity">
						<c:choose>
		 					<c:when test="${sessionMap.activityPosition.last}">
		 						<fmt:message key="label.learning.submit" />
		 					</c:when>
		 					<c:otherwise>
		 		 				<fmt:message key="label.learning.finished" />
		 					</c:otherwise>
		 				</c:choose>
		 			</span>
				</html:link>
			</c:otherwise>
		</c:choose>
</c:if>

</div> 

<div id="footer"></div>
</lams:Page>

</body>
</lams:html>
