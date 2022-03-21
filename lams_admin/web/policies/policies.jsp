<%@ include file="/taglibs.jsp"%>

<!DOCTYPE html>
<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="admin.policies.title"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>/admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen">
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
	<lams:JSImport src="includes/javascript/dialog.js" />
	<script type="text/javascript">
		$(window).on('load', function(){
			//dialog displaying user consents given for the specified policy
			$(".policy-consents-link").click(function() {
				var policyUid = $(this).data("policy-uid");
				
				var id = "policy-consents-dialog",
				dialog = $('#' + id),
				exists = dialog.length > 0
				url = '<c:url value="/policyManagement/displayUserConsents.do"/>?policyUid=' + policyUid;
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
					"togglePolicyStatus.do?<csrf:token/>", 
					{
						policyUid: policyUid,
						policyId: policyId,
						viewPreviousVersions: "${viewPreviousVersions}"
					},
					function() {}
				);			
			});
	
		});
	</script>
</lams:head>

<body class="stripes">
	<c:set var="help"><lams:help style="small" page="LAMS+Policies" /></c:set>
	<lams:Page type="admin" title="${title}" titleHelpURL="${help}">
		<p>
			<a href="<lams:LAMSURL/>admin/appadminstart.do" class="btn btn-default">
				<fmt:message key="appadmin.maintain" />
			</a>
			<c:if test="${viewPreviousVersions}">
				<a class="btn btn-default" href="<lams:WebAppURL />policyManagement/list.do">
					<fmt:message key="admin.policies.title"/>
				</a>
			</c:if>
		</p>
		
		<div id="policy-table">
			<%@ include file="policyTable.jsp"%>
		</div>
		
		<c:if test="${!viewPreviousVersions}">
			<a class="btn btn-default pull-right" href="<lams:WebAppURL />policyManagement/edit.do">
				<fmt:message key="label.add.new.policy"/>
			</a>
		</c:if>
	</lams:Page>
</body>
</lams:html>
