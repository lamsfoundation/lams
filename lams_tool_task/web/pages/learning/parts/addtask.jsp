<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
    <link rel="stylesheet" href="<lams:LAMSURL/>css/bootstrap5.custom.css">
    <link rel="stylesheet" href="<lams:LAMSURL/>includes/font-awesome6/css/all.css">
    <link rel="stylesheet" href="<lams:LAMSURL/>css/components.css">
    <link rel="stylesheet" href="<lams:LAMSURL/>learning/css/components-learner.css">
	
    <script src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
    <script src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
    <script src="<lams:LAMSURL/>includes/javascript/bootstrap5.bundle.min.js"></script>
    <lams:JSImport src="includes/javascript/common.js" />
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
<body class="component">
    <div class="component-page-wrapperx">

	<form:form action="saveNewTask.do" modelAttribute="taskListItemForm" method="post" id="taskListItemForm">
		<form:hidden path="mode" />
		<form:hidden path="sessionMapID" />

		<div class="card">
			<div class="card-header">
				<fmt:message key="label.learning.new.task.details" />
			</div>
			
			<div class="card-body">
				<lams:errors5/>

				<div class="mb-3">
					<label for="taskTitle">
						<fmt:message key="label.authoring.basic.resource.title.input" />
					</label>
					<form:input id="taskTitle" path="title" cssClass="form-control" />
				</div>
				<div class="mb-3">
					<label for="description">
						<fmt:message key="label.learning.comment.or.instruction" />
					</label>
					<lams:textarea irows="5" id="description" class="form-control" name="description"></lams:textarea>
				</div>

				<div class="mt-3 float-end">
					<lams:ImgButtonWrapper>
						<button onclick="cancel();" class="btn btn-sm btn-secondary btn-disable-on-submit btn-icon-cancel"> 
							<fmt:message key="label.cancel" />
						</button>
						
						<button onclick="saveTask()" class="btn btn-sm btn-secondary btn-disable-on-submit"> 
							<i class="fa fa-plus"></i>
							<fmt:message key="button.add" />
						</button>
					</lams:ImgButtonWrapper>
				</div>
			</div>
		</div>
	</form:form>

	</div>
</body>
</lams:html>
