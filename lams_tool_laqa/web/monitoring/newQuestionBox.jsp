<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		
		<script language="JavaScript" type="text/JavaScript">

			function submitMethod() {
				document.QaAuthoringForm.submit();
			}
			
			function submitMethod(actionMethod) {
				document.QaAuthoringForm.dispatch.value=actionMethod; 
				document.QaAuthoringForm.submit();
			}
			
		</script>		
	</lams:head>
	
	<body class="stripes">
		<table cellpadding="0">

			<html:form  action="/monitoring?validate=false" styleId="newQuestionForm" enctype="multipart/form-data" method="POST">				
			<html:hidden property="dispatch" value="addSingleQuestion"/>
			<html:hidden property="toolContentID"/>
			<html:hidden property="currentTab" styleId="currentTab" />
			<html:hidden property="activeModule"/>
			<html:hidden property="httpSessionID"/>								
			<html:hidden property="defaultContentIdStr"/>								
			<html:hidden property="defineLaterInEditMode"/>										
			<html:hidden property="contentFolderID"/>														
			<html:hidden property="editQuestionBoxRequest" value="false"/>																				
			
			<tr>
			<td>
			<table class="innerforms">
		
				<tr>
					<td>
						<div class="field-name">
							<fmt:message key="label.add.new.question"></fmt:message>
						</div>
						<lams:CKEditor id="newQuestion"
							value="${qaGeneralAuthoringDTO.editableQuestionText}"
							contentFolderID="${qaGeneralAuthoringDTO.contentFolderID}"></lams:CKEditor>
					</td>
				</tr>
				
				<tr>
					<td>
					<div class="field-name">
						<html:checkbox property="required" value="1" styleId="required"
							styleClass="noBorder">
						</html:checkbox>
						<label for="required">
							<fmt:message key="label.required.desc" />
						</label>
					</div>
					</td>
				</tr>

				<tr>
					<td>
						<div class="field-name">
							<fmt:message key="label.feedback"></fmt:message>
						</div>
						<lams:STRUTS-textarea property="feedback" rows="3" cols="60"></lams:STRUTS-textarea>							
					</td>
				</tr>
				

				<tr>
					<td align="center" valign="bottom">
						<table>
							<tr>
							<td> &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
								<a href="#" onclick="getElementById('newQuestionForm').submit();" class="button-add-item">
									<fmt:message key="label.save.question" />
								</a>
							</td> 
	
							<td>
								<a href="#" onclick="javascript:window.parent.hideMessage()" class="button">
									<fmt:message key="label.cancel" />
								</a>
							</td> 	
							</tr>					
						</table>
					</td>
				</tr>


			</table>				
			</td>
			</tr>
				
			</html:form>
		</table>

	</body>
</lams:html>