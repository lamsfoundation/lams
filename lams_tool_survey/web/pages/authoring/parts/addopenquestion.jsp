<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/common/taglibs.jsp"%>

<html>
	<head>
		<%@ include file="/common/header.jsp"%>
		<lams:css style="tabbed" />

		<%-- user for  surveysurveyitem.js --%>
		<script type="text/javascript">
	   var removeInstructionUrl = "<c:url value='/authoring/removeInstruction.do'/>";
       var addInstructionUrl = "<c:url value='/authoring/newInstruction.do'/>";
	</script>
		<script type="text/javascript"
			src="<html:rewrite page='/includes/javascript/surveyitem.js'/>"></script>

	</head>
	<body>

		<!-- Basic Info Form-->

		<%@ include file="/common/messages.jsp"%>
		<html:form action="/authoring/saveOrUpdateItem" method="post"
			styleId="surveyItemForm">
			<c:set var="formBean"
				value="<%=request
										.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
			<html:hidden property="sessionMapID" />
			<html:hidden property="contentFolderID" />
			<html:hidden property="itemIndex" />
			<html:hidden property="itemType" value="3" />

			<h2 class="no-space-left">
				<fmt:message key="label.authoring.basic.add.survey.question" />
			</h2>

			<div class="field-name">
				<fmt:message key="label.question" />
			</div>

			<lams:FCKEditor id="question.description"
				value="${formBean.question.description}"
				contentFolderID="${formBean.contentFolderID}"></lams:FCKEditor>

			<div class="space-top">
				<html:checkbox property="question.optional" styleClass="noBorder"
					styleId="questionOptional">
				</html:checkbox>

				<label for="questionOptional">
					<fmt:message key="label.authoring.basic.question.optional" />
				</label>
			</div>

		</html:form>

		<div class="space-bottom-top">
			<a href="#" onclick="submitSurveyItem()" class="button-add-item">
				<fmt:message key="label.authoring.basic.add.question" /> </a>
			<a href="javascript:;" onclick="cancelSurveyItem()"
				class="button space-left"> <fmt:message key="label.cancel" /> </a>
		</div>

	</body>
</html>
