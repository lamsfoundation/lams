<%@ include file="/common/taglibs.jsp"%>
<html>
	<head>
		<%@ include file="/common/header.jsp"%>
		<%-- user for  surveysurveyitem.js --%>
		<script type="text/javascript">
	   var removeInstructionUrl = "<c:url value='/authoring/removeInstruction.do'/>";
       var addInstructionUrl = "<c:url value='/authoring/newInstruction.do'/>";
	</script>
		<script type="text/javascript" src="<html:rewrite page='/includes/javascript/surveysurveyitem.js'/>"></script>
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
	hr {
		border: none 0;
		border-top: 1px solid #ccc;
		width: 650px;
		height: 1px;
		margin: 0px 10px 10px 0px;
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
						<html:hidden property="sessionMapID" />
						<html:hidden property="instructionList" styleId="instructionList" />
						<html:hidden property="itemIndex" />
						<table class="innerforms">
							<tr>
								<td colspan="2">
									<h2>
										<fmt:message key="label.authoring.basic.add.question" />
									</h2>
								</td>
							</tr>
							<tr>
								<td>
									<fmt:message key="label.authoring.basic.survey.description.input" />
								</td>
								<td>
									<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
									<c:set var="language"><lams:user property="localeLanguage"/></c:set>
									<fck:editor id="message.body" basePath="/lams/fckeditor/"
										imageBrowserURL="/FCKeditor/editor/filemanager/browser/default/browser.html?Type=Image&amp;Connector=connectors/jsp/connector"
										linkBrowserURL="/FCKeditor/editor/filemanager/browser/default/browser.html?Connector=connectors/jsp/connector"
										flashBrowserURL="/FCKeditor/editor/filemanager/browser/default/browser.html?Type=Flash&amp;Connector=connectors/jsp/connector"
										imageUploadURL="/FCKeditor/editor/filemanager/upload/simpleuploader?Type=Image"
										linkUploadURL="/FCKeditor/editor/filemanager/upload/simpleuploader?Type=File"
										flashUploadURL="/FCKeditor/editor/filemanager/upload/simpleuploader?Type=Flash"
											toolbarSet="Default-Learner" defaultLanguage="${language}" autoDetectLanguage="false">
										<c:out value="${formBean.description}" escapeXml="false" />
									</fck:editor>							
														
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
					 <a href="#" onclick="submitSurveyItem()" class="button-add-item"><fmt:message key="label.authoring.basic.add.question" /></a>  <a href="javascript:;" onclick="cancelSurveyItem()" class="button"><fmt:message key="label.cancel" /></a>
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
