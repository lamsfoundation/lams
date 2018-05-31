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
				${fn:length(policy.consents)}/${userCount}
			</td>
			<td>
				<html:link page="/policyManagement.do?method=edit&policyUid=${policy.uid}">
					<fmt:message key="admin.edit"/>
				</html:link>
				&nbsp;&nbsp;
				<html:link page="/policyManagement.do?method=delete&policyUid=${policy.uid}">
					<fmt:message key="admin.delete"/>
				</html:link>
				
				<c:if test="${policy.policyStateId == 1 || policy.policyStateId == 3}">
					<c:choose>
						<c:when test="${policy.policyStateId == 1}">
							<c:set var="statusTo"><fmt:message key="label.policy.status.inactive"/></c:set>
							<c:set var="newStatus">3</c:set>
						</c:when>
						<c:when test="${policy.policyStateId == 2}">
							
						</c:when>
						<c:when test="${policy.policyStateId == 3}">
							<c:set var="statusTo"><fmt:message key="label.policy.status.active"/></c:set>
							<c:set var="newStatus">1</c:set>
						</c:when>
					</c:choose>
					
					&nbsp;&nbsp;
					<html:link page="/policyManagement.do?method=changeStatus&policyUid=${policy.uid}&newStatus=${newStatus}">
						<fmt:message key="label.set.status.to">
							<fmt:param value="${statusTo}"/>
						</fmt:message>	
					</html:link>
				</c:if>
				
				<c:if test="${viewPreviousVersions != 'true' && policy.hasPreviousVersions()}">
					&nbsp;&nbsp;
					<html:link page="/policyManagement.do?method=viewPreviousVersions&policyId=${policy.policyId}">
						<fmt:message key="label.view.previous.versions"/>
					</html:link>
				</c:if>		
			</td>
		</tr>
	</c:forEach>
</table>

<c:if test="${viewPreviousVersions != 'true'}">
	<html:link styleClass="btn btn-primary pull-right" page="/policyManagement.do?method=edit">
		<fmt:message key="label.add.new.policy"/>
	</html:link>
</c:if>
