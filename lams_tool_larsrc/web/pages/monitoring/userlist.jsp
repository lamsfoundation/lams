<%@ include file="/common/taglibs.jsp"%>
<htm>
<head>
	    <%@ include file="/common/header.jsp" %>
</head>
<body>
	<table border="0" cellspacing="3" width="98%">
		<tr>
			<th>
				<fmt:message key="monitoring.label.user.loginname" />
			</th>
			<th>
				<fmt:message key="monitoring.label.user.name" />
			</th>
		</tr>
		<c:forEach var="user" items="${userList}">
			<tr>
				<td>
					${user.loginName}
				</td>
				<td>
					${user.firstName},${user.lastName}
				</td>
			</tr>
		</c:forEach>
		<tr>
			<td colspan="2" align="right">
				<a href="window.close()" style="button">Close</a>
			</td>
		</tr>
	</table>
</body>
</html>
