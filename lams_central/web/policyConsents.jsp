<%@ include file="/taglibs.jsp"%>

<c:if test="${not empty error}">
	<lams:Alert type="warn" id="errorMessage" close="false">	
		<c:out value="${error}" />
	</lams:Alert>
</c:if>

<table class="table table-striped table-condensed" >
	<c:forEach items="${policies}" var="policy">
		<tr>
			<td>
				<b>
					<c:out value="${policy.policyName}" />
				</b>
				<br>
				
				<fmt:message key="label.policy.type" />: 
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
				<br>

				<fmt:message key="label.version" />: <c:out value="${policy.version}" />
				<br>
				
				<fmt:message key="label.summary" />: <c:out value="${policy.summary}" />
				<br>
				
				<fmt:message key="label.full.policy" />: <c:out value="${policy.fullPolicy}" />
				
			</td>
		</tr>
	</c:forEach>
</table>

<html:link styleClass="btn btn-primary pull-right" page="/policyConsents.do?method=consent">
	<fmt:message key="label.consent"/>
</html:link>
