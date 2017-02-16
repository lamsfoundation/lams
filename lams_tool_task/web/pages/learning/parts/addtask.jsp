<!DOCTYPE html>
		

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<%@ include file="/common/header.jsp"%>
	
	<script type="text/javascript">

		function disableButtons() {
			$('.btn-disable-on-submit').prop('disabled', true);
		}
	
		function saveTask() {
			disableButtons();
			try { window.parent.disableButtons(); } catch(err) {}
			taskListItemForm.submit();
		}
		
		function cancel() {
			 window.parent.hideMessage();
		}
		
	</script>		
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
			<button onclick="cancel();" class="btn btn-sm btn-default btn-disable-on-submit"> <fmt:message
					key="label.cancel" />
			</button>
			<button onclick="saveTask()" class="btn btn-sm btn-default btn-disable-on-submit"> <fmt:message key="button.add" />
			</button>
		</lams:ImgButtonWrapper>
	</div>

</body>
</lams:html>
