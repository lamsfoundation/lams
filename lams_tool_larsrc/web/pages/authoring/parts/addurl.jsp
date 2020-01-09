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
		<script type="text/javascript" src="<lams:WebAppURL/>includes/javascript/rsrcresourceitem.js"></script>
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
				<input type="hidden" name="instructionList" id="instructionList" />
				<input type="hidden" name="itemType" id="itemType" value="1" />
				<form:hidden path="itemIndex" />

				<div class="form-group">
			    	<label for="title"><fmt:message key="label.authoring.basic.resource.title.input" /></label>:
					<form:input path="title" id="title" cssClass="form-control" />
			  	</div>	
			  
				<div class="form-group">
					<label for="url"><fmt:message key="label.authoring.basic.resource.url.input" /></label>:
					<form:input id="url" path="url"  cssClass="form-control"/>
					<br/>
					
					<form:checkbox path="openUrlNewWindow" id="openUrlNewWindow" cssClass="loffset5"/>
					&nbsp;<label for="openUrlNewWindow"><fmt:message key="open.in.new.window" /></label>
					<br/>
					
					<%@ include file="ratings.jsp"%>	
				</div>

			</form:form>

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
