<!DOCTYPE html>

<%@ page contentType="text/html; charset=utf-8" language="java"%>

<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-bean" prefix="bean"%>
<%@ taglib uri="tags-logic" prefix="logic"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<lams:html>
<lams:head>
	<lams:css/>
	<style>
		th, td {
			text-align: center;
		}
	</style>
	
	<script type="text/javascript" src="includes/javascript/getSysInfo.js"></script>
	<script type="text/javascript" src="loadVars.jsp"></script>
	<script type="text/javascript" src="includes/javascript/openUrls.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="includes/javascript/profile.js"></script>
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
									<a href="profile/displayPolicyDetails.do?policyUid=${policyDto.uid}" target="_new"
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
						<input class="btn btn-sm btn-default offset5" type="button"
							value="<fmt:message key="label.return.to.myprofile" />"
							onclick="javascript:document.location='index.do?method=profile'" />
					</div>

				</div>
			</div>
		</div>
	</div>
</div>
</body>
</lams:html>
