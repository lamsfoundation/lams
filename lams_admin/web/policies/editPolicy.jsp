<%@ include file="/taglibs.jsp"%>

<!DOCTYPE html>
<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="admin.policies.title"/></c:set>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	<title>${title}</title>

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>/admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen">

	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.validate.js"></script>
	<script type="text/javascript">	
		$(document).ready(function(){
			// validate signup form on keyup and submit
			var validator = $("#policy-form").validate({
				ignore: [],
				rules: {
					policyName: "required", 
					summary:{
	                	required: function() {
	
	                		//var ckeditorData = CKEDITOR.instances.summary.getData();
	                		//skip out empty values
	                		//CKEDITOR.instances.summary.value = ((ckeditorData == null) || (ckeditorData.replace(/&nbsp;| |<br \/>|\s|<p>|<\/p>|\xa0/g, "").length == 0)) ? "" : ckeditorData;
	
	                    	CKEDITOR.instances.summary.updateElement();
	                    	//alert("s"+ckeditorData);
	                    	//return false;
	                	}
	            	},
	            	fullPolicy:{
	                	required: function() {
	                    	CKEDITOR.instances.fullPolicy.updateElement();
	                	}
	            	}
				},
				messages: {
					policyName: "<c:set var="namev"><fmt:message key='label.name' /></c:set><fmt:message key="error.required"><fmt:param>${namev}</fmt:param></fmt:message>",
					summary: "<c:set var="namev"><fmt:message key='label.summary' /></c:set><fmt:message key="error.required"><fmt:param>${namev}</fmt:param></fmt:message>",
					fullPolicy: "<c:set var="namev"><fmt:message key='label.full.policy' /></c:set><fmt:message key="error.required"><fmt:param>${namev}</fmt:param></fmt:message>"
				},
				errorPlacement: function(error, element) {
					var placement = $(element).data('error');
					
			        if (element.attr("name") == "summary" ) { //Id of input field
			        	error.appendTo('#summary-error');
			        } else if (element.attr("name") == "fullPolicy" )  {//Id of input field
			            error.appendTo('#full-policy-error');
			        } else {
			            error.insertAfter(element);
			        }
			    }
			});
		});
	</script>
</lams:head>
<body class="stripes">
	<c:set var="title">${title}: <fmt:message key="admin.add.edit.policy"/></c:set>

	<lams:Page type="admin" title="${title}" >
		<div>
			<a href="<lams:LAMSURL/>admin/sysadminstart.do" class="btn btn-default">
				<fmt:message key="sysadmin.maintain" />
			</a>
			<a href="../policyManagement/list.do" class="btn btn-default loffset5">
				<fmt:message key="admin.policies.title" />
			</a>
		</div>
	<c:set var="csrfToken"><csrf:token/></c:set>	
    <form:form action="../policyManagement/save.do?${csrfToken}" modelAttribute="policyForm" id="policy-form" cssClass="voffset20" method="post">
			<form:hidden path="policyUid" />
			<form:hidden path="policyId" />
			<table class="table table-condensed table-no-border">
				<tr>
					<td><fmt:message key="label.name" />&nbsp;&nbsp;*</td>
					<td><form:input path="policyName" size="40" maxlength="255" cssClass="form-control" /></td>
				</tr>
				<tr>
					<td style="width: 250px;"><fmt:message key="label.policy.type" />:</td>
					<td>
						<form:select path="policyTypeId" cssClass="form-control">
							<form:option value="1"><fmt:message key="label.policy.type.site" /></form:option>
							<form:option value="2"><fmt:message key="label.policy.type.privacy" /></form:option>
							<form:option value="3"><fmt:message key="label.policy.type.third.party" /></form:option>
							<form:option value="4"><fmt:message key="label.policy.type.other" /></form:option>
						</form:select>
					</td>
				</tr>
				<tr>
					<td><fmt:message key="label.version" /></td>
					<td><form:input path="version" size="40" maxlength="255" cssClass="form-control"/></td>
				</tr>
				<tr>
					<td><fmt:message key="label.summary" />&nbsp;&nbsp;*</td>
					<td>
					  <lams:CKEditor id="summary" 
					     value="${policyForm.summary}" 
					     contentFolderID="../public/policies">
					  </lams:CKEditor>
					  <span id="summary-error"></span>
					</td>
				</tr>
				<tr>
					<td><fmt:message key="label.full.policy" />&nbsp;&nbsp;*</td>
					<td>
					  <lams:CKEditor id="fullPolicy" 
					     value="${policyForm.fullPolicy}" 
					     contentFolderID="../public/policies">
					  </lams:CKEditor>
					  <span id="full-policy-error"></span>
					</td>
				</tr>
				<tr>
					<td><fmt:message key="label.policy.status" /></td>
					<td>
		                <div class="radio">
		                    <label>
		                    	<form:radiobutton path="policyStateId" value="1" />
		                        <fmt:message key="label.policy.status.active" />
		                    </label>
		                </div>
		                <div class="radio">
		                    <label>
		                    	<form:radiobutton path="policyStateId" value="2" />
		                        <fmt:message key="label.policy.status.inactive" />
		                    </label>
		                    <span id="helpBlock" class="help-block"><fmt:message key="label.policy.status.hint" /></span>
		                </div>
					</td>
				</tr>
				
				<c:if test="${policyForm.policyUid != null}">
					<tr>
						<td><fmt:message key="label.minor.change" /></td>
						<td>
		                    <div class="radio">
		                        <form:checkbox path="minorChange" />
		                        <span id="helpBlock" class="help-block"><fmt:message key="label.policy.minor.change.hint" /></span>
		                    </div>
		                </td>
					</tr>
				</c:if>
				
			</table>
			
			<div class="pull-right">
				<a href="<lams:LAMSURL/>admin/policyManagement/list.do" class="btn btn-default">
					<fmt:message key="admin.cancel" />
				</a>
				<button type="submit" id="submitButton" class="btn btn-primary loffset5"><fmt:message key="admin.submit" /></button>
			</div>
		
		</form:form>
	</lams:Page>
</body>
</lams:html>
