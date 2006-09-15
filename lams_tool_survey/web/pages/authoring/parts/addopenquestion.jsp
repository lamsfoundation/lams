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
						<table >
							<tr>
								<td colspan="2">
									<h2>
										<fmt:message key="label.authoring.basic.add.survey.question" />
									</h2>
								</td>
							</tr>
							<tr>
								<td>
									<fmt:message key="label.question" />
								</td>
								<td>
									<html:checkbox property="question.optional" styleClass="noBorder">
										<fmt:message key="label.authoring.basic.question.optional" />
									</html:checkbox>
								</td>								
							</tr>
							<tr>
								<td colspan="2">
									<c:set var="language">
										<lams:user property="localeLanguage" />
									</c:set>
									<fck:editor id="question.description" basePath="/lams/fckeditor/"
										imageBrowserURL="/FCKeditor/editor/filemanager/browser/default/browser.html?Type=Image&amp;Connector=connectors/jsp/connector"
										linkBrowserURL="/FCKeditor/editor/filemanager/browser/default/browser.html?Connector=connectors/jsp/connector"
										flashBrowserURL="/FCKeditor/editor/filemanager/browser/default/browser.html?Type=Flash&amp;Connector=connectors/jsp/connector"
										imageUploadURL="/FCKeditor/editor/filemanager/upload/simpleuploader?Type=Image"
										linkUploadURL="/FCKeditor/editor/filemanager/upload/simpleuploader?Type=File"
										flashUploadURL="/FCKeditor/editor/filemanager/upload/simpleuploader?Type=Flash"
										toolbarSet="Default-Learner" defaultLanguage="${language}"
										autoDetectLanguage="false">
										<c:out value="${formBean.question.description}"
											escapeXml="false" />
									</fck:editor>
								</td>
							</tr>
						</table>
					</html:form>
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
