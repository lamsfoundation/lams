<%@ include file="/common/taglibs.jsp"%>

<!-- Add a URL Form-->
<div class="panel panel-default">
	<div class="panel-heading panel-title">
		<fmt:message key="label.learning.new.url" />
	</div>
	<div class="panel-body">
	
	<%@ include file="/common/messages.jsp"%>
	
	<html:form action="/learning/saveOrUpdateItem" method="post" styleId="resourceItemForm" focus="title" enctype="multipart/form-data">
	
		<html:hidden property="itemType" styleId="itemType" />
		<html:hidden property="mode" styleId="mode"/>
		<html:hidden property="sessionMapID"/>
	
		<div class="form-group">
	    	<label for="title"><fmt:message key="label.authoring.basic.resource.title.input" /></label>:
			<html:text property="title" tabindex="1" styleClass="form-control" styleId="resourcetitle"/>
	  	</div>	

		<div class="form-group" id="addurl">
	    	<label for="url"><fmt:message key="label.authoring.basic.resource.url.input" /></label>
			<html:text property="url" styleClass="form-control" tabindex="2" styleId="url"/><br/>&nbsp;
	        <label><html:checkbox property="openUrlNewWindow" tabindex="3" styleId="openUrlNewWindow" ></html:checkbox>&nbsp;
			 <fmt:message key="open.in.new.window" /></label>
	    </div>

		<div class="form-group">
	    	<label for="description"><fmt:message key="label.learning.comment.or.instruction" /></label>
			<html:text property="description" tabindex="5" maxlength="255" styleClass="form-control" />
	  	</div>	

		<div id="buttons" class="pull-right" >
	 		<html:button property="goback" onclick="javascript:cancel()" styleClass="btn btn-sm btn-default" styleId="cancelButton">
				<fmt:message key="button.cancel" />
			</html:button>&nbsp;
			<html:submit styleClass="btn btn-sm btn-default" styleId="submitButton">
			 	<fmt:message key="button.add" />
			</html:submit>
		</div>
	
	</html:form>
	
	<script type="text/javascript">
		$(document).ready(function(){
			$('#url').attr("placeholder","<fmt:message key="label.authoring.basic.resource.url.input.placeholder" />");
			$('#title').focus();
		});		
	
		$('#resourceItemForm').submit(submitResourceForm);
		$('#resourceItemForm').validate({
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
					url : '<fmt:message key="error.resource.item.invalid.url"/> '
				},
				title : {
					required : '<fmt:message key="error.resource.item.title.blank"/> '
				}
			},
		});
	</script>
	
</div>
</div>

