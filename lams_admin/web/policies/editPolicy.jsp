<%@ include file="/taglibs.jsp"%>

<!DOCTYPE html>
<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="admin.policies.title"/></c:set>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	<title>${title}</title>

	<link rel="stylesheet" href="<lams:LAMSURL/>css/bootstrap5.custom.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>includes/font-awesome6/css/all.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/components.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">

	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.validate.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap5.bundle.min.js"></script>
	<script type="text/javascript">	
		$(document).ready(function(){
			// validate signup form on keyup and submit
			var validator = $("#policy-form").validate({
				errorClass: 'text-danger form-text font-italic',
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
<body class="component pb-4 pt-2">
	<c:set var="title"><fmt:message key="admin.add.edit.policy"/></c:set>
	
	<%-- Build breadcrumb --%>
	<c:set var="breadcrumbTop"><lams:LAMSURL/>admin/appadminstart.do | <fmt:message key="appadmin.maintain" /></c:set>
	<c:set var="breadcrumbChild1"><lams:LAMSURL/>admin/policyManagement/list.do | <fmt:message key="admin.policies.title" /></c:set>
	<c:set var="breadcrumbActive">. | <fmt:message key="admin.add.edit.policy"/></c:set>
	<c:set var="breadcrumbItems" value="${breadcrumbTop}, ${breadcrumbChild1},${breadcrumbActive}"/>

	<lams:Page5 type="admin" title="${title}" breadcrumbItems="${breadcrumbItems}">

	    <form:form action="../policyManagement/save.do" modelAttribute="policyForm" id="policy-form" cssClass="voffset20" method="post">
			<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
			<form:hidden path="policyUid" />
			<form:hidden path="policyId" />
	
			<div class="row">
				<div class="col-6 offset-3">	
					<div class="mb-3">
					    <label for="policyName"><fmt:message key="label.name" /></label>&nbsp;<span class="text-danger">*</span>
					    <input id="policyName" maxlength="40" name="policyName" value="${policyForm.policyName}" class="form-control" required/>
					</div>
					<div class="mb-3">
					    <label for="policyTypeId"><fmt:message key="label.policy.type" /></label>
						<form:select path="policyTypeId" cssClass="form-select">
							<form:option value="1"><fmt:message key="label.policy.type.site" /></form:option>
							<form:option value="2"><fmt:message key="label.policy.type.privacy" /></form:option>
							<form:option value="3"><fmt:message key="label.policy.type.third.party" /></form:option>
							<form:option value="4"><fmt:message key="label.policy.type.other" /></form:option>
						</form:select>
					</div>
					<div class="mb-3">
					    <label for="version" class="form-label"><fmt:message key="label.version" /></label>&nbsp;<span class="text-danger">*</span>
					    <input id="version" maxlength="40" name="version" value="${policyForm.version}" class="form-control" required/>
					</div>
					<div class="mb-3">
						  <label for="summary" class="form-label"><fmt:message key="label.summary" /></label>&nbsp;<span class="text-danger">*</span>
						  <lams:CKEditor id="summary" 
						     value="${policyForm.summary}" 
						     contentFolderID="../public/policies">
						  </lams:CKEditor>
						  <span id="summary-error"></span>
					</div>
					<div class="mb-3">
					    <label for="fullPolicy" class="form-label"><fmt:message key="label.full.policy" /></label>&nbsp;<span class="text-danger">*</span>
					  	<lams:CKEditor id="fullPolicy" 
					    	 value="${policyForm.fullPolicy}" 
					     	contentFolderID="../public/policies">
						  </lams:CKEditor>
					  	<span id="full-policy-error"></span>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-6 offset-3">	
					<h4><fmt:message key="label.policy.status" /></h4>
			    	<small id="policyStatusHelpBlock" class="form-text text-muted">
						<fmt:message key="label.policy.status.hint" />
					</small>
					
					<div class="form-check mt-2">
						<form:radiobutton path="policyStateId" value="1" id="policyStateId" name="policyStateId" cssClass="form-check-input"/>
				    	<label class="form-check-label" for="policyStateId">
	                        <fmt:message key="label.policy.status.active" />
	                    </label>
	                </div>
	                
					<div class="form-check">
						<form:radiobutton path="policyStateId" value="2" id="policyStateId2" name="policyStateId" cssClass="form-check-input"/>
				    	<label class="form-check-label" for="policyStateId2">
		                   <fmt:message key="label.policy.status.inactive" />
		               </label>
		           </div>
				</div>
			</div>
			
			<c:if test="${policyForm.policyUid != null}">
				<div class="row">
					<div class="col-6 offset-3">	
						<div class="form-check mt-2">
							<form:checkbox id="minorChange" path="minorChange" name="minorChange" cssClass="form-check-input"/>
					    	<label class="form-check-label" for="disabled">
					    		<fmt:message key="label.minor.change" />
					    		<br>
								<small id="policyMinorChangeHelpBlock" class="form-text text-muted">
					    			<fmt:message key="label.policy.minor.change.hint" />
					    		</small>
					    	</label>
					    </div>
				    </div>
			    </div>
			</c:if>
					
			<div class="row mt-3">
				<div class="col-6 offset-3 text-end">	
					<a href="<lams:LAMSURL/>admin/policyManagement/list.do" class="btn btn-secondary">
							<fmt:message key="admin.cancel" />
						</a>
					<button type="submit" id="submitButton" class="btn btn-primary"><fmt:message key="admin.submit" /></button>
				</div>
			</div>
			
		</form:form>
	</lams:Page5>
</body>
</lams:html>
