<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/monitorToolSummaryAdvanced.js" ></script>

<h1>
	<img src="<lams:LAMSURL/>/images/tree_closed.gif" id="treeIcon" onclick="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv'), document.getElementById('treeIcon'), '<lams:LAMSURL/>');" />

	<a href="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv'), document.getElementById('treeIcon'),'<lams:LAMSURL/>');" >
		<fmt:message key="monitor.summary.th.advancedSettings" />
	</a>
</h1>
<br />
<div class="monitoring-advanced" id="advancedDiv" style="display:none">

<table class="alternative-color">

	<tr>
		<td>
			<fmt:message key="advanced.lockOnFinished" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${monitoringDTO.lockOnFinish == true}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="monitor.summary.td.addNotebook" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${monitoringDTO.reflectOnActivity == true}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<c:choose>
		<c:when test="${monitoringDTO.reflectOnActivity == true}">
			<tr>
				<td>
					<fmt:message key="monitor.summary.td.notebookInstructions" />
				</td>
				<td>
					${monitoringDTO.reflectInstructions}	
				</td>
			</tr>
		</c:when>
	</c:choose>
	
	<tr>
		<td>
			<fmt:message key="advanced.filteringEnabled" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${monitoringDTO.filteringEnabled == true}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<c:choose>
		<c:when test="${monitoringDTO.filteringEnabled == true}">
			<tr>
				<td>
					<fmt:message key="monitor.summary.td.filteredWords" />
				</td>
				<td>
					${monitoringDTO.filteredKeyWords}	
				</td>
			</tr>
		</c:when>
	</c:choose>
</table>
</div>



<c:set var="dto" value="${requestScope.monitoringDTO}" />
<c:forEach var="session" items="${dto.sessionDTOs}">
	<table cellspacing="0">
		<tr>
			<td colspan="3">
				<h2>
					${session.sessionName}
				</h2>
			</td>
		</tr>

		<tr>
			<td class="field-name" style="width: 30%;">
				<fmt:message>heading.totalLearners</fmt:message>
			</td>
			<td colspan="2">
				${session.numberOfLearners}
			</td>
		</tr>

		<tr>
			<td class="field-name" style="width: 30%;">
				<fmt:message>heading.totalMessages</fmt:message>
			</td>
			<td colspan="2">
				${session.numberOfPosts}
			</td>
		</tr>

		<tr>
			<td colspan="3">
				<h4>
					<fmt:message>heading.recentMessages</fmt:message>
				</h4>
			</td>
		</tr>

		<tr>
			<td colspan="3">
				<c:choose>
					<c:when test="${empty session.messageDTOs}">
						<fmt:message>message.noChatMessages</fmt:message>
					</c:when>
					<c:otherwise>
						<c:forEach var="message" items="${session.messageDTOs}">
							<div class="message">
								<div class="messageFrom">
									${message.from}
								</div>
								<lams:out escapeHtml="true" value="${message.body}"></lams:out>
							</div>
						</c:forEach>

					</c:otherwise>
				</c:choose>
			</td>
		</tr>

		<c:if test="${dto.reflectOnActivity}">
			<tr>
				<td colspan="3">
					<h4>
						Reflections
					</h4>
				</td>
			</tr>

			<tr>
				<th>
					<fmt:message>heading.learner</fmt:message>
				</th>
				<th>
					<fmt:message>heading.numPosts</fmt:message>
				</th>

				<th>
					<c:choose>
						<c:when test="${dto.reflectOnActivity}">
							<fmt:message key="heading.reflection" />
						</c:when>
						<c:otherwise>
						&nbsp;
					</c:otherwise>
					</c:choose>
				</th>
			</tr>

			<c:forEach var="user" items="${session.userDTOs}">
				<tr>
					<td>
						${user.jabberNickname}
					</td>
					<td>
						${user.postCount}
					</td>
					<c:if test="${dto.reflectOnActivity}">
						<td>
							<c:if test="${user.finishedReflection}">
								<c:url value="monitoring.do" var="openNotebook">
									<c:param name="dispatch" value="openNotebook" />
									<c:param name="uid" value="${user.uid}" />
								</c:url>

								<html:link
									href="javascript:launchPopup('${fn:escapeXml(openNotebook)}')">
									<fmt:message key="link.view" />
								</html:link>
							</c:if>
						</td>
					</c:if>
				</tr>
			</c:forEach>
		</c:if>
		<tr>
			<td colspan="3">
				<div>
					<html:form action="/monitoring" method="get" target="_blank">
						<div>
							<html:hidden property="dispatch" value="openChatHistory" />
							<html:hidden property="toolSessionID"
								value="${session.sessionID}" />
							<html:submit styleClass="button">
								<fmt:message>summary.editMessages</fmt:message>
							</html:submit>
						</div>
					</html:form>

					<html:form action="/learning" method="get" target="_blank">
						<div>
							<html:hidden property="toolSessionID"
								value="${session.sessionID}" />
							<html:hidden property="mode" value="teacher" />
							<html:submit styleClass="button">
								<fmt:message>summary.openChat</fmt:message>
							</html:submit>
						</div>
					</html:form>
				</div>
			</td>
		</tr>
	</table>
	<hr>
</c:forEach>
