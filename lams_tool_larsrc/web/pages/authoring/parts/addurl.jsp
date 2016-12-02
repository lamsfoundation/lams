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
	 				url: {
	 			    	required: true,
	 			    	url: true
	 			    },
				    title: {
				    	required: true
				    }
	 			},
				messages : {
					url : {
						required : '<fmt:message key="error.resource.item.url.blank"/> ',
						url : '<fmt:message key="error.resource.item.invalid.url"/> ',
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

		<!-- Basic Info Form-->
		<div class="panel panel-default add-file">
			<div class="panel-heading panel-title">
				<fmt:message key="label.authoring.basic.add.url" />
			</div>
			<div class="panel-body">

			<%@ include file="/common/messages.jsp"%>
	
			<html:form action="/authoring/saveOrUpdateItem" method="post" styleId="resourceItemForm">
				<html:hidden property="sessionMapID" />
				<input type="hidden" name="instructionList" id="instructionList" />
				<input type="hidden" name="itemType" id="itemType" value="1" />
				<html:hidden property="itemIndex" />

				<div class="form-group">
			    	<label for="title"><fmt:message key="label.authoring.basic.resource.title.input" /></label>
					<html:text property="title" size="55" styleClass="form-control form-control-inline" />
			  	</div>	
			  
				<div class="form-group">
					<label for="url"><fmt:message key="label.authoring.basic.resource.url.input" /></label>
					<html:text styleId="url" property="url" size="55" styleClass="form-control form-control-inline"/>
					&nbsp;<html:checkbox property="openUrlNewWindow" styleId="openUrlNewWindow" styleClass="loffset5"/>
					&nbsp;<label for="openUrlNewWindow"><fmt:message key="open.in.new.window" /></label>
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
					<i class="fa fa-plus"></i>&nbsp;<fmt:message key="label.authoring.basic.add.url" /> </a>
				
			</div>
			
			</div>
		</div>
	</body>
</lams:html>
