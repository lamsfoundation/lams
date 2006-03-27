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
   	<script type="text/javascript" src="${tool}includes/javascript/fckcontroller.js"></script>
	<script type="text/javascript" src="${tool}includes/javascript/tabcontroller.js"></script>
	<script type="text/javascript" src="${tool}includes/javascript/xmlrequest.js"></script>
	
	<link href="${tool}css/fckeditor_style.css" rel="stylesheet" type="text/css">
	
	<script type="text/javascript">
     //<![CDATA[
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
//		if(tabId != null)
//			document.forumMonitoringForm.currentTab.value=tabId;
//		document.forumMonitoringForm.method.value=method;
		document.forumMonitoringForm.submit();
	}
	//]]>        
	</script>

	<script type="text/javascript">
	//<![CDATA[
		<html:javascript dynamicJavascript="false" staticJavascript="true"/>
	//]]>		
	</script>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
</head>

