<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.daco.DacoConstants"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%@ page import="org.lamsfoundation.lams.util.FileValidatorUtil" %>

<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="UPLOAD_FILE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_MAX_SIZE)%></c:set>
<c:set var="UPLOAD_FILE_MAX_SIZE_AS_USER_STRING" scope="request"><%=FileValidatorUtil.formatSize(Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_MAX_SIZE))%></c:set>
<c:set var="EXE_STRING"><%=Configuration.get(ConfigurationKeys.EXE_EXTENSIONS)%></c:set>
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
<%-- To switch between the vertical/horizontal view --%>
<c:set var="ellipsisControl">
<c:choose>
	<c:when test="${sessionMap.learningView=='horizontal'}">
		<i class="fa-solid fa-ellipsis ms-2" title="<fmt:message key="label.common.view.change" />"
	 		onclick="javascript:changeView('${sessionMapID}',${displayedRecordNumber})" id="ellipsis"></i>
	</c:when>
	<c:otherwise>
		<i class="fa-solid fa-ellipsis-vertical ms-2" title="<fmt:message key="label.common.view.change" />"
	 		onclick="javascript:changeView('${sessionMapID}',${displayedRecordNumber})" id="ellipsis"></i>
	</c:otherwise>
</c:choose>
</c:set> 
<%-- push mode into request so it is available in the tab pages --%>
<c:set var="mode" scope="request">${mode}</c:set>

<lams:PageLearner title="${daco.title}" toolSessionID="${sessionMap.toolSessionID}">
	<link href="<c:url value='/includes/css/daco.css'/>" rel="stylesheet" type="text/css">
	
	<lams:JSImport src="includes/javascript/common.js" />
	<lams:JSImport src="includes/javascript/dacoCommon.js" relative="true" />
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap5.tabcontroller.js"></script>    
	<lams:JSImport src="includes/javascript/upload.js" />
	<script type="text/javascript">
		checkNextGateActivity('finishButton', '${toolSessionID}', '', function(){
			finishSession();
		});
	
	 	var currentTab = ${learningCurrentTab};
	 	var changeViewUrl = "<c:url value='/learning/changeView.do' />";
	 	var finishUrl = '<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}&displayedRecordNumber=${displayedRecordNumber}"/>';
	 	var continueReflectUrl = '<c:url value="/learning/startReflection.do?sessionMapID=${sessionMapID}&displayedRecordNumber=${displayedRecordNumber}"/>';
	 	var refreshQuestionSummariesUrl = '<c:url value="/learning/refreshQuestionSummaries.do"/>';
		var UPLOAD_FILE_MAX_SIZE = '${UPLOAD_FILE_MAX_SIZE}';
	 	var EXE_STRING = '${EXE_STRING}';
	 	
		<fmt:message key="errors.maxfilesize" var="LABEL_MAX_FILE_SIZE_VAR"><param>{0}</param></fmt:message>
		var LABEL_MAX_FILE_SIZE = '<c:out value="${LABEL_MAX_FILE_SIZE_VAR}" />';
		
		<fmt:message key="error.attachment.executable" var="LABEL_NOT_ALLOWED_EXE_VAR"/>
		var LABEL_NOT_ALLOWED_EXE = '<c:out value="${LABEL_NOT_ALLOWED_EXE_VAR}" />';	

		<fmt:message key="error.attachment.must.be.image" var="LABEL_NOT_ALLOWED_FORMAT_VAR"/>
		var LABEL_NOT_ALLOWED_FORMAT = '<c:out value="${LABEL_NOT_ALLOWED_FORMAT_VAR}" />';	

		$(document).on('load', function(){
			init();
		});
    </script>
    <lams:JSImport src="includes/javascript/dacoLearning.js" relative="true" />

	<div id="container-main">

		<c:if test='${mode=="learner"}'>
			<div id="instructions" class="instructions">
				<c:out value="${daco.instructions}" escapeXml="false"/>
			</div>
		</c:if>
		<div class="border-bottom float-end pb-2">
			<button type="button" class="btn btn-secondary btn-sm" onclick="javascript:changeView('${sessionMapID}',${displayedRecordNumber})">
				<i class="fa-solid ${sessionMap.learningView=='horizontal' ? 'fa-ellipsis' : 'fa-ellipsis-vertical'} me-2" 
						title="<fmt:message key="label.common.view.change" />" id="ellipsis"></i>
				<fmt:message key="label.common.view.change" />
			</button>
		</div>
	
		<nav>
			 <div class="nav nav-tabs" id="tabs" role="tablist">
			    <button class="nav-link active px-5" id="tab-1" data-bs-toggle="tab" data-bs-target="#tab-1-content" type="button" role="tab" aria-controls="tab-1-content" aria-selected="true"><fmt:message key="label.learning.heading.add" /></button>
			    <button class="nav-link px-5"  		 id="tab-2" data-bs-toggle="tab" data-bs-target="#tab-2-content" type="button" role="tab" aria-controls="tab-2-content" aria-selected="false"><fmt:message key="label.learning.heading.list" /></button>
			    <button class="nav-link px-5" 		 id="tab-3" data-bs-toggle="tab" data-bs-target="#tab-3-content" type="button" role="tab" aria-controls="tab-3-content" aria-selected="false"><fmt:message key="label.learning.heading.summary" /></button>
			</div>
		</nav>
		<div class="tab-content m-3" id="tabs-content">
			<div class="tab-pane fade show active"  id="tab-1-content" role="tabpanel" aria-labelledby="tab-1" tabindex="0"><jsp:include page="addRecord.jsp?displayedRecordNumber=${displayedRecordNumber}"/></div>
			<div class="tab-pane fade" 				id="tab-2-content" role="tabpanel" aria-labelledby="tab-2" tabindex="0"><jsp:include page="listRecords.jsp?includeMode=learning"/></div>
			<div class="tab-pane fade" 				id="tab-3-content" role="tabpanel" aria-labelledby="tab-3" tabindex="0"><jsp:include page="questionSummaries.jsp"/></div>
		</div>
	
		<c:if test="${mode != 'teacher'}">
			<div class="activity-bottom-buttons">
				<c:choose>
					<c:when test="${daco.reflectOnActivity && (not sessionMap.userFinished)}">
						<button type="button" name="FinishButton" onclick="javascript:continueReflect()" class="btn btn-primary btn-disable-on-submit na">
							<fmt:message key="label.learning.continue" />
						</button>
					</c:when>
					
					<c:otherwise>
						<button type="submit" name="FinishButton" id="finishButton" class="btn btn-primary btn-disable-on-submit na">
							<c:choose>
				 				<c:when test="${sessionMap.isLastActivity}">
				 					<fmt:message key="label.learning.submit" />
				 				</c:when>
				 				<c:otherwise>
				 		 			<fmt:message key="label.learning.finished" />
				 				</c:otherwise>
				 			</c:choose>
						</button>
					</c:otherwise>
				</c:choose>
			</div>
		</c:if>
	
	</div>
</lams:PageLearner>