<%@ include file="/common/taglibs.jsp"%>
<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		<lams:JSImport src="includes/javascript/authoring.js" relative="true" />
	</lams:head>

	<body>

		<div class="panel panel-default add-file">
		<div class="panel-heading panel-title">
			<fmt:message key="label.edit.nomination"></fmt:message>
		</div>
		
		<div class="panel-body">

		<form:form action="saveSingleNomination.do" modelAttribute="voteAuthoringForm" id="newNominationForm" method="POST">
			<form:hidden path="toolContentID" />
			<form:hidden path="currentTab" />
			<form:hidden path="httpSessionID" />
			<form:hidden path="contentFolderID" />
			<form:hidden path="editableNominationIndex" />
			<input type="hidden" name="editNominationBoxRequest" value="true" />

			<div class="form-group">
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
					
		</form:form>
		</div>
		</div>

	</body>
</lams:html>