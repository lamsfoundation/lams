	<c:set var="sessionMap" value="${sessionScope[questionForm.sessionMapID]}" />
	<div class="form-group">
    	<label for="description"><fmt:message key="label.authoring.basic.description" /></label>
		<lams:CKEditor id="description" value="${questionForm.description}" 
			contentFolderID="${sessionMap.contentFolderID}"
	                width="100%"
	                resizeParentFrameName="questionInputArea">
		</lams:CKEditor>
	</div>
  
