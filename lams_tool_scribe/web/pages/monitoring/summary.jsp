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
								
									?Please select a scribe:?

									<html:select property="appointedScribeUID" style="min-width: 150px;">
										<c:forEach var="user" items="${session.userDTOs}">
											<html:option value="${user.uid}">
												${user.firstName} ${user.lastName}
											</html:option>
										</c:forEach>
									</html:select>

									<div>
										<input type="submit" value="?submit?" class="button" />
									</div>

								</html:form>
							</c:when>

							<c:otherwise>
								?No users available?
							</c:otherwise>

						</c:choose>

					</c:when>

					<c:otherwise>
						<div class="field-name" style="text-align: left">
							?Appointed Scribe?
						</div>

						<p>
							${session.appointedScribe}
						</p>

						<div class="field-name" style="text-align: left">
							?Report?
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

								<html:link href="${fn:escapeXml(openNotebook)}" target="_blank">
									<fmt:message key="link.view" />
								</html:link>
							</c:if>
						</td>
					</c:if>
				</tr>
			</c:forEach>
		</c:if>
	</table>
</c:forEach>
