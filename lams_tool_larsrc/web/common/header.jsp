<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request"/>

<head>
	<title>
		<bean:message key="appName" />
	</title>
	<lams:css />

	<!-- HTTP 1.1 -->
	<meta http-equiv="Cache-Control" content="no-store"/>
	<!-- HTTP 1.0 -->
	<meta http-equiv="Pragma" content="no-cache"/>
	<!-- Prevents caching at the Proxy Server -->
	<meta http-equiv="Expires" content="0"/>
	
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/> 

	<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
	<script type="text/javascript" src="${tool}includes/javascript/tabcontroller.js"></script>
	<script type="text/javascript" src="${tool}includes/javascript/xmlrequest.js"></script>
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
</head>

