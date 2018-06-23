<%@ include file="/taglibs.jsp"%>

<!DOCTYPE html>
<lams:html>
<lams:head>
	<lams:css/>
	<lams:css suffix="learner"/>
	<link rel="stylesheet" href="<lams:LAMSURL/>/admin/css/admin.css" type="text/css" media="screen">
	<style>
		th, td {
			text-align: center;
		}
	</style>
</lams:head>

<body>
<div class="row no-gutter no-margin">
<div class="col-xs-12">
<div class="container" id="content">

	<h4>
		<c:out value="${policy.policyName}" />
	</h4>
	
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
	<br/>
	<fmt:message key="label.version" />: <c:out value="${policy.version}" />
	<br/><br/>
	
	<table class="table table-striped table-condensed" >
		<tr>
			<th><fmt:message key="admin.user.first_name" />&nbsp;<fmt:message key="admin.user.last_name" /></th>
			<th><fmt:message key="label.consented" /></th>
			<th><fmt:message key="label.consented.on"/></th>
		</tr>
		<c:forEach items="${userPolicyConsents}" var="userPolicyConsent">
			<tr>
				<td>
					<c:out value="${userPolicyConsent.firstName}" /> <c:out value="${userPolicyConsent.lastName}" />
				</td>
				<td>
					<c:choose>
						<c:when test="${userPolicyConsent.consentGivenByUser}">
							<i class="icon fa fa-check text-success fa-fw" title="Consent given"></i>
						</c:when>
						<c:otherwise>
							<i class="icon fa fa-times text-danger fa-fw" title="Consent not given"></i>
						</c:otherwise>
					</c:choose>
				</td>
				<td>
					<c:choose>
						<c:when test="${userPolicyConsent.dateAgreedOn != null}">
							<lams:Date value="${userPolicyConsent.dateAgreedOn}"/>
						</c:when>
						<c:otherwise>
							-
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
		</c:forEach>
	</table>

</div>
</div>
</div>
</body>
</lams:html>