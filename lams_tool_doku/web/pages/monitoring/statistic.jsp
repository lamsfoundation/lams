<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>

	<c:choose>
		<c:when test="${empty summaryList}">
			<lams:Alert type="info" id="no-session-summary" close="false">
				<fmt:message key="message.monitoring.summary.no.session" /> </b>
			</lams:Alert>
		</c:when>
		<c:otherwise>
			<lams:Alert type="info" id="no-session-summary" close="false">
				<fmt:message key="monitoring.summary.note" />
			</lams:Alert>
		</c:otherwise>
	</c:choose>
	
	<c:forEach var="group" items="${summaryList}" varStatus="firstGroup">
	
		<c:if test="${sessionMap.isGroupedActivity}">	
		    <div class="panel panel-default" >
	        <div class="panel-heading" id="heading${toolSessionDto.sessionID}">
				<span class="panel-title">
					<fmt:message key="monitoring.label.group" />&nbsp;${group.sessionName}
				</span>
	        </div>
		</c:if>
       
		<c:if test="${sessionMap.isGroupedActivity}">	
			</div>
    	</c:if>   
	</c:forEach>
