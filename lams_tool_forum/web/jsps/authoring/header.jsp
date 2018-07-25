<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<script type="text/javascript">
     //<![CDATA[
	function init(){
		var tag = document.getElementById("currentTab");
		if(tag == null || tag.value != "")
			selectTab(tag.value);
        else
			selectTab(1);
	}  
        
	function doSelectTab(tabId) {
		var tag = document.getElementById("currentTab");
		tag.value = tabId;
		selectTab(tabId);
	}

	function doSubmit(method) {
		var authorForm = document.getElementById("authoringForm");
		authorForm.action="<c:url value='/authoring/'/>"+method+".do";
		authorForm.submit();
	}
	
	//]]>        
</script>
