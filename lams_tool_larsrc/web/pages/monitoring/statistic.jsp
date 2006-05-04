<%@ include file="/common/taglibs.jsp"%>

<table border="0" cellspacing="3" width="98%">
	<c:forEach var="group" items="${summaryList}">
		<c:set var="groupSize" value="${fn:length(group)}"/>
		<c:forEach var="item" items="${group}" varStatus="status">
			<%-- display group name on first row--%>
			<c:if test="${status.index == 0}">
				<tr>
					<td>
						<B><fmt:message key="monitoring.label.group" />
						${item.sessionName}</B>
					</td>
				</tr>
				<tr>
					<td>
						<table border="0" cellspacing="3" width="98%">
							<tr>
								<th width="50">
									<fmt:message key="monitoring.label.type" />
								</th>
								<th width="300">
									<fmt:message key="monitoring.label.title" />
								</th>
								<th width="150">
									<fmt:message key="monitoring.label.suggest" />
								</th>
								<th width="180" align="center">
									<fmt:message key="monitoring.label.number.learners" />
								</th>
							</tr>
				</c:if>
							<tr>
								<td>
									<c:choose>
										<c:when test="${item.itemType == 1}">
											<fmt:message key="label.authoring.basic.resource.url"/>
										</c:when>
										<c:when test="${item.itemType == 2}">
											<fmt:message key="label.authoring.basic.resource.file"/>
										</c:when>
										<c:when test="${item.itemType == 3}">
											<fmt:message key="label.authoring.basic.resource.website"/>
										</c:when>
										<c:when test="${item.itemType == 4}">
											<fmt:message key="label.authoring.basic.resource.learning.object"/>
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
												<c:url value='/monitoring/listuser.do?toolSessionID=${item.sessionId}&itemUid=${item.itemUid}'/>
											</c:set>
											<a href="#" onclick="launchPopup('${listUrl}','listuser')"> ${item.viewNumber}<a>
										</c:when>
										<c:otherwise>
											0
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
				<c:if test="${status.count == groupSize}">
						</table>
					</td>
				</tr>
				</c:if>
		</c:forEach>
	</c:forEach>
</table>
