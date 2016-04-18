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


<body class="stripes" id="body" onLoad="init()">
<lams:Page type="navbar" title="${daco.title}">

<%-- To switch between the vertical/horizontal view --%>
<c:set var="ellipsisControl">
<c:choose>
	<c:when test="${sessionMap.learningView=='horizontal'}">
	<i class="fa fa-ellipsis-h loffset10" title="<fmt:message key="label.common.view.change" />"
	 onclick="javascript:changeView('${sessionMapID}',${displayedRecordNumber})" id="ellipsis"></i>
	</c:when>
	<c:otherwise>
	<i class="fa fa-ellipsis-v loffset10" title="<fmt:message key="label.common.view.change" />"
	 onclick="javascript:changeView('${sessionMapID}',${displayedRecordNumber})" id="ellipsis"></i>
	</c:otherwise>
</c:choose>
</c:set> 

<lams:HybridTabHeader control="true" title="${daco.title}" helpToolSignature="<%= DacoConstants.TOOL_SIGNATURE %>" helpModule="learning" extraControl="${ellipsisControl}">
 	<lams:HybridTab id="1" key="label.learning.heading.add" />
 	<lams:HybridTab id="2" key="label.learning.heading.list" />
	<lams:HybridTab id="3" key="label.learning.heading.summary" />
</lams:HybridTabHeader>	


<lams:HybridTabBody>
	<lams:TabBodys>
	 	<lams:TabBody id="1" titleKey="label.learning.heading.add" page="addRecord.jsp?displayedRecordNumber=${displayedRecordNumber}" />
 		<lams:TabBody id="2" titleKey="label.learning.heading.list" page="listRecords.jsp?includeMode=learning" />
	 	<lams:TabBody id="3" titleKey="label.learning.heading.summary" page="questionSummaries.jsp" />
	 </lams:TabBodys>

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
 
</lams:HybridTabBody>

<div id="footer"></div>
</lams:Page>

</body>
</lams:html>
