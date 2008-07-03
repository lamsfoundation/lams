<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="summaryList" value="${sessionMap.summaryList}" />


<table cellspacing="3">
	<c:if test="${empty summaryList}">
		<div align="center"><b> <fmt:message
			key="message.monitoring.summary.no.session" /> </b></div>
	</c:if>
	<c:forEach var="group" items="${summaryList}" varStatus="firstGroup">
		<c:set var="groupSize" value="${fn:length(group)}" />
		<c:forEach var="question" items="${group}" varStatus="status">
			<%-- display group name on first row--%>
			<c:if test="${status.index == 0}">
				<tr>
					<td colspan="4"><B><fmt:message
						key="label.monitoring.group" /> ${question.sessionName}</B> <SPAN
						style="font-size: 12px;"> <c:if
						test="${firstGroup.index==0}">
						<fmt:message key="note.monitoring.summary" />
					</c:if> </SPAN></td>
				</tr>
				<tr>
					<th width="20%" align="center"><fmt:message
						key="label.monitoring.type" /></th>
					<th width="35%"><fmt:message key="label.monitoring.title" />
					</th>
					<th width="25%"><fmt:message key="label.monitoring.suggest" />
					</th>
					<th width="20%" align="center"><fmt:message
						key="label.monitoring.number.learners" /></th>
				</tr>
			</c:if>
			<c:if test="${question.questionUid == -1}">
				<tr>
					<td colspan="4">
					<div align="left"><b> <fmt:message
						key="message.monitoring.summary.no.question.for.group" /> </b></div>
					</td>
				</tr>
			</c:if>
			<c:if test="${question.questionUid != -1}">
				<tr>
					<td><c:choose>
						<c:when test="${question.questionType == 1}">
							<fmt:message key="label.authoring.basic.textfield" />
						</c:when>
						<c:when test="${question.questionType == 2}">
							<fmt:message key="label.authoring.basic.textarea" />
						</c:when>
						<c:when test="${question.questionType == 3}">
							<fmt:message key="label.authoring.basic.number" />
						</c:when>
						<c:when test="${question.questionType == 4}">
							<fmt:message key="label.authoring.basic.date" />
						</c:when>
						<c:when test="${question.questionType == 5}">
							<fmt:message key="label.authoring.basic.file" />
						</c:when>
												<c:when test="${question.questionType == 6}">
							<fmt:message key="label.authoring.basic.image" />
						</c:when>
						<c:when test="${question.questionType == 7}">
							<fmt:message key="label.authoring.basic.radio" />
						</c:when>
						<c:when test="${question.questionType == 8}">
							<fmt:message key="label.authoring.basic.dropdown" />
						</c:when>
						<c:when test="${question.questionType == 9">
							<fmt:message key="label.authoring.basic.checkbox" />
						</c:when>
												<c:when test="${question.questionType == 10">
							<fmt:message key="label.authoring.basic.longlat" />
						</c:when>
					</c:choose></td>
					<td>${question.questionTitle}</td>
					<td><c:if test="${!question.questionCreateByAuthor}">
										${question.username}
									</c:if></td>
					<td align="center"><c:choose>
						<c:when test="${question.viewNumber > 0}">
							<c:set var="listUrl">
								<c:url
									value='/monitoring/listuser.do?toolSessionID=${question.sessionId}&questionUid=${question.questionUid}' />
							</c:set>
							<a href="#" onclick="launchPopup('${listUrl}','listuser')">
							${question.viewNumber}<a>
						</c:when>
						<c:otherwise>
											0
										</c:otherwise>
					</c:choose></td>
				</tr>
			</c:if>
		</c:forEach>
	</c:forEach>
</table>
