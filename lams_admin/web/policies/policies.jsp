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
				'height' : 500,
				'width'  : 600,
				'title'  : "<fmt:message key='label.user.consents'/>",
				'open'   : function() {
					$('iframe', this).attr('src', url);
				}
			}, true, exists);
		});

		//handler for "change-status" links
		$(document).on("click", ".change-status-link", function() {
			var policyUid = $(this).data("policy-uid");
			var policyId = $(this).data("policy-id");
			
			$("#policy-table").load(
				"policyManagement.do", 
				{
					method: "togglePolicyStatus",
					policyUid: policyUid,
					policyId: policyId,
					viewPreviousVersions: "${viewPreviousVersions}"
				},
				function() {}
			);			
		});

	});
</script>

<p>
	<a href="<lams:LAMSURL/>/admin/sysadminstart.do" class="btn btn-default">
		<fmt:message key="sysadmin.maintain" />
	</a>
	<c:if test="${viewPreviousVersions}">
		<html:link styleClass="btn btn-default" page="/policyManagement.do?method=list">
			<fmt:message key="admin.policies.title"/>
		</html:link>
	</c:if>
</p>

<div id="policy-table">
	<%@ include file="policyTable.jsp"%>
</div>

<c:if test="${!viewPreviousVersions}">
	<html:link styleClass="btn btn-default pull-right" page="/policyManagement.do?method=edit">
		<fmt:message key="label.add.new.policy"/>
	</html:link>
</c:if>
