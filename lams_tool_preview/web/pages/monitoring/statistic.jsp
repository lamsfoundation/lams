<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>

	<c:choose>
	<c:when test="${empty summaryList}">
		<lams:Alert type="info" id="no-session-summary" close="false">
			<fmt:message key="message.monitoring.summary.no.session" />
		</lams:Alert>
	</c:when>
	<c:otherwise>
		<lams:Alert type="info" id="session-summary-note" close="false">
			<fmt:message key="monitoring.summary.note" />
		</lams:Alert>
	</c:otherwise>
	</c:choose>
	
	<c:if test="${sessionMap.isGroupedActivity}">
	<div class="panel-group" id="accordionSessions" role="tablist" aria-multiselectable="true"> 
	</c:if>
	
	<c:forEach var="groupSummary" items="${summaryList}" varStatus="status">
		<c:if test="${sessionMap.isGroupedActivity}">	
		    <div class="panel panel-default" >
	        <div class="panel-heading" id="headingStats${groupSummary.sessionId}">
				<span class="panel-title">
					<fmt:message key="monitoring.label.group" />: <c:out value="${groupSummary.sessionName}" />
				</span>
	        </div>
	
	        <div class="panel-body">
		</c:if>
	
		<table class="table table-condensed table-no-border">
			<tr>
				<td class="field-name" width="30%">
					Session Id
				</td>
				<td>
					${groupSummary.sessionId}
				</td>
			</tr>
		</table>

		<c:if test="${sessionMap.isGroupedActivity}">	
			</div>
			</div>
		</c:if>
	</c:forEach>

	<c:if test="${sessionMap.isGroupedActivity}">
	</div>
	</c:if>
	