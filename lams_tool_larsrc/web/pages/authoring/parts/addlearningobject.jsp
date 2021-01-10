<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<lams:head>
		<%@ include file="addheader.jsp"%>
		<script type="text/javascript">
	       
		  $(document).ready(function(){
				$('#title').focus();
			});		
		  
			var extensionValidation = function(currentFile, files) {
			  var name = currentFile.data.name || currentFile.name,
			  	  extensionIndex = name.lastIndexOf('.'),
			  	  valid = extensionIndex < 0 || name.substring(extensionIndex).trim() == '.zip';
			  if (!valid) {
				  uppy.info('<fmt:message key="error.file.type.zip"/>', 'error', 10000);
			  }
			  
			  return valid;
		    }

		  	$.validator.addMethod('requireFileCount', function (value, element, param) {
				return uppy.getFiles().length >= +param;
			}, '<fmt:message key="error.resource.item.file.blank"/>');

								  
	 		$( "#resourceItemForm" ).validate({
	 			ignore: [],
				errorClass: "text-danger",
				wrapper: "span",
	 			rules: {
	 				'tmpFileUploadId' : {
	 					requireFileCount: 1
	 				},
				    title: {
				    	required: true
				    }
	 			},
				messages : {
					title : {
						required : '<fmt:message key="error.resource.item.title.blank"/> '
					}
				},
				errorPlacement: function(error, element) {
					error.insertAfter(element);
			    }
			});	 		
		</script>
		<script type="text/javascript" src="<lams:WebAppURL/>includes/javascript/rsrcresourceitem.js"></script>

	</lams:head>
	<body>

		<div class="panel panel-default add-file">
			<div class="panel-heading panel-title">
				<fmt:message key="label.authoring.basic.add.learning.object" />
			</div>
			
			<div class="panel-body">

			<lams:errors/>
			<c:set var="csrfToken"><csrf:token/></c:set>
			<form:form action="saveOrUpdateItem.do?${csrfToken}" method="post" modelAttribute="resourceItemForm" id="resourceItemForm" enctype="multipart/form-data">
				<input type="hidden" name="instructionList" id="instructionList" />
				<form:hidden path="sessionMapID" />
				<input type="hidden" name="itemType" id="itemType" value="4" />
				<form:hidden path="itemIndex" />
	
				<div class="form-group">
				   	<label for="title"><fmt:message key="label.authoring.basic.resource.title.input" /></label>:
					<form:input path="title" id="title" cssClass="form-control" />
			  	</div>	
			  

				<div class="form-group">
					<!-- <label for="file"><fmt:message key="label.authoring.basic.resource.zip.file.input" /></label> -->
					<c:set var="itemAttachment" value="${resourceItemForm}" />
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
					<fmt:message key="label.cancel" /> </button>
				<button onclick="submitResourceItem(); return false;" class="btn btn-default btn-sm btn-disable-on-submit">
					<i class="fa fa-plus"></i>&nbsp;<fmt:message key="label.authoring.basic.add.learning.object" /> </button>
			</div>
			
			</div>
		</div>

	</body>
</lams:html>
