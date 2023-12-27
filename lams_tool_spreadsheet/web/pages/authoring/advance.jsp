<%@ include file="/common/taglibs.jsp"%>

<!-- Advance Tab Content -->
	
<lams:SimplePanel titleKey="label.spreadsheet.options">
	
	<div class="radio">
		<label for="learnerNotAllowedToSave">
		<input type="radio" name="spreadsheet.learnerAllowedToSave" value="${false}" id="learnerNotAllowedToSave"
			<c:if test="${not spreadsheetForm.spreadsheet.learnerAllowedToSave}">checked="checked"</c:if> 
			onclick="document.forms.spreadsheetForm.isMarkingEnabled.disabled = true;
					 document.forms.spreadsheetForm.isMarkingEnabled.checked = false;"
		/>
			<fmt:message key="label.authoring.advanced.play.only" />
		</label>
	</div>
	
	<div class="radio">
		<label for="learnerAllowedToSave">
		<input type="radio" name="spreadsheet.learnerAllowedToSave" value="${true}" id="learnerAllowedToSave"
			<c:if test="${spreadsheetForm.spreadsheet.learnerAllowedToSave}">checked="checked"</c:if> 
			onclick="document.forms.spreadsheetForm.isMarkingEnabled.disabled = false;"
		/>
			<fmt:message key="label.authoring.advanced.learners.are.allowed.to.save" />
		</label>
	</div>
	
	<div class="checkbox loffset10">
		<label for="isMarkingEnabled">
		<form:checkbox path="spreadsheet.markingEnabled" id="isMarkingEnabled"
			disabled="${not spreadsheetForm.spreadsheet.learnerAllowedToSave}" />
		<fmt:message key="label.authoring.advanced.enable.spreadsheet.marking" />
		</label>
	</div>

</lams:SimplePanel>

<lams:OutcomeAuthor toolContentId="${spreadsheetForm.spreadsheet.contentId}" />

<lams:SimplePanel titleKey="label.activity.completion">
	<div class="checkbox">
		<label for="lockWhenFinished"><form:checkbox path="spreadsheet.lockWhenFinished" id="lockWhenFinished" />
		<fmt:message key="label.authoring.advance.lock.on.finished" /></label>
	</div>
</lams:SimplePanel>

