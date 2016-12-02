<%@ include file="/common/taglibs.jsp"%>

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
	    	<label for="title"><fmt:message key="label.authoring.basic.resource.title.input" /></label>
			<html:text property="title" size="50" tabindex="1" styleId="resourcetitle"/>
	  	</div>	

 		<div class="form-group" id="addfile">
	    	<label for="file"><fmt:message key="label.authoring.basic.resource.file.input" /></label>
	    	<html:file tabindex="3" property="file" styleId="file" style="display:inline"/>
	        <label><html:checkbox property="openUrlNewWindow" tabindex="4" styleId="openUrlNewWindow" ></html:checkbox>&nbsp;
			 <fmt:message key="open.in.new.window" /></label>
	    </div>

		<div class="form-group">
	    	<label for="description"><fmt:message key="label.learning.comment.or.instruction" /></label>
			<html:text property="description" tabindex="5" styleClass="text-area" maxlength="255" size="50" />
	  	</div>	

 		<html:button property="goback" onclick="javascript:cancel()" styleClass="btn btn-xs btn-default" styleId="cancelButton">
			<fmt:message key="button.cancel" />
		</html:button>&nbsp;
		<html:submit styleClass="btn btn-xs btn-default" styleId="submitButton">
		 	<fmt:message key="button.add" />
		</html:submit>
	
	</html:form>
	
	<script type="text/javascript">
		$('#resourceItemForm').submit(submitResourceForm);
		$('#resourceItemForm').validate({
			errorClass: "text-danger loffset5",
			wrapper: "span",
			rules: {
				file: {
			    	required: true,
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

