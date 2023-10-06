<%@ include file="/common/taglibs.jsp"%>

<!-- Add a URL Form-->
<div class="card lcard">
	<div class="card-header">
		<fmt:message key="label.learning.new.url" />
	</div>
	
	<div class="card-body">
		<lams:errors/>

		<form:form action="saveOrUpdateItem.do" method="post" modelAttribute="resourceItemForm" id="resourceItemForm" focus="title">
			<form:hidden path="itemType" id="itemType" />
			<form:hidden path="mode" id="mode"/>
			<form:hidden path="sessionMapID"/>

			<div class="mb-3" id="addurl">
				<label for="url"><fmt:message key="label.authoring.basic.resource.url.input" /></label>
				<form:input path="url" cssClass="form-control" id="url"/>
			</div>

			<div id="preview-panel" class="card lcard d-none">
				<div class="card-header">
					<fmt:message key="label.authoring.basic.resource.preview" />
				</div>
				<div id="item-content-0" class="card-body item-content">
					<div class="content-panel">
						<h3  class="embedded-title"></h3>
						<h4  class="embedded-description"></h4>
						<div class="embedded-content"></div>
					</div>
				</div>
			</div>

			<div class="mb-3">
				<label for="title"><fmt:message key="label.authoring.basic.resource.title.input" /></label>:
				<form:input path="title" cssClass="form-control"/>
			</div>

			<div class="mb-3">
		    	<div id="instructions-label">
		    		<fmt:message key="label.authoring.basic.instruction" />
		    	</div>
				<lams:CKEditor id="instructions-ckeditor" value="" contentFolderID="${learnerContentFolder}"
					ariaLabelledby="instructions-label"></lams:CKEditor>
			</div>

			<div id="buttons" class="float-end" >
				<button type="button" name="goback" onclick="javascript:cancel()" class="btn btn-sm btn-secondary" id="cancelButton">
					<i class="fa fa-cancel"></i>
					<fmt:message key="button.cancel" />
				</button>&nbsp;
				<button type="submit" class="btn btn-sm btn-secondary btn-disable-on-submit" id="submitButton">
					<i class="fa fa-plus"></i>
					<fmt:message key="button.add" />
				</button>
			</div>
		</form:form>

		<script type="text/javascript">
			$(document).ready(function(){
				$('#addresource #url').focus()
				.attr("placeholder","<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.authoring.basic.resource.url.placeholder' /></spring:escapeBody>")
				.blur(function(){
					var url = $(this).val();
					if (url.trim() != '') {
						url = encodeURIComponent(url);
						$('#addresource #item-content-0 .content-panel > *').empty();
						$.ajax({
							url: "https://ckeditor.iframe.ly/api/oembed?url=" + url,
							dataType: "jsonp",
							cache: true,
							type: "POST",
							jsonpCallback: 'iframelyCallback0',
							contentType: "application/json; charset=utf-8",
							error: function (xhr, status, error) {
								$('#addresource #preview-panel').addClass('d-none');

								console.log("Result: " + status + " " + error + " " + xhr.status + " " + xhr.statusText)
							}
						});
					}
				});
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
						required : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="error.resource.item.url.blank"/></spring:escapeBody> ',
						url : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="error.resource.item.invalid.url"/></spring:escapeBody> '
					},
					title : {
						required : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="error.resource.item.title.blank"/></spring:escapeBody> '
					}
				},
			});
		</script>
	</div>
</div>