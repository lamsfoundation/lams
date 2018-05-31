<%@ include file="/taglibs.jsp"%>

<p>
	<a href="<lams:LAMSURL/>/admin/sysadminstart.do" class="btn btn-default">
		<fmt:message key="sysadmin.maintain" />
	</a>
</p>

<c:if test="${not empty error}">
	<lams:Alert type="warn" id="errorMessage" close="false">	
		<c:out value="${error}" />
	</lams:Alert>
</c:if>

<table class="table table-striped table-condensed" >
	<tr>
		<th><fmt:message key="label.name" /></th>
		<th><fmt:message key="label.policy.status" /></th>
		<th><fmt:message key="label.policy.type" /></th>
		<th><fmt:message key="label.version" /></th>
		<th><fmt:message key="label.last.modified" /></th>
		<th><fmt:message key="label.agreements"/></th>
		<th><fmt:message key="admin.actions"/></th>
	</tr>
	<c:forEach items="${policies}" var="policy">
		<tr>
			<td>
				<c:out value="${policy.policyName}" />
			</td>
			<td>
				<c:choose>
					<c:when test="${policy.policyStateId == 1}">
						<fmt:message key="label.policy.status.active"/>
					</c:when>
					<c:when test="${policy.policyStateId == 2}">
						<fmt:message key="label.policy.status.inactive"/>
					</c:when>
					<c:when test="${policy.policyStateId == 3}">
						<fmt:message key="label.policy.status.draft"/>
					</c:when>
				</c:choose>
			</td>
			<td>
				<c:choose>
					<c:when test="${policy.policyTypeId == 1}">
						<fmt:message key="label.policy.type.site"/>
					</c:when>
					<c:when test="${policy.policyTypeId == 2}">
						<fmt:message key="label.policy.type.privacy"/>
					</c:when>
					<c:when test="${policy.policyTypeId == 3}">
						<fmt:message key="label.policy.type.third.party"/>
					</c:when>
					<c:when test="${policy.policyTypeId == 4}">
						<fmt:message key="label.policy.type.other"/>
					</c:when>
				</c:choose>
			</td>
			<td>
				<c:out value="${policy.version}" />
			</td>
			<td>
				<c:out value="${policy.lastModified}" />
			</td>
			<td>
				Consents
			</td>
			<td>
				<html:link page="/signupManagement.do?method=edit&policyUid=${policy.uid}">
					<fmt:message key="admin.edit"/>
				</html:link>
				&nbsp;&nbsp;
				<html:link page="/signupManagement.do?method=delete&policyUid=${policy.uid}">
					<fmt:message key="admin.delete"/>
				</html:link>
				
			</td>
		</tr>
	</c:forEach>
</table>

