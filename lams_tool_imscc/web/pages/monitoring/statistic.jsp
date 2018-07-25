<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>

<c:if test="${empty summaryList}">
	<lams:Alert type="info" id="no-session-summary" close="false">
		<fmt:message key="message.monitoring.summary.no.session" />
	</lams:Alert>
</c:if>

<c:forEach var="group" items="${summaryList}" varStatus="firstGroup">

	<c:if test="${sessionMap.isGroupedActivity}">
		<div class="panel panel-default" >
			<div class="panel-heading">
				<span class="panel-title">
					<fmt:message key="monitoring.label.group" /> ${group[0].sessionName}
				</span>
			</div>
		</div>
	</c:if>
	
	<div class="panel-body">
	<table class="table table-condensed table-striped">
		<tr>
			<th width="20%" align="center">
				<fmt:message key="monitoring.label.type" />
			</th>
			<th width="35%">
				<fmt:message key="monitoring.label.title" />
			</th>
			<th width="20%" align="center">
				<fmt:message key="monitoring.label.number.learners" />
			</th>
		</tr>
		
		<c:forEach var="item" items="${group}" varStatus="status">
			<c:if test="${item.itemUid == -1}">
				<tr>
					<td colspan="3">
						<div align="left">
							<b> <fmt:message key="message.monitoring.summary.no.resource.for.group" /> </b>
						</div>
					</td>
				</tr>
			</c:if>
			<c:if test="${item.itemUid != -1}">
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
							<c:when test="${item.itemType == 4}">
								<fmt:message key="label.authoring.basic.resource.learning.object" />
							</c:when>
						</c:choose>
					</td>
					<td>
						<c:out value="${item.itemTitle}" escapeXml="true"/>
					</td>
					<td align="center">
						<c:choose>
							<c:when test="${item.viewNumber > 0}">
								<c:set var="listUrl">
									<c:url value='/monitoring/listuser.do?toolSessionID=${item.sessionId}&itemUid=${item.itemUid}' />
								</c:set>
								<a href="#" onclick="launchPopup('${listUrl}','listuser')"> 
									${item.viewNumber}
								</a>
							</c:when>
							<c:otherwise>
								0
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</c:if>
		</c:forEach>
	</table>
	</div>
</c:forEach>
