<!DOCTYPE html>
		

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
	<lams:head>
		<%@ include file="addheader.jsp"%>
		<script type="text/javascript">
	 		$( "#resourceItemForm" ).validate({
				errorClass: "text-danger loffset5",
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
				   	<label for="title"><fmt:message key="label.authoring.basic.resource.title.input" /></label>
					<html:text property="title" size="55" styleClass="form-control form-control-inline" />
			  	</div>	
			  

				<div class="form-group">
					<label for="file"><fmt:message key="label.authoring.basic.resource.file.input" /></label>
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
				<a href="#" onclick="submitResourceItem()" class="btn btn-default btn-sm">
					<i class="fa fa-plus"></i>&nbsp;<fmt:message key="label.authoring.basic.add.file" /> </a>
			    
			</div>
						
			</div>
		</div>
	</body>
</lams:html>
