<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%@ page import="org.lamsfoundation.lams.util.FileValidatorUtil" %>
<c:set var="UPLOAD_FILE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_MAX_SIZE)%></c:set>
<c:set var="UPLOAD_FILE_MAX_SIZE_AS_USER_STRING"><%=FileValidatorUtil.formatSize(Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_MAX_SIZE))%></c:set>
<c:set var="EXE_FILE_TYPES"><%=Configuration.get(ConfigurationKeys.EXE_EXTENSIONS)%></c:set>

<!-- Add a File Form-->
<div class="panel panel-default">
	<div class="panel-heading panel-title">
		<fmt:message key="label.learning.new.file" />
	</div>
	<div class="panel-body">
	
	<%@ include file="/common/messages.jsp"%>
	
	<html:form action="/learning/saveOrUpdateItem" method="post" styleId="resourceItemForm" focus="title" enctype="multipart/form-data">
	
		<html:hidden property="itemType" styleId="itemType" />
		<html:hidden property="mode" styleId="mode"/>
		<html:hidden property="sessionMapID"/>
	
		<div class="form-group">
	    	<label for="title"><fmt:message key="label.authoring.basic.resource.title.input" /></label>:
			<html:text property="title" styleClass="form-control" tabindex="1" styleId="resourcetitle"/>
	  	</div>	

		<lams:FileUpload fileFieldname="file" maxFileSize="${UPLOAD_FILE_MAX_SIZE_AS_USER_STRING}" tabindex="2"/>

		<div class="form-group voffset10">
	    	<label for="description"><fmt:message key="label.learning.comment.or.instruction" /></label>
			<html:text property="description" tabindex="3" styleClass="form-control" maxlength="255"/>
	  	</div>	

		<lams:WaitingSpinner id="itemAttachmentArea_Busy"/>	
		<div id="buttons" class="pull-right">
	 		<html:button property="goback" onclick="javascript:cancel()" styleClass="btn btn-sm btn-default" styleId="cancelButton">
				<fmt:message key="button.cancel" />
			</html:button>&nbsp;
			<html:submit styleClass="btn btn-sm btn-default btn-disable-on-submit" styleId="submitButton">
			 	<fmt:message key="button.add" />
			</html:submit>
		</div>
	
	</html:form>
	
	<script type="text/javascript">

		$(document).ready(function(){
			$('#title').focus();
		});	
					
		$.validator.addMethod('validateType', function (value, element, param) {
			return validateNotExecutable(element.files[0], param);
		}, '<fmt:message key="error.attachment.executable"/>');

		$.validator.addMethod('validateSize', function (value, element, param) {
			return validateFileSize(element.files[0], param);
		}, '<fmt:message key="errors.maxfilesize"><fmt:param>${UPLOAD_FILE_MAX_SIZE_AS_USER_STRING}</fmt:param></fmt:message>');

		$('#resourceItemForm').submit(submitResourceForm);
		$('#resourceItemForm').validate({
			ignore: [],
			errorClass: "text-danger",
			wrapper: "span",
			rules: {
				file: {
			    	required: true,
			    	validateType: '<c:out value="${EXE_FILE_TYPES}"/>',
			    	validateSize: '<c:out value="${UPLOAD_FILE_MAX_SIZE}"/>', 
			    },
			    title: {
			    	required: true
			    }
			},
			messages : {
				file : {
					required : '<fmt:message key="error.resource.item.file.blank"/> '
				},
				title : {
					required : '<fmt:message key="error.resource.item.title.blank"/> '
				}
			},
			errorPlacement: function(error, element) {
		        if (element.hasClass("fileUpload")) {
		           error.insertAfter(element.parent());
		        } else {
		           error.insertAfter(element);
		        }
		    }
		});	
	</script>
	
</div>
</div>

