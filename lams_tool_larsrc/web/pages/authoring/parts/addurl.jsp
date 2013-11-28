<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		<lams:css style="tabbed" />
		<%-- user for  rsrcresourceitem.js --%>
		<script type="text/javascript">
	   var removeInstructionUrl = "<c:url value='/authoring/removeInstruction.do'/>";
       var addInstructionUrl = "<c:url value='/authoring/newInstruction.do'/>";
	</script>
		<script type="text/javascript"
			src="<html:rewrite page='/includes/javascript/rsrcresourceitem.js'/>"></script>
	</lams:head>
	<body class="tabpart">

		<!-- Basic Info Form-->
		<%@ include file="/common/messages.jsp"%>
		<html:form action="/authoring/saveOrUpdateItem" method="post"
			styleId="resourceItemForm">
			<html:hidden property="sessionMapID" />
			<input type="hidden" name="instructionList" id="instructionList" />
			<input type="hidden" name="itemType" id="itemType" value="1" />
			<html:hidden property="itemIndex" />

			<h2 class="no-space-left">
				<fmt:message key="label.authoring.basic.add.url" />
			</h2>

			<div class="field-name">
				<fmt:message key="label.authoring.basic.resource.title.input" />
			</div>

			<html:text property="title" size="55" />

			<div class="field-name space-top">
				<fmt:message key="label.authoring.basic.resource.url.input" />
			</div>
			<html:text property="url" size="55" />

			<div class="space-top">
				<html:checkbox property="openUrlNewWindow"
					styleId="openUrlNewWindow" styleClass="noBorder">
				</html:checkbox>
				<label for="openUrlNewWindow">
					<fmt:message key="open.in.new.window" />
				</label>
			</div>

		</html:form>

		<!-- Instructions -->
		<%@ include file="instructions.jsp"%>

		<lams:ImgButtonWrapper>
			<a href="#" onclick="submitResourceItem()" class="button-add-item"><fmt:message
					key="label.authoring.basic.add.url" /> </a>
			<a href="javascript:;" onclick="cancelResourceItem()"
				class="button space-left"><fmt:message key="label.cancel" /> </a>
		</lams:ImgButtonWrapper>
		<br />
	</body>
</lams:html>
