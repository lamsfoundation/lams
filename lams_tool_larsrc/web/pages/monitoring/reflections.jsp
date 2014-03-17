<%@ include file="/common/taglibs.jsp"%>

<br>
<h2>
	<fmt:message key="title.reflection"/>
</h2>

<table class="forms">

	<tr>
		<th style="width: 20%">
			<fmt:message key="monitoring.user.fullname"/>
		</th>
		<th style="width: 20%">
			<fmt:message key="monitoring.label.user.loginname"/>
		</th>
		<th>
			<fmt:message key="monitoring.user.reflection"/>
		</th>
	</tr>
										
	<c:forEach var="reflectDTO" items="${sessionMap.reflectList}">
		<tr>
			<td>
				${reflectDTO.fullName}
			</td>
			<td>
				${reflectDTO.loginName}
			</td>
			<td>
				<lams:out value="${reflectDTO.reflect}" escapeHtml="true" />
			</td>
		</tr>
	</c:forEach>

</table>
<br>
