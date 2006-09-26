<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">


<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.survey.SurveyConstants"%>
<html>
<head>
	    <%@ include file="/common/header.jsp" %>
</head>
<body>
	<div id="page-learner">
		<h1 class="no-tabs-below">
			<fmt:message key="title.chart.report"/>
		</h1>
		<div id="header-no-tabs-learner">
		</div>
		<div id="content-learner">
			<table border="0" cellspacing="3" width="98%">
				<tr>
					<td><fmt:message key="label.question"/></td>
					<td><c:out value="${question.description}" escapeXml="false"/></td>
				</tr>
				<tr>
					<td colspan="2"><fmt:message key="message.possible.answers"/></td>
				</tr>
				<c:forEach var="option" items="${question.options}" varStatus="status">
					<tr>
						<td>
							<%= SurveyConstants.OPTION_SHORT_HEADER %>${status.count}
						</td>
						<td>
							${option.description}
						</td>
					</tr>
				</c:forEach>
				<c:if test="${question.appendText}">
					<tr>
						<td>
							<fmt:message key="label.open.response"/>
						</td>
						<td></td>
					</tr>
				</c:if>
				<tr>
					<td colspan="2">
						<img src="<c:url value="/showChart.do?chartType=${chartType}&toolSessionID="/>${toolSessionID}&questionUid=${question.uid}" 
							title="<fmt:message key="message.view.pie.chart"/>">
					</td>
				</tr>
			</table>
		</div>
		<div id="footer-learner"></div>
		</div>
	</div>
</body>
</html>
