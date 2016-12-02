<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<c:set var="UPLOAD_FILE_LARGE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE)%></c:set>

<lams:html>
	<lams:head>
		<%@ include file="addheader.jsp"%>
		<script type="text/javascript">
			var UPLOAD_FILE_LARGE_MAX_SIZE = '<c:out value="${UPLOAD_FILE_LARGE_MAX_SIZE}"/>';
			
			$(document).ready(function(){
				$('#title').focus();
			});

						
	 		$( "#resourceItemForm" ).validate({
	 			ignore: [],
				errorClass: "text-danger",
				wrapper: "span",
	 			rules: {
	 				file: {
	 			    	required: true
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
					<i class="fa fa-spinner" style="display:none" id="itemAttachmentArea_Busy"></i>
				</div>
	
			</html:form>
	
			<!-- Instructions -->
			<%@ include file="instructions.jsp"%>
			<div><br/></div>
			<div><br/></div>
			<div class="voffset5 pull-right">
			    <a href="javascript:;" onclick="hideResourceItem()" class="btn btn-default btn-sm">
					<fmt:message key="label.cancel" /> </a>
				<a href="#nogo" onclick="submitResourceItem()" class="btn btn-default btn-sm">
					<i class="fa fa-plus"></i>&nbsp;<fmt:message key="label.authoring.basic.add.file" /> </a>
			    
			</div>
						
			</div>
		</div>
	</body>
</lams:html>
