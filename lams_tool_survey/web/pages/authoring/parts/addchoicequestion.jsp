<%@ include file="/common/taglibs.jsp"%>

	<%-- used for  surveyitem.js --%>
	<script type="text/javascript">
	   var removeInstructionUrl = "<c:url value='/authoring/removeInstruction.do'/>";
       var addInstructionUrl = "<c:url value='/authoring/newInstruction.do'/>";
	</script>
	<script type="text/javascript"
		src="<html:rewrite page='/includes/javascript/surveyitem.js'/>"></script>

		<!-- Basic Info Form-->
		<div class="panel panel-default add-file">
			<div class="panel-heading panel-title">
				<fmt:message key="label.authoring.basic.add.survey.question" />
			</div>
			<div class="panel-body">

			<%@ include file="/common/messages.jsp"%>
	
			<html:form action="/authoring/saveOrUpdateItem" method="post"
				styleId="surveyItemForm">
				<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
				<%-- This field is not belong STRUTS form --%>
				<input type="hidden" name="instructionList" id="instructionList" />
				<html:hidden property="sessionMapID" />
				<html:hidden property="itemIndex" />
				<html:hidden property="contentFolderID" />
				<%-- This value should be 1 or 2 --%>
				<html:hidden property="itemType" value="1" />

				<div class="form-group">
					<label for="question.description"><fmt:message key="label.question" /></label>
					<lams:CKEditor id="question.description" value="${formBean.question.description}"
						contentFolderID="${formBean.contentFolderID}">	
					</lams:CKEditor>
				</div>

				<div class="checkbox">
					<label for="questionOptional">
					<html:checkbox property="question.optional" styleId="questionOptional">
					</html:checkbox>
					<fmt:message key="label.authoring.basic.question.optional" />
					</label>
				</div>

				<div class="voffset5 form-inline">
					<a href="javascript:;" onclick="addInstruction()" class="btn btn-default btn-sm">
						<fmt:message key="label.authoring.basic.add.option" /> </a>
					<i class="fa fa-spinner" style="display: none" id="instructionArea_Busy" />

					<div class="checkbox loffset5"> 
						<label for="question.allowMultipleAnswer">
						<html:checkbox	property="question.allowMultipleAnswer" styleId="questionAllowMultipleAnswer">
						</html:checkbox> 
						<fmt:message key="label.authoring.basic.question.allow.muli.answer" />
						</label> 
					</div>
					
					<div class="checkbox loffset5"> 
						<label for="question.appendText">
						<html:checkbox property="question.appendText" styleId="questionAppendText">
						</html:checkbox> 
						<fmt:message key="label.authoring.basic.question.append.text" />
						</label>
					</div>
				</div>

			</html:form>
		<!-- Instructions -->

		<%@ include file="instructions.jsp"%>
		
		<a href="#" onclick="submitSurveyItem()" class="btn btn-default btn-sm pull-right">
			<i class="fa fa-plus"></i>&nbsp; <fmt:message key="label.authoring.basic.add.question" /> </a>
		<a href="javascript:;" onclick="cancelSurveyItem()"
			class="btn btn-default btn-sm pull-right roffset5"> <fmt:message key="label.cancel" /> </a>
		
		</div>
	</div>
