<%@ include file="/common/taglibs.jsp"%>

<table border="0" cellspacing="3" width="98%">
	<c:forEach var="group" items="summaryList">
		<c:forEach var="item" items="group" statusVar="status">
			<%-- display group name on first row--%>
			<c:if test="${status.index = 0}">
				<tr>
					<td>
						<fmt:message key="monitoring.label.group" />
						${item.sessionName}
					</td>
				</tr>
				<tr>
					<td>
						<table border="0" cellspacing="3">
							<tr>
								<th>
									<fmt:message key="monitoring.label.type" />
								</th>
								<th>
									<fmt:message key="monitoring.label.title" />
								</th>
								<th>
									<fmt:message key="monitoring.label.suggest" />
								</th>
								<th>
									<fmt:message key="monitoring.label.number.learners" />
								</th>
								<th>
									<!--hide/show-->
								</th>
							</tr>
							</c:if>
							<tr>
								<td>
									<c:choose>
										<c:when test="${item.itemHide == 1}">
											<fmt:message value="label.authoring.basic.resource.url"/>
										</c:when>
										<c:when test="${item.itemHide == 2}">
											<fmt:message value="label.authoring.basic.resource.file"/>
										</c:when>
										<c:when test="${item.itemHide == 3}">
											<fmt:message value="label.authoring.basic.resource.website"/>
										</c:when>
										<c:when test="${item.itemHide == 4}">
											<fmt:message value="label.authoring.basic.resource.learning.object"/>
										</c:when>
									</c:choose>
								</td>
								<td>
									${item.itemTitle}
								</td>
								<td>
									${item.username}
								</td>
								<td>
									<c:set var="listUrl">
										<c:url value='/montoring/listuser.do?toolSessionID=${item.sessionId}&itemUid=${item.itemUid}'/>
									</c:set>
									<a href="javascript:;" onclick="launchPopup("${listUrl}",'listuser')"> ${item.viewNumber}<a>
								</td>
								<td>
									<c:if test="${!item.itemCreateByAuthor}">
										<c:choose>
											<c:when test="${item.itemHide}">
												<a href="<c:url value='/montoring/showitem.do'/>?itemUid=${item.itemUid}"> <fmt:message key="monitoring.label.show" /> </a>
											</c:when>
											<c:otherwise>
												<a href="<c:url value='/montoring/hideitem.do'/>?itemUid=${item.itemUid}"> <fmt:message key="monitoring.label.hide" /> </a>
											</c:otherwise>
										</c:choose>
									</c:if>
								</td>
							</tr>
							<c:if test="${status.index = 0}">
						</table>
					</td>
				</tr>
			</c:if>
		</c:forEach>
	</c:forEach>
</table>
