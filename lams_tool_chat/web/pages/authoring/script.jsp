<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>

<%-- Authoring Script --%>

<script type="text/javascript">
	var imgRoot="${lams}images/";
	var themeName="aqua";
        
	function init(){

		// initialising tabs
		initTabSize(3);
	
        // open the current tab    
		var tag = document.getElementById("currentTab");
		if(tag.value != "")
			selectTab(tag.value);
     	else
			selectTab(1);
			
		// initialising fckeditor
		initEditor("Title");
		initEditor("Instructions");
		initEditor("OnlineInstruction");
		initEditor("OfflineInstruction");
	}  
        
	function doSelectTab(tabId) {
		var tag = document.getElementById("currentTab");
		tag.value = tabId;
		selectTab(tabId);
	}

	function doSubmit(method) {
		document.authoringForm.dispatch.value=method;
		document.authoringForm.submit();
	}
	
	function deleteAttachment(dispatch, uuid) {
		document.authoringForm.dispatch.value=dispatch;
		document.authoringForm.deleteFileUuid.value=uuid;
		document.authoringForm.submit();	
	}
	
</script>