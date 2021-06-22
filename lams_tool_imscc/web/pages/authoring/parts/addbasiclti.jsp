<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<%@ include file="/common/header.jsp"%>
		
	<lams:JSImport src="includes/javascript/commonCartridgeItem.js" relative="true" />
	<script type="text/javascript" src="${lams}includes/javascript/jquery.form.js"></script>
	<script type="text/javascript">
   		function formSubmit(){
   			$("#commonCartridgeItemForm").submit();
   		}
	</script>
</lams:head>
	
<body class="stripes">
	<form:form action="saveOrUpdateItem.do" modelAttribute="commonCartridgeItemForm" method="post" id="commonCartridgeItemForm">
	
		<c:set var="title"><fmt:message key="label.authoring.basic.add.basiclti.tool" /></c:set>
		<lams:Page title="${title}" type="learner">

			<lams:errors/>
			
			<form:hidden path="sessionMapID" />
			<input type="hidden" name="itemType" id="itemType" value="1" />
			<form:hidden path="itemIndex" />
				
			<div class="form-group">
			    <label for="title">
			    	<fmt:message key="label.authoring.basic.resource.title.input"/>
			    </label>
			    <form:input path="title" cssClass="form-control" id="title" tabindex="1"/>
			</div>
		
			<div class="form-group">
			    <label for="url">
			    	<fmt:message key="label.authoring.basic.bascilti.url" />
			    </label>
			    <form:input path="url" cssClass="form-control" id="url" tabindex="1"/>
			</div>
					
			<div class="form-group">
			    <label for="key">
			    	<fmt:message key="label.authoring.basic.bascilti.key" />
			    </label>
			    <form:input path="key" cssClass="form-control" id="key" tabindex="1"/>
			</div>
					
			<div class="form-group">
			    <label for="secret">
			    	<fmt:message key="label.authoring.basic.bascilti.secret" />
			    </label>
			    <form:input path="secret" cssClass="form-control" id="secret" tabindex="1"/>
			</div>
					
			<div class="form-group">
			    <label for="buttonText">
			    	<fmt:message key="label.authoring.basic.bascilti.button.text" />
			    </label>
			    <form:input path="buttonText" cssClass="form-control" id="buttonText" tabindex="1"/>
			</div>
		
			<div class="checkbox">
				<label for="openUrlNewWindow">
					<form:checkbox path="openUrlNewWindow" id="openUrlNewWindow"/>
					<fmt:message key="open.in.new.window" />
				</label>
			</div>
					
			<div class="form-group">
			    <label for="frameHeight">
			    	<fmt:message key="label.authoring.basic.bascilti.iframe.height" />
			    </label>
			    <form:input path="frameHeight" cssClass="form-control" id="frameHeight" tabindex="1"/>
			</div>
					
			<c:if test="${not empty commonCartridgeItemForm.customStr}">
				<div class="form-group">
				    <label for="customStr">
				    	<fmt:message key="label.authoring.basic.bascilti.custom.parameters" />
				    </label>
					<lams:textarea rows="5" name="customStr" class="form-control" id="customStr" tabindex="1"></lams:textarea>
				</div>						
			</c:if>

			<div class="voffset10 pull-right">
				<a href="#nogo" onclick="javascript:self.parent.tb_remove();" class="btn btn-sm btn-default loffset5">
					<fmt:message key="label.cancel" /> 
				</a>
				<a href="#nogo" onclick="javascript:formSubmit();" class="btn btn-sm btn-primary button-add-item">
					<fmt:message key="label.authoring.basic.add.url" /> 
				</a>
			</div>
					
		</lams:Page>

	</form:form>

</body>
</lams:html>
