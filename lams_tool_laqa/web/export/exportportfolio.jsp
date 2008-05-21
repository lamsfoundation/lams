<%--
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
License Information: http://lamsfoundation.org/licensing/lams/2.0/

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License version 2 as
  published by the Free Software Foundation.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA

  http://www.gnu.org/licenses/gpl.txt
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<title><fmt:message key="label.exportPortfolio" />
	</title>
	<lams:css localLinkPath="../" />
</lams:head>

<body class="stripes">

	<div id="content">

		<h1>
			<c:choose>
				<c:when test="${(portfolioExportMode == 'learner')}">
					<fmt:message key="label.export.learner" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.export.teacher" />
				</c:otherwise>
			</c:choose>
		</h1>


		<c:if
			test="${(userExceptionNoToolSessions == 'true') and (portfolioExportMode != 'learner')}">
			<p>
				<fmt:message key="error.noLearnerActivity" />
			</p>
		</c:if>


		<!-- LEARNER EXPORT -->

		<c:if test="${(userExceptionNoToolSessions != 'true') }">

			<c:if test="${(portfolioExportMode == 'learner')}">

				<c:forEach var="currentDto"
					items="${generalLearnerFlowDTO.listMonitoredAnswersContainerDTO}">

					<div>
						<p>
							<strong> <fmt:message key="label.question" /> : </strong>
							<c:out value="${currentDto.question}" escapeXml="false" />
						</p>

						<table cellspacing="0" class="alternative-color">
							<tr>
								<th style="width: 20%;">
									<fmt:message key="label.user" />
								</th>
								<th style="width: 20%;">
									<fmt:message key="label.learning.attemptTime" />
								</th>
								<th>
									<fmt:message key="label.response" />
								</th>
							</tr>

							<c:forEach var="questionAttemptData"
								items="${currentDto.questionAttempts}">

								<c:forEach var="sData" items="${questionAttemptData.value}">

									<c:set var="userData" scope="request" value="${sData.value}" />
									<c:set var="responseUid" scope="request"
										value="${userData.uid}" />

									<c:if test="${currentDto.questionUid == userData.questionUid}">

										<tr>
											<td>
												<c:out value="${userData.userName}" />
											</td>

											<td>
												<lams:Date value="${userData.attemptTime}" />
											</td>

											<td>
												<c:choose>
													<c:when
														test="${generalLearnerFlowDTO.userUid == userData.queUsrId or userData.visible == 'true'}">
														<c:out value="${userData.responsePresentable}"
															escapeXml="false" />
													</c:when>
													<c:otherwise>
														<i><fmt:message key="label.hidden" /> </i>
													</c:otherwise>
												</c:choose>
											</td>
										</tr>
									</c:if>
								</c:forEach>
							</c:forEach>
						</table>
					</div>
				</c:forEach>
			</c:if>


			<!-- TEACHER EXPORT -->

			<c:if test="${(portfolioExportMode != 'learner')}">

				<c:forEach var="groupDto" items="${listAllGroupsDTO}">
					<c:set var="sessionId" scope="request"
						value="${groupDto.sessionId}" />
					<c:set var="sessionName" scope="request"
						value="${groupDto.sessionName}" />
					<c:set var="groupData" scope="request"
						value="${groupDto.groupData}" />

					<h2>
						<fmt:message key="group.label" />
						:
						<c:out value="${sessionName}" />
					</h2>

					<c:forEach var="currentDto" items="${groupData}">

						<strong> <fmt:message key="label.question" /> : </strong>
						<c:out value="${currentDto.question}" escapeXml="false" />

						<table cellspacing="0" class="alternative-color">
							<tr>
								<th style="width: 20%;">
									<fmt:message key="label.user" />
								</th>
								<th style="width: 20%;">
									<fmt:message key="label.learning.attemptTime" />
								</th>
								<th>
									<fmt:message key="label.response" />
								</th>
							</tr>

							<c:forEach var="questionAttemptData"
								items="${currentDto.questionAttempts}">
								<c:forEach var="sData" items="${questionAttemptData.value}">
									<c:set var="userData" scope="request" value="${sData.value}" />
									<c:set var="responseUid" scope="request"
										value="${userData.uid}" />
									<c:set var="userSessionId" scope="request"
										value="${userData.sessionId}" />

									<c:if test="${sessionId == userSessionId}">
										<c:if test="${currentDto.questionUid == userData.questionUid}">

											<tr>
												<td>
													<c:out value="${userData.userName}" />
												</td>
												<td>
													<lams:Date value="${userData.attemptTime}" />
												</td>
												<td>
													<c:if test="${not userData.visible == 'true'}">
														<span style="font: italic;"><fmt:message
																key="label.hidden" /> </span>
													</c:if>
													<c:out value="${userData.responsePresentable}"
														escapeXml="false" />
												</td>
											</tr>

										</c:if>
									</c:if>

								</c:forEach>
							</c:forEach>
						</table>
					</c:forEach>
				</c:forEach>
			</c:if>

			<!--  REFLECTIONS -->

			<c:if test="${not empty reflectionsContainerDTO }">
				<h2>
					<fmt:message key="label.reflection" />
				</h2>

				<c:forEach var="currentDto" items="${reflectionsContainerDTO}">

					<%-- The actual output of a reflection --%>
					<c:set var="userName" scope="request"
						value="${currentDto.userName}" />
					<c:set var="userId" scope="request" value="${currentDto.userId}" />
					<c:set var="sessionId" scope="request"
						value="${currentDto.sessionId}" />
					<c:set var="reflectionUid" scope="request"
						value="${currentDto.reflectionUid}" />
					<c:set var="entry" scope="request" value="${currentDto.entry}" />

					<h4>
						<fmt:message key="label.user" />
						:
						<c:out value="${userName}" escapeXml="false" />
					</h4>
					<p>
						<c:out value="${entry}" escapeXml="false" />
					</p>

				</c:forEach>
			</c:if>

		</c:if>

	</div>
	<!--closes content-->

	<div id="footer"></div>
	<!--closes footer-->

</body>
</lams:html>
