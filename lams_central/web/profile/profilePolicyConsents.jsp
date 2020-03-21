<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL/></c:set>

<lams:html>
<lams:head>
	<lams:css/>
	<style>
		th, td {
			text-align: center;
		}
	</style>
	
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/profile.js"></script>
	<script type="text/javascript">
		$(document).ready(function () {
			//update dialog's height and title
			updateMyProfileDialogSettings('<fmt:message key="label.policies.consents" />', '80%');
		});
	</script>
</lams:head>

<body>
<div style="clear: both;"></div>
<div class="container">
	<div class="row vertical-center-row">
		<div>
			<div class="panel">
				<div class="panel-body">

					<table class="table table-condensed table-striped" >
						<tr>
							<th><fmt:message key="label.policy.name" /></th>
							<th><fmt:message key="label.policy.type" /></th>
							<th><fmt:message key="label.version" /></th>
							<th><fmt:message key="label.consented" /></th>
							<th><fmt:message key="label.consented.on"/></th>
						</tr>
					
						<c:forEach items="${policyDtos}" var="policyDto">
							<tr>
								<td>
									<a href="displayPolicyDetails.do?policyUid=${policyDto.uid}" target="_new"
											title="<fmt:message key="label.policy.details"/>">
										<c:out value="${policyDto.policyName}" />
									</a>
								</td>
								<td>
									<c:choose>
										<c:when test="${policyDto.policyTypeId == 1}">
											<fmt:message key="label.policy.type.site"/>
										</c:when>
										<c:when test="${policyDto.policyTypeId == 2}">
											<fmt:message key="label.policy.type.privacy"/>
										</c:when>
										<c:when test="${policyDto.policyTypeId == 3}">
											<fmt:message key="label.policy.type.third.party"/>
										</c:when>
										<c:when test="${policyDto.policyTypeId == 4}">
											<fmt:message key="label.policy.type.other"/>
										</c:when>
									</c:choose>
								</td>
								<td>
									<c:out value="${policyDto.version}" />
								</td>
								<td>
									<c:choose>
										<c:when test="${policyDto.consentedByUser}">
											<i class="icon fa fa-check text-success fa-fw" title="Consent given"></i>
										</c:when>
										<c:otherwise>
											<i class="icon fa fa-times text-danger fa-fw" title="Consent not given"></i>
										</c:otherwise>
									</c:choose>
								</td>
								<td>
									<lams:Date value="${policyDto.dateAgreedOn}"/>	
								</td>
							</tr>
						</c:forEach>
					</table>
					
					<div class="pull-right">
						<button type="button" class="btn btn-sm btn-secondary offset5" onclick="history.go(-1);">
							<fmt:message key="label.return.to.myprofile" />
						</button>
					</div>

				</div>
			</div>
		</div>
	</div>
</div>
</body>
</lams:html>
