<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>

<%-- Monitoring Script --%>

<script type="text/javascript">
	var imgRoot="${lams}images/";
	var themeName="aqua";
        
	function init(){
		initTabSize(4);
            
//		var tag = document.getElementById("currentTab");
//		if(tag.value != "")
//			selectTab(tag.value);
//      else
			selectTab(1);
	}  
        
	function doSelectTab(tabId) {
		var tag = document.getElementById("currentTab");
		tag.value = tabId;
		selectTab(tabId);
	}

	function doSubmit(method, tabId) {
		document.monitoringForm.method.value=method;
		document.monitoringForm.submit();
	}
</script>