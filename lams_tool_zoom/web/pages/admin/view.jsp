<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<c:set var="lams">
		<lams:LAMSURL />
	</c:set>
	<c:set var="tool">
		<lams:WebAppURL />
	</c:set>

	<lams:head>
		<title>
			<fmt:message key="activity.title" />
		</title>

		<lams:css/>
		<style type="text/css" scoped="scoped">
			.api {
				margin-top: 10px;
			}

			.api .control {
				padding-top: 7px;
				cursor: pointer;
			}

			.api:last-child .control {
				visibility: hidden;
			}

			.api:nth-last-child(2) .down, .api:first-child .up {
				visibility: hidden !important;
			}
		</style>

		<lams:JSImport src="includes/javascript/common.js" />
		<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
		<script type="text/javascript">
			function removeApi(elem) {
				$(elem).closest('.api').remove();
			}

			function validateApi(elem){
				var valid = true,
						api = $(elem).closest('.api');
				if (api.is(':last-child')) {
					// set this api as a real one
					api.clone().appendTo(api.parent()).find('input').val(null);
				}
				$('input', api).each(function(){
					valid &= $(this).val().trim() != '';
				});
				if (valid) {
					// api has content now, so it is valid
					api.removeClass('has-error');
				} else if (!api.is(':last-child')) {
					// mark empty api as invalid
					api.addClass('has-error');
				}
			}

			function submitApis() {
				var apis = [],
						error = false;
				$('.api').each(function(){
					var api = $(this);
					if (api.hasClass('has-error')) {
						error = true;
						return false;
					} else if (api.is(':last-child')) {
						// skip last row as it is the api placeholder
						return true;
					}
					apis.push({
						'email' : $('.email', api).val().trim(),
						'accountId' : $('.accountId', api).val().trim(),
						'clientId'   : $('.clientId', api).val().trim(),
						'clientSecret': $('.clientSecret', api).val().trim()
					});
				});

				if (error) {
					return false;
				}

				$('#apisJSON').val(JSON.stringify(apis));
				return true;
			}

			$(document).ready(function () {
				var apis = ${apis},
						api = $('.api');
				$.each(apis, function(){
					var apiEntry = api.clone().insertBefore(api);
					apiEntry.find('.email').val(this.email);
					apiEntry.find('.accountId').val(this.accountId);
					apiEntry.find('.clientId').val(this.clientId);
					apiEntry.find('.clientSecret').val(this.clientSecret);
				});
			});
		</script>
	</lams:head>

	<body class="stripes">
	<c:set var="title"><fmt:message key="admin.title" /></c:set>
	<lams:Page type="admin" title="${title}">
		<div class="container-fluid">
			<div class="panel panel-default panel-admin-page">
				<div class="panel-body panel-admin-body">
					<p><a href="<lams:LAMSURL/>/admin/appadminstart.do" class="btn btn-primary"><fmt:message key="appadmin.maintain" /></a></p>
					<c:if test="${saveOK}">
						<lams:Alert type="info" close="false">
							<fmt:message key='label.api.saved'/>
						</lams:Alert>
					</c:if>
					<lams:errors/>

					<div>
						<div class="row api">
							<div class="col-xs-3">
								<input type="text" class="form-control email" placeholder="<fmt:message key='label.api.email'/>" onkeyup="javascript:validateApi(this)"/>
							</div>
							<div class="col-xs-2">
								<input type="text" class="form-control accountId" placeholder="<fmt:message key='label.api.account.id'/>" onkeyup="javascript:validateApi(this)"/>
							</div>
							<div class="col-xs-2">
								<input type="text" class="form-control clientId" placeholder="<fmt:message key='label.api.client.id'/>" onkeyup="javascript:validateApi(this)"/>
							</div>
							<div class="col-xs-4">
								<input type="text" class="form-control clientSecret" placeholder="<fmt:message key='label.api.client.secret'/>" onkeyup="javascript:validateApi(this)"/>
							</div>
							<div class="col-xs-1 control" title="<fmt:message key='label.api.remove' />" onclick="javascript:removeApi(this)">
								<i class="fa fa-times"></i>
							</div>
						</div>
					</div>
					<form onsubmit="javascript:submitApis()" action="save.do" method="post">
						<input type="hidden" id="apisJSON" name="apisJSON" />
						<input type="submit" class="btn btn-primary pull-right voffset20" value='<fmt:message key="label.save" />' />
					</form>
				</div>
			</div>
		</div>
	</lams:Page>
	</body>
</lams:html>