<!DOCTYPE html>
		

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		<lams:css style="main" />

		<script type="text/javascript" src="${lams}includes/javascript/prototype.js"></script>
		<%-- user for  surveysurveyitem.js --%>
		<script type="text/javascript">
	   		var removeInstructionUrl = "<c:url value='/authoring/removeInstruction.do'/>";
       		var addInstructionUrl = "<c:url value='/authoring/newInstruction.do'/>";
		</script>
		<script type="text/javascript" src="<html:rewrite page='/includes/javascript/surveyitem.js'/>"></script>
		
		
		<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.js"></script>
		<script type="text/javascript">
			$.noConflict();
			jQuery(function() {
				//change size of an iframe on ckeditor's autogrow 
				CKEDITOR.instances["question.description"].on("instanceReady", function(e) {
				    e.editor.on('resize', function(reEvent){
				    	var iframe = window.parent.document.getElementById("questionInputArea");
				    	iframe.style.height = eval(iframe.contentWindow.document.body.scrollHeight) + 'px';
				    });
				});
			});
		</script>

	</lams:head>
	<body>

		<!-- Basic Info Form-->

		<%@ include file="/common/messages.jsp"%>
		<html:form action="/authoring/saveOrUpdateItem" method="post" styleId="surveyItemForm">
			<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
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

			<lams:CKEditor id="question.description" value="${formBean.question.description}"
				contentFolderID="${formBean.contentFolderID}" width="99%"
				resizeParentFrameName="questionInputArea">
			</lams:CKEditor>

			<div class="space-top">
				<html:checkbox property="question.optional" styleClass="noBorder"
					styleId="questionOptional">
				</html:checkbox>

				<label for="questionOptional">
					<fmt:message key="label.authoring.basic.question.optional" />
				</label>
			</div>

		</html:form>

		<lams:ImgButtonWrapper>
			<a href="#" onclick="submitSurveyItem()" class="button-add-item">
				<fmt:message key="label.authoring.basic.add.question" /> </a>
			<a href="javascript:;" onclick="cancelSurveyItem()"
				class="button space-left"> <fmt:message key="label.cancel" /> </a>
		</lams:ImgButtonWrapper>
		
	</body>
</lams:html>
