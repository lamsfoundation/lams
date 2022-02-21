<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<lams:html>
	<lams:head>
		<%@ include file="addheader.jsp"%>
		
		<style type="text/css">
		 	.item-content {
		 		padding: 5px;
		 	}
		 	
		 	.embedded-title {
		 		clear: both;
		 	}
		 	
		 	#preview-panel {
		 		clear: both;
		 	}
		</style>
	
		<script type="text/javascript">
			$(document).ready(function(){
				$('#url').focus()
						 .attr("placeholder","<fmt:message key="label.authoring.basic.resource.url.placeholder" />")
						 .blur(function(){
							 var url = $(this).val();
							 if (url.trim() != '') {
								 url = encodeURIComponent(url);
								 $('#item-content-0 .content-panel > *').empty();
								$.ajax({
								    url: "http://ckeditor.iframe.ly/api/oembed?url=" + url,
								    dataType: "jsonp",
								    cache: true,
								    type: "POST",
								    jsonpCallback: 'iframelyCallback0',
								    contentType: "application/json; charset=utf-8",
								    error: function (xhr, status, error) {
										$('#preview-panel').addClass('hidden');
										
								        console.log("Result: " + status + " " + error + " " + xhr.status + " " + xhr.statusText)
								    }
								});
							 }
						 })
						 // run on open panel in case it is edit
						 .blur();
			});

			// there is no item ID yet, so just use 0
			function iframelyCallback0(response) {
				if (!response || !response.html) {
					$('#preview-panel').addClass('hidden');
					return;
				}

				if (response.title && $('#title').val().trim() == '') {
					$('#title').val(response.title);
				}

				iframelyCallback(0, response);
				$('#preview-panel').removeClass('hidden');
			}
				
	 		$( "#resourceItemForm" ).validate({
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
						url : '<fmt:message key="error.resource.item.invalid.url"/> ',
					},
					title : {
						required : '<fmt:message key="error.resource.item.title.blank"/> '
					}
				}
			});

		</script>
		<lams:JSImport src="includes/javascript/rsrcresourceitem.js" relative="true" />
		<lams:JSImport src="includes/javascript/rsrcembed.js" relative="true" />
	</lams:head>
	<body>

		<!-- Basic Info Form-->
		<div class="panel panel-default add-file">
			<div class="panel-heading panel-title">
				<fmt:message key="label.authoring.basic.add.url" />
			</div>
			<div class="panel-body">

				<lams:errors/>
				<c:set var="csrfToken"><csrf:token/></c:set>
				<form:form action="saveOrUpdateItem.do?${csrfToken}" method="post" modelAttribute="resourceItemForm" id="resourceItemForm">
					<form:hidden path="sessionMapID" />
					<input type="hidden" name="itemType" id="itemType" value="1" />
					<form:hidden path="itemIndex" />
					
					<div class="form-group">
						<label for="url"><fmt:message key="label.authoring.basic.resource.url.input" /></label>:
						<form:input id="url" path="url"  cssClass="form-control"/>
					</div>
					
					<div class="form-group">
				    	<label for="title"><fmt:message key="label.authoring.basic.resource.title.input" /></label>:
						<form:input path="title" id="title" cssClass="form-control" />
				  	</div>	
					
					<div class="form-group">
					   	<label for="instructions"><fmt:message key="label.authoring.basic.instruction" /></label>:
						<lams:CKEditor id="instructions" value="${resourceItemForm.instructions}" contentFolderID="${authoringForm.contentFolderID}"></lams:CKEditor>
				  	</div>	
				  	
				  	<div class="form-group">
						<%@ include file="ratings.jsp"%>	
					</div>
				</form:form>
	
				<div class="voffset5 pull-right">
				    <button onclick="hideResourceItem(); return false;" class="btn btn-default btn-sm btn-disable-on-submit">
						<fmt:message key="label.cancel" /> </button>
					<button onclick="submitResourceItem(); return false;" class="btn btn-default btn-sm btn-disable-on-submit">
						<i class="fa fa-plus"></i>&nbsp;<fmt:message key="label.authoring.basic.add.url" /></button>
				</div>
				
				<div id="preview-panel" class="hidden">
					<h3 class="text-center"><fmt:message key="label.authoring.basic.resource.preview" /></h3>
					<div id="item-content-0" class="item-content">
						<div class="content-panel">
							<h4  class="embedded-title"></h3>
							<h5  class="embedded-description"></h4>
							<div class="embedded-content"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</body>
</lams:html>
