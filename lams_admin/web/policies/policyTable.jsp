<%@ include file="/taglibs.jsp"%>

<table class="table table-striped table-condensed" >
	<tr>
		<th><fmt:message key="label.name" /></th>
		<th><fmt:message key="label.policy.status" /></th>
		<th><fmt:message key="label.policy.type" /></th>
		<th><fmt:message key="label.version" /></th>
		<th><fmt:message key="label.last.modified" /></th>
		<th><fmt:message key="label.consents"/></th>
		<th colspan="3"><fmt:message key="admin.actions"/></th>
	</tr>
	<c:forEach items="${policies}" var="policy">
		<tr>
			<td>
				<c:out value="${policy.policyName}" />
			</td>
			<td>
				<c:choose>
					<c:when test="${policy.policyStateId == 1}">
						<span class="label label-success">
							<fmt:message key="label.policy.status.active"/>
						</span>
					</c:when>
					<c:otherwise>
						<span class="label label-warning">
							<fmt:message key="label.policy.status.inactive"/>
						</span>
					</c:otherwise>
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
				<lams:Date value="${policy.lastModified}"/>
			</td>
			<td>
				<a href="#nogo" class="policy-consents-link" data-policy-uid="${policy.uid}">
					${fn:length(policy.consents)}/${userCount}
				</a>
			</td>
			
			<!-- Actions columns -->
			<td>
				<a href="<lams:WebAppURL />policyManagement/edit.do?policyUid=${policy.uid}">
					<fmt:message key="admin.edit"/>
				</a>
			</td>
						
			<td>
				<c:if test="${!viewPreviousVersions && policy.hasPreviousVersions()}">
					<a href="<lams:WebAppURL />policyManagement/viewPreviousVersions.do?policyId=${policy.policyId}">
						<fmt:message key="label.view.previous.versions"/>
					</a>
				</c:if>	
			</td>

			<td>
				<a href="#nogo" class="change-status-link" data-policy-uid="${policy.uid}" data-policy-id="${policy.policyId}" data-policy-state="${policy.policyStateId}">
					<c:choose>
						<c:when test="${policy.policyStateId == 1}">
							<fmt:message key="label.deactivate"/>
						</c:when>
						<c:otherwise>
							<fmt:message key="label.activate"/>				
						</c:otherwise>
					</c:choose>
				</a>
			</td>
			<!-- End of Actions columns -->
				
		</tr>
	</c:forEach>
</table>
