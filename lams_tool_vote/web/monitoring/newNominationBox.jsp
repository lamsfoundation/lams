<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		<lams:css style="tabbed" />
		<script language="JavaScript" type="text/JavaScript">

			function submitMethod() {
				document.VoteAuthoringForm.submit();
			}
			
			function submitMethod(actionMethod) {
				document.VoteAuthoringForm.dispatch.value=actionMethod; 
				document.VoteAuthoringForm.submit();
			}
			
		</script>		
	</lams:head>
	
	<body>
			<html:form  action="/monitoring?validate=false" styleId="newNominationForm" enctype="multipart/form-data" method="POST">				
			<html:hidden property="dispatch" value="addSingleNomination"/>
			<html:hidden property="toolContentID"/>
			<html:hidden property="currentTab" styleId="currentTab" />
			<html:hidden property="activeModule"/>
			<html:hidden property="httpSessionID"/>								
			<html:hidden property="defaultContentIdStr"/>								
			<html:hidden property="defineLaterInEditMode"/>										
			<html:hidden property="contentFolderID"/>														
			<html:hidden property="editNominationBoxRequest" value="false"/>																				
			

			<div class="field-name">
				<fmt:message key="label.add.new.nomination"></fmt:message>
			</div>
			<lams:CKEditor id="newNomination"
				value="${voteGeneralAuthoringDTO.editableNominationText}"
				contentFolderID="${voteGeneralAuthoringDTO.contentFolderID}"
				height="370px"
				resizeParentFrameName="messageArea">
			</lams:CKEditor>

			
			<lams:ImgButtonWrapper>
				<a href="#" onclick="getElementById('newNominationForm').submit();" class="button-add-item">
					<fmt:message key="label.save.nomination" />
				</a>
				<a href="#" onclick="javascript:window.parent.hideMessage()" class="button space-left">
					<fmt:message key="label.cancel" />
				</a>
			</lams:ImgButtonWrapper>
			</html:form>
	</body>
</lams:html>
