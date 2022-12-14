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
		
		<table class="table table-striped table-condensed">
			<tr>
				<th width="20%">
					<fmt:message key="monitoring.label.type" />
				</th>
				<th>
					<fmt:message key="monitoring.label.title" />
				</th>
				<th width="25%">
					<fmt:message key="monitoring.label.suggest" />
				</th>
				<th width="20%" align="center">
					<fmt:message key="monitoring.label.number.learners" />
				</th>
			</tr>
		  
			<c:forEach var="item" items="${group.items}" varStatus="status">
				<tr>
					<td>
						<c:choose>
							<c:when test="${item.itemType == 1}">
								<fmt:message key="label.authoring.basic.resource.url" />
							</c:when>
							<c:when test="${item.itemType == 2}">
								<fmt:message key="label.authoring.basic.resource.file" />
							</c:when>
							<c:when test="${item.itemType == 3}">
								<fmt:message key="label.authoring.basic.resource.website" />
							</c:when>
						</c:choose>
					</td>
					<td>
						<c:out value="${item.itemTitle}" escapeXml="true"/>
					</td>
					<td>
						<c:if test="${!item.itemCreateByAuthor}">
							<c:out value="${item.username}" escapeXml="true"/>
						</c:if>
					</td>
					<td align="center">
						<c:choose>
							<c:when test="${item.viewNumber > 0}">
								<c:set var="listUrl">
									<c:url value='/monitoring/listuser.do?toolSessionID=${group.sessionId}&itemUid=${item.itemUid}' />
								</c:set>
								<a href="#" onclick="launchPopup('${listUrl}','listuser')"> ${item.viewNumber}<a>
							</c:when>
							<c:otherwise>
											0
										</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</c:forEach>
		</table>
       
		<c:if test="${sessionMap.isGroupedActivity}">	
		</div>
    	</c:if>   
	</c:forEach>
