<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<lams:head>
		<%@ include file="addheader.jsp"%>		
		<script type="text/javascript">
			var extensionValidation = function(currentFile, files) {
			  var name = currentFile.data.name || currentFile.name,
			  	  extensionIndex = name.lastIndexOf('.'),
			  	  valid = extensionIndex < 0 || !EXE_FILE_TYPES.includes(name.substring(extensionIndex).trim().toLowerCase());
			  if (!valid) {
				  uppy.info(EXE_FILE_ERROR, 'error', 10000);
			  }
			  
			  return valid;
		    }
		
			$(document).ready(function(){
				$('#title').focus();
			});

			$.validator.addMethod('requireFileCount', function (value, element, param) {
				return uppy.getFiles().length >= +param
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
		<lams:JSImport src="includes/javascript/rsrcresourceitem.js" relative="true" />
	</lams:head>
	<body>

		<div class="panel panel-default add-file">
			<div class="panel-heading panel-title">
				<fmt:message key="label.authoring.basic.add.file" />
			</div>
			
			<div class="panel-body">

			<lams:errors/>
			<c:set var="csrfToken"><csrf:token/></c:set>
			<form:form action="saveOrUpdateItem.do?${csrfToken}" method="post" modelAttribute="resourceItemForm" id="resourceItemForm" enctype="multipart/form-data">
				<input type="hidden" name="instructionList" id="instructionList" />
				<form:hidden path="sessionMapID" />
				<input type="hidden" name="itemType" id="itemType" value="2" />
				<form:hidden path="itemIndex" />
	
				<div class="form-group">
				   	<label for="title"><fmt:message key="label.authoring.basic.resource.title.input" /></label>:
					<form:input id="title" path="title" cssClass="form-control" />
			  	</div>	
			  

				<div class="form-group">
					<span id="itemAttachmentArea">
					<%@ include file="/pages/authoring/parts/itemattachment.jsp"%>
					</span>
					<lams:WaitingSpinner id="itemAttachmentArea_Busy"/>
				</div>			

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
					<i class="fa fa-plus"></i>&nbsp;<fmt:message key="label.authoring.basic.add.file" /> </button>
			    
			</div>
						
			</div>
		</div>
	</body>
</lams:html>
