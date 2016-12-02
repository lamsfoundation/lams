<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<c:set var="UPLOAD_FILE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_MAX_SIZE)%></c:set>

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

		<div class="input-group" id="addfile">
	    <span class="input-group-btn" style="font-size:inherit;">
				<button tabindex="2" id="fileButtonBrowse" type="button" class="btn btn-sm btn-default">
					<i class="fa fa-upload"></i> <fmt:message key="label.authoring.basic.resource.file.input"/>
				</button>
			</span>
			<input type="file" id="fileSelector" name="file" style="display:none"> 
			<input type="text" id="fileInputName" style="display:none;" disabled="disabled" placeholder="File not selected" class="form-control input-sm">
		</div>

		<script type="text/javascript">
			// Fake file upload
			document.getElementById('fileButtonBrowse').addEventListener('click', function() {
				document.getElementById('fileSelector').click();
			});
			
			document.getElementById('fileSelector').addEventListener('change', function() {
				$('#fileInputName').show();
				document.getElementById('fileInputName').value = this.value.replace(/^.*\\/, "");
				
			});
		</script>    

		<div class="form-group voffset10">
	    	<label for="description"><fmt:message key="label.learning.comment.or.instruction" /></label>
			<html:text property="description" tabindex="3" styleClass="form-control" maxlength="255"/>
	  	</div>	

		<div id="buttons" class="pull-right">
	 		<html:button property="goback" onclick="javascript:cancel()" styleClass="btn btn-sm btn-default" styleId="cancelButton">
				<fmt:message key="button.cancel" />
			</html:button>&nbsp;
			<html:submit styleClass="btn btn-sm btn-default" styleId="submitButton">
			 	<fmt:message key="button.add" />
			</html:submit>
		</div>
	
	</html:form>
	
	<script type="text/javascript">
	  var UPLOAD_FILE_MAX_SIZE = '<c:out value="${UPLOAD_FILE_MAX_SIZE}"/>';

		$(document).ready(function(){
			$('#title').focus();
		});	
					
		$.validator.addMethod('filesize', function (value, element, param) {
		    return this.optional(element) || (element.files[0].size <= param)
		}, '<fmt:message key="errors.maxfilesize"><fmt:param>{0}</fmt:param></fmt:message>');

	  
		$('#resourceItemForm').submit(submitResourceForm);
		$('#resourceItemForm').validate({
			ignore: [],
			errorClass: "text-danger",
			wrapper: "span",
			rules: {
				file: {
			    	required: true,
			    	filesize: UPLOAD_FILE_MAX_SIZE, 
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
		});	
	</script>
	
</div>
</div>

