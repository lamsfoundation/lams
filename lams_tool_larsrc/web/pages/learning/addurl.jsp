<%@ include file="/common/taglibs.jsp"%>

<!-- Add a URL Form-->
<div class="panel panel-default">
	<div class="panel-heading panel-title">
		<fmt:message key="label.learning.new.url" />
	</div>
	<div class="panel-body">
	
	<lams:errors/>
	
	<form:form action="saveOrUpdateItem.do" method="post" modelAttribute="resourceItemForm" id="resourceItemForm" focus="title">
	
		<form:hidden path="itemType" id="itemType" />
		<form:hidden path="mode" id="mode"/>
		<form:hidden path="sessionMapID"/>
		
		<div class="form-group" id="addurl">
	    	<label for="url"><fmt:message key="label.authoring.basic.resource.url.input" /></label>
			<form:input path="url" cssClass="form-control" tabindex="2" id="url"/>
	    </div>
	    
		<div class="form-group">
	    	<label for="title"><fmt:message key="label.authoring.basic.resource.title.input" /></label>:
			<form:input path="title" tabindex="1" cssClass="form-control" id="resourcetitle"/>
	  	</div>	

		<div class="form-group">
	    	<label for="instructions"><fmt:message key="label.authoring.basic.instruction" /></label>
			<lams:CKEditor id="instructions" value="" contentFolderID="${learnerContentFolder}"></lams:CKEditor>
	  	</div>	

		<div id="buttons" class="pull-right" >
	 		<button name="goback" onclick="javascript:cancel()" class="btn btn-sm btn-default" id="cancelButton">
				<fmt:message key="button.cancel" />
			</button>&nbsp;
			<button type="submit" class="btn btn-sm btn-default btn-disable-on-submit" id="submitButton">
			 	<fmt:message key="button.add" />
			</button>
		</div>
	
	</form:form>
	
	<script type="text/javascript">
		$(document).ready(function(){
			$('#url').focus().attr("placeholder","<fmt:message key="label.authoring.basic.resource.url.placeholder" />");
		});		
	
		$('#resourceItemForm').submit(submitResourceForm);
		$('#resourceItemForm').validate({
			ignore: 'hidden, div.cke_editable',
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