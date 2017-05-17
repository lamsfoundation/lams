<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<lams:html>
	<lams:head>
		<%@ include file="addheader.jsp"%>
		<script type="text/javascript">
			$(document).ready(function(){
				$('#url').attr("placeholder","<fmt:message key="label.authoring.basic.resource.url.placeholder" />");
				$('#title').focus();
			});		
	 		$( "#resourceItemForm" ).validate({
				errorClass: "text-danger",
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
			    	<label for="title"><fmt:message key="label.authoring.basic.resource.title.input" /></label>:
					<html:text property="title" styleId="title" styleClass="form-control" />
			  	</div>	
			  
				<div class="form-group">
					<label for="url"><fmt:message key="label.authoring.basic.resource.url.input" /></label>:
					<html:text styleId="url" property="url"  styleClass="form-control"/>
					<br/>
					
					<html:checkbox property="openUrlNewWindow" styleId="openUrlNewWindow" styleClass="loffset5"/>
					&nbsp;<label for="openUrlNewWindow"><fmt:message key="open.in.new.window" /></label>
					
					<html:checkbox property="allowRating" styleId="allowRating" styleClass="loffset5"/>
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
					<i class="fa fa-plus"></i>&nbsp;<fmt:message key="label.authoring.basic.add.url" /> </button>
				
			</div>
			
			</div>
		</div>
	</body>
</lams:html>
