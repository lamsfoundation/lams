<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<%@ include file="/common/header.jsp" %>
</lams:head>

<body class="stripes">

	<c:set var="title"><fmt:message key="label.monitoring.heading.access" /></c:set>
	<lams:Page type="learner" title="${title}">
		
		<table class="table table-condensed">
			<tr>
				<th>
					<fmt:message key="monitoring.label.access.time" />
				</th>
				<th>
					<fmt:message key="monitoring.label.user.name" />
				</th>
			</tr>
			
			<c:forEach var="user" items="${userList}">
				<tr>
					<td>
						<lams:Date value="${user.accessDate}"/>
					</td>
					<td>
						<c:out value="${user.firstName},${user.lastName}" escapeXml="true"/>
					</td>
				</tr>
			</c:forEach>
		</table>
		
		<a href="#nogo" onclick="javaqscript:window.close()" class="btn btn-primary">
			<fmt:message key="button.close" />
		</a>
	</lams:Page>
	
	<div id="footer"></div>
</body>
</lams:html>
