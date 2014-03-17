<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>

<table cellspacing="3">

	<c:if test="${empty summaryList}">
		<div align="center">
			<b> <fmt:message key="message.monitoring.summary.no.session" /> </b>
		</div>
	</c:if>
	
	<c:forEach var="group" items="${summaryList}" varStatus="firstGroup">
		<c:forEach var="item" items="${group.items}" varStatus="status">
		
			<%-- display group name on first row--%>
			<c:if test="${status.index == 0}">
				<tr>
					<td colspan="4">
						<c:if test="${sessionMap.isGroupedActivity}">
							<B><fmt:message key="monitoring.label.group" /> ${group.sessionName}</B> 
						</c:if>
					</td>
				</tr>
				<tr>
					<th width="20%">
						<fmt:message key="monitoring.label.type" />
					</th>
					<th width="35%">
						<fmt:message key="monitoring.label.title" />
					</th>
					<th width="25%">
						<fmt:message key="monitoring.label.suggest" />
					</th>
					<th width="20%" align="center">
						<fmt:message key="monitoring.label.number.learners" />
					</th>
				</tr>
			</c:if>
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
						${item.itemTitle}
					</td>
					<td>
						<c:if test="${!item.itemCreateByAuthor}">
							${item.username}
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
	</c:forEach>
</table>
