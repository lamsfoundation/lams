<%@ include file="/includes/taglibs.jsp"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<head>
	<title>
		<bean:message key="activity.title" />
	</title>
	<lams:css />
	<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
    <script type="text/javascript" src="${lams}fckeditor/fckeditor.js"></script>
   	<script type="text/javascript" src="${lams}includes/javascript/fckcontroller.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/tabcontroller.js"></script>
	<script type="text/javascript" src="${tool}includes/javascript/xmlrequest.js"></script>
	
	<link href="${lams}css/fckeditor_style.css" rel="stylesheet" type="text/css">
	
	<script type="text/javascript">
     //<![CDATA[
	var imgRoot="${lams}images/";
	var themeName="aqua";
        
	function init(){
		initTabSize(3);
            
//		var tag = document.getElementById("currentTab");
//		if(tag.value != "")
//			selectTab(tag.value);
//        else
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

	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
</head>