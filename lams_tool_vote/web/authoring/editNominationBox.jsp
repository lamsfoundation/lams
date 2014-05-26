<%@ include file="/common/taglibs.jsp"%>
<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		<lams:css style="tabbed" />
	</lams:head>

	<body>

		<html:form action="/authoring?validate=false"
			styleId="newNominationForm" enctype="multipart/form-data"
			method="POST">

			<html:hidden property="dispatch" value="saveSingleNomination" />
			<html:hidden property="toolContentID" />
			<html:hidden property="currentTab" styleId="currentTab" />
			<html:hidden property="httpSessionID" />
			<html:hidden property="contentFolderID" />
			<html:hidden property="editableNominationIndex" />
			<html:hidden property="editNominationBoxRequest" value="true" />


			<div class="field-name">
				<fmt:message key="label.edit.nomination"></fmt:message>
			</div>

			<lams:CKEditor id="newNomination"
				value="${voteGeneralAuthoringDTO.editableNominationText}"   
                height="370px"
				contentFolderID="${voteGeneralAuthoringDTO.contentFolderID}"
				resizeParentFrameName="messageArea">
			</lams:CKEditor>

			
			<lams:ImgButtonWrapper>
				<a href="#" onclick="getElementById('newNominationForm').submit();"
					class="button-add-item"> <fmt:message
						key="label.save.nomination" /> </a>

				<a href="#" onclick="javascript:window.parent.hideMessage()"
					class="button space-left"> <fmt:message key="label.cancel" /> </a>
			</lams:ImgButtonWrapper>
			
		</html:form>


	</body>
</lams:html>
