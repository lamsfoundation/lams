<%@ include file="/common/taglibs.jsp"%>


<html>
	<head>
		<%@ include file="/common/header.jsp"%>
		<%-- user for  surveysurveyitem.js --%>
		<script type="text/javascript">
	   var removeInstructionUrl = "<c:url value='/authoring/removeInstruction.do'/>";
       var addInstructionUrl = "<c:url value='/authoring/newInstruction.do'/>";
	</script>
		<script type="text/javascript"
			src="<html:rewrite page='/includes/javascript/surveyitem.js'/>"></script>
		<style type="text/css">
	<!--
	table { 
		 width:650px;
		 margin-left:0px; 
		 text-align:left; 
		 }
	
	td { 
		padding:4px; 
		font-size:12px;
	}
		
	-->
	</style>
	</head>
	<body class="tabpart">
		<table class="forms" border="0">
			<!-- Basic Info Form-->
			<tr>
				<td>
					<%@ include file="/common/messages.jsp"%>
					<html:form action="/authoring/saveOrUpdateItem" method="post" styleId="surveyItemForm">
						<c:set var="formBean"	value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
						<%-- This field is not belong STRUTS form --%>
						<input type="hidden" name="instructionList" id="instructionList" />
						<html:hidden property="sessionMapID" />
						<html:hidden property="itemIndex" />
						<html:hidden property="contentFolderID" />
						<%-- This value should be 1 or 2 --%>
						<html:hidden property="itemType" value="1" />
						<table >
							<tr>
								<td colspan="3">
									<h2>
										<fmt:message key="label.authoring.basic.add.survey.question" />
									</h2>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<fmt:message key="label.question" />
								</td>
								<td>
									<html:checkbox property="question.optional" styleClass="noBorder">
										<fmt:message key="label.authoring.basic.question.optional" />
									</html:checkbox>
								</td>								
							</tr>
							<tr>
								<td colspan="3">
									<lams:FCKEditor id="question.description"
										value="${formBean.question.description}"
										contentFolderID="${formBean.contentFolderID}"></lams:FCKEditor>
								</td>
							</tr>
							<tr>
								<td>
									<a href="javascript:;" onclick="addInstruction()" class="button"><fmt:message key="label.authoring.basic.add.option"/></a>
									<img src="${ctxPath}/includes/images/indicator.gif" style="display:none" id="instructionArea_Busy" />
								</td>
								<td>
									<html:checkbox property="question.allowMultipleAnswer" styleClass="noBorder">
										<fmt:message key="label.authoring.basic.question.allow.muli.answer" />
									</html:checkbox>
								</td>				
								<td>
									<html:checkbox property="question.appendText" styleClass="noBorder">
										<fmt:message key="label.authoring.basic.question.append.text" />
									</html:checkbox>
								</td>
							</tr>			
						</table>
					</html:form>
				</td>
			</tr>
			<tr>
				<!-- Instructions -->
				<td>
					<%@ include file="instructions.jsp"%>
				</td>
			</tr>

			<tr>
				<td align="center" valign="bottom">
					<a href="#" onclick="submitSurveyItem()" class="button-add-item">
						<fmt:message key="label.authoring.basic.add.question" /> 
					</a> 
					&nbsp;&nbsp;
					<a href="javascript:;" onclick="cancelSurveyItem()" class="button">
						<fmt:message key="label.cancel" /> 
					</a>
				</td>
			</tr>
			<tr>
				<td>
					&nbsp;
				</td>
			</tr>
		</table>
	</body>
</html>
