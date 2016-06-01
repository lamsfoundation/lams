<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>



	<c:if test="${empty summaryList}">
		<lams:Alert type="info" id="no-session-summary" close="false">
			<fmt:message key="message.monitoring.summary.no.session" />
		</lams:Alert>
	</c:if>
	
	<c:forEach var="summary" items="${summaryList}" varStatus="firstGroup">

		<c:if test="${sessionMap.isGroupedActivity}">	
		    <div class="panel panel-default" >
	        <div class="panel-heading" id="heading${summary.sessionId}">
				<span class="panel-title">
					<fmt:message key="monitoring.label.group" /> ${summary.sessionName}
				</span>
	        </div>
		</c:if>

		<table class="table table-striped table-condensed">
		<tr>
			<th class="text-center" width="20px;">
				#
			</th>				
			<th width="150px;">
				<fmt:message key="label.monitoring.summary.user.name" />
			</th>
			<th class="text-center" width="80px;" >
				<fmt:message key="label.monitoring.summary.mark" />
			</th>					
		</tr>	
	
		<c:forEach var="user" items="${summary.users}" varStatus="status">
			<tr>
				<td class="text-center" >
					${status.index + 1}
				</td>				
				<td>
					<c:out value="${user.firstName} ${user.lastName}" escapeXml="true"/>
				</td>
				<td class="text-center" >
					<c:choose>
						<c:when test='${summary.totalAttempts == 0}'>-</c:when> 
						<c:otherwise>${summary.mark}</c:otherwise>
					</c:choose>
				</td>
			</tr>
		</c:forEach>
		</table>
		
		<c:if test="${sessionMap.isGroupedActivity}">	
			</div>
		</c:if>
	
	</c:forEach>

