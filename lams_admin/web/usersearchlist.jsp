<%@ include file="/taglibs.jsp"%>

<script language="JavaScript" type="text/javascript">
	function resetFields() {
		document.UserSearchForm.sUserId.value = '';
		document.UserSearchForm.sLogin.value = '';
		document.UserSearchForm.sFirstName.value = '';
		document.UserSearchForm.sLastName.value = '';
	}
</script>

<h2>
	<a href="sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a> 
	: <fmt:message key="admin.user.find"/>
</h2>

<logic:notEqual name="isSysadmin" value="false">
<html-el:form action="/usersearch.do" method="post">
<html-el:hidden property="searched" />
<div align="center">&nbsp;<html-el:errors /><html-el:messages id="results" message="true"><bean:write name="results" /></html-el:messages></div>
<table class="alternative-color" cellspacing="0">
<tr>
	<th><fmt:message key="admin.user.userid"/></th>
	<th><fmt:message key="admin.user.login"/></th>
	<th><fmt:message key="admin.user.first_name"/></th>
	<th><fmt:message key="admin.user.last_name"/></th>
	<th><fmt:message key="admin.user.actions"/></th>
</tr>
<tr>
	<td><html-el:text property="sUserId" size="5" /></td>
	<td><html-el:text property="sLogin" size="10" /></td>
	<td><html-el:text property="sFirstName" size="10" /></td>
	<td><html-el:text property="sLastName" size="10" /></td>
	<td></td>
</tr>

<tr>
	<td colspan=5>
		<html:checkbox property="showAll">&nbsp;<fmt:message key="label.show.all.users"/></html:checkbox>&nbsp;&nbsp;&nbsp;
		<html:select property="resultsSection" style="width:40px">
			<html:option value="10 ">10</html:option>
			<html:option value="20 ">20</html:option>
			<html:option value="30 ">30</html:option>
			<html:option value="40 ">40</html:option>
			<html:option value="50 ">50</html:option>
			<html:option value="all">All</html:option>
		</html:select>&nbsp;<fmt:message key="label.results.per.page"/>
	</td>
<tr>
	<td colspan=5 align="center">
		<html-el:submit styleClass="button"><fmt:message key="admin.search"/></html-el:submit>
		<input class="button" type="button" value='<fmt:message key="admin.reset"/>' onclick="resetFields();" />
	</td>
</tr>

<logic:notEmpty name="userList">
	<tr>
		<td colspan=5><c:out value="${fullSize}"/> users found.</td>
	</tr>
	<c:forEach var="user" items="${userList}" begin="${start}" end="${start+resultsSection-1}" varStatus="status">
		<tr>
			<td>
				<c:out value="${user.userId}"/>
			</td>
			<td>
				<c:out value="${user.login}"/>
			</td>
			<td>
				<c:out value="${user.firstName}"/>
			</td>
			<td>
				<c:out value="${user.lastName}"/>
			</td>
			<td>
				<a href="user.do?method=edit&userId=<c:out value="${user.userId}"/>"><fmt:message key="admin.edit"/></a>
				&nbsp;
				<a href="user.do?method=remove&userId=<c:out value="${user.userId}"/>"><fmt:message key="admin.user.delete"/></a>
			</td>
		</tr>
	</c:forEach>
	<tr>
		<td colspan=5 align="center">
			<c:forEach var="index" varStatus="status" begin="0" end="${fullSize-1}" step="${resultsSection}">
				<c:if test="${start == index}"><c:out value="${status.count}"/>&nbsp;</c:if>
				<c:if test="${start != index}">
					<c:url var="pageLink" value="usersearch.do">
						<c:param name="start" value="${index}" />
						<c:param name="uid" value="${uid}" />
						<c:param name="l" value="${login}" />
						<c:param name="fn" value="${firstName}" />
						<c:param name="ln" value="${lastName}" />
						<c:param name="sa" value="${showAll}" />
						<c:param name="rs" value="${resultsSection}" />
					</c:url>
					<a href="<c:out value="${pageLink}"/>"><c:out value="${status.count}"/></a>&nbsp;
				</c:if>
			</c:forEach>
		</td>
	</tr>
</logic:notEmpty>

</table>
</html-el:form>
</logic:notEqual>