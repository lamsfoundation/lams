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
								<td colspan="3">
									<h2>
										<fmt:message key="label.authoring.basic.add.survey" />
									</h2>
								</td>
							</tr>
							<tr>
								<td>
									<html:checkbox property="question.compulsory" styleClass="noBorder">
										<fmt:message key="label.authoring.basic.question.compulsory" />
									</html:checkbox>
								</td>
								<td>
									<html:checkbox property="question.appendText" styleClass="noBorder">
										<fmt:message key="label.authoring.basic.question.append.text" />
									</html:checkbox>
								</td>
								<td>
									<fmt:message key="label.authoring.basic.question.max.answer" />
									<html:select property="question.maxAnswers"
										styleClass="noBorder" styleId="maxAnswerSelect">
										<c:forEach begin="1" end="${fn:length(instructionList)}"
											varStatus="status">
											<c:choose>
												<c:when
													test="${formBean.question.maxAnswers== status.index}">
													<option value="${status.index}" selected="true">
														${status.index}
													</option>
												</c:when>
												<c:otherwise>
													<option value="${status.index}">
														${status.index}
													</option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</html:select>
								</td>
							</tr>
							<tr>
								<td colspan="3">
									<hr size="1" style="width:630px;border-top: 1px solid #ccc;margin: 0px 10px 10px 0px;"/>
									<fmt:message key="label.description" />
								</td>
							</tr>
							<tr>
								<td colspan="3">
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
<script type="text/javascript">
	changeMaxAnswerOptions(${formBean.question.maxAnswers});
</script>		
	</body>
</html>
