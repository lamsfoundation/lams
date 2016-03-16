<!DOCTYPE html>
		

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		<lams:css style="main" />
		<%-- user for  rsrcresourceitem.js --%>
		<script type="text/javascript">
			var removeInstructionUrl = "<c:url value='/authoring/removeInstruction.do'/>";
    		var addInstructionUrl = "<c:url value='/authoring/newInstruction.do'/>";
		</script>
		<script type="text/javascript" src="<html:rewrite page='/includes/javascript/rsrcresourceitem.js'/>"></script>
	</lams:head>
	<body>

		<%@ include file="/common/messages.jsp"%>

		<!-- Basic Info Form-->
		<div class="panel panel-default add-file">
			<div class="panel-heading panel-title">
				<fmt:message key="label.authoring.basic.add.url" />
			</div>
			<div class="panel-body">
			<div class="form-group">

			<form>
			<html:form action="/authoring/saveOrUpdateItem" method="post" styleId="resourceItemForm">
				<html:hidden property="sessionMapID" />
				<input type="hidden" name="instructionList" id="instructionList" />
				<input type="hidden" name="itemType" id="itemType" value="1" />
				<html:hidden property="itemIndex" />

				<div class="form-group">
			    	<label for="title"><fmt:message key="label.authoring.basic.resource.title.input" /></label>
					<html:text property="title" size="55" />
			  	</div>	
			  

			<div class="field-name space-top">
				<fmt:message key="label.authoring.basic.resource.url.input" />
			</div>
			<html:text property="url" size="55" />

			<div class="space-top">
				<html:checkbox property="openUrlNewWindow" styleId="openUrlNewWindow" styleClass="noBorder" />
				<label for="openUrlNewWindow">
					<fmt:message key="open.in.new.window" />
				</label>
			</div>

		</html:form>

		<!-- Instructions -->
		<%@ include file="instructions.jsp"%>
		</form>

		<lams:ImgButtonWrapper>
			<a href="#" onclick="submitResourceItem()" class="button-add-item"><fmt:message
					key="label.authoring.basic.add.url" /> </a>
			<a href="javascript:;" onclick="cancelResourceItem()"
				class="button space-left"><fmt:message key="label.cancel" /> </a>
		</lams:ImgButtonWrapper>
		<br />
	</body>
</lams:html>
