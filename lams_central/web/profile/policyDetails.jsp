<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL/></c:set>

<lams:html>
<lams:head>
	<title><fmt:message key="label.policy.details" /></title>
	<link rel="icon" href="/lams/favicon.ico" type="image/x-icon" />
	<link rel="shortcut icon" href="/lams/favicon.ico" type="image/x-icon" />
	<link rel="stylesheet" href="${lams}css/components.css">
    <link rel="stylesheet" href="${lams}includes/font-awesome6/css/all.css">
    
    <script type="text/javascript" src="${lams}includes/javascript/bootstrap5.bundle.min.js"></script>
</lams:head>

<body>
<div style="clear: both;"></div>
<div class="container">
<div>
<div class="panel panel-default">
	<div class="panel-heading">
		<div class="panel-title">
			<h4><fmt:message key="label.policy.details" /></h4>
		</div>
	</div>

<div class="panel-body">		
	<table class="table table-striped table-no-border">
		<tr>
			<td><fmt:message key="label.name" /></td>
			<td><c:out value="${policy.policyName}" /></td>
		</tr>
		<tr>
			<td style="width: 250px;"><fmt:message key="label.policy.type" /></td>
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
		</tr>
		<tr>
			<td><fmt:message key="label.version" /></td>
			<td><c:out value="${policy.version}" escapeXml="false"/></td>
		</tr>
		<tr>
			<td><fmt:message key="label.summary" /></td>
			<td>
				<c:out value="${policy.summary}" escapeXml="false"/>
			</td>
		</tr>
		<tr>
			<td><fmt:message key="label.full.policy" /></td>
			<td>
				<c:out value="${policy.fullPolicy}" escapeXml="false"/>
			</td>
		</tr>
	</table>
					
	<div class="float-end">
		<button class="btn btn-light mt-2" type="button" onclick="javascript:window.close();">
			<i class="fa-solid fa-rotate-left me-1"></i>
			<fmt:message key="label.authoring.close" />
		</button>
	</div>

</div>
</div>
</div>
</div>
</body>
</lams:html>
