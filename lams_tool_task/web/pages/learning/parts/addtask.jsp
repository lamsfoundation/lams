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

	<form:form action="saveNewTask.do" modelAttribute="taskListItemForm" method="post" id="taskListItemForm">
		<form:hidden path="mode" />
		<form:hidden path="sessionMapID" />

		<h5>
			<fmt:message key="label.learning.new.task.details" />
		</h5>
		<lams:errors/>

		<div class="mb-3">
			<label for="taskTitle">
			<fmt:message key="label.authoring.basic.resource.title.input" />
			</label>
			<form:input id="taskTitle" path="title" cssClass="form-control" tabindex="1" />
		</div>
		<div class="mb-3">
			<label for="description"><fmt:message key="label.learning.comment.or.instruction" /></label>
			<lams:textarea irows="5" tabindex="2" id="description" class="form-control" name="description"></lams:textarea>
		</div>
	</form:form>

	<div class="mb-3">
		<lams:ImgButtonWrapper>
			<button onclick="cancel();" class="btn btn-sm btn-secondary btn-disable-on-submit"> 
				<fmt:message key="label.cancel" />
			</button>
			<button onclick="saveTask()" class="btn btn-sm btn-secondary btn-disable-on-submit"> 
				<fmt:message key="button.add" />
			</button>
		</lams:ImgButtonWrapper>
	</div>

</body>
</lams:html>
