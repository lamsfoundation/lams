<%@ include file="/common/taglibs.jsp"%>

	<%-- used for  surveyitem.js --%>
	<script type="text/javascript">
	   var removeInstructionUrl = "<c:url value='/authoring/removeInstruction.do'/>";
       var addInstructionUrl = "<c:url value='/authoring/newInstruction.do'/>";
	</script>
	<script type="text/javascript"
		src="<lams:WebAppURL/>includes/javascript/surveyitem.js"></script>

		<!-- Basic Info Form-->
		<div class="panel panel-default add-file">
			<div class="panel-heading panel-title">
				<fmt:message key="label.authoring.basic.add.survey.question" />
			</div>
			<div class="panel-body">

			
	
			<form:form action="saveOrUpdateItem.do" modelAttribute="surveyItemForm" id="surveyItemForm" method="post">
				<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
			
			<lams:errors/>
				<%-- This field is not belong STRUTS form --%>
				<input type="hidden" name="instructionList" id="instructionList" />
				<form:hidden path="sessionMapID" />
				<form:hidden path="itemIndex" />
				<form:hidden path="contentFolderID" />
				<%-- This value should be 1 or 2 --%>
				<form:hidden path="itemType" value="1" />

				<div class="form-group">
					<label for="question.description"><fmt:message key="label.question" /></label>
					<lams:CKEditor id="question.description" value="${surveyItemForm.question.description}"
						contentFolderID="${surveyItemForm.contentFolderID}">	
					</lams:CKEditor>
				</div>

				<div class="checkbox">
					<label for="questionOptional">
					<form:checkbox path="question.optional" id="questionOptional"/>
					<fmt:message key="label.authoring.basic.question.optional" />
					</label>
				</div>

				<div class="voffset5 form-inline">
					<a href="javascript:;" onclick="addInstruction()" class="btn btn-default btn-sm">
						<fmt:message key="label.authoring.basic.add.option" /> </a>
					<i class="fa fa-spinner" style="display: none" id="instructionArea_Busy" />

					<div class="checkbox loffset5"> 
						<label for="question.allowMultipleAnswer">
						<form:checkbox	path="question.allowMultipleAnswer" id="questionAllowMultipleAnswer"/>
						<fmt:message key="label.authoring.basic.question.allow.muli.answer" />
						</label> 
					</div>
					
					<div class="checkbox loffset5"> 
						<label for="question.appendText">
						<form:checkbox path="question.appendText" id="questionAppendText"/>
						<fmt:message key="label.authoring.basic.question.append.text" />
						</label>
					</div>
				</div>

			</form:form>
		<!-- Instructions -->

		<%@ include file="instructions.jsp"%>
		
		<a href="#" onclick="submitSurveyItem()" class="btn btn-default btn-sm pull-right">
			<i class="fa fa-plus"></i>&nbsp; <fmt:message key="label.authoring.basic.add.question" /> </a>
		<a href="javascript:;" onclick="cancelSurveyItem()"
			class="btn btn-default btn-sm pull-right roffset5"> <fmt:message key="label.cancel" /> </a>
		
		</div>
	</div>
