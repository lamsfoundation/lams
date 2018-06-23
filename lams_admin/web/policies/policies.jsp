<%@ include file="/taglibs.jsp"%>
<style>
	th, td {
		text-align: center;
	}
	a {
		white-space: nowrap;
	}
</style>

<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.min.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/dialog.js"></script>
<script type="text/javascript">
	$(window).load(function(){
		//dialog displaying user consents given for the specified policy
		$(".policy-consents-link").click(function() {
			var policyUid = $(this).data("policy-uid");
			
			var id = "policy-consents-dialog",
			dialog = $('#' + id),
			exists = dialog.length > 0
			url = '<c:url value="/policyManagement.do"/>?method=displayUserConsents&policyUid=' + policyUid;
			showDialog(id, {
				//'resizable' : false,
				'data' : {
					'orgID'  : "orgID"
				},
				'height' : 600,
				'width'  : 600,
				'title'  : "<fmt:message key='label.user.consents'/>",
				'open'   : function() {
					$('iframe', this).attr('src', url);
				}
			}, true, exists);
		});
	});
</script>

<p>
	<a href="<lams:LAMSURL/>/admin/sysadminstart.do" class="btn btn-default">
		<fmt:message key="sysadmin.maintain" />
	</a>
	<c:if test="${viewPreviousVersions == 'true'}">
		<html:link styleClass="btn btn-default" page="/policyManagement.do?method=list">
			<fmt:message key="admin.policies.title"/>
		</html:link>
	</c:if>
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
		<th><fmt:message key="label.consents"/></th>
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
			<td style="text-align: left;">
			
				<c:choose>
					<c:when test="${viewPreviousVersions == 'true'}">
						<html:link page="/policyManagement.do?method=edit&policyUid=${policy.uid}&isEditingPreviousVersion=true">
							<fmt:message key="label.create.new.draft"/>
						</html:link>
					</c:when>
					<c:otherwise>
						<html:link page="/policyManagement.do?method=edit&policyUid=${policy.uid}">
							<fmt:message key="admin.edit"/>
						</html:link>
						
						<c:choose>
							<c:when test="${policy.policyStateId == 1}">
								<c:set var="statusTo"><fmt:message key="label.policy.status.inactive"/></c:set>
								<c:set var="newStatus">2</c:set>
							</c:when>
							<c:otherwise>
								<c:set var="statusTo"><fmt:message key="label.policy.status.active"/></c:set>
								<c:set var="newStatus">1</c:set>						
							</c:otherwise>
						</c:choose>
							
						&nbsp;&nbsp;
						<html:link page="/policyManagement.do?method=changeStatus&policyUid=${policy.uid}&newStatus=${newStatus}">
							<fmt:message key="label.set.status.to">
								<fmt:param value="${statusTo}"/>
							</fmt:message>	
						</html:link>
						
						<c:if test="${policy.hasPreviousVersions()}">
							&nbsp;&nbsp;
							<html:link page="/policyManagement.do?method=viewPreviousVersions&policyId=${policy.policyId}">
								<fmt:message key="label.view.previous.versions"/>
							</html:link>
						</c:if>	
										
					</c:otherwise>
				</c:choose>
	
			</td>
		</tr>
	</c:forEach>
</table>

<c:if test="${viewPreviousVersions != 'true'}">
	<html:link styleClass="btn btn-default pull-right" page="/policyManagement.do?method=edit">
		<fmt:message key="label.add.new.policy"/>
	</html:link>
</c:if>
