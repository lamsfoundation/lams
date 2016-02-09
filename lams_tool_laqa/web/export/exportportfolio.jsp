<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<title><fmt:message key="label.exportPortfolio" />
	</title>
	<lams:css localLinkPath="../" />

	<script type="text/javascript"> 
		var pathToImageFolder = "./images/css/";
	</script>
	<script type="text/javascript" src="./javascript/jquery.js"></script>
</lams:head>

<body class="stripes">

	<div id="content">

		<h1><c:out value="${generalLearnerFlowDTO.activityTitle}" escapeXml="true"/> </h1>
			
		<h1><c:out value="${generalLearnerFlowDTO.activityInstructions}" escapeXml="false"/> </h1>

		<c:if test="${(portfolioExportMode != 'learner') && !isToolSessionAvailable}">
			<p>
				<fmt:message key="error.noLearnerActivity" />
			</p>
		</c:if>

		<!-- LEARNER EXPORT -->

		<c:if test="${isToolSessionAvailable}">

			<c:if test="${(portfolioExportMode == 'learner')}">

				<c:forEach var="currentDto" items="${generalLearnerFlowDTO.listMonitoredAnswersContainerDTO}">

					<div>
						<p>
							<strong> <fmt:message key="label.question" /> : </strong>
							<c:out value="${currentDto.question}" escapeXml="false" />
						</p>
						
						<c:if test="${not empty currentDto.feedback}">
							<p>
								<strong> <fmt:message key="label.feedback" /> : </strong>
								<c:out value="${currentDto.feedback}" escapeXml="false" />
							</p>
						</c:if>

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

							<c:forEach var="questionAttemptData" items="${currentDto.questionAttempts}">

								<c:forEach var="sData" items="${questionAttemptData.value}">

									<c:set var="userData" scope="request" value="${sData.value}" />
									<c:set var="responseUid" scope="request" value="${userData.uid}" />

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
														<c:out value="${userData.responsePresentable}" escapeXml="true" />
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
					<c:set var="sessionId" scope="request" value="${groupDto.sessionId}" />
					<c:set var="sessionName" scope="request" value="${groupDto.sessionName}" />
					<c:set var="groupData" scope="request" value="${groupDto.groupData}" />

					<h2>
						<fmt:message key="group.label" />
						:
						<c:out value="${sessionName}" />
					</h2>

					<c:forEach var="currentDto" items="${groupData}">

						<strong> <fmt:message key="label.question" /> : </strong>
						<c:out value="${currentDto.question}" escapeXml="false" />
						
						<c:if test="${not empty currentDto.feedback}">
							<strong> <fmt:message key="label.feedback" /> : </strong>
							<c:out value="${currentDto.feedback}" escapeXml="true" />
						</c:if>

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

							<c:forEach var="questionAttemptData" items="${currentDto.questionAttempts}">
								<c:forEach var="sData" items="${questionAttemptData.value}">
									<c:set var="userData" scope="request" value="${sData.value}" />
									<c:set var="responseUid" scope="request" value="${userData.uid}" />
									<c:set var="userSessionId" scope="request" value="${userData.sessionId}" />

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
					<c:set var="userName" scope="request" value="${currentDto.userName}" />
					<c:set var="userId" scope="request" value="${currentDto.userId}" />
					<c:set var="sessionId" scope="request" value="${currentDto.sessionId}" />
					<c:set var="reflectionUid" scope="request" value="${currentDto.reflectionUid}" />
					<c:set var="entry" scope="request" value="${currentDto.entry}" />

					<h4>
						<fmt:message key="label.user" />
						:
						<c:out value="${userName}" escapeXml="true" />
					</h4>
					<p>
						<lams:out value="${entry}" escapeHtml="true" />
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
