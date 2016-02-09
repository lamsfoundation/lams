<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.survey.SurveyConstants"%>
<c:set var="tool"><lams:WebAppURL/></c:set>
<c:set var="sessionMapID" value="${param.sessionMapID}"/>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<lams:html>
<lams:head>
	<%@ include file="/common/header.jsp" %>
</lams:head>

<body class="stripes">
		<div id="content">
		<h1>
			<c:out value="${sessionMap.title}"/>
		</h1>

			<c:forEach var="sessionEntry" items="${sessionMap.summaryList}" varStatus="sessionStatus">
				<c:set var="toolSession" value="${sessionEntry.key}"/>
				<c:set var="questionList" value="${sessionEntry.value}"/>
				<%-- only display question list once --%>
				<c:if test="${sessionStatus.first}">
					<h1><fmt:message key="label.authoring.basic.survey.list.title"/></h1>
					<c:forEach var="qe" items="${questionList}" varStatus="qeStatus">
						<c:set var="qe" value="${qe.key}"/>
							<table  class="alternative-color" cellspacing="0">
								<tr>
									<%-- <td><fmt:message key="label.question"/></td>--%>
									<th colspan="2" class="first"><fmt:message key="label.question"/> ${qeStatus.count} : <c:out value="${qe.description}" escapeXml="false"/></th>
								</tr>
								<c:forEach var="optionTitle" items="${qe.options}" varStatus="optTitleStatus">
									<tr>
										<td width="100px">
											<%= SurveyConstants.OPTION_SHORT_HEADER %>${optTitleStatus.count}
										</td>
										<td>
											<c:out value="${optionTitle.description}" escapeXml="false"/>
										</td>
									</tr>
								</c:forEach>
								<c:if test="${qe.appendText ||qe.type == 3}">
									<tr>
										<td>
											<fmt:message key="label.open.response"/>
										</td>
										<td>&nbsp;</td>
									</tr>
								</c:if>
								<tr>
							</table>
					</c:forEach>
				</c:if>
				<h1>${toolSession.sessionName}</h1>
				<c:forEach var="entry" items="${questionList}" varStatus="questionStatus">
					<c:set var="question" value="${entry.key}"/>
					<c:set var="answers" value="${entry.value}"/>
					<%-- only display session name once for each session group--%>
					<%-- Display question header --%>
					<table class="alternative-color" cellspacing="0">
						<tr>
							<th class="first" width="150px"><fmt:message key="label.question"/> ${questionStatus.count}</th>
							<c:forEach var="option" items="${question.options}" varStatus="optStatus">
								<th>
									<%= SurveyConstants.OPTION_SHORT_HEADER %>${optStatus.count}
								</th>
							</c:forEach>
							<c:if test="${question.appendText || question.type == 3}">
								<th>
									<fmt:message key="label.open.response"/>
								</th>
							</c:if>
						</tr>
					<c:forEach var="answer" items="${answers}">
						<%--  User answer list --%>
						<tr>
							<td><c:out value="${answer.replier.loginName}"/></td>
							<c:forEach var="option" items="${question.options}">
								<td>
									<c:if test="${not empty answer.answer}">
										<c:set var="checked" value="false"/>
										<c:forEach var="choice" items="${answer.answer.choices}">
											<c:if test="${choice == option.uid}">
												<c:set var="checked" value="true"/>
											</c:if>
										</c:forEach>
										<c:if test="${checked}">
											Y
										</c:if>
									</c:if>
									&nbsp;
								</td>
							</c:forEach>
							<c:if test="${question.appendText ||question.type == 3}">
								<td>
									<c:if test="${not empty answer.answer}">
										<lams:out value="${answer.answer.answerText}"/>
									</c:if>
									&nbsp;
								</td>
							</c:if>
						</tr>
						<%-- End all answers for this question --%>
						</c:forEach>
						</table>
						<%-- End question table --%>
					</c:forEach>
					
					<c:if test="${sessionMap.reflectOn}">
						<%-- End all answers for this question --%>
						<h3><fmt:message key="label.export.reflection" /></h3>
						<c:set var="reflectDTOSet" value="${sessionMap.reflectList[toolSession.sessionId]}" />
						<c:forEach var="reflectDTO" items="${reflectDTOSet}">
							<h4><c:out value="${reflectDTO.fullName}"/></h4>
							<p>
								<lams:out value="${reflectDTO.reflect}" escapeHtml="true" /> 
							</p>						
						</c:forEach>
					</c:if>
					
				<%-- End session table --%>
			</c:forEach>
		<div id="footer"></div>
		</div>
</body>
</lams:html>
