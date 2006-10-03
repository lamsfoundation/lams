<%@ include file="/common/taglibs.jsp"%>

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
			<td>
				<c:choose>
					<c:when
						test="${(not dto.autoSelectScribe) and  session.appointedScribe eq null}">

						<c:choose>
							<c:when test="${not empty session.userDTOs}">
								<html:form action="/monitoring">

									<html:hidden property="toolSessionID"
										value="${session.sessionID}" />
									<html:hidden property="dispatch" value="appointScribe" />

									<fmt:message key="heading.selectScribe" />

									<html:select property="appointedScribeUID"
										style="min-width: 150px;">
										<c:forEach var="user" items="${session.userDTOs}">
											<html:option value="${user.uid}">
												${user.firstName} ${user.lastName}
											</html:option>
										</c:forEach>
									</html:select>

									<div>
										<html:submit></html:submit>
									</div>

								</html:form>
							</c:when>

							<c:otherwise>
								<fmt:message key="message.noLearners" />
							</c:otherwise>

						</c:choose>

					</c:when>

					<c:otherwise>
						<div class="field-name" style="text-align: left">
							<fmt:message key="heading.appointedScribe" />
						</div>

						<p>
							${session.appointedScribe}
						</p>

						<div class="field-name" style="text-align: left">
							<fmt:message key="heading.totalLearners" />
						</div>

						<p>
							${session.numberOfLearners}
						</p>

						<div class="field-name" style="text-align: left">
							<fmt:message key="heading.numberOfVotes" />
						</div>

						<p>
							${session.numberOfVotes}
						</p>

						<div class="field-name" style="text-align: left">
							<fmt:message key="heading.report" />
						</div>
						<hr />
						<c:forEach var="report" items="${session.reportDTOs}">
							<p>
								<lams:out value="${report.headingDTO.headingText}" />
							</p>
							<p>
								<lams:out value="${report.entryText}" />
							</p>
							<hr />
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
	</table>

	<c:if test="${dto.reflectOnActivity}">
		<div class="field-name" style="text-align: left">
			<fmt:message key="heading.reflections" />
		</div>
		<table class="alternative-color">
			<tr>
				<th class="first">
					<fmt:message>heading.learner</fmt:message>
				</th>

				<th class="first">
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
						${user.firstName} ${user.lastName}
					</td>
					<c:if test="${dto.reflectOnActivity}">
						<td>
							<c:if test="${user.finishedReflection}">
								<c:url value="monitoring.do" var="openNotebook">
									<c:param name="dispatch" value="openNotebook" />
									<c:param name="uid" value="${user.uid}" />
								</c:url>

								<html:link href="${fn:escapeXml(openNotebook)}" target="_blank">
									<fmt:message key="link.view" />
								</html:link>
							</c:if>
						</td>
					</c:if>
				</tr>
			</c:forEach>
		</table>
	</c:if>
</c:forEach>
