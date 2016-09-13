<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		<script type="text/javascript" src="${tool}includes/javascript/authoring.js"></script>
	</lams:head>

	<body>
	
		<div class="panel panel-default add-file">
		<div class="panel-heading panel-title">
			<fmt:message key="label.add.new.nomination"></fmt:message>
		</div>
		
		<div class="panel-body">

		<html:form action="/authoring?validate=false" styleId="newNominationForm" enctype="multipart/form-data" method="POST">
			<html:hidden property="dispatch" value="addSingleNomination" />
			<html:hidden property="toolContentID" />
			<html:hidden property="currentTab" styleId="currentTab" />
			<html:hidden property="httpSessionID" />
			<html:hidden property="contentFolderID" />
			<html:hidden property="editNominationBoxRequest" value="false" />

			<div class='form-group'>
				<lams:CKEditor id="newNomination"
					value="${voteGeneralAuthoringDTO.editableNominationText}"
					contentFolderID="${voteGeneralAuthoringDTO.contentFolderID}">
				</lams:CKEditor>
			</div>
			
			<div class="voffset5 pull-right">
			    <a href="#" onclick="javascript:hideMessage()"
				class="btn btn-default btn-sm"> <fmt:message key="label.cancel" /></a>
				<a href="#" onclick="javascript:submitNomination()" class="btn btn-default btn-sm">
					<i class="fa fa-plus"></i>&nbsp; <fmt:message key="label.save.nomination" /> </a>
				
			</div>
			
		</html:form>
		
		</div>
		</div>
		
	</body>
</lams:html>
