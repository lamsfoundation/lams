<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="summaryList" value="${sessionMap.summaryList}" />

<c:if test="${empty summaryList}">
	<div align="center"><b> <fmt:message
		key="message.monitoring.summary.no.session" /> </b></div>
</c:if>

<table cellpadding="0">
	<c:forEach var="group" items="${summaryList}" varStatus="firstGroup">
		<c:set var="groupSize" value="${fn:length(group)}" />
		<c:forEach var="question" items="${group}" varStatus="status">
			<%-- display group name on first row--%>
			<c:if test="${status.first}">
				<tr>
					<td colspan="5"><B><fmt:message
						key="label.monitoring.group" /> ${question.sessionName}</B> <SPAN
						style="font-size: 12px;"> <c:if
						test="${firstGroup.index==0}">
						<fmt:message key="note.monitoring.summary" />
					</c:if> </SPAN></td>
				</tr>
				<tr>
					<th width="18%" align="center"><fmt:message
						key="label.monitoring.type" /></th>
					<th width="25%"><fmt:message key="label.monitoring.title" />
					</th>
					<th width="20%"><fmt:message key="label.monitoring.suggest" />
					</th>
					<th width="22%" align="center"><fmt:message
						key="label.monitoring.number.learners" /></th>
					<th width="15%"><!--hide/show--></th>
				</tr>
				<%-- End group title display --%>
			</c:if>
			<c:if test="${question.questionUid == -1}">
				<tr>
					<td colspan="5">
					<div class="align-left"><b> <fmt:message
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
						<c:when test="${question.questionType == 9}">
							<fmt:message key="label.authoring.basic.checkbox" />
						</c:when>
									<c:when test="${question.questionType == 10">
							<fmt:message key="label.authoring.basic.longlat" />
						</c:when>
					</c:choose></td>
					<td><a href="javascript:;"
						onclick="viewQuestion(${question.questionUid},'${sessionMapID}')">${question.questionTitle}</a>
					</td>
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
					<td align="center"><c:choose>
						<c:when test="${question.questionHide}">
							<a
								href="<c:url value='/monitoring/showquestion.do'/>?sessionMapID=${sessionMapID}&questionUid=${question.questionUid}"
								class="button"> <fmt:message key="label.monitoring.show" />
							</a>
						</c:when>
						<c:otherwise>
							<a
								href="<c:url value='/monitoring/hidequestion.do'/>?sessionMapID=${sessionMapID}&questionUid=${question.questionUid}"
								class="button"> <fmt:message key="label.monitoring.hide" />
							</a>
						</c:otherwise>
					</c:choose></td>
				</tr>
			</c:if>

			<%-- Reflection list  --%>
			<c:if test="${sessionMap.daco.reflectOnActivity && status.last}">
				<c:set var="userList"
					value="${sessionMap.reflectList[question.sessionId]}" />
				<c:forEach var="user" items="${userList}" varStatus="refStatus">
					<c:if test="${refStatus.first}">
						<tr>
							<td colspan="5">
							<h2><fmt:message key="title.reflection" /></h2>
							</td>
						</tr>
						<tr>
							<th colspan="2"><fmt:message
								key="label.monitoring.user.fullname" /></th>
							<th colspan="2"><fmt:message
								key="label.monitoring.user.loginname" /></th>
							<th><fmt:message key="monitoring.user.reflection" /></th>
						</tr>
					</c:if>
					<tr>
						<td colspan="2">${user.fullName}</td>
						<td colspan="2">${user.loginName}</td>
						<td><c:set var="viewReflection">
							<c:url
								value="/monitoring/viewReflection.do?toolSessionID=${question.sessionId}&userUid=${user.userUid}" />
						</c:set> <html:link href="javascript:launchPopup('${viewReflection}')">
							<fmt:message key="label.view" />
						</html:link></td>
					</tr>
				</c:forEach>
			</c:if>

		</c:forEach>

	</c:forEach>
</table>
