<%@ include file="/common/taglibs.jsp"%>
<%-- user for  surveysurveyitem.js --%>
<script type="text/javascript">
  		var removeInstructionUrl = "<c:url value='/authoring/removeInstruction.do'/>";
     		var addInstructionUrl = "<c:url value='/authoring/newInstruction.do'/>";
</script>
<script type="text/javascript" src="<html:rewrite page='/includes/javascript/surveyitem.js'/>"></script>

	<div class="panel panel-default add-file">
		<div class="panel-heading panel-title">
			<fmt:message key="label.authoring.basic.add.survey.question" />
		</div>
		<div class="panel-body">

		<%@ include file="/common/messages.jsp"%>

		<html:form action="/authoring/saveOrUpdateItem" method="post" styleId="surveyItemForm">
			<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
			<html:hidden property="sessionMapID" />
			<html:hidden property="contentFolderID" />
			<html:hidden property="itemIndex" />
			<html:hidden property="itemType" value="3" />

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

		</html:form>
        <a href="#" onclick="submitSurveyItem()" class="btn btn-default btn-sm pull-right">
			<fmt:message key="label.authoring.basic.add.question" /> </a>
        <a href="javascript:;" onclick="cancelSurveyItem()"
			class="btn btn-default btn-sm pull-right roffset5"> <fmt:message key="label.cancel" /> </a>
		
		
		
		</div>
	</div>
