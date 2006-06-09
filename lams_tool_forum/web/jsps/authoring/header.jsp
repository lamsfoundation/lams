<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>
<script type="text/javascript">
     //<![CDATA[
	var imgRoot="${lams}images/";
	var themeName="aqua";
        
	function init(){
		initTabSize(3);
            
		var tag = document.getElementById("currentTab");
		if(tag == null || tag.value != "")
			selectTab(tag.value);
        else
			selectTab(1);

	   initEditor("forum.title");
	   initEditor("forum.instructions");
	   initEditor("forum.onlineInstructions");
	   initEditor("forum.offlineInstructions");
	}  
        
	function doSelectTab(tabId) {
		var tag = document.getElementById("currentTab");
		tag.value = tabId;
		selectTab(tabId);
	}

	function doSubmit(method, tabId) {
		var authorForm = document.getElementById("authoringForm");
		if(tabId != null)
			authorForm.currentTab.value=tabId;
		authorForm.action=method;
		authorForm.submit();
	}
	
	//]]>        
</script>
