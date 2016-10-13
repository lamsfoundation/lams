<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		<lams:css style="tabbed" />
		<link href="${lams}css/jquery-ui-redmond-theme.css" rel="stylesheet" type="text/css" >
		
		<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
		<script language="JavaScript" type="text/JavaScript">

			function submitMethod() {
				document.QaAuthoringForm.submit();
			}
			
			function submitMethod(actionMethod) {
				document.QaAuthoringForm.dispatch.value=actionMethod; 
				document.QaAuthoringForm.submit();
			}
			
			$(function() {
				//change size of an iframe on ckeditor's autogrow 
				CKEDITOR.instances.newQuestion.on("instanceReady", function(e) {
				    e.editor.on('resize',function(reEvent){
				    	var iframe = window.parent.document.getElementById("messageArea");
				    	iframe.style.height=eval(iframe.contentWindow.document.body.scrollHeight)+'px';
				    });
				});
				
			 	//spinner
			 	$("#min-words-limit").spinner({ 
			 		min: 0
			 	});
			});
			
		</script>
	</lams:head>

	<body>
		<html:form action="/authoring?validate=false" styleId="newQuestionForm" enctype="multipart/form-data" method="POST">
			<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
			<c:set var="sessionMap" value="${sessionScope[formBean.httpSessionID]}" scope="request"/>

			<html:hidden property="dispatch" value="saveSingleQuestion" />
			<html:hidden property="toolContentID" />
			<html:hidden property="currentTab" styleId="currentTab" />
			<html:hidden property="httpSessionID" />
			<html:hidden property="contentFolderID" />
			<html:hidden property="editableQuestionIndex" />
			<html:hidden property="editQuestionBoxRequest" value="true" />

			<div class="field-name space-top">
				<fmt:message key="label.edit.question"></fmt:message>
			</div>

			<lams:CKEditor id="newQuestion"
				value="${formBean.editableQuestionText}"
				contentFolderID="${formBean.contentFolderID}" width="99%">
			</lams:CKEditor>

			<div class="field-name space-top">
				<html:checkbox property="required" value="1" styleId="required" styleClass="noBorder"/>
				
				<label for="required">
					<fmt:message key="label.required.desc" />
				</label>
			</div>
			
			<div class="field-name small-space-top" >
				<label for="min-words-limit">
					<fmt:message key="label.minimum.number.words" >
						<fmt:param> </fmt:param>
					</fmt:message>
				</label>
				<html:text property="minWordsLimit" styleId="min-words-limit"/>
			</div>

			<div class="field-name small-space-top">
				<fmt:message key="label.feedback"></fmt:message>
			</div>
			<lams:STRUTS-textarea property="feedback" rows="3" cols="75"/>

			<lams:ImgButtonWrapper>
				<a href="#" onclick="getElementById('newQuestionForm').submit();"
					class="button-add-item"> <fmt:message key="label.save.question" />
				</a>
				<a href="#" onclick="javascript:window.parent.hideMessage()"
					class="button space-left"> <fmt:message key="label.cancel" />
				</a>
			</lams:ImgButtonWrapper>
		</html:form>

	</body>
</lams:html>
