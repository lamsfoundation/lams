<%@ include file="/taglibs.jsp"%>

<c:if test="${empty monitors}">
	<p><fmt:message key="message.no.monitors" /></p>
</c:if>

<c:if test="${not empty monitors}">
	<p><fmt:message key="message.check.to.add.monitor" /></p>

	<table class="alternative-color">
		<tr>
			<th></th>
			<th><fmt:message key="admin.user.login" /></th>
			<th><fmt:message key="admin.user.name" /></th>
			<th><fmt:message key="admin.user.email" /></th>
		</tr>
		<c:forEach items="${monitors}" var="user">
			<tr>
				<td>
					<input id="staff" name="staff" type="checkbox" value="<c:out value="${user.userId}" />" checked="checked" />
				</td>
				<td>
					<c:out value="${user.login}" />
				</td>
				<td>
					<c:out value="${user.title}" /> <c:out value="${user.firstName}" /> <c:out value="${user.lastName}" />
				</td>
				<td>
					<c:out value="${user.email}" />
				</td>
			</tr>
		</c:forEach>
	</table>
</c:if>