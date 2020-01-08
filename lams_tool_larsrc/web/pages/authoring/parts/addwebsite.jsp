<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%@ page import="org.lamsfoundation.lams.util.FileValidatorUtil" %>
<c:set var="UPLOAD_FILE_LARGE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE)%></c:set>
<c:set var="UPLOAD_FILE_MAX_SIZE_AS_USER_STRING"><%=FileValidatorUtil.formatSize(Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE))%></c:set>

<lams:html>
	<lams:head>		
		<%@ include file="addheader.jsp"%>
		<script type="text/javascript">
		  var UPLOAD_FILE_LARGE_MAX_SIZE = '<c:out value="${UPLOAD_FILE_LARGE_MAX_SIZE}"/>';
		  
			$(document).ready(function(){
				$('#title').focus();
			});	

			$.validator.addMethod("fileType", function(value, element) {
				return this.optional(element) || element.files[0].type == 'application/zip' || element.files[0].type == 'application/x-zip-compressed';
			});
			
			$.validator.addMethod('validateSize', function (value, element, param) {
				return validateFileSize(element.files[0], param);
			}, '<fmt:message key="errors.maxfilesize"><fmt:param>${UPLOAD_FILE_MAX_SIZE_AS_USER_STRING}</fmt:param></fmt:message>');

								  
	 		$( "#resourceItemForm" ).validate({
	 			ignore: [],
				errorClass: "text-danger",
				wrapper: "span",
	 			rules: {
	 				file: {
	 			    	required: true,
	 			    	fileType: true,
	 			    	validateSize: UPLOAD_FILE_LARGE_MAX_SIZE,
	 			    },
				    title: {
				    	required: true
				    }
	 			},
				messages : {
					file : {
						required : '<fmt:message key="error.resource.item.file.blank"/> ',
						fileType: '<fmt:message key="error.file.type.zip"/>'
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
		<script type="text/javascript" src="<lams:WebAppURL/>includes/javascript/rsrcresourceitem.js"></script>

	</lams:head>
	<body>

		<div class="panel panel-default add-file">
			<div class="panel-heading panel-title">
				<fmt:message key="label.authoring.basic.add.website" />
			</div>
			
			<div class="panel-body">

			<lams:errors/>
			<c:set var="csrfToken"><csrf:token/></c:set>
			<form:form action="saveOrUpdateItem.do?${csrfToken}" method="post" modelAttribute="resourceItemForm" id="resourceItemForm" enctype="multipart/form-data">
				<form:hidden path="sessionMapID" />
				<input type="hidden" name="instructionList" id="instructionList" />
				<input type="hidden" name="itemType" id="itemType" value="3" />
				<form:hidden path="itemIndex" />
	
				<div class="form-group">
				   	<label for="title"><fmt:message key="label.authoring.basic.resource.title.input" /></label>:
					<form:input path="title" id="title"  cssClass="form-control" />
			  	</div>	
			  

				<div class="form-group">
					<!--  <label for="file"><fmt:message key="label.authoring.basic.resource.zip.file.input" /></label>: -->
					<span id="itemAttachmentArea">
					<%@ include file="/pages/authoring/parts/itemattachment.jsp"%>
					</span>
				</div>

				<lams:WaitingSpinner id="itemAttachmentArea_Busy"/>
	
				<div class="form-group">
					<%@ include file="ratings.jsp"%>	
				</div>
				
			</form:form>
		
			<!-- Instructions -->
			<%@ include file="instructions.jsp"%>
			<div><br/></div>
			<div><br/></div>
			<div class="voffset5 pull-right">
			    <button onclick="hideResourceItem(); return false;" class="btn btn-default btn-sm btn-disable-on-submit">
					<fmt:message key="label.cancel" /> </a>
				<button onclick="submitResourceItem(); return false;" class="btn btn-default btn-sm btn-disable-on-submit">
					<i class="fa fa-plus"></i>&nbsp;<fmt:message key="label.authoring.basic.add.website" /> </button>
			</div>
		<br />


	</body>
</lams:html>
