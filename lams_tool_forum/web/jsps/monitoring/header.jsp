<%@ include file="/includes/taglibs.jsp"%>
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
		initTabSize(4);
		selectTab(1);
	}  
        
	function doSelectTab(tabId) {
		var tag = document.getElementById("currentTab");
		tag.value = tabId;
		selectTab(tabId);
	}

	//]]>        
</script>