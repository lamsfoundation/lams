	<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
	<c:set var="sessionMap" value="${sessionScope[formBean.sessionMapID]}" />
	<div class="form-group">
    	<label for="description"><fmt:message key="label.authoring.basic.description" /></label>
		<lams:CKEditor id="description" value="${formBean.description}" 
			contentFolderID="${sessionMap.contentFolderID}"
	                width="100%"
	                resizeParentFrameName="questionInputArea">
		</lams:CKEditor>
	</div>
  
