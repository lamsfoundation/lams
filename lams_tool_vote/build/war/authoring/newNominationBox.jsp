<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		<lams:css style="main" />
		
		<script type="text/javascript">			
			$(function() {
				//change size of an iframe on ckeditor's autogrow 
				CKEDITOR.instances.newNomination.on("instanceReady", function(e) {
				    e.editor.on('resize', function(reEvent){
				    	var iframe = window.parent.document.getElementById("messageArea");
				    	iframe.style.height = eval(iframe.contentWindow.document.body.scrollHeight) + 'px';
				    });
				});
			});
		</script>
	</lams:head>

	<body>
		<html:form action="/authoring?validate=false" styleId="newNominationForm" enctype="multipart/form-data" method="POST">
			<html:hidden property="dispatch" value="addSingleNomination" />
			<html:hidden property="toolContentID" />
			<html:hidden property="currentTab" styleId="currentTab" />
			<html:hidden property="httpSessionID" />
			<html:hidden property="contentFolderID" />
			<html:hidden property="editNominationBoxRequest" value="false" />

			<div class="field-name">
				<fmt:message key="label.add.new.nomination"></fmt:message>
			</div>
			<lams:CKEditor id="newNomination"
				value="${voteGeneralAuthoringDTO.editableNominationText}"
				contentFolderID="${voteGeneralAuthoringDTO.contentFolderID}"
                height="370px" width="99%"
				resizeParentFrameName="messageArea">
			</lams:CKEditor>
			
			<lams:ImgButtonWrapper>
				<a href="#" onclick="document.getElementById('newNominationForm').submit();"
					class="button-add-item"> <fmt:message
						key="label.save.nomination" /> </a>

				<a href="#" onclick="javascript:window.parent.hideMessage()"
					class="button space-left"> <fmt:message key="label.cancel" />
				</a>
			</lams:ImgButtonWrapper>

		</html:form>
	</body>
</lams:html>
