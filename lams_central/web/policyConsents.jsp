<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<title><fmt:message key="label.policies.title" /></title>
	<link rel="icon" href="/lams/favicon.ico" type="image/x-icon" />
	<link rel="shortcut icon" href="/lams/favicon.ico" type="image/x-icon" />
	<lams:css/>
	<lams:css suffix="main"/>
	<style>
		.alert.alert-danger {
			margin-right: 5px;
		}
		.policy-details {
			padding-left: 10px;
		}
		h5 {
		    margin-bottom: 5px;
		}
	</style>
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.validate.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.form.js"></script>
	<script type="text/javascript" src="/lams/includes/javascript/bootstrap.min.js"></script>
    <script>
		$(document).ready(function(){	
		    $("#consents-form").validate({
	    	    invalidHandler: function(form, validator) {
	    		      var errors = validator.numberOfInvalids();
	    		      if (errors) {
		    		      $("#consent-all").removeClass("alert-info").addClass("alert-danger");
	    		      } else {
	    		    	  $("#consent-all").addClass("alert-info").removeClass("alert-danger");
	    		      }
	    		},
	    		errorPlacement: function(error, element) {
	    		    error.insertBefore( element.parent("label") );
	    		},
	    		errorClass: "alert alert-danger"
		  	});

		 	// the following method must come AFTER .validate()
		    $('.required-field').each(function() {
		        $(this).rules('add', {
		            required: true,
					messages: {
						required: "<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.consent.required' /></spring:escapeBody>"
					}
		        });
		    });
		}); 
	</script>
</lams:head>
<body class="offcanvas-hidden">
<div id="page-wrapper">
<div class="content">
	
	<h4>
		<fmt:message key="label.policies.title" />
	</h4>
			
	<lams:Alert close="false" id="consent-all" type="info">
		<fmt:message key="label.agree.to.policies.before.proceeding" />
	</lams:Alert>
	
	<form action="/lams/policyConsents/consent.do" method="post" id="consents-form">
	<table class="table table-striped table-condensed" >
		<c:forEach items="${policies}" var="policy">
			<tr>
				<td>
					<h4>
						<c:out value="${policy.policyName}" />
					</h4>
					
					<div class="policy-details">
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
						
						<h5>
							<fmt:message key="label.summary" />
						</h5>
						<c:out value="${policy.summary}" escapeXml="false"/>
						
						<h5>
							<fmt:message key="label.full.policy" />
						</h5>
						<c:out value="${policy.fullPolicy}" escapeXml="false"/>
					
						<div class="checkbox">
							<label for="policy-${policy.uid}">
								<input type="checkbox" name="policy${policy.uid}" value="${policy.uid}"
										id="policy-${policy.uid}" class="required-field"
										<c:if test="${policy.consentedByUser}">checked="checked"</c:if>>
								<fmt:message key="label.agree.to.policy" />
							</label>
						</div>
					</div>
					
				</td>
			</tr>
		</c:forEach>
	</table>
	
		<div style="overflow:auto;">
			<button type="submit" class="btn btn-primary pull-right">
				<fmt:message key="label.consent"/>
			</button>
		</div>
	</form>
	
</div>
</div>
</body>
</lams:html>