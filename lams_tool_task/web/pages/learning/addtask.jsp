<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
	</lams:head>
	<body>


		<html:form action="/learning/saveOrUpdateItem" method="post" styleId="taskListItemForm">
			<html:hidden property="mode"/>
			<html:hidden property="sessionMapID"/>

			<div class="field-name-alternative-color space-top">
				<fmt:message key="label.learning.new.task.details" />
			</div>
			<%@ include file="/common/messages.jsp"%>

			<div class="field-name space-top">
				<fmt:message key="label.authoring.basic.resource.title.input" />
			</div>

			<html:text property="title" size="40" tabindex="1" />

			<div class="field-name space-top">
				<fmt:message key="label.learning.comment.or.instruction" />
			</div>

			<html:textarea rows="5" cols="20" tabindex="4" styleClass="text-area"
				property="description" />
				
				
		</html:form>
		
		<lams:ImgButtonWrapper>
			<a href="#" onclick="taskListItemForm.submit();" class="button-add-item">
				<fmt:message key="button.add" /> 
			</a>
			<a href="javascript:;" onclick="window.parent.hideMessage();" class="button space-left">
				<fmt:message key="label.cancel" />
			</a>
		</lams:ImgButtonWrapper>
		
	</body>
</lams:html>
