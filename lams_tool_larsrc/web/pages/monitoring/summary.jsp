<%@ include file="/common/taglibs.jsp"%>

<table border="0" cellspacing="3" width="98%">
	<c:if test="${empty summaryList}">
		<div align="center">
			<b> <fmt:message key="message.monitoring.summary.no.session" /> </b>
		</div>
	</c:if>
	<c:forEach var="group" items="${summaryList}" varStatus="firstGroup">
		<c:set var="groupSize" value="${fn:length(group)}" />
		<c:forEach var="item" items="${group}" varStatus="status">
			<%-- display group name on first row--%>
			<c:if test="${status.index == 0}">
				<tr>
					<td>
						<B><fmt:message key="monitoring.label.group" /> ${item.sessionName}</B> <SPAN style="font-size: 12px;"> <c:if test="${firstGroup.index==0}">
								<fmt:message key="monitoring.summary.note" />
							</c:if> </SPAN>
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
								<th width="30">
									<!--hide/show-->
								</th>
							</tr>
							<%-- End group title display --%>
							</c:if>
							<c:if test="${item.itemUid == -1}">
								<tr>
									<td colspan="5">
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
										<a href="javascript:;" onclick="viewItem(${item.itemUid})">${item.itemTitle}</a>
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
													<c:url value='/monitoring/listuser.do?toolSessionID=${item.sessionId}&itemUid=${item.itemUid}' />
												</c:set>
												<a href="#" onclick="launchPopup('${listUrl}','listuser')"> ${item.viewNumber}<a>
											</c:when>
											<c:otherwise>
												0
											</c:otherwise>
										</c:choose>
									</td>
									<td align="center">
										<c:choose>
											<c:when test="${item.itemHide}">
												<a href="<c:url value='/monitoring/showitem.do'/>?itemUid=${item.itemUid}" class="button"> <fmt:message key="monitoring.label.show" /> </a>
											</c:when>
											<c:otherwise>
												<a href="<c:url value='/monitoring/hideitem.do'/>?itemUid=${item.itemUid}" class="button"> <fmt:message key="monitoring.label.hide" /> </a>
											</c:otherwise>
										</c:choose>
									</td>
								</tr>
							</c:if>
							<c:if test="${status.count == groupSize}">
						</table>
					</td>
				</tr>
			</c:if>
		</c:forEach>
	</c:forEach>
</table>
