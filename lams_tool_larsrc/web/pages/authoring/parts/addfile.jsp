<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%@ page import="org.lamsfoundation.lams.util.FileValidatorUtil" %>
<c:set var="UPLOAD_FILE_LARGE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE)%></c:set>
<c:set var="UPLOAD_FILE_MAX_SIZE_AS_USER_STRING"><%=FileValidatorUtil.formatSize(Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE))%></c:set>
<c:set var="EXE_FILE_TYPES"><%=Configuration.get(ConfigurationKeys.EXE_EXTENSIONS)%></c:set>

<lams:html>
	<lams:head>
		<%@ include file="addheader.jsp"%>		
		<script type="text/javascript">
			var UPLOAD_FILE_LARGE_MAX_SIZE = '<c:out value="${UPLOAD_FILE_LARGE_MAX_SIZE}"/>';
			
			$(document).ready(function(){
				$('#title').focus();
			});

			$.validator.addMethod('validateType', function (value, element, param) {
				return validateNotExecutable(element.files[0], param);
			}, '<fmt:message key="error.attachment.executable"/>');

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
				    	validateType: '<c:out value="${EXE_FILE_TYPES}"/>',
				    	validateSize: '<c:out value="${UPLOAD_FILE_LARGE_MAX_SIZE}"/>', 
				    },
				    title: {
				    	required: true
				    }
	 			},
				messages : {
					file : {
						required : '<fmt:message key="error.resource.item.file.blank"/> ',
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
		<script type="text/javascript" src="<html:rewrite page='/includes/javascript/rsrcresourceitem.js'/>"></script>
	</lams:head>
	<body>

		<div class="panel panel-default add-file">
			<div class="panel-heading panel-title">
				<fmt:message key="label.authoring.basic.add.file" />
			</div>
			
			<div class="panel-body">

			<%@ include file="/common/messages.jsp"%>

			<html:form action="/authoring/saveOrUpdateItem" method="post" styleId="resourceItemForm" enctype="multipart/form-data">
				<input type="hidden" name="instructionList" id="instructionList" />
				<html:hidden property="sessionMapID" />
				<input type="hidden" name="itemType" id="itemType" value="2" />
				<html:hidden property="itemIndex" />
	
				<div class="form-group">
				   	<label for="title"><fmt:message key="label.authoring.basic.resource.title.input" /></label>:
					<html:text styleId="title" property="title" styleClass="form-control" />
			  	</div>	
			  

				<div class="form-group">
					<c:set var="itemAttachment" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
					<span id="itemAttachmentArea">
					<%@ include file="/pages/authoring/parts/itemattachment.jsp"%>
					</span>
					<lams:WaitingSpinner id="itemAttachmentArea_Busy"/>
				</div>			
	
				<div class="form-group">
					<html:checkbox property="allowRating" styleId="allowRating"/>
					&nbsp;<label for="allowRating"><fmt:message key="label.enable.rating" /></label>
				</div>
			</html:form>
	
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
