<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<c:set var="tool"><lams:WebAppURL/></c:set>
<%@ page import="org.lamsfoundation.lams.tool.survey.SurveyConstants"%>
<lams:html>
<lams:head>
	    <%@ include file="/common/header.jsp" %>
</lams:head>
<body>
		<div id="content">
		<h1>
			<fmt:message key="title.chart.report"/>
		</h1>

			<c:forEach var="entry" items="${answerList}" varStatus="status">
				<c:set var="user" value="${entry.key}"/>
				<c:set var="question" value="${entry.value}"/>
				<%--  display question header  --%>
				<c:if test="${status.first}">
					<h2><fmt:message key="label.question"/></h2>
					<table  class="alternative-color" cellspacing="0">
					<tr>
						<%-- <td><fmt:message key="label.question"/></td>--%>
						<th colspan="2" class="first"><c:out value="${question.description}" escapeXml="false"/></th>
					</tr>
					<%-- 
					<tr>
						<td colspan="2"><b><fmt:message key="message.possible.answers"/></b></td>
					</tr>
					--%>
					<c:forEach var="option" items="${question.options}" varStatus="optStatus">
						<tr>
							<td  width="50px">
								<%= SurveyConstants.OPTION_SHORT_HEADER %>${optStatus.count}
							</td>
							<td>
								<c:out value="${option.description}" escapeXml="true"/>
							</td>
						</tr>
					</c:forEach>
					<c:if test="${question.appendText ||question.type == 3}">
						<tr>
							<td  width="50px">
								<fmt:message key="label.open.response"/>
							</td>
							<td>&nbsp;</td>
						</tr>
					</c:if>
					<tr>
					</table>
				<%--  End first check  --%>
				</c:if>
				<c:if test="${status.first}">
					<h2><fmt:message key="label.answer"/></h2>
					<div align="center">
						<table class="alternative-color" cellspacing="0">
							<tr>
								<th class="first"><fmt:message key="label.learner"/></th>
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
				</c:if>
						<%--  User answer list --%>
						<tr>
							<td>${user.loginName}</td>
							<c:forEach var="option" items="${question.options}">
								<td>
									<c:if test="${not empty question.answer}">
										<c:set var="checked" value="false"/>
										<c:forEach var="choice" items="${question.answer.choices}">
											<c:if test="${choice == option.uid}">
												<c:set var="checked" value="true"/>
											</c:if>
										</c:forEach>
										<c:if test="${checked}">
											<img src="${tool}/includes/images/tick_red.gif" title="<fmt:message key="message.learner.choose.answer"/>">
										</c:if>
									</c:if>
									&nbsp;
								</td>
							</c:forEach>
							<c:if test="${question.appendText ||question.type == 3}">
								<td>
									<c:if test="${not empty question.answer}">
										<lams:out value="${question.answer.answerText}" escapeHtml="true"/>
									</c:if>
									&nbsp;
								</td>
							</c:if>
						</tr>
					<c:if test="${status.last}">
						</table>
						</div>
					<%--  End first check  --%>
					</c:if>
				</c:forEach>
		</div>
		<div id="learner"></div>
		</div>
	</div>
</body>
</lams:html>
