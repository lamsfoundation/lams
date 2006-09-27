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
			<table class="alternative-color">
				<tr>
					<%-- <th class="first"><fmt:message key="label.question"/></th> --%>
					<th colspan="2" class="first"><c:out value="${question.description}" escapeXml="false"/></th>
				</tr>
				<%-- 
				<tr>
					<td colspan="2"><fmt:message key="message.possible.answers"/></td>
				</tr>
				--%>
				<c:forEach var="option" items="${question.options}" varStatus="status">
					<tr>
						<td style="width:50px">
							<%= SurveyConstants.OPTION_SHORT_HEADER %>${status.count}
						</td>
						<td>
							${option.description}
						</td>
					</tr>
				</c:forEach>
				<c:if test="${question.appendText}">
					<tr>
						<td colspan="2">
							<fmt:message key="label.open.response"/>
						</td>
					</tr>
				</c:if>
			</table>
			<div align="center">
			<img src="<c:url value="/showChart.do?chartType=${chartType}&toolSessionID="/>${toolSessionID}&questionUid=${question.uid}" 
							title="<fmt:message key="message.view.pie.chart"/>">
			</div>
		</div>
		<div id="footer-learner"></div>
		</div>
	</div>
</body>
</html>
