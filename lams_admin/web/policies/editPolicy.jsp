<%@ include file="/taglibs.jsp"%>

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

		$('#cancelButton').on('click', function (e) {
			e.preventDefault();
			window.location.href = "<lams:LAMSURL/>admin/policyManagement.do?method=list";
	    });
	});
</script>

<div>
	<a href="<lams:LAMSURL/>/admin/sysadminstart.do" class="btn btn-default">
		<fmt:message key="sysadmin.maintain" />
	</a>
	<a href="policyManagement.do?method=list" class="btn btn-default loffset5">
		<fmt:message key="admin.policies.title" />
	</a>
</div>

<html:form action="/policyManagement.do" styleId="policy-form" method="post">
	<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
	<html:hidden property="method" value="save" />
	<html:hidden property="policyUid" />
	<html:hidden property="policyId" />
	
	<table class="table table-condensed table-no-border">
		<tr>
			<td><fmt:message key="label.name" />&nbsp;&nbsp;*</td>
			<td><html:text property="policyName" size="40" maxlength="255" styleClass="form-control"/></td>
		</tr>
		<tr>
			<td style="width: 250px;"><fmt:message key="label.policy.type" />:</td>
			<td>
				<html:select property="policyTypeId" styleClass="form-control">
					<html:option value="1"><fmt:message key="label.policy.type.site" /></html:option>
					<html:option value="2"><fmt:message key="label.policy.type.privacy" /></html:option>
					<html:option value="3"><fmt:message key="label.policy.type.third.party" /></html:option>
					<html:option value="4"><fmt:message key="label.policy.type.other" /></html:option>
				</html:select>
			</td>
		</tr>
		<tr>
			<td><fmt:message key="label.version" /></td>
			<td><html:text property="version" size="40" maxlength="255" styleClass="form-control"/></td>
		</tr>
		<tr>
			<td><fmt:message key="label.summary" />&nbsp;&nbsp;*</td>
			<td>
			  <lams:CKEditor id="summary" 
			     value="${policyForm.map.summary}" 
			     contentFolderID="../public/policies">
			  </lams:CKEditor>
			  <span id="summary-error"></span>
			</td>
		</tr>
		<tr>
			<td><fmt:message key="label.full.policy" />&nbsp;&nbsp;*</td>
			<td>
			  <lams:CKEditor id="fullPolicy" 
			     value="${policyForm.map.fullPolicy}" 
			     contentFolderID="../public/policies">
			  </lams:CKEditor>
			  <span id="full-policy-error"></span>
			</td>
		</tr>
		<tr>
			<td><fmt:message key="label.policy.status" /></td>
			<td>
				<html:radio property="policyStateId" value="1" />&nbsp;<fmt:message key="label.policy.status.active" />
				<br>
				<html:radio property="policyStateId" value="2" />&nbsp;<fmt:message key="label.policy.status.inactive" /> 
				<div>
					<fmt:message key="label.policy.status.hint" />
				</div>
			</td>
		</tr>
		
		<c:if test="${formBean.map.policyUid != null}">
			<tr>
				<td><fmt:message key="label.minor.change" /></td>
				<td><html:checkbox property="minorChange" /></td>
			</tr>
		</c:if>
		
	</table>
	
	<div class="pull-right">
		<html:cancel styleId="cancelButton" styleClass="btn btn-default">
			<fmt:message key="admin.cancel" />
		</html:cancel>

		<html:submit styleId="submitButton" styleClass="btn btn-primary loffset5">
			<fmt:message key="admin.submit" />
		</html:submit>
	</div>

</html:form>