<!DOCTYPE html>
		

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<%@ include file="/common/header.jsp"%>
</lams:head>
<body>


	<html:form action="/learning/saveNewTask" method="post" styleId="taskListItemForm">
		<html:hidden property="mode" />
		<html:hidden property="sessionMapID" />

		<h5>
			<fmt:message key="label.learning.new.task.details" />
		</h5>
		<%@ include file="/common/messages.jsp"%>

		<div class="form-group">
			<label for="taskTitle"><fmt:message key="label.authoring.basic.resource.title.input" /></label>
			<html:text styleId="taskTitle" property="title" styleClass="form-control" tabindex="1" />
		</div>
		<div class="form-group">
			<label for="description"><fmt:message key="label.learning.comment.or.instruction" /></label>
			<lams:STRUTS-textarea rows="5" tabindex="2" styleId="description" styleClass="form-control" property="description" />
		</div>
	</html:form>

	<div class="form-group">
		<lams:ImgButtonWrapper>
			<a href="javascript:;" onclick="window.parent.hideMessage();" class="btn btn-sm btn-default"> <fmt:message
					key="label.cancel" />
			</a>
			<a href="#" onclick="taskListItemForm.submit();" class="btn btn-sm btn-default"> <fmt:message key="button.add" />
			</a>
		</lams:ImgButtonWrapper>
	</div>

</body>
</lams:html>
